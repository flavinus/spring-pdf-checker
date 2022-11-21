package fr.flavinus.app.service;

import java.io.InputStream;
import org.springframework.stereotype.Service;
import fr.flavinus.app.payload.PdfCheckerPayload;
import fr.flavinus.pdf.PdfChecker;

@Service
public class PdfCheckerService {   
    public PdfCheckerPayload checkFile(InputStream stream, long fileSize) {
        try {
            return new PdfCheckerPayload(PdfChecker.run(stream, fileSize));
        } catch (Exception e) {
            e.printStackTrace();
            return new PdfCheckerPayload(-1);
        }
        
    }
}
