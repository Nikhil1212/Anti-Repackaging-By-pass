package ModifiedAppsRun;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import Logs.LogAnalysis;
import ProcessMission.FetchProcessIdDumpsys;
import signatureAddition.AntiTampering;
import signatureAddition.AppLaunchAndDump;
import signatureAddition.CommandExecute;
import signatureAddition.StartingPoint;
import signatureAddition.PullApps.AppsPull;


/**
 * This class assumes that the modified version of the app is already generated.
 * @author nikhil
 *
 */
public class OnePlusApps {

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		String filePath="/home/nikhil/Documents/apps/OnePlusApps.txt";
		File file=new File(filePath);
		Scanner scanner=new Scanner(file);
		int packageCount=0;
		while(scanner.hasNext())
		{
			try
			{
				String packageName=scanner.next();
				//Refer the anti-tampering class
				
			//	packageName="com.upi.axispay";
				System.out.println("package Count is :"+(++packageCount));
				
				String originalApkDirectory=AppsPull.appDirectoryPrefix+packageName+"/";

				String dumpPathDirectory="/home/nikhil/Documents/apps/uiautomator/rootEmulator/"+packageName;

			//	Process process=CommandExecute.commandExecutionSh(AntiTampering.pathToLS+" "+originalApkDirectory);
				
				String logPathModifed="/home/nikhil/Documents/apps/logs/"+packageName+"/"+AppLaunchAndDump.deviceIdSynonym[4]+".txt";
				
				int count=isSplitApk(packageName,originalApkDirectory);
				
				String modifiedAppsDirectory=AppsPull.modifiedAppsDirectoryPath+"SignatureByPass/";
				String localBaseModifiedApkPath="/home/nikhil/Documents/apps/"+packageName+".apk";
				
			//	String modifiedApkLocalPath=
				String modifiedApkPathHD=modifiedAppsDirectory+"modified_"+packageName+".apk";
				
			
				String	copyCommand="cp "+modifiedApkPathHD+" "+localBaseModifiedApkPath;
				
				CommandExecute.commandExecutionSh(copyCommand);

				
				File file2=new File(localBaseModifiedApkPath);
				if(!file2.exists())
					continue;
				
			//	String modifiedApkPath=StartingPoint.modifiedApkGenerationAntiTampering(packageName,originalApkDirectory+"base.apk");
				
				
					
				String installationCommand=finalRun.AntiTampering.getInstallationCommand(packageName,localBaseModifiedApkPath,count,AppLaunchAndDump.deviceId[3]);
				
				AntiTampering.takeDumpOnDevice(AppLaunchAndDump.deviceId[3], AppLaunchAndDump.deviceIdSynonym[4],installationCommand,count,packageName,dumpPathDirectory,originalApkDirectory);

				String pidModified=FetchProcessIdDumpsys.retrievePID(packageName, AppLaunchAndDump.deviceId[3]);
				
				LogAnalysis.usingLogcat(packageName, pidModified, logPathModifed,AppLaunchAndDump.deviceId[3]);
				
				finalRun.AntiTampering.uninstallApp(packageName,AppLaunchAndDump.deviceId[3]);
				
				CommandExecute.commandExecution("rm -r /home/nikhil/Documents/apps/"+packageName);
				
				CommandExecute.commandExecution("rm "+localBaseModifiedApkPath);
				
				
			}
			
			catch (Exception e) {
				
				e.printStackTrace();
			
			}
		//	break;

			
			
		}

	}

	public static int isSplitApk(String packageName, String originalApkDirectory) throws IOException, InterruptedException {
		int count=0;
		
		Process process=CommandExecute.commandExecutionSh(AntiTampering.pathToLS+" "+originalApkDirectory);
		
		//fetchPermissionRequested.grantPermissions("com.phonepe.app", pathToApk);
		BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line=bufferedReader.readLine();
		String apkPaths="";//appPathDirectory+line;
		while(line!=null)
		{
			String temp=originalApkDirectory+line;
			count++;
			System.out.println(line);
			line=bufferedReader.readLine();
			apkPaths=apkPaths+" "+temp;
		}
		return count;
	}

}
