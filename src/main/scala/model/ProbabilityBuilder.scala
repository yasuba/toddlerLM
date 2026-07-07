package model

import model.ContextAnalyzer.countFrequencies

type ProbabilityTable = Map[Context, Map[String, Double]]

trait ProbabilityBuilder {
  def mkProbabilityTable(context: List[Map[Context, Seq[String]]]): ProbabilityTable
  def context(nGramSize: Int): List[Map[Context, Seq[String]]]
  def probabilityTables(n: Int, context: Int => List[Map[Context, Seq[String]]]): List[(ProbabilityTable, Int)]
}

object ProbabilityBuilder {
  def calculateProbabilities(listOfMaps: List[Map[Context, Seq[String]]]): ProbabilityTable =
    countFrequencies(listOfMaps).map { freq =>
      val total = freq._2.values.sum
      (freq._1, freq._2.map(tokensAndCounts => (tokensAndCounts._1, tokensAndCounts._2.toDouble / total.toDouble)))
    }

  def apply(tokenizedCorpus: Map[Int, List[String]]): ProbabilityBuilder =
    new ProbabilityBuilder {
      override def mkProbabilityTable(context: List[Map[Context, Seq[String]]]): ProbabilityTable = ProbabilityBuilder.calculateProbabilities(context)

      override def context(nGramSize: Int): List[Map[Context, Seq[String]]] = ContextBuilder.nGram(nGramSize, tokenizedCorpus.toList.map(_._2))

      // this will generate tables for every nGram size descending from the original down to a unigram. These are to act
      // as fallbacks in case the current context cannot be found in the original probabilityTable. This is known as the
      // backoff model, which is a classic NLP strategy.
      def probabilityTables(n: Int, context: Int => List[Map[Context, Seq[String]]]): List[(ProbabilityTable, Int)] =
        (1 to n).toList.map { size =>
          (mkProbabilityTable(context(size)), size)
        }
    }
}
