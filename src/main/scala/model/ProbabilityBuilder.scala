package model

import model.ContextAnalyzer.countFrequencies

object ProbabilityBuilder {

  type ProbabilityTable = Map[Context, Map[String, Double]]

  def calculateProbabilities(listOfMaps: List[Map[Context, Seq[String]]]): ProbabilityTable =
    countFrequencies(listOfMaps).map { freq =>
      val total = freq._2.values.sum
      (freq._1, freq._2.map(tokensAndCounts => (tokensAndCounts._1, tokensAndCounts._2.toDouble / total.toDouble)))
    }
}
