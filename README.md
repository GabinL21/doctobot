[![SonarCloud](https://sonarcloud.io/images/project_badges/sonarcloud-orange.svg)](https://sonarcloud.io/summary/new_code?id=GabinL21_doctobot)

# Doctobot

## Introduction

Doctobot is a [Doctolib](https://doctolib.com) scraper and notifier, to help you find faster the appointment you're
seeking!

## Architecture

<img width="1380" alt="Doctobot Architecture" src="https://github.com/GabinL21/doctobot/assets/67428953/50d7426e-d09c-4389-86d8-99b386ce99d6">

## Configuration

### Doctolib URLs

You can enter a list of Doctolib appointment URLs to watch in the [feeder properties](https://github.com/GabinL21/doctobot/blob/main/feeder/src/main/resources/application.yml).  
URLs must end in "/booking/availabilities", on the page where available appointments should show up.

### Emails

You can set up your email address(es) to receive your appointment notifications in the [feeder properties](https://github.com/GabinL21/doctobot/blob/main/feeder/src/main/resources/application.yml).

You also need to configure an email address and the SMTP to send the notifications in the [notifier properties](https://github.com/GabinL21/doctobot/blob/main/notifier/src/main/resources/application.yml).  
If you're using Gmail, the SMTP is already configured, you only need to enter your email address and your [app password](https://support.google.com/mail/answer/185833).

## How to Run

Doctobot is based on a microservice architecture.  
Everything is containerized, only [Docker](https://www.docker.com) is required to run it!

### Build Docker Images

```bash
./mvnw clean install jib:dockerBuild
```

### Create & Run Docker Containers

```bash
docker compose up -d
```
