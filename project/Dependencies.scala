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
import sbt.*

object Dependencies {
  def commonDependencies: Seq[ModuleID] = Seq(
    "ch.qos.logback" % "logback-classic" % "1.5.6",
  )

  def modelsDependencies: Seq[ModuleID] = Seq(
    "io.circe" %% "circe-generic" % "0.14.9",
    "io.circe" %% "circe-parser" % "0.14.9" % Test,
    "org.scalatest" %% "scalatest" % "3.2.19" % Test
  )

  def readerDependencies: Seq[ModuleID] = Seq(
    "io.circe" %% "circe-parser" % "0.14.9",
    "org.apache.kafka" % "kafka-clients" % "3.8.0",
    "com.typesafe" % "config" % "1.4.3"
  )

  def writerDependencies: Seq[ModuleID] = Seq(
    "io.circe" %% "circe-parser" % "0.14.9",
    "org.apache.kafka" % "kafka-clients" % "3.8.0",
    "com.typesafe" % "config" % "1.4.3"
  )

  def examplesDependencies: Seq[ModuleID] = Seq.empty[ModuleID]
}
