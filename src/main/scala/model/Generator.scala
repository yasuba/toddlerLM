package model

import model.ProbabilityBuilder.ProbabilityTable

import scala.annotation.tailrec
import scala.util.{Failure, Random, Success, Try}

trait Generator {
  def generate(seed: List[String], useSampling: Boolean): String
  def mkProbabilityTable(context: List[Map[Context, Seq[String]]]): ProbabilityTable
  def context(nGramSize: Int): List[Map[Context, Seq[String]]]
}

object Generator {

  def apply(n: Int, tokenizedCorpus: Map[Int, List[String]]): Generator = new Generator {

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
    val probabilityTables: List[(ProbabilityTable, Int)] =
      (1 to n).toList.map { size =>
        (mkProbabilityTable(context(size)), size)
      }

//    probabilityTables.last._1.take(3).foreach(c => println(s"context is ${c}"))

    // When the model is no longer able to find the context, we generate a random word.
    def randomWord: Map[String, Double] = {
      val table =
        probabilityTables.find(_._2 == n).map(_._1).getOrElse(throw new Exception(s"Could not find a probability for nGram size $n"))
      table.toList(Random.nextInt(table.size))._2
    }

    @tailrec
    def findPrediction(seed: List[String], nGramSize: Int): (Map[String, Double], Int) =
      if (seed.isEmpty) {
        println(s"seed empty: $nGramSize")
        (randomWord, nGramSize)
      } else probabilityTables
        .find(_._2 == nGramSize)
        .flatMap(t => t._1.get(Context(seed)))
        .map(_.filterNot(_._1 == "<SEP>"))
        .filter(_.nonEmpty) match {
        case Some(pred) =>
          println(s"found pred n=$nGramSize, pred is ${pred.keys}")
          (pred, nGramSize)
        case None       =>
          println(s"found no pred: $nGramSize")
          findPrediction(seed.tail, nGramSize - 1)
      }

    override def generate(seed: List[String], useSampling: Boolean = false): String = {
      // recursively get next most likely token from table
      @tailrec
      def nextToken(seed: List[String], acc: String, times: Int): String =
        if (times == 0) {
          acc
        } else {
          val predictions: (Map[String, Double], Int) = findPrediction(seed, n)
          val orderedPredictions                      = predictions._1.toList.sortBy(-_._2)
          val predictedToken                          = {
            val pred = if (!useSampling) orderedPredictions else Random.shuffle(orderedPredictions)
            pred.headOption.map(_._1).getOrElse("No next token found")
          }
          val newSeed                                 = (seed :+ predictedToken).takeRight(n)
          if (predictedToken == "<END>") acc else nextToken(newSeed, acc + s" " + predictedToken, times - 1)
        }
      val initialContext                                                 = seed.takeRight(n)
      nextToken(initialContext, seed.mkString(" "), 50)
    }
  }

}
