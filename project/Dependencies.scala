import sbt.*

object Dependencies {
  def commonDependencies: Seq[ModuleID] = Seq(
    "ch.qos.logback" % "logback-classic" % "1.5.6",
    "org.apache.kafka" % "kafka-clients" % "3.7.1"
  )
}
