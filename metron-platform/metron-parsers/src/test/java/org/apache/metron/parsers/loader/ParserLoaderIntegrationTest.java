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

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Properties;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.metron.bundles.BundleSystem;
import org.apache.metron.common.configuration.SensorParserConfig;
import org.apache.metron.enrichment.integration.components.ConfigUploadComponent;
import org.apache.metron.integration.ComponentRunner;
import org.apache.metron.integration.components.ZKServerComponent;
import org.apache.metron.parsers.interfaces.MessageParser;
import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ParserLoaderIntegrationTest {

  @Before
  public void before() {
    BundleSystem.reset();
  }

  @After
  public void after() {
    BundleSystem.reset();
  }

  @Test
  public void loadParserWithZooKeeper() throws Exception {
    final Properties topologyProperties = new Properties();
    ZKServerComponent zkServerComponent = new ZKServerComponent()
        .withPostStartCallback((zkComponent) -> {
          topologyProperties
              .setProperty(ZKServerComponent.ZOOKEEPER_PROPERTY, zkComponent.getConnectionString());
        });

    ConfigUploadComponent configUploadComponent = new ConfigUploadComponent().withBundleProperties(
        Files.readAllBytes(
            Paths.get("../metron-integration-test/src/main/config/zookeeper/bundle.properties")))
        .withTopologyProperties(topologyProperties);

    ComponentRunner runner = new ComponentRunner.Builder().withComponent("zk", zkServerComponent)
        .withComponent("config", configUploadComponent).withMillisecondsBetweenAttempts(5000)
        .withNumRetries(10).withCustomShutdownOrder(new String[]{"config", "zk"}).build();
    runner.start();

    RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
    CuratorFramework client = CuratorFrameworkFactory
        .newClient(zkServerComponent.getConnectionString(), retryPolicy);
    client.start();

    try {

      SensorParserConfig sensorParserConfig = new SensorParserConfig();
      sensorParserConfig.setParserClassName("org.apache.metron.parsers.csv.CSVParser");

      Optional<MessageParser<JSONObject>> parserOptional = ParserLoader
          .loadParser(client, sensorParserConfig);
      Assert.assertTrue(parserOptional.isPresent());
      Assert.assertTrue(MessageParser.class.isAssignableFrom(parserOptional.get().getClass()));
    } finally {
      client.close();
      runner.stop();
    }

  }

  @Test
  public void loadParserFailsWithNoProperties() throws Exception {
    final Properties topologyProperties = new Properties();
    ZKServerComponent zkServerComponent = new ZKServerComponent()
        .withPostStartCallback((zkComponent) -> {
          topologyProperties
              .setProperty(ZKServerComponent.ZOOKEEPER_PROPERTY, zkComponent.getConnectionString());
        });

    ConfigUploadComponent configUploadComponent = new ConfigUploadComponent().withBundleProperties(
        Files.readAllBytes(
            Paths.get("../metron-integration-test/src/main/config/zookeeper/bundle.properties")))
        .withTopologyProperties(topologyProperties);

    ComponentRunner runner = new ComponentRunner.Builder().withComponent("zk", zkServerComponent).build();
    runner.start();

    RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
    CuratorFramework client = CuratorFrameworkFactory
        .newClient(zkServerComponent.getConnectionString(), retryPolicy);
    client.start();

    try {

      SensorParserConfig sensorParserConfig = new SensorParserConfig();
      sensorParserConfig.setParserClassName("org.apache.metron.parsers.csv.CSVParser");

      Optional<MessageParser<JSONObject>> parserOptional = ParserLoader
          .loadParser(client, sensorParserConfig);
      Assert.assertFalse(parserOptional.isPresent());
    } finally {
      client.close();
      runner.stop();
    }

  }

  @Test
  public void loadParserWithZooKeeperFailsWithBadParser() throws Exception {
    final Properties topologyProperties = new Properties();
    ZKServerComponent zkServerComponent = new ZKServerComponent()
        .withPostStartCallback((zkComponent) -> {
          topologyProperties
              .setProperty(ZKServerComponent.ZOOKEEPER_PROPERTY, zkComponent.getConnectionString());
        });

    ConfigUploadComponent configUploadComponent = new ConfigUploadComponent().withBundleProperties(
        Files.readAllBytes(
            Paths.get("../metron-integration-test/src/main/config/zookeeper/bundle.properties")))
        .withTopologyProperties(topologyProperties);

    ComponentRunner runner = new ComponentRunner.Builder().withComponent("zk", zkServerComponent)
        .withComponent("config", configUploadComponent).withMillisecondsBetweenAttempts(5000)
        .withNumRetries(10).withCustomShutdownOrder(new String[]{"config", "zk"}).build();
    runner.start();

    RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
    CuratorFramework client = CuratorFrameworkFactory
        .newClient(zkServerComponent.getConnectionString(), retryPolicy);
    client.start();

    try {

      SensorParserConfig sensorParserConfig = new SensorParserConfig();
      sensorParserConfig.setParserClassName("org.apache.metron.parsers.foo.BarParser");

      Optional<MessageParser<JSONObject>> parserOptional = ParserLoader
          .loadParser(client, sensorParserConfig);
      Assert.assertFalse(parserOptional.isPresent());
    } finally {
      client.close();
      runner.stop();
    }

  }


}