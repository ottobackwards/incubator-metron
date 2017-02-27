#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
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
package ${package}.integration.validation;

import org.apache.metron.integration.utils.TestUtils;
import org.apache.metron.parsers.integration.ParserValidation;
import org.apache.metron.test.utils.ValidationUtils;
import org.junit.Assert;

import java.util.List;

public class ${parserClassName}DataValidation implements ParserValidation {

  private String ${parserName}DataPath;

  /**
   * Custom Parser Validation with support for paths
   * @param ${parserName}DataPath
   */
  public ${parserClassName}DataValidation(String ${parserName}DataPath){
    this.${parserName}DataPath = ${parserName}DataPath;
  }
  @Override
  public String getName() {
    return "${parserName} Data Validation";
  }

  @Override
  public void validate(String sensorType, List<byte[]> actualMessages) throws Exception {
    List<byte[]> expectedMessages = TestUtils.readSampleData(${parserName}DataPath);
    Assert.assertEquals(expectedMessages.size(), actualMessages.size());
    for (int i = 0; i < actualMessages.size(); i++) {
      String expectedMessage = new String(expectedMessages.get(i));
      String actualMessage = new String(actualMessages.get(i));
      try {
        ValidationUtils.assertJSONEqual(expectedMessage, actualMessage);
      } catch (Throwable t) {
        System.out.println("expected: " + expectedMessage);
        System.out.println("actual: " + actualMessage);
        throw t;
      }
    }
  }
}
