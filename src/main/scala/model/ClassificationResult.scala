package model

import io.circe._
import io.circe.generic.semiauto._

final case class ClassificationResult(
  phrase: String,
  sentiment: String,
  confidence: Double,
  modelUsed: String,
  notes: Option[String] = None
)

object ClassificationResult {
  implicit val encoder: Encoder[ClassificationResult] = deriveEncoder
}
