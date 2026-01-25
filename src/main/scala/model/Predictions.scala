package model

trait Predictions {
  def nextTokenPredictions(tokens: List[String]): Map[String, Double]
}

object Predictions {
  def apply(contextsAndTokens: List[Map[Context, Seq[String]]]): Predictions = new Predictions {
    def nextTokenPredictions(tokens: List[String]): Map[String, Double] = {
      val probabilityTable = ProbabilityBuilder.calculateProbabilities(contextsAndTokens)

      probabilityTable(Context(tokens))
    }
  }
}
