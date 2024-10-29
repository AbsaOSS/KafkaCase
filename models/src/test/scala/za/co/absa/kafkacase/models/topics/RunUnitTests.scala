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

class RunUnitTests extends AnyFunSuite {
  private val instance =  Run(
    event_id = "TestId",
    job_ref = "TestJob",
    tenant_id = "TestTenant",
    source_app = "UnitTestSrc",
    source_app_version = "v9000",
    environment = "UnitTestEnv",
    timestamp_start = 12345,
    timestamp_end = 6789,
    jobs = Seq(Run.Job(
      catalog_id = "FooId",
      status = Run.Status.Killed(),
      timestamp_start = 12346,
      timestamp_end = 6788,
      message = "TestingLikeCrazy"
  ))
  )

  private val json =
    """{
      |  "event_id" : "TestId",
      |  "job_ref" : "TestJob",
      |  "tenant_id" : "TestTenant",
      |  "source_app" : "UnitTestSrc",
      |  "source_app_version" : "v9000",
      |  "environment" : "UnitTestEnv",
      |  "timestamp_start" : 12345,
      |  "timestamp_end" : 6789,
      |  "jobs" : [
      |    {
      |      "catalog_id" : "FooId",
      |      "status" : "killed",
      |      "timestamp_start" : 12346,
      |      "timestamp_end" : 6788,
      |      "message" : "TestingLikeCrazy"
      |    }
      |  ]
      |}""".stripMargin

  test("Serializes to JSON properly") {
    assertResult(json)(instance.asJson.toString())
  }

  test("Deserializes from JSON properly") {
    assertResult(instance)(decode[Run](json).getOrElse(throw new Exception("Failed to parse JSON")))
  }
}
