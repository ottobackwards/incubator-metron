/**
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 * agreements.  See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership.  The ASF licenses this file to you under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with the License.  You may obtain
 * a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.apache.metron.parsers.loader;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Optional;
import org.apache.metron.bundles.BundleSystem;
import org.apache.metron.bundles.util.BundleProperties;
import org.apache.metron.common.configuration.SensorParserConfig;
import org.apache.metron.parsers.interfaces.MessageParser;
import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ParserLoaderTest {

  @Before
  public void beforeTest() {
    BundleSystem.reset();
  }

  @After
  public void afterTest() {
    BundleSystem.reset();
  }

  @Test
  public void loadParserWithBundleProperties() throws Exception {
   File propertiesFile = new File("../metron-integration-test/src/main/config/zookeeper/bundle.properties");
   try(FileInputStream fileInputStream = new FileInputStream(propertiesFile)) {
     BundleProperties bundleProperties = BundleProperties
         .createBasicBundleProperties(fileInputStream, new HashMap());
     SensorParserConfig sensorParserConfig = new SensorParserConfig();
     sensorParserConfig.setParserClassName("org.apache.metron.parsers.csv.CSVParser");

     Optional<MessageParser<JSONObject>> parserOptional = ParserLoader
         .loadParser(sensorParserConfig, bundleProperties);
     Assert.assertTrue(parserOptional.isPresent());
     Assert.assertTrue(MessageParser.class.isAssignableFrom(parserOptional.get().getClass()));
   }
  }

  @Test
  public void loadParserFailsWithInvalidParserAndBundleProperties() throws Exception {
    File propertiesFile = new File("../metron-integration-test/src/main/config/zookeeper/bundle.properties");
    try(FileInputStream fileInputStream = new FileInputStream(propertiesFile)) {
      BundleProperties bundleProperties = BundleProperties
          .createBasicBundleProperties(fileInputStream, new HashMap());
      SensorParserConfig sensorParserConfig = new SensorParserConfig();
      sensorParserConfig.setParserClassName("org.apache.metron.parsers.foo.BarParser");

      Optional<MessageParser<JSONObject>> parserOptional = ParserLoader
          .loadParser(sensorParserConfig, bundleProperties);
      Assert.assertFalse(parserOptional.isPresent());
    }
  }

}