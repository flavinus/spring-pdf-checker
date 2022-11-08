package fr.flavinus.app.service;

import java.io.InputStream;
import org.springframework.stereotype.Service;

import fr.flavinus.app.payload.PdfCheckerPayload;
import fr.flavinus.pdf.PdfChecker;

@Service
public class PdfCheckerService {   
    public PdfCheckerPayload checkFile(InputStream stream, String fileName, String fileType, long fileSize) {
        try {
            return new PdfCheckerPayload(PdfChecker.execute(stream));
        } catch (Exception ex) {
            return new PdfCheckerPayload(-1);
        } finally {
            try {
                if(stream != null) stream.close();
            } catch (Throwable ignore) {}
        }
    }
}
