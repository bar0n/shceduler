#!/usr/bin/env bash
# heroku plugins:install heroku-cli-deploy
heroku deploy:jar backend/target/backend-0.0.1-SNAPSHOT.jar --app shceduler