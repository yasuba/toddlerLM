import cats.effect.*
import cats.implicits.*
import ciris.*
import client.{LlmClient, SparkClient}
import com.comcast.ip4s.{Hostname, Port}
import fs2.io.net.Network
import org.http4s.CacheDirective.`max-age`
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.headers.`Cache-Control`
import org.http4s.{Response, Status, Uri}
import org.typelevel.log4cats.SelfAwareStructuredLogger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import routing.ServiceRoutes
import server.ServiceConfig
import service.SentimentService

import scala.concurrent.duration.{Duration, DurationInt}

object Main extends IOApp {
  given logger: SelfAwareStructuredLogger[IO] = Slf4jLogger.getLogger[IO]

  given uriConfigDecoder: ConfigDecoder[String, Uri] =
    ConfigDecoder[String].mapEither { (key, value) =>
      Uri.fromString(value).leftMap { parseFailure =>
        ConfigError(s"Invalid URI for key '$key': ${parseFailure.details}")
      }
    }

  private val serviceConfig: ConfigValue[Effect, ServiceConfig] = (
    env("OPENAI_URI").as[Uri],
    env("OPENAI_API_KEY").as[String]
  ).parMapN { case (uri, apiKey) =>
    ServiceConfig(
      llmUrl = uri,
      llmToken = apiKey
    )
  }

  override def run(args: List[String]): IO[ExitCode] = {

    val sentimentService = for {
      config     <- Resource.eval(serviceConfig.load[IO])
      httpClient <- EmberClientBuilder.default[IO].build
      llmClient   = LlmClient[IO](httpClient, config)
      sparkClient = SparkClient[IO]()
    } yield SentimentService(sparkClient, llmClient)

    sentimentService.use { sentiment =>
      EmberServerBuilder
        .default[IO](Async[IO], Network.forAsync[IO])
        .withPort(Port.fromInt(8080).get)
        .withHost(Hostname.fromString("0.0.0.0").get)
        .withHttpApp(ServiceRoutes.apply[IO](sentiment).orNotFound)
        .withIdleTimeout(Duration.Inf)
        .withRequestHeaderReceiveTimeout(Duration.Inf)
        .withErrorHandler { (unhandled: Throwable) =>
          for {
            _ <- logger.error(unhandled)(
                   s"An unhandled error occurred that wasn't caught in the route it was thrown from: ${unhandled.getMessage}"
                 )
          } yield Response[IO](status = Status.InternalServerError)
            .withHeaders(`Cache-Control`(`max-age`(10.seconds)))
        }
        .build
        .use(_ => IO.never)
        .as(ExitCode.Success)
    }
  }
}
