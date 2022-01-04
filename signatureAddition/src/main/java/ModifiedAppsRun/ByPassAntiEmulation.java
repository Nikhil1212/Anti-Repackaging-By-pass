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

public class ByPassAntiEmulation {

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		String filePath="/home/nikhil/Documents/apps/EmulatorDetectionCheckPresent.txt";
		
		File file=new File(filePath);
		Scanner scanner=new Scanner(file);
		int packageCount=0;
		while(scanner.hasNext())
		{
			try
			{
				String packageName=scanner.next();
				//Refer the anti-tampering class
				
				System.out.println("package Count is :"+(++packageCount));
				
				String originalApkDirectory=AppsPull.appDirectoryPrefix+packageName+"/";

				String dumpPathDirectory="/home/nikhil/Documents/apps/uiautomator/rootEmulator/"+packageName;

				Process process=CommandExecute.commandExecutionSh(AntiTampering.pathToLS+" "+originalApkDirectory);
				
				String logPathModifed="/home/nikhil/Documents/apps/logs/"+packageName+"/"+AppLaunchAndDump.deviceIdSynonym[5]+".txt";
					
				int count=ByPassAntiTampering.isSplitApk(packageName,originalApkDirectory);
				
				String modifiedApkPath=StartingPoint.modifiedApkGenerationAntiEmulator(packageName);
				
				String installationCommand=finalRun.AntiTampering.getInstallationCommand(packageName,modifiedApkPath,count,AppLaunchAndDump.deviceId[1]);
				
				AntiTampering.takeDumpOnDevice(AppLaunchAndDump.deviceId[1], AppLaunchAndDump.deviceIdSynonym[5],installationCommand,count,packageName,dumpPathDirectory,originalApkDirectory);

				String pidModified=FetchProcessIdDumpsys.retrievePID(packageName, AppLaunchAndDump.deviceId[1]);
				
				LogAnalysis.usingLogcat(packageName, pidModified, logPathModifed,AppLaunchAndDump.deviceId[1]);
				
				finalRun.AntiTampering.uninstallApp(packageName,AppLaunchAndDump.deviceId[1]);
			}
			
			catch (Exception e) {
				
				e.printStackTrace();
			
			}
			
			
		}
	}

}
