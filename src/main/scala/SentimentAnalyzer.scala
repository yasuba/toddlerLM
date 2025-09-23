import org.apache.spark.ml.classification.NaiveBayes
import org.apache.spark.ml.feature.{HashingTF, StopWordsRemover, StringIndexer, StringIndexerModel, Tokenizer}
import org.apache.spark.ml.{Pipeline, PipelineModel}
import org.apache.spark.sql.{DataFrame, SparkSession}


object SentimentAnalyzer {

  private val sparkSession: SparkSession = SparkSession.builder().master("local[*]").appName("sparkle").getOrCreate()

  private val trainingData: DataFrame = sparkSession.read.option("header", "true").csv("src/main/resources/sentiments.csv")
  private val testData: DataFrame     = sparkSession.read.option("header", "true").csv("src/main/resources/testData.csv")

  private val tokenizer: Tokenizer               = Tokenizer().setInputCol("sentence").setOutputCol("words")
  private val stopWordsRemover: StopWordsRemover = StopWordsRemover().setInputCol("words").setOutputCol("sandyzedWords")
  private val hashingTF: HashingTF               = HashingTF().setInputCol("sandyzedWords").setOutputCol("hashedWords")
  private val labelIndexer                       = new StringIndexer().setInputCol("sentiment").setOutputCol("label")
  private val nb: NaiveBayes                     = new NaiveBayes().setLabelCol("label").setFeaturesCol("hashedWords")


  private val pipeline: Pipeline     = new Pipeline().setStages(Array(tokenizer, stopWordsRemover, hashingTF, labelIndexer, nb))
  val model: PipelineModel           = pipeline.fit(trainingData)
//  private val predictions: DataFrame = model.transform(testData)

  private val labels: Array[String]  = model.stages(3).asInstanceOf[StringIndexerModel].labelsArray.flatten

  def main(args: Array[String]): Unit = {
    import sparkSession.implicits._

    val howAreYou = scala.io.StdIn.readLine("How are you today? ")
    
    val df = Seq(howAreYou).toDF("sentence")

    val predictions: DataFrame = model.transform(df)

    predictions.select("sentence", "prediction").collect().foreach { row =>
      val sentence = row.getString(0)
      val predIdx = row.getDouble(1).toInt
      val label = labels(predIdx)
      println(s"Sentence: $sentence, Predicted Sentiment: $label")
    }
  }
}