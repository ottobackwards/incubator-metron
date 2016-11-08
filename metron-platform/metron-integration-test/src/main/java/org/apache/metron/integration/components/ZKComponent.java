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
package org.apache.metron.integration.components;


import com.google.common.base.Function;
import kafka.api.FetchRequest;
import kafka.api.FetchRequestBuilder;
import kafka.common.TopicExistsException;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.FetchResponse;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.javaapi.consumer.SimpleConsumer;
import kafka.message.MessageAndOffset;
import kafka.server.KafkaConfig;
import kafka.server.KafkaServer;
import kafka.utils.*;
import kafka.zk.EmbeddedZookeeper;
import org.I0Itec.zkclient.ZkClient;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.metron.integration.InMemoryComponent;
import org.apache.metron.integration.wrapper.AdminUtilsWrapper;
import org.apache.metron.integration.wrapper.TestUtilsWrapper;

import java.nio.ByteBuffer;
import java.util.*;


public class ZKComponent implements InMemoryComponent {

  public static final String ZOOKEEPER_PROPERTY = "kafka.zk";
  private transient EmbeddedZookeeper zkServer;
  private String zookeeperConnectString;

  private Function<ZKComponent, Void> postStartCallback;

  public ZKComponent withPostStartCallback(Function<ZKComponent, Void> f) {
    postStartCallback = f;
    return this;
  }

  @Override
  public void start() {
    // setup an EmbeddedZookeeper allow it to pick a port
    // this will avoid collisions
    zkServer = new EmbeddedZookeeper();
    zookeeperConnectString = "127.0.0.1:" + zkServer.port();
    System.out.println(">>>>>>>>>>>ZOOKEEPER RUNNING ON " + zookeeperConnectString + "<<<<<<<<<<<<<<<<<<<");
    if(postStartCallback != null) {
      postStartCallback.apply(this);
    }
  }

  public void test() throws Exception{
    ZkClient test = new ZkClient(zookeeperConnectString, 30000, 30000, ZKStringSerializer$.MODULE$);
    try {
      if(test.readData("/") == null){
        throw new Exception("cannot read zookeeper root");
      }
    }finally{
      test.close();
    }

  }

  public String getZookeeperConnect() {
    return zookeeperConnectString;
  }

  @Override
  public void stop() {
    if (zkServer != null) {
      zkServer.shutdown();
    }
  }
}
