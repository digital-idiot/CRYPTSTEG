package stegano;

import java.io.IOException;

import org.apache.pdfbox.ExtractText;

import stegano.utilities.FileOperations;
import stegano.utilities.Log;

/**
 * Takes plain text file path as input and uses 
 * <p>
 * {@link stegano.Encrypt} to generate out.zip which consists of a key file and the modified image
 * @author Sayantan Majumdar
 * @since 1.0
 */
public class UserInput {

	/**
	 * Main module 
	 * @param args Input arguments where
	 * <p>
	 * args[0]->secret key
	 * args[1]->Input file
	 * args[2]->Input image
	 * args[3]->Output directory
	 * </p>
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException	{
		Process p=null;
		try	{
			if(args.length<4 || args[0].isEmpty() || args[1].isEmpty() || args[2].isEmpty() || args[3].isEmpty())
				throw new IOException("Invalid Input"); 
			else if(args[0].length()>16 )
				throw new IOException("Secret key length must be between 1-16");
			String[] x={"zenity","--progress","--pulsate","--no-cancel","--text=Encrypting..."};
			p=new ProcessBuilder(x).start();
			if(FileOperations.is_pdf(args[1])) {	    	  
		    	  String[] s={args[1], args[3]+"/out.txt"};
		    	  ExtractText.main(s); //extract text from pdf file
		    	  args[1]=args[3]+"/out.txt";
		    }
			new Encrypt(args[0],args[1],args[2],args[3]);
			p.destroy();
			String x1[]={"zenity","--info","--title=Result","--text=Done!"};
			p=new ProcessBuilder(x1).start();
			p.waitFor();
		}
		catch(Exception e) {
			if(p!=null)
				p.destroy();
			String s=Log.createLog(args[3], e), x[]={"zenity","--error","--text="+s};
			p=new ProcessBuilder(x).start();
			p.waitFor();
		}
	}
}
