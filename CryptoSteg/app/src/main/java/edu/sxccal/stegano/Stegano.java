package edu.sxccal.stegano;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import edu.sxccal.stegano.utilities.DecodeQR;
import edu.sxccal.stegano.utilities.Log;
import edu.sxccal.stegano.utilities.Unzip;


/**
 * This is the main activity
 * @author Sayantan Majumdar
 * @since 1.0
 */
public class Stegano extends Activity implements OnClickListener {
	/**
	 * @param filePath External storage directory path
	 */
	private Button scanBtn,encr,decr,ab,dqr;
	private static String scanContent="No result";
	public static final String filePath=Environment.getExternalStorageDirectory().getAbsolutePath()+"/Stegano";
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        //load the main activity layout
        setContentView(R.layout.activity_stegano);
        //Create directory 
        File dir=new File(Stegano.filePath);
        if(!dir.exists())
        	dir.mkdir();        
        //Check which button is pressed
        scanBtn = (Button)findViewById(R.id.scan_button);
		decr=(Button)findViewById(R.id.decrypt);
        ab=(Button)findViewById(R.id.ab);
        encr=(Button)findViewById(R.id.encrypt);
        dqr=(Button)findViewById(R.id.decode);
        ab.setOnClickListener(this);    
        decr.setOnClickListener(this);
        scanBtn.setOnClickListener(this);
		encr.setOnClickListener(this);
        dqr.setOnClickListener(this);
    }

	@Override
	public void onClick(View v)	{
		if(v.getId()==R.id.scan_button)	{
			IntentIntegrator scanner = new IntentIntegrator(this); //Zxing android interface library
			scanner.initiateScan(); //Requires BarcodeScanner app by Zxing to be installed in the phone			
		}
		if(v.getId()==R.id.ab) {
			Intent about=new Intent(Stegano.this,About.class);
			startActivity(about);
		}
		if(v.getId()==R.id.decrypt) {
			Intent decrypt= new Intent(Stegano.this,Decrypt.class);
        	startActivity(decrypt);
		}
		if(v.getId()==R.id.encrypt) {
			Intent encrypt=new Intent(Stegano.this,Encrypt.class);
			startActivity(encrypt);
		}
		if(v.getId()==R.id.decode) {
			Intent qr=new Intent(Stegano.this,DecodeQR.class);
			startActivity(qr);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if(intent!=null) {
			IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);		
			if (scanningResult != null) {
				scanContent = scanningResult.getContents();		
				if(checkExternalMedia())
					writeToFile();
			}
		}
		else {
		    Toast toast = Toast.makeText(getApplicationContext(),"No scan data received!", Toast.LENGTH_SHORT);
		    toast.setGravity(Gravity.CENTER,0,0);
		    toast.show();		    
		}	
	}

	/**
	 * checks if there is read and write access to device storage
	 * @return true if media has both RW access false otherwise
	 */
	private boolean checkExternalMedia()	{
		    boolean readable;
		    boolean writeable;
		    String state = Environment.getExternalStorageState();
		    if (Environment.MEDIA_MOUNTED.equals(state))
		        readable = writeable = true;
		    else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
		        readable = true;
		        writeable = false;
		    }
		    else
		        readable = writeable = false;
		    return (readable && writeable);
	}		
	private void writeToFile() {
		    File dir = new File (filePath),file;
			file = new File(dir, "decoded.zip");
		    try {
		        FileOutputStream fos = new FileOutputStream(file);
				fos.write(scanContent.getBytes("ISO-8859-1"));
		        fos.close();
				Unzip.unzip(file.getAbsolutePath(), Stegano.filePath);
				file.delete();
		    } 
		    catch(IOException e) {
		    	Log.create_log(e, getApplicationContext()); //Write logs to log.txt
		    }
			Toast toast = Toast.makeText(getApplicationContext(),"Zip file extracted to: "+filePath, Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER,0,0);
			toast.show();
	}	
}