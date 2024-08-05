import Dependencies.*

ThisBuild / scalaVersion     := "3.3.3"
ThisBuild / organization     := "za.co.absa.KafkaCase"

Global / onChangedBuildSource := ReloadOnSourceChanges

lazy val commonSettings = Seq(
  assembly / test := {},
  publish := {},
  libraryDependencies ++= commonDependencies,
)

lazy val models = project
  .settings(
    commonSettings ++ Seq(
      name := "KafkaCase-Models",
      libraryDependencies ++= modelsDependencies
    )
  )

lazy val reader = project
  .settings(
    commonSettings ++ Seq(
      name := "KafkaCase-Reader",
      libraryDependencies ++= readerDependencies
    )
  )

lazy val writer = project
  .settings(
    commonSettings ++ Seq(
      name := "KafkaCase-Writer",
      libraryDependencies ++= writerDependencies
    )
  )

lazy val examples = project
  .settings(
    commonSettings ++ Seq(
      name := "KafkaCase-Examples",
      libraryDependencies ++= examplesDependencies
    )
  )
  .dependsOn(models, reader, writer)
