package signatureAddition;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import InstallerVerification.InstallerVerificationFrontEnd;
import InstallerVerification.fetchPermissionRequested;
import Logs.LogAnalysis;
import analysingDumpSys.DumpSysAnalysis;
/**
 * This class takes the package name as an input and handle the split-apks scenarios and install the app, launch it and takes the dump using adb shell uiautomator.
 * @author nikhil
 *
 */
public class AppInstallationAndDump {

	public static String main(String packageName) throws Exception {
		// TODO Auto-generated method stub
		/**
		 * Considering the app has been installed. The package Name should be the arguement. 
		 */
		String directoryPath="";
		String uiDump_adb_Sideload_Path="";
		try {
			//String packageName="com.freecharge.android";
			String command1=LogAnalysis.pathToadb+" shell pm path "+packageName;
			Process process=CommandExecute.commandExecution(command1);
			BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(process.getInputStream()));
			String apkPath=bufferedReader.readLine();
			if(apkPath==null|| apkPath.length()==0)
			{
				InstallerVerificationFrontEnd.updateDatabaseByPassable(packageName, 'E', "App is not currently installed on the device");
				throw new Exception("App did not install");	
			}
			int count=0;
			/**
			 * Command to create a directory that contains the app 
			 */
			String baseApkPath=apkPath.substring(8);
			directoryPath="/home/nikhil/Documents/apps/InstallerVerification/"+packageName;
			CommandExecute.commandExecution("mkdir "+directoryPath);
			String apksPath="";
			while(apkPath!=null)
			{
				System.out.println(apkPath);
				count++;
				apksPath=apksPath+parseToFetchApk(apkPath,directoryPath)+" ";
				apkPath=bufferedReader.readLine();
			}
			CommandExecute.commandExecution(LogAnalysis.pathToadb+" uninstall "+packageName);
			if(count==1)
			{
				/**
				 * Install command
				 */
				String installCommand=LogAnalysis.pathToadb+" install -g "+apksPath;
				System.out.println(installCommand);
				CommandExecute.commandExecution(installCommand);
			}
			else
			{
				/**
				 * App has split-apks
				 */
				String installCommand=LogAnalysis.pathToadb+" install-multiple -g "+apksPath;
				System.out.println(installCommand);
				CommandExecute.commandExecution(installCommand);
				String pathToApk="/home/nikhil/Downloads/googleplay-api-master/"+packageName+".apk";

				fetchPermissionRequested.grantPermissions(packageName, pathToApk);
			}
			/**
			 * Launch the app and capture the screen
			 */

			DumpSysAnalysis.launchTheApp(packageName);


			uiDump_adb_Sideload_Path="/home/nikhil/Documents/apps/uiautomator/InstallerVerification/"+packageName+"_original_sidedload.xml";
			uiautomator.Main.dumpScreenXml(uiDump_adb_Sideload_Path,packageName);
			uiautomator.Main.dumpScreenXml(uiDump_adb_Sideload_Path,packageName);	
			
			/**
			 * return uiDump_orignal2Path;
			 */

		}
		catch (Exception e) {
			
			e.printStackTrace();
		}
		finally {
			CommandExecute.commandExecution("rm -r "+directoryPath);
		}
		return uiDump_adb_Sideload_Path;
	}

	public static String parseToFetchApk(String apkPath, String directoryPath) throws IOException, InterruptedException {

		String pattern="package:";
		String pathToApk=apkPath.substring(pattern.length());
		String apkName=apkPath.substring(apkPath.lastIndexOf('/')+1);
		String command=LogAnalysis.pathToadb+" pull "+pathToApk+" "+directoryPath+"/"+apkName;
		System.out.println(command);
		CommandExecute.commandExecution(command);
		String apkLocalPath=directoryPath+"/"+apkName;
		return apkLocalPath;
	}

}
