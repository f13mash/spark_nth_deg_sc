name := "soundcloud_1"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies += "org.apache.spark" % "spark-core_2.11" % "2.0.0-preview" force()
libraryDependencies += "org.apache.spark" % "spark-sql_2.11" % "2.0.0-preview" force()
libraryDependencies += "com.databricks" % "spark-csv_2.11" % "1.4.0"
libraryDependencies += "commons-io" % "commons-io" % "2.5"
libraryDependencies += "com.google.inject" % "guice" % "3.0"


resolvers += "Spark Packages Repo" at "http://dl.bintray.com/spark-packages/maven"
