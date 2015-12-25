package stegano.utilities;

import java.io.IOException;

import main.*;

public class DoStegano
{     
	private String decoded_str="";
	public DoStegano(String imgfile, String dir) throws IOException 
	{     
		  encode(imgfile, dir);		  
	}
	public DoStegano(String imgfile) throws IOException
	{
		decoded_str=decode(imgfile);
	}	
	public String getDecoded_str() 
	{
		return decoded_str;
	}		
	private void encode(String imgfile, String dir) throws IOException
	{
		String s[]={"-e",dir+"/cipher.txt",imgfile,dir+"/steg.jpg"};
		Embed.main(s);
		FileOperations.deleteFile(dir+"/cipher.txt");
	}
	private String decode(String imgfile) throws IOException
	{
		String s[]={"-e","cipher.txt",imgfile};
		Extract.main(s);
		String cipherContents=FileOperations.readFile("cipher.txt");
		FileOperations.deleteFile("cipher.txt");
		return FileOperations.stringToBits(cipherContents);	  
	}	
}