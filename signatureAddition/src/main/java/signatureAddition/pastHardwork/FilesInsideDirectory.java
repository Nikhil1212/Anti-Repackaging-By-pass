package signatureAddition.pastHardwork;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Scanner;

import Logs.LogAnalysis;
import signatureAddition.AppLaunchAndDump;
import signatureAddition.CommandExecute;
import uiautomator.ResourceIDAnalysis;

public class FilesInsideDirectory {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String packageName="";
		String filePath="/home/nikhil/Documents/apps/dataset/packageNames_2.txt";
		File file=new File(filePath);
		Scanner scanner=new Scanner(file);
		String outputFilePath="/home/nikhil/Documents/apps/apkFormat.txt";
		String output="";
		while(scanner.hasNext())
		{
			try
			{

				packageName=scanner.next();

				String appPathDirectory="/home/nikhil/Documents/apps/dataset/"+packageName+"/";

				Process process=CommandExecute.commandExecution(AppLaunchAndDump.pathToLS+" "+appPathDirectory);
				
				//fetchPermissionRequested.grantPermissions("com.phonepe.app", pathToApk);
				BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(process.getInputStream()));
				String line=bufferedReader.readLine();
				int count=0;
				String apkPaths="";//appPathDirectory+line;
				while(line!=null)
				{
					String temp=appPathDirectory+line;
					count++;
					System.out.println(line);
					line=bufferedReader.readLine();
					apkPaths=apkPaths+" "+temp;
				}
				System.out.println(apkPaths);
				if(count==1)
				{
					output=output+packageName+"\n";
				}
				
			}
			
			catch (Exception e) {
				// TODO: handle exception
				
				e.printStackTrace();
			}
		
	}
		ResourceIDAnalysis.writeToFile(outputFilePath, output);

}
}
