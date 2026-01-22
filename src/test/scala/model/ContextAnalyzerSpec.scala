package model

import munit.FunSuite

class ContextAnalyzerSpec extends FunSuite {

  test("ContextAnalyzer can count the frequency of next tokens for any given context") {
    val corpus = scala.io.Source
      .fromResource("corpus.csv")
      .getLines()
      .toList

    val tokenized = Tokenizer(corpus).tokenizeCSV
    val contexts  = tokenized.map(t => ContextBuilder.nGram(1, t._2)).toList

    val freq     = ContextAnalyzer.countFrequencies(contexts)

    val obtained = freq
      .map { case (ctx, m) =>
        ctx -> m.filter { case (_, count) => count > 1 }
      }
      .filter { case (_, m) => m.nonEmpty }

    val expected = Map(
      Context(List("go"))    -> Map("on" -> 2),
      Context(List("don't")) -> Map("like" -> 3),
      Context(List("want"))  -> Map("to" -> 3),
      Context(List("ice"))   -> Map("cream" -> 2),
      Context(List("on"))    -> Map("the" -> 2),
      Context(List("I"))     -> Map("want" -> 2, "don't" -> 4, "love" -> 2, "like" -> 3)
    )

    assertEquals(obtained, expected)
  }
}
