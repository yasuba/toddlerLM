import cats.effect.{ExitCode, IO, IOApp}
import model.{Context, Generator, Tokenizer}

object ProbabilityMain extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {

    def prompt(msg: String): IO[String] = IO.blocking {
      print(msg)
      scala.io.StdIn.readLine()
    }

    val corpus: List[String] = scala.io.Source
      .fromResource("corpus.csv")
      .getLines()
      .toList

    def formatToTwoDecimalPlaces(d: Double): Double =
      BigDecimal(d)
        .setScale(2, BigDecimal.RoundingMode.HALF_UP)
        .toDouble

    def probabilitiesGen(generator: Generator, seed: List[String]): IO[Unit] = {
      val context  = generator.context(seed.size)
      val table    = generator.mkProbabilityTable(context)
      val probs    = table.get(Context(seed))
      val response = probs
        .flatMap(_.toList.sortBy(-_._2).headOption)
        .map(sAndP => s"Most probable next token is ${sAndP._1} with a probability of ${formatToTwoDecimalPlaces(sAndP._2 * 100)}%")
        .getOrElse("Context not found in ProbabilityTable")
      IO.println(response)
    }

    def promptLoop: IO[Unit] =
      (for {
        seedInput <- prompt("Enter some words:")
        seed       = seedInput.trim.split("\\s+").toList
        tokenized  = Tokenizer(corpus).tokenizeCSV
        generator  = Generator(3, tokenized)
        _         <- probabilitiesGen(generator, seed)
      } yield ()).foreverM

    promptLoop.as(ExitCode.Success)
  }
}
