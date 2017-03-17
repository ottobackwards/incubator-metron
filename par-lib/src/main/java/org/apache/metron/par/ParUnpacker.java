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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    private static String HASH_FILENAME = "nar-md5sum";
    private static final FileSelector NAR_FILTER = new FileSelector() {
        @Override
        public boolean includeFile(FileSelectInfo fileSelectInfo) throws Exception {
            final String nameToTest = fileSelectInfo.getFile().getName().getExtension();
            return nameToTest.equals("nar") && fileSelectInfo.getFile().isFile();
        }

        @Override
        public boolean traverseDescendents(FileSelectInfo fileSelectInfo) throws Exception {
            return true;
        }
    };

    public static ExtensionMapping unpackNars(final FileSystemManager fileSystemManager, ParProperties props){
        try{
            final List<URI> narLibraryDirs = props.getNarLibraryDirectories();
            final URI frameworkWorkingDir = props.getFrameworkWorkingDirectory();
            final URI extensionsWorkingDir = props.getExtensionsWorkingDirectory();
            final URI docsWorkingDir = props.getComponentDocumentationWorkingDirectory();


            FileObject unpackedFramework = null;
            final FileObject frameworkWorkingDirFO = fileSystemManager.resolveFile(frameworkWorkingDir);
            final FileObject extensionsWorkingDirFO = fileSystemManager.resolveFile(extensionsWorkingDir);
            final FileObject docsWorkingDirFO = fileSystemManager.resolveFile(docsWorkingDir);
            final Set<FileObject> unpackedExtensions = new HashSet<>();
            final List<FileObject> narFiles = new ArrayList<>();

            // make sure the nar directories are there and accessible
            FileUtils.ensureDirectoryExistAndCanReadAndWrite(frameworkWorkingDirFO);
            FileUtils.ensureDirectoryExistAndCanReadAndWrite(extensionsWorkingDirFO);
            FileUtils.ensureDirectoryExistAndCanReadAndWrite(docsWorkingDirFO);

            for (URI narLibraryDir : narLibraryDirs) {

                FileObject narDir = fileSystemManager.resolveFile(narLibraryDir);

                // Test if the source NARs can be read
                FileUtils.ensureDirectoryExistAndCanRead(narDir);

                FileObject[] dirFiles = narDir.findFiles(NAR_FILTER);
                if (dirFiles != null) {
                    List<FileObject> fileList = Arrays.asList(dirFiles);
                    narFiles.addAll(fileList);
                }
            }

            if (!narFiles.isEmpty()) {
                final long startTime = System.nanoTime();
                logger.info("Expanding " + narFiles.size() + " NAR files with all processors...");
                for (FileObject narFile : narFiles) {
                    logger.debug("Expanding NAR file: " + narFile.getURL().toString());

                    // get the manifest for this nar
                    Manifest manifest = null;
                    try(JarInputStream jis = new JarInputStream(narFile.getContent().getInputStream())) {
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
                        final String narId = manifest.getMainAttributes().getValue("Nar-Id");
                        // determine if this is the framework
                        if (ParClassLoaders.FRAMEWORK_NAR_ID.equals(narId)) {
                            if (unpackedFramework != null) {
                                throw new IllegalStateException(
                                        "Multiple framework NARs discovered. Only one framework is permitted.");
                            }

                            unpackedFramework = unpackNar(narFile, frameworkWorkingDirFO);
                        } else {
                            unpackedExtensions.add(unpackNar(narFile, extensionsWorkingDirFO));
                        }
                }

                // ensure we've found the framework nar
                if (unpackedFramework == null) {
                    throw new IllegalStateException("No framework NAR found.");
                } else if (!unpackedFramework.isReadable()) {
                    throw new IllegalStateException("Framework NAR cannot be read.");
                }

                // Determine if any nars no longer exist and delete their
                // working directories. This happens
                // if a new version of a nar is dropped into the lib dir.
                // ensure no old framework are present
                final FileObject[] frameworkWorkingDirContents = frameworkWorkingDirFO.getChildren();
                if (frameworkWorkingDirContents != null) {
                    for (final FileObject unpackedNar : frameworkWorkingDirContents) {
                        if (!unpackedFramework.equals(unpackedNar)) {
                            FileUtils.deleteFile(unpackedNar, true);
                        }
                    }
                }

                // ensure no old extensions are present
                final FileObject[] extensionsWorkingDirContents = extensionsWorkingDirFO.getChildren();
                if (extensionsWorkingDirContents != null) {
                    for (final FileObject unpackedNar : extensionsWorkingDirContents) {
                        if (!unpackedExtensions.contains(unpackedNar)) {
                            FileUtils.deleteFile(unpackedNar, true);
                        }
                    }
                }
                final long endTime = System.nanoTime();
                logger.info("NAR loading process took " + (endTime - startTime) + " nanoseconds.");
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
            mapExtensions(extensionsWorkingDirFO, docsWorkingDirFO, extensionMapping);
            return extensionMapping;
        } catch (IOException | URISyntaxException | NotInitializedException e) {
            logger.warn("Unable to load NAR library bundles due to " + e
                    + " Will proceed without loading any further Nar bundles");
            if (logger.isDebugEnabled()) {
                logger.warn("", e);
            }
        }
        return null;
    }

    private static void mapExtensions(final FileObject workingDirectory, final FileObject docsDirectory,
            final ExtensionMapping mapping) throws IOException {
        final FileObject[] directoryContents = workingDirectory.getChildren();
        if (directoryContents != null) {
            for (final FileObject file : directoryContents) {
                if (file.isFolder()) {
                    mapExtensions(file, docsDirectory, mapping);
                } else if (file.getName().getExtension().equals("jar")) {
                    unpackDocumentation(file, docsDirectory, mapping);
                }
            }
        }
    }

    /**
     * Unpacks the specified nar into the specified base working directory.
     *
     * @param nar
     *            the nar to unpack
     * @param baseWorkingDirectory
     *            the directory to unpack to
     * @return the directory to the unpacked NAR
     * @throws IOException
     *             if unable to explode nar
     */
    private static FileObject unpackNar(final FileObject nar, final FileObject baseWorkingDirectory)
            throws IOException, NotInitializedException {

        final FileObject narWorkingDirectory = baseWorkingDirectory.resolveFile(nar.getName().getBaseName() + "-unpacked");

        // if the working directory doesn't exist, unpack the nar
        if (!narWorkingDirectory.exists()) {
            unpack(nar, narWorkingDirectory, calculateMd5sum(nar));
        } else {
            // the working directory does exist. Run MD5 sum against the nar
            // file and check if the nar has changed since it was deployed.
            final byte[] narMd5 = calculateMd5sum(nar);
            final FileObject workingHashFile = narWorkingDirectory.getChild(HASH_FILENAME);
            if (!workingHashFile.exists()) {
                FileUtils.deleteFile(narWorkingDirectory, true);
                unpack(nar, narWorkingDirectory, narMd5);
            } else {
                final byte[] hashFileContents = IOUtils.toByteArray(workingHashFile.getContent().getInputStream());
                if (!Arrays.equals(hashFileContents, narMd5)) {
                    logger.info("Contents of nar {} have changed. Reloading.",
                            new Object[] { nar.getURL() });
                    FileUtils.deleteFile(narWorkingDirectory, true);
                    unpack(nar, narWorkingDirectory, narMd5);
                }
            }
        }

        return narWorkingDirectory;
    }

    /**
     * Unpacks the NAR to the specified directory. Creates a checksum file that
     * used to determine if future expansion is necessary.
     *
     * @param workingDirectory
     *            the root directory to which the NAR should be unpacked.
     * @throws IOException
     *             if the NAR could not be unpacked.
     */
    private static void unpack(final FileObject nar, final FileObject workingDirectory, final byte[] hash)
            throws IOException {

        try(JarInputStream jarFile = new JarInputStream(nar.getContent().getInputStream())){
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
            final ExtensionMapping extensionMapping) throws IOException {
        // determine the components that may have documentation
        if (!determineDocumentedNiFiComponents(jar, extensionMapping)) {
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

    /*
     * Returns true if this jar file contains a NiFi component
     */
    private static boolean determineDocumentedNiFiComponents(final FileObject jar,
            final ExtensionMapping extensionMapping) throws IOException {
        try (final JarInputStream jarFile = new JarInputStream(jar.getContent().getInputStream())) {
            JarEntry jarEntry;
            boolean hasProc = false;
            boolean hasRpt = false;
            boolean hasCont = false;
            while ((jarEntry = jarFile.getNextJarEntry()) != null) {
                if (jarEntry.getName().equals("META-INF/services/org.apache.nifi.processor.Processor")) {
                    hasProc = true;
                    extensionMapping.addAllProcessors(determineDocumentedNiFiComponents(jarFile,
                            jarEntry));
                } else if (jarEntry.getName().equals("META-INF/services/org.apache.nifi.reporting.ReportingTask")) {
                    hasRpt = true;
                    extensionMapping.addAllReportingTasks(determineDocumentedNiFiComponents(jarFile,
                            jarEntry));
                } else if (jarEntry.getName().equals("META-INF/services/org.apache.nifi.controller.ControllerService")) {
                    hasCont = true;
                    extensionMapping.addAllControllerServices(determineDocumentedNiFiComponents(jarFile,
                            jarEntry));
                }
            }
            if (!hasCont && !hasProc && !hasRpt) {
                return false;
            }
            return true;
        }
    }

    private static List<String> determineDocumentedNiFiComponents(final JarInputStream jarFile,
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
