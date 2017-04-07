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
import junit.framework.Assert;
import org.apache.metron.TestConstants;
import org.apache.metron.bundles.BundleClassLoaders;
import org.apache.metron.bundles.ExtensionClassInitializer;
import org.apache.metron.bundles.util.FileUtilities;
import org.apache.metron.bundles.util.FileUtils;
import org.apache.metron.common.Constants;
import org.apache.metron.enrichment.integration.components.ConfigUploadComponent;
import org.apache.metron.integration.BaseIntegrationTest;
import org.apache.metron.integration.ComponentRunner;
import org.apache.metron.integration.ProcessorResult;
import org.apache.metron.integration.components.KafkaComponent;
import org.apache.metron.integration.components.ZKServerComponent;
import org.apache.metron.integration.processors.KafkaMessageSet;
import org.apache.metron.integration.processors.KafkaProcessor;
import org.apache.metron.integration.utils.TestUtils;
import org.apache.metron.parsers.integration.ParserValidation;
import org.apache.metron.parsers.integration.components.ParserTopologyComponent;
import org.apache.metron.parsers.integration.validation.PathedSampleDataValidation;
import org.apache.metron.test.TestDataType;
import org.apache.metron.test.utils.SampleDataUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class ASABundleIntegrationTest extends BaseIntegrationTest{
  static final Map<String,String> EMPTY_MAP = new HashMap<String,String>();
  static final String sensorType = "asa";
  static final String ERROR_TOPIC = "parser_error";
  protected List<byte[]> inputMessages;
  @AfterClass
  public static void after(){
    ExtensionClassInitializer.reset();
    BundleClassLoaders.reset();
    FileUtils.reset();
  }

  @BeforeClass
  public static void copyResources() throws IOException {
    copyResources("./src/test/resources","./target/local");
    Path bundlePath = Paths.get("../metron-parser-asa-extension/metron-parser-asa-bundle/target/metron-parser-asa-bundle-0.3.1.bundle");
    Path bundleTargetPath = Paths.get("./target/local/metron/extension_lib");
    Files.copy(bundlePath, bundleTargetPath.resolve("metron-parser-asa-bundle-0.3.1.bundle"), REPLACE_EXISTING);
  }

  public static void copyResources(String source, String target) throws IOException {
    final Path sourcePath = Paths.get(source);
    final Path targetPath = Paths.get(target);

    Files.walkFileTree(sourcePath, new SimpleFileVisitor<Path>() {

      @Override
      public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
              throws IOException {

        Path relativeSource = sourcePath.relativize(dir);
        Path target = targetPath.resolve(relativeSource);

        if(!Files.exists(target)) {
          Files.createDirectories(target);
        }
        return FileVisitResult.CONTINUE;

      }

      @Override
      public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
              throws IOException {

        Path relativeSource = sourcePath.relativize(file);
        Path target = targetPath.resolve(relativeSource);

        Files.copy(file, target, REPLACE_EXISTING);

        return FileVisitResult.CONTINUE;
      }
    });
  }

  @Test
  public void testLocal() throws Exception{
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
            .withGlobalConfigsPath("./target/local/zookeeper/")
            .withParserConfigsPath("../metron-parser-asa-extension/metron-parser-asa/" + TestConstants.THIS_PARSER_CONFIGS_PATH);

    ParserTopologyComponent parserTopologyComponent = new ParserTopologyComponent.Builder()
            .withSensorType(sensorType)
            .withTopologyProperties(topologyProperties)
            .withBrokerUrl(kafkaComponent.getBrokerList()).build();

    //UnitTestHelper.verboseLogging();
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
      add(new PathedSampleDataValidation("../metron-parser-asa-extension/metron-parser-asa/src/test/resources/data/parsed/test.parsed"));
    }};
  }

  protected String getGlobalConfigPath() throws Exception{
    return "../../../../metron-platform/metron-integration-test/src/main/config/zookeeper/";
  }

  protected String getSampleDataPath() throws Exception {
    return "../metron-parser-asa-extension/metron-parser-asa/src/test/resources/data/raw/test.raw";
  }
}
