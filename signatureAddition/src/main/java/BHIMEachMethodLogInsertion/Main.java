package BHIMEachMethodLogInsertion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import LogInsertion.ClassMethodLogInsertion;
import signatureAddition.CommandExecute;
import signatureAddition.StartingPoint;

public class Main {

	public static void main(String[] args) throws Exception {

		String pathToApk="/home/nikhil/Downloads/googleplay-api-master/in.org.npci.upiapp.apk";
		String packageName="in.org.npci.upiapp";
		String pathToAssemblyDirectory="/home/nikhil/Documents/apps/"+packageName+"/smali";
	//	StartingPoint.disassembleApk(pathToApk, packageName);
		String directoryPath="/home/nikhil/Downloads/googleplay-api-master/bitcoin.paper.app/";
		String command="ls -R "+directoryPath+" | awk ' /:$/&&f{s=$0;f=0} /:$/&&!f{sub(/:$/,\"\");s=$0;f=1;next} NF&&f{ print s\"/\"$0 }'";
		System.out.println(command);
		Process process=CommandExecute.commandExecution("ls ");
		String outputFilePath="/home/nikhil/Downloads/googleplay-api-master/SmaliFullPath.txt";
		String commandToExecuteTree="tree -fi "+pathToAssemblyDirectory;
		//System.out.println(commandToExecuteTree);
			//Process process= CommandExecute.commandExecution(commandToExecuteTree);
		BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line=bufferedReader.readLine();
		System.out.println(line);
		 while(line!=null)
		 {
			 System.out.println(line);
			 line=bufferedReader.readLine();
		 }
		File file=new File(outputFilePath);

		Scanner scanner=new Scanner(file);
		while(scanner.hasNext())
		{
			String filePath=scanner.next();
			if(!filePath.contains(".smali") || filePath.contains("com/google"))
				continue;
			File file2=new File(filePath);
			Scanner scanner2=new Scanner(file2);
			String outputContents="";
			String methodSignature="";
			String className=ClassMethodLogInsertion.fetchTagForInsertionClassName(filePath);
			System.out.println(filePath);
			while(scanner2.hasNext())
			{
				 line=scanner2.nextLine();
				outputContents=outputContents+line+"\n";
				if(findMethodKeyword(line))
				{
					methodSignature=fetchMethodSignature(line);

					String localsString=scanner2.nextLine();
					if(localsString.length()==0)
					{
						while(localsString.length()==0)
						{
							localsString=scanner2.nextLine();
						}
							
					}
					if(localsString.contains(".locals "))
					localsString = updateLocalsCountIfRequired(localsString);
					else
					{
						outputContents=outputContents+localsString+"\n";
						continue;
					}
						
					String codeToBeInserted=ClassMethodLogInsertion.logGenerationCode(className, methodSignature);
					outputContents=outputContents+localsString+"\n"+codeToBeInserted+"\n";
				}

			}

			writeToTheFile(outputContents,filePath);
		}
		/**
		 * 
		 * Build the apk, sign it, launch it, generate the logs.
		 */
	}

	public static String updateLocalsCountIfRequired(String localsString) {

		System.out.println("Value is :"+localsString);
		String localsKeyword=".locals ";
		int index=localsString.indexOf(localsKeyword);
		int startingPoint=index+localsKeyword.length();
		index=startingPoint;
		String count=localsString.substring(index);
		System.out.println("Value of the count is :"+count);
		int registerCount=Integer.parseInt(count);
		if(registerCount<2)
		{
			return (localsString.substring(0, startingPoint)+"2");
		}
		return localsString;
	}

	public static String fetchMethodSignature(String string) {
		//String demo=".method public constructor <init>(I)V";
		int length=".method ".length();
		return string.substring(length);
		//return null;
	}

	private static boolean findMethodKeyword(String line) {
		// TODO Auto-generated method stub
		if(line.contains(".method "))
			return true;
		return false;
	}

	public static void writeToTheFile(String outputContents, String fileName) throws IOException{
		// TODO Auto-generated method stub
		File file=new File(fileName);
		FileWriter fileWriter=new FileWriter(file);
		fileWriter.write(outputContents);
		fileWriter.close();
	}

}
