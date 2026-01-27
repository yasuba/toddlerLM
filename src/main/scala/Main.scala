import cats.*
import cats.effect.{ExitCode, IO, IOApp}
import model.{Context, Generator, Tokenizer}

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

    def taskReader(task: String, generator: Generator, seed: List[String], nGramSize: Int): IO[Unit] =
      task match {
        case "s" => sentenceGen(generator, seed)
        case "p" => probabilitiesGen(nGramSize, generator, seed)
        case _   => IO.println("Please enter either s or p")
      }

    def formatToTwoDecimalPlaces(d: Double): Double =
      BigDecimal(d)
        .setScale(2, BigDecimal.RoundingMode.HALF_UP)
        .toDouble

    def probabilitiesGen(nGramSize: Int, generator: Generator, seed: List[String]): IO[Unit] = {
      val context  = generator.context(nGramSize)
      val table    = generator.mkProbabilityTable(context)
      val probs    = table.get(Context(seed))
      val response = probs
        .flatMap(_.toList.sortBy(-_._2).headOption)
        .map(sAndP => s"Most probable next token is ${sAndP._1} with a probability of ${formatToTwoDecimalPlaces(sAndP._2 * 100)}%")
        .getOrElse("Context not found in ProbabilityTable")
      IO.println(response)
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
        task      <- prompt("Do you want a sentence or probabilities? Enter s or p:")
        _         <- taskReader(task, generator, seed, seed.size)
      } yield ()).foreverM

    promptLoop.as(ExitCode.Success)
  }

}
