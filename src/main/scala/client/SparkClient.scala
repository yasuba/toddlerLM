package client

import cats.Monad
import cats.implicits._
import model.ClassificationResult
import org.apache.spark.ml.classification.{LogisticRegression, NaiveBayes}
import org.apache.spark.ml.feature._
import org.apache.spark.ml.{Pipeline, PipelineModel}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{DataFrame, SparkSession}

trait SparkClient[F[_]] {
  def classify(phrase: String): F[Option[ClassificationResult]]
}

object SparkClient {

  private val sparkSession: SparkSession = SparkSession
    .builder()
    .master("local")
    .appName("sparkle")
    .config("spark.driver.bindAddress", "127.0.0.1")
    .config("spark.driver.host", "127.0.0.1")
    .getOrCreate()

  sparkSession.sparkContext.setLogLevel("ERROR")

  private val trainingData: DataFrame = sparkSession.read.option("header", "true").csv("src/main/resources/sentiments.csv")
  private val testData: DataFrame     = sparkSession.read.option("header", "true").csv("src/main/resources/testData.csv")

  private val tokenizer: Tokenizer               = new Tokenizer().setInputCol("sentence").setOutputCol("words")
  private val stopWordsRemover: StopWordsRemover = new StopWordsRemover().setInputCol("words").setOutputCol("sandyzedWords")
  private val hashingTF: HashingTF               = new HashingTF().setInputCol("sandyzedWords").setOutputCol("hashedWords")
  private val labelIndexer                       = new StringIndexer().setInputCol("sentiment").setOutputCol("label")
  private val nb: NaiveBayes                     = new NaiveBayes().setLabelCol("label").setFeaturesCol("hashedWords")
  private val lr: LogisticRegression             = new LogisticRegression().setLabelCol("label").setFeaturesCol("hashedWords")

  val wordsDF = trainingData.withColumn("word", explode(split(lower(col("sentence")), "\\s+"))).select("sentiment", "word")

//  val mostCommonWords = wordsDF.groupBy("sentiment", "word").count().orderBy(desc("count")).toJSON
//
//  val sharedWordsPerSentiment =
//    wordsDF
//      .groupBy("word")
//      .agg(
//        countDistinct("sentiment")
//          .as("sentiment_count"),
//        count("*").as("total_occurrences")
//      )
//      .filter(col("sentiment_count") === 2)
//      .orderBy(desc("total_occurrences"))
//      .toJSON
//
//  val uniqueWordsPerSentiment =
//    wordsDF
//      .groupBy("sentiment")
//      .agg(
//        countDistinct("word").as("unique_words")
//      )
//      .toJSON

  private val pipeline: Pipeline = new Pipeline().setStages(Array(tokenizer, stopWordsRemover, hashingTF, labelIndexer, lr))
  val model: PipelineModel       = pipeline.fit(trainingData)

  private val labels: Array[String] = model.stages(3).asInstanceOf[StringIndexerModel].labelsArray.flatten

  def apply[F[_]: Monad](): SparkClient[F] =
    new SparkClient[F] {

      import sparkSession.implicits._

      override def classify(phrase: String): F[Option[ClassificationResult]] = {
        val df                     = Seq(phrase).toDF("sentence")
        val predictions: DataFrame = model.transform(df)

        import org.apache.spark.ml.linalg.Vector
        val results: Array[(String, String, Double)] = predictions
          .select("sentence", "prediction", "probability")
          .collect()
          .map { row =>
            val sentence    = row.getString(0)
            val predIdx     = row.getDouble(1).toInt
            val label       = labels(predIdx)
            val probability = row.getAs[Vector](2)
            val confidence  = probability(predIdx)
            (sentence, label, confidence)
          }

        val resultOpt = results.headOption.map { sentenceAndLabelAndConfidence =>
          ClassificationResult(
            sentenceAndLabelAndConfidence._1,
            sentenceAndLabelAndConfidence._2,
            sentenceAndLabelAndConfidence._3,
            "Spark ML",
            None
          )
        }
        resultOpt.pure[F]
      }
    }
}
