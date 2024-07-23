import Dependencies.*

ThisBuild / scalaVersion     := "3.3.3"
ThisBuild / organization     := "za.co.absa.KafkaCase"

Global / onChangedBuildSource := ReloadOnSourceChanges

lazy val root = (project in file("."))
  .settings(
    name := "KafkaCase",
    libraryDependencies ++= commonDependencies,
    assembly / test := {},
    publish := {},
  )
  .enablePlugins(AssemblyPlugin)
