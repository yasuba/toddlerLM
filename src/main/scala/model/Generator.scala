package model

import model.ProbabilityBuilder.ProbabilityTable

import scala.annotation.tailrec
import scala.util.{Failure, Random, Success, Try}

trait Generator {
  def generate(seed: List[String], sentenceLength: Int): String
}

object Generator {

  def apply(tokens: List[String], tokenizedCorpus: Map[Int, List[String]]): Generator = new Generator {

    /*
      How to generate the next tokens - this can be done greedily (deterministic) which means you pick the most likely token or sampling (probabilistic) where you randomly sample according to probabilities
      Start with greedy first
     */

    def context(nGramSize: Int): List[Map[Context, Seq[String]]]                       = ContextBuilder.nGram(nGramSize, tokenizedCorpus.toList.map(_._2))
    def mkProbabilityTable(context: List[Map[Context, Seq[String]]]): ProbabilityTable = ProbabilityBuilder.calculateProbabilities(context)

    // this will generate a new table with a smaller nGram than the previous one in case the current context cannot be found
    // in the probabilityTable. This is known as the backoff model, which is a classic NLP strategy.
    val probabilityTable: ProbabilityTable = mkProbabilityTable(context(tokens.size))

    // When the model is no longer able to find the context, we generate a random word.
    def randomWord: Map[String, Double] = probabilityTable.toList(Random.nextInt(probabilityTable.size))._2

    @tailrec
    def findPrediction(seed: List[String]): Map[String, Double] =
      if (seed.isEmpty) randomWord
      else probabilityTable.get(Context(seed)) match {
        case Some(pred) => pred
        case None       =>
          println(s"seed tail is ${seed.tail}")
          findPrediction(seed.tail)
      }

    override def generate(seed: List[String], sentenceLength: Int): String = {
      // recursively get next most likely token from table
      @tailrec
      def nextToken(seed: List[String], acc: String, times: Int): String = {
        val predictions: Map[String, Double] = findPrediction(seed)
        if (times == 0) {
          acc
        } else {
          val newSeed = predictions.toList.sortBy(-_._2).headOption.map(_._1).getOrElse("No next token found")
          nextToken(List(newSeed), acc + s" " + newSeed, times - 1)
        }
      }
      nextToken(seed, seed.mkString(" "), sentenceLength)
    }
  }

}
