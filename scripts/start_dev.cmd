@ECHO OFF

TITLE Palette Hub Development Environment

docker-compose -f %~dp0..\docker-compose.yaml -f %~dp0..\docker-compose.dev.yaml up -d