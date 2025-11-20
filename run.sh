#!/bin/sh

echo "Loading environment variables from .env"
set -o allexport
source .env

JAVA_OPTS="-server -Xmx2048m -Xms512m" sbt service/run