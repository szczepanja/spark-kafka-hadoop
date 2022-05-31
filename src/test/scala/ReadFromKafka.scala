import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.DataFrame

object ReadFromKafka {
  def main(args: Array[String]): Unit = {

    val topic = if (args.length > 0 ) args(0)
    else "input"

    implicit val spark: SparkSession = SparkSession
      .builder()
      .appName("Spark read JSON from Kafka")
      .getOrCreate()

    val records: DataFrame = spark
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", ":9092")
      .option("subscribe", topic)
      .load()

    import spark.implicits._

    val newValue = records.select('value cast "string")

    newValue
      .writeStream
      .format("console")
      .option("truncate", value = false)
      .start()
      .awaitTermination()
  }

  def loadFile(path: String)(implicit spark: SparkSession): DataFrame = {
    spark
      .read
      .text(path)
  }
}
