#!/bin/bash

for f in samples/*
do
    echo "\n\n# Check file $f"
    curl http://localhost:8080/pdf/checkFile -F file=@$f
done
echo "\n\n"

# Correction of file using offset + 5 (length of %%EOF) doesn't look seems to work
#split -b 19160 samples/test_3.pdf tmpfile
