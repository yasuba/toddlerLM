package model

import model.ProbabilityBuilder.*

import scala.annotation.tailrec
import scala.util.{Failure, Random, Success, Try}

trait Generator {
  def generate(seed: List[String], useSampling: Boolean): String
}

object Generator {

  def apply(n: Int, tokenizedCorpus: Map[Int, List[String]]): Generator = new Generator {

    val probabilityBuilder = ProbabilityBuilder(tokenizedCorpus)
    /*
      How to generate the next tokens - this can be done greedily (deterministic) which means you pick the most likely
      token or sampling (probabilistic) where you randomly sample according to probabilities
      Start with greedy first
     */

    // When the model is no longer able to find the context, we generate a random word.
    def randomWord: Map[String, Double] = {
      val table =
        probabilityBuilder
          .probabilityTables(n, probabilityBuilder.context)
          .find(_._2 == n)
          .map(_._1)
          .getOrElse(throw new Exception(s"Could not find a probability for nGram size $n"))
      table.toList(Random.nextInt(table.size))._2
    }

    @tailrec
    def findPrediction(seed: List[String], contextSize: Int): (Map[String, Double], Int) =
      if (seed.isEmpty) {
        (randomWord, contextSize)
      } else probabilityBuilder
        .probabilityTables(n, probabilityBuilder.context)
        .find(_._2 == contextSize)
        .flatMap(t => t._1.get(Context(seed)))
        .map(_.filterNot(_._1 == "<SEP>"))
        .filter(_.nonEmpty) match {
        case Some(pred) =>
          (pred, contextSize)
        case None       =>
          findPrediction(seed.tail, contextSize - 1)
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
