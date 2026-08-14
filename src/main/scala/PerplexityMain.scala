import cats.effect.{ExitCode, IO, IOApp}
import model.{Perplexity, Tokenizer}
import java.io.PrintWriter

object PerplexityMain extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {

    val corpus: List[String] = scala.io.Source
      .fromResource("response-pairs-corpus.csv")
      .getLines()
      .toList

    val goldResponses: List[String] = scala.io.Source
      .fromResource("held-out-probes-and-responses.txt")
      .getLines()
      .toList

    def promptLoop(): Unit =
      val tokenized  = Tokenizer(corpus).tokenizeCSV
      val tokGR      = Tokenizer(goldResponses).tokenizeCSV
      val p          = Perplexity(tokenized)
      val inputs     = goldResponses.map(line => line.take(line.indexOf("<SEP>")))
      val pw = new PrintWriter("results/heldout_results.csv")
      pw.println("probe,order,perplexity,scoredCount,oovCount,goldLength")

      List(2, 3, 4).foreach { n =>
        val results = inputs.map { input =>
          val i = input.trim.split("\\s+").toList
          p.getProbesAndGoldResponse(i, tokGR) match {
            case Some(gold) =>
              val (pplOpt, scored, oov) = p.responsePerplexity(i, gold, n)
              val ppl = pplOpt.map(_.toString).getOrElse("NA")
              pw.println(s"${i.mkString(" ")},$n,$ppl,$scored,$oov,${gold.length}")

            case None =>
              println(s"no gold response for: $input")
          }
        }
      }
      pw.close()

    promptLoop()
    IO(ExitCode.Success)
  }
}

