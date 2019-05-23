#!/usr/bin/env sh

#Ensures that erlang OTP is initialized before java calls it
epmd -daemon

java -jar /usr/local/lemport/lemport-1.0.jar
