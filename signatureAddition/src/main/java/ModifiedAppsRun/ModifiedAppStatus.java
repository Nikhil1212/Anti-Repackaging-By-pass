package ModifiedAppsRun;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import Logs.LogAnalysis;
import ProcessMission.FetchProcessIdDumpsys;
import databaseUpdate.TableUpdate;
import finalRun.RootDetection;
import finalRun.isDumpGenerated;
import signatureAddition.AntiTampering;
import signatureAddition.AppLaunchAndDump;
import signatureAddition.CommandExecute;
import signatureAddition.GettingConstantDumps;
import signatureAddition.RootEmulation;
import signatureAddition.StartingPoint;
import signatureAddition.PullApps.AppsPull;
import signatureAddition.pastHardwork.restartSmartphone;

/**
 * This class assumes that the apps in which the check is present; those list of apps is stored in a file. And this class takes this file 
 * as an input and generate the modified version of the app and then installs and launch the app and capture the dump and the logs.
 * @author nikhil
 *
 */
public class ModifiedAppStatus {

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		String filePath="/home/nikhil/Documents/apps/RootEmulator_ModifiedAppsList.txt";
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
				if(packageCount<400)
					continue;

				String originalApkDirectory=AppsPull.appDirectoryPrefix+packageName+"/";

				String dumpPathDirectory="/home/nikhil/Documents/apps/uiautomator/rootEmulator/"+packageName;

				//	Process process=CommandExecute.commandExecutionSh(AntiTampering.pathToLS+" "+originalApkDirectory);

				String logPathModifed="/home/nikhil/Documents/apps/logs/"+packageName+"/"+AppLaunchAndDump.deviceIdSynonym[6]+".txt";

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



				String installationCommand=finalRun.AntiTampering.getInstallationCommand(packageName,localBaseModifiedApkPath,count,AppLaunchAndDump.deviceId[0]);

				takeDumpOnDevice(AppLaunchAndDump.deviceId[0], AppLaunchAndDump.deviceIdSynonym[6],installationCommand,count,packageName,dumpPathDirectory,originalApkDirectory);


				String pidModified=FetchProcessIdDumpsys.retrievePID(packageName, AppLaunchAndDump.deviceId[0]);

				if(pidModified==null)
				{
					//App did not run. update the table
					isDumpGenerated.updateTable(packageName, "ModifiedAppRunningStatus",'N');

				}

				//	LogAnalysis.usingLogcat(packageName, pidModified, logPathModifed,AppLaunchAndDump.deviceId[0]);

				finalRun.AntiTampering.uninstallApp(packageName,AppLaunchAndDump.deviceId[0]);

				CommandExecute.commandExecution("rm -r /home/nikhil/Documents/apps/"+packageName);

				CommandExecute.commandExecution("rm "+localBaseModifiedApkPath);

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

	public static void takeDumpOnDevice(String deviceId, String deviceSynonym, String installationCommand, int count,
			String packageName, String directoryPath, String appPathDirectory) throws Exception {
		String destinationPath="";
		//RootEmulation.dumpTheAppScreen(packageName, destinationPath2, deviceId[2]);
		AntiTampering.uninstallApp(packageName, deviceId);

		CommandExecute.commandExecutionSh(installationCommand);



		if(!RootEmulation.isAppInstalled(packageName, deviceId))
		{
			//Check whether restarting works
			restartSmartphone.restart(deviceId);
			//installAndGrantPermission(installationCommand, count, packageName, appPathDirectory, deviceId);

		}
		if(!RootEmulation.isAppInstalled(packageName, deviceId))
		{
			TableUpdate.updateTable(packageName, "isAppInstalled");
			return;

		}

		destinationPath="/data/local/tmp/"+packageName+".apk";
		CommandExecute.commandExecution(LogAnalysis.pathToadb+" -s "+deviceId+" shell rm "+destinationPath);


		//DumpSysAnalysis.launchTheApp(packageName);
		//destinationPath=directoryPath+"/"+deviceSynonym+"_python.xml";

		destinationPath=directoryPath+"/"+deviceSynonym+"_BuiltIn.xml";

		//DumpSysAnalysis.launchTheApp(packageName);

		String clearLogcat=LogAnalysis.pathToadb+" -s "+deviceId +" shell logcat -b all -c";



		//CommandExecute.commandExecution(pathToadb+" install "+pathToApk);

		CommandExecute.commandExecution(clearLogcat);


		RootEmulation.launchTheApp(packageName,deviceId);

		//	GettingConstantDumps.main(packageName, destinationPath, deviceId);
		//DumpUIAutomatorPython.main(destinationPath,deviceId);


		//	uninstallApp(packageName,deviceId);


	}


}
