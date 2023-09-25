#!/bin/bash

# move into palettehub dir
cd /home/palettehub/palettehub

# remove all dangling images
docker image prune -f

# pull changes from github
git pull

# shutdown to save resources
docker compose -f docker-compose.yaml -f docker-compose.prod.yaml down

# build and deploy
docker compose -f docker-compose.yaml -f docker-compose.prod.yaml up --build -d