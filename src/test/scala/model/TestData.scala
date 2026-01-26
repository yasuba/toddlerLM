package model

object TestData {

  val corpus: List[String] = scala.io.Source
    .fromResource("corpus.csv")
    .getLines()
    .toList

  val tokenized: Map[Int, Seq[String]]          = Tokenizer(corpus).tokenizeCSV
  val contexts: List[Map[Context, Seq[String]]] = ContextBuilder.nGram(1, tokenized.toList.map(_._2.toList))
}
