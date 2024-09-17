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

package za.co.absa.KafkaCase.models

import io.circe.jawn.decode
import io.circe.syntax.EncoderOps
import org.scalatest.funsuite.AnyFunSuite
import za.co.absa.KafkaCase.Models.SchemaRunTopic

class SchemaRunTopicUnitTests extends AnyFunSuite {
  private val instance =  SchemaRunTopic(
    id = "DebugId",
    job_ref = "UnitTestJob",
    app_id_snow = "N/A",
    source_app = "ThisTest",
    environment = "TEST",
    timestamp_start = 12345,
    timestamp_end = 67890,
    data_definition_id = "Foo",
    status = SchemaRunTopic.Status.Killed(),
    message = "FooBar"
  )

  private val json =
    """{
      |  "id" : "DebugId",
      |  "job_ref" : "UnitTestJob",
      |  "app_id_snow" : "N/A",
      |  "source_app" : "ThisTest",
      |  "environment" : "TEST",
      |  "timestamp_start" : 12345,
      |  "timestamp_end" : 67890,
      |  "data_definition_id" : "Foo",
      |  "status" : "Killed",
      |  "message" : "FooBar"
      |}""".stripMargin

  test("Serializes to JSON properly") {
    assertResult(json)(instance.asJson.toString())
  }

  test("Deserializes from JSON properly") {
    assertResult(instance)(decode[SchemaRunTopic](json).getOrElse(throw new Exception("Failed to parse JSON")))
  }
}
