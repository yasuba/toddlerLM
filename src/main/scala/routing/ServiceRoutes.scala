package routing

import cats.effect.kernel.Async
import cats.syntax.all._
import model.Codecs._
import org.http4s.circe.CirceEntityEncoder._
import org.http4s.dsl.Http4sDsl
import org.http4s.HttpRoutes
import service.SentimentService

object ServiceRoutes {

  def apply[F[_]: Async](sentimentService: SentimentService[F]): HttpRoutes[F] = {

    object dsl extends Http4sDsl[F]
    import dsl._

    HttpRoutes.of[F] { case request @ POST -> Root =>
      request.as[String].flatMap { sentiment =>
        sentimentService.classify(sentiment).flatMap {
          case Some(result) =>
            Ok(result)
          case None         =>
            NotFound()
        }
      }
    }
  }
}
