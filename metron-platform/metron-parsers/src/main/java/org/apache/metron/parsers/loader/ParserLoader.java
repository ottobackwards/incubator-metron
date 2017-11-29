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

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.swing.text.html.Option;
import org.apache.curator.framework.CuratorFramework;
import org.apache.metron.bundles.BundleSystem;
import org.apache.metron.bundles.BundleSystemBuilder;
import org.apache.metron.bundles.NotInitializedException;
import org.apache.metron.bundles.util.BundleProperties;
import org.apache.metron.common.Constants;
import org.apache.metron.common.configuration.ConfigurationsUtils;
import org.apache.metron.common.configuration.SensorParserConfig;
import org.apache.metron.parsers.bolt.ParserBolt;
import org.apache.metron.parsers.interfaces.MessageParser;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Loads a Parser from the Metron Bundle Extension System. This Bundle by be resident on the local
 * filesystem or it may located in HDFS.
 */
public class ParserLoader {

  private static final Logger LOG = LoggerFactory.getLogger(ParserBolt.class);

  /**
   * Loads a parser from a configuration.
   *
   * @param client the CuratorFramework
   * @param parserConfig the configuration
   * @return Optional of MessageParser
   */
  @SuppressWarnings("unchecked")
  public static Optional<MessageParser<JSONObject>> loadParser(
      CuratorFramework client, SensorParserConfig parserConfig) {
    MessageParser<JSONObject> parser = null;
    try {
      // fetch the BundleProperties from zookeeper
      Optional<BundleProperties> bundleProperties = getBundleProperties(client);
      if (bundleProperties.isPresent()) {
        return loadParserFromBundleSystem(bundleProperties.get(), parserConfig);
      } else {
        LOG.error("BundleProperties are missing!");
        return Optional.empty();
      }
    } catch (Exception e) {
      LOG.error("Failed to load parser " + parserConfig.getParserClassName(), e);
      return Optional.empty();
    }
  }

  /**
   * Loads a parser from configuration using passed BundleProperties.
   * @param parserConfig the Parser config
   * @param bundleProperties the BundleProperties
   * @return Optional of MessageParser
   */
  public static Optional<MessageParser<JSONObject>> loadParser(
      SensorParserConfig parserConfig, BundleProperties bundleProperties) {
    try {
      return loadParserFromBundleSystem(bundleProperties, parserConfig);
    } catch (Exception e) {
      LOG.error("Failed to load parser " + parserConfig.getParserClassName(), e);
      return Optional.empty();
    }
  }

  @SuppressWarnings("unchecked")
  private static Optional<MessageParser<JSONObject>> loadParserFromBundleSystem(
      BundleProperties properties, SensorParserConfig parserConfig)
      throws ClassNotFoundException, NotInitializedException, InstantiationException,
      IllegalAccessException {
    BundleSystem bundleSystem = new BundleSystemBuilder().withBundleProperties(properties).build();
    MessageParser<JSONObject> parser = null;
    parser = bundleSystem.createInstance(parserConfig.getParserClassName(), MessageParser.class);
    if (parser == null) {
      return Optional.empty();
    }
    return Optional.of(parser);
  }

  private static Optional<BundleProperties> getBundleProperties(CuratorFramework client)
      throws Exception {
    BundleProperties properties = null;
    byte[] propBytes = ConfigurationsUtils
        .readFromZookeeper(Constants.ZOOKEEPER_ROOT + "/bundle.properties", client);
    if (propBytes.length > 0) {
      // read in the properties
      properties = BundleProperties
          .createBasicBundleProperties(new ByteArrayInputStream(propBytes), new HashMap<>());
      return Optional.of(properties);
    }
    return Optional.empty();
  }
}
