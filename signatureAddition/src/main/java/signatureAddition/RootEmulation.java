package signatureAddition;

import java.io.BufferedReader;
/**
 * This class assumes that the modified version of the app's apk is already generated and we are installing the app on different devices
 * and generating the dump. Currently, this doesn't handle the split apks scenarios.
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import Logs.LogAnalysis;
import signatureAddition.pastHardwork.DeviceStatus;


public class RootEmulation {

	public static String deviceId[]={"14011JEC202909", "0248f4221b4ca0ee","emulator-5554"};
	public static String deviceIdSynonym[]={"real","rooted","emulator"};

	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		//int i=0;
		String pathToApk="/home/nikhil/Documents/apps/dataset/packageNames_2.txt";
		File file=new File(pathToApk);
		Scanner scanner=new Scanner(file);
		String packageName="";
		while(scanner.hasNext())
		{
			try
			{

				packageName=scanner.next();
				String directoryPath="/home/nikhil/Documents/apps/uiautomator/rootEmulator/"+packageName;

				createDirectory(directoryPath);
				
				String fileNamePath="/home/nikhil/Documents/apps/dataset/"+packageName+"/base.apk";
				
				String modifiedApkPath="/home/nikhil/Documents/apps/modified_"+packageName+".apk";
				for(int j=0;j<2;j++)
				{
					appInstallation(fileNamePath,packageName, deviceId[j]);
					if(!isAppInstalled(packageName,deviceId[j]))
					{
						updateDatabaseByPassable(packageName, "App did not installed");
						throw new Exception("App did not installed");
					}
					String destinationPath=directoryPath+"/"+deviceIdSynonym[j]+".xml";
					
					dumpTheAppScreen(packageName, destinationPath, deviceId[j]);
				
					dumpTheAppScreen(packageName, destinationPath, deviceId[j]);
					
					
					
				}
				for(int j=1;j<deviceId.length;j++)
				{
					appInstallation(modifiedApkPath, packageName, deviceId[j]);
					if(!isModifedAppAlive(packageName,deviceId[j]))
					{
						//update the MySQL Table
						updateDatabaseByPassable(packageName, "App crashed");
						throw new Exception("App did not installed");

						
					}
						
					String destinationPath=directoryPath+"/modified_"+deviceIdSynonym[j]+".xml";
					
					dumpTheAppScreen(packageName, destinationPath, deviceId[j]);
				
					dumpTheAppScreen(packageName, destinationPath, deviceId[j]);
				}
				
				//Below code will uninstall the apps on the three devices. It is necessary because of space constraints.
				for(int j=0;j<deviceId.length;j++)
				{
					CommandExecute.commandExecution(LogAnalysis.pathToadb+" -s "+deviceId[j]+" uninstall "+packageName);
				}
				
				
			}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				for(int j=0;j<deviceId.length;j++)
				{
					CommandExecute.commandExecution(LogAnalysis.pathToadb+" -s "+deviceId[j]+" uninstall "+packageName);
				}
			}

	}

}
	public static boolean isAppInstalled(String packageName, String deviceId) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		String command=LogAnalysis.pathToadb+" -s "+deviceId+" shell pm path "+packageName;
		Process process= CommandExecute.commandExecution(command);
		BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(process.getInputStream()));
		String output=bufferedReader.readLine();
		if(output==null)
			return false;
		return true; 
		
	}
	public static Statement initialization() {
		// TODO Auto-generated method stub
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String sqlurl="jdbc:mysql://10.192.38.90:3306/apps";
			Connection cn=DriverManager.getConnection(sqlurl,"nikhil","nikhil");
			Statement st=cn.createStatement();
			return st;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static void updateDatabaseByPassable(String packageName, String remarks) throws SQLException {
		// TODO Auto-generated method stub
		String checkQuery="Select * from ModifiedApkStatus where packageName='"+packageName+"';";
		System.out.println(checkQuery);
		Statement statement1=initialization();
		ResultSet  resultSet=statement1.executeQuery(checkQuery);
		int flag=0;
		while(resultSet.next())
		{
			flag=1;
			resultSet.getString(1);			
		}
		if(flag==0)
		{
			String query="Insert into ModifiedApkStatus values ('"+packageName+"','"+remarks+"');";
			System.out.println(query);

			Statement statement=initialization();
			statement.executeUpdate(query);
		}
		else
		{
			String query="Update ModifiedApkStatus set Remarks ='"+remarks+"'where packageName='"+packageName+"';";
			System.out.println(query);

			Statement statement=initialization();
			statement.executeUpdate(query);
		}
	}

	private static boolean isModifedAppAlive(String packageName, String deviceId) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		String command=LogAnalysis.pathToadb+" -s "+deviceId+" shell dumpsys meminfo "+packageName+" | grep pid";
		Process process= CommandExecute.commandExecution(command);
		BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(process.getInputStream()));
		String output=bufferedReader.readLine();
		if(output==null)
			return false;
		return true;
	}

	public static void dumpTheAppScreen(String packageName, String destinationPath, String deviceId) throws IOException, InterruptedException {
	Process takeDump=CommandExecute.commandExecution(LogAnalysis.pathToadb+" -s "+deviceId+" shell uiautomator dump  /data/local/tmp/dump.xml ");//+directoryPath+"/"+deviceId[j]);
		
	
		//pull the xml
	BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(takeDump.getInputStream()));
	String line=bufferedReader.readLine();
	System.out.println(line);
	if(line==null)
	{
		System.out.println("Unable to take dump");
		return;
	}
	
		String pullXml=LogAnalysis.pathToadb+" -s "+deviceId+" pull /data/local/tmp/dump.xml "+destinationPath;
		
		CommandExecute.commandExecution(pullXml);
		Thread.sleep(1000);
				
	}

	public static void createDirectory(String pathToDir) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		CommandExecute.commandExecution("mkdir "+pathToDir);
		return ;
	}

	private static void appInstallation(String fileNamePath, String packageName, String deviceId) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		
		if(!DeviceStatus.isDeviceAvailable())
		{
			System.out.println("Device unavilable");
			System.exit(0);
		}
		CommandExecute.commandExecution(LogAnalysis.pathToadb+" -s "+deviceId+" uninstall "+packageName);
		
		String apkPathOnSmartphone=" /data/local/tmp/"+packageName+".apk";

		String pushApkCommand=LogAnalysis.pathToadb+" -s "+deviceId+" push "+fileNamePath+apkPathOnSmartphone;
		String installThroughPMCommand=LogAnalysis.pathToadb+" -s "+ deviceId +" shell pm install -g"+apkPathOnSmartphone;
		String removeApkFromDevice=LogAnalysis.pathToadb+" -s "+ deviceId + " shell rm"+apkPathOnSmartphone;

		CommandExecute.commandExecution(pushApkCommand);

		CommandExecute.commandExecution(installThroughPMCommand);
		/**
		 * We are using pm as we want to give all the permission an app wants during the installation time
		 */

		CommandExecute.commandExecution(removeApkFromDevice);
		
		
		launchTheApp(packageName,deviceId);
		/**
		 * As the apk has been installed, so no use of this apk
		 */
		//Process pr=CommandExecute.commandExecution(commandToFilterLogsUsingPackageName);


		/**
		 * Let's uninstall the app command to be executed is :/home/nikhil/Downloads/platform-tools_r30.0.5-linux/platform-tools/adb -s emulator-5554 shell dumpsys meminfo com.ausmallfinancebank.amb | grep pid
		 * /home/nikhil/Downloads/platform-tools_r30.0.5-linux/platform-tools/adb -s emulator-5554 shell dumpsys meminfo com.ausmallfinancebank.amb | grep pid

		 */

		
	}
	public static void launchTheApp(String packageName, String deviceId) throws IOException, InterruptedException {
		
		//RestartADB.main();
		
		String launchableActivityCommand=LogAnalysis.pathToadb+" -s "+deviceId+" shell monkey -p "+packageName+" -c android.intent.category.LAUNCHER 1";
		
		CommandExecute.commandExecution(launchableActivityCommand);
		Thread.sleep(60000);
	}
}
