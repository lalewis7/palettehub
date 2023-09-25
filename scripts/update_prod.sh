#!/bin/bash

# move into palettehub dir
cd /home/palettehub

# remove all dangling images
docker image prune -f

# pull changes from github
git pull

# build and deploy
docker compose -f docker-compose.yaml -f docker-compose.prod.yaml up --build -d