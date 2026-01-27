package model

import model.ProbabilityBuilder.ProbabilityTable

import scala.annotation.tailrec
import scala.util.{Failure, Random, Success, Try}

trait Generator {
  def generate(seed: List[String], sentenceLength: Int, useSampling: Boolean): String
  def mkProbabilityTable(context: List[Map[Context, Seq[String]]]): ProbabilityTable
  def context(nGramSize: Int): List[Map[Context, Seq[String]]]
}

object Generator {

  def apply(tokens: List[String], tokenizedCorpus: Map[Int, List[String]]): Generator = new Generator {

    /*
      How to generate the next tokens - this can be done greedily (deterministic) which means you pick the most likely
      token or sampling (probabilistic) where you randomly sample according to probabilities
      Start with greedy first
     */

    def context(nGramSize: Int): List[Map[Context, Seq[String]]]                       = ContextBuilder.nGram(nGramSize, tokenizedCorpus.toList.map(_._2))
    def mkProbabilityTable(context: List[Map[Context, Seq[String]]]): ProbabilityTable = ProbabilityBuilder.calculateProbabilities(context)

    // this will generate tables for every nGram size descending from the original down to a unigram. These are to act
    // as fallbacks in case the current context cannot be found in the original probabilityTable. This is known as the
    // backoff model, which is a classic NLP strategy.
    def probabilityTables: List[(ProbabilityTable, Int)] =
      (0 to tokens.size).toList.map { size =>
        mkProbabilityTable(context(size))
      }.zipWithIndex

    // When the model is no longer able to find the context, we generate a random word.
    def randomWord: Map[String, Double] = {
      val table =
        probabilityTables.find(_._2 == tokens.size).map(_._1).getOrElse(throw new Exception(s"Could not find a probability for nGram size ${tokens.size}"))
      table.toList(Random.nextInt(table.size))._2
    }

    @tailrec
    def findPrediction(seed: List[String], nGramSize: Int): (Map[String, Double], Int) =
      if (seed.isEmpty) (randomWord, nGramSize)
      else probabilityTables.find(_._2 == nGramSize).flatMap(t => t._1.get(Context(seed))) match {
        case Some(pred) => (pred, nGramSize)
        case None       =>
          findPrediction(seed.tail, nGramSize - 1)
      }

    override def generate(seed: List[String], sentenceLength: Int, useSampling: Boolean = false): String = {
      // recursively get next most likely token from table
      @tailrec
      def nextToken(seed: List[String], acc: String, times: Int): String =
        if (times == 0) {
          acc
        } else {
          val predictions: (Map[String, Double], Int) = findPrediction(seed, tokens.size)
          val orderedPredictions                      = predictions._1.toList.sortBy(-_._2)
          val predictedToken                          = {
            val pred = if (!useSampling) orderedPredictions else Random.shuffle(orderedPredictions)
            pred.headOption.map(_._1).getOrElse("No next token found")
          }
          val newSeed                                 = (seed :+ predictedToken).takeRight(predictions._2)
          nextToken(newSeed, acc + s" " + predictedToken, times - 1)
        }
      nextToken(seed, seed.mkString(" "), sentenceLength)
    }
  }

}
