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

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.jar.*;

import org.apache.commons.io.IOUtils;
import org.apache.commons.vfs2.*;
import org.apache.metron.par.util.StringUtils;
import org.apache.metron.par.util.FileUtils;
import org.apache.metron.par.util.ParProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public final class ParUnpacker {

    private static final Logger logger = LoggerFactory.getLogger(ParUnpacker.class);
    private static String HASH_FILENAME = "par-md5sum";
    private static String archiveExtension = ParProperties.DEFAULT_ARCHIVE_EXTENSION;
    private static final FileSelector PAR_FILTER = new FileSelector() {
        @Override
        public boolean includeFile(FileSelectInfo fileSelectInfo) throws Exception {
            final String nameToTest = fileSelectInfo.getFile().getName().getExtension();
            return nameToTest.equals(archiveExtension) && fileSelectInfo.getFile().isFile();
        }

        @Override
        public boolean traverseDescendents(FileSelectInfo fileSelectInfo) throws Exception {
            return true;
        }
    };

    public static ExtensionMapping unpackPars(final FileSystemManager fileSystemManager, ParProperties props){
        try{
            final List<URI> parLibraryDirs = props.getParLibraryDirectories();
            final URI frameworkWorkingDir = props.getFrameworkWorkingDirectory();
            final URI extensionsWorkingDir = props.getExtensionsWorkingDirectory();
            final URI docsWorkingDir = props.getComponentDocumentationWorkingDirectory();
            archiveExtension = props.getArchiveExtension();

            FileObject unpackedFramework = null;
            final FileObject frameworkWorkingDirFO = fileSystemManager.resolveFile(frameworkWorkingDir);
            final FileObject extensionsWorkingDirFO = fileSystemManager.resolveFile(extensionsWorkingDir);
            final FileObject docsWorkingDirFO = fileSystemManager.resolveFile(docsWorkingDir);
            final Set<FileObject> unpackedExtensions = new HashSet<>();
            final List<FileObject> parFiles = new ArrayList<>();

            // make sure the par directories are there and accessible
            FileUtils.ensureDirectoryExistAndCanReadAndWrite(frameworkWorkingDirFO);
            FileUtils.ensureDirectoryExistAndCanReadAndWrite(extensionsWorkingDirFO);
            FileUtils.ensureDirectoryExistAndCanReadAndWrite(docsWorkingDirFO);

            for (URI parLibraryDir : parLibraryDirs) {

                FileObject parDir = fileSystemManager.resolveFile(parLibraryDir);

                // Test if the source PARs can be read
                FileUtils.ensureDirectoryExistAndCanRead(parDir);

                FileObject[] dirFiles = parDir.findFiles(PAR_FILTER);
                if (dirFiles != null) {
                    List<FileObject> fileList = Arrays.asList(dirFiles);
                    parFiles.addAll(fileList);
                }
            }

            if (!parFiles.isEmpty()) {
                final long startTime = System.nanoTime();
                logger.info("Expanding " + parFiles.size() + " PAR files with all processors...");
                for (FileObject parFile : parFiles) {
                    logger.debug("Expanding PAR file: " + parFile.getURL().toString());

                    // get the manifest for this par
                    Manifest manifest = null;
                    try(JarInputStream jis = new JarInputStream(parFile.getContent().getInputStream())) {
                        JarEntry je;
                        manifest = jis.getManifest();
                        if(manifest == null){
                            while((je=jis.getNextJarEntry())!=null){
                                if(je.getName().equals("META-INF/MANIFEST.MF")){
                                    manifest = new Manifest(jis);
                                    break;
                                }
                            }
                        }
                    }
                        final String parId = manifest.getMainAttributes().getValue(props.getMetaIdPrefix() + "-Id");
                        // determine if this is the framework
                        /* OPF extension point
                        if (ParClassLoaders.FRAMEWORK_PAR_ID.equals(parId)) {
                            if (unpackedFramework != null) {
                                throw new IllegalStateException(
                                        "Multiple framework PARs discovered. Only one framework is permitted.");
                            }

                            unpackedFramework = unpackPar(parFile, frameworkWorkingDirFO);
                        } else {
                            unpackedExtensions.add(unpackPar(parFile, extensionsWorkingDirFO));
                        }
                        */
                    unpackedExtensions.add(unpackPar(parFile, extensionsWorkingDirFO));
                }

                /*
                // ensure we've found the framework par
                if (unpackedFramework == null) {
                    throw new IllegalStateException("No framework PAR found.");
                } else if (!unpackedFramework.isReadable()) {
                    throw new IllegalStateException("Framework PAR cannot be read.");
                }
                */

                // Determine if any pars no longer exist and delete their
                // working directories. This happens
                // if a new version of a par is dropped into the lib dir.
                // ensure no old framework are present
                final FileObject[] frameworkWorkingDirContents = frameworkWorkingDirFO.getChildren();
                if (frameworkWorkingDirContents != null) {
                    for (final FileObject unpackedPar : frameworkWorkingDirContents) {
                      // OPF  if (!unpackedFramework.equals(unpackedPar)) {
                            FileUtils.deleteFile(unpackedPar, true);
                      // OPF  }
                    }
                }

                // ensure no old extensions are present
                final FileObject[] extensionsWorkingDirContents = extensionsWorkingDirFO.getChildren();
                if (extensionsWorkingDirContents != null) {
                    for (final FileObject unpackedPar : extensionsWorkingDirContents) {
                        if (!unpackedExtensions.contains(unpackedPar)) {
                            FileUtils.deleteFile(unpackedPar, true);
                        }
                    }
                }
                final long endTime = System.nanoTime();
                logger.info("PAR loading process took " + (endTime - startTime) + " nanoseconds.");
            }

            // attempt to delete any docs files that exist so that any
            // components that have been removed
            // will no longer have entries in the docs folder
            final FileObject[] docsFiles = docsWorkingDirFO.getChildren();
            if (docsFiles != null) {
                for (final FileObject file : docsFiles) {
                    FileUtils.deleteFile(file, true);
                }
            }
            final ExtensionMapping extensionMapping = new ExtensionMapping();
            mapExtensions(extensionsWorkingDirFO, docsWorkingDirFO, extensionMapping, props);
            return extensionMapping;
        } catch (IOException | URISyntaxException | NotInitializedException e) {
            logger.warn("Unable to load PAR library bundles due to " + e
                    + " Will proceed without loading any further Par bundles");
            if (logger.isDebugEnabled()) {
                logger.warn("", e);
            }
        }
        return null;
    }

    private static void mapExtensions(final FileObject workingDirectory, final FileObject docsDirectory,
            final ExtensionMapping mapping, final ParProperties props) throws IOException {
        final FileObject[] directoryContents = workingDirectory.getChildren();
        if (directoryContents != null) {
            for (final FileObject file : directoryContents) {
                if (file.isFolder()) {
                    mapExtensions(file, docsDirectory, mapping, props);
                } else if (file.getName().getExtension().equals("jar")) {
                    unpackDocumentation(file, docsDirectory, mapping, props);
                }
            }
        }
    }

    /**
     * Unpacks the specified par into the specified base working directory.
     *
     * @param par
     *            the par to unpack
     * @param baseWorkingDirectory
     *            the directory to unpack to
     * @return the directory to the unpacked par
     * @throws IOException
     *             if unable to explode par
     */
    private static FileObject unpackPar(final FileObject par, final FileObject baseWorkingDirectory)
            throws IOException, NotInitializedException {

        final FileObject parWorkingDirectory = baseWorkingDirectory.resolveFile(par.getName().getBaseName() + "-unpacked");

        // if the working directory doesn't exist, unpack the par
        if (!parWorkingDirectory.exists()) {
            unpack(par, parWorkingDirectory, calculateMd5sum(par));
        } else {
            // the working directory does exist. Run MD5 sum against the par
            // file and check if the par has changed since it was deployed.
            final byte[] parMd5 = calculateMd5sum(par);
            final FileObject workingHashFile = parWorkingDirectory.getChild(HASH_FILENAME);
            if (!workingHashFile.exists()) {
                FileUtils.deleteFile(parWorkingDirectory, true);
                unpack(par, parWorkingDirectory, parMd5);
            } else {
                final byte[] hashFileContents = IOUtils.toByteArray(workingHashFile.getContent().getInputStream());
                if (!Arrays.equals(hashFileContents, parMd5)) {
                    logger.info("Contents of par {} have changed. Reloading.",
                            new Object[] { par.getURL() });
                    FileUtils.deleteFile(parWorkingDirectory, true);
                    unpack(par, parWorkingDirectory, parMd5);
                }
            }
        }

        return parWorkingDirectory;
    }

    /**
     * Unpacks the PAR to the specified directory. Creates a checksum file that
     * used to determine if future expansion is necessary.
     *
     * @param workingDirectory
     *            the root directory to which the PAR should be unpacked.
     * @throws IOException
     *             if the PAR could not be unpacked.
     */
    private static void unpack(final FileObject par, final FileObject workingDirectory, final byte[] hash)
            throws IOException {

        try(JarInputStream jarFile = new JarInputStream(par.getContent().getInputStream())){
            JarEntry je;
            while((je=jarFile.getNextJarEntry())!=null){
                String name = je.getName();
                FileObject f = workingDirectory.resolveFile(name);
                if (je.isDirectory()) {
                    FileUtils.ensureDirectoryExistAndCanAccess(f);
                } else {
                    makeFile(jarFile, f);
                }
            }
            jarFile.close();
        }

        final FileObject hashFile = workingDirectory.resolveFile(HASH_FILENAME);
        FileUtils.createFile(hashFile, hash);
    }

    private static void unpackDocumentation(final FileObject jar, final FileObject docsDirectory,
            final ExtensionMapping extensionMapping, final ParProperties props) throws IOException {
        // determine the components that may have documentation
        if (!determineDocumentedComponents(jar, extensionMapping, props)) {
            return;
        }

        // look for all documentation related to each component
        try (final JarInputStream jarFile = new JarInputStream(jar.getContent().getInputStream())) {
            for (final String componentName : extensionMapping.getAllExtensionNames()) {
                final String entryName = "docs/" + componentName;

                // go through each entry in this jar
                JarEntry jarEntry;
                while ((jarEntry = jarFile.getNextJarEntry()) != null) {

                    // if this entry is documentation for this component
                    if (jarEntry.getName().startsWith(entryName)) {
                        final String name = StringUtils.substringAfter(jarEntry.getName(), "docs/");

                        // if this is a directory create it
                        if (jarEntry.isDirectory()) {
                            final FileObject componentDocsDirectory = docsDirectory.getChild(name);

                            // ensure the documentation directory can be created
                            if (!componentDocsDirectory.exists()) {
                                componentDocsDirectory.createFolder();
                                if (!componentDocsDirectory.exists()) {
                                    logger.warn("Unable to create docs directory "
                                            + componentDocsDirectory.getURL());

                                    break;
                                }
                            } else {
                                // if this is a file, write to it
                                final FileObject componentDoc = docsDirectory.getChild(name);
                                makeFile(jarFile, componentDoc);
                            }
                        }
                    }

                }
            }
        }
    }

    final static String META_FMT = "META-INF/services/%s";

    /*
     * Returns true if this jar file contains a par component
     */
    private static boolean determineDocumentedComponents(final FileObject jar,
                                                         final ExtensionMapping extensionMapping, final ParProperties props) throws IOException {
        try (final JarInputStream jarFile = new JarInputStream(jar.getContent().getInputStream())) {
            JarEntry jarEntry;

            // The ParProperties has configuration for the extension names and classnames
            final Map<String,String> extensions = props.getParExtensionTypes();
            if(extensions.isEmpty()){
                logger.info("No Extensions configured in properties");
                return false;
            }

            while ((jarEntry = jarFile.getNextJarEntry()) != null) {
                for (Map.Entry<String,String> extensionEntry : extensions.entrySet()) {
                    if(jarEntry.getName().equals(String.format(META_FMT, extensionEntry.getValue()))){
                       extensionMapping.addAllExtensions(extensionEntry.getKey(),determineDocumentedComponents(jarFile,jarEntry));
                    }
                }
            }
            return true;
        }
    }

    private static List<String> determineDocumentedComponents(final JarInputStream jarFile,
                                                              final JarEntry jarEntry) throws IOException {
        final List<String> componentNames = new ArrayList<>();

        if (jarEntry == null) {
            return componentNames;
        }


        final BufferedReader reader = new BufferedReader(new InputStreamReader(
                jarFile));
        String line;
        while ((line = reader.readLine()) != null) {
            final String trimmedLine = line.trim();
            if (!trimmedLine.isEmpty() && !trimmedLine.startsWith("#")) {
                final int indexOfPound = trimmedLine.indexOf("#");
                final String effectiveLine = (indexOfPound > 0) ? trimmedLine.substring(0,
                        indexOfPound) : trimmedLine;
                componentNames.add(effectiveLine);
            }
        }


        return componentNames;
    }

    /**
     * Creates the specified file, whose contents will come from the
     * <tt>InputStream</tt>.
     *
     * @param inputStream
     *            the contents of the file to create.
     * @param file
     *            the file to create.
     * @throws IOException
     *             if the file could not be created.
     */
    private static void makeFile(final InputStream inputStream, final FileObject file) throws IOException, FileSystemException{
        FileUtils.createFile(file, inputStream);
    }

    /**
     * Calculates an md5 sum of the specified file.
     *
     * @param file
     *            to calculate the md5sum of
     * @return the md5sum bytes
     * @throws IOException
     *             if cannot read file
     */
    private static byte[] calculateMd5sum(final FileObject file) throws IOException {
        try (final InputStream inputStream = file.getContent().getInputStream()) {
            final MessageDigest md5 = MessageDigest.getInstance("md5");

            final byte[] buffer = new byte[1024];
            int read = inputStream.read(buffer);

            while (read > -1) {
                md5.update(buffer, 0, read);
                read = inputStream.read(buffer);
            }

            return md5.digest();
        } catch (NoSuchAlgorithmException nsae) {
            throw new IllegalArgumentException(nsae);
        }
    }

    private ParUnpacker() {
    }
}
