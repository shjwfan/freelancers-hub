#!/bin/bash

cd web/src/main/resources/webapp || exit 1

if yarn lint; then
  echo "Linting completed"
else
  echo "Lint failure"
  exit 1
fi

if yarn build; then
  echo "Build completed"
else
  echo "Build failure"
  exit 1
fi

cd ../../../../..

if ./mvnw clean package; then
  echo "Package completed"
else
  echo "Package failure"
  exit 1
fi
