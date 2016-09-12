import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by mahesh on 10/09/16.
  */
object Main extends App{
  Logger.getLogger("org").setLevel(Level.OFF)
  Logger.getLogger("akka").setLevel(Level.OFF)

  if(args.length != 2) {
    println("Usage `sbt \"run full_path_to_feed_file connection_degree\"`")
    throw new Error("Expected 2 arguments.")
  }

  val APP_NAME = "SoundCloud"
  val APP_MASTER = "local[*]"
  val FEED_USER_CONN = args(0)
  val CONNECTION_DEGREE = Integer.parseInt(args(1))
  //setup spark conf
  val conf = new SparkConf().setMaster(APP_MASTER).setAppName(APP_NAME).set("feed.user.conn", FEED_USER_CONN)
  val sc = new SparkContext(conf)

  services.UserConn.initialize(sc)
  services.UserConn.getConnectionWithDeg(CONNECTION_DEGREE)
}
