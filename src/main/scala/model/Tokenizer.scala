package model

sealed trait Tokenizer {
  def tokenizeCSV: Map[Int, Seq[String]]
}

case object Tokenizer {
  def apply(corpus: List[String]): Tokenizer =
    new Tokenizer {
      override def tokenizeCSV: Map[Int, Seq[String]] =
        corpus
          .drop(1)
          .map { line =>
            val cols       = line.replaceAll(" ", "").split('|').tail.map(_.trim)
            val sentenceId = cols(0).toInt
            val word       = cols(1)
            sentenceId -> word
          }
          .groupBy(_._1)
          .map { case (sid, pairs) => sid -> pairs.map(_._2) }
    }
}

/*
scala.io.Source
          .fromResource(filename)
          .getLines()
          .toList
 */
