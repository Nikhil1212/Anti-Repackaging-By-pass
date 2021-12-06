package LogInsertion;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import Logs.LogAnalysis;
import analysingDumpSys.DumpSysAnalysis;
import signatureAddition.CommandExecute;
import signatureAddition.resignedApp;

public class ResignedAppsLaunch {

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		String FilePath="/home/nikhil/Documents/apps/FilteredApps.txt";
		File file=new File(FilePath);
		Scanner scanner=new Scanner(file);
		int count=0;
		while(scanner.hasNext())
		{
			String packageName="";
			String pathToDisAssembleCodeDirectory="";
			String pathToResignedApk="";

			try
			{	
				packageName=scanner.next();
				String pathToOriginalApk="/home/nikhil/Downloads/googleplay-api-master/"+packageName+".apk";
				pathToResignedApk="/home/nikhil/Documents/apps/ReSignedApks/"+packageName+".apk";
				resignedApp.signApk(packageName, pathToOriginalApk, pathToResignedApk);
				
				CommandExecute.commandExecution(LogAnalysis.pathToadb+" install -g "+pathToResignedApk);
				DumpSysAnalysis.launchTheApp(packageName);
				
			}
			catch (Exception e) {
				// TODO: handle exception
			}

	}

}
}
