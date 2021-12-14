package signatureAddition;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import Logs.LogAnalysis;
import ProcessMission.FetchProcessIdDumpsys;
import analysingDumpSys.DumpSysAnalysis;

/**
 * This will install the modified version of the app on the rooted device handling the split-apks scenarios and taking the dump. This will also generate the resigned version of the split-apks.
 * @author nikhil
 *
 */
public class ModifiedApkRun {
	//public static String installationCommand="";
	public static String pathToadb="/home/nikhil/Downloads/platform-tools_r30.0.5-linux/platform-tools/adb";

	public static String readlink="/bin/readlink";
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String pathToApk="/home/nikhil/Documents/apps/apkGeneration_1.txt";
		File file=new File(pathToApk);
		Scanner scanner=new Scanner(file);
		String pathTillDataset="/home/nikhil/Documents/apps/dataset/";
		String modifiedBaseApkPath="";
		while(scanner.hasNext())
		{
			String directoryPath="";

			try
			{
				
				String packageName=scanner.next();
				String installationCommand="";
				modifiedBaseApkPath="/home/nikhil/Documents/apps/modified_"+packageName+".apk";
				directoryPath="/home/nikhil/Documents/apps/"+packageName+"/";
			//	String directoryPath="";
				int count=0;
				if(isSplitApks(packageName))
				{
					count=2;
					//Resigned the split-apks scenarios and handle install-multiple command and 
					String pathToPackage=pathTillDataset+packageName+"/";
					
					String fetchApkInsideDirectory=AppLaunchAndDump.pathToLS+" "+pathToPackage;
					System.out.println(fetchApkInsideDirectory);
					//Process process=Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", "readlink -f "+ fetchApkInsideDirectory});///home/nikhil/Documents/apps/com.mbanking.aprb.aprb"});
					
					//process.waitFor();
					
					Process process=CommandExecute.commandExecution(fetchApkInsideDirectory);
					BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(process.getInputStream()));
					String line=bufferedReader.readLine();
					String apkPaths="";
					while(line!=null)
					{
						//create a directory
						String splitApkPath=pathToPackage+line;
						System.out.println("value of split apks path: "+splitApkPath);
						if(!line.contains(".apk"))
						{
							line=bufferedReader.readLine();
							continue;
						}
						if(line.contains("base.apk"))
						{
							line=bufferedReader.readLine();
							continue;
						}
						directoryPath="/home/nikhil/Documents/apps/"+packageName+"/";
						String lastPart=line.substring(line.indexOf("split_"));
						RootEmulation.createDirectory(directoryPath);
						
						String copyCommand="cp "+splitApkPath+" "+directoryPath;
						System.out.println("copy command is :"+copyCommand);
						CommandExecute.commandExecution(copyCommand);
						
						String modifiedBuildApkPath = directoryPath+lastPart;
						apkPaths=apkPaths+" "+modifiedBuildApkPath;
						StartingPoint.signApk(packageName,modifiedBuildApkPath);
						line=bufferedReader.readLine();
					}
					installationCommand=pathToadb+ " install-multiple -g "+modifiedBaseApkPath+" "+ apkPaths;
					
				}
				else
				{
					installationCommand=pathToadb+ " install -g "+modifiedBaseApkPath;
				}
				
				
				
				System.out.println("Installation Command value :"+installationCommand);
				CommandExecute.commandExecution(installationCommand);
				if(!RootEmulation.isAppInstalled(packageName, "0248f4221b4ca0ee"))
				{
					System.out.println("App did not installed.");
					continue;
				}
			//	AppLaunchAndDump.installAndGrantPermission(installationCommand, count, packageName, appPathDirectory+packageName+"/", "0248f4221b4ca0ee");
				
				DumpSysAnalysis.launchTheApp(packageName);
				//Now dump it.
				
				if(!FetchProcessIdDumpsys.isAppRunning(packageName))
				{
					updateTable(packageName,'N');
				}
				else
					updateTable(packageName, 'Y');
				String outputPathDirectory="/home/nikhil/Documents/apps/modifiedAppRootDetection/"+packageName;
				RootEmulation.createDirectory(outputPathDirectory);
				String dumpPath=outputPathDirectory+"/_rooted.xml";
				RootEmulation .dumpTheAppScreen(packageName, dumpPath, "0248f4221b4ca0ee");

				StartingPoint.removeDirectory(directoryPath);
				//CommandExecute.commandExecution("rm "+modifiedBaseApkPath);
				installationCommand="";
				CommandExecute.commandExecution(LogAnalysis.pathToadb+" uninstall "+packageName);
			}
			catch (Exception e) {
			//	StartingPoint.removeDirectory(directoryPath);
//				CommandExecute.commandExecution("rm "+modifiedBaseApkPath);
				

				e.printStackTrace();
			}
			//break;
		}
		
	}

	private static void updateTable(String packageName, char c) throws SQLException {
		// TODO Auto-generated method stub
		String checkQuery="Select * from ModifiedAppRunningStatus where packageName='"+packageName+"';";
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
			String query="Insert into ModifiedAppRunningStatus values ('"+packageName+"','"+c+"');";
			System.out.println(query);

			Statement statement=DataBaseConnect.initialization();
			statement.executeUpdate(query);
		}
		else
		{
			String query="Update ModifiedAppRunningStatus set isAppRunning ='"+c+"'where packageName='"+packageName+"';";
			System.out.println(query);

			Statement statement=DataBaseConnect.initialization();
			statement.executeUpdate(query);
			
		}
	}

	public static boolean isSplitApks(String packageName) throws IOException, InterruptedException {
		String packageDirectory="/home/nikhil/Documents/apps/dataset/"+packageName+"/";

		Process process=CommandExecute.commandExecution(AppLaunchAndDump.pathToLS+" "+packageDirectory);
		
		//fetchPermissionRequested.grantPermissions("com.phonepe.app", pathToApk);
		BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line=bufferedReader.readLine();
		int count=0;
		
		String apkPaths="";//appPathDirectory+line;
		while(line!=null)
		{
			//System.out.println("Value of temp is :"+temp);
			count++;
			System.out.println(line);
			line=bufferedReader.readLine();
		//	apkPaths=apkPaths+" "+temp;
		}
		if(count > 1)
		{
			return true;
		}
			return false;
		
	}

}
