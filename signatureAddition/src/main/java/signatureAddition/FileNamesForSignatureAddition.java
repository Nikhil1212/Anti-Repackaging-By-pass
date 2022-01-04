package signatureAddition;
//Currently, this code lists the files which needs to be considered whether toByteArray is present or not inside the disassembled code of the aprb app  
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class FileNamesForSignatureAddition {
	

	public static void codeInjectionProcess(String key, String absolutePathAppFolder) throws Exception {
		
		List<String> list  = new ArrayList<String>();
		list=listInitializationForSignaturePattern(list);
		for(int i=0;i<list.size();i++)
		{
			
			System.out.println(list.get(i));
			Process process=Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", "grep -r -l '"+list.get(i)+"' "+ absolutePathAppFolder});///home/nikhil/Documents/apps/com.mbanking.aprb.aprb"});
			
			process.waitFor();
			
			/*
			 * This above command is executed to fetch the file names which are calling signature->toPattern 
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
			
			Iterator<String> iterator = validFiles.iterator();
			    while (iterator.hasNext())
		        {
		        	//count++;
		        	String filePath=iterator.next();
		        	if(filePath.contains(".smali"))
		    				FinalCodeInjection.codeInjection(filePath, key,list.get(i));
		        	//FinalCodeInjection.codeInjection(filePath, key,patternToCharArray);
		        	//FinalCodeInjection.codeInjection(filePath, key,patternToHashCode);
		        }

		}
		/*String patternToByteArray="Landroid/content/pm/Signature;->toByteArray()";
		String patternToCharArray="Landroid/content/pm/Signature;->toCharArray()";
		String patternToHashCode="Landroid/content/pm/Signature;->hashCode()";*/
				//fileForCodeInjection.codeInjection(validFiles);

	}

	public static boolean isFileLib(String filePath) {
		// TODO Auto-generated method stub
		if(filePath==null)
			return false;
		if(filePath.contains(".so"))
			return true;
		return false;
					
		/*String fileName=filePath.contains(".so")));
		System.out.println(fileName);
		if(fileName.substring(0,3).equals("lib"))
			return true;
		return false;*/
	}

	public static List<String> listInitializationForSignaturePattern(List<String> list) {
		// TODO Auto-generated method stub
		list.add("Landroid/content/pm/Signature;->toByteArray()");
		list.add("Landroid/content/pm/Signature;->hashCode()");
		list.add("Landroid/content/pm/Signature;->getPublicKey()"); //Truely grateful for this statement as we went to the source code and analyse the various signature methods possible.
		list.add("Landroid/content/pm/Signature;->toCharsString()");
		list.add("Landroid/content/pm/Signature;->getFlags()");
		list.add("Landroid/content/pm/Signature;->toChars()");
		list.add("Landroid/content/pm/Signature;->getChainSignatures()");
		list.add("Landroid/content/pm/Signature;->equals(");
		list.add("Landroid/content/pm/Signature;->writeToParcel(");
		
		return list;
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
			if(ValidFilesForCodeInjection.fileCheck(line))
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
