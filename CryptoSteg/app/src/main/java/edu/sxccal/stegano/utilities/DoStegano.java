package edu.sxccal.stegano.utilities;

import java.io.IOException;

import edu.sxccal.stegano.Stegano;
import edu.sxccal.stegano.utilities.f5.Embed;
import edu.sxccal.stegano.utilities.f5.Extract;

/**
 * Steganography module
 * @author Sayantan Majumdar
 */

public class DoStegano {
    private String decoded_str="";

    /**
     * Call this constructor for encoding
     * @param cipher Cipher string
     * @param imgfile Path to input image
     * @throws Exception
     */
    public DoStegano(String cipher, String imgfile) throws Exception {
        encode(cipher, imgfile);
    }

    /**
     * Call this constructor for decoding
     * @param imgfile Path to steg image
     * @throws IOException
     */
    public DoStegano(String imgfile) throws IOException {
        decoded_str=decode(imgfile);
    }

    /**
     * Get decoded string
     * @return decoded string obtained from steg image
     */
    public String getDecodedString() {
        return decoded_str;
    }
    private void encode(String cipher, String imgfile) throws Exception {
         new Embed(imgfile,Stegano.filePath+"/steg.jpg",cipher);
    }
    private String decode(String imgfile) throws IOException {
        new Extract(imgfile);
        String cipherContents=FileOperations.readFile(Stegano.filePath+"/cipher.txt");
        FileOperations.deleteFile(Stegano.filePath+"/cipher.txt");
        return FileOperations.stringToBits(cipherContents);
    }
}