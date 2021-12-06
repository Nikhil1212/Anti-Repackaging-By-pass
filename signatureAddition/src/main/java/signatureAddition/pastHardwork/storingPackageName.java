package signatureAddition;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class storingPackageName {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String FilePath="/home/nikhil/Downloads/ATADetector-master/apks/pathToApps.txt";
		File file=new File(FilePath);
		String outputFilePath="/home/nikhil/Documents/apps/packageNames.txt";
		String fileContents="";
		Scanner scanner=new Scanner(file);
		while(scanner.hasNext())
		{
			try
			{
				String pathToOriginalApk=scanner.next();
				String packageName=StartingPoint.getPackageName(pathToOriginalApk);
				fileContents=fileContents+packageName+"\n";
			}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		FileWriter fileWriter=new FileWriter(outputFilePath);
		fileWriter.write(fileContents);
		fileWriter.close();


	}}
