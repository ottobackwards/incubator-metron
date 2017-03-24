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
package org.apache.metron.par.util;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.metron.par.ParManifestEntry;
import org.apache.metron.par.bundle.BundleCoordinate;
import org.apache.metron.par.bundle.BundleDetails;

import java.io.IOException;
import java.io.InputStream;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

public class ParBundleUtil {

    /**
     * Creates a BundleDetails from the given NAR working directory.
     *
     * @param parDirectory the directory of an exploded NAR which contains a META-INF/MANIFEST.MF
     *
     * @return the BundleDetails constructed from the information in META-INF/MANIFEST.MF
     */
    public static BundleDetails fromParDirectory(final FileObject parDirectory, ParProperties props) throws FileSystemException, IllegalStateException {
        if (parDirectory == null) {
            throw new IllegalArgumentException("NAR Directory cannot be null");
        }

        final FileObject manifestFile = parDirectory.resolveFile("META-INF/MANIFEST.MF");
        try (final InputStream fis = manifestFile.getContent().getInputStream()) {
            final Manifest manifest = new Manifest(fis);

            final Attributes attributes = manifest.getMainAttributes();
            final String prefix = props.getMetaIdPrefix();
            final BundleDetails.Builder builder = new BundleDetails.Builder();
            builder.workingDir(parDirectory);

            final String group = attributes.getValue(prefix + ParManifestEntry.PRE_GROUP.getManifestName());
            final String id = attributes.getValue(prefix + ParManifestEntry.PRE_ID.getManifestName());
            final String version = attributes.getValue(prefix + ParManifestEntry.PRE_VERSION.getManifestName());
            builder.coordinate(new BundleCoordinate(group, id, version));

            final String dependencyGroup = attributes.getValue(prefix + ParManifestEntry.PRE_DEPENDENCY_GROUP.getManifestName());
            final String dependencyId = attributes.getValue(prefix + ParManifestEntry.PRE_DEPENDENCY_ID.getManifestName());
            final String dependencyVersion = attributes.getValue(prefix + ParManifestEntry.PRE_DEPENDENCY_VERSION.getManifestName());
            if (!StringUtils.isBlank(dependencyId)) {
                builder.dependencyCoordinate(new BundleCoordinate(dependencyGroup, dependencyId, dependencyVersion));
            }

            builder.buildBranch(attributes.getValue(ParManifestEntry.BUILD_BRANCH.getManifestName()));
            builder.buildTag(attributes.getValue(ParManifestEntry.BUILD_TAG.getManifestName()));
            builder.buildRevision(attributes.getValue(ParManifestEntry.BUILD_REVISION.getManifestName()));
            builder.buildTimestamp(attributes.getValue(ParManifestEntry.BUILD_TIMESTAMP.getManifestName()));
            builder.buildJdk(attributes.getValue(ParManifestEntry.BUILD_JDK.getManifestName()));
            builder.builtBy(attributes.getValue(ParManifestEntry.BUILT_BY.getManifestName()));

            return builder.build();
        }catch(IOException ioe){
            throw new FileSystemException("failed reading manifest file " + manifestFile.getURL(),ioe);
        }
    }

}
