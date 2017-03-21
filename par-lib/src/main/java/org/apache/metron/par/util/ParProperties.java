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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.*;

/**
 * The ParProperties class holds all properties which are needed for various
 * values to be available at runtime. It is strongly tied to the startup
 * properties needed and is often refer to as the 'nifi.properties' file. The
 * properties contains keys and values. Great care should be taken in leveraging
 * this class or passing it along. Its use should be refactored and minimized
 * over time.
 */
public abstract class ParProperties {

    // core properties
    public static final String PROPERTIES_FILE_PATH = "par.properties.file.path";
    public static final String PAR_LIBRARY_DIRECTORY = "par.library.directory";
    public static final String PAR_LIBRARY_DIRECTORY_PREFIX = "par.library.directory.";
    public static final String PAR_WORKING_DIRECTORY = "par.working.directory";
    public static final String COMPONENT_DOCS_DIRECTORY = "par.documentation.working.directory";
    public static final String ARCHIVE_EXTENSION = "par.archive.extension";
    public static final String META_ID_PREFIX = "par.meta.id.prefix";

    // defaults
    public static final String DEFAULT_ARCHIVE_EXTENSION = "par";
    public static final String DEFAULT_PAR_WORKING_DIR = "./work/par";
    public static final String DEFAULT_PAR_LIBRARY_DIR = "./lib";
    public static final String DEFAULT_COMPONENT_DOCS_DIRECTORY = "./work/docs/components";
    public static final String DEFAULT_META_ID_PREFIX = "Par";

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

    public URI getParWorkingDirectory() throws URISyntaxException {
            return getURI(getProperty(PAR_WORKING_DIRECTORY, DEFAULT_PAR_WORKING_DIR));
    }

    public URI getFrameworkWorkingDirectory() throws URISyntaxException{
        return new URI(getParWorkingDirectory().toString().concat("framework/"));
    }

    public URI getExtensionsWorkingDirectory() throws URISyntaxException{
        return new URI(getParWorkingDirectory().toString().concat("extensions/"));
    }

    public List<URI> getParLibraryDirectories() throws URISyntaxException{

        List<URI> parLibraryPaths = new ArrayList<>();

        // go through each property
        for (String propertyName : getPropertyKeys()) {
            // determine if the property is a par library path
            if (StringUtils.startsWith(propertyName, PAR_LIBRARY_DIRECTORY_PREFIX)
                    || PAR_LIBRARY_DIRECTORY.equals(propertyName)) {
                // attempt to resolve the path specified
                String parLib = getProperty(propertyName);
                if (!StringUtils.isBlank(parLib)) {
                    parLibraryPaths.add(getURI(parLib));
                }
            }
        }

        if (parLibraryPaths.isEmpty()) {
            parLibraryPaths.add(getURI(DEFAULT_PAR_LIBRARY_DIR));
        }

        return parLibraryPaths;
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
    
    public String getMetaIdPrefix(){
        return getProperty(META_ID_PREFIX,DEFAULT_META_ID_PREFIX);
    }

    public String getArchiveExtension(){
        return getProperty(ARCHIVE_EXTENSION,DEFAULT_ARCHIVE_EXTENSION);
    }

    public URI getComponentDocumentationWorkingDirectory() throws URISyntaxException {
        return getURI(getProperty(COMPONENT_DOCS_DIRECTORY, DEFAULT_COMPONENT_DOCS_DIRECTORY));
    }

    public static ParProperties createBasicParProperties(final String propertiesFilePath, final Map<String, String> additionalProperties) {
        final Map<String, String> addProps = (additionalProperties == null) ? Collections.EMPTY_MAP : additionalProperties;
        final Properties properties = new Properties();
        final String parPropertiesFilePath = (propertiesFilePath == null)
                ? System.getProperty(ParProperties.PROPERTIES_FILE_PATH)
                : propertiesFilePath;
        if (parPropertiesFilePath != null) {
            final File propertiesFile = new File(parPropertiesFilePath.trim());
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
        return new ParProperties() {
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
