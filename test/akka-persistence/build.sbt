import sbt._

val akkaV = "2.5.1"

lazy val root = (project in file(".")).
  settings(
    name                 := "testing",
    scalaVersion         := "2.11.8",
    scalacOptions        ++= Seq("-encoding", "UTF-8", "-deprecation", "-feature", "-unchecked", "-language:postfixOps", "-language:implicitConversions", "-Dproperty=true"),
    libraryDependencies  ++= Seq(
      "com.typesafe.akka" %% "akka-persistence-cassandra" % "0.52",
      "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",
      "com.typesafe.akka" %% "akka-persistence-query" % akkaV,
      "ch.qos.logback"    % "logback-classic"             % "1.1.7",
      "org.scalatest"     % "scalatest_2.11"              % "3.0.1" % "test"
    ),
    cancelable in Global := true
  )
