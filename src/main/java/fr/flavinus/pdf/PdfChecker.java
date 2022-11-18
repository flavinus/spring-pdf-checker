package fr.flavinus.pdf;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/** PdfChecker is used to find the offset of the last valid EOF of document ("%%EOF")
 * 
 * - A valid EOF it positionned at the begining of a line
 * - If the document doesn't start with PDF header we retun -1 (starts with "%PDF-1.4")
 *
 * Notes:
 * 
 * This package contains the buisness logic and should be split to an external library
 * It is independant and must not contain nothing in relation with spring framework
 *
 * At the end of execution the offset count may differ from one with the file size received from controller
 * This probably depends on the last line if it ends with a \n
 * 
 * TODO:
 * - learn more about PDF charsets
 * - verify if we may have issues in offset count with windows EOL (\r\n)
 */
public class PdfChecker {

    public static final Charset usedCharset = StandardCharsets.US_ASCII;

    private static byte[] header_bytes = "%PDF".getBytes(PdfChecker.usedCharset);
    private static byte[] eof_bytes = "\n%%EOF".getBytes(PdfChecker.usedCharset);

    private static int bufferSize = 8; // arbitrary size (enough for our requirements)

    // Attention cas des document trop courts
    public static long run(InputStream input, long fileSize) throws Exception {
        // Check header
        byte[] buffer = new byte[bufferSize];
        int read = input.read(buffer);
        if(read == -1 || indexOf(buffer, header_bytes) != 0) {
            return -1;
        }

        // Fast forward! we jump directly to the end of file
        input.skip(fileSize - 2 * bufferSize);

        // Check footer
        read = input.read(buffer);
        if(read != -1) {
            int index = indexOf(buffer, eof_bytes);

            // we have a diff of 1, probably related to the leading \n .... whyyyyy "+1" !?
            if(index != -1) return fileSize - bufferSize + index + 1;
        }
        return -1;
    }

    // Found in internet (todo, review it)
    public static int indexOf(byte[] outerArray, byte[] smallerArray) {
        for(int i = 0; i <= outerArray.length - smallerArray.length; i++) {
            boolean found = true;
            for(int j = 0; j < smallerArray.length; j++) {
               if (outerArray[i+j] != smallerArray[j]) {
                   found = false;
                   break;
               }
            }
            if (found) return i;
         }
       return -1;  
    }
}
