package model

import io.circe._
import io.circe.generic.semiauto._

final case class ToddlerPhrase(phrase: String)

object ToddlerPhrase {
  implicit val tpDecoder: Decoder[ToddlerPhrase] = deriveDecoder
  implicit val tpEncoder: Encoder[ToddlerPhrase] = deriveEncoder
}
