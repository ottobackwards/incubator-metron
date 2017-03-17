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