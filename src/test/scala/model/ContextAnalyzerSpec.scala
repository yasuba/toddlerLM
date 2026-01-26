package model

import model.TestData.contexts
import munit.FunSuite

class ContextAnalyzerSpec extends FunSuite {

  test("ContextAnalyzer can count the frequency of next tokens for any given context") {
    val freq = ContextAnalyzer.countFrequencies(contexts)

    val obtained = freq
      .map { case (ctx, m) =>
        ctx -> m.filter { case (_, count) => count >= 3 && count <= 4 }
      }
      .filter { case (_, m) => m.nonEmpty }

    val expected = Map(
      Context(List("love")) -> Map("my" -> 4),
      Context(List("I"))    -> Map("feel" -> 4),
      Context(List("to"))   -> Map("go" -> 3),
      Context(List("That")) -> Map("is" -> 3)
    )

    assertEquals(obtained, expected)
  }
}
