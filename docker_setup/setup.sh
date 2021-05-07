#!/bin/bash
mkdir -p api/target

## DB setup
docker-compose up -d --build minesweeper-mysql

## API setup
cd ../backend
cp ./Dockerfile ../docker_setup/api/

mvn clean install
cp ./target/minesweeper-api-0.0.1-SNAPSHOT.jar ../docker_setup/api/target

cd ../docker_setup

docker-compose up -d --build minesweeper-api

