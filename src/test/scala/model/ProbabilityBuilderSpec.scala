package model

import model.TestData.contexts
import munit.FunSuite

class ProbabilityBuilderSpec extends FunSuite {

  test("ContextAnalyzer can calculate the probability of next tokens occurring for any given context") {
    val prob = ProbabilityBuilder.calculateProbabilities(contexts)

    val obtained = prob.filter(p => p._2.size == 4)

    val expected =
      Map(
        Context(List("love")) -> Map("mummy" -> 0.2222222222222222, "my" -> 0.4444444444444444, "ice" -> 0.1111111111111111, "daddy" -> 0.2222222222222222),
        Context(List("the"))  -> Map("big" -> 0.25, "park!" -> 0.25, "bed" -> 0.25, "ground" -> 0.25),
        Context(List("feel")) -> Map("poorly" -> 0.25, "happy" -> 0.25, "better" -> 0.25, "sad" -> 0.25)
      )

    assertEquals(obtained, expected)
  }

}
