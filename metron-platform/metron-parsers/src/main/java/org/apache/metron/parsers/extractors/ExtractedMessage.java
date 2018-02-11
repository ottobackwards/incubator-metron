/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.metron.parsers.extractors;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * {@code ExtractedMessage} represents a message and possibly field values
 * extracted from a  message wrapping format.
 *
 * {@code ExtractedMessage} is returned by {@code MessageExtractor} operations
 *
 */
public class ExtractedMessage {

  /**
   * Builder for ExtractedMessage instances.
   */
  public static class Builder {
    private byte[] message;
    private Map<String,Object> fields = new HashMap<>();

    /**
     * Builder with a message.
     * @param message the {@code byte[]} message
     * @return {@code Builder}
     */
    public Builder withMessage(byte[] message) {
      this.message = message;
      return this;
    }

    /**
     * Builder with a {@code Map} of fields.
     * @param fields {@code Map} of fields
     * @return {@code Builder}
     */
    public Builder withFields(Map<String,Object> fields) {
      if (fields != null) {
        this.fields.putAll(fields);
      }
      return this;
    }

    /**
     * Builds a new {@code ExtractedMessage} object.
     * @return {@code ExtractedMessage}
     */
    public ExtractedMessage build() {
      return new ExtractedMessage(message, fields);
    }
  }

  private byte[] message;
  private Map<String,Object> fields;

  private ExtractedMessage(byte[] message, Map<String, Object> fields) {
    this.message = message;
    this.fields = fields;
  }

  /**
   * Returns an {@code Optional} of the {@code byte[]} message.
   * @return {@code Optional}
   */
  public Optional<byte[]> getMessage() {
    return Optional.ofNullable(message);
  }

  /**
   * Returns an {@code Optional} of the {@code Map} of fields.
   * @return {@code Optional}
   */
  public Optional<Map<String, Object>> getFields() {
    return Optional.ofNullable(fields);
  }
}
