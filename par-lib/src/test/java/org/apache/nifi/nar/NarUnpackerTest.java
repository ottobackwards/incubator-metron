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
package org.apache.nifi.nar;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.nifi.util.NiFiProperties;
import org.apache.nifi.util.VFSClassloaderUtil;
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

import static org.apache.nifi.nar.util.TestUtil.loadSpecifiedProperties;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class NarUnpackerTest {

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
    public void testUnpackNars() throws FileSystemException, URISyntaxException {

        NiFiProperties properties = loadSpecifiedProperties("/NarUnpacker/conf/nifi.properties", Collections.EMPTY_MAP);

        assertEquals("./target/NarUnpacker/lib/",
                properties.getProperty("nifi.nar.library.directory"));
        assertEquals("./target/NarUnpacker/lib2/",
                properties.getProperty("nifi.nar.library.directory.alt"));

        // create a FileSystemManager
        FileSystemManager fileSystemManager = VFSClassloaderUtil.generateVfs();
        final ExtensionMapping extensionMapping = NarUnpacker.unpackNars(fileSystemManager,properties);

        assertEquals(2, extensionMapping.getAllExtensionNames().size());

        assertTrue(extensionMapping.getAllExtensionNames().contains(
                "org.apache.nifi.processors.dummy.one"));
        assertTrue(extensionMapping.getAllExtensionNames().contains(
                "org.apache.nifi.processors.dummy.two"));
        final FileObject extensionsWorkingDir = fileSystemManager.resolveFile(properties.getExtensionsWorkingDirectory());
        FileObject[] extensionFiles = extensionsWorkingDir.getChildren();

        Set<String> expectedNars = new HashSet<>();
        expectedNars.add("dummy-one.nar-unpacked");
        expectedNars.add("dummy-two.nar-unpacked");
        assertEquals(expectedNars.size(), extensionFiles.length);

        for (FileObject extensionFile : extensionFiles) {
            Assert.assertTrue(expectedNars.contains(extensionFile.getName().getBaseName()));
        }
    }

    @Test
    public void testUnpackNarsFromEmptyDir() throws IOException, FileSystemException , URISyntaxException {

        final File emptyDir = new File("./target/empty/dir");
        emptyDir.delete();
        emptyDir.deleteOnExit();
        assertTrue(emptyDir.mkdirs());

        final Map<String, String> others = new HashMap<>();
        others.put("nifi.nar.library.directory.alt", emptyDir.toString());
        NiFiProperties properties = loadSpecifiedProperties("/NarUnpacker/conf/nifi.properties", others);
        // create a FileSystemManager
        FileSystemManager fileSystemManager = VFSClassloaderUtil.generateVfs();
        final ExtensionMapping extensionMapping = NarUnpacker.unpackNars(fileSystemManager, properties);

        assertEquals(1, extensionMapping.getAllExtensionNames().size());
        assertTrue(extensionMapping.getAllExtensionNames().contains(
                "org.apache.nifi.processors.dummy.one"));

        final FileObject extensionsWorkingDir = fileSystemManager.resolveFile(properties.getExtensionsWorkingDirectory());
        FileObject[] extensionFiles = extensionsWorkingDir.getChildren();

        assertEquals(1, extensionFiles.length);
        assertEquals("dummy-one.nar-unpacked", extensionFiles[0].getName().getBaseName());
    }

    @Test
    public void testUnpackNarsFromNonExistantDir() throws FileSystemException, URISyntaxException {

        final File nonExistantDir = new File("./target/this/dir/should/not/exist/");
        nonExistantDir.delete();
        nonExistantDir.deleteOnExit();

        final Map<String, String> others = new HashMap<>();
        others.put("nifi.nar.library.directory.alt", nonExistantDir.toString());
        NiFiProperties properties = loadSpecifiedProperties("/NarUnpacker/conf/nifi.properties", others);
        // create a FileSystemManager
        FileSystemManager fileSystemManager = VFSClassloaderUtil.generateVfs();
        final ExtensionMapping extensionMapping = NarUnpacker.unpackNars(fileSystemManager, properties);

        assertTrue(extensionMapping.getAllExtensionNames().contains(
                "org.apache.nifi.processors.dummy.one"));

        assertEquals(1, extensionMapping.getAllExtensionNames().size());

        final FileObject extensionsWorkingDir = fileSystemManager.resolveFile(properties.getExtensionsWorkingDirectory());
        FileObject[] extensionFiles = extensionsWorkingDir.getChildren();

        assertEquals(1, extensionFiles.length);
        assertEquals("dummy-one.nar-unpacked", extensionFiles[0].getName().getBaseName());
    }

    @Test
    public void testUnpackNarsFromNonDir() throws IOException , FileSystemException, URISyntaxException {

        final File nonDir = new File("./target/file.txt");
        nonDir.createNewFile();
        nonDir.deleteOnExit();

        final Map<String, String> others = new HashMap<>();
        others.put("nifi.nar.library.directory.alt", nonDir.toString());
        NiFiProperties properties = loadSpecifiedProperties("/NarUnpacker/conf/nifi.properties", others);
        // create a FileSystemManager
        FileSystemManager fileSystemManager = VFSClassloaderUtil.generateVfs();
        final ExtensionMapping extensionMapping = NarUnpacker.unpackNars(fileSystemManager, properties);

        assertNull(extensionMapping);
    }
}
