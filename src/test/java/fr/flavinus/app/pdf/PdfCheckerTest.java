package fr.flavinus.app.pdf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.junit.Test;
import fr.flavinus.pdf.PdfChecker;

public class PdfCheckerTest {

    @Test
    public void testIndexOf() {
        byte[] outer = {1, 2, 3, 4};
        assertEquals(0, PdfChecker.indexOf(outer, new byte[]{1, 2}));
        assertEquals(1, PdfChecker.indexOf(outer, new byte[]{2, 3}));
        assertEquals(2, PdfChecker.indexOf(outer, new byte[]{3, 4}));
        assertEquals(0, PdfChecker.indexOf(outer, new byte[]{1, 2, 3, 4}));
        assertEquals(-1, PdfChecker.indexOf(outer, new byte[]{4, 4}));
        assertEquals(-1, PdfChecker.indexOf(outer, new byte[]{4, 5}));
        assertEquals(-1, PdfChecker.indexOf(outer, new byte[]{4, 5, 6, 7, 8}));
    }

    @Test
    public void testPdfBytesCheckerRunString() {
        pdfBytesCheckerTestString("not a pdf", -1);
        pdfBytesCheckerTestString("%PDF-1.4% \nincomplete content", -1);
        pdfBytesCheckerTestString("%PDF-1.4% \ncontent\n%%EOF", 19);        
    }

    private void pdfBytesCheckerTestString(String input, long offset) {
        byte[] bytes = input.getBytes();
        try {
            assertEquals(offset, PdfChecker.run(new ByteArrayInputStream(bytes), bytes.length));
        } catch (Exception e) {
            assertNull(e);
        }
    }

    @Test
    public void testPdfBytesCheckerRunFile() {

        pdfBytesCheckerTestFile("samples/minimal-pdf-sample.pdf", 8422);
        pdfBytesCheckerTestFile("README.md", -1);

        pdfBytesCheckerTestFile("samples/test_1.pdf", 4568);
        pdfBytesCheckerTestFile("samples/test_2.pdf", 22195);
        pdfBytesCheckerTestFile("samples/test_3.pdf", -1); // 19155
        pdfBytesCheckerTestFile("samples/test_4.pdf", -1); // 19155
        pdfBytesCheckerTestFile("samples/test_5.pdf", -1);
        pdfBytesCheckerTestFile("samples/test_6.pdf", -1); // 18835
        pdfBytesCheckerTestFile("samples/test_7.pdf", -1); // 22145
        pdfBytesCheckerTestFile("samples/test_8.pdf", -1); // 22139
    }

    private void pdfBytesCheckerTestFile(String filename, long offset) {
        InputStream inputStream = null;
        long fileSize = -1;
        try {
            File file = new File(filename);
            fileSize = file.length();
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {}
        assertNotNull("Failed to load test file '" + filename + "'", inputStream);

        try {
            assertEquals("Failed pdfBytesChecker execute on file '" + filename + "'", offset, PdfChecker.run(inputStream, fileSize));
        } catch (Exception e) {
            assertNull(e);
        }
    }
}
