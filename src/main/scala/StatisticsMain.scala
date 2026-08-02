import cats.effect.{ExitCode, IO, IOApp}
import model.{Statistics, Tokenizer}

import java.io.PrintWriter

object StatisticsMain extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {

    val corpus: List[String] = scala.io.Source
      .fromResource("response-pairs-corpus.csv")
      .getLines()
      .toList

    val tokenized = Tokenizer(corpus).tokenizeCSV
    val pw        = new PrintWriter("statistics.csv")
    pw.println(s"order,total,deterministic,ambiguous,%deterministic,meanContinuations(ambiguous),maxContinuations")

    List(2, 3, 4).foreach { n =>
      val stats = Statistics(tokenized, n).contextAmbiguity
      pw.println(
        s"${stats.contextSize},${stats.total},${stats.deterministic},${stats.ambiguous},${stats.fractionDeterministic * 100},${stats.meanContinuationsWhenAmbiguous},${stats.maxContinuations}"
      )
    }
    pw.close()

    IO(ExitCode.Success)
  }
}
