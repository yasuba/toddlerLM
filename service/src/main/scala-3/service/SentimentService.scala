package service

import cats.effect.*
import cats.syntax.all.*
import client.{LlmClient, SparkClient}
import model.*

trait SentimentService[F[_]] {
  def classify(phrase: String): F[ClassificationResult]
}

object SentimentService {

  def apply[F[_]: Sync](
    sparkClient: SparkClient[F],
    llmClient: LlmClient[F]
//    ragLogger: RagLogger[F]
  ): SentimentService[F] =

    new SentimentService[F] {
      override def classify(phrase: String): F[ClassificationResult] =
        for
        // simple rule: Spark for short phrases, LLM for long/complex
        model  <- decideModel(phrase)
        result <- model match {
                    case "spark" => sparkClient.classify(phrase)
                    case "llm" => llmClient.classify(phrase)
                  }
//        _      <- ragLogger.logPhrase(phrase, result) // <- logging for Phase 2
        cResult = ClassificationResult(result, "", 0, "", None)
        yield cResult

      private def decideModel(phrase: String): F[String] =
        if phrase.length < 20 then "spark".pure[F]
        else "llm".pure[F]
    }
}
