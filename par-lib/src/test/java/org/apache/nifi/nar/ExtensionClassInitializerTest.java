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
package org.apache.nifi.nar;

import org.apache.nifi.util.NiFiProperties;
import org.junit.Test;

import static org.junit.Assert.*;

public class ExtensionClassInitializerTest {
  @Test(expected = NotInitializedException.class)
  public void testNotInitializedClassloader() throws Exception{
    NiFiProperties properties = NiFiProperties.createBasicNiFiProperties("src/test/resources/nifi.properties", null);
    NarThreadContextClassLoader.createInstance(NarThreadContextClassLoaderTest.WithPropertiesConstructor.class.getName(),
            NarThreadContextClassLoaderTest.WithPropertiesConstructor.class, properties);
  }
  @Test(expected = NotInitializedException.class)
  public void testNotInitializedExtensionManager() throws Exception{
    ExtensionManager.getClassLoader("org.junit.Test");
  }
}