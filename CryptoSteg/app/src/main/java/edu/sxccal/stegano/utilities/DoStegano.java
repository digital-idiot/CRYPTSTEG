package edu.sxccal.stegano.utilities;

import java.io.IOException;

import edu.sxccal.stegano.Stegano;
import info.guardianproject.f5android.Embed;
import info.guardianproject.f5android.Extract;

public class DoStegano
{
    private String decoded_str="";
    public DoStegano(String cipher, String imgfile) throws IOException
    {
        encode(cipher, imgfile);
    }
    public DoStegano(String imgfile) throws IOException
    {
        decoded_str=decode(imgfile);
    }
    public String getDecoded_str()
    {
        return decoded_str;
    }
    private void encode(String cipher, String imgfile) throws IOException
    {
        new Embed(Stegano.filePath,imgfile,Stegano.filePath+"/steg.jpg",cipher);
    }
    private String decode(String imgfile) throws IOException
    {
        new Extract(imgfile);
        String cipherContents=FileOperations.readFile(Stegano.filePath+"/cipher.txt");
        FileOperations.deleteFile("cipher.txt");
        return FileOperations.stringToBits(cipherContents);
    }
}