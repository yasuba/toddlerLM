package model

import munit.FunSuite

class ContextBuilderSpec extends FunSuite {

  test("ContextBuilder takes tokens and returns the context and the next tokens for a bigram") {
    val obtained = ContextBuilder.nGram(1, List("My", "biscuit's", "broken"))
    val expected = Map(Context(Seq("My")) -> List("biscuit's"), Context(List("biscuit's")) -> List("broken"))
    assertEquals(obtained, expected)
  }

  test("ContextBuilder takes tokens and returns the context and the next tokens for a trigram (context length increasing to n)") {
    val obtained = ContextBuilder.nGram(2, List("My", "biscuit's", "broken"))
    val expected = Map(Context(Seq("My", "biscuit's")) -> List("broken"))
    assertEquals(obtained, expected)
  }
}
