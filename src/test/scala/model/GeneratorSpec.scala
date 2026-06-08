package model

import munit.FunSuite

class GeneratorSpec extends FunSuite {

  test("Generator generates a sentence of the correct length from a given seed") {
    val tokens          = List("My", "biscuit's", "broken")
    val tokenizedCorpus = Map(0 -> tokens)
    val generator       = Generator(tokens, tokenizedCorpus)
    val seed            = List("My")
    val sentenceLength  = 2
    val result          = generator.generate(seed, sentenceLength, false)
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
    val result          = generator.generate(seed, sentenceLength, false)
    assertEquals(result, "My")
  }

  test("Generator handles empty seed by generating random word") {
    val tokens          = List("My", "biscuit's", "broken")
    val tokenizedCorpus = Map(0 -> tokens)
    val generator       = Generator(tokens, tokenizedCorpus)
    val seed            = List()
    val sentenceLength  = 1
    val result          = generator.generate(seed, sentenceLength, false)
    assert(result.nonEmpty)
  }

  test("Generator stops generating when next predicted token is <EOS>") {
    val tokens          =
      List("i'm excited <SEP> you're excited<EOS>", "i feel happy <SEP> you feel happy <EOS>", "i'm happy <SEP> you're happy <EOS>").flatMap(_.split(" "))
    val tokenizedCorpus = Map(0 -> tokens)
    val generator       = Generator(tokens, tokenizedCorpus)
    val seed            = List("hello")
    val sentenceLength  = 3
    val result          = generator.generate(seed, sentenceLength, false)
    assert(!result.contains("<EOS>"))
  }
}
