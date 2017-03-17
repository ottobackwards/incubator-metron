package org.apache.nifi.nar.util;

import org.apache.nifi.nar.integration.NarUnpackerIntegrationTest;
import org.apache.nifi.util.NiFiProperties;

import java.net.URISyntaxException;
import java.util.Map;

public class TestUtil {
  public static NiFiProperties loadSpecifiedProperties(final String propertiesFile, final Map<String, String> others) {
    String filePath;
    try {
      filePath = NarUnpackerIntegrationTest.class.getResource(propertiesFile).toURI().getPath();
    } catch (URISyntaxException ex) {
      throw new RuntimeException("Cannot load properties file due to "
              + ex.getLocalizedMessage(), ex);
    }
    return NiFiProperties.createBasicNiFiProperties(filePath, others);
  }
}
