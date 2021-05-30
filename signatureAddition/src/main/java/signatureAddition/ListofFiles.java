package signatureAddition;
//Currently, this code lists the files which needs to be considered whether toByteArray is present or not inside the disassembled code of the aprb app  
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;

public class ListofFiles {
	

	public static void codeInjectionProcess(String key, String absolutePathAppFolder) throws IOException, InterruptedException {
		
		
		Process process=Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", "grep -r -l 'Landroid/content/pm/Signature;->toByteArray' "+ absolutePathAppFolder});///home/nikhil/Documents/apps/com.mbanking.aprb.aprb"});
		
		process.waitFor();
		
		/*
		 * This above command is executed to fetch the file names which are calling signature->toByteArray 
		 * 
		 */
		
		printErrors(process);
		
		/**
		 * This is used to fetch the errors from the grep command, if any.
		 * 
		 */

		
		HashSet <String> validFiles =fetchValidFiles(process);
		
		/*
		 * It will store the files which needs to be considered for the code Injection
		 */
		
		Iterator<String> i = validFiles.iterator();
		    while (i.hasNext())
	        {
	        	//count++;
	        	String filePath=i.next();
	        	fetchingRegisterValue.codeInjection(filePath, key);
	        }
		//fileForCodeInjection.codeInjection(validFiles);

	}

	private static HashSet<String> fetchValidFiles(Process process) throws IOException, InterruptedException {

		/*
		 * This code will 
		 */
		HashSet <String> validFiles=new HashSet<String>();
		BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(process.getInputStream()));
		
		String line=bufferedReader.readLine();
		while(line!=null)
		{
			//System.out.println(line);
			if(fileForCodeInjection.fileCheck(line))
			{
				validFiles.add(line);
			}
			line=bufferedReader.readLine();
		}
		System.out.println("List of valid files :"+validFiles);
		return validFiles;
		
	}

	private static void printErrors(Process process) throws IOException {
		// TODO Auto-generated method stub
		BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(process.getErrorStream()));
		String line="";
		line=bufferedReader.readLine();
		while(line!=null)
		{
			//System.out.println(line);
			System.out.println(line);
			line=bufferedReader.readLine();
		}
	}

	

}
