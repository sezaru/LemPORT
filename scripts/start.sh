#!/usr/bin/env sh

#Ensures that erlang OTP is initialized before java calls it
erl -noshell -sname ensure_initialized&

# Avoids race condition issues
sleep 2

java -jar /usr/local/lemport/lemport-1.0.jar
