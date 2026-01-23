package model

trait ProbabilityAnalyzer  {
  def nextTokenPredictions(tokens: List[String]): Map[String, Double]
}

object ProbabilityAnalyzer {
  def apply(contextsAndTokens: List[Map[Context, Seq[String]]]): ProbabilityAnalyzer = new ProbabilityAnalyzer {
    def nextTokenPredictions(tokens: List[String]): Map[String, Double] = {
      val probabilityTable = ProbabilityBuilder.calculateProbabilities(contextsAndTokens)

      probabilityTable(Context(tokens))
    }
  }
}
