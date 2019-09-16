/*
 * Copyright 2018-2019 Scala Steward contributors
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

package org.scalasteward.core.update

import cats.Applicative
import cats.implicits._
import org.scalasteward.core.data.Update
import org.scalasteward.core.persistence.KeyValueStore

final class UpdateRepository[F[_]: Applicative](
    kvStore: KeyValueStore[F, String, List[Update.Single]]
) {
  private val key = "updates"

  def deleteAll: F[Unit] =
    kvStore.put(key, List.empty)

  def saveMany(updates: List[Update.Single]): F[Unit] =
    kvStore.modify(key)(maybeList => Some(maybeList.getOrElse(List.empty) ++ updates)).void
}
