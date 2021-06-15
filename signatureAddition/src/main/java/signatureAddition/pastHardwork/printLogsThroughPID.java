package signatureAddition.pastHardwork;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import signatureAddition.CommandExecute;
import signatureAddition.LogAnalysis;
import signatureAddition.StartingPoint;

public class printLogsThroughPID {

	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		String FilePath="/home/nikhil/Documents/apks/pathToApps_3.txt";
		File file=new File(FilePath);
		Scanner scanner=new Scanner(file);
		while(scanner.hasNext())
		{
		initializationADB();
		String pathToOriginalApk=scanner.next();
		
		String packageName=StartingPoint.getPackageName(pathToOriginalApk);
		/**
		 * Install the app
		 */
		installApp(pathToOriginalApk, packageName);
		System.out.println("App installation happended successfully");
		/**
		 * Launch the app
		 */
		LogAnalysis.launchTheApp(packageName, pathToOriginalApk);
		
		String pid=fetchPID(packageName);
		String commandPID=LogAnalysis.pathToadb+" shell logcat -d --pid "+pid;
		System.out.println(commandPID);
		String commandPackageName=LogAnalysis.pathToadb+" shell logcat -d  | grep "+packageName;
		
		String logPathForOriginalApp="/home/nikhil/Documents/apps/logOutput/try1_"+packageName+".txt";

		Process processPackageName=CommandExecute.commandExecution(commandPackageName);
		Process processPID=CommandExecute.commandExecution(commandPID);
		String contentPackageName=generateLogs(processPackageName);
		String contentPID=generateLogs(processPID);
		String finalContent=contentPackageName+"\n"+contentPID;
		
		writeToFile(finalContent,logPathForOriginalApp);
		
		System.out.println("The end....................");
	}
	}

	private static void installApp(String pathToApkPC,String packageName) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		uninstallApp(packageName);
		String pathToApkOnSmartphone=" /data/local/tmp/"+packageName+".apk";
		String pushCommand=LogAnalysis.pathToadb+" push "+pathToApkPC+ pathToApkOnSmartphone;
		
		
		String installation=LogAnalysis.pathToadb+" shell pm install -g "+pathToApkOnSmartphone;
		String clearLog=LogAnalysis.pathToadb+" shell logcat -c";

		try {
			CommandExecute.commandExecution(pushCommand);
			System.out.println("Successfully pushed");
			CommandExecute.commandExecution(installation);
			System.out.println("Installation happened");
			CommandExecute.commandExecution(clearLog);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void uninstallApp(String packageName) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		String uninstallCommand=LogAnalysis.pathToadb+" uninstall "+packageName;
		CommandExecute.commandExecution(uninstallCommand);
	}

	public static String fetchPID(String packageName) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		String command=LogAnalysis.pathToadb+" shell logcat pidof "+packageName;
		Process process=CommandExecute.commandExecution(command);
		BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line=bufferedReader.readLine();
		return line;
	}

	public static void initializationADB() throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		String commandKillAdb=LogAnalysis.pathToadb+" kill-server";
		String adbDevices=LogAnalysis.pathToadb+" devices";
		
		CommandExecute.commandExecution(commandKillAdb);
		CommandExecute.commandExecution(adbDevices);
	}

	public static void writeToFile(String finalContent, String filePath) throws IOException {
	
		FileWriter fileWriter=new FileWriter(filePath);
		fileWriter.write(finalContent);
		fileWriter.close();
	}

	private static String generateLogs(Process process) throws IOException {
		// TODO Auto-generated method stub
		//String contents="";
		BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line="";
		String completeLogs="";

		while((line=bufferedReader.readLine())!=null)
		{
			System.out.println(line);
			completeLogs=completeLogs.concat(line+"\n");
		}
		return completeLogs;
		//return null;
	}

}
