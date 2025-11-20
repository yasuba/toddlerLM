package model

import cats.effect.Concurrent
import client.LlmClient.{ChatChoice, ChatMessage, ChatRequest, ChatResponse}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.syntax.EncoderOps
import io.circe.{Decoder, Encoder, Json}
import org.http4s.circe.{jsonEncoderOf, jsonOf}
import org.http4s.{EntityDecoder, EntityEncoder}

object Codecs {
  given Decoder[ChatMessage] = deriveDecoder[ChatMessage]

  given Decoder[ChatChoice] = deriveDecoder[ChatChoice]

  given Decoder[ChatResponse] = deriveDecoder[ChatResponse]

  given Encoder[ChatMessage] = m =>
    Json.obj(
      "role"    -> m.role.asJson,
      "content" -> m.content.asJson
    )

  given Encoder[ChatRequest] = r =>
    Json.obj(
      "model"       -> r.model.asJson,
      "messages"    -> r.messages.asJson,
      "temperature" -> r.temperature.asJson
    )

  given Encoder[ClassificationResult] = deriveEncoder[ClassificationResult]

  given [F[_]: Concurrent]: EntityEncoder[F, ChatRequest]  = jsonEncoderOf[F, ChatRequest]
  given [F[_]: Concurrent]: EntityDecoder[F, ChatResponse] = jsonOf[F, ChatResponse]

}
