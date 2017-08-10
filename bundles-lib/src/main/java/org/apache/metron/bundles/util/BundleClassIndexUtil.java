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

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.metron.bundles.VFSBundleClassLoader;

public class BundleClassIndexUtil {

  public static Iterable<FileObject> getResources(FileObject bundleFile, String resourceFile) {
    Set<FileObject> resources = new HashSet<>();
    List<FileObject> resourceFiles = new ArrayList<>();
    FileSystemManager manager = bundleFile.getFileSystem().getFileSystemManager();
    try {
      if (manager.canCreateFileSystem(bundleFile)) {
        // create a Jar filesystem from the bundle
        FileObject fsBundleFile = manager.createFileSystem(bundleFile);

        // resolve the dependency directory within the bundle
        FileObject deps = fsBundleFile.resolveFile(VFSBundleClassLoader.DEPENDENCY_PATH);
        if (deps.exists() && deps.isFolder()) {
          FileObject[] depJars = deps.getChildren();
          for (FileObject jarFileObject : depJars) {
            // create a filesystem from each jar and add it as
            // a resource
            jarFileObject = manager.createFileSystem(jarFileObject);
            resources.add(jarFileObject);
          }
        }
      }
      for ( FileObject resource : resources){
        FileObject thisResourceFile = resource.resolveFile(resourceFile);
        if(thisResourceFile.exists()) {
          resourceFiles.add(thisResourceFile);
        }
      }
    }catch (FileSystemException fse) {
      resourceFiles.clear();
    }

    return resourceFiles;
  }

  public static FileObject getResource(FileObject bundleFile, String resourceFile) {
    Iterable<FileObject> resources = getResources(bundleFile,resourceFile);
    if(resources != null && resources.iterator().hasNext()) {
      Iterator<FileObject> it = resources.iterator();
      return it.next();
    }
    return null;
  }
}
