package edu.sxccal.stegano.utilities;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.File;

import edu.sxccal.stegano.Stegano;

/**
 * Decryption module
 * @author Sayantan Majumdar
 */

public class DoDecrypt {
    public DoDecrypt(String skey, String keyfile, String imgfile) throws Exception {
        decrypt(skey,keyfile,imgfile);
    }
    private String extractChars(int nrows, int ncols, char mat[][], boolean flag[][]) {
        String s="";
        for(int i=0;i<nrows ;++i)
            for(int j=0;j<ncols;++j)
                if(flag[i][j])
                    s+=mat[i][j];
        return s;
    }
    private void initMatrix(String s, int nrows, int ncols, int num[], char mat[][], boolean flag[][]) throws IOException {
        try {
            int k=0;
            for(int i=0;i<ncols;++i)
                for(int j=0;j<nrows;++j)
                    if(flag[j][num[i]])
                        mat[j][num[i]]=s.charAt(k++);
        }
        catch(StringIndexOutOfBoundsException e) {
            throw new IOException("Invalid Secret Key or input files");
        }
    }
    private void initKeyArray(String keyfile, int num[]) throws IOException {
        DataInputStream key=new DataInputStream(new FileInputStream(keyfile));
        int i=0;
        boolean eof=false;
        while(!eof) {
            try {
                num[i++]=key.readInt();
            }
            catch(EOFException e) {
                eof=true;
                key.close();
            }
            catch(ArrayIndexOutOfBoundsException e1) {
                throw new IOException("Invalid Secret Key or input files");
            }
        }
    }

    private void decrypt(String skey,String keyfile, String imgfile) throws Exception {
        String bits=new DoStegano(imgfile).getDecodedString();
        GenKey gk=new GenKey(skey);
        int ncols=gk.getColumnSize(),num[]=new int[ncols], len=bits.length(),nrows=len/ncols, ndecrypt=gk.getEncryptionNumber();
        initKeyArray(keyfile, num);
        if(len>nrows*ncols)
            nrows++;
        boolean flag[][]=new boolean[nrows][ncols];
        DoEncrypt.initMatrix(bits, nrows, ncols, flag);
        char mat[][]=new char[nrows][ncols];
        for(int i=0;i<ndecrypt;++i) {
            initMatrix(bits, nrows, ncols, num, mat, flag);
            bits=extractChars(nrows, ncols, mat, flag);
        }
        String dt=FileOperations.bitsToAscii(bits);
        File dir=new File(Stegano.filePath+"/Decrypted");
        if(!dir.exists())
            dir.mkdir();
        FileOperations.writeToFile(dt, Stegano.filePath+"/Decrypted/decrypted.txt");
    }
}