/*
 * Copyright 2024 ABSA Group Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import Dependencies.*

lazy val scala_2_12 = "2.12.19"
lazy val scala_2_13 = "2.13.14"

ThisBuild / scalaVersion := scala_2_13

Global / onChangedBuildSource := ReloadOnSourceChanges

val mergeStrategy = assembly / assemblyMergeStrategy := {
  case PathList("module-info.class") => MergeStrategy.discard
  case _ => MergeStrategy.first
}

lazy val commonSettings = Seq(
  assembly / test := {},
  publish := {},
  libraryDependencies ++= commonDependencies,
  crossScalaVersions := Seq(scala_2_12, scala_2_13)
)

lazy val models = project
  .disablePlugins(sbtassembly.AssemblyPlugin)
  .settings(
    commonSettings ++ Seq(
      name := "KafkaCase-Models",
      libraryDependencies ++= modelsDependencies
    )
  )
  .enablePlugins(AutomateHeaderPlugin)

lazy val reader = project
  .disablePlugins(sbtassembly.AssemblyPlugin)
  .settings(
    commonSettings ++ Seq(
      name := "KafkaCase-Reader",
      libraryDependencies ++= readerDependencies
    )
  )
  .enablePlugins(AutomateHeaderPlugin)

lazy val writer = project
  .disablePlugins(sbtassembly.AssemblyPlugin)
  .settings(
    commonSettings ++ Seq(
      name := "KafkaCase-Writer",
      libraryDependencies ++= writerDependencies
    )
  )
  .enablePlugins(AutomateHeaderPlugin)

lazy val examples = project
  .settings(
    commonSettings ++ Seq(
      name := "KafkaCase-Examples",
      libraryDependencies ++= examplesDependencies,
      mergeStrategy
    )
  )
  .enablePlugins(AutomateHeaderPlugin)
  .dependsOn(models, reader, writer)
