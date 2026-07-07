import cats.effect.{ExitCode, IO, IOApp}
import model.{Perplexity, Tokenizer}

object PerplexityMain extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {

    val corpus: List[String] = scala.io.Source
      .fromResource("response-pairs-corpus.csv")
      .getLines()
      .toList

    val inputs = List(
      "the duck is crying because he lost his mummy",
      "after you eat me i'll be a spoon",
      "why do you work",
      "i don't want to go to nursery",
      "i want frothy milk",
      "it's sunny outside",
      "i did it all by my own self",
      "tummy hurts",
      "do a picture so i can grab a paper in here"
    )

    def promptLoop(): Unit =
      val tokenized  = Tokenizer(corpus).tokenizeCSV
      val p          = Perplexity(tokenized)
      val d          = inputs.map { input =>
        val i = input.trim.split("\\s+").toList
        p.getGoldResponse(i) match {
          case Some(gold) => Some(input -> p.responsePerplexity(i, gold, 4))
          case None       => println(s"no gold response for: $input"); None
        }
      }
      println(s"probabilities are ${d}")

    promptLoop()
    IO(ExitCode.Success)
  }
}
