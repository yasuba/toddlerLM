import cats.*
import cats.effect.{ExitCode, IO, IOApp}
import model.{Generator, Tokenizer}

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

    def promptInt(msg: String): IO[Int] = {
      def loop: IO[Int] = for {
        input  <- prompt(msg)
        result <- input.trim match {
                    case s if s.matches("\\d+") => IO.pure(s.toInt)
                    case _                      => IO.println("Please enter a valid number.") *> loop
                  }
      } yield result
      loop
    }

    def samplingPrompt(msg: IO[String]): IO[Boolean] =
      msg.flatMap {
        case "yes" => IO(true)
        case "no"  => IO(false)
        case _     => samplingPrompt(prompt("Do you want to use sampling? yes or no:"))
      }

    def sentenceGen(generator: Generator, seed: List[String]): IO[Unit] =
      for {
        sentenceLength    <- promptInt("How many words do you want to generate?")
        length             = sentenceLength
        samplingResponse   = prompt("Do you want to use sampling? yes or no:")
        sampling          <- samplingPrompt(samplingResponse)
        generatedSentence <- IO(generator.generate(seed, length, sampling))
        _                 <- IO.println(s"Most likely sentence will be $generatedSentence")
      } yield ()

    def promptLoop: IO[Unit] =
      (for {
        seedInput <- prompt("Enter some words:")
        seed       = seedInput.trim.split("\\s+").toList
        tokenized  = Tokenizer(corpus).tokenizeCSV
        generator  = Generator(seed, tokenized)
        _         <- sentenceGen(generator, seed)
      } yield ()).foreverM

    promptLoop.as(ExitCode.Success)
  }

}
