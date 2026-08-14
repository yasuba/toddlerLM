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
    val pw        = new PrintWriter("results/statistics.csv")
    pw.println(s"order,total,deterministic,ambiguous,%deterministic,meanContinuations(ambiguous),maxContinuations")

    List(2, 3, 4).foreach { n =>
      val statistics = Statistics(tokenized, n)
      val stats      = statistics.contextAmbiguity

      pw.println(
        s"${stats.contextSize},${stats.total},${stats.deterministic},${stats.ambiguous},${stats.fractionDeterministic * 100},${stats.meanContinuationsWhenAmbiguous},${stats.maxContinuations}"
      )
    }
    pw.close()

    val pw2   = new PrintWriter("results/zipf.csv")
    val vocab = Statistics(tokenized, 3).vocabularyStats

    val ranked = vocab.freqMap.toList.sortBy(-_._2) // most frequent first
    // write index+1 as rank, word, frequency
    ranked.zipWithIndex.foreach { case ((word, count), i) =>
      pw2.println(s"${i + 1},$word,$count")
    }

    pw2.close()

    IO(ExitCode.Success)
  }
}
