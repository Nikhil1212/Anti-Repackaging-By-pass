package signatureAddition;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import Logs.LogAnalysis;
import analysingDumpSys.DumpSysAnalysis;
import signatureAddition.pastHardwork.HelloUiAutomator;

/**
 * So, this class takes the package name as an input and installs the original app if the apk is present in the local machine. Handles split-apks scenarios. This will also grant the permissions and launches the app.
 * Currently, we are trying to find the bug in the uiautomator part, so we are taking the dump two times for a single installation and then we will compare the resource id.
 * @author nikhil
 *
 */
public class AppLaunchAndDump {
	public  static String pathToLS="/bin/ls";
	public  static String readlink="/bin/readlink";
	
	public static String deviceId[]={"14011JEC202909", "emulator-5554", "0248f4221b4ca0ee","ab06bf54"};  //"93d6906c",
	public static String deviceIdSynonym[]={"real", "emulator","rooted","repackaged","modifiedRepackaged","modifiedEmulator","modifiedRoot","modifiedHooking"};
	

	public static void main(String[] args) throws Exception {

		String pathToApk="/home/nikhil/Documents/apps/FridaReRun.txt";
		File file=new File(pathToApk);
		Scanner scanner=new Scanner(file);
		String packageName="";
		
		//lockUnlockPhone("1995", deviceId[0]);
		
		//lockUnlockPhone("1234", deviceId[2]);
		
		int count1=0;
		
		while(scanner.hasNext())
		{
			try
			{
				

				packageName=scanner.next();
			//	packageName="com.ibank.icici_secure_app";
				//packageName="com.csam.icici.bank.mimobile";
				//packageName="com.icicibank.iMcanada";
				count1++;
				System.out.println(count1);
			//	packageName="com.suryodaybank.mobilebanking";
			//	packageName="net.one97.paytm";
				
				//CommandExecute.commandExecution(LogAnalysis.pathToadb+" uninstall "+packageName);

					
				String dumpPathDirectory="/home/nikhil/Documents/apps/uiautomator/rootEmulator/"+packageName;

				RootEmulation.createDirectory(dumpPathDirectory);
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
				String installationCommandSingleDevice="";
				if(count>1)
				{
					System.out.println("It is a split apk scenario");
					installationCommandNonRoot=LogAnalysis.pathToadb+" -s "+ deviceId[0]+" install-multiple -g "+ apkPaths;
					installationCommandRoot=LogAnalysis.pathToadb+" -s "+ deviceId[2]+" install-multiple -g "+ apkPaths;
					installationCommandEmulator=LogAnalysis.pathToadb+" -e install-multiple -g "+apkPaths;
					installationCommandSingleDevice=LogAnalysis.pathToadb+" install-multiple -g "+ apkPaths;
				}
				else
				{
					installationCommandNonRoot=LogAnalysis.pathToadb+" -s "+ deviceId[0]+ " install -g "+apkPaths;
					installationCommandRoot=LogAnalysis.pathToadb+" -s "+ deviceId[2]+ " install -g "+apkPaths;
					installationCommandEmulator=LogAnalysis.pathToadb+" -e install -g "+apkPaths;
					installationCommandSingleDevice=LogAnalysis.pathToadb+ " install -g "+apkPaths;
				}
				takeDumpOnDevice(deviceId[2], deviceIdSynonym[2],installationCommandSingleDevice,count,packageName,dumpPathDirectory,appPathDirectory);
				//takeDumpOnDevice(deviceId[2], "real_2",installationCommandNonRoot,count,packageName,dumpPathDirectory,appPathDirectory);
				
				//takeDumpOnDevice(deviceId[1], deviceIdSynonym[1],installationCommandEmulator,count,packageName,dumpPathDirectory,appPathDirectory);
				
				
			//	break;
				
			}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}


		}
	}

	public static void lockUnlockPhone(String password, String deviceId) throws IOException, InterruptedException {

	CommandExecute.commandExecution(LogAnalysis.pathToadb+" -s "+deviceId +" shell input keyevent 26"); //Power button (locked)
	CommandExecute.commandExecution(LogAnalysis.pathToadb+" -s "+deviceId +" shell input keyevent 26"); //Power button (again pressed) to see the screen.
	
	CommandExecute.commandExecution(LogAnalysis.pathToadb+ " -s "+deviceId +" shell input swipe 200 900 200 300");
	CommandExecute.commandExecution(LogAnalysis.pathToadb+" -s "+deviceId +" shell input text "+password); //Password
	CommandExecute.commandExecution(LogAnalysis.pathToadb+ " -s "+deviceId + " shell input keyevent 66");  //Enter Key


	}

	public static void takeDumpOnDevice(String deviceId, String deviceSynonym, String installationCommand, int count,
			String packageName, String directoryPath, String appPathDirectory) throws Exception {
		String destinationPath="";
		//RootEmulation.dumpTheAppScreen(packageName, destinationPath2, deviceId[2]);
		uninstallApp(packageName, deviceId);
		
		
		installAndGrantPermission(installationCommand, count, packageName, appPathDirectory, deviceId);
		
	
		

		//DumpSysAnalysis.launchTheApp(packageName);
		//destinationPath=directoryPath+"/"+deviceSynonym+"_python.xml";
		
		destinationPath=directoryPath+"/"+deviceSynonym+"_BuiltIn.xml";
		
		//DumpSysAnalysis.launchTheApp(packageName);
		RootEmulation.launchTheApp(packageName,deviceId);
		
		GettingConstantDumps.main(packageName, destinationPath, deviceId);
		//DumpUIAutomatorPython.main(destinationPath,deviceId);
		
			
		uninstallApp(packageName,deviceId);

		
	}

	public static void installAndGrantPermission(String installationCommand, int count, String packageName, String appPathDirectory, String deviceId ) throws Exception {
		// TODO Auto-generated method stub
		CommandExecute.commandExecution(installationCommand);
		if(count>1)
			fetchPermissionRequested.grantPermissions(packageName, appPathDirectory+"/base.apk", deviceId);
	}

	private static void uninstallApp(String packageName, String deviceId) throws IOException, InterruptedException {
	
		
		CommandExecute.commandExecution(LogAnalysis.pathToadb+" -s "+ deviceId+ " uninstall "+packageName);
		
	}

}
