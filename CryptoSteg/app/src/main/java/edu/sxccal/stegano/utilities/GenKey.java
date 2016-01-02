package edu.sxccal.stegano.utilities;

/**
 * Generate encryption and column numbers
 * @author Sayantan Majumdar
 */
public class GenKey {
	private int ncols, nencrypt;
	/**
	 *  
	 * @param s Input secret key string
	 */
	public GenKey(String s)	{
		genKey(s);
	}
	/**
	 * 
	 * @return number of columns
	 */
	public int getColumnSize()
	{
		return ncols;
	}
	/**
	 * 
	 * @return number of times the encryption algorithm will have to done
	 */
	public int getEncryptionNumber()
	{
		return nencrypt;
	}
	private int calculateNumbers(String s) {
		int sum=0;
		for(int i=0;i<s.length();++i)
			sum+=(s.charAt(i)-48)*(i+1);
		return sum;
	}
	private void genKey(String skey) {
		int j=17,mat[]=new int[17];
		for(int i=1;i<17;++i)
			mat[i]=j--;
		int base=mat[skey.length()];
		long sum=0;
		for(int i=0;i<skey.length();++i)
			sum+=(int)skey.charAt(i)*Math.pow(base, i+1);
		String s=""+sum;		
		int val=calculateNumbers(s);
		ncols=(int)(sum%val);		
		if(ncols==0)
			ncols=val;
		else if(ncols>256)
			ncols=256;
		s=new StringBuilder(s).reverse().toString();
		val=calculateNumbers(s);
		nencrypt=(int)(sum%val);		
		if(nencrypt==0)
			nencrypt=val;	
		else if(nencrypt>64)
			nencrypt=64;
	}	
}
