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

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.metron.bundles.bundle.BundleCoordinate;
import org.apache.metron.bundles.bundle.BundleDetails;
import org.apache.metron.bundles.util.BundleProperties;
import org.apache.metron.bundles.util.BundleUtil;
import org.apache.metron.bundles.util.VFSClassloaderUtil;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class BundleUtilTest {
    Map<String, String> additionalProperties = new HashMap<>();
    @Test
    public void testManifestWithVersioningAndBuildInfo() throws IOException , URISyntaxException{

        BundleProperties properties = BundleProperties.createBasicBundleProperties("src/test/resources/bundle.properties", additionalProperties);
        // create a FileSystemManager
        FileSystemManager fileSystemManager = VFSClassloaderUtil.generateVfs(properties.getArchiveExtension());

        final FileObject parDir = fileSystemManager.resolveFile(BundleProperties.getURI("src/test/resources/utils-bundles/bundle-with-versioning"));
        final BundleDetails narDetails = BundleUtil.fromBundleDirectory(parDir, properties);
        assertEquals(parDir.getURL(), narDetails.getWorkingDirectory().getURL());

        assertEquals("org.apache.nifi", narDetails.getCoordinate().getGroup());
        assertEquals("nifi-hadoop-nar", narDetails.getCoordinate().getId());
        assertEquals("1.2.0", narDetails.getCoordinate().getVersion());

        assertEquals("org.apache.nifi.hadoop", narDetails.getDependencyCoordinate().getGroup());
        assertEquals("nifi-hadoop-libraries-nar", narDetails.getDependencyCoordinate().getId());
        assertEquals("1.2.1", narDetails.getDependencyCoordinate().getVersion());

        assertEquals("NIFI-3380", narDetails.getBuildBranch());
        assertEquals("1.8.0_74", narDetails.getBuildJdk());
        assertEquals("a032175", narDetails.getBuildRevision());
        assertEquals("HEAD", narDetails.getBuildTag());
        assertEquals("2017-01-23T10:36:27Z", narDetails.getBuildTimestamp());
        assertEquals("bbende", narDetails.getBuiltBy());
    }

    @Test
    public void testManifestWithoutVersioningAndBuildInfo() throws IOException, URISyntaxException {
        BundleProperties properties = BundleProperties.createBasicBundleProperties("src/test/resources/bundle.properties",  additionalProperties);
        // create a FileSystemManager
        FileSystemManager fileSystemManager = VFSClassloaderUtil.generateVfs(properties.getArchiveExtension());

        final FileObject parDir = fileSystemManager.resolveFile(BundleProperties.getURI("src/test/resources/utils-bundles/bundle-without-versioning"));
        final BundleDetails parDetails = BundleUtil.fromBundleDirectory(parDir, properties);
        assertEquals(parDir.getURL(), parDetails.getWorkingDirectory().getURL());

        assertEquals(BundleCoordinate.DEFAULT_GROUP, parDetails.getCoordinate().getGroup());
        assertEquals("nifi-hadoop-nar", parDetails.getCoordinate().getId());
        assertEquals(BundleCoordinate.DEFAULT_VERSION, parDetails.getCoordinate().getVersion());

        assertEquals(BundleCoordinate.DEFAULT_GROUP, parDetails.getDependencyCoordinate().getGroup());
        assertEquals("nifi-hadoop-libraries-nar", parDetails.getDependencyCoordinate().getId());
        assertEquals(BundleCoordinate.DEFAULT_VERSION, parDetails.getDependencyCoordinate().getVersion());

        assertNull(parDetails.getBuildBranch());
        assertEquals("1.8.0_74", parDetails.getBuildJdk());
        assertNull(parDetails.getBuildRevision());
        assertNull(parDetails.getBuildTag());
        assertNull(parDetails.getBuildTimestamp());
        assertEquals("bbende", parDetails.getBuiltBy());
    }

    @Test
    public void testManifestWithoutNarDependency() throws IOException, URISyntaxException {
        BundleProperties properties = BundleProperties.createBasicBundleProperties("src/test/resources/bundle.properties",  additionalProperties);
        // create a FileSystemManager
        FileSystemManager fileSystemManager = VFSClassloaderUtil.generateVfs(properties.getArchiveExtension());

        final FileObject parDir = fileSystemManager.resolveFile(BundleProperties.getURI("src/test/resources/utils-bundles/bundle-without-dependency"));
        final BundleDetails parDetails = BundleUtil.fromBundleDirectory(parDir, properties);
        assertEquals(parDir.getURL(), parDetails.getWorkingDirectory().getURL());

        assertEquals("org.apache.nifi", parDetails.getCoordinate().getGroup());
        assertEquals("nifi-hadoop-nar", parDetails.getCoordinate().getId());
        assertEquals("1.2.0", parDetails.getCoordinate().getVersion());

        assertNull(parDetails.getDependencyCoordinate());

        assertEquals("NIFI-3380", parDetails.getBuildBranch());
        assertEquals("1.8.0_74", parDetails.getBuildJdk());
        assertEquals("a032175", parDetails.getBuildRevision());
        assertEquals("HEAD", parDetails.getBuildTag());
        assertEquals("2017-01-23T10:36:27Z", parDetails.getBuildTimestamp());
        assertEquals("bbende", parDetails.getBuiltBy());
    }

    @Test(expected = IOException.class)
    public void testFromManifestWhenNarDirectoryDoesNotExist() throws IOException, URISyntaxException {
        BundleProperties properties = BundleProperties.createBasicBundleProperties("src/test/resources/bundle.properties", additionalProperties);
        // create a FileSystemManager
        FileSystemManager fileSystemManager = VFSClassloaderUtil.generateVfs(properties.getArchiveExtension());

        final FileObject manifest = fileSystemManager.resolveFile(BundleProperties.getURI("src/test/resources/utils-bundles/bundle-does-not-exist"));
        BundleUtil.fromBundleDirectory(manifest, properties );
    }

}
