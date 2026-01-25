package model

case class Context(words: Seq[String])

object ContextBuilder {

  def nGram(n: Int, tokens: List[List[String]]): List[Map[Context, Seq[String]]] =
    tokens.map { t =>
      t.sliding(n + 1).foldLeft(Map.empty[Context, Seq[String]]) { (acc, window) =>
        val context   = window.take(n)
        val nextToken = window.last
        acc.updated(Context(context), acc.getOrElse(Context(context), Seq.empty) :+ nextToken)
      }
    }
}
