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
package org.apache.metron.integration;

import com.google.common.base.Function;
import org.apache.metron.TestConstants;
import org.apache.metron.integration.components.KafkaComponent;
import org.apache.metron.integration.components.KafkaWithZKComponent;
import org.apache.metron.integration.components.ZKComponent;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Properties;

public abstract class BaseIntegrationTest {
    protected KafkaWithZKComponent getKafkaComponent(final Properties topologyProperties, List<KafkaWithZKComponent.Topic> topics){
        return new KafkaWithZKComponent()
                .withPostStartCallback(new Function<KafkaWithZKComponent, Void>() {
                    @Nullable
                    @Override
                    public Void apply(@Nullable KafkaWithZKComponent kafkaComponent) {
                        topologyProperties.setProperty(KafkaWithZKComponent.ZOOKEEPER_PROPERTY, kafkaComponent.getZookeeperConnect());
                        return null;
                    }
                });
    }
  protected KafkaComponent getTestKafkaComponent(final Properties topologyProperties, List<KafkaComponent.Topic> topics) {
    return new KafkaComponent()
            .withTopologyProperties(topologyProperties)
            .withTopics(topics);
  }

  protected ZKComponent getZkComponent(final Properties topologyProperties){
      return new ZKComponent()
              .withPostStartCallback(new Function<ZKComponent, Void>() {
                  @Nullable
                  @Override
                  public Void apply(@Nullable ZKComponent zKComponent) {
                      topologyProperties.setProperty(ZKComponent.ZOOKEEPER_PROPERTY, zKComponent.getZookeeperConnect());
                      return null;
                  }
              });
   }

}
