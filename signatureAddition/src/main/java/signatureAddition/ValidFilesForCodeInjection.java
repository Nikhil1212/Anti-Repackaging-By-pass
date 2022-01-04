package signatureAddition;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;

public class ValidFilesForCodeInjection {
	static HashSet <String> libraryList;
	static {
		libraryList = new HashSet<String>();
		String listOfLibrariesPath="/home/nikhil/Documents/apps/TextFiles/LibraryList.txt";
		File file=new File(listOfLibrariesPath);
	      Scanner scanner = null;
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      while(scanner.hasNext())
	      {
	    	  libraryList.add(scanner.next());
	      }
	    
	}

	public static boolean fileCheck(String fileNameAbsolutePath) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		//String fileNameAbsolutePath="/home/nikhil/Documents/apps/com.mbanking.aprb.aprb/smali/a2.smali";
	//	String fileNameAbsolutePath="/home/nikhil/Documents/apps/com.mbanking.aprb.aprb/smali/com/google/android/gms/common/h.smali";

		char chArr[]=fileNameAbsolutePath.toCharArray();
		for(int i=0;i<fileNameAbsolutePath.length();i++)
		{
			/**
			 * Reasons for converting / to . is our library files are present in the form of . e.g com.google, etc.
			 */
			if(chArr[i]=='/')
				chArr[i]='.';
		}
		String string=new String(chArr);
		System.out.println(string);
		  int flag=0;
		 Iterator<String> i = libraryList.iterator();
		 int count=0;
	        while (i.hasNext())
	        {
	        	//count++;
	        	String library=i.next();
	        	//System.out.println(library);
	        	if(string.contains(library))
	        	{
	        		flag=1;
	        		break;
	        	}
	        	
	        }
	  //      System.out.println("Value of the count is : "+count);
	        if(flag==0)
	        	return true;
	        return false;
	            
		/*if(string.contains("com.google"))
			System.out.println("Ignore this file for insertion");
		else
			System.out.println("File needs to be considered for the modification");
		System.out.println("The new string is :"+string);*/
		
	}

	public static void codeInjection(HashSet<String> validFiles) {
		// TODO Auto-generated method stub
		
	}

}
