package model

import io.circe._
import io.circe.generic.semiauto.*

final case class ClassificationResult(
  phrase: String,
  sentiment: String,
  confidence: Double,
  modelUsed: String,
  notes: Option[String] = None
)

object ClassificationResult {
  given Encoder[ClassificationResult] = deriveEncoder
}
