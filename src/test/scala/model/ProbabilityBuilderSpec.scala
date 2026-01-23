package model

import model.TestData.contexts
import munit.FunSuite

class ProbabilityBuilderSpec extends FunSuite {

  test("ContextAnalyzer can calculate the probability of next tokens occurring for any given context") {
    val prob = ProbabilityBuilder.calculateProbabilities(contexts)

    val obtained = prob.filter(p => p._2.size > 2 && p._2.size < 4)

    val expected =
      Map(
        Context(List("and")) -> Map("ball" -> 0.3333333333333333, "scraped" -> 0.3333333333333333, "glueing" -> 0.3333333333333333),
        Context(List("My"))  -> Map("dog" -> 0.3333333333333333, "ice" -> 0.3333333333333333, "biscuit's" -> 0.3333333333333333),
        Context(List("not")) -> Map("very" -> 0.3333333333333333, "ok" -> 0.3333333333333333, "changing" -> 0.3333333333333333)
      )

    assertEquals(obtained, expected)
  }

}
