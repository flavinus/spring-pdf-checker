package fr.flavinus.app.pdf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

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
    public void testPdfCheckerRunString() {
        pdfCheckerTestString("not a pdf", -1);
        pdfCheckerTestString("%PDF-1.4% \nincomplete content", -1);
        pdfCheckerTestString("%PDF-1.4% \ncontent\n%%EOF", 19);        
    }

    private void pdfCheckerTestString(String input, long offset) {
        assertEquals(offset, PdfChecker.execute(new ByteArrayInputStream(input.getBytes())));
    }

    @Test
    public void testPdfCheckerRunFile() {

        pdfCheckerTestFile("samples/minimal-pdf-sample.pdf", 8422);
        pdfCheckerTestFile("README.md", -1);

        /* Waiting for commit confirmation
        pdfCheckerTestFile("samples/test_1.pdf", 4568);
        pdfCheckerTestFile("samples/test_2.pdf", 22195);
        pdfCheckerTestFile("samples/test_3.pdf", 19155);
        pdfCheckerTestFile("samples/test_4.pdf", 19155);
        pdfCheckerTestFile("samples/test_5.pdf", -1);
        pdfCheckerTestFile("samples/test_6.pdf", 18835);
        pdfCheckerTestFile("samples/test_7.pdf", 22145);
        pdfCheckerTestFile("samples/test_8.pdf", 22139);
        */
    }

    private void pdfCheckerTestFile(String filename, long offset) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(new File(filename));
        } catch (FileNotFoundException e) {}
        assertNotNull("Failed to load test file '" + filename + "'", inputStream);
        assertEquals("Failed pdfChecker execute on file '" + filename + "'", offset, PdfChecker.execute(inputStream));
    }
}
