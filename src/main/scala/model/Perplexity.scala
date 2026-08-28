package model

import model.ProbabilityBuilder.*

import scala.annotation.tailrec

trait Perplexity {
  // Takes a generated response, a gold response from the corpus and the context size and returns a score of how probable the model found it
  def responsePerplexity(input: List[String], goldResponse: List[String], contextSize: Int): (Option[Double], Int, Int)
  def getProbesAndGoldResponse(input: List[String], corpus: Map[Int, List[String]]): Option[List[String]]
}

object Perplexity {
  def apply(tokenizedCorpus: Map[Int, List[String]]): Perplexity =
    new Perplexity {

      val probabilityBuilder = ProbabilityBuilder(tokenizedCorpus)

      val controlTokens = Set("<SEP>", "<NARR>", "<INFO>", "<EMO>", "<REQ>", "<OBS>")

      override def getProbesAndGoldResponse(input: List[String], corpus: Map[Int, List[String]]): Option[List[String]] =
        corpus.values
          .map { line =>
            val sep = line.indexOf("<SEP>")
            line.take(sep) -> line.drop(sep + 1)
          }
          .find(_._1 == input) // exact equality, first match
          .map(_._2)

      override def responsePerplexity(input: List[String], goldResponse: List[String], contextSize: Int): (Option[Double], Int, Int) = {

        val tables = probabilityBuilder
          .probabilityTables(contextSize, probabilityBuilder.context)

//        println(s"the int is ${tables}")

        @tailrec
        def probabilityOf(token: String, context: List[String], cs: Int): Double =
          // Try the table at this context size first; on a miss, drop the oldest
          // context token and try the next-smaller table — mirroring findPrediction.
          tables
            .find(_._2 == cs)
            .flatMap(t => t._1.get(Context(context)))
            .flatMap(dist => dist.get(token)) match {
            case Some(p) => p
            case None    =>
              if (contextSize <= 1 || context.isEmpty) 0.0
              else probabilityOf(token, context.tail, contextSize - 1)
          }

        val full = input ++ List("<SEP>") ++ goldResponse

        val responseStart = input.length + 1

        val (logProbs, oovCount) =
          (responseStart until full.length).foldLeft((List.empty[Double], 0)) { case ((logs, oov), i) =>
            val token   = full(i)
            val context = full.slice(i - contextSize, i)
            if (controlTokens.contains(token)) (logs, oov)
            else probabilityOf(token, context, contextSize) match {
              case 0.0 => (logs, oov + 1)
              case p   =>
//                println(s"$token | ctx=${context.mkString(" ")} | p=$p")
                (math.log(p) :: logs, oov)
            }
          }

        if (logProbs.isEmpty) (None, logProbs.length, oovCount)
        else (Some(math.exp(-logProbs.sum / logProbs.length)), logProbs.length, oovCount)
      }
    }
}
