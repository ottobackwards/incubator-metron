/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.nifi.nar.util;

import org.apache.nifi.nar.integration.NarUnpackerIntegrationTest;
import org.apache.nifi.util.NiFiProperties;

import java.net.URISyntaxException;
import java.util.Map;

public class TestUtil {
  public static NiFiProperties loadSpecifiedProperties(final String propertiesFile, final Map<String, String> others) {
    String filePath;
    try {
      filePath = NarUnpackerIntegrationTest.class.getResource(propertiesFile).toURI().getPath();
    } catch (URISyntaxException ex) {
      throw new RuntimeException("Cannot load properties file due to "
              + ex.getLocalizedMessage(), ex);
    }
    return NiFiProperties.createBasicNiFiProperties(filePath, others);
  }
}
