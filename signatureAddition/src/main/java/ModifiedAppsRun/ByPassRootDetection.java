package ModifiedAppsRun;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import Logs.LogAnalysis;
import ProcessMission.FetchProcessIdDumpsys;
import signatureAddition.AntiTampering;
import signatureAddition.AppLaunchAndDump;
import signatureAddition.CommandExecute;
import signatureAddition.StartingPoint;
import signatureAddition.PullApps.AppsPull;

/**
 * This class assumes that the apps in which the check is present; those list of apps is stored in a file. And this class takes this file 
 * as an input and generate the modified version of the app and then installs and launch the app and capture the dump and the logs.
 * @author nikhil
 *
 */
public class ByPassRootDetection {

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub

		//Have the filePath
		
		String filePath="/home/nikhil/Documents/apps/RootCheckPresentTool.txt";
		
		File file=new File(filePath);
		Scanner scanner=new Scanner(file);
		int packageCount=0;
		String appPrefixDirectory="/home/nikhil/Documents/apps/dataset/";
		while(scanner.hasNext())
		{
			try
			{
				String packageName=scanner.next();
				//Refer the anti-tampering class
				
				System.out.println("package Count is :"+(++packageCount));
				
				
				String originalApkDirectory=appPrefixDirectory+packageName+"/";

				String dumpPathDirectory="/home/nikhil/Documents/apps/uiautomator/rootEmulator/"+packageName;

				Process process=CommandExecute.commandExecutionSh(AntiTampering.pathToLS+" "+originalApkDirectory);
				
				String logPathModifed="/home/nikhil/Documents/apps/logs/"+packageName+"/"+AppLaunchAndDump.deviceIdSynonym[6]+".txt";
					
				int count=ByPassAntiTampering.isSplitApk(packageName,originalApkDirectory);
				
				String modifiedApkPath=StartingPoint.modifiedApkGenerationRootDetection(packageName);
				
				CommandExecute.commandExecution("rm -r /home/nikhil/Documents/apps/"+packageName);

				String installationCommand=finalRun.AntiTampering.getInstallationCommand(packageName,modifiedApkPath,count,AppLaunchAndDump.deviceId[2]);
				
				finalRun.AntiTampering.takeDumpOnDevice(AppLaunchAndDump.deviceId[2], AppLaunchAndDump.deviceIdSynonym[5],installationCommand,count,packageName,dumpPathDirectory,originalApkDirectory);

				String pidModified=FetchProcessIdDumpsys.retrievePID(packageName, AppLaunchAndDump.deviceId[2]);
				
				LogAnalysis.usingLogcat(packageName, pidModified, logPathModifed,AppLaunchAndDump.deviceId[2]);
				
				finalRun.AntiTampering.uninstallApp(packageName,AppLaunchAndDump.deviceId[2]);
				
				CommandExecute.commandExecution("rm -r /home/nikhil/Documents/apps/"+packageName);
				
				CommandExecute.commandExecution("rm "+modifiedApkPath);
				
				//break;
			}
			
			catch (Exception e) {
				
				e.printStackTrace();
			
			}
			
			
		}
		
	}

}
