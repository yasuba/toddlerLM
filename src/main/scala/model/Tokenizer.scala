package model

sealed trait Tokenizer {
  def tokenizeCSV: Map[Int, List[String]]
}

case object Tokenizer {
  def apply(corpus: List[String]): Tokenizer =
    new Tokenizer {
      override def tokenizeCSV: Map[Int, List[String]] =
        corpus
          .flatMap { line =>
            val cols = line.split('|').toList.headOption.toList
            cols.map(_.trim)
          }
          .zipWithIndex
          .map { case (col, idx) => (idx + 1) -> col.split(" ").toList }
          .toMap
    }
}

/*
scala.io.Source
          .fromResource(filename)
          .getLines()
          .toList
 */
