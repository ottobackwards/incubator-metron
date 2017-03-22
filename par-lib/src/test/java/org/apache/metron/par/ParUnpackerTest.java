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

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.metron.par.util.ParProperties;
import org.apache.metron.par.util.VFSClassloaderUtil;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.Set;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.apache.metron.par.util.TestUtil.loadSpecifiedProperties;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ParUnpackerTest {

    @BeforeClass
    public static void copyResources() throws IOException {

        final Path sourcePath = Paths.get("./src/test/resources");
        final Path targetPath = Paths.get("./target");

        Files.walkFileTree(sourcePath, new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                    throws IOException {

                Path relativeSource = sourcePath.relativize(dir);
                Path target = targetPath.resolve(relativeSource);

                Files.createDirectories(target);

                return FileVisitResult.CONTINUE;

            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                    throws IOException {

                Path relativeSource = sourcePath.relativize(file);
                Path target = targetPath.resolve(relativeSource);

                Files.copy(file, target, REPLACE_EXISTING);

                return FileVisitResult.CONTINUE;
            }
        });
    }

    @Test
    public void testUnpackPars() throws FileSystemException, URISyntaxException {

        ParProperties properties = loadSpecifiedProperties("/ParUnpacker/conf/par.properties", Collections.EMPTY_MAP);

        assertEquals("./target/ParUnpacker/lib/",
                properties.getProperty("par.library.directory"));
        assertEquals("./target/ParUnpacker/lib2/",
                properties.getProperty("par.library.directory.alt"));

        // create a FileSystemManager
        FileSystemManager fileSystemManager = VFSClassloaderUtil.generateVfs(properties.getArchiveExtension());
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

    @Test
    public void testUnpackParsFromEmptyDir() throws IOException, FileSystemException , URISyntaxException {

        final File emptyDir = new File("./target/empty/dir");
        emptyDir.delete();
        emptyDir.deleteOnExit();
        assertTrue(emptyDir.mkdirs());

        final Map<String, String> others = new HashMap<>();
        others.put("par.library.directory.alt", emptyDir.toString());
        ParProperties properties = loadSpecifiedProperties("/ParUnpacker/conf/par.properties", others);
        // create a FileSystemManager
        FileSystemManager fileSystemManager = VFSClassloaderUtil.generateVfs(properties.getArchiveExtension());
        final ExtensionMapping extensionMapping = ParUnpacker.unpackPars(fileSystemManager, properties);

        assertEquals(1, extensionMapping.getAllExtensionNames().size());
        assertTrue(extensionMapping.getAllExtensionNames().contains(
                "org.apache.nifi.processors.dummy.one"));

        final FileObject extensionsWorkingDir = fileSystemManager.resolveFile(properties.getExtensionsWorkingDirectory());
        FileObject[] extensionFiles = extensionsWorkingDir.getChildren();

        assertEquals(1, extensionFiles.length);
        assertEquals("dummy-one.foo-unpacked", extensionFiles[0].getName().getBaseName());
    }

    @Test
    public void testUnpackParsFromNonExistantDir() throws FileSystemException, URISyntaxException {

        final File nonExistantDir = new File("./target/this/dir/should/not/exist/");
        nonExistantDir.delete();
        nonExistantDir.deleteOnExit();

        final Map<String, String> others = new HashMap<>();
        others.put("par.library.directory.alt", nonExistantDir.toString());
        ParProperties properties = loadSpecifiedProperties("/ParUnpacker/conf/par.properties", others);
        // create a FileSystemManager
        FileSystemManager fileSystemManager = VFSClassloaderUtil.generateVfs(properties.getArchiveExtension());
        final ExtensionMapping extensionMapping = ParUnpacker.unpackPars(fileSystemManager, properties);

        assertTrue(extensionMapping.getAllExtensionNames().contains(
                "org.apache.nifi.processors.dummy.one"));

        assertEquals(1, extensionMapping.getAllExtensionNames().size());

        final FileObject extensionsWorkingDir = fileSystemManager.resolveFile(properties.getExtensionsWorkingDirectory());
        FileObject[] extensionFiles = extensionsWorkingDir.getChildren();

        assertEquals(1, extensionFiles.length);
        assertEquals("dummy-one.foo-unpacked", extensionFiles[0].getName().getBaseName());
    }

    @Test
    public void testUnpackParsFromNonDir() throws IOException , FileSystemException, URISyntaxException {

        final File nonDir = new File("./target/file.txt");
        nonDir.createNewFile();
        nonDir.deleteOnExit();

        final Map<String, String> others = new HashMap<>();
        others.put("par.library.directory.alt", nonDir.toString());
        ParProperties properties = loadSpecifiedProperties("/ParUnpacker/conf/par.properties", others);
        // create a FileSystemManager
        FileSystemManager fileSystemManager = VFSClassloaderUtil.generateVfs(properties.getArchiveExtension());
        final ExtensionMapping extensionMapping = ParUnpacker.unpackPars(fileSystemManager, properties);

        assertNull(extensionMapping);
    }
}
