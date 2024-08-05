ThisBuild / organization     := "za.co.absa.KafkaCase"

publish / skip := true // skipping publishing of the root of the project, publishing only some submodules

ThisBuild / organizationName := "ABSA Group Limited"
ThisBuild / organizationHomepage := Some(url("https://www.absa.africa"))
ThisBuild / description := "Adapter for communicating from Scala with Kafka via case classes"
ThisBuild / startYear := Some(2024)
ThisBuild / licenses += "Apache-2.0" -> url("https://www.apache.org/licenses/LICENSE-2.0.txt")
ThisBuild / homepage := Some(url("https://github.com/AbsaOSS/KafkaCase"))