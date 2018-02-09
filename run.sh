#!/usr/bin/env bash
java -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=8000,suspend=n \
   -jar backend/target/backend-0.0.1-SNAPSHOT.jar
#-Xdebug -Xrunjdwp:transport=dt_socket,address=5005,server=y,suspend=y