package model

import munit.FunSuite

class PerplexitySpec extends FunSuite {

  test("Perplexity getGoldResponse will filter the corpus for the gold response") {
    val firstTokens     = List("my", "biscuit's", "broken", "<SEP>", "your", "biscuit's", "broken", "<EOS>", "it", "still", "tastes", "the", "same", "<END>")
    val secondTokens    = List("is", "it", "tomorrow", "<SEP>", "no", "it", "isn't", "tomorrow", "<EOS>", "it's", "still", "today", "<END>")
    val tokenizedCorpus = Map(0 -> firstTokens, 1 -> secondTokens)
    val P               = Perplexity(tokenizedCorpus)
    val result          = P.getProbesAndGoldResponse(List("is", "it", "tomorrow"), tokenizedCorpus)
    assertEquals(result, Some(List("no", "it", "isn't", "tomorrow", "<EOS>", "it's", "still", "today", "<END>")))
  }

  test("Perplexity.responsePerplexity should return 1.0 for a seen probe with only one possible response") {
    val corpus: List[String] = scala.io.Source
      .fromResource("response-pairs-corpus.csv")
      .getLines()
      .toList
    val tokenizedCorpus      = Tokenizer(corpus).tokenizeCSV
    val P                    = Perplexity(tokenizedCorpus)
    val gold                 = List("we're", "nearly", "there", "<EOS>", "just", "a", "few", "more", "minutes", "<END>")
    val result               = P.responsePerplexity(List("are", "we", "nearly", "there", "yet"), gold, 3)
    assertEquals(result, (Some(1.0), 10, 0))
  }

  test("Perplexity.responsePerplexity should return >1.0 for a seen probe with more than one possible response") {
    val corpus: List[String] = scala.io.Source
      .fromResource("response-pairs-corpus.csv")
      .getLines()
      .toList
    val tokenizedCorpus      = Tokenizer(corpus).tokenizeCSV
    val P                    = Perplexity(tokenizedCorpus)
    val gold                 = List("no", "it", "isn't", "tomorrow", "<EOS>", "it's", "still", "today", "<END>")
    val result               = P.responsePerplexity(List("is", "it", "tomorrow"), gold, 3)
    assertEquals(result, (Some(1.080059738892306),9,0))
  }

}
