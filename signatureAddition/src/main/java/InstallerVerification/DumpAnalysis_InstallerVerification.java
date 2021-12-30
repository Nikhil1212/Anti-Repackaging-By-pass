package InstallerVerification;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import Logs.LogAnalysis;
import analysingDumpSys.DumpSysAnalysis;
import signatureAddition.AppLaunchAndDump;
import signatureAddition.CommandExecute;
import signatureAddition.GettingConstantDumps;
import signatureAddition.RootEmulation;
import signatureAddition.fetchPermissionRequested;


/**
 * This assumes that the app is already installed from the Play Store and then we take the dump and then side-load, then we uninstall and take the dump again by side-loading the app.
 * @author nikhil
 *
 */
public class DumpAnalysis_InstallerVerification {

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		String filePath="/home/nikhil/Documents/apps/falseNegativeResults.txt";
		File file=new File(filePath);
		Scanner scanner=new Scanner(file);
		
		String packageName="";
		while(scanner.hasNext())
		{
			packageName=scanner.next();
			try {
				
				String pathToApk="/home/nikhil/Documents/apps/dataset/"+packageName+"/base.apk";

				fetchPermissionRequested.grantPermissions(packageName, pathToApk);
				
				
				DumpSysAnalysis.launchTheApp(packageName);

				String filePathPSApp="/home/nikhil/Documents/apps/uiautomator/rootEmulator/"+packageName+"/ps_BuiltIn.xml";
				
				GettingConstantDumps.main(packageName, filePathPSApp, AppLaunchAndDump.deviceId[0]);

				String filePathSideLoad="/home/nikhil/Documents/apps/uiautomator/rootEmulator/"+packageName+"/sideLoad_BuiltIn.xml";
				
				//uninstallApp(packageName);
				
				SideLoadAppInstall.main(packageName);
				
				RootEmulation.dumpTheAppScreen(packageName, filePathSideLoad, AppLaunchAndDump.deviceId[0]);
				
				uninstallApp(packageName);
				
				
				
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
		}
	}

	private static void uninstallApp(String packageName) {
		// TODO Auto-generated method stub
		try {
			CommandExecute.commandExecution(LogAnalysis.pathToadb+" uninstall "+packageName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
