
package Logs;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;

import org.json.JSONObject;

import signatureAddition.CommandExecute;
import signatureAddition.StartingPoint;
import signatureAddition.resignedApp;
import signatureAddition.pastHardwork.AnalyseActivities;
import signatureAddition.pastHardwork.AnalysingJSON;
import signatureAddition.pastHardwork.TagDifferenceWithApk;
import signatureAddition.pastHardwork.printLogsThroughPID;

import signatureAddition.*;
/**
 * This class is the starting point that finds out whether an app has anti-tampering check present or not by analysing various features like Activity, toast message, app crashed, 
 * @author nikhil
 *
 */
public class LogAnalysis {
	public static String pathToadb="/home/nikhil/Downloads/platform-tools_r30.0.5-linux/platform-tools/adb";

	static 	String pathToaapt="/home/nikhil/Android/Sdk/build-tools/27.0.3/aapt";
	public static String toastKilled="Toast already killed"; 


	public static void main(String[] args) throws IOException, InterruptedException {


		String FilePath="/home/nikhil/Documents/apps/ForeignAppsPackageName.txt";
		File file=new File(FilePath);
		Scanner scanner=new Scanner(file);
		int count=0;
		//ExecutePython.downloadApks(FilePath);
		while(scanner.hasNext())
		{
			String packageName="";
			String 	pathToDisAssembleCodeDirectory="";
			try
			{

				packageName=scanner.next();
				//packageName="com.indigo.hdfcloans";
				
				pathToDisAssembleCodeDirectory="/home/nikhil/Documents/apps/"+packageName;

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
				//restartSmartphone.restart();
				printLogsThroughPID.initializationADB();

				String fileContentsLogsPidRepackaged=appLogGeneration(pathToResignedApk,logPathForResignedApp);


				//System.out.println("Checking whether we are able to see AccountInvalidator *******************\n****************\n**************");
				//	String fileContents=new String(Files.readAllBytes(Paths.get(logPathForResignedApp)));

				boolean result=checkAntiTamperingPresence(packageName,pathToOriginalApk,logPathForOriginalApp,logPathForResignedApp,fileContentsLogsPidOriginal	,fileContentsLogsPidRepackaged);

				/**
				 * For the time being, I have commented the code related to by-passing the anti-tampering check.
				 * 
				 */


			//	String pathToModifiedApk=generatingModifiedApk(packageName,pathToOriginalApk);
				//String fileContentsLogsPidModified=appLogGeneration(pathToModifiedApk,logPathForModifiedApp);

				/*if(result==true)
				{
					/**
				 * The check is present, we are trying to by-pass it.
				 */
				//	

				//boolean byPassableResult=isItByPassable(packageName,pathToOriginalApk,logPathForOriginalApp,logPathForResignedApp,fileContentsLogsPidOriginal,fileContentsLogsPidRepackaged,fileContentsLogsPidModified,logPathForModifiedApp);

				/**
				 * Update the database stating that we are successful in by-passing the check

					//	updateDatabaseByPassable(packageName,""+byPassableResult);

				}
				else
				{

					updateAntiRepackagingCheckPresence(packageName, 'N', "Could not find the check.");

					updateDatabaseByPassable(packageName, "NA","NA");
					continue;
				}*/

				//System.out.println(fileContents);

				printLogsThroughPID.initializationADB();
				//	System.exit(0);

				/*				String orignalLogJSONPath=removeDuplicateLogsStatement.removeduplicateLogs(logPathForOriginalApp);
				String resignedLogJSONPath=removeDuplicateLogsStatement.removeduplicateLogs(logPathForResignedApp);
				String modifiedLogJSONPath=removeDuplicateLogsStatement.removeduplicateLogs(logPathForModifiedApp);

				AnalysingJSON.analyseJSON(orignalLogJSONPath, resignedLogJSONPath, modifiedLogJSONPath);
				 */
			}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}


			CommandExecute.commandExecution(pathToadb+" uninstall "+packageName);
			StartingPoint.removeDirectory(pathToDisAssembleCodeDirectory);
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
		String query="Select remarks from antiTamperingCheckModified_v2 where packageName='"+packageName+"';";//.mbanking.aprb.aprb';"
		ResultSet  resultSet=statement.executeQuery(query);
		while(resultSet.next())
		{
			String remarks=resultSet.getString(1);
			return remarks;
		}


		return null;
	}

	public static boolean checkAntiTamperingPresence(String packageName, String pathToOriginalApk, String logPathForOriginalApp, String logPathForResignedApp, String fileContentsLogsPidOriginal, String fileContentsLogsPidRepackaged) throws Exception, IOException {
		
		
		/**
		 * This method will find out whether an app has anti-tampering check present or not by analysing the logs from the logcat.
		 * 
		 */



		boolean appCrash=methodAppCrash(packageName);
		if(appCrash==true)
		{
			updateAntiTamperingCheckPresence(packageName, 'Y', "App Crashed");
			return true;
		}
		boolean resultActivity=differenceActiviyNameLogs(packageName,logPathForOriginalApp,logPathForResignedApp);
		if(resultActivity==true)
		{
			updateAntiTamperingCheckPresence(packageName, 'Y', "Different Activity Observed");
			return true;
		}

		boolean resultToast=checkDifferenceToastLogs(packageName,logPathForOriginalApp,logPathForResignedApp);
		if(resultToast==true)
		{
			updateAntiTamperingCheckPresence(packageName, 'Y', "Toast Message");
			return true;
		}
		//	boolean appCrash=methodAppCrash(packageName);
		boolean finalResult=false;

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
			//HashSet<String> disjointTagsOriginalApps=LogAnalysis_sameApp.sameAppTwoTimesLogAnalysis(pathToOriginalApk);
			//System.out.println("The output of disjoint tags same app run two times:"+disjointTagsOriginalApps);

			String orignalLogJSONPath=removeDuplicateLogsStatement.removeduplicateLogs(filePidOriginalPath);
			String resignedLogJSONPath=removeDuplicateLogsStatement.removeduplicateLogs(filePidRepackagedPath);

			String 	pathToDisAssembleCodeDirectory="/home/nikhil/Documents/apps/"+packageName;


			String originalTags=new String (Files.readAllBytes(Paths.get(orignalLogJSONPath)));
			String repackagedTags=new String (Files.readAllBytes(Paths.get(resignedLogJSONPath)));

			JSONObject jsonOriginal = new JSONObject(originalTags);  
			JSONObject jsonRepackaged = new JSONObject(repackagedTags);  

			String outputOriginalTagsAppPath="/home/nikhil/Documents/apps/TagsLogcatAPK/"+packageName+"_original.txt";
			String outputRepackagedTagsAppPath="/home/nikhil/Documents/apps/TagsLogcatAPK/"+packageName+"_repackaged.txt";


			/**
			 * Let's disassemble the apk
			 */

			StartingPoint.disassembleApk(pathToOriginalApk, packageName);

			TagDifferenceWithApk.filterTagsFromLogcatAndApk(jsonOriginal,outputOriginalTagsAppPath,pathToDisAssembleCodeDirectory);
			TagDifferenceWithApk.filterTagsFromLogcatAndApk(jsonRepackaged,outputRepackagedTagsAppPath,pathToDisAssembleCodeDirectory);

			boolean result=TagDifferenceWithApk.finalComparisonLogAnalysis(outputOriginalTagsAppPath,outputRepackagedTagsAppPath);

			StartingPoint.removeDirectory(pathToDisAssembleCodeDirectory);

			if(result)
			{
				System.out.println("There is difference in the orignal and repackaged logs after we make sure that the tags which are coming in the logs are also the part of the apk");
				/**
				 * Update the table with the Remarks "Really there is a tag difference"
				 */
				String remarks="Really there is a tag difference ";
				updateAntiTamperingCheckPresence(packageName, 'Y', remarks);
				return result;
				//	MainLogAnalysisTwoTimes.updateTagDifference(packageName,1);
			}
			else
			{
				System.out.println("Please proceed for the screen capture analysis");
				String remarks="Need to capture the screen and find the image difference";
				updateAntiTamperingCheckPresence(packageName, 'N', remarks);


				/**
				 * Complete this function
				 */
				return imageAnalysis(packageName);
			}



			/*			String remarks="Tag Difference: ";
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

	public static boolean imageAnalysis(String packageName) {
		// TODO Auto-generated method stub
		return false;
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

	public static boolean methodAppCrash(String packageName) throws Exception {
		
		Process pr=CommandExecute.commandExecution(pathToadb+" shell pidof "+packageName);
		BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(pr.getInputStream()));
		String pid=bufferedReader.readLine();
		if(pid!=null)
			return false;
		/**
		 * Dump the screen and check whether we can see the package name
		 */
//		Process pr=CommandExecute.commandExecution(pathToadb+" shell uiautomator dump  "+packageName);
		String uiDump_orignalPath="/home/nikhil/Documents/apps/uiautomator/AppsAnalysis/"+packageName+"_ToFindAppCrash.xml";
		uiautomator.Main.dumpScreenXml(uiDump_orignalPath, packageName);
		String fileContents=new String(Files.readAllBytes(Paths.get(uiDump_orignalPath)));
		if(fileContents.contains(packageName))
			return false;
		return true;
	}

	public static boolean differenceActiviyNameLogs(String packageName, String logPathForOriginalApp,
			String logPathForResignedApp) throws FileNotFoundException, SQLException {
		System.out.println("\n***************************** \n \n fetching activity names");

		
		HashSet<String> activiyOriginalHashSet=FetchActivity.fetchActivity(logPathForOriginalApp, packageName);
		
		AnalyseActivities.iterateHashSet(activiyOriginalHashSet);
		System.out.println("from original app : "+activiyOriginalHashSet);
		
		
		HashSet<String> activiyRepckagedHashSet=FetchActivity.fetchActivity(logPathForResignedApp, packageName);
		AnalyseActivities.iterateHashSet(activiyRepckagedHashSet);

		System.out.println("from app's other run: "+activiyRepckagedHashSet);

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

		try {
			FileNamesForSignatureAddition.codeInjectionProcess(signCertificateKey, pathToDisAssembleCode);
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
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


		String clearLogcat=pathToadb+" shell logcat -b all -c";



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
				updateAntiTamperingCheckPresence(packageName, 'E', "Unable to retrieve the process id of the original app");

			}
			else
				updateAntiTamperingCheckPresence(packageName, 'Y', "App Crashed");

			return null;
		}
		else
		{
			//as in the first line only we can get the package name.That's why immeditate break;
			String pid=line;
			System.out.println("pid of the app with package name "+packageName+" is "+line);

			//System.out.println(analysingLogsUsingPID);

		//	usingLogcat(packageName,pid,logPathForOutput);
			
			storingLogOutputUsingGrepPackageName(packageName,AppLaunchAndDump.deviceId[0]);
			storingLogOutputUsingPID(packageName,pid,AppLaunchAndDump.deviceId[0]);

		//	String directoryLocationForStoringLogs="/home/nikhil/Documents/logs/";

			String filePath1=directoryLocationForStoringLogs+"logs_"+packageName+"_PID.txt";
			String filePath2="/home/nikhil/Documents/logs/fromProgram"+packageName+".txt";


			String str1=new String(Files.readAllBytes(Paths.get(filePath1)));
			//	System.out.println(str1);
			String str2=new String(Files.readAllBytes(Paths.get(filePath2)));
			String str3=str1+str2;
			//System.out.println(str2);
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
			CommandExecute.commandExecution(removeFile1);
			return str1;

		}
		//return -1;
	}

	public static void usingLogcat(String packageName, String pid, String logPathForOutput, String deviceId) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		storingLogOutputUsingGrepPackageName(packageName,deviceId);
		if(pid!=null)
		storingLogOutputUsingPID(packageName,pid,deviceId);

		String directoryLocationForStoringLogs="/home/nikhil/Documents/logs/";

		String filePath1=directoryLocationForStoringLogs+"logs_"+packageName+"_PID.txt";
		String filePath2="/home/nikhil/Documents/logs/fromProgram"+packageName+".txt";


		String str1="";
		if(pid!=null)
		str1=new String(Files.readAllBytes(Paths.get(filePath1)));
		
		//	System.out.println(str1);
		
		String str2=new String(Files.readAllBytes(Paths.get(filePath2)));
		String str3=str1+str2;
		//System.out.println(str2);
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
		CommandExecute.commandExecution(removeFile1);

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
			updateAntiTamperingCheckPresence(packageName, 'E', "Invalid Apk, Unable to Install");
			updateDatabaseByPassable(packageName, "NA", "Invalid Apk, Unable to Install");

			throw new Exception("App not installed");

		}

	}

	public static void updateAntiTamperingCheckPresence(String packageName, char c, String remarks) throws SQLException {
		// TODO Auto-generated method stub
		String checkQuery="Select * from TagDifferenceOriginalApps_2 where packageName='"+packageName+"';";
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
			String query="Insert into TagDifferenceOriginalApps_2 values ('"+packageName+"','"+c+"','"+remarks+"');";
			System.out.println(query);

			Statement statement=DataBaseConnect.initialization();
			statement.executeUpdate(query);
		}
		else
		{
			String query="Update TagDifferenceOriginalApps_2 set IsCheckPresent ='"+c+"', Remarks='"+remarks+"' where packageName='"+packageName+"';";
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
		
		System.out.println(fetchLauncherActivity);
		String fetchOutputOfAapt=pathToaapt+" dump badging "+pathToApk;
		
		Process  process=CommandExecute.commandExecution(fetchOutputOfAapt);
		String launchableActivityCommand=pathToadb+" shell monkey -p "+packageName+" -c android.intent.category.LAUNCHER 1";
		
		System.out.println(launchableActivityCommand);
		CommandExecute.commandExecution(launchableActivityCommand);

		

		Thread.sleep(15000);
	}

	public static void storingLogOutputUsingPID(String packageName, String pid, String deviceId) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		System.out.println("PID of the app is : "+pid);
		String directoryLocationForStoringLogs="/home/nikhil/Documents/logs/";

		String phoneDirectory_1="/data/local/tmp/fromProgrampid.txt";
		String testFilePath="/data/local/tmp/myfile.txt";
		String analysingLogsUsingPID=pathToadb+" -s " +deviceId+" logcat -v brief -d --pid "+pid+" -f "+testFilePath;// > "+phoneDirectory_1;

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
		String pullCommand=pathToadb+" -s "+deviceId+" pull "+testFilePath+" "+filePath;
		Process process=CommandExecute.commandExecution(pullCommand);

		/**
		 * Remove the file
		 */
		String removeTxtFileCommand=LogAnalysis.pathToadb+" -s "+ deviceId+" shell rm "+testFilePath;
		CommandExecute.commandExecution(removeTxtFileCommand);
	

	}

	public static void storingLogOutputUsingGrepPackageName(String packageName, String deviceId) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		String phoneDirectory="/data/local/tmp/fromProgramPackageName.txt";
		String commandToFilterLogsUsingPackageName=pathToadb+" -s "+ deviceId+" shell logcat -v brief -d | grep "+packageName+" > "+phoneDirectory;

		System.out.println(commandToFilterLogsUsingPackageName);

		Process process3=CommandExecute.commandExecution(commandToFilterLogsUsingPackageName);

		String devicePCDirectory="/home/nikhil/Documents/logs/fromProgram"+packageName+".txt";
		String pullFromPhoneToPC=pathToadb+" -s "+ deviceId+" pull "+phoneDirectory+" "+devicePCDirectory;
		CommandExecute.commandExecution(pullFromPhoneToPC);

	}


}
