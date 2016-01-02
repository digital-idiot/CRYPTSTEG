package edu.sxccal.stegano.utilities.f5;

import james.JpegEncoder;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Embed {
	Bitmap image = null;
	FileOutputStream dataOut = null;
    File file, outFile;
    JpegEncoder jpg;
    int i, Quality = 90;
    // Check to see if the input file name has one of the extensions:
    // .tif, .gif, .jpg
    // If not, print the standard use info.
    String comment = "JPEG Encoder Copyright 1998, James R. Weeks and BioElectroMech.  ";
    String inFileName = null;
    String outFileName = null;
    
    public Embed(String inFileName, String outFileName, String secret_message) throws Exception {
    	this.inFileName = inFileName;
    	this.file = new File(this.inFileName);
    	this.outFileName = outFileName;
    	this.outFile = new File(outFileName);
    	if(this.file.exists())
			dataOut = new FileOutputStream(outFile);
		image = BitmapFactory.decodeFile(this.inFileName);
		jpg = new JpegEncoder(image, Quality, dataOut, comment);
		jpg.Compress(new ByteArrayInputStream(secret_message.getBytes("ISO-8859-1")));
		dataOut.close();
    }
}
