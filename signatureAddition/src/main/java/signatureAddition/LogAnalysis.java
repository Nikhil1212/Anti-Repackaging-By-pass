package signatureAddition;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;

import signatureAddition.pastHardwork.AnalysingJSON;
import signatureAddition.pastHardwork.ExecutePython;
import signatureAddition.pastHardwork.printLogsThroughPID;
import signatureAddition.pastHardwork.restartSmartphone;

public class LogAnalysis {
	public static String pathToadb="/home/nikhil/Downloads/platform-tools_r30.0.5-linux/platform-tools/adb";

	//public static 	String pathToadb="/home/nikhil/Android/Sdk/platform-tools/adb";
	static 	String pathToaapt="/home/nikhil/Android/Sdk/build-tools/27.0.3/aapt";
	public static String toastKilled="Toast already killed"; 


	public static void main(String[] args) throws IOException, InterruptedException {

		String FilePath="/home/nikhil/Documents/apps/packageNames_2.txt";
		File file=new File(FilePath);
		Scanner scanner=new Scanner(file);
		int count=0;
		//ExecutePython.downloadApks(FilePath);
		while(scanner.hasNext())
		{
			try
			{

				String packageName=scanner.next();
				String pathToOriginalApk="/home/nikhil/Downloads/googleplay-api-master/"+packageName+".apk";
				String pathToResignedApk="/home/nikhil/Documents/apps/ReSignedApks/"+packageName+".apk";

				/**
				 * Creating resigned version
				 */

				resignedApp.signApk(packageName, pathToOriginalApk, pathToResignedApk);

				//String pathToModifiedApk="/home/nikhil/Documents/apps/ModifiedApks/"+packageName+".apk";

				String logPathForOriginalApp="/home/nikhil/Documents/apps/logcatOutput/original_"+packageName+".txt";
				String logPathForResignedApp="/home/nikhil/Documents/apps/logcatOutput/resigned_"+packageName+".txt";
				String logPathForModifiedApp="/home/nikhil/Documents/apps/logcatOutput/modifed_"+packageName+".txt";


				String fileContentsLogsPidOriginal=appLogGeneration(pathToOriginalApk,logPathForOriginalApp);
			//	printLogsThroughPID.initializationADB();
				restartSmartphone.restart();
				printLogsThroughPID.initializationADB();
				
				String fileContentsLogsPidRepackaged=appLogGeneration(pathToResignedApk,logPathForResignedApp);
				

				//System.out.println("Checking whether we are able to see AccountInvalidator *******************\n****************\n**************");
				//	String fileContents=new String(Files.readAllBytes(Paths.get(logPathForResignedApp)));

				boolean result=checkAntiTamperingPresence(packageName,pathToOriginalApk,logPathForOriginalApp,logPathForResignedApp,fileContentsLogsPidOriginal	,fileContentsLogsPidRepackaged);
				if(result==true)
				{
					/**
					 * The check is present, we are trying to by-pass it.
					 */
					String pathToModifiedApk=generatingModifiedApk(packageName,pathToOriginalApk);
					String fileContentsLogsPidModified=appLogGeneration(pathToModifiedApk,logPathForModifiedApp);

					boolean byPassableResult=isItByPassable(packageName,pathToOriginalApk,logPathForOriginalApp,logPathForResignedApp,fileContentsLogsPidOriginal,fileContentsLogsPidRepackaged,fileContentsLogsPidModified,logPathForModifiedApp);

					/**
					 * Update the database stating that we are successful in by-passing the check
					 */
					//	updateDatabaseByPassable(packageName,""+byPassableResult);

				}
				else
				{

					updateAntiRepackagingCheckPresence(packageName, 'N', "Could not find the check.");

					updateDatabaseByPassable(packageName, "NA","NA");
					continue;
				}
				CommandExecute.commandExecution(pathToadb+" uninstall "+packageName);

				//System.out.println(fileContents);

				printLogsThroughPID.initializationADB();
				//	System.exit(0);

				String orignalLogJSONPath=removeDuplicateLogsStatement.removeduplicateLogs(logPathForOriginalApp);
				String resignedLogJSONPath=removeDuplicateLogsStatement.removeduplicateLogs(logPathForResignedApp);
				String modifiedLogJSONPath=removeDuplicateLogsStatement.removeduplicateLogs(logPathForModifiedApp);

				AnalysingJSON.analyseJSON(orignalLogJSONPath, resignedLogJSONPath, modifiedLogJSONPath);

			}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		}

	}

	public static void executeScrcpy() throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		CommandExecute.commandExecution("/snap/bin/scrcpy");
	}

	public static void updateDatabaseByPassable(String packageName, String result, String observation) throws SQLException {
		// TODO Auto-generated method stub
		String checkQuery="Select * from ByPassableInfo where packageName='"+packageName+"';";
		System.out.println(checkQuery);
		Statement statement1=DataBaseConnect.initialization();
		ResultSet  resultSet=statement1.executeQuery(checkQuery);
		int flag=0;
		while(resultSet.next())
		{
			flag=1;
			resultSet.getString(1);			
		}
		if(flag==0)
		{
			String query="Insert into ByPassableInfo values ('"+packageName+"','"+result+"','"+observation+"');";
			System.out.println(query);

			Statement statement=DataBaseConnect.initialization();
			statement.executeUpdate(query);
		}
		else
		{
			String query="Update ByPassableInfo set isByPassable ='"+result+"'where packageName='"+packageName+"';";
			System.out.println(query);

			Statement statement=DataBaseConnect.initialization();
			statement.executeUpdate(query);
		}

	}

	public static boolean isItByPassable(String packageName, String pathToOriginalApk, String logPathForOriginalApp,
			String logPathForResignedApp, String fileContentsLogsPidOriginal, String fileContentsLogsPidRepackaged,
			String fileContentsLogsPidModified, String logPathForModifiedApp) throws Exception {
		String observation=queryToDataBase(packageName);

		/**
		 * Let's try to find the reasons for the differnet behaviour of the apps in the 
		 * original and the resigned
		 */
		if(observation.contains("Activity"))
		{
			/**
			 * It means that there is different set of activities observed	for original and resigned app.
			 */
			boolean result=differenceActiviyNameLogs(packageName, logPathForOriginalApp, logPathForModifiedApp);
			updateDatabaseByPassable(packageName, ""+(!result),observation);
			if(result==false)
			{
				/*
				 * We have successful in by-passing the check
				 */
				System.out.println("Yes, for the packagename: "+packageName+", we are successful in by-passing the check");
			}
			else
			{
				System.out.println("Still there is a difference in the activities observed :"+packageName);
			}
		}
		else if (observation.contains("Toast"))
		{
			/**
			 * It means that on the resigned version, we are seeing the toast message 
			 * but not on the original app
			 */
			boolean result=checkDifferenceToastLogs(packageName,logPathForModifiedApp, logPathForResignedApp );
			updateDatabaseByPassable(packageName, ""+(result),observation);
			if(result==false)
			{
				System.out.println("Still we are seeing the toast message on the modified version also");
			}
			else
				System.out.println("Yes, we have by-passed :"+packageName);
		}
		else if (observation.contains("Crashed"))
		{
			/**
			 * The app is getting crashed on the repackaged version
			 */
			boolean result=methodAppCrash(packageName);
			updateDatabaseByPassable(packageName, ""+(!result),observation);

			if(result)
			{
				System.out.println("Still the app is getting crashed !!"+packageName);
			}
			else
				System.out.println("Now the app is not getting crashed :"+packageName);

		}
		else if (observation.contains("Tag Difference")) 
		{


			System.out.println("Well, we need to look into it for this aspect!!");

			String filePidOriginalPath="/home/nikhil/Documents/logs/"+packageName+"_original.txt";
			String filePidModifiedPath="/home/nikhil/Documents/logs/"+packageName+"_modified.txt";
			writeToFile(filePidOriginalPath,filePidModifiedPath,fileContentsLogsPidOriginal,fileContentsLogsPidModified);

			int flag=1;
			HashSet<String> disjointTagsOriginalApps=LogAnalysis_sameApp.sameAppTwoTimesLogAnalysis(pathToOriginalApk);
			System.out.println("The output of disjoint tags same app run two times:"+disjointTagsOriginalApps);

			String orignalLogJSONPath=removeDuplicateLogsStatement.removeduplicateLogs(filePidOriginalPath);
			String modifiedLogJSONPath=removeDuplicateLogsStatement.removeduplicateLogs(filePidModifiedPath);

			HashSet<String>disjointTagsOriginalModified=AnalysingJSON.analyseJSONSameApps(orignalLogJSONPath, modifiedLogJSONPath, packageName);

			//if(disjointTagsOriginalApps.containsAll(disjointTagsOriginalResigned))
			//return true;
			String remarks="Tag Difference: ";
			Iterator<String> i = disjointTagsOriginalModified.iterator();
			flag=0;
			while (i.hasNext())
			{
				String element=i.next();
				if(disjointTagsOriginalApps.contains(element))
					continue;
				else
				{
					flag=1;
					remarks=remarks+","+element;
				}

			}
			if(flag==1)
			{
				/**
				 * There are some logs which are not there in the orignal version.
				 */
				updateDatabaseByPassable(packageName, "false", remarks);
				return false;
				//updateAntiRepackagingCheckPresence(packageName, 'Y', remarks);
				//return true;
			}
			else
			{
				/**
				 * It means that the logs generated are the same for the modified and the original app. Hence we are successful
				 * in by-passing the check
				 */
				updateDatabaseByPassable(packageName, "true", remarks);
				return true;
			}

		}
		return false;
	}

	private static String queryToDataBase(String packageName) throws SQLException {
		// TODO Auto-generated method stub
		Statement statement=DataBaseConnect.initialization();
		String query="Select remarks from antiTamperingCheckModified where packageName='"+packageName+"';";//.mbanking.aprb.aprb';"
		ResultSet  resultSet=statement.executeQuery(query);
		while(resultSet.next())
		{
			String remarks=resultSet.getString(1);
			return remarks;
		}


		return null;
	}

	private static boolean checkAntiTamperingPresence(String packageName, String pathToOriginalApk, String logPathForOriginalApp, String logPathForResignedApp, String fileContentsLogsPidOriginal, String fileContentsLogsPidRepackaged) throws Exception, IOException {
		/**
		 * This method will find out whether an app has anti-tampering check present or not by analysing the logs from the logcat.
		 * 
		 */



		boolean appCrash=methodAppCrash(packageName);
		if(appCrash==true)
		{
			updateAntiRepackagingCheckPresence(packageName, 'Y', "App Crashed");
			return true;
		}
		boolean resultActivity=differenceActiviyNameLogs(packageName,logPathForOriginalApp,logPathForResignedApp);
		if(resultActivity==true)
		{
			updateAntiRepackagingCheckPresence(packageName, 'Y', "Different Activity Observed");
			return true;
		}

		boolean resultToast=checkDifferenceToastLogs(packageName,logPathForOriginalApp,logPathForResignedApp);
		if(resultToast==true)
		{
			updateAntiRepackagingCheckPresence(packageName, 'Y', "Toast Message");
			return true;
		}
		//	boolean appCrash=methodAppCrash(packageName);
		boolean finalResult=resultToast|resultActivity;

		if(finalResult==true)
			return finalResult;
		else
		{
			/**
			 * It means, it's not that trivial to identify the check. So, we have to perform the tag analysis of the log
			 */
			String filePidOriginalPath="/home/nikhil/Documents/logs/"+packageName+"_original.txt";
			String filePidRepackagedPath="/home/nikhil/Documents/logs/"+packageName+"_repackaged.txt";
			writeToFile(filePidOriginalPath,filePidRepackagedPath,fileContentsLogsPidOriginal,fileContentsLogsPidRepackaged);

			int flag=1;
			HashSet<String> disjointTagsOriginalApps=LogAnalysis_sameApp.sameAppTwoTimesLogAnalysis(pathToOriginalApk);
			System.out.println("The output of disjoint tags same app run two times:"+disjointTagsOriginalApps);

			String orignalLogJSONPath=removeDuplicateLogsStatement.removeduplicateLogs(filePidOriginalPath);
			String resignedLogJSONPath=removeDuplicateLogsStatement.removeduplicateLogs(filePidRepackagedPath);

			HashSet<String>disjointTagsOriginalResigned=AnalysingJSON.analyseJSONSameApps(orignalLogJSONPath, resignedLogJSONPath, packageName);

			//if(disjointTagsOriginalApps.containsAll(disjointTagsOriginalResigned))
			//return true;
			String remarks="Tag Difference: ";
			Iterator<String> i = disjointTagsOriginalResigned.iterator();
			flag=0;
			while (i.hasNext())
			{
				String element=i.next();
				if(disjointTagsOriginalApps.contains(element))
					continue;
				else
				{
					flag=1;
					remarks=remarks+","+element;
				}

			}
			if(flag==1)
			{
				updateAntiRepackagingCheckPresence(packageName, 'Y', remarks);
				return true;
			}

			return false;
			/**
			 * For false part we are updating inside the main method
			 */

		}
		//return false;
	}

	private static void writeToFile(String filePidOriginal, String filePidRepackaged,
			String fileContentsLogsPidOriginal, String fileContentsLogsPidRepackaged) throws IOException {
		// TODO Auto-generated method stub

		File fileOriginal=new File(filePidOriginal);
		File fileRepackaged=new File(filePidRepackaged);

		// Create a file 

		fileOriginal.createNewFile();
		fileRepackaged.createNewFile();

		FileWriter fileWriter=new FileWriter(filePidOriginal);
		//write the contents to the file
		fileWriter.write(fileContentsLogsPidOriginal);
		fileWriter.close();


		fileWriter=new FileWriter(filePidRepackaged);
		fileWriter.write(fileContentsLogsPidRepackaged);
		fileWriter.close();

	}

	private static boolean methodAppCrash(String packageName) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		Process pr=CommandExecute.commandExecution(pathToadb+" shell pidof "+packageName);
		BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(pr.getInputStream()));
		String pid=bufferedReader.readLine();
		if(pid==null)
			return true;
		return false;
	}

	public static boolean differenceActiviyNameLogs(String packageName, String logPathForOriginalApp,
			String logPathForResignedApp) throws FileNotFoundException, SQLException {
		System.out.println("\n***************************** \n \n fetching activity names");

		HashSet<String> activiyOriginalHashSet=FetchActivity.fetchActivity(logPathForOriginalApp, packageName);
		System.out.println("from original app : "+activiyOriginalHashSet);

		HashSet<String> activiyRepckagedHashSet=FetchActivity.fetchActivity(logPathForResignedApp, packageName);
		System.out.println("from repackaged app : "+activiyRepckagedHashSet);

		String fileContents=packageName+":\noriginal:"+activiyOriginalHashSet+"\nResigned:"+activiyRepckagedHashSet+"\n";
		if (activiyRepckagedHashSet.containsAll(activiyOriginalHashSet) && activiyOriginalHashSet.containsAll(activiyRepckagedHashSet))
		{
			System.out.println("Oh No different activity names has been found on the original and repackaged app");
		}
		else
		{
			//System.out.println("Different set of activities. There is high chance that anti-tampering check is present.");
			//updateAntiRepackagingCheckPresence(packageName,'Y',"Different Activity Observed");
			return true;
		}
		return false;
		// TODO Auto-generated method stub
		//return fileContents;	
	}

	public static boolean checkDifferenceToastLogs(String packageName,String logPathForOriginalApp, String logPathForResignedApp) throws SQLException, IOException {
		/**
		 * This method checks whether an app is showing an error message through the Toast message 
		 * on the resigned version.
		 */

		String originalLogContents=new String(Files.readAllBytes(Paths.get(logPathForOriginalApp))); 
		String resignedLogContents=new String(Files.readAllBytes(Paths.get(logPathForResignedApp))); 
		if(!originalLogContents.contains(toastKilled) && resignedLogContents.contains(toastKilled))
		{
			//updateAntiRepackagingCheckPresence(packageName,'Y',"Toast Message");
			return true; //it means yes, there is a difference in the Toast message
		}
		return false;
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

	public static String appLogGeneration(String pathToApkFromPC, String logPathForOutput) throws Exception {
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

		checkApkInstall(packageName);

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

			System.out.println("The app is currently not running. The app has anti-repacakging check present. There is a high chance that the app is getting crashed.");
			if(logPathForOutput.contains("original"))
			{
				updateAntiRepackagingCheckPresence(packageName, 'E', "Unable to retrieve the process id of the original app");
	
			}
			else
				updateAntiRepackagingCheckPresence(packageName, 'Y', "App Crashed");

			return null;
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
			System.out.println(str1);
			String str2=new String(Files.readAllBytes(Paths.get(filePath2)));
			String str3=str1+str2;
			System.out.println(str2);
			//String outputFilePath="/home/nikhil/Documents/logs/universal_"+packageName+".txt";
			FileWriter fileWriter=new FileWriter(logPathForOutput);
			fileWriter.write(str3);
			fileWriter.close();
			System.out.println("Successfully wrote to the file");


			String removeFile1="rm "+filePath1;
			String removeFile2="rm "+filePath2;

			/*
			 * Removing the files
			 */
			//	CommandExecute.commandExecution(removeFile1);
			CommandExecute.commandExecution(removeFile2);
			//CommandExecute.commandExecution(pathToadb+" uninstall "+packageName);
			CommandExecute.commandExecution(clearLogcat);


			return str1;
			//File contents of the logs retrieved using the pid
			/*
			File file2=new File(directoryLocationForStoringLogs+"_.txt");
			file2.createNewFile();*/
			//so let's remove the two files 


		}
		//return -1;
	}

	public static void checkApkInstall(String packageName) throws Exception {
		// TODO Auto-generated method stub
		String command=pathToadb+" shell pm path "+packageName;
		Process process= CommandExecute.commandExecution(command);
		BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line=bufferedReader.readLine();
		if(line==null)
		{
			System.out.println("App not installed");
			updateAntiRepackagingCheckPresence(packageName, 'E', "Invalid Apk, Unable to Install");
			updateDatabaseByPassable(packageName, "NA", "Invalid Apk, Unable to Install");
			
			throw new Exception("App not installed");
			
		}

	}

	public static void updateAntiRepackagingCheckPresence(String packageName, char c, String remarks) throws SQLException {
		// TODO Auto-generated method stub
		String checkQuery="Select * from antiTamperingCheckModified where packageName='"+packageName+"';";
		System.out.println(checkQuery);
		Statement statement1=DataBaseConnect.initialization();
		ResultSet  resultSet=statement1.executeQuery(checkQuery);
		int flag=0;
		while(resultSet.next())
		{
			flag=1;
			resultSet.getString(1);
		}
		if(flag==0)
		{
			String query="Insert into antiTamperingCheckModified values ('"+packageName+"','"+c+"','"+remarks+"');";
			System.out.println(query);

			Statement statement=DataBaseConnect.initialization();
			statement.executeUpdate(query);
		}
		else
		{
			String query="Update antiTamperingCheckModified set IsCheckPresent ='"+c+"', Remarks='"+remarks+"' where packageName='"+packageName+"';";
			System.out.println(query);

			Statement statement=DataBaseConnect.initialization();
			statement.executeUpdate(query);
		}

	}

	private static int fetchActivityTaskManagerCount(String outputFilePath) throws IOException, InterruptedException {

		System.out.println("Output file path is :"+outputFilePath);
		int count=0;
		File file=new File(outputFilePath);
		Scanner scanner=new Scanner(file);
		while(scanner.hasNext())
		{
			String line=scanner.next();
			if(line.contains("ActivityTaskManager"))
			{
				count++;
			}
		}
		return count;


	}

	public static void launchTheApp(String packageName, String pathToApk) throws IOException, InterruptedException {
		// TODO Auto-generated method stub

		String fetchLauncherActivity= pathToadb+" shell \"cmd package resolve-activity --brief ";	//packageName | tail -n 1\";
		fetchLauncherActivity=fetchLauncherActivity + packageName +" | tail -n 1\"";
		// strcat (fetchLauncherActivity, " | tail -n 1\"");
		System.out.println(fetchLauncherActivity);
		//String pathToApk=null;
		String fetchOutputOfAapt=pathToaapt+" dump badging "+pathToApk;
		Process  process=CommandExecute.commandExecution(fetchOutputOfAapt);
		String launchableActivityCommand=pathToadb+" shell monkey -p "+packageName+" -c android.intent.category.LAUNCHER 1";
		System.out.println(launchableActivityCommand);
		CommandExecute.commandExecution(launchableActivityCommand);
		
			BufferedReader buf2 = new BufferedReader(new InputStreamReader(process.getInputStream()));
		//BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
	/*	System.out.println("Before while loop");
		while ((line=buf2.readLine())!=null) {
			if(line.contains(patternForLaunchableActivity))
			{
				//fetch the launcher activity name
				String temp=line.substring(patternForLaunchableActivity.length());
				//we are trimming it.
				int index=temp.indexOf("'");
				launchableActivityCommand=launchableActivityCommand.concat(temp.substring(0,index));
				break;
			}
			else 
				continue;*/
			//as in the first line only we can get the package name.That's why immeditate break;
			//	Files.write(Paths.get(filePath), (line+"\n").getBytes(),  StandardOpenOption.APPEND);
		
		//buf2.close();

		Thread.sleep(15000);
	}

	private static void storingLogOutputUsingPID(String packageName, String pid) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		System.out.println("PID of the app is : "+pid);
		String directoryLocationForStoringLogs="/home/nikhil/Documents/logs/";

		String phoneDirectory_1="/data/local/tmp/fromProgrampid.txt";
		String testFilePath="/data/local/tmp/myfile.txt";
		String analysingLogsUsingPID=pathToadb+" logcat -v brief -d --pid "+pid+" -f "+testFilePath;// > "+phoneDirectory_1;

		/**
		 * Pull this file to PC and save it to the  
		 */

		String filePath=directoryLocationForStoringLogs+"logs_"+packageName+"_PID.txt";
		File file=new File(filePath);
		file.createNewFile();

		System.out.println(analysingLogsUsingPID);
		Process process2=CommandExecute.commandExecution(analysingLogsUsingPID);

		/**
		 * Let's pull this file 
		 */
		String pullCommand=pathToadb+" pull "+testFilePath+" "+filePath;
		Process process=CommandExecute.commandExecution(pullCommand);

		/**
		 * Remove the file
		 */
		String removeTxtFileCommand=LogAnalysis.pathToadb+" shell rm "+testFilePath;
		CommandExecute.commandExecution(removeTxtFileCommand);
		/*
		BufferedReader buf1 = new BufferedReader(new InputStreamReader(process2.getInputStream()));
		//BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
		String line="";

		while ((line=buf1.readLine())!=null) {
			//as in the first line only we can get the package name.That's why immeditate break;
			Files.write(Paths.get(filePath), (line+"\n").getBytes(),  StandardOpenOption.APPEND);
			System.out.println(line);
		}
		buf1.close();*/

	}

	private static void storingLogOutputUsingGrepPackageName(String packageName) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		String phoneDirectory="/data/local/tmp/fromProgramPackageName.txt";
		String commandToFilterLogsUsingPackageName=pathToadb+" shell logcat -v brief -d | grep "+packageName+" > "+phoneDirectory;

		System.out.println(commandToFilterLogsUsingPackageName);

		Process process3=CommandExecute.commandExecution(commandToFilterLogsUsingPackageName);

		String devicePCDirectory="/home/nikhil/Documents/logs/fromProgram"+packageName+".txt";
		String pullFromPhoneToPC=pathToadb+" pull "+phoneDirectory+" "+devicePCDirectory;
		CommandExecute.commandExecution(pullFromPhoneToPC);

	}


}
