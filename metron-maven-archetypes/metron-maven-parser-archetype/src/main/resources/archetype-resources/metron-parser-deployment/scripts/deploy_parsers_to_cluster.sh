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

while (( "$#" )); do
  case "$1" in
    -p|--inventoryPath)
      INVENTORY_PATH=$2
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

# there must be INVENTORY_PATH
if [ -z "$INVENTORY_PATH" ]; then
    echo "Missing inventory path";
    exit 1
else
    echo "Using inventory path:  '$INVENTORY_PATH'";
fi

# but it must exist
if [ ! -d "$INVENTORY_PATH" ]; then
    echo "Inventory Path ['$INVENTORY_PATH'] does not exist!"
    exit 1
fi

ansible-playbook -v -i $INVENTORY_PATH
