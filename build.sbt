ThisBuild / version := "0.1.0"
ThisBuild / scalaVersion := "2.13.10"

lazy val root = (project in file("."))
  .settings(
    name := "ForestKafka"
  )

libraryDependencies ++= Seq(
  "org.apache.kafka" % "kafka-clients" % "3.4.0",
  "org.mongodb.scala" %% "mongo-scala-driver" % "4.9.1",
  "org.typelevel" %% "cats-effect" % "3.4.11"
) 
