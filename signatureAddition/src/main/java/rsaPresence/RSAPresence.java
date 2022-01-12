package rsaPresence;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import Logs.LogAnalysis;
import ProcessMission.FetchProcessIdDumpsys;
import databaseUpdate.TableUpdate;
import finalRun.RealDeviceRun;
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
 * It simply installs the app, apk available in harddisk.
 * @author nikhil
 *
 */
public class RSAPresence {

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		String filePath="/home/nikhil/Documents/apps/v2AppsAnalysis.txt";
		File file=new File(filePath);
		Scanner scanner=new Scanner(file);
		int packageCount=0;
		String packageName="";
		int count1=0;
		while(scanner.hasNext())
		{
			try
			{
				packageName=scanner.next();
				//packageName="in.org.npci.upiapp";
				count1++;
				//	if(count1<=1350)
				//	continue;

				System.out.println("Package Number count  :"+count1);


				AntiTampering.uninstallApp(packageName,AppLaunchAndDump.deviceId[0]);
				//	String logPathOriginal="/home/nikhil/Documents/apps/logs/"+packageName+"/original.txt";

				String logPathRepackaged="/home/nikhil/Documents/apps/logs/"+packageName+"/repackaged.txt";

				String logPathModified="/home/nikhil/Documents/apps/logs/"+packageName+"/modified.txt";


				CommandExecute.commandExecution("mkdir /home/nikhil/Documents/apps/logs/"+packageName);

				String dumpPathDirectory="/home/nikhil/Documents/apps/uiautomator/rootEmulator/"+packageName;

				RootEmulation.createDirectory(dumpPathDirectory);
				String originalApkDirectory=AppsPull.appDirectoryPrefix+packageName+"/";

				Process process=CommandExecute.commandExecutionSh(RealDeviceRun.pathToLS+" "+originalApkDirectory);

				//fetchPermissionRequested.grantPermissions("com.phonepe.app", pathToApk);
				BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(process.getInputStream()));
				String line=bufferedReader.readLine();
				int count=0;
				String apkPaths="";//appPathDirectory+line;
				while(line!=null)
				{
					String temp=originalApkDirectory+line;
					count++;
					System.out.println(line);
					line=bufferedReader.readLine();
					apkPaths=apkPaths+" "+temp;
				}

				System.out.println(apkPaths);
				String installationCommandRealDevice="";
				if(count>1)
				{
					System.out.println("It is a split apk scenario");
					installationCommandRealDevice=LogAnalysis.pathToadb+" -s "+ AppLaunchAndDump.deviceId[0]+" install-multiple -g "+ apkPaths;
				}
				else
				{
					installationCommandRealDevice=LogAnalysis.pathToadb+ " -s "+ AppLaunchAndDump.deviceId[0]+ " install -g "+apkPaths;
				}
				CommandExecute.commandExecutionSh(installationCommandRealDevice);


				//Check if the app didn't installed
				if(!RootEmulation.isAppInstalled(packageName, AppLaunchAndDump.deviceId[0]))
				{
					restartSmartphone.restart(AppLaunchAndDump.deviceId[0]);
					CommandExecute.commandExecutionSh(installationCommandRealDevice);
				}

			}
			catch (Exception e) {

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
