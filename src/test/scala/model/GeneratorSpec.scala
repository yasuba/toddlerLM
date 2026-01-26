package model

import munit.FunSuite

class GeneratorSpec extends FunSuite {

  test("Generator generates a sentence of the correct length from a given seed") {
    val tokens          = List("My", "biscuit's", "broken")
    val tokenizedCorpus = Map(0 -> tokens)
    val generator       = Generator(tokens, tokenizedCorpus)
    val seed            = List("My")
    val sentenceLength  = 2
    val result          = generator.generate(seed, sentenceLength)
    val resultTokens    = result.split(" ")
    assertEquals(resultTokens.length, seed.length + sentenceLength)
    assert(result.startsWith("My"))
  }

  test("Generator returns only the seed if sentenceLength is 0") {
    val tokens          = List("My", "biscuit's", "broken")
    val tokenizedCorpus = Map(0 -> tokens)
    val generator       = Generator(tokens, tokenizedCorpus)
    val seed            = List("My")
    val sentenceLength  = 0
    val result          = generator.generate(seed, sentenceLength)
    assertEquals(result, "My")
  }

  test("Generator handles empty seed by generating random word") {
    val tokens          = List("My", "biscuit's", "broken")
    val tokenizedCorpus = Map(0 -> tokens)
    val generator       = Generator(tokens, tokenizedCorpus)
    val seed            = List()
    val sentenceLength  = 1
    val result          = generator.generate(seed, sentenceLength)
    assert(result.nonEmpty)
  }
}
