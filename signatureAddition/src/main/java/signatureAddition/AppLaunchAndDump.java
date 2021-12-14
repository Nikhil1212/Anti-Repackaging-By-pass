package signatureAddition;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import Logs.LogAnalysis;
import analysingDumpSys.DumpSysAnalysis;

/**
 * So, this class takes the package name as an input and installs the app if the apk is present in the local machine. Handles split-apks scenarios also. This will also grant the permissions and launches the app.
 * Currently, we are trying to find the bug in the uiautomator part, so we are taking the dump two times for a single installation and then we will compare the resource id.
 * @author nikhil
 *
 */
public class AppLaunchAndDump {
	public  static String pathToLS="/bin/ls";
	public  static String readlink="/bin/readlink";
	
	public static String deviceId[]={"14011JEC202909", "emulator-5554", "0248f4221b4ca0ee"};
	public static String deviceIdSynonym[]={"real", "emulator","rooted"};


	public static void main(String[] args) throws Exception {

		String pathToApk="/home/nikhil/Documents/apps/dataset/packageNames_2.txt";
		File file=new File(pathToApk);
		Scanner scanner=new Scanner(file);
		String packageName="";
		while(scanner.hasNext())
		{
			try
			{

				packageName=scanner.next();
			//	packageName="net.one97.paytm";
				uninstallApp(packageName, deviceId[0]);
			
				uninstallApp(packageName, deviceId[2]);
				
				String directoryPath="/home/nikhil/Documents/apps/uiautomator/rootEmulator/"+packageName;

				RootEmulation.createDirectory(directoryPath);
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
				String installationCommandNonRoot="";
				String installationCommandRoot="";
				String installationCommandEmulator="";
				
				System.out.println(apkPaths);
				String installationCommandRootSingleDevice="";
				if(count>1)
				{
					System.out.println("It is a split apk scenario");
					installationCommandNonRoot=LogAnalysis.pathToadb+" -s "+ deviceId[0]+" install-multiple -g "+ apkPaths;
					installationCommandRoot=LogAnalysis.pathToadb+" -s "+ deviceId[2]+" install-multiple -g "+ apkPaths;
					installationCommandEmulator=LogAnalysis.pathToadb+" -e install-multiple -g "+apkPaths;
					installationCommandRootSingleDevice=LogAnalysis.pathToadb+" -s "+ deviceId[2]+" install-multiple -g "+ apkPaths;

				}
				else
				{
					installationCommandNonRoot=LogAnalysis.pathToadb+" -s "+ deviceId[0]+ " install -g "+apkPaths;
					installationCommandRoot=LogAnalysis.pathToadb+" -s "+ deviceId[2]+ " install -g "+apkPaths;
					installationCommandEmulator=LogAnalysis.pathToadb+" -e install -g "+apkPaths;
					installationCommandRootSingleDevice=LogAnalysis.pathToadb+ " install -g "+apkPaths;
				}
				installAndGrantPermission(installationCommandNonRoot,count, packageName, appPathDirectory, deviceId[0]);
				
			
				installAndGrantPermission(installationCommandRoot,count, packageName, appPathDirectory, deviceId[2]);
				

				//DumpSysAnalysis.launchTheApp(packageName);
				String destinationPath=directoryPath+"/"+deviceIdSynonym[2]+".xml";
				
				//DumpSysAnalysis.launchTheApp(packageName);
				RootEmulation.launchTheApp(packageName,deviceId[2]);
				RootEmulation.dumpTheAppScreen(packageName, destinationPath, deviceId[2]);
				RootEmulation.dumpTheAppScreen(packageName, destinationPath, deviceId[2]);


				
				installAndGrantPermission(installationCommandNonRoot,count, packageName, appPathDirectory,deviceId[0]);
				
				destinationPath=directoryPath+"/"+deviceIdSynonym[0]+".xml";
				
				RootEmulation.launchTheApp(packageName,deviceId[0]);
				RootEmulation.dumpTheAppScreen(packageName, destinationPath, deviceId[0]);
				RootEmulation.dumpTheAppScreen(packageName, destinationPath, deviceId[0]);
				
				
				
			//	RootEmulation.dumpTheAppScreen(packageName, destinationPath, deviceId[2]);
			
				uninstallApp(packageName,deviceId[2]);

				uninstallApp(packageName,deviceId[0]);
			//	break;
				
			}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}


		}
	}

	public static void installAndGrantPermission(String installationCommand, int count, String packageName, String appPathDirectory, String deviceId ) throws Exception {
		// TODO Auto-generated method stub
		CommandExecute.commandExecution(installationCommand);
		if(count>1)
			fetchPermissionRequested.grantPermissions(packageName, appPathDirectory+"base.apk", deviceId);
	}

	private static void uninstallApp(String packageName, String deviceId) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		CommandExecute.commandExecution(LogAnalysis.pathToadb+" -s "+ deviceId+ " uninstall "+packageName);
		
	}

}
