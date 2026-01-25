ThisBuild / version      := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "3.3.7"

ThisBuild / scalafmtOnCompile := true

val CatsVersion       = "2.13.0"
val CatsEffectVersion = "3.6.3"
val LogbackVersion    = "1.5.21"
val MunitVersion      = "1.2.1"

lazy val root = (project in file("."))
  .settings(
    name := "toddlerLM",
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-core"       % CatsVersion,
      "org.typelevel" %% "cats-effect"     % CatsEffectVersion,
      "org.scalameta" %% "munit"           % MunitVersion   % Test,
      "ch.qos.logback" % "logback-classic" % LogbackVersion % Runtime
    )
  )
