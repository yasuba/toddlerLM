package client

import cats.MonadThrow
import cats.effect.Concurrent
import cats.implicits.*
import model.Codecs.given
import org.http4s.client.Client
import org.http4s.headers.Authorization
import org.http4s.{AuthScheme, Credentials, Header, Method, Request}
import org.typelevel.log4cats.SelfAwareStructuredLogger
import server.ServiceConfig

trait LlmClient[F[_]] {
  def classify(text: String): F[String]
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
  )(using logger: SelfAwareStructuredLogger[F], F: MonadThrow[F]): LlmClient[F] =
    new LlmClient[F] {

      def classify(text: String): F[String] = {
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
              .map(_.choices.headOption.map(_.message.content).getOrElse(""))
              .onError { err =>
                logger.error(s"Failed to decode ChatResponse: ${err.getMessage}")
              }
          }
      }
    }
}
