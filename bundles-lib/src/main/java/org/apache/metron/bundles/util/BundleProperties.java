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
package org.apache.metron.bundles.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.*;

/**
 * The BundleProperties class holds all properties which are needed for various
 * values to be available at runtime. It is strongly tied to the startup
 * properties needed and is often refer to as the 'nifi.properties' file. The
 * properties contains keys and values. Great care should be taken in leveraging
 * this class or passing it along. Its use should be refactored and minimized
 * over time.
 */
public abstract class BundleProperties {

    // core properties
    public static final String PROPERTIES_FILE_PATH = "bundle.properties.file.path";
    public static final String BUNDLE_LIBRARY_DIRECTORY = "bundle.library.directory";
    public static final String BUNDLE_LIBRARY_DIRECTORY_PREFIX = "bundle.library.directory.";
    public static final String BUNDLE_WORKING_DIRECTORY = "bundle.working.directory";
    public static final String COMPONENT_DOCS_DIRECTORY = "bundle.documentation.working.directory";
    public static final String ARCHIVE_EXTENSION = "bundle.archive.extension";
    public static final String META_ID_PREFIX = "bundle.meta.id.prefix";
    public static final String BUNDLE_EXTENSION_TYPE_PREFIX = "bundle.extension.type.";

    // defaults
    public static final String DEFAULT_ARCHIVE_EXTENSION = "bundle";
    public static final String DEFAULT_BUNDLE_WORKING_DIR = "./work/bundle/";
    public static final String DEFAULT_BUNDLE_LIBRARY_DIR = "./lib/";
    public static final String DEFAULT_COMPONENT_DOCS_DIRECTORY = "./work/docs/components/";
    public static final String DEFAULT_META_ID_PREFIX = "Bundle";

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

    public URI getBundleWorkingDirectory() throws URISyntaxException {
            return getURI(getProperty(BUNDLE_WORKING_DIRECTORY, DEFAULT_BUNDLE_WORKING_DIR));
    }

    public URI getFrameworkWorkingDirectory() throws URISyntaxException{
        return getConcatedDirURI(getBundleWorkingDirectory(),"framework/");
    }

    public URI getExtensionsWorkingDirectory() throws URISyntaxException{
        return getConcatedDirURI(getBundleWorkingDirectory(),"extensions/");
    }

    private static URI getConcatedDirURI(URI original, String dir) throws URISyntaxException{
        String uri = original.toString();
        StringBuilder builder = new StringBuilder(uri);
        if(uri.endsWith("/") == false){
            builder.append("/");
        }
        builder.append(dir);
        return new URI(builder.toString());
    }

    public URI getBundleLibraryDirectory() throws URISyntaxException{
        String bundleLib = getProperty(BUNDLE_LIBRARY_DIRECTORY);
        return getURI(bundleLib);
    }

    public List<URI> getBundleLibraryDirectories() throws URISyntaxException{

        List<URI> bundleLibraryPaths = new ArrayList<>();

        // go through each property
        for (String propertyName : getPropertyKeys()) {
            // determine if the property is a bundle library path
            if (StringUtils.startsWith(propertyName, BUNDLE_LIBRARY_DIRECTORY_PREFIX)
                    || BUNDLE_LIBRARY_DIRECTORY.equals(propertyName)) {
                // attempt to resolve the path specified
                String bundleLib = getProperty(propertyName);
                if (!StringUtils.isBlank(bundleLib)) {
                    bundleLibraryPaths.add(getURI(bundleLib));
                }
            }
        }

        if (bundleLibraryPaths.isEmpty()) {
            bundleLibraryPaths.add(getURI(DEFAULT_BUNDLE_LIBRARY_DIR));
        }

        return bundleLibraryPaths;
    }

    public Map<String,String> getParExtensionTypes(){
        HashMap<String,String> extensionTypeMap = new HashMap<>();

        // go through each property
        for (String propertyName : getPropertyKeys()){
            // determine if the property is an extention type
            if(StringUtils.startsWith(propertyName, BUNDLE_EXTENSION_TYPE_PREFIX)){
                // attempt to resolve class name
                String className = getProperty(propertyName);
                if(!StringUtils.isBlank(className)){
                    // get the extension name
                    String extensionName = StringUtils.substringAfterLast(propertyName,".");
                    if(!StringUtils.isBlank(extensionName)){
                        extensionTypeMap.put(extensionName,className);
                    }
                }
            }
        }
        return extensionTypeMap;
    }

    public static URI getURI(String path)throws URISyntaxException{
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

    public static BundleProperties createBasicParProperties(final String propertiesFilePath, final Map<String, String> additionalProperties) {
        final Map<String, String> addProps = (additionalProperties == null) ? new HashMap<>() : additionalProperties;
        final Properties properties = new Properties();
        final String bundlePropertiesFilePath = (propertiesFilePath == null)
                ? System.getProperty(BundleProperties.PROPERTIES_FILE_PATH)
                : propertiesFilePath;
        if (bundlePropertiesFilePath != null) {
            final File propertiesFile = new File(bundlePropertiesFilePath.trim());
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
        return new BundleProperties() {
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
