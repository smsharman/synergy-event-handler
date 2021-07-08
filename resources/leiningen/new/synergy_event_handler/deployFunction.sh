#!/bin/bash
aws lambda create-function \
    --region eu-west-1 \
    --function-name synergy-handler-{{name}} \
    --zip-file fileb://$(pwd)/../target/synergy-handler-{{name}}.jar \
    --role arn:aws:iam::979590819078:role/syn-evt-lambda \
    --handler {{name}}.core.Route \
    --runtime java11 \
    --timeout 15 \
    --memory-size 1024 \
    --cli-connect-timeout 6000