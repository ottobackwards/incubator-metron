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
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.vfs2.*;
import org.apache.commons.vfs2.impl.VFSClassLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * A <tt>ClassLoader</tt> for loading PARs (plugin archives). PARs are designed to
 * allow isolating bundles of code (comprising one-or-more
 * plugin classes and their
 * dependencies) from other such bundles; this allows for dependencies and
 * processors that require conflicting, incompatible versions of the same
 * dependency to run in a single instance of a given process.</p>
 *
 * <p>
 * <tt>ParClassLoader</tt> follows the delegation model described in
 * {@link ClassLoader#findClass(java.lang.String) ClassLoader.findClass(...)};
 * classes are first loaded from the parent <tt>ClassLoader</tt>, and only if
 * they cannot be found there does the <tt>ParClassLoader</tt> provide a
 * definition. Specifically, this means that resources are loaded from the application's
 * <tt>conf</tt>
 * and <tt>lib</tt> directories first, and if they cannot be found there, are
 * loaded from the PAR.</p>
 *
 * <p>
 * The packaging of a PAR is such that it is a ZIP file with the following
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
 * also includes two additional par properties: {@code Par-Id} and
 * {@code Par-Dependency-Id}.
 * </p>
 *
 * <p>
 * The {@code Par-Id} provides a unique identifier for this PAR.
 * </p>
 *
 * <p>
 * The {@code Par-Dependency-Id} is optional. If provided, it indicates that
 * this PAR should inherit all of the dependencies of the PAR with the provided
 * ID. Often times, the PAR that is depended upon is referred to as the Parent.
 * This is because its ClassLoader will be the parent ClassLoader of the
 * dependent PAR.
 * </p>
 *
 * <p>
 * If a PAR is built using NiFi's Maven PAR Plugin, the {@code Par-Id} property
 * will be set to the artifactId of the PAR. The {@code Par-Dependency-Id} will
 * be set to the artifactId of the PAR that is depended upon. For example, if
 * PAR A is defined as such:
 *
 * <pre>
 * ...
 * &lt;artifactId&gt;par-a&lt;/artifactId&gt;
 * &lt;packaging&gt;par&lt;/packaging&gt;
 * ...
 * &lt;dependencies&gt;
 *   &lt;dependency&gt;
 *     &lt;groupId&gt;group&lt;/groupId&gt;
 *     &lt;artifactId&gt;par-z&lt;/artifactId&gt;
 *     <b>&lt;type&gt;par&lt;/type&gt;</b>
 *   &lt;/dependency&gt;
 * &lt;/dependencies&gt;
 * </pre>
 * </p>
 *
 *
 * <p>
 * Then the MANIFEST.MF file that is created for PAR A will have the following
 * properties set:
 * <ul>
 * <li>{@code Par-Id: par-a}</li>
 * <li>{@code Par-Dependency-Id: par-z}</li>
 * </ul>
 * </p>
 *
 * <p>
 * Note, above, that the {@code type} of the dependency is set to {@code par}.
 * </p>
 *
 * <p>
 * If the PAR has more than one dependency of {@code type} {@code par}, then the
 * Maven PAR plugin will fail to build the PAR.
 * </p>
 */
public class ParClassLoader extends VFSClassLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParClassLoader.class);

    public static class Builder {

        private FileSystemManager fileSystemManager;
        private FileObject parWorkingDirectory;
        private FileObject[] existingPaths;
        private ClassLoader parentClassLoader;

        public Builder withFileSystemManager(FileSystemManager fileSystemManager) {
            this.fileSystemManager = fileSystemManager;
            return this;
        }

        public Builder withParWorkingDirectory(FileObject parWorkingDirectory) {
            this.parWorkingDirectory = parWorkingDirectory;
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
                LOGGER.warn(parWorkingDirectory + " does not contain META-INF/bundled-dependencies!");
            }
            paths.add(dependencies);
            if (dependencies.isFolder()) {
                for (FileObject libJar : dependencies.findFiles(JAR_FILTER)) {
                    paths.add(libJar);
                }
            }
            return paths.toArray(new FileObject[0]);
        }
        public ParClassLoader build() throws FileSystemException{
            FileObject[] paths = updateClasspath(parWorkingDirectory,existingPaths);
            return new ParClassLoader(fileSystemManager, parWorkingDirectory, paths, parentClassLoader);
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
     * The PAR for which this <tt>ClassLoader</tt> is responsible.
     */
    private final FileObject parWorkingDirectory;

    /**
     * Construct a par class loader with the specific parent.
     *
     * @param parWorkingDirectory directory to explode par contents to
     * @param parentClassLoader parent class loader of this par
     * @throws IllegalArgumentException if the PAR is missing the Java Services
     * API file for implementations.
     * @throws ClassNotFoundException if any of the <tt>FlowFileProcessor</tt>
     * implementations defined by the Java Services API cannot be loaded.
     * @throws IOException if an error occurs while loading the PAR.
     */
    private ParClassLoader(final FileSystemManager fileSystemManager, final FileObject parWorkingDirectory, final FileObject[] classPaths, final ClassLoader parentClassLoader) throws FileSystemException {
        super(classPaths,fileSystemManager,parentClassLoader);
        this.parWorkingDirectory = parWorkingDirectory;
    }

    public FileObject getWorkingDirectory() {
        return parWorkingDirectory;
    }



    @Override
    protected String findLibrary(final String libname) {
        try {
            FileObject dependencies = parWorkingDirectory.resolveFile("META-INF/bundled-dependencies");
            if (!dependencies.isFolder()) {
                LOGGER.warn(parWorkingDirectory + " does not contain META-INF/bundled-dependencies!");
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

        // not found in the par. try system native dir
        return null;
    }

    @Override
    public String toString() {
        return ParClassLoader.class.getName() + "[" + parWorkingDirectory.getName().toString() + "]";
    }
}
