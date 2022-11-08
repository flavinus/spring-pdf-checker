#!/bin/bash

for f in samples/*
do
    echo "\n\n# Check file $f"
    curl http://localhost:8080/pdf/checkFile -F file=@$f
done
echo "\n\n"
