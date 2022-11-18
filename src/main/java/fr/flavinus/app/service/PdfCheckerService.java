package fr.flavinus.app.service;

import java.io.InputStream;
import org.springframework.stereotype.Service;

import fr.flavinus.app.payload.PdfCheckerPayload;
import fr.flavinus.pdf.PdfChecker;

@Service
public class PdfCheckerService {   
    public PdfCheckerPayload checkFile(InputStream stream) {
        return new PdfCheckerPayload(PdfChecker.execute(stream));
    }
}
