package service

import cats.effect._
import cats.syntax.all._
import client.{LlmClient, SparkClient}
import model.ClassificationResult

trait SentimentService[F[_]] {
  def classify(phrase: String): F[Option[ClassificationResult]]
}

object SentimentService {

  def apply[F[_]: Sync](
    sparkClient: SparkClient[F],
    llmClient: LlmClient[F]
//    ragLogger: RagLogger[F]
  ): SentimentService[F] =

    new SentimentService[F] {
      override def classify(phrase: String): F[Option[ClassificationResult]] =
        for {
          model  <- decideModel(phrase)
          result <- model match {
                      case "spark" => sparkClient.classify(phrase)
                      case "llm"   => llmClient.classify(phrase)
                    }
          //        _      <- ragLogger.logPhrase(phrase, result) // <- logging for Phase 2
        } yield result

      private def decideModel(phrase: String): F[String] =
        if (phrase.length < 20) "spark".pure[F]
        else "llm".pure[F]
    }
}
