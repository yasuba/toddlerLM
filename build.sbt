ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.6"

val sparkVersion = "4.0.0"

lazy val root = (project in file("."))
  .settings(
    name := "sparkle",
    libraryDependencies ++= Seq(
      ("org.apache.spark" %% "spark-core" % sparkVersion).cross(CrossVersion.for3Use2_13),
      ("org.apache.spark" %% "spark-sql" % sparkVersion).cross(CrossVersion.for3Use2_13),
      // Add other Spark modules as needed, e.g., spark-mllib
      ("org.apache.spark" %% "spark-mllib" % sparkVersion).cross(CrossVersion.for3Use2_13)
    )
  )
