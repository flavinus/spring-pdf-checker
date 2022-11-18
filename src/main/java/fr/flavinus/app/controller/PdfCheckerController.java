package fr.flavinus.app.controller;

import fr.flavinus.app.payload.PdfCheckerPayload;
import fr.flavinus.app.service.PdfCheckerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// TODO: see how the content type works (read from request or verified by framework) is it possible to exploit this ?
// TODO: file size limitations configured in application.properties but doesn't looks to be effective

@RestController
public class PdfCheckerController {

    @Autowired
    PdfCheckerService service;

    @PostMapping("/pdf/checkFile")
    public PdfCheckerPayload checkFile(@RequestParam("file") MultipartFile file) {
        InputStream stream = null;
        try {
            return service.checkFile(stream = file.getInputStream());
        } catch (IOException ex) {
            // TODO: fails for every files if called from "/pdf/checkFiles"
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "File read failure", ex);
        } finally {
            try {
                if(stream != null) stream.close();
            } catch (Throwable ignore) {}
        }
    }

    @PostMapping("/pdf/checkFiles")
    public List<PdfCheckerPayload> submitFiles(@RequestParam("files") MultipartFile[] files) {
        return Arrays.asList(files)
            .stream()
            .map(file -> checkFile(file))
            .collect(Collectors.toList());
    }
}