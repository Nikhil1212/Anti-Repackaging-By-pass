package signatureAddition.PullApps;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Scanner;

import Logs.LogAnalysis;
import signatureAddition.CommandExecute;

public class PackageNamePull {
		
		public static void main(String[] args) throws FileNotFoundException {
			// TODO Auto-generated method stub
			String pathToApk="/home/nikhil/Documents/apps/TextFiles/AppsReDownload.txt";
			File file=new File(pathToApk);
			Scanner scanner=new Scanner(file);
			String outputContents="";
			while(scanner.hasNext())
			{
				try
				{
					String packageName=scanner.next();
					String localDirectoryPath="/home/nikhil/Documents/apps/dataset/"+packageName;
					
					CommandExecute.commandExecution("mkdir "+localDirectoryPath);
					
					String pathToAppCommand=LogAnalysis.pathToadb+" shell pm path "+packageName;
					//	addingPathForRePackaged(rePackageNamePath,packageName);
						
					
					Process process=CommandExecute.commandExecution(pathToAppCommand);
					pathToAppCommand=pathToAppCommand.concat(packageName);
						
						
						System.out.println(pathToAppCommand);
						/*
						 * Let's fetch the directory in which base.apk
						 * file is present
						 */
						BufferedReader buf = new BufferedReader(new InputStreamReader(process.getInputStream()));
						//String pathToBase_apk = "";
						String line =buf.readLine();
						
						System.out.println(line);
						if(line!=null)
						{
							String pullCommand=LogAnalysis.pathToadb+" pull "+line.substring(8)+" "+localDirectoryPath;
							CommandExecute.commandExecution(pullCommand);	
							System.out.println(line);
							line=buf.readLine();
						}
							
					
					
				}
				catch (Exception e) {
					// TODO: handle exception
				}
			}
			
	}

}
