import sbt.*

object Dependencies {
  def commonDependencies: Seq[ModuleID] = Seq(
    "ch.qos.logback" % "logback-classic" % "1.5.6",
    "io.circe" %% "circe-generic" % "0.14.9",
    "io.circe" %% "circe-parser" % "0.14.9",
    "org.apache.kafka" % "kafka-clients" % "3.7.1"
  )
}
