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

class EdlaChangeUnitTests extends AnyFunSuite {
  private val instance =  EdlaChange(
    event_id = "TestEventId",
    tenant_id = "TestTenantId",
    source_app = "TestSrc",
    source_app_version = "v9000",
    environment = "UnitTestEnv",
    timestamp_event = 12345,
    catalog_id = "TestCatalog",
    operation = EdlaChange.Operation.Delete(),
    location = "UnitTest",
    format = "TestFormat",
    formatOptions = Map("Foo" -> "Bar")
  )

  private val json =
    """{
      |  "event_id" : "TestEventId",
      |  "tenant_id" : "TestTenantId",
      |  "source_app" : "TestSrc",
      |  "source_app_version" : "v9000",
      |  "environment" : "UnitTestEnv",
      |  "timestamp_event" : 12345,
      |  "catalog_id" : "TestCatalog",
      |  "operation" : "delete",
      |  "location" : "UnitTest",
      |  "format" : "TestFormat",
      |  "formatOptions" : {
      |    "Foo" : "Bar"
      |  }
      |}""".stripMargin

  test("Serializes to JSON properly") {
   assertResult(json)(instance.asJson.toString())
  }

  test("Deserializes from JSON properly") {
    assertResult(instance)(decode[EdlaChange](json).getOrElse(throw new Exception("Failed to parse JSON")))
  }
}
