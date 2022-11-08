package fr.flavinus.pdf;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

/** PdfChecker is used to find the offset of the last valid EOF of document ("%%EOF")
 * 
 * - A valid EOF it positionned at the begining of a line
 * - If the document doesn't start with PDF header we retun -1 (starts with "%PDF-1.4")
 *
 * Notes:
 * 
 * 1 : This package contains the buisness logic and should be split to an external library
 * It is independant and must not contain nothing in relation with spring framework
 * 
 * 2 : PdfChecker instances are not re-usable objects
 * This is why the constructor is private and we provide the "execute" static method
 * 
 * 3 : At the end of execution the offset count may differ from one with the file size received from controller
 * This probably depends on the last line if it ends with a \n
 * 
 * TODO:
 * - learn more about PDF charsets
 * - see if usage of regex is a problem for performances
 * - verify if we may have issues in offset count with windows EOL (\r\n)
 */
public class PdfChecker {

    private static final Charset usedCharset = StandardCharsets.US_ASCII;
    private static final Pattern PDF_HEADER_PATTERN = Pattern.compile("^%PDF-\\d\\.\\d.*");
    private static final Pattern EOF_PATTERN = Pattern.compile("^%%EOF.*");   

    private long currentLineOffset = 0;
    private long lastEofOffset = -1;
    
    private PdfChecker() {}

    private long run(InputStream input) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input, usedCharset));
        String line;
        while ((line = reader.readLine()) != null) {                        
            if(currentLineOffset == 0 && !lineMatchPdfHeader(line)) {
                return -1;
            } else if(lineMatchEof(line)) {            
                lastEofOffset = currentLineOffset;
            }
            currentLineOffset += line.getBytes(usedCharset).length + 1;
        }
        return lastEofOffset;
    }

    public static boolean lineMatchPdfHeader(String line) {
        return PDF_HEADER_PATTERN.matcher(line).matches();
    }

    public static boolean lineMatchEof(String line) {
        return EOF_PATTERN.matcher(line).matches();
    }

    public static long execute(InputStream stream) {        
        try {
            return new PdfChecker().run(stream);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }        
    }
}
