package finalRun;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Scanner;

import Logs.LogAnalysis;
import ProcessMission.FetchProcessIdDumpsys;
import parseXml.Main;
import signatureAddition.*;
import signatureAddition.PullApps.AppsPull;


/**
 * So, this class takes the package name as an input and installs the original app if the apk is present in the local machine. Handles split-apks scenarios. This will also grant the permissions and launches the app.
 * Currently, we are trying to find the bug in the uiautomator part, so we are taking the dump two times for a single installation and then we will compare the resource id.
 * @author nikhil
 *
 */
public class AntiEmulation {
	public  static String pathToLS="/bin/ls";
	public  static String readlink="/bin/readlink";
	
	public static String deviceId[]={"14011JEC202909", "emulator-5554", "0248f4221b4ca0ee"};  //"93d6906c",
	public static String deviceIdSynonym[]={"real", "emulator","rooted","repackaged","modified"};


	public static void main(String[] args) throws Exception {

		String pathToApk="/home/nikhil/Documents/apps/AntiEmulatorReRun.txt";
		File file=new File(pathToApk);
		Scanner scanner=new Scanner(file);
		String packageName="";
		
	//	lockUnlockPhone("1995", deviceId[2]);
		
	//	lockUnlockPhone("1234", deviceId[2]);
		
		int count1=0;
		
		while(scanner.hasNext())
		{
			try
			{
				
				packageName=scanner.next();
				//packageName="com.phonepe.app";
				count1++;
				System.out.println("Package Number count  :"+count1);
			
				
				uninstallApp(packageName, deviceId[1]);
				String logPathEmulator="/home/nikhil/Documents/apps/logs/"+packageName+"/emulator.txt";

				
				CommandExecute.commandExecution("mkdir /home/nikhil/Documents/apps/logs/"+packageName);

				String dumpPathDirectory="/home/nikhil/Documents/apps/uiautomator/rootEmulator/"+packageName;

				RootEmulation.createDirectory(dumpPathDirectory);
				String originalApkDirectory=AppsPull.appDirectoryPrefix+packageName+"/";

				Process process=CommandExecute.commandExecutionSh(AntiEmulation.pathToLS+" "+originalApkDirectory);
				
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
				String installationCommandEmulator="";
				if(count>1)
				{
					System.out.println("It is a split apk scenario");
					installationCommandEmulator=LogAnalysis.pathToadb+" -e install-multiple -g "+ apkPaths;
				}
				else
				{
					installationCommandEmulator=LogAnalysis.pathToadb+ " -e install -g "+apkPaths;
				}
				takeDumpOnDevice(deviceId[1], deviceIdSynonym[1],installationCommandEmulator,count,packageName,dumpPathDirectory,originalApkDirectory);
				
				String pidOriginal=FetchProcessIdDumpsys.retrievePID(packageName,deviceId[1]);
				
				LogAnalysis.usingLogcat(packageName, pidOriginal, logPathEmulator, deviceId[1]);
				
				
				uninstallApp(packageName,deviceId[1]);
				
				//break;
				
				//takeDumpOnDevice(deviceId[2], "real_2",installationCommandNonRoot,count,packageName,dumpPathDirectory,appPathDirectory);
				
				//Create the repackaged version and then generate the installation Command
				
				

			}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}


		}
	}

	private static void updateByPassInfo(String packageName, boolean differentLogs, String pattern) throws SQLException {
		// TODO Auto-generated method stub
		if(differentLogs)
		{
			//Not able to by-pass the check
			Main.updateTable(packageName, 'N', "Log Analysis", "ByPass_AntiTampering_Automation");
			return;
			
		}
		if(pattern!=null)
		{
			//Dump is different
			Main.updateTable(packageName, 'N', pattern, "ByPass_AntiTampering_Automation");
		}
		else
			Main.updateTable(packageName, 'Y', "Able to locate the check", "ByPass_AntiTampering_Automation");
	
	}

	public static void updateTableDesiredWay(String packageName, String val, boolean differentLogs) throws SQLException {
		// TODO Auto-generated method stub
		if(differentLogs)
		{
			Main.updateTable(packageName, 'Y', "Log Analysis", "AntiTampering_Automation");
				
		}
		else if (val!=null)
		{
			Main.updateTable(packageName, 'Y', val, "AntiTampering_Automation");
			
		}
	}

	public static String  isDumpDifferent(String packageName, String dumpPathDirectory, String appS1, String appS2) throws Exception {
		// TODO Auto-generated method stub
		String	realPath=dumpPathDirectory+"/"+appS1+"_BuiltIn.xml";
		
		String	repackagedPath=dumpPathDirectory+"/"+appS2+"_BuiltIn.xml";
		String pattern[]= {"resource-id","text","class","content-desc"};
		
		HashSet hashSetOriginal[]= {new HashSet<String>(),new HashSet<String>(),new HashSet<String>(),new HashSet<String>()};
		
		HashSet hashSetRepackaged[]= {new HashSet<String>(),new HashSet<String>(),new HashSet<String>(),new HashSet<String>()};
	
		for(int i=0;i<pattern.length;i++)
		{
			hashSetOriginal[i]=Main.analysePatternInXml(packageName, realPath, pattern[i]);
			hashSetRepackaged[i]=Main.analysePatternInXml(packageName, repackagedPath, pattern[i]);		
		}
		for(int i=0;i<pattern.length;i++)
		{
			if(hashSetOriginal[i].size()!=hashSetRepackaged[i].size())
			{
				Main.updateTable(packageName, 'Y', pattern[i],"AntiTampering_Automation");
				return pattern[i];
			}
		}
		return null;
		
	}

	public static String getInstallationCommand(String packageName, String apkPath, int count, String deviceId) throws Exception {

		String installationCommandSingleDevice="";
		if(count==1)
		{
			installationCommandSingleDevice=LogAnalysis.pathToadb+" -s "+deviceId +" install -g "+apkPath;
		}
		else
		{
			installationCommandSingleDevice=ModifiedApkRun.generateRepackagedApk(packageName, apkPath,deviceId);
		}	
		return installationCommandSingleDevice;
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
			String packageName, String dumpDirectoryPath, String appPathDirectory) throws Exception {
		String destinationPath="";
		//RootEmulation.dumpTheAppScreen(packageName, destinationPath2, deviceId[2]);
		uninstallApp(packageName, deviceId);
		
		
		installAndGrantPermission(installationCommand, count, packageName, appPathDirectory, deviceId);
		
	
		

		//DumpSysAnalysis.launchTheApp(packageName);
		//destinationPath=directoryPath+"/"+deviceSynonym+"_python.xml";
		
		destinationPath=dumpDirectoryPath+"/"+deviceSynonym+"_BuiltIn.xml";
		
		//DumpSysAnalysis.launchTheApp(packageName);
		
		String clearLogcat=LogAnalysis.pathToadb+" -s " +deviceId+" shell logcat -b all -c";



		//CommandExecute.commandExecution(pathToadb+" install "+pathToApk);

		CommandExecute.commandExecution(clearLogcat);
		
		
		RootEmulation.launchTheApp(packageName,deviceId);
		
		GettingConstantDumps.main(packageName, destinationPath, deviceId);
		//DumpUIAutomatorPython.main(destinationPath,deviceId);
		
			
	//	uninstallApp(packageName,deviceId);

		
	}

	public static void installAndGrantPermission(String installationCommand, int count, String packageName, String appPathDirectory, String deviceId ) throws Exception {
		// TODO Auto-generated method stub
		CommandExecute.commandExecutionSh(installationCommand);
		if(count>1)
			fetchPermissionRequested.grantPermissions(packageName, appPathDirectory+"base.apk", deviceId);
	}

	private static void uninstallApp(String packageName, String deviceId) throws IOException, InterruptedException {
	
		
		CommandExecute.commandExecution(LogAnalysis.pathToadb+" -s "+ deviceId+ " uninstall "+packageName);
		
	}

}
