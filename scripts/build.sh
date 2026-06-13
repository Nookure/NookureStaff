#!/usr/bin/env bash

if [ "$1" == "--full" ]; then
  echo "Building full project"
  ./scripts/genProto.sh
  ./scripts/migrate.sh
fi

echo "Building project..."
./gradlew shadowJar

echo "Build successful!"
echo "You can find the jar file in the build/libs directory."