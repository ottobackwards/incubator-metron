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
package org.apache.metron.parsers.bolt;

import org.apache.commons.vfs2.FileSystemManager;
import org.apache.curator.framework.CuratorFramework;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.metron.bundles.*;
import org.apache.metron.bundles.bundle.Bundle;
import org.apache.metron.bundles.util.BundleProperties;
import org.apache.metron.bundles.util.HDFSFileUtilities;
import org.apache.metron.bundles.util.VFSClassloaderUtil;
import org.apache.metron.common.Constants;
import org.apache.metron.common.configuration.ConfigurationsUtils;
import org.apache.metron.common.configuration.SensorParserConfig;
import org.apache.metron.parsers.interfaces.MessageParser;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class ParserLoader {
  private static final Logger LOG = LoggerFactory.getLogger(ParserBolt.class);
  public static Optional<MessageParser<JSONObject>> loadParser(CuratorFramework client, SensorParserConfig parserConfig){
    MessageParser<JSONObject> parser = null;
    try {
      // fetch the BundleProperties from zookeeper
      Optional<BundleProperties> bundleProperties = getBundleProperties(client);
      if (bundleProperties.isPresent()) {
        // if we have the properties
        // setup the bundles
        BundleProperties props = bundleProperties.get();
        URI uri = props.getBundleLibraryDirectory();
        if (uri.getScheme().toLowerCase().startsWith("hdfs")) {
          Configuration fsConf = new Configuration();
          fsConf.set("fs.defaultFS", String.format("%s://%s",uri.getScheme(),uri.getAuthority()));

          FileSystem fileSystem = FileSystem.get(fsConf);
          // need to setup the filesystem from hdfs
          ExtensionClassInitializer.initializeFileUtilities(new HDFSFileUtilities(fileSystem));
        }
        FileSystemManager fileSystemManager = VFSClassloaderUtil.generateVfs(props.getArchiveExtension());

        ArrayList<Class> classes = new ArrayList<>();
        classes.add(MessageParser.class);
        // future
        //classes.add(StellarFunction.class);

        ExtensionClassInitializer.initialize(classes);

        // create a FileSystemManager
        Bundle systemBundle = ExtensionManager.createSystemBundle(fileSystemManager, props);
        ExtensionMapping mapping = BundleUnpacker.unpackBundles(fileSystemManager, systemBundle, props);
        BundleClassLoaders.getInstance().init(fileSystemManager,fileSystemManager.resolveFile(props.getFrameworkWorkingDirectory()),fileSystemManager.resolveFile(props.getExtensionsWorkingDirectory()),props);

        ExtensionManager.discoverExtensions(systemBundle, BundleClassLoaders.getInstance().getBundles());


        parser = BundleThreadContextClassLoader.createInstance(parserConfig.getParserClassName(),MessageParser.class,props);

      }else{
        LOG.error("BundleProperties are missing!");
      }
    }catch(Exception e){
      LOG.error("Failed to load parser " + parserConfig.getParserClassName(),e);
      return Optional.empty();
    }
    return Optional.of(parser);
  }

  private static Optional<BundleProperties> getBundleProperties(CuratorFramework client) throws Exception{
    BundleProperties properties = null;
    byte[] propBytes = ConfigurationsUtils.readFromZookeeper(Constants.ZOOKEEPER_ROOT + "/bundle.properties",client);
    if(propBytes.length > 0 ) {
      // read in the properties
      properties = BundleProperties.createBasicBundleProperties(new ByteArrayInputStream(propBytes),new HashMap<>());
    }
    return Optional.of(properties);
  }
}
