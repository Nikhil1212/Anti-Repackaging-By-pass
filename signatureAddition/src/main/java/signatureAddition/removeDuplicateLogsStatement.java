package signatureAddition;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class removeDuplicateLogsStatement {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		//System.out.println("Hello Nikhil");
		String listOfFilesPath="/home/nikhil/Documents/apps/absolutePaths.txt";
		File file=new File(listOfFilesPath);
		 Scanner scanner=new Scanner(file);
		while(scanner.hasNext())
		{
			String inputFilePath=scanner.next();
			System.out.println("Input file path is :"+inputFilePath);
			String packageNameTxt=getPackageNameFromFileName(inputFilePath);
			String outputFilePath="/home/nikhil/Documents/apps/logOutputNew/filteredLogsRemoveDuplicate/"+packageNameTxt;
			File inputFile=new File(inputFilePath);
			File outputFile=new File(outputFilePath);
			System.out.println("output file path :"+outputFilePath);
			outputFile.createNewFile();
			String outputContents="";
			Scanner scanner2=new Scanner(inputFile);
			while (scanner2.hasNext()) {
				String string =  scanner2.nextLine();
				//String currentContents=new String (Files.readAllBytes(Paths.get(outputFilePath)));
				//System.out.println(currentContents);
				if(outputContents.contains(string))
					continue;
				else
				{
					System.out.println(string);
					outputContents=outputContents.concat(string+"\n");
				}
			}
			FileWriter fileWriter=new FileWriter(outputFile);
			fileWriter.write(outputContents);
			fileWriter.close();
		}
			
	}

	public static String getPackageNameFromFileName(String inputFilePath) {
		// TODO Auto-generated method stub
		int start=inputFilePath.lastIndexOf('/');
		//String txt=".txt";
		//int end=inputFilePath.indexOf(txt);
		System.out.println(inputFilePath.substring(start+1));
		return inputFilePath.substring(start+1);
	}

}
