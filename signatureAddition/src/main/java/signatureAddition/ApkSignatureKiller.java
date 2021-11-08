package signatureAddition;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class ApkSignatureKiller {


	public static String pathToNKS="/home/nikhil/Downloads/ApkSignatureKiller-master/nkstool.jar";
	public static String pathToJava="/usr/bin/java";
	public static String pathToConfig="/home/nikhil/Downloads/ApkSignatureKiller-master/config.txt";
	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		String filePath="/home/nikhil/Downloads/ApkSignatureKiller-master/packageNames.txt";
		File file=new File(filePath);
		Scanner scanner=new Scanner(file);
		String signInfoContents="sign.enable=true\n"
				+ "sign.file=test.keystore\n"
				+ "sign.password=123456\n"
				+ "sign.alias=user\n"
				+ "sign.aliasPassword=654321";
		
		while(scanner.hasNext())
		{
			String packageName=scanner.next();
			/**
			 * Write a new config.txt
			 */
			String directoryPath="/home/nikhil/Documents/apps/dataset/"+packageName+"/";
			
			String apkPath="/home/nikhil/Documents/apps/dataset/"+packageName+"/base.apk";
			String apkOutputPath="/home/nikhil/Documents/apps/apkSignatureKiller/"+packageName+".apk";
			String fileContents="apk.signed="+apkPath+"\napk.src="+apkPath+"\napk.out="+apkOutputPath+"\n"+signInfoContents;
			writeToFile(pathToConfig,fileContents);
			CommandExecute.commandExecution(pathToJava+" -jar "+pathToNKS);
		
		}
	}
	private static void writeToFile(String pathToConfig2, String fileContents) throws IOException {
		// TODO Auto-generated method stub
		FileWriter fileWriter=new FileWriter(pathToConfig);
		fileWriter.write(fileContents);
		fileWriter.close();
		
	}

}
