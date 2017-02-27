#!/bin/bash
# 
# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
# 
#     http://www.apache.org/licenses/LICENSE-2.0
# 
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

# we need to pull out the sensor argument
# since we will be manipulating the stack
# store away the original string for passing to storm
ORIGINAL_ARGS="$@"
while (( "$#" )); do
  case "$1" in
    -s|--sensor)
      SENSOR=$2
      shift 2
      ;;
    --)
      shift
      break
      ;;
    -*|--*=)
      shift 2
      ;;
  esac
done

# if it is json | csv | grok then change to basic, since they are in the
# metron-parser-basic jar together
if [[ "$SENSOR" == "json" || "$SENSOR" == "csv" || "$SENSOR" == "grok" ]]; then
    SENSOR="base"
fi

METRON_VERSION=${project.version}
METRON_HOME=/usr/metron/$METRON_VERSION
METRON_TELEMETRY_LIB=$METRON_HOME/telemetry/$SENSOR/lib
TELEMETRY_JAR=metron-parser-$SENSOR-$METRON_VERSION-uber.jar
storm jar $METRON_TELEMETRY_LIB/$TELEMETRY_JAR org.apache.metron.parsers.topology.ParserTopologyCLI $ORIGINAL_ARGS
