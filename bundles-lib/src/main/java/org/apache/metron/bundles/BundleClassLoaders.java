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
package org.apache.metron.bundles;

import java.net.URISyntaxException;
import java.util.*;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.metron.bundles.bundle.Bundle;
import org.apache.metron.bundles.bundle.BundleCoordinates;
import org.apache.metron.bundles.bundle.BundleDetails;
import org.apache.metron.bundles.util.BundleProperties;
import org.apache.metron.bundles.util.BundleSelector;
import org.apache.metron.bundles.util.FileUtils;
import org.apache.metron.bundles.util.BundleUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A singleton class used to initialize the extension and framework classloaders.
 */
public final class BundleClassLoaders {

  private static volatile BundleClassLoaders bundleClassLoaders;
  private volatile InitContext initContext;
  private static final Logger logger = LoggerFactory.getLogger(BundleClassLoaders.class);

  private final static class InitContext {

    private final List<FileObject> extensionDirs;
    private final Map<String, Bundle> bundles;
    private final Map<String, ClassLoader> coordinateClassLoaderLookup;
    private final Map<String, Set<BundleCoordinates>> idBundleLookup;
    private final BundleProperties properties;

    private InitContext(
        final List<FileObject> extensionDirs,
        final Map<String, Bundle> bundles,
        final Map<String, ClassLoader> coordinateClassLoaderLookup,
        final Map<String, Set<BundleCoordinates>> idBundleLookup,
        final BundleProperties properties) {
      this.extensionDirs = extensionDirs;
      this.bundles = bundles;
      this.coordinateClassLoaderLookup = coordinateClassLoaderLookup;
      this.idBundleLookup = idBundleLookup;
      this.properties = properties;
    }
  }

  private BundleClassLoaders() {
  }

  /**
   * @return The singleton instance of the BundleClassLoaders
   */
  public static BundleClassLoaders getInstance() {
    BundleClassLoaders result = bundleClassLoaders;
    if (result == null) {
      synchronized (BundleClassLoaders.class) {
        result = bundleClassLoaders;
        if (result == null) {
          bundleClassLoaders = new BundleClassLoaders();
          result = bundleClassLoaders;
        }
      }
    }
    return result;
  }

  /**
   * Uninitializes the BundleClassloaders.  After calling this <code>init</code> must be called
   * before the rest of the methods are called afterwards. This is for TESTING ONLY at this time.
   * Reset does not unload or clear any loaded classloaders.
   */

  public static void reset() {
    getInstance().unInit();
  }

  private void unInit() {
    synchronized (this) {
      initContext = null;
    }
  }

  /**
   * Initializes and loads the BundleClassLoaders. This method must be called before the rest of the
   * methods to access the classloaders are called.  Multiple calls to this method will have no
   * effect, unless a different set of extension directories is passed, which will result in an
   * <code>IllegaStateException</code>
   *
   * @param fileSystemManager the FileSystemManager
   * @param extensionsDirs where to find extension artifacts
   * @param props BundleProperties
   * @throws FileSystemException if any issue occurs while working with the bundle files.
   * @throws java.lang.ClassNotFoundException if unable to load class definition
   * @throws IllegalStateException when already initialized with a given set of extension
   * directories and extensionDirs does not match
   */
  public void init(final FileSystemManager fileSystemManager, final List<FileObject> extensionsDirs,
      BundleProperties props)
      throws FileSystemException, ClassNotFoundException, URISyntaxException {
    if (extensionsDirs == null || fileSystemManager == null) {
      throw new NullPointerException("cannot have empty arguments");
    }
    InitContext ic = initContext;
    if (ic == null) {
      synchronized (this) {
        ic = initContext;
        if (ic == null) {
          initContext = load(fileSystemManager, extensionsDirs, props);
          ic = initContext;
        }
      }
    } else {
      boolean matching = CollectionUtils
          .isEqualCollection(initContext.extensionDirs, extensionsDirs);
      if (!matching) {
        throw new IllegalStateException(
            "Cannot reinitialize and extension directories cannot change");
      }
      matching = initContext.properties.match(props);
      if (!matching) {
        throw new IllegalStateException(
            "Cannot re-initialize and properties cannot change");
      }
    }
  }

  private InitContext load(final FileSystemManager fileSystemManager,
      final List<FileObject> extensionsDirs, BundleProperties props)
      throws FileSystemException, ClassNotFoundException, URISyntaxException {
    // get the system classloader
    final ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();

    // find all bundle files and create class loaders for them.
    final Map<String, Bundle> directoryBundleLookup = new LinkedHashMap<>();
    final Map<String, ClassLoader> coordinateClassLoaderLookup = new HashMap<>();
    final Map<String, Set<BundleCoordinates>> idBundleLookup = new HashMap<>();

    for (FileObject extensionsDir : extensionsDirs) {
      // make sure the bundle directory is there and accessible
      FileUtils.ensureDirectoryExistAndCanRead(extensionsDir);

      final List<FileObject> bundleDirContents = new ArrayList<>();
      FileObject[] dirFiles = extensionsDir
          .findFiles(new BundleSelector(props.getArchiveExtension()));
      if (dirFiles != null) {
        List<FileObject> fileList = Arrays.asList(dirFiles);
        bundleDirContents.addAll(fileList);
      }

      if (!bundleDirContents.isEmpty()) {
        final List<BundleDetails> bundleDetails = new ArrayList<>();
        final Map<String, String> bundleCoordinatesToBundleFile = new HashMap<>();

        // load the bundle details which includes bundle dependencies
        for (final FileObject bundleFile : bundleDirContents) {
          if (!bundleFile.exists() || !bundleFile.isFile()) {
            continue;
          }
          BundleDetails bundleDetail = null;
          try {
            bundleDetail = getBundleDetails(bundleFile, props);
          } catch (IllegalStateException e) {
            logger.warn("Unable to load BUNDLE {} due to {}, skipping...",
                new Object[]{bundleFile.getURL(), e.getMessage()});
          }

          // prevent the application from starting when there are two BUNDLEs with same group, id, and version
          final String bundleCoordinate = bundleDetail.getCoordinates().getCoordinates();
          if (bundleCoordinatesToBundleFile.containsKey(bundleCoordinate)) {
            final String existingBundleWorkingDir = bundleCoordinatesToBundleFile
                .get(bundleCoordinate);
            throw new IllegalStateException(
                "Unable to load BUNDLE with coordinates " + bundleCoordinate
                    + " and bundle file " + bundleDetail.getBundleFile()
                    + " because another BUNDLE with the same coordinates already exists at "
                    + existingBundleWorkingDir);
          }

          bundleDetails.add(bundleDetail);
          bundleCoordinatesToBundleFile.put(bundleCoordinate,
              bundleDetail.getBundleFile().getURL().toURI().toString());
        }

        for (final Iterator<BundleDetails> bundleDetailsIter = bundleDetails.iterator();
            bundleDetailsIter.hasNext(); ) {
          final BundleDetails bundleDetail = bundleDetailsIter.next();
          // populate bundle lookup
          idBundleLookup.computeIfAbsent(bundleDetail.getCoordinates().getId(),
              id -> new HashSet<>()).add(bundleDetail.getCoordinates());
          directoryBundleLookup
              .put(bundleDetail.getBundleFile().getURL().toURI().toString(),
                  new Bundle(bundleDetail));
        }
      }
    }
      return new InitContext(extensionsDirs, new LinkedHashMap<>(directoryBundleLookup),
          new LinkedHashMap<>(coordinateClassLoaderLookup), new LinkedHashMap<>(idBundleLookup),
          props);
  }

  public ClassLoader createClassLoaderForBundle(Bundle bundle) {
    if(bundle.getClassLoader() != null) {
      return bundle.getClassLoader();
    }

    try {
      final FileObject bundleFile = bundle.getBundleDetails().getBundleFile();
      if (!bundleFile.exists() || !bundleFile.isFile()) {
        return null;
      }
      FileSystemManager fileSystemManager = bundleFile.getFileSystem().getFileSystemManager();
      final BundleDetails bundleDetail = bundle.getBundleDetails();
      final BundleCoordinates bundleDependencyCoordinate = bundleDetail
          .getDependencyCoordinates();

      // see if this class loader is eligible for loading
      ClassLoader potentialBundleClassLoader = null;
      if (bundleDependencyCoordinate == null) {
        potentialBundleClassLoader = createBundleClassLoader(fileSystemManager,
            bundleDetail.getBundleFile(), ClassLoader.getSystemClassLoader());
      } else {
        final String dependencyCoordinateStr = bundleDependencyCoordinate
            .getCoordinates();

        // if the declared dependency has already been loaded
        if (initContext.coordinateClassLoaderLookup.containsKey(dependencyCoordinateStr)) {
          final ClassLoader bundleDependencyClassLoader = initContext.coordinateClassLoaderLookup
              .get(dependencyCoordinateStr);
          potentialBundleClassLoader = createBundleClassLoader(
              fileSystemManager, bundleDetail.getBundleFile(),
              bundleDependencyClassLoader);
        } else {
          // get all bundles that match the declared dependency id
          final Set<BundleCoordinates> coordinates = initContext.idBundleLookup
              .get(bundleDependencyCoordinate.getId());

          // ensure there are known bundles that match the declared dependency id
          if (coordinates != null && !coordinates
              .contains(bundleDependencyCoordinate)) {
            // ensure the declared dependency only has one possible bundle
            if (coordinates.size() == 1) {
              // get the bundle with the matching id
              final BundleCoordinates coordinate = coordinates.stream()
                  .findFirst().get();

              // if that bundle is loaded, use it
              if (initContext.coordinateClassLoaderLookup
                  .containsKey(coordinate.getCoordinates())) {
                logger.warn(String.format(
                    "While loading '%s' unable to locate exact BUNDLE dependency '%s'. Only found one possible match '%s'. Continuing...",
                    bundleDetail.getCoordinates().getCoordinates(),
                    dependencyCoordinateStr,
                    coordinate.getCoordinates()));

                final ClassLoader bundleDependencyClassLoader = initContext.coordinateClassLoaderLookup
                    .get(coordinate.getCoordinates());
                potentialBundleClassLoader = createBundleClassLoader(
                    fileSystemManager, bundleDetail.getBundleFile(),
                    bundleDependencyClassLoader);
              }
            }
          }
        }
      }

      // if we were able to create the bundle class loader, store it and remove the details
      final ClassLoader bundleClassLoader = potentialBundleClassLoader;
      if (bundleClassLoader != null) {
        bundle.setClassLoader(bundleClassLoader);
        initContext.bundles
            .put(bundleDetail.getBundleFile().getURL().toURI().toString(),
                bundle);
        initContext.coordinateClassLoaderLookup
            .put(bundleDetail.getCoordinates().getCoordinates(),
                bundleClassLoader);
      }
      return bundleClassLoader;

    } catch (FileSystemException | ClassNotFoundException | URISyntaxException e) {
      return null;
    }
  }

  /**
   * Creates a new BundleClassLoader. The parentClassLoader may be null.
   *
   * @param bundleFile the Bundle File
   * @param parentClassLoader parent classloader of bundle
   * @return the bundle classloader
   * @throws FileSystemException ioe
   * @throws ClassNotFoundException cfne
   */
  private static ClassLoader createBundleClassLoader(final FileSystemManager fileSystemManager,
      final FileObject bundleFile, final ClassLoader parentClassLoader)
      throws FileSystemException, ClassNotFoundException {
    logger.debug("Loading Bundle file: " + bundleFile.getURL());
    final ClassLoader bundleClassLoader = new VFSBundleClassLoader.Builder()
        .withFileSystemManager(fileSystemManager)
        .withBundleFile(bundleFile)
        .withParentClassloader(parentClassLoader).build();
    logger.info(
        "Loaded Bundle file: " + bundleFile.getURL() + " as class loader " + bundleClassLoader);
    return bundleClassLoader;
  }

  /**
   * Loads the details for the specified BUNDLE. The details will be extracted from the manifest
   * file.
   *
   * @param bundleFile the bundle file
   * @return details about the Bundle
   * @throws FileSystemException ioe
   */
  private static BundleDetails getBundleDetails(final FileObject bundleFile, BundleProperties props)
      throws FileSystemException {
    return BundleUtil.fromBundleFile(bundleFile, props);
  }

  /**
   * @param extensionFile the bundle file
   * @return the bundle for the specified bundle file. Returns null when no bundle exists
   * @throws IllegalStateException if the bundles have not been loaded
   */
  public Bundle getBundle(final FileObject extensionFile) {
    if (initContext == null) {
      throw new IllegalStateException("Extensions class loaders have not been loaded.");
    }

    try {
      return initContext.bundles.get(extensionFile.getURL().toURI().toString());
    } catch (URISyntaxException | FileSystemException e) {
      if (logger.isDebugEnabled()) {
        logger.debug("Unable to get extension classloader for bundle '{}'",
            extensionFile.getName().toString());
      }
      return null;
    }
  }

  /**
   * @return the extensions that have been loaded
   * @throws IllegalStateException if the extensions have not been loaded
   */
  public Set<Bundle> getBundles() {
    if (initContext == null) {
      throw new IllegalStateException("Bundles have not been loaded.");
    }

    return new LinkedHashSet<>(initContext.bundles.values());
  }

}
