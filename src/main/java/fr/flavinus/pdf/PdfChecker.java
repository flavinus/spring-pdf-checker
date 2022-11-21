package fr.flavinus.pdf;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/** PdfChecker is used to find the offset of the last valid EOF of document ("\n%%EOF")
 * If the document doesn't start with PDF header we retun -1 (ex: "%PDF-1.4")
 *
 * This package contains the buisness logic and should be split to an external library
 * It is independant and must not contain nothing in relation with spring framework
 */
public class PdfChecker {

    public static final Charset usedCharset = StandardCharsets.US_ASCII;
    private static byte[] header_bytes = "%PDF".getBytes(PdfChecker.usedCharset);
    private static byte[] eof_bytes = "\n%%EOF".getBytes(PdfChecker.usedCharset); //new byte[]{0xA,0x25, 0x25, 0x45, 0x4F, 0x46};
    private static int bufferSize = 8; // arbitrary size (enough for our requirements)

    public static long run(InputStream input, long fileSize) throws Exception {
        // Check PDF header
        byte[] buffer = new byte[bufferSize];
        int read = input.read(buffer);
        if(read == -1 || indexOf(buffer, header_bytes) != 0) {
            return -1;
        }

        // Fast forward! we jump directly to the end of file
        input.skip(fileSize - (2 * bufferSize));

        // Check PDF footer
        read = input.read(buffer);
        if(read != -1) {
            int index = indexOf(buffer, eof_bytes);

            // TODO: we have a diff of 1 !?            
            // This is not related to length of byte encoded "\n" cf unit test testLineFeedLength
            if(index != -1) return fileSize - bufferSize + index + 1;
        }
        return -1;
    }

    // Found on internet (todo: review it)
    public static int indexOf(byte[] subject, byte[] search) {
        for(int i = 0; i <= subject.length - search.length; i++) {
            boolean found = true;
            for(int j = 0; j < search.length; j++) {
               if (subject[i+j] != search[j]) {
                   found = false;
                   break;
               }
            }
            if (found) return i;
         }
       return -1;  
    }
}
