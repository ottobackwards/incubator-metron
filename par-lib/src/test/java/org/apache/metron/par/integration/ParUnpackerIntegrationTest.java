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
package org.apache.metron.par.integration;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.apache.hadoop.fs.permission.FsAction;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.metron.par.ExtensionMapping;
import org.apache.metron.par.ParUnpacker;
import org.apache.metron.par.integration.components.MRComponent;
import org.apache.metron.par.util.HDFSFileUtilities;
import org.apache.metron.par.util.ParProperties;
import org.apache.metron.par.util.VFSClassloaderUtil;
import org.apache.metron.par.ExtensionClassInitializer;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.apache.metron.par.util.TestUtil.loadSpecifiedProperties;
import static org.junit.Assert.*;

public class ParUnpackerIntegrationTest {
  static MRComponent component;
  static Configuration configuration;
  static FileSystem fileSystem;
  @BeforeClass
  public static void setup() {
    component = new MRComponent().withBasePath("target/hdfs");
    component.start();
    configuration = component.getConfiguration();

    try {
      fileSystem = FileSystem.newInstance(configuration);
      fileSystem.mkdirs(new Path("/work/"),new FsPermission(FsAction.READ_WRITE,FsAction.READ_WRITE,FsAction.READ_WRITE));
      fileSystem.copyFromLocalFile(new Path("src/test/resources/par.properties"), new Path("/work/"));
      fileSystem.copyFromLocalFile(new Path("src/test/resources/ParUnpacker/lib/"), new Path("/"));
      fileSystem.copyFromLocalFile(new Path("src/test/resources/ParUnpacker/lib2/"), new Path("/"));
      RemoteIterator<LocatedFileStatus> files = fileSystem.listFiles(new Path("/"),true);
      System.out.println("==============(BEFORE)==============");
      while (files.hasNext()){
        LocatedFileStatus fileStat = files.next();
        System.out.println(fileStat.getPath().toString());
      }
      ExtensionClassInitializer.initializeFileUtilities(new HDFSFileUtilities(fileSystem));
    } catch (IOException e) {
      throw new RuntimeException("Unable to start cluster", e);
    }
  }

  @AfterClass
  public static void teardown(){
    try {
      RemoteIterator<LocatedFileStatus> files = fileSystem.listFiles(new Path("/"), true);
      System.out.println("==============(AFTER)==============");
      while (files.hasNext()) {
        LocatedFileStatus fileStat = files.next();
        System.out.println(fileStat.getPath().toString());
      }
    }catch(Exception e){}
    component.stop();
  }

  @Test
  public void testUnpackPars() throws Exception {
    // we unpack TWICE, because the code is different
    // if the files are there already
    // this still doesn't test what happens
    // if the HASH is different

    unpackPars();
    unpackPars();
  }
  public void unpackPars() throws Exception {
    // setup properties
    ParProperties properties = loadSpecifiedProperties("/ParUnpacker/conf/par.properties", Collections.EMPTY_MAP);
    // get the port we ended up with and set the paths
    properties.setProperty(ParProperties.PAR_LIBRARY_DIRECTORY,configuration.get("fs.defaultFS") + "/lib/");
    properties.setProperty(ParProperties.PAR_LIBRARY_DIRECTORY_PREFIX + "alt",configuration.get("fs.defaultFS") + "/lib2/");
    properties.setProperty(ParProperties.PAR_WORKING_DIRECTORY,configuration.get("fs.defaultFS") + "/work/");
    properties.setProperty(ParProperties.COMPONENT_DOCS_DIRECTORY,configuration.get("fs.defaultFS") + "/work/docs/components/");
    FileSystemManager fileSystemManager = VFSClassloaderUtil.generateVfs(properties.getProperty(ParProperties.ARCHIVE_EXTENSION));
    final ExtensionMapping extensionMapping = ParUnpacker.unpackPars(fileSystemManager,properties);

    assertEquals(2, extensionMapping.getAllExtensionNames().size());

    assertTrue(extensionMapping.getAllExtensionNames().contains(
            "org.apache.nifi.processors.dummy.one"));
    assertTrue(extensionMapping.getAllExtensionNames().contains(
            "org.apache.nifi.processors.dummy.two"));
    final FileObject extensionsWorkingDir = fileSystemManager.resolveFile(properties.getExtensionsWorkingDirectory());
    FileObject[] extensionFiles = extensionsWorkingDir.getChildren();

    Set<String> expectedPars = new HashSet<>();
    expectedPars.add("dummy-one.foo-unpacked");
    expectedPars.add("dummy-two.foo-unpacked");
    assertEquals(expectedPars.size(), extensionFiles.length);

    for (FileObject extensionFile : extensionFiles) {
      Assert.assertTrue(expectedPars.contains(extensionFile.getName().getBaseName()));
    }
  }
}