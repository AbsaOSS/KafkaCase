import sbt.*

object Dependencies {
  def commonDependencies: Seq[ModuleID] = Seq(
    "ch.qos.logback" % "logback-classic" % "1.5.6",
  )

  def modelsDependencies: Seq[ModuleID] = Seq.empty[ModuleID]

  def readerDependencies: Seq[ModuleID] = Seq(
    "io.circe" %% "circe-parser" % "0.14.9",
    "org.apache.kafka" % "kafka-clients" % "3.8.0"
  )

  def writerDependencies: Seq[ModuleID] = Seq(
    "io.circe" %% "circe-parser" % "0.14.9",
    "org.apache.kafka" % "kafka-clients" % "3.8.0"
  )

  def examplesDependencies: Seq[ModuleID] = Seq(
    "io.circe" %% "circe-generic" % "0.14.9",
  )
}
