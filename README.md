[![SonarCloud](https://sonarcloud.io/images/project_badges/sonarcloud-orange.svg)](https://sonarcloud.io/summary/new_code?id=GabinL21_doctobot)

# Doctobot

## Introduction

Doctobot is a [Doctolib](https://doctolib.com) scraper and notifier, to help you find faster the appointment you're
seeking.

## Architecture

<img width="1380" alt="Doctobot Architecture" src="https://github.com/GabinL21/doctobot/assets/67428953/50d7426e-d09c-4389-86d8-99b386ce99d6">

## How to Run

Doctobot is based on a microservice architecture.  
Only [Docker](https://www.docker.com) is required to run it!

### Build Docker Images

```bash
./mvnw clean install jib:dockerBuild
```

### Create & Run Docker Containers

```bash
docker compose up -d
```
