import sbt._

val akkaV = "2.4.17"

lazy val root = (project in file(".")).
  settings(
    name                 := "testing",
    scalaVersion         := "2.11.8",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-persistence-cassandra" % "0.23",
      "com.typesafe.akka" %% "akka-persistence-query-experimental" % akkaV
    )
  )
