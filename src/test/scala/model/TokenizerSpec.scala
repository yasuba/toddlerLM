package model

import munit.FunSuite

class TokenizerSpec extends FunSuite {

  test("Tokenizer takes a string and returns it as tokens") {

    val corpus = scala.io.Source
      .fromResource("corpus.csv")
      .getLines()
      .toList
      .take(2)

    val obtained = Tokenizer(corpus).tokenizeCSV
    val expected = Map(1 -> List("The", "duck", "is", "crying", "because", "he", "lost", "his", "mummy"), 2 -> List("My", "biscuit's", "broken"))
    assertEquals(obtained, expected)
  }
}
