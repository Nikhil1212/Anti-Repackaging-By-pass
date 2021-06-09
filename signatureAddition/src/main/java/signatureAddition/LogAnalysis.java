package signatureAddition;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class LogAnalysis {
	static 	String pathToadb="/home/nikhil/Android/Sdk/platform-tools/adb";
	static 	String pathToaapt="/home/nikhil/Android/Sdk/build-tools/27.0.3/aapt";

	public static void main(String[] args) throws IOException, InterruptedException, SQLException {

		String FilePath="/home/nikhil/Documents/apks/pathToApps_3.txt";
		File file=new File(FilePath);
		Scanner scanner=new Scanner(file);
		while(scanner.hasNext())
		{
			String pathToOriginalApk=scanner.next();
			String packageName=StartingPoint.getPackageName(pathToOriginalApk);
			String pathToResignedApk="/home/nikhil/Documents/apps/ReSignedApks/"+packageName+".apk";

			
			/**
			 * Creating resigned version
			 */
			
			resignedApp.signApk(packageName, pathToOriginalApk, pathToResignedApk);

			//String pathToModifiedApk="/home/nikhil/Documents/apps/ModifiedApks/"+packageName+".apk";

			String pathToModifiedApk=generatingModifiedApk(packageName,pathToOriginalApk);
			String logPathForOriginalApp="/home/nikhil/Documents/apps/logOutput/original_"+packageName+".txt";
			String logPathForResignedApp="/home/nikhil/Documents/apps/logOutput/resigned_"+packageName+".txt";
			String logPathForModifiedApp="/home/nikhil/Documents/apps/logOutput/modifed_"+packageName+".txt";


			int originalCount=apkLogAnalysis(pathToOriginalApk,logPathForOriginalApp);
			int resignedCount=apkLogAnalysis(pathToResignedApk,logPathForResignedApp);
			int modifedCount=apkLogAnalysis(pathToModifiedApk,logPathForModifiedApp);

			System.out.println("original count: "+originalCount);
			System.out.println("resigned count: "+resignedCount);
			System.out.println("modified count: "+modifedCount);

			if(originalCount==resignedCount)
				System.out.println(packageName+" seems to launch in the same way as the original version. So, there is a chance that no anti-repackaging check is present");
			else
			{
				System.out.println(packageName+" seems to have anti-repackaging check present");
				if(originalCount==modifedCount)
				{
					System.out.println(packageName+" For this app, we are successful in by-passing the check");
				}
				else
					System.out.println(packageName+": We are not successful in by-passing the anti-repackaging check !!");	
			}

			/**
			 * Let's update the table with the values fetched
			 */
			updateTable(packageName,originalCount,resignedCount,modifedCount);

		}

	}

	private static String generatingModifiedApk(String packageName, String pathToOriginalApk) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		String pathToDisAssembleCode="/home/nikhil/Documents/apps/"+packageName;

		System.out.println(packageName);

		//package name is retrieved using aapt 
		StartingPoint.disassembleApk(pathToOriginalApk,packageName);

		//Successfully disassmeble the apk with ignoring resource
		String fullRSAfetch= StartingPoint.fetchRSAName(packageName);

		String signCertificateKey=fetchCertificateKey.getCertificateInHex(fullRSAfetch, packageName);
		System.out.println(signCertificateKey);

		FileNamesForSignatureAddition.codeInjectionProcess(signCertificateKey, pathToDisAssembleCode);
		//fetchCertificateKey.codeInjection();
		String modifiedApkPath=StartingPoint.buildApk(packageName);
		StartingPoint.signApk(packageName, modifiedApkPath);//, modifiedApkPath);
		//signApk(packageName, modifiedApkPath);

		StartingPoint.removeDirectory(pathToDisAssembleCode);
		return modifiedApkPath;
	}

	private static void updateTable(String packageName, int originalCount, int resignedCount, int modifedCount) throws SQLException {
		// TODO Auto-generated method stub
		Statement statement=DataBaseConnect.initialization();

		String query="Insert ignore into ActivityTaskManagerCount values ('"+packageName+"',"+originalCount+","+resignedCount+","+modifedCount+");";
		statement.executeUpdate(query);
	}

	private static int apkLogAnalysis(String pathToApkFromPC, String logPathForOutput) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		System.out.println("let's try to fetch the pid of an app from its packagename");
		String directoryLocationForStoringLogs="/home/nikhil/Documents/logs/";

		String devices=pathToadb+" devices";
		CommandExecute.commandExecution(devices);


		//String pathToApkFromPC="/home/nikhil/Documents/apks/repackagedAprb.apk";

		String packageName=StartingPoint.getPackageName(pathToApkFromPC);

		CommandExecute.commandExecution(pathToadb+" uninstall "+packageName);

		/**
		 * If the app is already being used in the smartphone
		 */

		String apkPathOnSmartphone=" /data/local/tmp/"+packageName+".apk";

		String pushApkCommand=pathToadb+" push "+pathToApkFromPC+apkPathOnSmartphone;
		String installThroughPMCommand=pathToadb+" shell pm install -g"+apkPathOnSmartphone;
		String removeApkFromDevice=pathToadb+" shell rm"+apkPathOnSmartphone;

		CommandExecute.commandExecution(pushApkCommand);

		CommandExecute.commandExecution(installThroughPMCommand);
		/**
		 * We are using pm as we want to give all the permission an app wants during the installation time
		 */

		CommandExecute.commandExecution(removeApkFromDevice);
		/**
		 * As the apk has been installed, so no use of this apk
		 */
		//Process pr=CommandExecute.commandExecution(commandToFilterLogsUsingPackageName);


		/**
		 * Let's uninstall the app
		 */

		String clearLogcat=pathToadb+" shell logcat -c";



		//CommandExecute.commandExecution(pathToadb+" install "+pathToApk);

		CommandExecute.commandExecution(clearLogcat);

		launchTheApp(packageName,pathToApkFromPC);
		/**
		 * The second arguement required is to fetch the launcher activity of the app
		 */

		String command=pathToadb+" shell pidof "+packageName;
		Process process=CommandExecute.commandExecution(command);

		BufferedReader buf = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line = "";
		line=buf.readLine();
		if(line==null)
		{
			System.out.println("The app is currently not running");
		}
		else
		{
			//as in the first line only we can get the package name.That's why immeditate break;
			String pid=line;
			System.out.println("pid of the app with package name "+packageName+" is "+line);

			//System.out.println(analysingLogsUsingPID);


			storingLogOutputUsingGrepPackageName(packageName);
			storingLogOutputUsingPID(packageName,pid);

			String filePath1=directoryLocationForStoringLogs+"logs_"+packageName+"_PID.txt";
			String filePath2="/home/nikhil/Documents/logs/fromProgram"+packageName+".txt";


			String str1=new String(Files.readAllBytes(Paths.get(filePath1)));

			String str2=new String(Files.readAllBytes(Paths.get(filePath2)));;
			String str3=str1+str2;
			//String outputFilePath="/home/nikhil/Documents/logs/universal_"+packageName+".txt";
			FileWriter fileWriter=new FileWriter(logPathForOutput);
			fileWriter.write(str3);
			fileWriter.close();
			System.out.println("Command executed successfully");


			String removeFile1="rm "+filePath1;
			String removeFile2="rm "+filePath2;

			/*
			 * Removing the files
			 */
			CommandExecute.commandExecution(removeFile1);
			CommandExecute.commandExecution(removeFile2);
			CommandExecute.commandExecution(pathToadb+" uninstall "+packageName);
			CommandExecute.commandExecution(clearLogcat);


			return fetchActivityTaskManagerCount(logPathForOutput);
			/*
			File file2=new File(directoryLocationForStoringLogs+"_.txt");
			file2.createNewFile();*/
			//so let's remove the two files 


		}
		return -1;
	}

	private static int fetchActivityTaskManagerCount(String outputFilePath) throws IOException, InterruptedException {

		System.out.println("Output file path is :"+outputFilePath);
		int count=0;
		File file=new File(outputFilePath);
		Scanner scanner=new Scanner(file);
		//int count=0;
		while(scanner.hasNext())
		{
			String line=scanner.next();
			if(line.contains("ActivityTaskManager"))
			{
				count++;
			}
		}
		return count;
		//String grepCommand="/bin/grep -o -i ActivityTaskManager "+outputFilePath+" | /usr/bin/wc -l";
		/*String grepCommand="grep -wc \"ActivityTaskManager\" "+ outputFilePath;

		System.out.println(grepCommand);
		String str1=new String(Files.readAllBytes(Paths.get(outputFilePath)));

		Process process=CommandExecute.commandExecution(grepCommand);

		BufferedReader buf2 = new BufferedReader(new InputStreamReader(process.getInputStream()));
			System.out.println("Before while loop");
			String line=buf2.readLine();
			System.out.println("Number of ActivityTaskManager :"+line);
			if(line==null)
			{
				System.out.println("value returned is null");
				return -1;
			}
		 */	
		//return Integer.parseInt(line);

	}

	private static void launchTheApp(String packageName, String pathToApk) throws IOException, InterruptedException {
		// TODO Auto-generated method stub

		String fetchLauncherActivity= pathToadb+" shell \"cmd package resolve-activity --brief ";	//packageName | tail -n 1\";
		fetchLauncherActivity=fetchLauncherActivity + packageName +" | tail -n 1\"";
		// strcat (fetchLauncherActivity, " | tail -n 1\"");
		System.out.println(fetchLauncherActivity);
		//String pathToApk=null;
		String fetchOutputOfAapt=pathToaapt+" dump badging "+pathToApk;
		Process  process=CommandExecute.commandExecution(fetchOutputOfAapt);
		String line="";
		String patternForLaunchableActivity="launchable-activity: name='";
		String launchableActivityCommand=pathToadb+" shell am start -n "+packageName+"/";
		BufferedReader buf2 = new BufferedReader(new InputStreamReader(process.getInputStream()));
		//BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
		System.out.println("Before while loop");
		while ((line=buf2.readLine())!=null) {
			if(line.contains(patternForLaunchableActivity))
			{
				//fetch the launcher activity name
				String temp=line.substring(patternForLaunchableActivity.length());
				//we are trimming it.
				int index=temp.indexOf("'");
				launchableActivityCommand=launchableActivityCommand.concat(temp.substring(0,index));
				System.out.println(launchableActivityCommand);
				CommandExecute.commandExecution(launchableActivityCommand);
				break;
			}
			else 
				continue;
			//as in the first line only we can get the package name.That's why immeditate break;
			//	Files.write(Paths.get(filePath), (line+"\n").getBytes(),  StandardOpenOption.APPEND);
		}
		buf2.close();

		Thread.sleep(10000);
	}

	private static void storingLogOutputUsingPID(String packageName, String pid) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		System.out.println("PID of the app is : "+pid);
		String directoryLocationForStoringLogs="/home/nikhil/Documents/logs/";

		String phoneDirectory_1="/data/local/tmp/fromProgrampid.txt";

		String analysingLogsUsingPID=pathToadb+" shell logcat --pid "+pid+" -d";// > "+phoneDirectory_1;


		String filePath=directoryLocationForStoringLogs+"logs_"+packageName+"_PID.txt";
		File file=new File(filePath);
		file.createNewFile();

		//CommandExecute.commandExecution(clearLogcat);
		System.out.println(analysingLogsUsingPID);
		Process process2=CommandExecute.commandExecution(analysingLogsUsingPID);

		/**
		 * Let's pull this file 
		 */
		String pullCommand=pathToadb+" pull "+phoneDirectory_1+" "+filePath;
		//Process process=CommandExecute.commandExecution(pullCommand);


		BufferedReader buf1 = new BufferedReader(new InputStreamReader(process2.getInputStream()));
		//BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
		String line="";

		while ((line=buf1.readLine())!=null) {
			//as in the first line only we can get the package name.That's why immeditate break;
			Files.write(Paths.get(filePath), (line+"\n").getBytes(),  StandardOpenOption.APPEND);
			System.out.println(line);
		}
		buf1.close();

	}

	private static void storingLogOutputUsingGrepPackageName(String packageName) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		String phoneDirectory="/data/local/tmp/fromProgramPackageName.txt";
		String commandToFilterLogsUsingPackageName=pathToadb+" shell logcat -d | grep "+packageName+" > "+phoneDirectory;

		System.out.println(commandToFilterLogsUsingPackageName);

		Process process3=CommandExecute.commandExecution(commandToFilterLogsUsingPackageName);

		String deviceDirectory="/home/nikhil/Documents/logs/fromProgram"+packageName+".txt";
		String pullFromPhoneToPC=pathToadb+" pull "+phoneDirectory+" "+deviceDirectory;
		CommandExecute.commandExecution(pullFromPhoneToPC);

	}
	private static String fetchPackageNamefromapkPath(String apkPath) {

		String packageName="";
		int len=apkPath.length();
		/**
		 * fetch the position of the last / or \
		 */
		int index=apkPath.lastIndexOf('/');
		if(index==-1)
		{
			index=apkPath.lastIndexOf('\\');
		}
		packageName=apkPath.substring(index+1, len-4);
		return packageName;
	}


}
