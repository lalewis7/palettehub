#!/bin/bash
MY_PATH="$(dirname -- "${BASH_SOURCE[0]}")"
docker-compose -f $MY_PATH/../docker-compose.yaml -f $MY_PATH/../docker-compose.dev.yaml up -d