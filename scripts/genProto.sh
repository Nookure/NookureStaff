#!/usr/bin/env bash

echo "Generating protobuf files..."

git submodule update --init

protoc --proto_path=NookureStaff-Protobuf --java_out=lite:NookureStaff-API/src/main/java/ NookureStaff-Protobuf/*.proto