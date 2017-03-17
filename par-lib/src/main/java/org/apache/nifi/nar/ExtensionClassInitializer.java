package org.apache.nifi.nar;

import org.apache.nifi.util.FileUtilities;
import org.apache.nifi.util.FileUtils;

import java.util.List;

public class ExtensionClassInitializer {
  public static void initialize(List<Class> extentionsClassList){
    ExtensionManager.InitClassDefinitions(extentionsClassList);
    NarThreadContextClassLoader.InitClasses(extentionsClassList);
  }
  public static void initializeFileUtilities(FileUtilities utilities){
    FileUtils.init(utilities);
  }
}
