<!--
Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
-->

# Metron Extension Deployment

Metron Extensions have varied packaging, as it is defined by the extension type and it's requirements.
What is common across extension types is the deployment of the [Metron Bundles](../../bundles-lib) containing 
the extension library (if present).

These Bundles are deployed to HDFS under /apps/metron/extension_lib.
The /apps/metron/extension_working directory is used at runtime by the bundle system.

> NOTE: Bundles may also be deployed locally on the cluster under /usr/metron/VERSION/

```bash
    drwxrwxr-x   - metron hadoop          0 2017-06-27 16:15 /apps/metron/extension_lib
    drwxrwxr-x   - metron hadoop          0 2017-06-27 16:16 /apps/metron/extension_working
```


See specific extension deployment information

- [Parser Extension Deployment](metron-parser-extensions/parser_extension_deployment.md)
