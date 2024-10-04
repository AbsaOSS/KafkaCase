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

package za.co.absa.kafkacase.models.utils

import scala.util.control.NonFatal

// Inspired by https://dkomanov.medium.com/scala-try-with-resources-735baad0fd7d
//      and for-comprehension contract by ATUM's ARMImplits
object ResourceHandler {
  def withResource[TResource <: AutoCloseable, TResult](resourceFactory: => TResource)(operation: TResource => TResult): TResult = {
    val resource: TResource = resourceFactory
    require(resource != null, "resource is null")
    var exception: Throwable = null
    try {
      operation(resource)
    } catch {
      case NonFatal(ex) =>
        exception = ex
        throw ex
    } finally {
      closeAndAddSuppressed(exception, resource)
    }
  }

  private def closeAndAddSuppressed(ex: Throwable, resource: AutoCloseable): Unit = {
    if (ex != null) {
      try {
        resource.close()
      } catch {
        case NonFatal(suppressed) =>
          ex.addSuppressed(suppressed)
      }
    } else {
      resource.close()
    }
  }

  // implementing a for-comprehension contract inspired by ATUM's ARMImplits
  implicit class ResourceWrapper[TResource <: AutoCloseable](resourceFactory: => TResource) {
    def foreach(operation: TResource => Unit): Unit = withResource(resourceFactory)(operation)

    def map[TResult](operation: TResource => TResult): TResult = withResource(resourceFactory)(operation)

    def flatMap[TResult](operation: TResource => TResult): TResult = withResource(resourceFactory)(operation)

    def withFilter(ignored: TResource => Boolean): ResourceWrapper[TResource] = this
  }
}
