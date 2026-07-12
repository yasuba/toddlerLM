import cats.effect.{ExitCode, IO, IOApp}
import model.{Perplexity, Tokenizer}

object PerplexityMain extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {

    val corpus: List[String] = scala.io.Source
      .fromResource("response-pairs-corpus.csv")
      .getLines()
      .toList

    val inputs = List(
      "ballerinas do this but they don't hold cake in their hands",
      "you have to wash your hair",
      "you're not very strong you can't go on aeroplanes",
      "after a sleep time he's going to be two",
      "we should eat those fruits before they get moldier",
      "when I go high up that means I'm a big girl",
      "wiggle wiggle jellyfish",
      "mummy can I have you",
      "I want to fly my kite today",
      "it's missing the red thing I can't able to find it",
      "mummy watch this",
      "thomas's wheel broke off",
      "I want to try to fly outside",
      "it tastes a bit stingy because of the hoops",
      "daddy was at the top so he can win the prize",
      "baby's too small to go on the ride",
      "let's do the train tracks because I never do that for ages",
      "who sings the song on the phone",
      "maybe he can lift it over to her and so her can get the next present",
      "all the sky is going to cover the cloud and I think it's going to rain",
      "I laughed because I'm shy",
      "I like peanut butter because I have fingers"
    )

    val goldResponses: List[String] = scala.io.Source
      .fromResource("held-out-probes-and-responses.txt")
      .getLines()
      .toList

    def promptLoop(): Unit =
      val tokenized  = Tokenizer(corpus).tokenizeCSV
      val tokGR      = Tokenizer(goldResponses).tokenizeCSV
      val p          = Perplexity(tokenized)
      val d          = inputs.map { input =>
        val i = input.trim.split("\\s+").toList
        p.getProbesAndGoldResponse(i, tokGR) match {
          case Some(gold) => Some(input -> p.responsePerplexity(i, gold, 2))
          case None       => println(s"no gold response for: $input"); None
        }
      }
      println(s"probabilities are ${d}")

    promptLoop()
    IO(ExitCode.Success)
  }
}
