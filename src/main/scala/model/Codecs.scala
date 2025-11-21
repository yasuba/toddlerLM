package model

import client.LlmClient.{ChatChoice, ChatMessage, ChatRequest, ChatResponse}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.syntax.EncoderOps
import io.circe.{Decoder, Encoder, Json}

object Codecs {
  implicit val cm: Decoder[ChatMessage] = deriveDecoder[ChatMessage]

  implicit val cc: Decoder[ChatChoice] = deriveDecoder[ChatChoice]

  implicit val cr: Decoder[ChatResponse] = deriveDecoder[ChatResponse]

  implicit val cmE: Encoder[ChatMessage] = m =>
    Json.obj(
      "role"    -> m.role.asJson,
      "content" -> m.content.asJson
    )

  implicit val crE: Encoder[ChatRequest] = r =>
    Json.obj(
      "model"       -> r.model.asJson,
      "messages"    -> r.messages.asJson,
      "temperature" -> r.temperature.asJson
    )

  implicit val clRE: Encoder[ClassificationResult] = deriveEncoder[ClassificationResult]
}
