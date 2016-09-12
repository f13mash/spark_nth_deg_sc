package services

import org.apache.spark.SparkContext
import org.apache.spark.sql._
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.sql.functions.{collect_list, concat, concat_ws, lit, sort_array}

/**
  * Created by mahesh on 10/09/16.
  */
object UserConn {

  case class Connection(user: String, contacts: Array[String])


  val DELIMITER = "\t"
  val USER_FEED = "feed.user.conn"

  var sc: SparkContext = null

  lazy val sql: SQLContext = new SQLContext(sc)

  lazy val userFeed = sql.read
    .format("com.databricks.spark.csv").option("header", false)
    .schema(
      StructType(Array(
        StructField("u1", StringType, false),
        StructField("u2", StringType, false)
      ))
    )
    .option("delimiter", DELIMITER)
    .load(sql.getConf(USER_FEED))

  lazy val users = userFeed.select("u1").union(userFeed.select("u2")).distinct().withColumnRenamed("u1", "connections").orderBy("connections")

  lazy val userConnectionsDegree1 = userFeed.select("u1", "u2")
    .union(userFeed.select("u2", "u1")).distinct().cache()

  def initialize(sc: SparkContext) = {
    this.sc = sc
  }


  def getConnectionWithDeg(degree: Int) = {
    var output:Array[Row]  = null

    if(degree == 0) {
      output = users.collect()
    }
    else {
      var nthDegConnection = userConnectionsDegree1.as("user_nth_conn").withColumnRenamed("u1", "a1").withColumnRenamed("u2", "a2")

      for (i <- (2 to degree)) {
        nthDegConnection = nthDegConnection.join(userConnectionsDegree1, nthDegConnection("a2") === userConnectionsDegree1("u1"))
        nthDegConnection = nthDegConnection.select("a1", "a2")
          .union(nthDegConnection.select("a1", "u2"))
          .distinct()
          .where(nthDegConnection("a1").notEqual(nthDegConnection("a2")))
      }

      output = nthDegConnection.groupBy("a1")
        .agg(concat(nthDegConnection("a1"), lit(DELIMITER), concat_ws(DELIMITER, sort_array(collect_list("a2")))).as("connections"))
        .orderBy("a1")
        .select("connections")
        .collect()
    }

    println(s"\n\n*********** User Connections[Degree = ${degree}] ***********")
    output.foreach(row => println(row.getString(0)))
    println(s"******************** END OUTPUT *********************")
  }

}
