import org.apache.spark.ml.classification.{LogisticRegression, NaiveBayes}
import org.apache.spark.ml.feature.*
import org.apache.spark.ml.{Pipeline, PipelineModel}
import org.apache.spark.sql.functions.*
import org.apache.spark.sql.{DataFrame, SparkSession}

object SentimentAnalyzer {

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

  private val tokenizer: Tokenizer               = Tokenizer().setInputCol("sentence").setOutputCol("words")
  private val stopWordsRemover: StopWordsRemover = StopWordsRemover().setInputCol("words").setOutputCol("sandyzedWords")
  private val hashingTF: HashingTF               = HashingTF().setInputCol("sandyzedWords").setOutputCol("hashedWords")
  private val labelIndexer                       = new StringIndexer().setInputCol("sentiment").setOutputCol("label")
  private val nb: NaiveBayes                     = new NaiveBayes().setLabelCol("label").setFeaturesCol("hashedWords")
  private val lr: LogisticRegression             = new LogisticRegression().setLabelCol("label").setFeaturesCol("hashedWords")

  val wordsDF = trainingData.withColumn("word", explode(split(lower(col("sentence")), "\\s+"))).select("sentiment", "word")

  val mostCommonWords = wordsDF.groupBy("sentiment", "word").count().orderBy(desc("count")).toJSON

  val sharedWordsPerSentiment =
    wordsDF
      .groupBy("word")
      .agg(
        countDistinct("sentiment")
          .as("sentiment_count"),
        count("*").as("total_occurrences")
      )
      .filter(col("sentiment_count") === 2)
      .orderBy(desc("total_occurrences"))
      .toJSON

  val uniqueWordsPerSentiment =
    wordsDF
      .groupBy("sentiment")
      .agg(
        countDistinct("word").as("unique_words")
      )
      .toJSON

  private val pipeline: Pipeline = new Pipeline().setStages(Array(tokenizer, stopWordsRemover, hashingTF, labelIndexer, lr))
  val model: PipelineModel       = pipeline.fit(trainingData)
  //  private val predictions: DataFrame = model.transform(testData)

  private val labels: Array[String] = model.stages(3).asInstanceOf[StringIndexerModel].labelsArray.flatten

  def main(args: Array[String]): Unit = {
    import sparkSession.implicits.*

    val howAreYou = scala.io.StdIn.readLine("How are you today? ")

    val df = Seq(howAreYou).toDF("sentence")

    val predictions: DataFrame = model.transform(df)

    val results: Array[(String, String)] = predictions.select("sentence", "prediction").collect().map { row =>
      val sentence = row.getString(0)
      val predIdx  = row.getDouble(1).toInt
      val label    = labels(predIdx)
      (sentence, label)
    }
    results.headOption.foreach { (sentence, label) =>
      // TODO select a response based first on label, then on types of words used in sentence - match a similar response from a selection or generate one somehow?
      println(s"Sentence: $sentence, Predicted Sentiment: $label")
      mostCommonWords.foreach(j => println(s"Most popular words are $j"))
      sharedWordsPerSentiment.foreach(j => println(s"Shared words are $j"))
      uniqueWordsPerSentiment.foreach(j => println(s"Unique words are $j"))
    }
  }
}

/*
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.streaming._

val spark = SparkSession.builder()
  .appName("Live Toddler Sentiment Stream")
  .master("local[*]")
  .getOrCreate()

// Read from a directory as a stream
val streamingDF = spark.readStream
  .format("text")  // or "csv" if you want structured data
  .option("maxFilesPerTrigger", 1)  // process one file at a time
  .load("path/to/input/directory")

// Apply your trained model to the streaming data
// (assuming you've already trained and saved your pipeline)
val pipelineModel = PipelineModel.load("path/to/saved/model")

val predictions = pipelineModel.transform(streamingDF)
 */
