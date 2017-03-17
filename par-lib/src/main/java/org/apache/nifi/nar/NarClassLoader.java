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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.vfs2.*;
import org.apache.commons.vfs2.impl.VFSClassLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * A <tt>ClassLoader</tt> for loading NARs (NiFi archives). NARs are designed to
 * allow isolating bundles of code (comprising one-or-more NiFi
 * <tt>FlowFileProcessor</tt>s, <tt>FlowFileComparator</tt>s and their
 * dependencies) from other such bundles; this allows for dependencies and
 * processors that require conflicting, incompatible versions of the same
 * dependency to run in a single instance of NiFi.</p>
 *
 * <p>
 * <tt>NarClassLoader</tt> follows the delegation model described in
 * {@link ClassLoader#findClass(java.lang.String) ClassLoader.findClass(...)};
 * classes are first loaded from the parent <tt>ClassLoader</tt>, and only if
 * they cannot be found there does the <tt>NarClassLoader</tt> provide a
 * definition. Specifically, this means that resources are loaded from NiFi's
 * <tt>conf</tt>
 * and <tt>lib</tt> directories first, and if they cannot be found there, are
 * loaded from the NAR.</p>
 *
 * <p>
 * The packaging of a NAR is such that it is a ZIP file with the following
 * directory structure:
 *
 * <pre>
 *   +META-INF/
 *   +-- bundled-dependencies/
 *   +-- &lt;JAR files&gt;
 *   +-- MANIFEST.MF
 * </pre>
 * </p>
 *
 * <p>
 * The MANIFEST.MF file contains the same information as a typical JAR file but
 * also includes two additional NiFi properties: {@code Nar-Id} and
 * {@code Nar-Dependency-Id}.
 * </p>
 *
 * <p>
 * The {@code Nar-Id} provides a unique identifier for this NAR.
 * </p>
 *
 * <p>
 * The {@code Nar-Dependency-Id} is optional. If provided, it indicates that
 * this NAR should inherit all of the dependencies of the NAR with the provided
 * ID. Often times, the NAR that is depended upon is referred to as the Parent.
 * This is because its ClassLoader will be the parent ClassLoader of the
 * dependent NAR.
 * </p>
 *
 * <p>
 * If a NAR is built using NiFi's Maven NAR Plugin, the {@code Nar-Id} property
 * will be set to the artifactId of the NAR. The {@code Nar-Dependency-Id} will
 * be set to the artifactId of the NAR that is depended upon. For example, if
 * NAR A is defined as such:
 *
 * <pre>
 * ...
 * &lt;artifactId&gt;nar-a&lt;/artifactId&gt;
 * &lt;packaging&gt;nar&lt;/packaging&gt;
 * ...
 * &lt;dependencies&gt;
 *   &lt;dependency&gt;
 *     &lt;groupId&gt;group&lt;/groupId&gt;
 *     &lt;artifactId&gt;nar-z&lt;/artifactId&gt;
 *     <b>&lt;type&gt;nar&lt;/type&gt;</b>
 *   &lt;/dependency&gt;
 * &lt;/dependencies&gt;
 * </pre>
 * </p>
 *
 *
 * <p>
 * Then the MANIFEST.MF file that is created for NAR A will have the following
 * properties set:
 * <ul>
 * <li>{@code Nar-Id: nar-a}</li>
 * <li>{@code Nar-Dependency-Id: nar-z}</li>
 * </ul>
 * </p>
 *
 * <p>
 * Note, above, that the {@code type} of the dependency is set to {@code nar}.
 * </p>
 *
 * <p>
 * If the NAR has more than one dependency of {@code type} {@code nar}, then the
 * Maven NAR plugin will fail to build the NAR.
 * </p>
 */
public class NarClassLoader extends VFSClassLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(NarClassLoader.class);

    public static class Builder {

        private FileSystemManager fileSystemManager;
        private FileObject narWorkingDirectory;
        private FileObject[] existingPaths;
        private ClassLoader parentClassLoader;

        public Builder withFileSystemManager(FileSystemManager fileSystemManager) {
            this.fileSystemManager = fileSystemManager;
            return this;
        }

        public Builder withNarWorkingDirectory(FileObject narWorkingDirectory) {
            this.narWorkingDirectory = narWorkingDirectory;
            return this;
        }

        public Builder withParentClassloader(ClassLoader parentClassloader){
            this.parentClassLoader = parentClassloader;
            return this;
        }

        public Builder withClassPaths(FileObject[] paths){
            this.existingPaths = paths;
            return this;
        }

        private FileObject[] updateClasspath(FileObject root, FileObject[] otherPaths) throws FileSystemException{
            final List<FileObject> paths = new ArrayList<>();
            // for compiled classes, META-INF/, etc.
            paths.add(root);

            if(otherPaths != null && otherPaths.length > 0){
                for ( FileObject path : otherPaths) {
                    paths.add(path);
                }
            }

            FileObject dependencies = root.resolveFile("META-INF/bundled-dependencies");
            if (!dependencies.isFolder()) {
                LOGGER.warn(narWorkingDirectory + " does not contain META-INF/bundled-dependencies!");
            }
            paths.add(dependencies);
            if (dependencies.isFolder()) {
                for (FileObject libJar : dependencies.findFiles(JAR_FILTER)) {
                    paths.add(libJar);
                }
            }
            return paths.toArray(new FileObject[0]);
        }
        public NarClassLoader build() throws FileSystemException{
            FileObject[] paths = updateClasspath(narWorkingDirectory,existingPaths);
            return new NarClassLoader(fileSystemManager, narWorkingDirectory, paths, parentClassLoader);
        }
    }

    private static final FileSelector JAR_FILTER = new FileSelector() {
        @Override
        public boolean includeFile(FileSelectInfo fileSelectInfo) throws Exception {
            final String nameToTest = fileSelectInfo.getFile().getName().getExtension();
            return nameToTest.equals("jar") && fileSelectInfo.getFile().isFile();
        }

        @Override
        public boolean traverseDescendents(FileSelectInfo fileSelectInfo) throws Exception {
            return true;
        }
    };

    /**
     * The NAR for which this <tt>ClassLoader</tt> is responsible.
     */
    private final FileObject narWorkingDirectory;

    /**
     * Construct a nar class loader with the specific parent.
     *
     * @param narWorkingDirectory directory to explode nar contents to
     * @param parentClassLoader parent class loader of this nar
     * @throws IllegalArgumentException if the NAR is missing the Java Services
     * API file for <tt>FlowFileProcessor</tt> implementations.
     * @throws ClassNotFoundException if any of the <tt>FlowFileProcessor</tt>
     * implementations defined by the Java Services API cannot be loaded.
     * @throws IOException if an error occurs while loading the NAR.
     */
    private NarClassLoader(final FileSystemManager fileSystemManager, final FileObject narWorkingDirectory, final FileObject[] classPaths, final ClassLoader parentClassLoader) throws FileSystemException {
        super(classPaths,fileSystemManager,parentClassLoader);
        this.narWorkingDirectory = narWorkingDirectory;
    }

    public FileObject getWorkingDirectory() {
        return narWorkingDirectory;
    }



    @Override
    protected String findLibrary(final String libname) {
        try {
            FileObject dependencies = narWorkingDirectory.resolveFile("META-INF/bundled-dependencies");
            if (!dependencies.isFolder()) {
                LOGGER.warn(narWorkingDirectory + " does not contain META-INF/bundled-dependencies!");
            }

            final FileObject nativeDir = dependencies.resolveFile("native");
            final FileObject libsoFile = nativeDir.resolveFile( "lib" + libname + ".so");
            final FileObject dllFile = nativeDir.resolveFile(libname + ".dll");
            final FileObject soFile = nativeDir.resolveFile(libname + ".so");
            if (libsoFile.exists()) {
                return libsoFile.getURL().toString();
            } else if (dllFile.exists()) {
                return dllFile.getURL().toString();
            } else if (soFile.exists()) {
                return soFile.getURL().toString();
            }
        }catch(FileSystemException fse){
            LOGGER.error("Failed to get dependencies",fse);
        }

        // not found in the nar. try system native dir
        return null;
    }

    @Override
    public String toString() {
        return NarClassLoader.class.getName() + "[" + narWorkingDirectory.getName().toString() + "]";
    }
}
