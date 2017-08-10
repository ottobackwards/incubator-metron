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

package org.apache.metron.bundles.util;

import static java.lang.Class.forName;
import static org.apache.metron.bundles.util.TestUtil.loadSpecifiedProperties;
import static org.junit.Assert.*;

import java.lang.annotation.Annotation;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.metron.bundles.annotation.behavior.RequiresInstanceClassLoading;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ClassIndexUtilTest {
  static final Map<String, String> EMPTY_MAP = new HashMap<String, String>();

  FileSystemManager fileSystemManager;
  FileObject bundleFile;
  String className;
  @Before
  public void setUp() throws Exception {
    ResourceCopier.copyResources(Paths.get("./src/test/resources"),Paths.get("./target"));
    BundleProperties properties = loadSpecifiedProperties("/BundleMapper/conf/bundle.properties",
        EMPTY_MAP);

    assertEquals("./target/BundleMapper/lib/",
        properties.getProperty("bundle.library.directory"));
    assertEquals("./target/BundleMapper/lib2/",
        properties.getProperty("bundle.library.directory.alt"));

    fileSystemManager = FileSystemManagerFactory.createFileSystemManager(new String[] {properties.getArchiveExtension()});
    FileObject lib = fileSystemManager.resolveFile(properties.getBundleLibraryDirectory());
    bundleFile = lib.resolveFile("metron-parser-bar-bundle-0.4.1.bundle");
    Assert.assertTrue(bundleFile.exists());
    className = properties.getProperty("bundle.extension.type.parser");
  }

  @Test
  public void getSubclassesNames() throws Exception {
    Iterable<String> it = ClassIndexUtil.getSubclassesNames(forName(className),bundleFile);
    int count = 0;
    String name = null;
    for(String thisName : it){
      count++;
      name = thisName;
    }
    Assert.assertTrue(count == 1);
    Assert.assertEquals("org.apache.metron.bar.BarParser",name);
  }

  @Test
  public void getPackageClassesNames() throws Exception {
    Iterable<String> it = ClassIndexUtil.getPackageClassesNames("org.apache.metron.parsers",bundleFile);
    int count = 0;
    String name = null;
    for(String thisName : it){
      count++;
      name = thisName;
    }
    Assert.assertTrue(count == 0);
  }

  @Test
  public void getAnnotatedNames() throws Exception {
    Iterable<String> it = ClassIndexUtil.getAnnotatedNames(RequiresInstanceClassLoading.class,bundleFile);
    int count = 0;
    String name = null;
    for(String thisName : it){
      count++;
      name = thisName;
    }
    Assert.assertTrue(count == 0);
  }

  @Test
  public void getClassSummary() throws Exception {
    String summary = ClassIndexUtil.getClassSummary(Class.forName(className),bundleFile);
    Assert.assertNull(summary);
  }

}