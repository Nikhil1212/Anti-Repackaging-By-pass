package analysingDumpSys;
/**
 * It launches the app and after some amount of time, finds out the information related to meminfo of the apps.
 * So, currently we are launching the same apk two times, and then figuring out the difference in the count of the objects.
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Scanner;

import signatureAddition.*;
import signatureAddition.pastHardwork.ExecutePython;
import signatureAddition.pastHardwork.printLogsThroughPID;

public class DumpSysAnalysis {
	public static 	String pathToadb="/home/nikhil/Android/Sdk/platform-tools/adb";
	static 	String pathToaapt="/home/nikhil/Android/Sdk/build-tools/27.0.3/aapt";
	public static String toastKilled="Toast already killed"; 
	public static void main(String[] args) throws IOException, InterruptedException {

		String FilePath="/home/nikhil/Documents/apps/falseResults.txt";
		File file=new File(FilePath);
		Scanner scanner=new Scanner(file);
//		ExecutePython.downloadApks(FilePath);

		while(scanner.hasNext())
		{
			try
			{
				String packageName=scanner.next();
				String pathToOriginalApk="/home/nikhil/Downloads/googleplay-api-master/"+packageName+".apk";
				String pathToDumpsysOriginal="/home/nikhil/Documents/apps/dumpsys/"+packageName+"_original.txt";
				String pathToDumpsysRepackaged="/home/nikhil/Documents/apps/dumpsys/"+packageName+"_repackaged.txt";
				
				appLaunch(pathToOriginalApk);

				printLogsThroughPID.initializationADB();
				dataMembers dataMembersOriginal=computeCounts(packageName,pathToDumpsysOriginal);

				appLaunch(pathToOriginalApk);
				updateDumpSysObjectAnalysisOriginal(packageName,dataMembersOriginal,"DumpSysObjectAnalysisOriginal");

				//	appLaunch(pathToOriginalApk);
				String pathToResignedApk="/home/nikhil/Documents/apps/ReSignedApks/"+packageName+".apk";
				resignedApp.signApk(packageName, pathToOriginalApk, pathToResignedApk);

				StartingPoint.signApk(packageName, pathToResignedApk);

				appLaunch(pathToResignedApk);
				dataMembers dataMembersRepackaged=computeCounts(packageName,pathToDumpsysRepackaged);

				//updateCounts(packageName,dataMembersOriginal,dataMembersRepackaged);
				updateDumpSysObjectAnalysisOriginal(packageName,dataMembersRepackaged,"DumpSysObjectAnalysisRepackaged");

				CommandExecute.commandExecution(pathToadb+" uninstall "+packageName);
				/**
				 * Remove the apk from PC
				 */
				//CommandExecute.commandExecution("rm "+pathToOriginalApk);
			}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		}

	}

	private static void updateDumpSysObjectAnalysisOriginal(String packageName, dataMembers dataMembersOriginal, String tableName) throws SQLException {
		// TODO Auto-generated method stub

		int AppContexts=dataMembersOriginal.AppContexts;

		int Activities=dataMembersOriginal.Activities;
		int Views=dataMembersOriginal.Views;
		int ViewRootImpl=   dataMembersOriginal.ViewRootImpl;
		int Assets=dataMembersOriginal.Assets;
		int AssetManagers=     dataMembersOriginal.AssetManagers;   
		int LocalBinders =dataMembersOriginal.LocalBinders;    
		int ProxyBinders=  dataMembersOriginal.ProxyBinders;     
		int Parcelmemory=  dataMembersOriginal.Parcelmemory;    
		int Parcelcount=   dataMembersOriginal.AppContexts;    
		int DeathRecipients=    dataMembersOriginal.Parcelcount;
		int OpenSSLSockets=dataMembersOriginal.OpenSSLSockets;
		int WebViews=dataMembersOriginal.WebViews;

		String checkQuery="Select * from "+tableName+" where packageName='"+packageName+"';";
		System.out.println(checkQuery);
		Statement statement1=DataBaseConnect.initialization();
		ResultSet  resultSet=statement1.executeQuery(checkQuery);
		int flag=0;
		while(resultSet.next())
		{
			flag=1;
			resultSet.getString(1);
		}
		if(flag==1)
		{
			String deleteQuery="Delete from "+tableName+" where packageName='"+packageName+"';";
			System.out.println(deleteQuery);

			Statement statement=DataBaseConnect.initialization();
			statement.executeUpdate(deleteQuery);
		}
		String query="Insert into "+tableName+" values ('"+packageName+"',"+Views+","+ViewRootImpl+","+AppContexts+","+Activities+","+Assets+","+AssetManagers+","+LocalBinders+","+ProxyBinders+","+Parcelmemory+","+Parcelcount+","+DeathRecipients+","+OpenSSLSockets+","+WebViews+");";

		System.out.println(query);
		Statement statement=DataBaseConnect.initialization();

		try {
			statement.executeUpdate(query);
			System.out.println("Successfully updated the database");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}




	private static void appLaunch(String pathToApkFromPC) throws Exception{
		// TODO Auto-generated method stub
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


		/**
		 * Let's uninstall the app
		 */

		String clearLogcat=pathToadb+" shell logcat -c";



		//CommandExecute.commandExecution(pathToadb+" install "+pathToApk);

		CommandExecute.commandExecution(clearLogcat);

		launchTheApp(packageName,pathToApkFromPC);

	}

	private static void updateCounts(String packageName, dataMembers dataMembersOriginal,
			dataMembers dataMembersOriginal2) {

		int AppContexts=(int)Math.abs(dataMembersOriginal.AppContexts-dataMembersOriginal2.AppContexts);
		int Activities=(int)Math.abs(dataMembersOriginal.Activities-dataMembersOriginal2.Activities);
		int Views=(int)Math.abs(dataMembersOriginal.Views-dataMembersOriginal2.Views);
		int ViewRootImpl=   (int)Math.abs(dataMembersOriginal.ViewRootImpl-dataMembersOriginal2.ViewRootImpl);
		int Assets=(int)Math.abs(dataMembersOriginal.Assets-dataMembersOriginal2.Assets);
		int AssetManagers=     (int)Math.abs(dataMembersOriginal.AssetManagers-dataMembersOriginal2.AssetManagers);   
		int LocalBinders =(int)Math.abs(dataMembersOriginal.LocalBinders-dataMembersOriginal2.LocalBinders);    
		int ProxyBinders=  (int)Math.abs(dataMembersOriginal.ProxyBinders-dataMembersOriginal2.ProxyBinders);     
		int Parcelmemory=  (int)Math.abs(dataMembersOriginal.Parcelmemory-dataMembersOriginal2.Parcelmemory);    
		int Parcelcount=   (int)Math.abs(dataMembersOriginal.AppContexts-dataMembersOriginal2.AppContexts);    
		int DeathRecipients=    (int)Math.abs(dataMembersOriginal.Parcelcount-dataMembersOriginal2.Parcelcount);
		int OpenSSLSockets=(int)Math.abs(dataMembersOriginal.OpenSSLSockets-dataMembersOriginal2.OpenSSLSockets);
		int WebViews=(int)Math.abs(dataMembersOriginal.WebViews-dataMembersOriginal2.WebViews);

		String query="Insert into DumpSysObjectAnalysis values ('"+packageName+"',"+Views+","+ViewRootImpl+","+AppContexts+","+Activities+","+Assets+","+AssetManagers+","+LocalBinders+","+ProxyBinders+","+Parcelmemory+","+Parcelcount+","+DeathRecipients+","+OpenSSLSockets+","+WebViews+");";

		System.out.println(query);
		Statement statement=DataBaseConnect.initialization();

		try {
			statement.executeUpdate(query);
			System.out.println("Successfully updated the database");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static dataMembers computeCounts(String packageName, String pathToDumpsysOutput) throws Exception {
		// TODO Auto-generated method stub
		dataMembers dataMembers=new dataMembers();

		String dumpsysCommand=LogAnalysis.pathToadb+" shell dumpsys meminfo '"+packageName+"' -d > "+pathToDumpsysOutput;
		Process process= CommandExecute.commandExecution(dumpsysCommand);
		BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(process.getInputStream()));
		String output=bufferedReader.readLine();
		System.out.println(output);
		if(output==null)
			throw new Exception("Dumpsys command not executed properly.");
		int index=output.indexOf(':');

		dataMembers.AppContexts=fetchValue(output,index);//Integer.parseInt(output.substring(start, i));
		dataMembers.Activities=fetchValue(output, output.lastIndexOf(':'));


		dumpsysCommand=LogAnalysis.pathToadb+" shell dumpsys meminfo '"+packageName+"' | grep AssetManagers ";
		process= CommandExecute.commandExecution(dumpsysCommand);
		bufferedReader=new BufferedReader(new InputStreamReader(process.getInputStream()));
		output=bufferedReader.readLine();
		System.out.println(output);
		index=output.indexOf(':');

		dataMembers.Assets=fetchValue(output,index);//Integer.parseInt(output.substring(start, i));
		dataMembers.AssetManagers=fetchValue(output, output.lastIndexOf(':'));


		dumpsysCommand=LogAnalysis.pathToadb+" shell dumpsys meminfo '"+packageName+"' | grep ViewRootImpl ";
		process= CommandExecute.commandExecution(dumpsysCommand);
		bufferedReader=new BufferedReader(new InputStreamReader(process.getInputStream()));
		output=bufferedReader.readLine();
		System.out.println(output);
		index=output.indexOf(':');

		dataMembers.Views=fetchValue(output,index);//Integer.parseInt(output.substring(start, i));
		dataMembers.ViewRootImpl=fetchValue(output, output.lastIndexOf(':'));


		dumpsysCommand=LogAnalysis.pathToadb+" shell dumpsys meminfo '"+packageName+"' | grep Binders ";
		process= CommandExecute.commandExecution(dumpsysCommand);
		bufferedReader=new BufferedReader(new InputStreamReader(process.getInputStream()));
		output=bufferedReader.readLine();
		System.out.println(output);
		index=output.indexOf(':');

		dataMembers.LocalBinders=fetchValue(output,index);//Integer.parseInt(output.substring(start, i));
		dataMembers.ProxyBinders=fetchValue(output, output.lastIndexOf(':'));

		dumpsysCommand=LogAnalysis.pathToadb+" shell dumpsys meminfo '"+packageName+"' | grep Sockets ";
		process= CommandExecute.commandExecution(dumpsysCommand);
		bufferedReader=new BufferedReader(new InputStreamReader(process.getInputStream()));
		output=bufferedReader.readLine();
		System.out.println(output);
		index=output.indexOf(':');

		dataMembers.DeathRecipients=fetchValue(output,index);//Integer.parseInt(output.substring(start, i));
		dataMembers.OpenSSLSockets=fetchValue(output, output.lastIndexOf(':'));

		dumpsysCommand=LogAnalysis.pathToadb+" shell dumpsys meminfo '"+packageName+"' | grep Parcel ";
		process= CommandExecute.commandExecution(dumpsysCommand);
		bufferedReader=new BufferedReader(new InputStreamReader(process.getInputStream()));
		output=bufferedReader.readLine();
		System.out.println(output);
		index=output.indexOf(':');

		dataMembers.Parcelmemory=fetchValue(output,index);//Integer.parseInt(output.substring(start, i));
		dataMembers.Parcelcount=fetchValue(output, output.lastIndexOf(':'));


		/**
		 * Let's fetch the Activities from the lastIndex
		 */

		return dataMembers;
	}

	private static int fetchValue(String dumpSysObject, int index) {
		// TODO Auto-generated method stub
		int i;
		for(i=index+1;i<dumpSysObject.length();i++)
		{
			if(dumpSysObject.charAt(i)==' ')
				continue;
			else 
				break;
		}
		int start=i;
		for(i=start;i<dumpSysObject.length();i++)
		{
			if(dumpSysObject.charAt(i)==' ')
				break;
		}
		return Integer.parseInt(dumpSysObject.substring(start, i));

	}

	private static void checkActiviyNameLogs(String packageName, String logPathForOriginalApp,
			String logPathForResignedApp) throws FileNotFoundException, SQLException {

		HashSet<String> activiyOriginalHashSet=FetchActivity.fetchActivity(logPathForOriginalApp, packageName);
		System.out.println("from original app : "+activiyOriginalHashSet);
		HashSet<String> activiyRepckagedHashSet=FetchActivity.fetchActivity(logPathForResignedApp, packageName);
		System.out.println("from repackaged app : "+activiyRepckagedHashSet);

		if (activiyRepckagedHashSet.containsAll(activiyOriginalHashSet))
		{
			System.out.println("Oh No different activity names has been found on the original and repackaged app");
		}
		else
		{
			System.out.println("Different set of activities. There is high chance that anti-tampering check is present.");
			updateAntiRepackagingCheckPresence(packageName,'Y',"Different Activity Observed");
		}
		// TODO Auto-generated method stub

	}

	/*private static void checkToastLogs(String packageName,String logPathForOriginalApp, String logPathForResignedApp) throws SQLException, IOException {
		// TODO Auto-generated method stub
		String originalLogContents=new String(Files.readAllBytes(Paths.get(logPathForOriginalApp))); 
		String resignedLogContents=new String(Files.readAllBytes(Paths.get(logPathForResignedApp))); 
		if(!originalLogContents.contains(toastKilled) && resignedLogContents.contains(toastKilled))
		{
			updateAntiRepackagingCheckPresence(packageName,'Y',"Toast Message");
		}
	}*/

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

	private static int appLogGeneration(String pathToApkFromPC, String logPathForOutput) throws IOException, InterruptedException {
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

			System.out.println("The app is currently not running. The app has anti-repacakging check present. There is a high chance that the app is getting crashed.");
			try {
				updateAntiRepackagingCheckPresence(packageName,'Y',"App crashed");
				return -1;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
			//System.out.println(str1);
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
			CommandExecute.commandExecution(removeFile1);
			CommandExecute.commandExecution(removeFile2);
			//			CommandExecute.commandExecution(pathToadb+" uninstall "+packageName);
			CommandExecute.commandExecution(clearLogcat);


			return fetchActivityTaskManagerCount(logPathForOutput);
			/*
			File file2=new File(directoryLocationForStoringLogs+"_.txt");
			file2.createNewFile();*/
			//so let's remove the two files 


		}
		return -1;
	}

	public static void updateAntiRepackagingCheckPresence(String packageName, char c, String remarks) throws SQLException {
		// TODO Auto-generated method stub
		String query="Insert ignore into AntiRepackagingCheckPresence values ('"+packageName+"','"+c+"','"+remarks+"');";
		System.out.println(query);

		Statement statement=DataBaseConnect.initialization();
		statement.executeUpdate(query);
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
		String line="";
		String patternForLaunchableActivity="launchable-activity: name='";
		// adb shell monkey -p in.amazon.mShop.android.shopping -c android.intent.category.LAUNCHER 1
		String launchableActivityCommand=pathToadb+" shell monkey -p "+packageName+" -c android.intent.category.LAUNCHER 1";
		BufferedReader buf2 = new BufferedReader(new InputStreamReader(process.getInputStream()));
		//BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
		System.out.println("Before while loop");
		CommandExecute.commandExecution(launchableActivityCommand);
		/*	while ((line=buf2.readLine())!=null) {
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
		}*/
		buf2.close();

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
		String removeTxtFileCommand=DumpSysAnalysis.pathToadb+" shell rm "+testFilePath;
		CommandExecute.commandExecution(removeTxtFileCommand);

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
