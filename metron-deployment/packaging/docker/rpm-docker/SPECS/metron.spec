#
#  Licensed to the Apache Software Foundation (ASF) under one or more
#  contributor license agreements.  See the NOTICE file distributed with
#  this work for additional information regarding copyright ownership.
#  The ASF licenses this file to You under the Apache License, Version 2.0
#  (the "License"); you may not use this file except in compliance with
#  the License.  You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.
#
%define timestamp           %(date +%Y%m%d%H%M)
%define version             %{?_version}%{!?_version:UNKNOWN}
%define full_version        %{version}%{?_prerelease}
%define prerelease_fmt      %{?_prerelease:.%{_prerelease}}          
%define vendor_version      %{?_vendor_version}%{!?_vendor_version: UNKNOWN}
%define url                 http://metron.incubator.apache.org/
%define base_name           metron
%define name                %{base_name}-%{vendor_version}
%define versioned_app_name  %{base_name}-%{version}
%define buildroot           %{_topdir}/BUILDROOT/%{versioned_app_name}-root
%define installpriority     %{_priority} # Used by alternatives for concurrent version installs
%define __jar_repack        %{nil}

%define metron_root         %{_prefix}/%{base_name}
%define metron_home         %{metron_root}/%{full_version}
%define telemetry_home       %{metron_home}/telemetry

# ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Name:           %{base_name}
Version:        %{version}
Release:        %{timestamp}%{prerelease_fmt}
BuildRoot:      %{buildroot}
BuildArch:      noarch
Summary:        Apache Metron provides a scalable advanced security analytics framework
License:        ASL 2.0
Group:          Applications/Internet
URL:            %{url}
Source0:        metron-common-%{full_version}-archive.tar.gz
Source1:        metron-parsers-common-%{full_version}-archive.tar.gz
Source2:        metron-parser-asa-%{full_version}-archive.tar.gz
Source3:        metron-parser-base-%{full_version}-archive.tar.gz
Source4:        metron-parser-bro-%{full_version}-archive.tar.gz
Source5:        metron-parser-cef-%{full_version}-archive.tar.gz
Source6:        metron-parser-fireeye-%{full_version}-archive.tar.gz
Source7:        metron-parser-ise-%{full_version}-archive.tar.gz
Source8:        metron-parser-lancope-%{full_version}-archive.tar.gz
Source9:        metron-parser-logstash-%{full_version}-archive.tar.gz
Source10:        metron-parser-paloalto-%{full_version}-archive.tar.gz
Source11:        metron-parser-snort-%{full_version}-archive.tar.gz
Source12:        metron-parser-sourcefire-%{full_version}-archive.tar.gz
Source13:        metron-parser-squid-%{full_version}-archive.tar.gz
Source14:        metron-parser-websphere-%{full_version}-archive.tar.gz
Source15:        metron-parser-yaf-%{full_version}-archive.tar.gz
Source16:        metron-elasticsearch-%{full_version}-archive.tar.gz
Source17:        metron-data-management-%{full_version}-archive.tar.gz
Source18:        metron-solr-%{full_version}-archive.tar.gz
Source19:        metron-enrichment-%{full_version}-archive.tar.gz
Source20:        metron-indexing-%{full_version}-archive.tar.gz
Source21:        metron-pcap-backend-%{full_version}-archive.tar.gz
Source22:        metron-profiler-%{full_version}-archive.tar.gz

%description
Apache Metron provides a scalable advanced security analytics framework

%prep
rm -rf %{_rpmdir}/%{buildarch}/%{versioned_app_name}*
rm -rf %{_srcrpmdir}/%{versioned_app_name}*

%build
rm -rf %{_builddir}
mkdir -p %{_builddir}/%{versioned_app_name}

%clean
rm -rf %{buildroot}
rm -rf %{_builddir}/*

%install
rm -rf %{buildroot}
mkdir -p %{buildroot}%{metron_home}
mkdir -p %{buildroot}%{telemetry_home}
mkdir -p %{buildroot}%{telemetry_home}/asa
mkdir -p %{buildroot}%{telemetry_home}/base
mkdir -p %{buildroot}%{telemetry_home}/bro
mkdir -p %{buildroot}%{telemetry_home}/cef
mkdir -p %{buildroot}%{telemetry_home}/fireeye
mkdir -p %{buildroot}%{telemetry_home}/ise
mkdir -p %{buildroot}%{telemetry_home}/lancope
mkdir -p %{buildroot}%{telemetry_home}/logstash
mkdir -p %{buildroot}%{telemetry_home}/paloalto
mkdir -p %{buildroot}%{telemetry_home}/snort
mkdir -p %{buildroot}%{telemetry_home}/sourcefire
mkdir -p %{buildroot}%{telemetry_home}/websphere
mkdir -p %{buildroot}%{telemetry_home}/yaf

# copy source files and untar
tar -xzf %{SOURCE0} -C %{buildroot}%{metron_home}
tar -xzf %{SOURCE1} -C %{buildroot}%{metron_home}
tar -xzf %{SOURCE2} -C %{buildroot}%{telemetry_home}/asa
tar -xzf %{SOURCE3} -C %{buildroot}%{telemetry_home}/base
tar -xzf %{SOURCE4} -C %{buildroot}%{telemetry_home}/bro
tar -xzf %{SOURCE5} -C %{buildroot}%{telemetry_home}/cef
tar -xzf %{SOURCE6} -C %{buildroot}%{telemetry_home}/fireeye
tar -xzf %{SOURCE7} -C %{buildroot}%{telemetry_home}/ise
tar -xzf %{SOURCE8} -C %{buildroot}%{telemetry_home}/lancope
tar -xzf %{SOURCE9} -C %{buildroot}%{telemetry_home}/logstash
tar -xzf %{SOURCE10} -C %{buildroot}%{telemetry_home}/paloalto
tar -xzf %{SOURCE11} -C %{buildroot}%{telemetry_home}/snort
tar -xzf %{SOURCE12} -C %{buildroot}%{telemetry_home}/sourcefire
tar -xzf %{SOURCE14} -C %{buildroot}%{telemetry_home}/websphere
tar -xzf %{SOURCE15} -C %{buildroot}%{telemetry_home}/yaf
tar -xzf %{SOURCE16} -C %{buildroot}%{metron_home}
tar -xzf %{SOURCE17} -C %{buildroot}%{metron_home}
tar -xzf %{SOURCE18} -C %{buildroot}%{metron_home}
tar -xzf %{SOURCE19} -C %{buildroot}%{metron_home}
tar -xzf %{SOURCE20} -C %{buildroot}%{metron_home}
tar -xzf %{SOURCE21} -C %{buildroot}%{metron_home}
tar -xzf %{SOURCE22} -C %{buildroot}%{metron_home}

# ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

%package        common
Summary:        Metron Common
Group:          Applications/Internet
Provides:       common = %{version}

%description    common
This package installs the Metron common files %{metron_home}

%files          common

%defattr(-,root,root,755)
%dir %{metron_root}
%dir %{metron_home}
%dir %{metron_home}/bin
%dir %{metron_home}/lib
%{metron_home}/bin/zk_load_configs.sh
%{metron_home}/bin/stellar
%attr(0644,root,root) %{metron_home}/lib/metron-common-%{full_version}.jar

# ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

%package        parsers-common
Summary:        Metron Common Parser Files
Group:          Applications/Internet
Provides:       parsers-common = %{version}

%description    parsers-common
This package installs the Metron Parser files

%files          parsers-common
%defattr(-,root,root,755)
%dir %{metron_root}
%dir %{metron_home}
%dir %{metron_home}/bin
%dir %{metron_home}/config
%dir %{metron_home}/patterns
%dir %{metron_home}/lib
%{metron_home}/patterns/common
%{metron_home}/bin/start_parser_topology.sh
%attr(0644,root,root) %{metron_home}/lib/metron-parsers-common-%{full_version}-uber.jar

# ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

%package        parser-asa
Summary:        Metron ASA Parser Files
Group:          Applications/Internet
Provides:       parser-asa = %{version}

%description    parser-asa
This package installs the Metron ASA Parser files

%files          parser-asa
%defattr(-,root,root,755)
%dir %{metron_root}
%dir %{metron_home}
%dir %{telemetry_home}
%dir %{telemetry_home}/asa/config
%dir %{telemetry_home}/asa/config/zookeeper
%dir %{telemetry_home}/asa/config/zookeeper/parsers
%dir %{telemetry_home}/asa/config/zookeeper/enrichments
%dir %{telemetry_home}/asa/config/zookeeper/indexing
%dir %{telemetry_home}/asa/patterns
%dir %{telemetry_home}/asa/lib
%{telemetry_home}/asa/config/zookeeper/parsers/asa.json
%{telemetry_home}/asa/config/zookeeper/enrichments/asa.json
%{telemetry_home}/asa/config/zookeeper/indexing/asa.json
%{telemetry_home}/asa/patterns/asa
%{telemetry_home}/asa/patterns/common
%attr(0644,root,root) %{telemetry_home}/asa/lib/metron-parser-asa-%{full_version}-uber.jar

# ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

%package        parser-base
Summary:        Metron Base Parser Files
Group:          Applications/Internet
Provides:       parser-base = %{version}

%description    parser-base
This package installs the Metron Base Parser files

%files          parser-base
%defattr(-,root,root,755)
%dir %{metron_root}
%dir %{metron_home}
%dir %{telemetry_home}
%dir %{telemetry_home}/base/config/zookeeper
%dir %{telemetry_home}/base/config/zookeeper/parsers
%dir %{telemetry_home}/base/config/zookeeper/enrichments
%dir %{telemetry_home}/base/config/zookeeper/indexing
%dir %{telemetry_home}/base/patterns
%dir %{telemetry_home}/base/lib
%{telemetry_home}/base/config/zookeeper/parsers/csv.json
%{telemetry_home}/base/config/zookeeper/parsers/grok.json
%{telemetry_home}/base/config/zookeeper/parsers/jsonMap.json
%{telemetry_home}/base/config/zookeeper/enrichments/csv.json
%{telemetry_home}/base/config/zookeeper/enrichments/grok.json
%{telemetry_home}/base/config/zookeeper/enrichments/jsonMap.json
%{telemetry_home}/base/config/zookeeper/indexing/csv.json
%{telemetry_home}/base/config/zookeeper/indexing/grok.json
%{telemetry_home}/base/config/zookeeper/indexing/jsonMap.json
%{telemetry_home}/base/patterns/common
%attr(0644,root,root) %{telemetry_home}/base/lib/metron-parser-base-%{full_version}-uber.jar

# ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

%package        parser-bro
Summary:        Metron Bro Parser Files
Group:          Applications/Internet
Provides:       parser-bro = %{version}

%description    parser-bro
This package installs the Metron Bro Parser files

%files          parser-bro
%defattr(-,root,root,755)
%dir %{metron_root}
%dir %{metron_home}
%dir %{telemetry_home}
%dir %{telemetry_home}/bro/config
%dir %{telemetry_home}/bro/config/zookeeper
%dir %{telemetry_home}/bro/config/zookeeper/parsers
%dir %{telemetry_home}/bro/config/zookeeper/enrichments
%dir %{telemetry_home}/bro/config/zookeeper/indexing
%dir %{telemetry_home}/bro/lib
%{telemetry_home}/bro/config/zookeeper/parsers/bro.json
%{telemetry_home}/bro/config/zookeeper/enrichments/bro.json
%{telemetry_home}/bro/config/zookeeper/indexing/bro.json
%attr(0644,root,root) %{telemetry_home}/bro/lib/metron-parser-bro-%{full_version}-uber.jar


# ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

%package        parser-cef
Summary:        Metron CEF Parser Files
Group:          Applications/Internet
Provides:       parser-cef = %{version}

%description    parser-cef
This package installs the Metron CEF Parser files

%files          parser-cef
%defattr(-,root,root,755)
%dir %{metron_root}
%dir %{metron_home}
%dir %{telemetry_home}
%dir %{telemetry_home}/cef/config
%dir %{telemetry_home}/cef/config/zookeeper
%dir %{telemetry_home}/cef/config/zookeeper/parsers
%dir %{telemetry_home}/cef/config/zookeeper/enrichments
%dir %{telemetry_home}/cef/config/zookeeper/indexing
%dir %{telemetry_home}/cef/lib
%{telemetry_home}/cef/config/zookeeper/parsers/cef.json
%{telemetry_home}/cef/config/zookeeper/enrichments/cef.json
%{telemetry_home}/cef/config/zookeeper/indexing/cef.json
%attr(0644,root,root) %{telemetry_home}/cef/lib/metron-parser-cef-%{full_version}-uber.jar


# ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

%package        parser-fireeye
Summary:        Metron Fireeye Parser Files
Group:          Applications/Internet
Provides:       parser-fireeye = %{version}

%description    parser-fireeye
This package installs the Metron Fireeye Parser files

%files          parser-fireeye
%defattr(-,root,root,755)
%dir %{metron_root}
%dir %{metron_home}
%dir %{telemetry_home}
%dir %{telemetry_home}/fireeye/config
%dir %{telemetry_home}/fireeye/config/zookeeper
%dir %{telemetry_home}/fireeye/config/zookeeper/parsers
%dir %{telemetry_home}/fireeye/config/zookeeper/enrichments
%dir %{telemetry_home}/fireeye/config/zookeeper/indexing
%dir %{telemetry_home}/fireeye/patterns
%dir %{telemetry_home}/fireeye/lib
%{telemetry_home}/fireeye/config/zookeeper/parsers/fireeye.json
%{telemetry_home}/fireeye/config/zookeeper/enrichments/fireeye.json
%{telemetry_home}/fireeye/config/zookeeper/indexing/fireeye.json
%{telemetry_home}/fireeye/patterns/fireeye
%{telemetry_home}/fireeye/patterns/common
%attr(0644,root,root) %{telemetry_home}/fireeye/lib/metron-parser-fireeye-%{full_version}-uber.jar


# ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

%package        parser-ise
Summary:        Metron Ise Parser Files
Group:          Applications/Internet
Provides:       parser-ise = %{version}

%description    parser-ise
This package installs the Metron Ise Parser files

%files          parser-ise
%defattr(-,root,root,755)
%dir %{metron_root}
%dir %{metron_home}
%dir %{telemetry_home}
%dir %{telemetry_home}/ise/config
%dir %{telemetry_home}/ise/config/zookeeper
%dir %{telemetry_home}/ise/config/zookeeper/parsers
%dir %{telemetry_home}/ise/config/zookeeper/enrichments
%dir %{telemetry_home}/ise/config/zookeeper/indexing
%dir %{telemetry_home}/ise/patterns
%dir %{telemetry_home}/ise/lib
%{telemetry_home}/ise/config/zookeeper/parsers/ise.json
%{telemetry_home}/ise/config/zookeeper/enrichments/ise.json
%{telemetry_home}/ise/config/zookeeper/indexing/ise.json
%{telemetry_home}/ise/patterns/common
%attr(0644,root,root) %{telemetry_home}/ise/lib/metron-parser-ise-%{full_version}-uber.jar


# ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

%package        parser-lancope
Summary:        Metron Lancope Parser Files
Group:          Applications/Internet
Provides:       parser-lancope = %{version}

%description    parser-lancope
This package installs the Metron Lancope Parser files

%files          parser-lancope
%defattr(-,root,root,755)
%dir %{metron_root}
%dir %{metron_home}
%dir %{telemetry_home}
%dir %{telemetry_home}/lancope/config
%dir %{telemetry_home}/lancope/config/zookeeper
%dir %{telemetry_home}/lancope/config/zookeeper/parsers
%dir %{telemetry_home}/lancope/config/zookeeper/enrichments
%dir %{telemetry_home}/lancope/config/zookeeper/indexing
%dir %{telemetry_home}/lancope/patterns
%dir %{telemetry_home}/lancope/lib
%{telemetry_home}/lancope/config/zookeeper/parsers/lancope.json
%{telemetry_home}/lancope/config/zookeeper/enrichments/lancope.json
%{telemetry_home}/lancope/config/zookeeper/indexing/lancope.json
%{telemetry_home}/lancope/patterns/common
%attr(0644,root,root) %{telemetry_home}/lancope/lib/metron-parser-lancope-%{full_version}-uber.jar


# ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

%package        parser-logstash
Summary:        Metron Logstash Parser Files
Group:          Applications/Internet
Provides:       parser-logstash = %{version}

%description    parser-logstash
This package installs the Metron Logstash Parser files

%files          parser-logstash
%defattr(-,root,root,755)
%dir %{metron_root}
%dir %{metron_home}
%dir %{telemetry_home}
%dir %{telemetry_home}/logstash/config
%dir %{telemetry_home}/logstash/config/zookeeper
%dir %{telemetry_home}/logstash/config/zookeeper/parsers
%dir %{telemetry_home}/logstash/config/zookeeper/enrichments
%dir %{telemetry_home}/logstash/config/zookeeper/indexing
%dir %{telemetry_home}/logstash/patterns
%dir %{telemetry_home}/logstash/lib
%{telemetry_home}/logstash/config/zookeeper/parsers/logstash.json
%{telemetry_home}/logstash/config/zookeeper/enrichments/logstash.json
%{telemetry_home}/logstash/config/zookeeper/indexing/logstash.json
%{telemetry_home}/logstash/patterns/common
%attr(0644,root,root) %{telemetry_home}/logstash/lib/metron-parser-logstash-%{full_version}-uber.jar


# ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

%package        parser-paloalto
Summary:        Metron Aalo Alto Parser Files
Group:          Applications/Internet
Provides:       parser-paloalto = %{version}

%description    parser-paloalto
This package installs the Metron Palo Alto Parser files

%files          parser-paloalto
%defattr(-,root,root,755)
%dir %{metron_root}
%dir %{metron_home}
%dir %{telemetry_home}
%dir %{telemetry_home}/paloalto/config
%dir %{telemetry_home}/paloalto/config/zookeeper
%dir %{telemetry_home}/paloalto/config/zookeeper/parsers
%dir %{telemetry_home}/paloalto/config/zookeeper/enrichments
%dir %{telemetry_home}/paloalto/config/zookeeper/indexing
%dir %{telemetry_home}/paloalto/patterns
%dir %{telemetry_home}/paloalto/lib
%{telemetry_home}/paloalto/config/zookeeper/parsers/paloalto.json
%{telemetry_home}/paloalto/config/zookeeper/enrichments/paloalto.json
%{telemetry_home}/paloalto/config/zookeeper/indexing/paloalto.json
%{telemetry_home}/paloalto/patterns/common
%attr(0644,root,root) %{telemetry_home}/paloalto/lib/metron-parser-paloalto-%{full_version}-uber.jar


# ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

%package        parser-snort
Summary:        Metron Snort Parser Files
Group:          Applications/Internet
Provides:       parser-snort = %{version}

%description    parser-snort
This package installs the Metron Snort Parser files

%files          parser-snort
%defattr(-,root,root,755)
%dir %{metron_root}
%dir %{metron_home}
%dir %{telemetry_home}
%dir %{telemetry_home}/snort/config
%dir %{telemetry_home}/snort/config/zookeeper
%dir %{telemetry_home}/snort/config/zookeeper/parsers
%dir %{telemetry_home}/snort/config/zookeeper/enrichments
%dir %{telemetry_home}/snort/config/zookeeper/indexing
%dir %{telemetry_home}/snort/patterns
%dir %{telemetry_home}/snort/lib
%{telemetry_home}/snort/config/zookeeper/parsers/snort.json
%{telemetry_home}/snort/config/zookeeper/enrichments/snort.json
%{telemetry_home}/snort/config/zookeeper/indexing/snort.json
%{telemetry_home}/snort/patterns/common
%attr(0644,root,root) %{telemetry_home}/snort/lib/metron-parser-snort-%{full_version}-uber.jar


# ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

%package        parser-sourcefire
Summary:        Metron Sourcefire Parser Files
Group:          Applications/Internet
Provides:       parser-sourcefire = %{version}

%description    parser-sourcefire
This package installs the Metron sourcefire Parser files

%files          parser-sourcefire
%defattr(-,root,root,755)
%dir %{metron_root}
%dir %{metron_home}
%dir %{telemetry_home}
%dir %{telemetry_home}/sourcefire/config
%dir %{telemetry_home}/sourcefire/config/zookeeper
%dir %{telemetry_home}/sourcefire/config/zookeeper/parsers
%dir %{telemetry_home}/sourcefire/config/zookeeper/enrichments
%dir %{telemetry_home}/sourcefire/config/zookeeper/indexing
%dir %{telemetry_home}/sourcefire/patterns
%dir %{telemetry_home}/sourcefire/lib
%{telemetry_home}/sourcefire/config/zookeeper/parsers/sourcefire.json
%{telemetry_home}/sourcefire/config/zookeeper/enrichments/sourcefire.json
%{telemetry_home}/sourcefire/config/zookeeper/indexing/sourcefire.json
%{telemetry_home}/sourcefire/patterns/sourcefire
%{telemetry_home}/sourcefire/patterns/common
%attr(0644,root,root) %{telemetry_home}/sourcefire/lib/metron-parser-sourcefire-%{full_version}-uber.jar


# ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

%package        parser-websphere
Summary:        Metron Websphere Parser Files
Group:          Applications/Internet
Provides:       parser-websphere = %{version}

%description    parser-websphere
This package installs the Metron Websphere Parser files

%files          parser-websphere
%defattr(-,root,root,755)
%dir %{metron_root}
%dir %{metron_home}
%dir %{telemetry_home}
%dir %{telemetry_home}/websphere/config
%dir %{telemetry_home}/websphere/config/zookeeper
%dir %{telemetry_home}/websphere/config/zookeeper/parsers
%dir %{telemetry_home}/websphere/config/zookeeper/enrichments
%dir %{telemetry_home}/websphere/config/zookeeper/indexing
%dir %{telemetry_home}/websphere/patterns
%{telemetry_home}/websphere/config/zookeeper/parsers/websphere.json
%{telemetry_home}/websphere/config/zookeeper/enrichments/websphere.json
%{telemetry_home}/websphere/config/zookeeper/indexing/websphere.json
%{telemetry_home}/websphere/patterns/websphere
%{telemetry_home}/websphere/patterns/common
%dir %{telemetry_home}/websphere/lib
%attr(0644,root,root) %{telemetry_home}/websphere/lib/metron-parser-websphere-%{full_version}-uber.jar


# ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

%package        parser-yaf
Summary:        Metron Yaf Parser Files
Group:          Applications/Internet
Provides:       parser-yaf = %{version}

%description    parser-yaf
This package installs the Metron Yaf Parser files

%files          parser-yaf
%defattr(-,root,root,755)
%dir %{metron_root}
%dir %{metron_home}
%dir %{telemetry_home}
%dir %{telemetry_home}/yaf/config
%dir %{telemetry_home}/yaf/config/zookeeper
%dir %{telemetry_home}/yaf/config/zookeeper/parsers
%dir %{telemetry_home}/yaf/config/zookeeper/enrichments
%dir %{telemetry_home}/yaf/config/zookeeper/indexing
%dir %{telemetry_home}/yaf/patterns
%dir %{telemetry_home}/yaf/lib
%{telemetry_home}/yaf/config/zookeeper/parsers/yaf.json
%{telemetry_home}/yaf/config/zookeeper/enrichments/yaf.json
%{telemetry_home}/yaf/config/zookeeper/indexing/yaf.json
%{telemetry_home}/yaf/patterns/yaf
%{telemetry_home}/yaf/patterns/common
%attr(0644,root,root) %{telemetry_home}/yaf/lib/metron-parser-yaf-%{full_version}-uber.jar


# ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
%package        elasticsearch
Summary:        Metron Elasticsearch Files
Group:          Applications/Internet
Provides:       elasticsearch = %{version}

%description    elasticsearch
This package installs the Metron Elasticsearch files

%files          elasticsearch
%defattr(-,root,root,755)
%dir %{metron_root}
%dir %{metron_home}
%dir %{metron_home}/bin
%dir %{metron_home}/config
%dir %{metron_home}/lib
%{metron_home}/bin/start_elasticsearch_topology.sh
%{metron_home}/config/elasticsearch.properties
%attr(0644,root,root) %{metron_home}/lib/metron-elasticsearch-%{full_version}-uber.jar

# ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

%package        data-management
Summary:        Metron Data Management Files
Group:          Applications/Internet
Provides:       data-management = %{version}

%description    data-management
This package installs the Metron Parser files

%files          data-management
%defattr(-,root,root,755)
%dir %{metron_root}
%dir %{metron_home}
%dir %{metron_home}/bin
%dir %{metron_home}/lib
%{metron_home}/bin/Whois_CSV_to_JSON.py
%{metron_home}/bin/geo_enrichment_load.sh
%{metron_home}/bin/flatfile_loader.sh
%{metron_home}/bin/prune_elasticsearch_indices.sh
%{metron_home}/bin/prune_hdfs_files.sh
%{metron_home}/bin/threatintel_bulk_prune.sh
%{metron_home}/bin/threatintel_taxii_load.sh
%attr(0644,root,root) %{metron_home}/lib/metron-data-management-%{full_version}.jar

# ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

%package        solr
Summary:        Metron Solr Files
Group:          Applications/Internet
Provides:       solr = %{version}

%description    solr
This package installs the Metron Solr files

%files          solr
%defattr(-,root,root,755)
%dir %{metron_root}
%dir %{metron_home}
%dir %{metron_home}/bin
%dir %{metron_home}/config
%dir %{metron_home}/lib
%{metron_home}/bin/start_solr_topology.sh
%{metron_home}/config/solr.properties
%attr(0644,root,root) %{metron_home}/lib/metron-solr-%{full_version}-uber.jar

# ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~  

%package        enrichment
Summary:        Metron Enrichment Files
Group:          Applications/Internet
Provides:       enrichment = %{version}

%description    enrichment
This package installs the Metron Enrichment files

%files          enrichment
%defattr(-,root,root,755)
%dir %{metron_root}
%dir %{metron_home}
%dir %{metron_home}/bin
%dir %{metron_home}/config
%dir %{metron_home}/flux
%dir %{metron_home}/flux/enrichment
%{metron_home}/bin/latency_summarizer.sh
%{metron_home}/bin/start_enrichment_topology.sh
%{metron_home}/config/enrichment.properties
%{metron_home}/flux/enrichment/remote.yaml
%exclude %{metron_home}/flux/enrichment/test.yaml
%attr(0644,root,root) %{metron_home}/lib/metron-enrichment-%{full_version}-uber.jar

# ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~  

%package        indexing
Summary:        Metron Indexing Files
Group:          Applications/Internet
Provides:       indexing = %{version}

%description    indexing
This package installs the Metron Indexing files

%files          indexing
%defattr(-,root,root,755)
%dir %{metron_root}
%dir %{metron_home}
%dir %{metron_home}/flux
%dir %{metron_home}/flux/indexing
%dir %{metron_home}/config/zookeeper/indexing
%{metron_home}/flux/indexing/remote.yaml
%{metron_home}/config/zeppelin/metron/metron-yaf-telemetry.json
%{metron_home}/config/zookeeper/indexing/error.json


# ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

%package        pcap
Summary:        Metron PCAP
Group:          Applications/Internet
Provides:       pcap = %{version}

%description    pcap
This package installs the Metron PCAP files %{metron_home}

%files          pcap
%defattr(-,root,root,755)
%dir %{metron_root}
%dir %{metron_home}
%dir %{metron_home}/config
%dir %{metron_home}/bin
%dir %{metron_home}/flux
%dir %{metron_home}/flux/pcap
%dir %{metron_home}/lib
%{metron_home}/config/pcap.properties
%{metron_home}/bin/pcap_inspector.sh
%{metron_home}/bin/pcap_query.sh
%{metron_home}/bin/start_pcap_topology.sh
%{metron_home}/flux/pcap/remote.yaml
%attr(0644,root,root) %{metron_home}/lib/metron-pcap-backend-%{full_version}.jar

# ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

%package        profiler
Summary:        Metron Profiler
Group:          Applications/Internet
Provides:       profiler = %{version}

%description    profiler
This package installs the Metron Profiler %{metron_home}

%files          profiler
%defattr(-,root,root,755)
%dir %{metron_root}
%dir %{metron_home}
%dir %{metron_home}/config
%dir %{metron_home}/bin
%dir %{metron_home}/flux
%dir %{metron_home}/flux/profiler
%dir %{metron_home}/lib
%{metron_home}/config/profiler.properties
%{metron_home}/bin/start_profiler_topology.sh
%{metron_home}/flux/profiler/remote.yaml
%attr(0644,root,root) %{metron_home}/lib/metron-profiler-%{full_version}-uber.jar


# ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

%changelog
* Thu Mar 02 2017 Otto Fowler <ottobackwards@gmail.com> - 0.3.1
- remove enrichment configurations that are now per parser
* Thu Jan 19 2017 Justin Leet <justinjleet@gmail.com> - 0.3.1
- Replace GeoIP files with new implementation
* Thu Nov 03 2016 David Lyle <dlyle65535@gmail.com> - 0.2.1
- Add ASA parser/enrichment configuration files 
* Thu Jul 21 2016 Michael Miklavcic <michael.miklavcic@gmail.com> - 0.2.1
- Remove parser flux files
- Add new enrichment files
* Thu Jul 14 2016 Michael Miklavcic <michael.miklavcic@gmail.com> - 0.2.1
- Adding PCAP subpackage
- Added directory macros to files sections
* Thu Jul 14 2016 Justin Leet <justinjleet@gmail.com> - 0.2.1
- Adding Enrichment subpackage
* Thu Jul 14 2016 Justin Leet <justinjleet@gmail.com> - 0.2.1
- Adding Solr subpackage
* Thu Jul 14 2016 Justin Leet <justinjleet@gmail.com> - 0.2.1
- Adding Data Management subpackage
* Thu Jul 14 2016 Justin Leet <jsutinjleet@gmail.com> - 0.2.1
- Adding Elasticsearch subpackage
* Wed Jul 13 2016 Justin Leet <justinjleet@gmail.com> - 0.2.1
- Adding Parsers subpackage
* Tue Jul 12 2016 Michael Miklavcic <michael.miklavcic@gmail.com> - 0.2.1
- First packaging
