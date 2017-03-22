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

import java.util.*;

public class ExtensionMapping {

    private final Map<String,List<String>> extensionNameMap = new HashMap<>();

    void addExtension(final String extensionName, String extensionClassName){
        if(extensionNameMap.containsKey(extensionName)){
            extensionNameMap.get(extensionName).add(extensionClassName);
        }else{
            List<String> list = new ArrayList<>();
            list.add(extensionClassName);
            extensionNameMap.put(extensionName,list);
        }
    }

    void addAllExtensions(final String extensionName, List<String> extensionClassNames){
        if(extensionNameMap.containsKey(extensionName)){
            extensionNameMap.get(extensionName).addAll(extensionClassNames);
        }else{
            List<String> list = new ArrayList<>();
            list.addAll(extensionClassNames);
            extensionNameMap.put(extensionName,list);
        }
    }

    public List<String> getExtensionNames(String extensionName){
        if(extensionNameMap.containsKey(extensionName)){
            return Collections.unmodifiableList(extensionNameMap.get(extensionName));
        }else{
            return Collections.EMPTY_LIST;
        }
    }

    public Map<String,List<String>> getAllExtensions(){
        return Collections.unmodifiableMap(extensionNameMap);
    }

    public List<String> getAllExtensionNames() {
        final List<String> extensionNames = new ArrayList<>();
        for (Map.Entry<String,List<String>> entry : extensionNameMap.entrySet()){
            extensionNames.addAll(entry.getValue());
        }
        return extensionNames;
    }

    public List<String> getAllExtensionNames(String extensionName) {
        final List<String> extensionNames = new ArrayList<>();
        if(extensionNameMap.containsKey(extensionName)){
            extensionNames.addAll(extensionNameMap.get(extensionName));
        }
        return extensionNames;
    }
}
