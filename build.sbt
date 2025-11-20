ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.6"

ThisBuild / scalafmtOnCompile := true

val CirceVersion   = "0.14.15"
val CirisVersion   = "3.11.1"
val Http4sVersion  = "0.23.33"
val LogbackVersion = "1.5.21"
val MunitVersion   = "1.2.1"
val SparkVersion   = "4.0.0"

lazy val root = (project in file("."))
  .settings(name := "sparkle")
  .aggregate(service, spark)

lazy val spark   = (project in file("spark"))
  .settings(
    libraryDependencies ++= Seq(
      ("org.apache.spark" %% "spark-core"  % SparkVersion).cross(CrossVersion.for3Use2_13),
      ("org.apache.spark" %% "spark-mllib" % SparkVersion).cross(CrossVersion.for3Use2_13),
      ("org.apache.spark" %% "spark-sql"   % SparkVersion).cross(CrossVersion.for3Use2_13)
    )
  )

lazy val service = (project in file("service"))
  .settings(
    libraryDependencies ++= Seq(
      "io.circe"      %% "circe-generic"       % CirceVersion,
      "io.circe"      %% "circe-parser"        % CirceVersion,
      "is.cir"        %% "ciris"               % CirisVersion,
      "org.http4s"    %% "http4s-circe"        % Http4sVersion,
      "org.http4s"    %% "http4s-dsl"          % Http4sVersion,
      "org.http4s"    %% "http4s-ember-client" % Http4sVersion,
      "org.http4s"    %% "http4s-ember-server" % Http4sVersion,
      "org.scalameta" %% "munit"               % MunitVersion   % Test,
      "ch.qos.logback" % "logback-classic"     % LogbackVersion % Runtime
    )
  )
