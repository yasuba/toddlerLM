package client

trait SparkClient[F[_]] {
  def classify(phrase: String): F[String]
}

object SparkClient {
  def apply[F[_]](): SparkClient[F] =
    new SparkClient[F] {
      override def classify(phrase: String): F[String] =
        ???
    }
}
