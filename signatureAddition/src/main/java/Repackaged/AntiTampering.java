package Repackaged;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Scanner;

import Logs.LogAnalysis;
import ProcessMission.FetchProcessIdDumpsys;
import analysingDumpSys.DumpSysAnalysis;
import parseXml.Main;
import signatureAddition.CommandExecute;
import signatureAddition.ModifiedApkRun;
import signatureAddition.RootEmulation;
import signatureAddition.StartingPoint;
import signatureAddition.fetchPermissionRequested;
import signatureAddition.pastHardwork.GettingConstantDumps;
import signatureAddition.pastHardwork.HelloUiAutomator;

/**
 * So, this class takes the package name as an input and installs the original app if the apk is present in the local machine. Handles split-apks scenarios. This will also grant the permissions and launches the app.
 * Currently, we are trying to find the bug in the uiautomator part, so we are taking the dump two times for a single installation and then we will compare the resource id.
 * @author nikhil
 *
 */
public class AntiTampering {
	public  static String pathToLS="/bin/ls";
	public  static String readlink="/bin/readlink";
	
	public static String deviceId[]={"14011JEC202909", "emulator-5554", "0248f4221b4ca0ee"};  //"93d6906c",
	public static String deviceIdSynonym[]={"real", "emulator","rooted","repackaged"};


	public static void main(String[] args) throws Exception {

		String pathToApk="/home/nikhil/Documents/apps/dataset/packageNames_2.txt";
		File file=new File(pathToApk);
		Scanner scanner=new Scanner(file);
		String packageName="";
		
		lockUnlockPhone("1995", deviceId[0]);
		
	//	lockUnlockPhone("1234", deviceId[2]);
		
		int count1=0;
		
		while(scanner.hasNext())
		{
			try
			{
				
				packageName=scanner.next();
			//	packageName="com.icicibank.iMcanada";
				//packageName="com.icicibank.iMcanada";
			//	packageName="com.phonepe.app";
				count1++;
				System.out.println(count1);
			//	packageName="com.suryodaybank.mobilebanking";
			//	packageName="net.one97.paytm";
				
				//CommandExecute.commandExecution(LogAnalysis.pathToadb+" uninstall "+packageName);

				String logPathOriginal="/home/nikhil/Documents/apps/logs/"+packageName+"/original.txt";

				String logPathRepackaged="/home/nikhil/Documents/apps/logs/"+packageName+"/repackaged.txt";
				CommandExecute.commandExecution("mkdir /home/nikhil/Documents/apps/logs/"+packageName);

				String dumpPathDirectory="/home/nikhil/Documents/apps/uiautomator/rootEmulator/"+packageName;

				RootEmulation.createDirectory(dumpPathDirectory);
				String originalApkDirectory="/home/nikhil/Documents/apps/dataset/"+packageName+"/";

				Process process=CommandExecute.commandExecution(AntiTampering.pathToLS+" "+originalApkDirectory);
				
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
				takeDumpOnDevice(deviceId[0], deviceIdSynonym[0],installationCommandSingleDevice,count,packageName,dumpPathDirectory,originalApkDirectory);
				
				String pidOriginal=FetchProcessIdDumpsys.retrievePID(packageName);
				
				LogAnalysis.usingLogcat(packageName, pidOriginal, logPathOriginal);
				
				
				uninstallApp(packageName,deviceId[0]);
				
				
				
				//takeDumpOnDevice(deviceId[2], "real_2",installationCommandNonRoot,count,packageName,dumpPathDirectory,appPathDirectory);
				
				//Create the repackaged version and then generate the installation Command
				
				String outputDirectoryPath="/home/nikhil/Documents/apps/"+packageName+"/";
				CommandExecute.commandExecution("mkdir "+outputDirectoryPath);

				String baseOriginalApkPath=originalApkDirectory+"base.apk";
				CommandExecute.commandExecution("cp "+ baseOriginalApkPath + " "+outputDirectoryPath);
				
				String repackagedApkPath=outputDirectoryPath+"base.apk";
				StartingPoint.signApk(packageName,repackagedApkPath);
				
				
				if(count==1)
				{
					installationCommandSingleDevice=LogAnalysis.pathToadb+ " install -g "+repackagedApkPath;
				}
				else
				{
					installationCommandSingleDevice=ModifiedApkRun.generateRepackagedApk(packageName, repackagedApkPath);
				}
				takeDumpOnDevice(deviceId[0], deviceIdSynonym[3],installationCommandSingleDevice,count,packageName,dumpPathDirectory,originalApkDirectory);
			
				
				String pidRepackaged=FetchProcessIdDumpsys.retrievePID(packageName);
				
				LogAnalysis.usingLogcat(packageName, pidRepackaged, logPathRepackaged);
				
				uninstallApp(packageName,deviceId[0]);
				
				boolean resultsFromLogs=isDifferentLogs(packageName,pidOriginal,pidRepackaged,logPathOriginal,logPathRepackaged);
				
				CommandExecute.commandExecution("rm -r "+outputDirectoryPath);
				
				//break;
				
			}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}


		}
	}

	public static boolean isDifferentLogs(String packageName, String pidOriginal, String pidRepackaged,
			String logPathOriginal, String logPathRepackaged) throws SQLException, IOException {
		
		if(pidOriginal!=null && pidRepackaged==null)
		{
		
			Main.updateTable(packageName, 'Y', "App crashed Log Analysis", "AntiTampering_Automation");
			return true;
		}
		
		if 	(LogAnalysis.differenceActiviyNameLogs(packageName, logPathOriginal, logPathRepackaged))
		{
			Main.updateTable(packageName, 'Y', "Difference Activity Observed Log Analysis", "AntiTampering_Automation");
			return true;
		}
			
		
		if (LogAnalysis.checkDifferenceToastLogs(packageName, logPathOriginal, logPathRepackaged))
		{
			Main.updateTable(packageName, 'Y', "Difference Toast Message observed Log Analysis", "AntiTampering_Automation");
			return true;
		}
			
		return false;
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
		
		String clearLogcat=LogAnalysis.pathToadb+" shell logcat -b all -c";



		//CommandExecute.commandExecution(pathToadb+" install "+pathToApk);

		CommandExecute.commandExecution(clearLogcat);
		
		
		RootEmulation.launchTheApp(packageName,deviceId);
		
		GettingConstantDumps.main(packageName, destinationPath, deviceId);
		//DumpUIAutomatorPython.main(destinationPath,deviceId);
		
			
	//	uninstallApp(packageName,deviceId);

		
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
