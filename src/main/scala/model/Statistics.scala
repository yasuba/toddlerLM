package model

trait Statistics  {
  def contextAmbiguity: AmbiguityStats
  def vocabularyStats: VocabStats
}
object Statistics {

  def apply(tokenizedCorpus: Map[Int, List[String]], n: Int): Statistics = new Statistics {
    val ctxBuilder: ContextBuilder.type   = ContextBuilder
    val ctxAnalyzer: ContextAnalyzer.type = ContextAnalyzer
    val probabilityBuilder                = ProbabilityBuilder(tokenizedCorpus)

    val frequencyTable: Map[Context, Map[String, Int]] = ctxAnalyzer.countFrequencies(probabilityBuilder.context(n))

    override def contextAmbiguity: AmbiguityStats = {
      val total: Int              = frequencyTable.size
      val deterministicCount: Int = frequencyTable.count(_._2.size == 1) // contexts with exactly one next token

      val order2ContextWith17Continuations = frequencyTable.filter(_._2.size == 17)

      // println(s"Key of order2 with 17 continuations is ${order2ContextWith17Continuations.keys}: this is the model's freedom point - where sub-styles diverge")

      val ambiguousCount: Int = frequencyTable.count(_._2.size > 1) // contexts with a number of next token options

      val fractionDeterministic: Double = deterministicCount.toDouble / total
      // among the ambiguousCount ones, how much choice on average?
      val ambiguousContexts             = frequencyTable.filter(_._2.size > 1)
      val meanContinuations             =
        if (ambiguousContexts.nonEmpty)
          ambiguousContexts.values.map(_.size).sum.toDouble / ambiguousContexts.size
        else 0.0

      // the biggest fan-out — the context with the most continuations
      val maxContinuations = frequencyTable.values.map(_.size).maxOption.getOrElse(0)

      AmbiguityStats(
        n,
        total = total,
        deterministic = deterministicCount,
        ambiguous = ambiguousCount,
        fractionDeterministic = fractionDeterministic,
        meanContinuationsWhenAmbiguous = meanContinuations,
        maxContinuations = maxContinuations
      )
    }

    override def vocabularyStats: VocabStats = {
      val allTokens: List[String] =
        tokenizedCorpus.values.flatten.toList.filterNot(_ == "<SEP>").filterNot(_ == "<EOS>").filterNot(_ == "<END>").filter(_.nonEmpty)
      val freq: Map[String, Int]  = allTokens.groupBy(identity).view.mapValues(_.size).toMap

      val tokens          = allTokens.length
      val types           = freq.size
      val ttr             = types.toDouble / tokens
      val hapax           = freq.count(_._2 == 1)
      val hapaxProportion = hapax.toDouble / types

      VocabStats(
        tokens,
        freq,
        types,
        ttr,
        hapax,
        hapaxProportion
      )
    }
  }
}

case class AmbiguityStats(
  contextSize: Int,
  total: Int,
  deterministic: Int,
  ambiguous: Int,
  fractionDeterministic: Double,
  meanContinuationsWhenAmbiguous: Double,
  maxContinuations: Int
)

case class VocabStats(
  tokens: Int,
  freqMap: Map[String, Int],
  types: Int,
  ttr: Double,
  hapaxCount: Int,
  hapaxProportion: Double
)
