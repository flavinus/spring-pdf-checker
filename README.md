
# Spring PDF Checker

This project provides a small spring boot application which exposes webservices allowing to get the offset of the last %%EOF of a PDF document.

## Build & test

`mvn package`

## Run

`mvn spring-boot:run`

## Usage 

Authentication is disabled on services.

You can run `demo.sh` for a quick demo.

### Submit a file
`curl http://localhost:8080/pdf/checkFile -F file=@my-file.pdf -v`

### Submit several files
`curl http://localhost:8080/pdf/checkFiles -F files=@my-file1.pdf -F files=@my-file2.pdf -v`

### Health checks
`curl http://localhost:8080/actuator/health -v`

### Stop server properly
`curl -XPOST http://localhost:8080/actuator/shutdown -v`


## TODO

les fichiers 3 a 8 ne puvent etre ouverts avec editeur PDF
=> tenter de corriger les fichiers en utilisant l'offset retourné pour tronquer les fichiers


## Resources

- PDF file format:
    https://resources.infosecinstitute.com/topic/pdf-file-format-basic-structure/


