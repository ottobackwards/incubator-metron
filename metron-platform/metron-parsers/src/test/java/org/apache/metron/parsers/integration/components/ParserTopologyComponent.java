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
package org.apache.metron.parsers.integration.components;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.metron.common.spout.kafka.SpoutConfig;
import org.apache.metron.integration.InMemoryComponent;
import org.apache.metron.integration.UnableToStartException;
import org.apache.metron.parsers.topology.ParserTopologyBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ParserTopologyComponent implements InMemoryComponent {

  private Properties topologyProperties;
  private String brokerUrl;
  private String sensorType;
  private LocalCluster stormCluster;

  public static class Builder {
    Properties topologyProperties;
    String brokerUrl;
    String sensorType;
    public Builder withTopologyProperties(Properties topologyProperties) {
      this.topologyProperties = topologyProperties;
      return this;
    }
    public Builder withBrokerUrl(String brokerUrl) {
      this.brokerUrl = brokerUrl;
      return this;
    }
    public Builder withSensorType(String sensorType) {
      this.sensorType = sensorType;
      return this;
    }

    public ParserTopologyComponent build() {
      return new ParserTopologyComponent(topologyProperties, brokerUrl, sensorType);
    }
  }

  public ParserTopologyComponent(Properties topologyProperties, String brokerUrl, String sensorType) {
    this.topologyProperties = topologyProperties;
    this.brokerUrl = brokerUrl;
    this.sensorType = sensorType;
  }
  public String getZookeeperConnectString() {
    String configured = topologyProperties.getProperty("kafka.zk");
    return configured == null ? "localhost:2000":configured;
  }
  @Override
  public void start() throws UnableToStartException {
    String zookeeperConnectionString = getZookeeperConnectString();
    try {
      TopologyBuilder topologyBuilder = ParserTopologyBuilder.build( zookeeperConnectionString
                                                                   , brokerUrl
                                                                   , sensorType
                                                                   , SpoutConfig.Offset.BEGINNING
                                                                   , 1
                                                                   , 1
                                                                   , 1
                                                                   , 1
                                                                   , 1
                                                                   , 1
                                                                   , 1
                                                                   , 1
                                                                   , null
                                                                   );
      Map<String, Object> stormConf = new HashMap<>();
      String[] split = zookeeperConnectionString.split(":");
      ArrayList servers = new ArrayList();
      servers.add(split[0]);
     // stormConf.put(Config.TOPOLOGY_DEBUG, true);
      stormCluster = new LocalCluster(split[0],Long.parseLong(split[1]));
      stormConf.put(Config.STORM_ZOOKEEPER_SERVERS,servers);
      stormConf.put(Config.STORM_ZOOKEEPER_PORT,Integer.valueOf(split[1]));
      stormCluster.submitTopology(sensorType, stormConf, topologyBuilder.createTopology());
    } catch (Exception e) {
      throw new UnableToStartException("Unable to start parser topology for sensorType: " + sensorType, e);
    }
  }

  @Override
  public void stop() {
    if (stormCluster != null) {
      try{
        stormCluster.killTopology(sensorType);
      }catch(Exception e){}
      stormCluster.shutdown();
    }
  }
}
