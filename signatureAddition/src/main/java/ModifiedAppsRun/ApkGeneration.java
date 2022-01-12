package ModifiedAppsRun;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class ApkGeneration {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String filePathRootEmulation="/home/nikhil/Documents/apps/RootEmulator.txt";
		String filePathSignature="/home/nikhil/Documents/apps/ModifiedAppsAntiTampering.txt";
		String fileContentsRoot=new String (Files.readAllBytes(Paths.get(filePathRootEmulation)));		
		
		String fileContentsSignature=new String (Files.readAllBytes(Paths.get(filePathSignature)));		
		
		String packageNameFilePath="/home/nikhil/Documents/apps/ATv2.txt";
		File file=new File(packageNameFilePath);
		Scanner scanner=new Scanner(file);
		
		String AppNotGenerated="";
		while (scanner.hasNext()) {
			String packageName = (String) scanner.next();
			if(fileContentsSignature.contains(packageName))
			{
				continue;
			}
			AppNotGenerated=AppNotGenerated+packageName+"\n";
	
		}
		System.out.println(AppNotGenerated);
	}

}
