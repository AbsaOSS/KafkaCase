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

package za.co.absa.KafkaCase.Writer

import io.circe.Encoder
import io.circe.syntax.*
import org.slf4j.LoggerFactory
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import za.co.absa.KafkaCase.Writer.WriterImpl.log

import java.util.Properties

class WriterImpl[TType: Encoder](props: Properties, topic: String) extends Writer[TType] {
  private val producer = new KafkaProducer[String, String](props)

  def Write(key: String, value: TType): Unit = {
    log.info(f"Writing: $key => $value")
    producer.send(new ProducerRecord[String, String](topic, key, value.asJson.noSpaces))
  }
  
  def Flush(): Unit = producer.flush()

  override def close(): Unit = producer.close()
}

object WriterImpl {
  private val log = LoggerFactory.getLogger(this.getClass)
}
