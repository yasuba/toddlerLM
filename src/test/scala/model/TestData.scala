package model

object TestData {

  val corpus: List[String] = scala.io.Source
    .fromResource("corpus.csv")
    .getLines()
    .toList

  val tokenized: Map[Int, Seq[String]]          = Tokenizer(corpus).tokenizeCSV
  val contexts: List[Map[Context, Seq[String]]] = tokenized.map(t => ContextBuilder.nGram(1, t._2)).toList
}
