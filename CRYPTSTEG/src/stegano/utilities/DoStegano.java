package stegano.utilities;

import java.io.IOException;

import main.*;
/**
 * Steganography module
 * @author Sayantan Majumdar
 * @since 1.0
 */

public class DoStegano {     
	private String decoded_str="";
	/**
	 * Call this constructor to encode data inside a JPEG image
	 * @param imgfile Path to the image
	 * @param dir Output directory
	 * @throws IOException
	 */
	public DoStegano(String imgfile, String dir) throws IOException	{     
		  encode(imgfile, dir);		  
	}
	/**
	 * Call this constructor to decode a steg image
	 * @param imgfile Path to the image
	 * @throws IOException
	 */
	public DoStegano(String imgfile) throws IOException {
		decoded_str=decode(imgfile);
	}
	/**
	 * Get the decoded string
	 * @return String 
	 */
	public String getDecodedString() {
		return decoded_str;
	}		
	private void encode(String imgfile, String dir) throws IOException {
		String s[]={"-e",dir+"/cipher.txt",imgfile,dir+"/steg.jpg"};
		Embed.main(s);
		FileOperations.deleteFile(dir+"/cipher.txt");
	}
	private String decode(String imgfile) throws IOException {
		String s[]={"-e","cipher.txt",imgfile};
		Extract.main(s);
		String cipherContents=FileOperations.readFile("cipher.txt");
		FileOperations.deleteFile("cipher.txt");
		return FileOperations.stringToBits(cipherContents);	  
	}	
}