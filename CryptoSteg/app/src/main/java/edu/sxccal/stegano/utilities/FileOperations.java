package edu.sxccal.stegano.utilities;

import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by sayantan on 25/12/15.
 */
public class FileOperations {
         /**
         * Converts ASCII to 8-bit binary string
         * @param cipher String containing the cipher text
         * @return Binary string
         */
        public static String stringToBits(String cipher)
        {
            String bits="";
            for(int i=0;i<cipher.length();++i)
            {
                int mask=1<<7, n=cipher.charAt(i);
                while(mask!=0)
                {
                    bits+=(n&mask)!=0?1:0;
                    mask>>=1;
                }
            }
            return bits;
        }
        /**
         * Converts bits to ASCII
         * @param bits Input bits in String format
         * @return ASCII string
         */
        public static String bitsToAscii(String bits)
        {
            String ascii="";
            for(int i=0;i<bits.length();)
            {
                int asc=0,j,power=7;
                for(j=i;j<i+8 && j<bits.length();++j,--power)
                {
                    int c=bits.charAt(j)-48;
                    asc+=c*(int)Math.pow(2, power);
                }
                i=j;
                ascii+=(char)asc;
            }
            return ascii;
        }
        /**
         * Read file contents
         * @param s
         * @return
         * @throws IOException
         */
        public static String readFile(String s) throws IOException
        {
            FileInputStream fis=new FileInputStream(s);
            String data="";
            int c;
            while((c=fis.read())!=-1)
                data+=(char)c;
            fis.close();
            return data;
        }
        /**
         * Write to a file
         * @param out Output file
         * @param s String to be written
         * @throws IOException
         */
        public static void writeToFile(String s, String out) throws IOException
        {
            FileOutputStream fos=new FileOutputStream(out);
            for(int i=0;i<s.length();++i)
                fos.write(s.charAt(i));
            fos.close();
        }
        /**
         * Delete a file
         * @param f File to be deleted
         */
        public static void deleteFile(String f)
        {
            File file=new File(f);
            file.delete();
        }
        /**
         * Checks whether the input file is a pdf file
         * @param file Input file
         * @return true if pdf false otherwise
         */
        public static boolean is_pdf(String file)
        {
            String ext=file.substring(file.lastIndexOf('.')+1, file.length());
            if(ext.equalsIgnoreCase("pdf"))
                return true;
            return false;
        }
}

