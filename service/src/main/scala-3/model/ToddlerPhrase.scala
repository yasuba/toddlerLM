package model

import io.circe._
import io.circe.generic.semiauto.*

final case class ToddlerPhrase(phrase: String)

object ToddlerPhrase:
  given Decoder[ToddlerPhrase] = deriveDecoder
  given Encoder[ToddlerPhrase] = deriveEncoder
