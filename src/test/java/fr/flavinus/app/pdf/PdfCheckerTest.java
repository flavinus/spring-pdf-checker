package fr.flavinus.app.pdf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import java.io.ByteArrayInputStream;
import org.junit.Test;
import fr.flavinus.pdf.PdfChecker;

public class PdfCheckerTest {
    
    @Test
    public void testlineMatchPdfHeader() {

        assertFalse(PdfChecker.lineMatchPdfHeader(""));
        assertFalse(PdfChecker.lineMatchPdfHeader("blablabla"));
        assertFalse(PdfChecker.lineMatchPdfHeader("%PDF-20.0% fake text document"));

        assertTrue(PdfChecker.lineMatchPdfHeader("%PDF-1.4% fake text document"));
    }

    @Test
    public void testlineMatchEof() {

        assertFalse(PdfChecker.lineMatchEof(""));
        assertFalse(PdfChecker.lineMatchEof("blablabla"));
        assertFalse(PdfChecker.lineMatchEof("blabla %%EOFblabla"));
        assertFalse(PdfChecker.lineMatchEof("blabla %%EOF"));
        assertFalse(PdfChecker.lineMatchEof("F%%EOF"));

        assertTrue(PdfChecker.lineMatchEof("%%EOF"));
        assertTrue(PdfChecker.lineMatchEof("%%EOF%%EOF"));
        assertTrue(PdfChecker.lineMatchEof("%%EOFblabla"));
    }

    @Test
    public void testPdfCheckerRun() {
        pdfCheckerExecute("not a pdf", -1);
        pdfCheckerExecute("%PDF-1.4% \nincomplete content", -1);
        pdfCheckerExecute("%PDF-1.4% \ncontent\n%%EOF", 19);        
    }

    private void pdfCheckerExecute(String input, long offset) {
        assertEquals(offset, PdfChecker.execute(new ByteArrayInputStream(input.getBytes())));
    }
}
