package signatureAddition.PullApps;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Scanner;

import Logs.LogAnalysis;
import signatureAddition.CommandExecute;


/**
 * This class will pull the app from the Internal storage section of Mobile device to the local storage.
 * @author nikhil
 *
 */
public class AppsPull {
	
	//host=%5Busb%3A001%2C026%5D
	//host=%5Busb%3A001%2C029%5D/Internal
	//host=%5Busb%3A001%2C032%5D/Internal
	//host=%5Busb%3A001%2C036%5D/Internal
	//host=%5Busb%3A001%2C039%5D/Internal
	//host=%5Busb%3A001%2C045%5D/Internal
	public static String appDirectoryPrefix="/run/user/1000/gvfs/mtp:host\\=%5Busb%3A001%2C012%5D/Internal\\ shared\\ storage/dataset/";

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		String pathToApk="/home/nikhil/Documents/apps/PackageNames.txt";
		File file=new File(pathToApk);
		Scanner scanner=new Scanner(file);
		String outputContents="";
		while(scanner.hasNext())
		{
			try
			{

				String packageName=scanner.next();
			//	packageName="com.phonepe.app";
				String localDirectoryPath="/home/nikhil/Documents/apps/dataset/"+packageName;
				
				CommandExecute.commandExecution("mkdir "+localDirectoryPath);
				
				String appDirectoryPath=AppsPull.appDirectoryPrefix+packageName+"/";
				
				Process process=Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", "ls "+appDirectoryPath});///home/nikhil/Documents/apps/com.mbanking.aprb.aprb"});
				
				process.waitFor();
				
//				Process process=CommandExecute.commandExecution("ls "+command);
				
				
				
				BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(process.getInputStream()));
				String line=bufferedReader.readLine();
				int count=0;
				while(line!=null)
				{
					System.out.println(line);
					
					Process process1=Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", "cp "+appDirectoryPath+line +" " +localDirectoryPath});///home/nikhil/Documents/apps/com.mbanking.aprb.aprb"});
					
					process1.waitFor();
					//CommandExecute.commandExecution("cp "+line+" "+localDirectoryPath);
					line=bufferedReader.readLine();
				}
				if(count==0)
					outputContents=outputContents+packageName+"\n";
				
				
			}
			catch (Exception e) {
				// TODO: handle exception
				
				e.printStackTrace();
			}
			//break;
	}
		System.out.println(outputContents);

}
}
