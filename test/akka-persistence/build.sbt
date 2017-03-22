import sbt._

val akkaV = "2.4.17"

lazy val root = (project in file(".")).
  settings(
    name                 := "testing",
    scalaVersion         := "2.11.8",
    scalacOptions        ++= Seq("-encoding", "UTF-8", "-deprecation", "-feature", "-unchecked", "-language:postfixOps", "-language:implicitConversions"),
    libraryDependencies  ++= Seq(
      "com.typesafe.akka" %% "akka-persistence-cassandra" % "0.23",
      "com.typesafe.akka" %% "akka-persistence-query-experimental" % akkaV,
      "ch.qos.logback"    % "logback-classic"              % "1.1.7"
    ),
    fork in run := true
  )
