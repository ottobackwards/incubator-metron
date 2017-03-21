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
package org.apache.metron.par;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.metron.par.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A singleton class used to initialize the extension and framework
 * classloaders.
 */
public final class ParClassLoaders {

    // OPF: This needs to be extendable or go
    public static final String FRAMEWORK_PAR_ID = "nifi-framework-nar";
    public static final String JETTY_PAR_ID = "nifi-jetty-bundle";

    private static volatile ParClassLoaders ncl;
    private volatile InitContext initContext;
    private static final Logger logger = LoggerFactory.getLogger(ParClassLoaders.class);

    private final static class InitContext {

        private final FileObject frameworkWorkingDir;
        private final FileObject extensionWorkingDir;
        private final ClassLoader frameworkClassLoader;
        private final Map<String, ClassLoader> extensionClassLoaders;

        private InitContext(
                final FileObject frameworkDir,
                final FileObject extensionDir,
                final ClassLoader frameworkClassloader,
                final Map<String, ClassLoader> extensionClassLoaders) {
            this.frameworkWorkingDir = frameworkDir;
            this.extensionWorkingDir = extensionDir;
            this.frameworkClassLoader = frameworkClassloader;
            this.extensionClassLoaders = extensionClassLoaders;
        }
    }

    private ParClassLoaders() {
    }

    /**
     * @return The singleton instance of the ParClassLoaders
     */
    public static ParClassLoaders getInstance() {
        ParClassLoaders result = ncl;
        if (result == null) {
            synchronized (ParClassLoaders.class) {
                result = ncl;
                if (result == null) {
                    ncl = result = new ParClassLoaders();
                }
            }
        }
        return result;
    }

    /**
     * Initializes and loads the ParClassLoaders. This method must be called
     * before the rest of the methods to access the classloaders are called and
     * it can be safely called any number of times provided the same framework
     * and extension working dirs are used.
     *
     * @param frameworkWorkingDir where to find framework artifacts
     * @param extensionsWorkingDir where to find extension artifacts
     * @throws FileSystemException if any issue occurs while exploding par working directories.
     * @throws java.lang.ClassNotFoundException if unable to load class definition
     * @throws IllegalStateException already initialized with a given pair of
     * directories cannot reinitialize or use a different pair of directories.
     */
    public void init(final FileSystemManager fileSystemManager, final FileObject frameworkWorkingDir, final FileObject extensionsWorkingDir) throws FileSystemException, ClassNotFoundException {
        if (frameworkWorkingDir == null || extensionsWorkingDir == null || fileSystemManager == null) {
            throw new NullPointerException("cannot have empty arguments");
        }
        InitContext ic = initContext;
        if (ic == null) {
            synchronized (this) {
                ic = initContext;
                if (ic == null) {
                    initContext = ic = load(fileSystemManager, frameworkWorkingDir, extensionsWorkingDir);
                }
            }
        }
        boolean matching = initContext.extensionWorkingDir.equals(extensionsWorkingDir)
                && initContext.frameworkWorkingDir.equals(frameworkWorkingDir);
        if (!matching) {
            throw new IllegalStateException("Cannot reinitialize and extension/framework directories cannot change");
        }
    }

    /**
     * Should be called at most once.
     */
    private InitContext load(final FileSystemManager fileSystemManager, final FileObject frameworkWorkingDir, final FileObject extensionsWorkingDir) throws FileSystemException, ClassNotFoundException {
        // get the system classloader
        final ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();

        // find all par files and create class loaders for them.
        final Map<String, ClassLoader> extensionDirectoryClassLoaderLookup = new LinkedHashMap<>();
        final Map<String, ClassLoader> parIdClassLoaderLookup = new HashMap<>();

        // make sure the par directory is there and accessible
        FileUtils.ensureDirectoryExistAndCanAccess(frameworkWorkingDir);
        FileUtils.ensureDirectoryExistAndCanAccess(extensionsWorkingDir);

        final List<FileObject> parWorkingDirContents = new ArrayList<>();
        final FileObject[] frameworkWorkingDirContents = frameworkWorkingDir.getChildren();
        if (frameworkWorkingDirContents != null) {
            parWorkingDirContents.addAll(Arrays.asList(frameworkWorkingDirContents));
        }
        final FileObject[] extensionsWorkingDirContents = extensionsWorkingDir.getChildren();
        if (extensionsWorkingDirContents != null) {
            parWorkingDirContents.addAll(Arrays.asList(extensionsWorkingDirContents));
        }

        if (!parWorkingDirContents.isEmpty()) {
            final List<ParDetails> parDetails = new ArrayList<>();

            // load the par details which includes and par dependencies
            for (final FileObject unpackedPar : parWorkingDirContents) {
                final ParDetails parDetail = getParDetails(unpackedPar);

                // ensure the par contained an identifier
                if (parDetail.getParId() == null) {
                    logger.warn("No PAR Id found. Skipping: " + unpackedPar.getURL());
                    continue;
                }

                // store the par details
                parDetails.add(parDetail);
            }

            // attempt to locate the jetty par
            ClassLoader jettyClassLoader = null;
            for (final Iterator<ParDetails> parDetailsIter = parDetails.iterator(); parDetailsIter.hasNext();) {
                final ParDetails parDetail = parDetailsIter.next();

                // look for the jetty par
                if (JETTY_PAR_ID.equals(parDetail.getParId())) {
                    // create the jetty classloader
                    jettyClassLoader = createParClassLoader(fileSystemManager, parDetail.getParWorkingDirectory(), systemClassLoader);

                    // remove the jetty par since its already loaded
                    parIdClassLoaderLookup.put(parDetail.getParId(), jettyClassLoader);
                    parDetailsIter.remove();
                    break;
                }
            }

            // ensure the jetty par was found
            if (jettyClassLoader == null) {
                throw new IllegalStateException("Unable to locate Jetty bundle.");
            }

            int parCount;
            do {
                // record the number of pars to be loaded
                parCount = parDetails.size();

                // attempt to create each par class loader
                for (final Iterator<ParDetails> parDetailsIter = parDetails.iterator(); parDetailsIter.hasNext();) {
                    final ParDetails parDetail = parDetailsIter.next();
                    final String parDependencies = parDetail.getParDependencyId();

                    // see if this class loader is eligible for loading
                    ClassLoader parClassLoader = null;
                    if (parDependencies == null) {
                        parClassLoader = createParClassLoader(fileSystemManager,parDetail.getParWorkingDirectory(), jettyClassLoader);
                    } else if (parIdClassLoaderLookup.containsKey(parDetail.getParDependencyId())) {
                        parClassLoader = createParClassLoader(fileSystemManager, parDetail.getParWorkingDirectory(), parIdClassLoaderLookup.get(parDetail.getParDependencyId()));
                    }

                    // if we were able to create the par class loader, store it and remove the details
                    if (parClassLoader != null) {
                        extensionDirectoryClassLoaderLookup.put(parDetail.getParWorkingDirectory().getURL().toString(), parClassLoader);
                        parIdClassLoaderLookup.put(parDetail.getParId(), parClassLoader);
                        parDetailsIter.remove();
                    }
                }

                // attempt to load more if some were successfully loaded this iteration
            } while (parCount != parDetails.size());

            // see if any pars couldn't be loaded
            for (final ParDetails parDetail : parDetails) {
                logger.warn(String.format("Unable to resolve required dependency '%s'. Skipping PAR %s", parDetail.getParDependencyId(), parDetail.getParWorkingDirectory().getURL()));
            }
        }

        return new InitContext(frameworkWorkingDir, extensionsWorkingDir, parIdClassLoaderLookup.get(FRAMEWORK_PAR_ID), new LinkedHashMap<>(extensionDirectoryClassLoaderLookup));
    }

    /**
     * Creates a new ParClassLoader. The parentClassLoader may be null.
     *
     * @param parDirectory root directory of par
     * @param parentClassLoader parent classloader of par
     * @return the par classloader
     * @throws FileSystemException ioe
     * @throws ClassNotFoundException cfne
     */
    private static ClassLoader createParClassLoader(final FileSystemManager fileSystemManager, final FileObject parDirectory, final ClassLoader parentClassLoader) throws FileSystemException, ClassNotFoundException {
        logger.debug("Loading PAR file: " + parDirectory.getURL());
        ParClassLoader.Builder builder = new ParClassLoader.Builder()
                .withFileSystemManager(fileSystemManager)
                .withParWorkingDirectory(parDirectory)
                .withParentClassloader(parentClassLoader);
        final ClassLoader parClassLoader = builder.build();
        logger.info("Loaded PAR file: " + parDirectory.getURL() + " as class loader " + parClassLoader);
        return parClassLoader;
    }

    /**
     * Loads the details for the specified PAR. The details will be extracted
     * from the manifest file.
     *
     * @param parDirectory the par directory
     * @return details about the PAR
     * @throws FileSystemException ioe
     */
    private static ParDetails getParDetails(final FileObject parDirectory) throws FileSystemException {
        final ParDetails parDetails = new ParDetails();
        parDetails.setParWorkingDirectory(parDirectory);

        final FileObject manifestFile = parDirectory.resolveFile("META-INF/MANIFEST.MF");
        try (final InputStream fis = manifestFile.getContent().getInputStream()) {
            final Manifest manifest = new Manifest(fis);
            final Attributes attributes = manifest.getMainAttributes();

            // get the par details
            parDetails.setParId(attributes.getValue("Par-Id"));
            parDetails.setParDependencyId(attributes.getValue("Par-Dependency-Id"));
        }catch(IOException ioe){
            throw new FileSystemException("failed reading manifest file " + manifestFile.getURL(),ioe);
        }

        return parDetails;
    }

    /**
     * @return the framework class loader
     *
     * @throws IllegalStateException if the frame class loader has not been
     * loaded
     */
    public ClassLoader getFrameworkClassLoader() {
        if (initContext == null) {
            throw new IllegalStateException("Framework class loader has not been loaded.");
        }

        return initContext.frameworkClassLoader;
    }

    /**
     * @param extensionWorkingDirectory the directory
     * @return the class loader for the specified working directory. Returns
     * null when no class loader exists for the specified working directory
     * @throws IllegalStateException if the class loaders have not been loaded
     */
    public ClassLoader getExtensionClassLoader(final FileObject extensionWorkingDirectory) {
        if (initContext == null) {
            throw new IllegalStateException("Extensions class loaders have not been loaded.");
        }

        try {
            return initContext.extensionClassLoaders.get(extensionWorkingDirectory.getURL().toString());
        } catch (final IOException ioe) {
            if(logger.isDebugEnabled()){
                logger.debug("Unable to get extension classloader for working directory '{}'", extensionWorkingDirectory);
            }
            return null;
        }
    }

    /**
     * @return the extension class loaders
     * @throws IllegalStateException if the class loaders have not been loaded
     */
    public Set<ClassLoader> getExtensionClassLoaders() {
        if (initContext == null) {
            throw new IllegalStateException("Extensions class loaders have not been loaded.");
        }

        return new LinkedHashSet<>(initContext.extensionClassLoaders.values());
    }

    private static class ParDetails {

        private String parId;
        private String parDependencyId;
        private FileObject parWorkingDirectory;

        public String getParDependencyId() {
            return parDependencyId;
        }

        public void setParDependencyId(String parDependencyId) {
            this.parDependencyId = parDependencyId;
        }

        public String getParId() {
            return parId;
        }

        public void setParId(String parId) {
            this.parId = parId;
        }

        public FileObject getParWorkingDirectory() {
            return parWorkingDirectory;
        }

        public void setParWorkingDirectory(FileObject parWorkingDirectory) {
            this.parWorkingDirectory = parWorkingDirectory;
        }
    }

}
