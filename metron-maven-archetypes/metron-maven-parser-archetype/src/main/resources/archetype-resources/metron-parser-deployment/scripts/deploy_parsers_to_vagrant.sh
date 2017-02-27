#!/usr/bin/env bash
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

#
# extracts information from the host environment that is useful for
# troubleshooting Apache Metron deployments
#
# we need to pull out the sensor argument
# since we will be manipulating the stack
# store away the original string for passing to storm

INVENTORY_PATH=../inventory/full-dev-platform
VAGRANT_PATH=
INVENTORY_PATH_INPUT=

while getopts v:p: opt; do
    case $opt in
        v)
            VAGRANT_PATH=$OPTARG
            ;;
        p)
            INVENTORY_PATH_INPUT=$OPTARG
            ;;
    esac
done

# there must be VAGRANT_PATH
if [ -z "$VAGRANT_PATH" ]; then
    echo "Missing vagrant path";
    exit 1
else
    echo "Using vagrant path:  '$VAGRANT_PATH'";
fi

# but that may have been overridden
if [ -n "$INVENTORY_PATH_INPUT" ]; then
    INVENTORY_PATH=$INVENTORY_PATH_INPUT
fi

echo "Using inventory path:  '$INVENTORY_PATH'";

# but they must exist
if [ ! -d "$VAGRANT_PATH" ]; then
    echo "Vagrant Path ['$VAGRANT_PATH'] does not exist!"
    exit 1
fi

if [ ! -d "$INVENTORY_PATH" ]; then
    echo "Inventory Path ['$INVENTORY_PATH'] does not exist!"
    exit 1
fi


ansible-playbook -v -i $INVENTORY_PATH --private-key=$VAGRANT_PATH/.vagrant/machines/node1/virtualbox/private_key -u vagrant parser_install.yml
