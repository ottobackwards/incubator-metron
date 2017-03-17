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
package org.apache.nifi.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.*;

/**
 * The NiFiProperties class holds all properties which are needed for various
 * values to be available at runtime. It is strongly tied to the startup
 * properties needed and is often refer to as the 'nifi.properties' file. The
 * properties contains keys and values. Great care should be taken in leveraging
 * this class or passing it along. Its use should be refactored and minimized
 * over time.
 */
public abstract class NiFiProperties {

    // core properties
    public static final String PROPERTIES_FILE_PATH = "nifi.properties.file.path";
    public static final String NAR_LIBRARY_DIRECTORY = "nifi.nar.library.directory";
    public static final String NAR_LIBRARY_DIRECTORY_PREFIX = "nifi.nar.library.directory.";
    public static final String NAR_WORKING_DIRECTORY = "nifi.nar.working.directory";
    public static final String COMPONENT_DOCS_DIRECTORY = "nifi.documentation.working.directory";
    // zookeeper properties
    public static final String ZOOKEEPER_CONNECT_STRING = "nifi.zookeeper.connect.string";
    public static final String ZOOKEEPER_CONNECT_TIMEOUT = "nifi.zookeeper.connect.timeout";
    public static final String ZOOKEEPER_SESSION_TIMEOUT = "nifi.zookeeper.session.timeout";
    public static final String ZOOKEEPER_ROOT_NODE = "nifi.zookeeper.root.node";

    // defaults
    public static final String DEFAULT_NAR_WORKING_DIR = "./work/nar";
    public static final String DEFAULT_NAR_LIBRARY_DIR = "./lib";
    public static final String DEFAULT_COMPONENT_DOCS_DIRECTORY = "./work/docs/components";

    /**
     * Retrieves the property value for the given property key.
     *
     * @param key the key of property value to lookup
     * @return value of property at given key or null if not found
     */
    public abstract String getProperty(String key);
    public abstract void setProperty(String key, String value);

    /**
     * Retrieves all known property keys.
     *
     * @return all known property keys
     */
    public abstract Set<String> getPropertyKeys();

    public String getProperty(final String key, final String defaultValue) {
        final String value = getProperty(key);
        return (value == null || value.trim().isEmpty()) ? defaultValue : value;
    }

    // getters for core properties //

    public URI getNarWorkingDirectory() throws URISyntaxException {
            return getURI(getProperty(NAR_WORKING_DIRECTORY, DEFAULT_NAR_WORKING_DIR));
    }

    public URI getFrameworkWorkingDirectory() throws URISyntaxException{
        return new URI(getNarWorkingDirectory().toString().concat("framework/"));
    }

    public URI getExtensionsWorkingDirectory() throws URISyntaxException{
        return new URI(getNarWorkingDirectory().toString().concat("extensions/"));
    }

    public List<URI> getNarLibraryDirectories() throws URISyntaxException{

        List<URI> narLibraryPaths = new ArrayList<>();

        // go through each property
        for (String propertyName : getPropertyKeys()) {
            // determine if the property is a nar library path
            if (StringUtils.startsWith(propertyName, NAR_LIBRARY_DIRECTORY_PREFIX)
                    || NAR_LIBRARY_DIRECTORY.equals(propertyName)) {
                // attempt to resolve the path specified
                String narLib = getProperty(propertyName);
                if (!StringUtils.isBlank(narLib)) {
                    narLibraryPaths.add(getURI(narLib));
                }
            }
        }

        if (narLibraryPaths.isEmpty()) {
            narLibraryPaths.add(getURI(DEFAULT_NAR_LIBRARY_DIR));
        }

        return narLibraryPaths;
    }

    private URI getURI(String path)throws URISyntaxException{
        // we may have URI's or paths or relative paths
        //
        // if it is not a URI string then use Paths.get().getURI()
        if(path.matches("^[A-Za-z].*//.*$")){
            return new URI(path);
        }
        return Paths.get(path).toUri();
    }

    public URI getComponentDocumentationWorkingDirectory() throws URISyntaxException {
        return getURI(getProperty(COMPONENT_DOCS_DIRECTORY, DEFAULT_COMPONENT_DOCS_DIRECTORY));
    }

    public static NiFiProperties createBasicNiFiProperties(final String propertiesFilePath, final Map<String, String> additionalProperties) {
        final Map<String, String> addProps = (additionalProperties == null) ? Collections.EMPTY_MAP : additionalProperties;
        final Properties properties = new Properties();
        final String nfPropertiesFilePath = (propertiesFilePath == null)
                ? System.getProperty(NiFiProperties.PROPERTIES_FILE_PATH)
                : propertiesFilePath;
        if (nfPropertiesFilePath != null) {
            final File propertiesFile = new File(nfPropertiesFilePath.trim());
            if (!propertiesFile.exists()) {
                throw new RuntimeException("Properties file doesn't exist \'"
                        + propertiesFile.getAbsolutePath() + "\'");
            }
            if (!propertiesFile.canRead()) {
                throw new RuntimeException("Properties file exists but cannot be read \'"
                        + propertiesFile.getAbsolutePath() + "\'");
            }
            InputStream inStream = null;
            try {
                inStream = new BufferedInputStream(new FileInputStream(propertiesFile));
                properties.load(inStream);
            } catch (final Exception ex) {
                throw new RuntimeException("Cannot load properties file due to "
                        + ex.getLocalizedMessage(), ex);
            } finally {
                if (null != inStream) {
                    try {
                        inStream.close();
                    } catch (final Exception ex) {
                        /**
                         * do nothing *
                         */
                    }
                }
            }
        }
        addProps.entrySet().stream().forEach((entry) -> {
            properties.setProperty(entry.getKey(), entry.getValue());
        });
        return new NiFiProperties() {
            @Override
            public String getProperty(String key) {
                return properties.getProperty(key);
            }

            @Override
            public Set<String> getPropertyKeys() {
                return properties.stringPropertyNames();
            }

            @Override
            public void setProperty(String key, String value){
                properties.setProperty(key,value);
            }

        };
    }
}
