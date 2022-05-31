import org.scalatest._
import flatspec._
import matchers._
import org.apache.spark.sql.SparkSession

class ReadFromKafkaSpec extends AnyFlatSpec with should.Matchers {

  "ReadFromKafka" should "execute main" in {
    implicit val spark: SparkSession = SparkSession
      .builder()
      .master("local[*]")
      .appName("Spark read JSON from Kafka")
      .getOrCreate()

    import spark.implicits._

    val actual = ReadFromKafka.loadFile("src/test/resources/input.json").as[String].collect()
    actual should not be empty
  }

}
