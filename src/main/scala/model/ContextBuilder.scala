package model

case class Context(words: Seq[String])

object ContextBuilder {

  def nGram(n: Int, tokens: Seq[String]): Map[Context, Seq[String]] = {
    tokens.sliding(n + 1).map { window =>
      val context = window.take(n)
      val nextToken = window.last
      (Context(context) -> Seq(nextToken))
    }
      .toMap
  }
}
