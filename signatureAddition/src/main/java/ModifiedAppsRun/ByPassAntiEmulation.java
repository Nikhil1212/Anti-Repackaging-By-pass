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

public class ByPassAntiEmulation {

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		String filePath="/home/nikhil/Documents/apps/EDReRunApps.txt";
		File file=new File(filePath);
		Scanner scanner=new Scanner(file);
		int packageCount=0;
		String packageName="";
		String	localBaseModifiedApkPath="";
		while(scanner.hasNext())
		{
			try
			{
				 packageName=scanner.next();
				//Refer the anti-tampering class

				System.out.println("package Count is :"+(++packageCount));
				if(packageCount < 224)
					continue;


				String originalApkDirectory=AppsPull.appDirectoryPrefix+packageName+"/";

				String dumpPathDirectory="/home/nikhil/Documents/apps/uiautomator/rootEmulator/"+packageName;

				//	Process process=CommandExecute.commandExecutionSh(AntiTampering.pathToLS+" "+originalApkDirectory);

				String logPathModifed="/home/nikhil/Documents/apps/logs/"+packageName+"/"+AppLaunchAndDump.deviceIdSynonym[5]+".txt";

				int count=isSplitApk(packageName,originalApkDirectory);

				String modifiedAppsDirectory=AppsPull.modifiedAppsDirectoryPath+"RootEmulator/";
				 localBaseModifiedApkPath="/home/nikhil/Documents/apps/"+packageName+".apk";

				//	String modifiedApkLocalPath=
				String modifiedApkPathHD=modifiedAppsDirectory+"modified_"+packageName+".apk";



				String	copyCommand="cp "+modifiedApkPathHD+" "+localBaseModifiedApkPath;

				CommandExecute.commandExecutionSh(copyCommand);

				File file2=new File(localBaseModifiedApkPath);

				if(!file2.exists())
					continue;


				//	String modifiedApkPath=StartingPoint.modifiedApkGenerationAntiTampering(packageName,originalApkDirectory+"/base.apk");



				String installationCommand=finalRun.AntiTampering.getInstallationCommand(packageName,localBaseModifiedApkPath,count,AppLaunchAndDump.deviceId[1]);

				AntiTampering.takeDumpOnDevice(AppLaunchAndDump.deviceId[1], AppLaunchAndDump.deviceIdSynonym[5],installationCommand,count,packageName,dumpPathDirectory,originalApkDirectory);

				String pidModified=FetchProcessIdDumpsys.retrievePID(packageName, AppLaunchAndDump.deviceId[1]);

				LogAnalysis.usingLogcat(packageName, pidModified, logPathModifed,AppLaunchAndDump.deviceId[1]);

				finalRun.AntiTampering.uninstallApp(packageName,AppLaunchAndDump.deviceId[1]);

				CommandExecute.commandExecution("rm -r /home/nikhil/Documents/apps/"+packageName);

				CommandExecute.commandExecution("rm "+localBaseModifiedApkPath);


				//remove the idsig file
			}

			catch (Exception e) {
				try {
					CommandExecute.commandExecution("rm -r /home/nikhil/Documents/apps/"+packageName);
					CommandExecute.commandExecution("rm "+localBaseModifiedApkPath);

				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			
				e.printStackTrace();

			}


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
