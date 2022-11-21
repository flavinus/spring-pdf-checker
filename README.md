
# Spring PDF Checker

This project provides a small spring boot application which exposes webservices allowing to check validity of a PDF document by returning the offset of the last %%EOF in document.
 * We check for validity of PDF header before searching EOF
 * The offset is searched only at the very end of document

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

## Resources

- PDF file format:
    https://resources.infosecinstitute.com/topic/pdf-file-format-basic-structure/


