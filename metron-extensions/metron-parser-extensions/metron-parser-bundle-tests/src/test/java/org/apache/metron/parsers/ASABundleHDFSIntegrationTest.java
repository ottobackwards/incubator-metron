package org.apache.metron.parsers;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.apache.hadoop.fs.permission.FsAction;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.metron.bundles.ExtensionClassInitializer;
import org.apache.metron.bundles.util.HDFSFileUtilities;
import org.apache.metron.integration.components.MRComponent;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ASABundleHDFSIntegrationTest {
  static final Map<String,String> EMPTY_MAP = new HashMap<String,String>();
  static MRComponent component;
  static Configuration configuration;
  static FileSystem fileSystem;
  @BeforeClass
  public static void setup() {
 /*   component = new MRComponent().withBasePath("target/hdfs");
    component.start();
    configuration = component.getConfiguration();

    try {
      fileSystem = FileSystem.newInstance(configuration);
      fileSystem.mkdirs(new Path("/work/"),new FsPermission(FsAction.READ_WRITE,FsAction.READ_WRITE,FsAction.READ_WRITE));
      fileSystem.copyFromLocalFile(new Path("src/test/resources/bundle.properties"), new Path("/work/"));
      fileSystem.copyFromLocalFile(new Path("src/test/resources/BundleUnpacker/lib/"), new Path("/"));
      fileSystem.copyFromLocalFile(new Path("src/test/resources/BundleUnpacker/lib2/"), new Path("/"));
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
    */
  }
  @Test
  public void testHDFS(){

  }

  @Test
  public void testLocal(){
    // setup a local lib etc set of dirs
    // copy the asa nar there

    // put the properties into zookeeper
    // create the parser topology

    // start
  }
}
