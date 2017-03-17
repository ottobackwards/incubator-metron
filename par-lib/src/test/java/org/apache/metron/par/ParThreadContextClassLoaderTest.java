/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.metron.par;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.metron.par.util.ParProperties;
import org.junit.AfterClass;
import org.junit.Test;

public class ParThreadContextClassLoaderTest {


    @AfterClass
    public static void after(){
        ExtensionClassInitializer.reset();
    }

    @Test
    public void validateWithPropertiesConstructor() throws Exception {
        ParProperties properties = ParProperties.createBasicParProperties("src/test/resources/par.properties", null);
        ExtensionClassInitializer.initialize(new ArrayList<>());
        assertTrue(ParThreadContextClassLoader.createInstance(WithPropertiesConstructor.class.getName(),
                WithPropertiesConstructor.class, properties) instanceof WithPropertiesConstructor);
    }

    @Test(expected = IllegalStateException.class)
    public void validateWithPropertiesConstructorInstantiationFailure() throws Exception {
        ExtensionClassInitializer.initialize(new ArrayList<>());
        Map<String, String> additionalProperties = new HashMap<>();
        additionalProperties.put("fail", "true");
        ParProperties properties = ParProperties.createBasicParProperties("src/test/resources/par.properties", additionalProperties);
        ParThreadContextClassLoader.createInstance(WithPropertiesConstructor.class.getName(), WithPropertiesConstructor.class, properties);
    }

    @Test
    public void validateWithDefaultConstructor() throws Exception {
        ExtensionClassInitializer.initialize(new ArrayList<>());
        ParProperties properties = ParProperties.createBasicParProperties("src/test/resources/par.properties", null);
        assertTrue(ParThreadContextClassLoader.createInstance(WithDefaultConstructor.class.getName(),
                WithDefaultConstructor.class, properties) instanceof WithDefaultConstructor);
    }

    @Test(expected = IllegalStateException.class)
    public void validateWithWrongConstructor() throws Exception {
        ExtensionClassInitializer.initialize(new ArrayList<>());
        ParProperties properties = ParProperties.createBasicParProperties("src/test/resources/par.properties", null);
        ParThreadContextClassLoader.createInstance(WrongConstructor.class.getName(), WrongConstructor.class, properties);
    }

    public static class WithPropertiesConstructor {
        public WithPropertiesConstructor(ParProperties properties) {
            if (properties.getProperty("fail") != null) {
                throw new RuntimeException("Intentional failure");
            }
        }
    }

    public static class WithDefaultConstructor {
        public WithDefaultConstructor() {

        }
    }

    public static class WrongConstructor {
        public WrongConstructor(String s) {

        }
    }
}
