import cats.*
import cats.effect.{ExitCode, IO, IOApp}
import model.{Generator, Tokenizer}

import scala.io.StdIn

object Main extends IOApp {
  private def prompt(msg: String): IO[String] = IO.blocking {
    print(msg)
    scala.io.StdIn.readLine()
  }

  val corpus: List[String] = scala.io.Source
    .fromResource("corpus.csv")
    .getLines()
    .toList

  override def run(args: List[String]): IO[ExitCode] = {

    def promptLoop: IO[Unit] =
      (for {
        input            <- prompt("Enter some words: ")
        sentenceLength   <- prompt("How long would you like the sentence to be? ")
        _                 = println(s"input is $input")
        tokens            = input.split(" ").toList
        length            = sentenceLength.toInt
        tokenized         = Tokenizer(corpus).tokenizeCSV
        generatedSentence = Generator(tokens, tokenized).generate(tokens, length)
        _                <- IO.println(s"Most likely sentence will be $generatedSentence")
      } yield ()).foreverM

    promptLoop.as(ExitCode.Success)
  }

}
