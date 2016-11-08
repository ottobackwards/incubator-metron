/**
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
package org.apache.metron.common.configuration;

import org.apache.commons.io.FilenameUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.metron.common.Constants;
import org.apache.metron.common.configuration.enrichment.SensorEnrichmentConfig;
import org.apache.metron.common.utils.JSONUtils;
import org.apache.zookeeper.KeeperException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.metron.common.configuration.ConfigurationType.ENRICHMENT;
import static org.apache.metron.common.configuration.ConfigurationType.GLOBAL;
import static org.apache.metron.common.configuration.ConfigurationType.PARSER;
import static org.apache.metron.common.configuration.ConfigurationType.PROFILER;

public class ConfigurationsUtils {

  public static CuratorFramework getClient(String zookeeperUrl) {
    RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
    return CuratorFrameworkFactory.newClient(zookeeperUrl, retryPolicy);
  }

  public static void writeGlobalConfigToZookeeper(Map<String, Object> globalConfig, String zookeeperUrl) throws Exception {
    CuratorFramework client = getClient(zookeeperUrl);
    try{
     client.start();
      writeGlobalConfigToZookeeper(globalConfig, client);
    }finally {
      client.close();
    }
  }
  public static void writeGlobalConfigToZookeeper(Map<String, Object> globalConfig, CuratorFramework client) throws Exception {
    writeGlobalConfigToZookeeper(JSONUtils.INSTANCE.toJSON(globalConfig), client);
  }

  public static void writeGlobalConfigToZookeeper(byte[] globalConfig, String zookeeperUrl) throws Exception {
    CuratorFramework client = getClient(zookeeperUrl);
    client.start();
    try {
      writeGlobalConfigToZookeeper(globalConfig, client);
    }
    finally {
      client.close();
    }
  }

  public static void writeGlobalConfigToZookeeper(byte[] globalConfig, CuratorFramework client) throws Exception {
    GLOBAL.deserialize(new String(globalConfig));
    writeToZookeeper(GLOBAL.getZookeeperRoot(), globalConfig, client);
  }

  public static void writeProfilerConfigToZookeeper(byte[] config, CuratorFramework client) throws Exception {
    PROFILER.deserialize(new String(config));
    writeToZookeeper(PROFILER.getZookeeperRoot(), config, client);
  }

  public static void writeSensorParserConfigToZookeeper(String sensorType, SensorParserConfig sensorParserConfig, String zookeeperUrl) throws Exception {
    writeSensorParserConfigToZookeeper(sensorType, JSONUtils.INSTANCE.toJSON(sensorParserConfig), zookeeperUrl);
  }

  public static void writeSensorParserConfigToZookeeper(String sensorType, byte[] configData, String zookeeperUrl) throws Exception {
    CuratorFramework client = getClient(zookeeperUrl);
    client.start();
    try {
      writeSensorParserConfigToZookeeper(sensorType, configData, client);
    }
    finally {
      client.close();
    }
  }

  public static void writeSensorParserConfigToZookeeper(String sensorType, byte[] configData, CuratorFramework client) throws Exception {
    SensorParserConfig c = (SensorParserConfig) PARSER.deserialize(new String(configData));
    c.init();
    writeToZookeeper(PARSER.getZookeeperRoot() + "/" + sensorType, configData, client);
  }

  public static void writeSensorEnrichmentConfigToZookeeper(String sensorType, SensorEnrichmentConfig sensorEnrichmentConfig, String zookeeperUrl) throws Exception {
    writeSensorEnrichmentConfigToZookeeper(sensorType, JSONUtils.INSTANCE.toJSON(sensorEnrichmentConfig), zookeeperUrl);
  }

  public static void writeSensorEnrichmentConfigToZookeeper(String sensorType, byte[] configData, String zookeeperUrl) throws Exception {
    CuratorFramework client = getClient(zookeeperUrl);
    client.start();
    try {
      writeSensorEnrichmentConfigToZookeeper(sensorType, configData, client);
    }
    finally {
      client.close();
    }
  }

  public static void writeSensorEnrichmentConfigToZookeeper(String sensorType, byte[] configData, CuratorFramework client) throws Exception {
    SensorEnrichmentConfig c = (SensorEnrichmentConfig) ENRICHMENT.deserialize(new String(configData));
    if(c.getIndex() == null ) {
      throw new IllegalStateException("Attempting to write a malformed sensor config: missing index.\n" + new String(configData));
    }
    writeToZookeeper(ENRICHMENT.getZookeeperRoot() + "/" + sensorType, configData, client);
  }

  public static void writeConfigToZookeeper(String name, Map<String, Object> config, String zookeeperUrl) throws Exception {
    writeConfigToZookeeper(name, JSONUtils.INSTANCE.toJSON(config), zookeeperUrl);
  }

  public static void writeConfigToZookeeper(String name, byte[] config, String zookeeperUrl) throws Exception {
    CuratorFramework client = getClient(zookeeperUrl);
    client.start();
    try {
      writeToZookeeper(Constants.ZOOKEEPER_TOPOLOGY_ROOT + "/" + name, config, client);
    }
    finally {
      client.close();
    }
  }

  public static void writeToZookeeper(String path, byte[] configData, CuratorFramework client) throws Exception {
    try {
      client.setData().forPath(path, configData);
    } catch (KeeperException.NoNodeException e) {
      client.create().creatingParentsIfNeeded().forPath(path, configData);
    }
  }

  public static void updateConfigsFromZookeeper(Configurations configurations, CuratorFramework client) throws Exception {
    configurations.updateGlobalConfig(readGlobalConfigBytesFromZookeeper(client));
  }

  public static void updateParserConfigsFromZookeeper(ParserConfigurations configurations, CuratorFramework client) throws Exception {
    updateConfigsFromZookeeper(configurations, client);
    List<String> sensorTypes = client.getChildren().forPath(PARSER.getZookeeperRoot());
    for(String sensorType: sensorTypes) {
      configurations.updateSensorParserConfig(sensorType, readSensorParserConfigBytesFromZookeeper(sensorType, client));
    }
  }

  public static void updateEnrichmentConfigsFromZookeeper(EnrichmentConfigurations configurations, CuratorFramework client) throws Exception {
    updateConfigsFromZookeeper(configurations, client);
    List<String> sensorTypes = client.getChildren().forPath(ENRICHMENT.getZookeeperRoot());
    for(String sensorType: sensorTypes) {
      configurations.updateSensorEnrichmentConfig(sensorType, readSensorEnrichmentConfigBytesFromZookeeper(sensorType, client));
    }
  }

  public static SensorEnrichmentConfig readSensorEnrichmentConfigFromZookeeper(String sensorType, CuratorFramework client) throws Exception {
    return JSONUtils.INSTANCE.load(new ByteArrayInputStream(readFromZookeeper(ENRICHMENT.getZookeeperRoot() + "/" + sensorType, client)), SensorEnrichmentConfig.class);
  }

  public static SensorParserConfig readSensorParserConfigFromZookeeper(String sensorType, CuratorFramework client) throws Exception {
    return JSONUtils.INSTANCE.load(new ByteArrayInputStream(readFromZookeeper(PARSER.getZookeeperRoot() + "/" + sensorType, client)), SensorParserConfig.class);
  }

  public static byte[] readGlobalConfigBytesFromZookeeper(CuratorFramework client) throws Exception {
    return readFromZookeeper(GLOBAL.getZookeeperRoot(), client);
  }

  public static byte[] readProfilerConfigBytesFromZookeeper(CuratorFramework client) throws Exception {
    return readFromZookeeper(PROFILER.getZookeeperRoot(), client);
  }

  public static byte[] readSensorParserConfigBytesFromZookeeper(String sensorType, CuratorFramework client) throws Exception {
    return readFromZookeeper(PARSER.getZookeeperRoot() + "/" + sensorType, client);
  }

  public static byte[] readSensorEnrichmentConfigBytesFromZookeeper(String sensorType, CuratorFramework client) throws Exception {
    return readFromZookeeper(ENRICHMENT.getZookeeperRoot() + "/" + sensorType, client);
  }

  public static byte[] readConfigBytesFromZookeeper(String name, CuratorFramework client) throws Exception {
    return readFromZookeeper(Constants.ZOOKEEPER_TOPOLOGY_ROOT + "/" + name, client);
  }

  public static byte[] readFromZookeeper(String path, CuratorFramework client) throws Exception {
    return client.getData().forPath(path);
  }

  public static void uploadConfigsToZookeeper(String globalConfigPath,
                                              String parsersConfigPath,
                                              String enrichmentsConfigPath,
                                              String profilerConfigPath,
                                              String zookeeperUrl) throws Exception {
    CuratorFramework client = getClient(zookeeperUrl);
    try{
      client.start();
      uploadConfigsToZookeeper(globalConfigPath, parsersConfigPath, enrichmentsConfigPath, profilerConfigPath, client);
    }finally {
      client.close();
    }
  }

  public static void uploadConfigsToZookeeper(String rootFilePath, CuratorFramework client) throws Exception {
    uploadConfigsToZookeeper(rootFilePath, rootFilePath, rootFilePath, rootFilePath, client);
  }

  public static void uploadConfigsToZookeeper(String globalConfigPath,
                                              String parsersConfigPath,
                                              String enrichmentsConfigPath,
                                              String profilerConfigPath,
                                              CuratorFramework client) throws Exception {

    // global
    if (globalConfigPath != null) {
      byte[] globalConfig = readGlobalConfigFromFile(globalConfigPath);
      if (globalConfig.length > 0) {
        ConfigurationsUtils.writeGlobalConfigToZookeeper(readGlobalConfigFromFile(globalConfigPath), client);
      }
    }

    // parsers
    if (parsersConfigPath != null) {
      Map<String, byte[]> sensorParserConfigs = readSensorParserConfigsFromFile(parsersConfigPath);
      for (String sensorType : sensorParserConfigs.keySet()) {
        ConfigurationsUtils.writeSensorParserConfigToZookeeper(sensorType, sensorParserConfigs.get(sensorType), client);
      }
    }

    // enrichments
    if (enrichmentsConfigPath != null) {
      Map<String, byte[]> sensorEnrichmentConfigs = readSensorEnrichmentConfigsFromFile(enrichmentsConfigPath);
      for (String sensorType : sensorEnrichmentConfigs.keySet()) {
        ConfigurationsUtils.writeSensorEnrichmentConfigToZookeeper(sensorType, sensorEnrichmentConfigs.get(sensorType), client);
      }
    }

    // profiler
    if (profilerConfigPath != null) {
      byte[] globalConfig = readProfilerConfigFromFile(profilerConfigPath);
      if (globalConfig.length > 0) {
        ConfigurationsUtils.writeProfilerConfigToZookeeper(readProfilerConfigFromFile(profilerConfigPath), client);
      }
    }
  }

  public static byte[] readGlobalConfigFromFile(String rootPath) throws IOException {
    byte[] globalConfig = new byte[0];
    File configPath = new File(rootPath, GLOBAL.getName() + ".json");
    if (configPath.exists()) {
      globalConfig = Files.readAllBytes(configPath.toPath());
    }
    return globalConfig;
  }

  public static Map<String, byte[]> readSensorParserConfigsFromFile(String rootPath) throws IOException {
    return readSensorConfigsFromFile(rootPath, PARSER);
  }

  public static Map<String, byte[]> readSensorEnrichmentConfigsFromFile(String rootPath) throws IOException {
    return readSensorConfigsFromFile(rootPath, ENRICHMENT);
  }


  /**
   * Read the Profiler configuration from a file.  There is only a single profiler configuration.
   * @param rootPath Path to the Profiler configuration.
   */
  public static byte[] readProfilerConfigFromFile(String rootPath) throws IOException {

    byte[] config = new byte[0];
    File configPath = new File(rootPath, PROFILER.getName() + ".json");
    if (configPath.exists()) {
      config = Files.readAllBytes(configPath.toPath());
    }

    return config;
  }

  public static Map<String, byte[]> readSensorConfigsFromFile(String rootPath, ConfigurationType configType) throws IOException {
    Map<String, byte[]> sensorConfigs = new HashMap<>();
    File configPath = new File(rootPath, configType.getDirectory());
    if (configPath.exists()) {
      File[] children = configPath.listFiles();
      if (children != null) {
        for (File file : children) {
          sensorConfigs.put(FilenameUtils.removeExtension(file.getName()), Files.readAllBytes(file.toPath()));
        }
      }
    }
    return sensorConfigs;
  }


  public interface ConfigurationVisitor{
    void visit(ConfigurationType configurationType, String name, String data);
  }

  public static void visitConfigs(CuratorFramework client, ConfigurationVisitor callback) throws Exception {
    visitConfigs(client, callback, GLOBAL);
    visitConfigs(client, callback, PARSER);
    visitConfigs(client, callback, ENRICHMENT);
    visitConfigs(client, callback, PROFILER);
  }

  public static void visitConfigs(CuratorFramework client, ConfigurationVisitor callback, ConfigurationType configType) throws Exception {

    if (client.checkExists().forPath(configType.getZookeeperRoot()) != null) {

      if (configType.equals(GLOBAL)) {
        byte[] globalConfigData = client.getData().forPath(configType.getZookeeperRoot());
        callback.visit(configType, "global", new String(globalConfigData));

      } else if (configType.equals(PARSER) || configType.equals(ENRICHMENT) || configType.equals(PROFILER)) {
        List<String> children = client.getChildren().forPath(configType.getZookeeperRoot());
        for (String child : children) {

          byte[] data = client.getData().forPath(configType.getZookeeperRoot() + "/" + child);
          callback.visit(configType, child, new String(data));
        }
      }
    }
  }

  public static void dumpConfigs(PrintStream out, CuratorFramework client) throws Exception {
    ConfigurationsUtils.visitConfigs(client, (type, name, data) -> {
      type.deserialize(data);
      out.println(type + " Config: " + name + "\n" + data);
    });
  }
}
