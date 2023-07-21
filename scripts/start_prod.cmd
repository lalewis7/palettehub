@ECHO OFF

TITLE Palette Hub Production Environment

docker-compose -f %~dp0..\docker-compose.yaml -f %~dp0..\docker-compose.prod.yaml up -d