package model

object ContextAnalyzer {

  def countFrequencies(listOfMaps:List[Map[Context, Seq[String]]]): Map[Context, Map[String, Int]] = {
    val contextMap: Map[Context, Seq[String]] = listOfMaps.foldLeft(Map.empty[Context, Seq[String]]) {
      case (acc, map) =>
        map.foldLeft(acc) { case (innerAcc, (ctx, seq)) =>
          innerAcc.updated(ctx, innerAcc.getOrElse(ctx, Seq.empty) ++ seq)
        }
    }

    contextMap.map { ctxAndTokens =>
      val counts = ctxAndTokens._2.groupBy(identity).view.mapValues(_.size).toMap
      (ctxAndTokens._1, counts)
    }
  }
}
