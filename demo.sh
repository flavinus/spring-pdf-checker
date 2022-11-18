#!/bin/bash

echo "\n\n#################################\n# Start server & ait for server to be UP"
mvn spring-boot:run & sleep 5

echo "\n\n#################################\n# Check service"
curl http://localhost:8080/actuator/health -v

# Test files
echo "\n\n#################################\n# Test files"
for f in samples/*
do
    echo "\n# Check file $f"
    curl http://localhost:8080/pdf/checkFile -F file=@$f
done


echo "\n\n#################################\n# Stop server"
curl -XPOST http://localhost:8080/actuator/shutdown -v

echo "\n\n"


# Correction of file using offset + 5 (length of %%EOF) doesn't look seems to work
#split -b 19160 samples/test_3.pdf tmpfile
