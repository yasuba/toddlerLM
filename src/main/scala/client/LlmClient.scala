package client

import cats.MonadThrow
import cats.effect.Concurrent
import cats.implicits._
import model.ClassificationResult
import model.Codecs._
import org.http4s.circe.{jsonEncoderOf, jsonOf}
import org.http4s.client.Client
import org.http4s.headers.Authorization
import org.http4s.{AuthScheme, Credentials, EntityDecoder, EntityEncoder, Method, Request}
import org.typelevel.log4cats.SelfAwareStructuredLogger
import server.ServiceConfig

trait LlmClient[F[_]] {
  def classify(text: String): F[Option[ClassificationResult]]
}

object LlmClient {

  case class ChatMessage(role: String, content: String)

  case class ChatRequest(
    model: String,
    messages: List[ChatMessage],
    temperature: Double = 0
  )

  case class ChatChoice(message: ChatMessage)

  case class ChatResponse(choices: List[ChatChoice])

  def apply[F[_]: Concurrent](
    client: Client[F],
    config: ServiceConfig
  )(implicit logger: SelfAwareStructuredLogger[F], F: MonadThrow[F]): LlmClient[F] =
    new LlmClient[F] {

      implicit val entityECr: EntityEncoder[F, ChatRequest]   = jsonEncoderOf[F, ChatRequest]
      implicit val entityyDCr: EntityDecoder[F, ChatResponse] = jsonOf[F, ChatResponse]

      def classify(text: String): F[Option[ClassificationResult]] = {
        val prompt =
          s"""Classify the toddler sentiment as strictly "happy" or "sad".
             |Text: "$text"
             |Return only the single word.""".stripMargin

        val body    = ChatRequest(
          model = "gpt-4o-mini", // or any cheap model
          messages = List(ChatMessage("user", prompt))
        )
        val request = Request[F](
          method = Method.POST,
          uri = config.llmUrl
        )
          .withHeaders(Authorization(Credentials.Token(AuthScheme.Bearer, config.llmToken)))
          .withEntity(body)

        client
          .run(request)
          .use { res =>
            res
              .as[ChatResponse]
              .map(_.choices.headOption.map { c =>
                ClassificationResult(text, c.message.content, 0, "gpt-4o-mini", None)
              })
              .onError { err =>
                logger.error(s"Failed to decode ChatResponse: ${err.getMessage}")
              }
          }
      }
    }
}
