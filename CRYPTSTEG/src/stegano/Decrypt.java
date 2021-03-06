package stegano;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.DataInputStream;

import stegano.utilities.GenKey;
import stegano.utilities.Log;
import stegano.utilities.DoStegano;
import stegano.utilities.FileOperations;

/**
 * Main Decryption module
 * @author Sayantan Majumdar
 * @since 1.0
 */
public class Decrypt {
	private static String extractChars(int nrows, int ncols, char mat[][], boolean flag[][]) {
		String s="";		
		for(int i=0;i<nrows ;++i)		
			for(int j=0;j<ncols;++j)	
				if(flag[i][j])
					s+=mat[i][j];		
		return s;
	}
	private static void initMatrix(String s, int nrows, int ncols, int num[], char mat[][], boolean flag[][]) throws IOException {
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
	private static void initKeyArray(String keyfile, int num[]) throws IOException {
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
	private static void decrypt(String skey,String imgfile, String keyfile, String dir) throws Exception {	
		String bits=new DoStegano(imgfile).getDecodedString();
		GenKey gk=new GenKey(skey);		
		int ncols=gk.getColumnSize(),num[]=new int[ncols], len=bits.length(),nrows=len/ncols, ndecrypt=gk.getEncryptionNumber();
		initKeyArray(keyfile, num);
		if(len>nrows*ncols)
			nrows++;		
		boolean flag[][]=new boolean[nrows][ncols];
		Encrypt.initMatrix(bits, nrows, ncols, flag);
		char mat[][]=new char[nrows][ncols];
		for(int i=0;i<ndecrypt;++i) {			
			initMatrix(bits, nrows, ncols, num, mat,flag);			
			bits=extractChars(nrows, ncols, mat, flag);		
		}		
		String dt=FileOperations.bitsToAscii(bits);		
		FileOutputStream dos=new FileOutputStream(dir+"/decrypted.txt");
		for(int i=0;i<dt.length();++i)
			dos.write(dt.charAt(i));
		dos.close();		
	}
	/**
	 * 
	 * @param args input arguments where
	 * <p>
	 * args[0]-> secret key
	 * args[1]-> image file
	 * args[2]-> random key file
	 * args[3]-> output directory
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public static void main(String[] args) throws InterruptedException, IOException	{
		Process p=null;
		try	{			
			if(args.length<4 || args[0].isEmpty() || args[1].isEmpty() || args[2].isEmpty() || args[3].isEmpty())
				throw new IOException("Invalid input");	
			else if(args[0].length()>16)
				throw new IOException("Secret key length must be between 1-16");	
			String[] x={"zenity","--progress","--pulsate","--no-cancel","--text=Decrypting..."};
			p=new ProcessBuilder(x).start();
			decrypt(args[0], args[1], args[2], args[3]);
			p.destroy();
			String x1[]={"zenity","--info","--title=Result","--text=Done!"};
			p=new ProcessBuilder(x1).start();
			p.waitFor();			
		}		
		catch(Exception e) {
			if(p!=null)
				p.destroy();
			String s=Log.createLog(args[3],e), x[]={"zenity","--error","--text="+s};
			p=new ProcessBuilder(x).start();
			p.waitFor();
		}
	}
}
