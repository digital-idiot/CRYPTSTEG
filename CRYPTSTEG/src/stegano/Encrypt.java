package stegano;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.DataOutputStream;
import java.security.SecureRandom;

import stegano.utilities.*;

/**
 * Main Encryption module
 * @author Sayantan Majumdar 
 * @since 1.0
 */
public class Encrypt {
	private static int ncols;	
	Encrypt(String skey, String s, String imgfile, String dir) throws Exception	{
		encryptFile(skey,s,imgfile,dir);
	}
	private void initMatrix(String s, int nrows, char mat[][]) {
		int k=0;		
		for(int i=0;i<nrows;++i)
			for(int j=0;j<ncols;++j)
				if(k<s.length())
					mat[i][j]=s.charAt(k++);					
	}	
	
	/**
	 * Initializes a 2D boolean flag matrix
	 * @param s Input String 
	 * @param nrows row size
	 * @param ncols column size
	 * @param flag 2D boolean matrix
	 */
	public static void initMatrix(String s, int nrows, int ncols, boolean flag[][])	{
		int k=0;
		for(int i=0;i<nrows;++i) {
			for(int j=0;j<ncols;++j) {
				flag[i][j]=false;
				if(k++<s.length())
						flag[i][j]=true;
			}
		}
	}
	private void generateKey(String dir, int key_arr[]) throws IOException {		
		SecureRandom srand=new SecureRandom();		
		DataOutputStream k=new DataOutputStream(new FileOutputStream(dir+"/key.txt"));	
		boolean arr[]=new boolean[ncols];		
		for(int i=0;i<ncols;++i) {			
			int r=srand.nextInt(ncols);
			if(!arr[r]) {
				key_arr[i]=r;
				k.writeInt(r);				
			}			
			else
				i--;
			arr[r]=true;
		}	
		k.close();			
	}		
	private String generateCipher(int nrows, int key[], char mat[][], boolean flag[][]) {
		String cipher_text="";		
		for(int i=0;i<ncols;++i)
			for(int j=0;j<nrows;++j)
				if(flag[j][key[i]])
					cipher_text+=mat[j][key[i]];
		return cipher_text;
	}
	
	/**
	 * Performs the encryption operation
	 * @param s File containing the plain text
	 * @throws IOException	 
	 */
	private void encryptFile(String skey, String s, String imgfile, String dir) throws Exception {		
		GenKey gk=new GenKey(skey);
		ncols=gk.getColumnSize();
		int nencrypt=gk.getEncryptionNumber();
		String pt=FileOperations.readFile(s);		
		int len=pt.length()*8,nrows=len/ncols;
		if(len>(nrows*ncols))
			nrows++;		
		String binary_pt=FileOperations.stringToBits(pt);		
		boolean flag[][]=new boolean[nrows][ncols];
		initMatrix(binary_pt,nrows,ncols, flag);			
		int k[]=new int[ncols];
		String cipher=binary_pt;
		generateKey(dir,k);
		char mat[][]=new char[nrows][ncols];
		for(int i=0;i<nencrypt;++i)	{
			initMatrix(cipher, nrows, mat);	
			cipher=generateCipher(nrows,k,mat, flag);				
		}	
		cipher=FileOperations.bitsToAscii(cipher);
		FileOperations.writeToFile(cipher, dir+"/cipher.txt");
		new DoStegano(imgfile,dir);
		String img=dir+"/steg.jpg",keyfile=dir+"/key.txt";
		String files[]={img,keyfile};
		ZipCreator.createZip(dir+"/out.zip", files);
		FileOperations.deleteFile(img);
		FileOperations.deleteFile(keyfile);
		new QRCode(dir+"/out.zip", dir);
	}
}