package model

case class Context(words: Seq[String])

object ContextBuilder {

  def nGram(contextSize: Int, tokens: List[List[String]]): List[Map[Context, Seq[String]]] =
    tokens.map { t =>
      t.sliding(contextSize + 1).foldLeft(Map.empty[Context, Seq[String]]) { (acc, window) =>
        val context   = window.take(contextSize)
        val nextToken = window.last
        acc.updated(Context(context), acc.getOrElse(Context(context), Seq.empty) :+ nextToken)
      }
    }

}
