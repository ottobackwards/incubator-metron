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

import org.apache.metron.par.util.ParProperties;
import org.junit.Test;

public class ExtensionClassInitializerTest {
  @Test(expected = NotInitializedException.class)
  public void testNotInitializedClassloader() throws Exception{
    ParProperties properties = ParProperties.createBasicParProperties("src/test/resources/par.properties", null);
    ParThreadContextClassLoader.createInstance(ParThreadContextClassLoaderTest.WithPropertiesConstructor.class.getName(),
            ParThreadContextClassLoaderTest.WithPropertiesConstructor.class, properties);
  }
  @Test(expected = NotInitializedException.class)
  public void testNotInitializedExtensionManager() throws Exception{
    ExtensionManager.getInstanceClassLoader("org.junit.Test");
  }
}