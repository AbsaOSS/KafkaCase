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

package za.co.absa.kafkacase.models.topics

import io.circe.jawn.decode
import io.circe.syntax.EncoderOps
import org.scalatest.funsuite.AnyFunSuite

class SchemaRunUnitTests extends AnyFunSuite {
  private val instance =  SchemaRun(
    app_id_snow = "N/A",
    data_definition_id = "Foo",
    environment = "TEST",
    guid = "DebugId",
    job_ref = "UnitTestJob",
    message = "FooBar",
    source_app = "ThisTest",
    status = SchemaRun.Status.Killed(),
    timestamp_end = 67890,
    timestamp_start = 12345
  )

  private val json =
    """{
      |  "app_id_snow" : "N/A",
      |  "data_definition_id" : "Foo",
      |  "environment" : "TEST",
      |  "guid" : "DebugId",
      |  "job_ref" : "UnitTestJob",
      |  "message" : "FooBar",
      |  "source_app" : "ThisTest",
      |  "status" : "Killed",
      |  "timestamp_end" : 67890,
      |  "timestamp_start" : 12345
      |}""".stripMargin

  test("Serializes to JSON properly") {
    assertResult(json)(instance.asJson.toString())
  }

  test("Deserializes from JSON properly") {
    assertResult(instance)(decode[SchemaRun](json).getOrElse(throw new Exception("Failed to parse JSON")))
  }
}
