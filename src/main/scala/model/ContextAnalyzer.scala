package model

object ContextAnalyzer {

  private type FrequencyTable = Map[Context, Map[String, Int]]

  private def contextMap(listOfMaps: List[Map[Context, Seq[String]]]): Map[Context, Seq[String]] =
    listOfMaps.foldLeft(Map.empty[Context, Seq[String]]) { case (acc, map) =>
      map.foldLeft(acc) { case (innerAcc, (ctx, seq)) =>
        innerAcc.updated(ctx, innerAcc.getOrElse(ctx, Seq.empty) ++ seq)
      }
    }

  def countFrequencies(listOfMaps: List[Map[Context, Seq[String]]]): FrequencyTable =
    contextMap(listOfMaps).map { ctxAndTokens =>
      val counts = ctxAndTokens._2.groupBy(identity).view.mapValues(_.size).toMap
      (ctxAndTokens._1, counts)
    }
}
