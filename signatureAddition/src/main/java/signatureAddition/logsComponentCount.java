package signatureAddition;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class logsComponentCount {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		System.out.println("Hello Nikhil");
		String listOfPackageNames="/home/nikhil/Documents/apps/packageNames.txt";
		File fileListofPackages=new File(listOfPackageNames);
		Scanner scanner1=new Scanner(fileListofPackages);
		while(scanner1.hasNext())
		{
			String packageName=scanner1.next();
			System.out.println("For the package name : "+packageName);
			String originalFilePath="/home/nikhil/Documents/apps/logOutput_component_count/original_"+packageName+".txt";//.bankofbaroda.mconnect.txt"
			String resignedFilePath="/home/nikhil/Documents/apps/logOutput_component_count/resigned_"+packageName+".txt";//.bankofbaroda.mconnect.txt";
			String modifiedFilePath="/home/nikhil/Documents/apps/logOutput_component_count/modifed_"+packageName+".txt";//.bankofbaroda.mconnect.txt";
			
			String mapOriginal=generateOutput(originalFilePath);
			String mapResigned=generateOutput(resignedFilePath);
			String mapModified=generateOutput(modifiedFilePath);
			String fileContents="************************\nOriginal Apps Components\n "+mapOriginal+"\n***************\n App resigned\n"+mapResigned+"\n**********************\nApp Modified\n"+mapModified+"\n***************";
			System.out.println(fileContents);
			
			String filePath="/home/nikhil/Documents/apps/logsComponentCountNumber/"+packageName+".txt";
			FileWriter myWriter = new FileWriter(filePath);
			myWriter.write(fileContents);
			myWriter.close();
		
		}
		//String filePath="/home/nikhil/Documents/apps/logOutput_component_count/modifed_com.bankofbaroda.mconnect.txt";
		}

	private static String generateOutput(String FilePath) throws FileNotFoundException {
		// TODO Auto-generated method stub
		File file=new File(FilePath);
		HashMap<String, Integer> map = new HashMap();
		Scanner scanner=new Scanner(file);
		while(scanner.hasNext())
		{
			String text=scanner.next();
			if(map.containsKey(text))
			{
				int count=map.get(text);
				map.put(text, ++count);
			}
			else
				map.put(text, 1);
			
		}
		return ""+map;
		
	}

}
