package routing

import cats.effect.kernel.Async
import cats.syntax.all.*
import org.http4s.dsl.Http4sDsl
import org.http4s.{HttpRoutes, Response, Status}
import org.http4s.circe.CirceEntityEncoder.*
import model.Codecs.given_Encoder_ClassificationResult
import service.SentimentService

object ServiceRoutes {

  def apply[F[_]: Async](sentimentService: SentimentService[F]): HttpRoutes[F] = {

    object dsl extends Http4sDsl[F]
    import dsl.*

    HttpRoutes.of[F] { case request @ POST -> Root =>
      request.as[String].flatMap { sentiment =>
        sentimentService.classify(sentiment).flatMap { result =>
          Status.Ok(result)
        }
      }
    }
  }
}
