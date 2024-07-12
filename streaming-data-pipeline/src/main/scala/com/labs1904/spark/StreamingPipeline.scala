package com.labs1904.spark

import org.apache.hadoop.hbase.client.{ConnectionFactory, Get}
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hbase.{HBaseConfiguration, TableName}
import org.apache.log4j.Logger
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.streaming.{OutputMode, Trigger}

/**
 * Spark Structured Streaming app
 *
 */
object StreamingPipeline {
  lazy val logger: Logger = Logger.getLogger(this.getClass)
  val jobName = "StreamingPipeline"

  case class Review(marketplace:String,	customer_id: String,	review_id:String,	product_id:String,	product_parent:String,	product_title:String,	product_category: String,	star_rating: Int,	helpful_votes: Long,	total_votes: Long,	vine: String, verified_purchase: String,	review_headline: String, review_body: String,	review_date: String)
  case class JoinedReview(marketplace:String,	customer_id: String,	review_id:String,	product_id:String,	product_parent:String,	product_title:String,	product_category: String,	star_rating: Int,	helpful_votes: Long,	total_votes: Long,	vine: String, verified_purchase: String,	review_headline: String, review_body: String,	review_date: String,	birthdate: String,	mail: String,	name: String,	sex: String,	username: String)

  implicit def stringToBytes(str:String):Array[Byte] = Bytes.toBytes(str)
  implicit def bytesToString(bytes: Array[Byte]): String = Bytes.toString(bytes)
  //var review: StructType = Encoders.product[Review].schema


  def main(args: Array[String]): Unit = {
    try {
      val spark = SparkSession.builder()
        .config("spark.hadoop.dfs.client.use.datanode.hostname", "true")
        .config("spark.hadoop.fs.defaultFS", "hdfs://manager.hourswith.expert:8020")
        .appName(jobName).master("local[*]").getOrCreate()

      val bootstrapServers = "cdh01.hourswith.expert:9092,cdh02.hourswith.expert:9092,cdh03.hourswith.expert:9092"

      import spark.implicits._

      val ds = spark
        .readStream
        .format("kafka")
        .option("kafka.bootstrap.servers", bootstrapServers)
        .option("subscribe", "reviews")
        .option("startingOffsets", "earliest")
        .option("maxOffsetsPerTrigger", "20")
        .load()
        .selectExpr("CAST(value AS STRING)").as[String]

      ds.printSchema()
      val wordPile = ds.flatMap(row =>{row.split("\t")})//.map(word => word.replaceAll("[^A-Za-z]", "")).map(word => word.toLowerCase())

      val reviews = ds.map(unparsedReview => {
        val csvArray = unparsedReview.split("\t")
        Review(csvArray(0),csvArray(1), csvArray(2), csvArray(3), csvArray(4), csvArray(5), csvArray(6), csvArray(7).toInt, csvArray(8).toLong, csvArray(9).toLong, csvArray(10), csvArray(11), csvArray(12), csvArray(13), csvArray(14),
        )
      })

     // val customerIds = reviews.map(review => review.customer_id)

      val customers = reviews.mapPartitions(partition=> {
        val conf = HBaseConfiguration.create()
        conf.set("hbase.zookeeper.quorum", "cdh01.hourswith.expert:2181,cdh02.hourswith.expert:2181,cdh03.hourswith.expert:2181")
        val connection = ConnectionFactory.createConnection(conf)
        val table = connection.getTable(TableName.valueOf("ieschbacher:users"))

        val iter = partition.map(review => {

          val get = new Get(Bytes.toBytes(review.customer_id)).addFamily(Bytes.toBytes("f1"))
          val result = table.get(get)
          logger.debug(result)
          JoinedReview(review.marketplace,	review.customer_id,	review.review_id,	review.product_id,	review.product_parent,	review.product_title,	review.product_category,	review.star_rating,	review.helpful_votes,	review.total_votes,	review.vine, review.verified_purchase,	review.review_headline, review.review_body,	review.review_date, result.getValue(Bytes.toBytes("f1"), Bytes.toBytes("birthdate")), result.getValue(Bytes.toBytes("f1"), Bytes.toBytes("mail")), result.getValue(Bytes.toBytes("f1"), Bytes.toBytes("name")), result.getValue(Bytes.toBytes("f1"), Bytes.toBytes("sex")), result.getValue(Bytes.toBytes("f1"), Bytes.toBytes("username")))
        }).toList.iterator

        connection.close()

        iter
      })




      val query = customers.writeStream
        .outputMode(OutputMode.Append())
        .format("json")
        .option("path", "/user/ieschbacher/reviews")
        .option("checkpointLocation", "/user/ieschbacher/reviews_checkpoint")
        .trigger(Trigger.ProcessingTime("5 seconds"))
        .start()

      query.awaitTermination()
    } catch {
      case e: Exception => logger.error(s"$jobName error in main", e)
    }
  }
}
