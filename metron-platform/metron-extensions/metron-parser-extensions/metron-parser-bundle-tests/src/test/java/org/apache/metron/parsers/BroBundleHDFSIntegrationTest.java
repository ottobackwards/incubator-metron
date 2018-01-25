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
package org.apache.metron.parsers;


import com.google.common.base.Function;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.annotation.Nullable;
import junit.framework.Assert;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsAction;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.metron.TestConstants;
import org.apache.metron.bundles.BundleSystem;
import org.apache.metron.bundles.util.BundleProperties;
import org.apache.metron.common.Constants;
import org.apache.metron.enrichment.integration.components.ConfigUploadComponent;
import org.apache.metron.integration.BaseIntegrationTest;
import org.apache.metron.integration.ComponentRunner;
import org.apache.metron.integration.ProcessorResult;
import org.apache.metron.integration.components.KafkaComponent;
import org.apache.metron.integration.components.MRComponent;
import org.apache.metron.integration.components.ZKServerComponent;
import org.apache.metron.integration.processors.KafkaMessageSet;
import org.apache.metron.integration.processors.KafkaProcessor;
import org.apache.metron.integration.utils.TestUtils;
import org.apache.metron.parsers.integration.ParserValidation;
import org.apache.metron.parsers.integration.components.ParserTopologyComponent;
import org.apache.metron.parsers.integration.validation.PathedSampleDataValidation;
import org.apache.metron.test.utils.ResourceCopier;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class BroBundleHDFSIntegrationTest extends BaseIntegrationTest {
  static final Map<String,String> EMPTY_MAP = new HashMap<String,String>();
  static final String sensorType = "bro";
  static final String ERROR_TOPIC = "parser_error";
  static MRComponent mrComponent;
  static Configuration configuration;
  static FileSystem fileSystem;
  protected List<byte[]> inputMessages;

  @AfterClass
  public static void after(){
    mrComponent.stop();
    BundleSystem.reset();
  }

  @BeforeClass
  public static void setup() {
    BundleSystem.reset();
    mrComponent = new MRComponent().withBasePath("target/hdfs");
    mrComponent.start();
    configuration = mrComponent.getConfiguration();

    try {

      // copy the correct things in
      ResourceCopier.copyResources(Paths.get("./src/test/resources"),Paths.get("./target/remote"));

      // we need to patch the properties file
      BundleProperties properties = BundleProperties.createBasicBundleProperties("./target/remote/zookeeper/bundle.properties",new HashMap<>());
      String hdfsPrefix  = configuration.get("fs.defaultFS");
      properties.setProperty(BundleProperties.BUNDLE_LIBRARY_DIRECTORY, hdfsPrefix + "/extension_lib/");
      FileOutputStream fso = new FileOutputStream("./target/remote/zookeeper/bundle.properties");
      properties.storeProperties(fso,"HDFS UPDATE");
      fso.flush();
      fso.close();

      fileSystem = FileSystem.newInstance(configuration);
      if(!fileSystem.mkdirs(new Path("/work/"),new FsPermission(FsAction.READ_WRITE,FsAction.READ_WRITE,FsAction.READ_WRITE))){
        System.out.println("FAILED MAKE DIR");
      }
      fileSystem.copyFromLocalFile(new Path("./target/remote/metron/extension_lib/"), new Path("/"));
      fileSystem.copyFromLocalFile(new Path("./target/remote/zookeeper/bundle.properties"), new Path("/work/"));
    } catch (IOException e) {
      throw new RuntimeException("Unable to start cluster", e);
    }

  }

  @Test
  public void testHDFS() throws Exception{
    final Properties topologyProperties = new Properties();
    inputMessages = TestUtils.readSampleData(getSampleDataPath());
    final KafkaComponent kafkaComponent = getKafkaComponent(topologyProperties, new ArrayList<KafkaComponent.Topic>() {{
      add(new KafkaComponent.Topic(sensorType, 1));
      add(new KafkaComponent.Topic(Constants.ENRICHMENT_TOPIC, 1));
      add(new KafkaComponent.Topic(ERROR_TOPIC,1));
    }});
    topologyProperties.setProperty("kafka.broker", kafkaComponent.getBrokerList());

    ZKServerComponent zkServerComponent = getZKServerComponent(topologyProperties);

    ConfigUploadComponent configUploadComponent = new ConfigUploadComponent()
            .withTopologyProperties(topologyProperties)
            .withGlobalConfigsPath("./target/remote/zookeeper/")
            .withParserConfigsPath("../metron-parser-bro-extension/metron-parser-bro/" + TestConstants.THIS_PARSER_CONFIGS_PATH);

    ParserTopologyComponent parserTopologyComponent = new ParserTopologyComponent.Builder()
            .withSensorType(sensorType)
            .withTopologyProperties(topologyProperties)
            .withBrokerUrl(kafkaComponent.getBrokerList()).build();

    ComponentRunner runner = new ComponentRunner.Builder()
            .withComponent("zk", zkServerComponent)
            .withComponent("kafka", kafkaComponent)
            .withComponent("config", configUploadComponent)
            .withComponent("org/apache/storm", parserTopologyComponent)
            .withMillisecondsBetweenAttempts(5000)
            .withNumRetries(10)
            .withCustomShutdownOrder(new String[] {"org/apache/storm","config","kafka","zk"})
            .build();
    runner.start();

    try {
      kafkaComponent.writeMessages(sensorType, inputMessages);
      ProcessorResult<List<byte[]>> result = runner.process(getProcessor());
      List<byte[]> outputMessages = result.getResult();
      StringBuffer buffer = new StringBuffer();
      if (result.failed()){
        result.getBadResults(buffer);
        buffer.append(String.format("%d Valid Messages Processed", outputMessages.size())).append("\n");
        dumpParsedMessages(outputMessages,buffer);
        Assert.fail(buffer.toString());
      } else {
        List<ParserValidation> validations = getValidations();
        if (validations == null || validations.isEmpty()) {
          buffer.append("No validations configured for sensorType " + sensorType + ".  Dumping parsed messages").append("\n");
          dumpParsedMessages(outputMessages,buffer);
          Assert.fail(buffer.toString());
        } else {
          for (ParserValidation validation : validations) {
            System.out.println("Running " + validation.getName() + " on sensorType " + sensorType);
            validation.validate(sensorType, outputMessages);
          }
        }
      }
    } finally {
      runner.stop();
    }
  }

  public void dumpParsedMessages(List<byte[]> outputMessages, StringBuffer buffer) {
    for (byte[] outputMessage : outputMessages) {
      buffer.append(new String(outputMessage)).append("\n");
    }
  }

  @SuppressWarnings("unchecked")
  private KafkaProcessor<List<byte[]>> getProcessor(){

    return new KafkaProcessor<>()
            .withKafkaComponentName("kafka")
            .withReadTopic(Constants.ENRICHMENT_TOPIC)
            .withErrorTopic(ERROR_TOPIC)
            .withValidateReadMessages(new Function<KafkaMessageSet, Boolean>() {
              @Nullable
              @Override
              public Boolean apply(@Nullable KafkaMessageSet messageSet) {
                return (messageSet.getMessages().size() + messageSet.getErrors().size() == inputMessages.size());
              }
            })
            .withProvideResult(new Function<KafkaMessageSet,List<byte[]>>(){
              @Nullable
              @Override
              public List<byte[]> apply(@Nullable KafkaMessageSet messageSet) {
                return messageSet.getMessages();
              }
            });
  }

  public List<ParserValidation> getValidations() {
    return new ArrayList<ParserValidation>() {{
      add(new PathedSampleDataValidation("../metron-parser-bro-extension/metron-parser-bro/src/test/resources/data/parsed/test.parsed"));
    }};
  }

  protected String getGlobalConfigPath() throws Exception{
    return "../../../../metron-integration-test/src/main/config/zookeeper/";
  }

  protected String getSampleDataPath() throws Exception {
    return "../metron-parser-bro-extension/metron-parser-bro/src/test/resources/data/raw/test.raw";
  }
}
