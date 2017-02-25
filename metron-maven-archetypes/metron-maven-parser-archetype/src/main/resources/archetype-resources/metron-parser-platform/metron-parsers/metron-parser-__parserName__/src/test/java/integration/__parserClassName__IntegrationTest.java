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
package ${package}.integration;

import org.apache.metron.parsers.integration.ParserIntegrationTest;
import org.apache.metron.parsers.integration.ParserValidation;
import ${package}.integration.validation.${parserClassName}DataValidation;

import java.util.ArrayList;
import java.util.List;

public class ${parserClassName}IntegrationTest extends ParserIntegrationTest {
  @Override
  public String getSensorType() {
    return "${parserName}";
  }

  @Override
  protected String getSampleDataPath(){
    return "src/test/resources/${parserName}/raw/test.raw";
  }

  @Override
  protected String getGlobalConfigPath() throws Exception{
    return "src/test/resources";
  }
  @Override
  protected List<ParserValidation> getValidations() {
    // SampleDataValidation is used to provide a custom path
    // to resources outside the Metron main project and build
    return new ArrayList<ParserValidation>() {{
      add(new ${parserClassName}DataValidation("src/test/resources/${parserName}/parsed/test.parsed"));
    }};
  }
}
