package signatureAddition;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import EachMethodLogInsertionBHIM.Main;
import signatureAddition.pastHardwork.updateTableWithAppDownload;

public class fetchContacts {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		String FilePath="/home/nikhil/Documents/apps/newApps.txt";
		File file=new File(FilePath);
		Scanner scanner=new Scanner(file);
		//	ExecutePython.downloadApks(FilePath);
		String modifiedApkPath1="";
		String modifiedApkPath="";
		int count=0;
		
		while(scanner.hasNext())
		{
			String packageName=scanner.next();
			String apkPath="/home/nikhil/Downloads/googleplay-api-master/"+packageName+".apk";
			String launcherActivityName=fetchLaunchableActivityName(apkPath);
			String remarks="";
			String assemblyDirectory="/home/nikhil/Documents/apps/"+packageName;
			String pathToManifest=assemblyDirectory+"/AndroidManifest.xml";
			if(launcherActivityName==null)
			{
				remarks="Launchable activity name is null.";
				System.out.println(remarks);
				updateTable(packageName,'E',remarks);
				continue;
			}
			StartingPoint.disassembleApk(apkPath,packageName);

			if(!checkContactsPermission(apkPath))
			{
				remarks="This app does not have the permission to read the contact details";
				System.out.println(remarks);
				updateTable(packageName,'E',remarks);
				CommandExecute.commandExecution("rm -r "+assemblyDirectory);

				//updateManifestFile(pathToManifest);
				continue;
			}


			System.out.println("Launchable acitivity name is :"+launcherActivityName);
			String filePath=fetchTheFileName(assemblyDirectory,launcherActivityName);

			try
			{
				if(filePath==null)
				{
					remarks="Unable to find the file Name for the app :";
					updateTable(packageName,'E',remarks);
					CommandExecute.commandExecution("rm -r "+assemblyDirectory);

					continue;
				}



				/**
				 * 
				 * Let's inject the code in the given file Path
				 */

				updateFileByInsertingCode(filePath,launcherActivityName);

				/**
				 * Build the apk, sign it and then do the zipalign.
				 * 
				 */
				StartingPoint.buildApk(packageName);
				modifiedApkPath="/home/nikhil/Documents/apps/modified_"+packageName+".apk";
				modifiedApkPath1="/home/nikhil/Documents/apps/modified_"+packageName+"_1.apk";

				String logPathForOutput="/home/nikhil/Documents/apps/AccessContacts/"+packageName+".txt";
				/**
				 * Let's do zipalign for the Android 11.
				 */
				zipalign(modifiedApkPath,modifiedApkPath1);
				StartingPoint.signApk(packageName, modifiedApkPath1);

				String logs=LogAnalysis.appLogGeneration(modifiedApkPath1, logPathForOutput);
				if(logs.contains("Test User"))
				{
					System.out.println("Yeah, we are able to access the contact details");
					remarks="Success";
					updateTable(packageName,'Y',remarks);

				}
				else
				{
					remarks="Kuch tooh gadbad hai";
					System.out.println(remarks);
					updateTable(packageName,'N',remarks);

				}

				/**
				 * Launch the app and store the logs from logcat to some file
				 */


			}
			catch (Exception e) {
				remarks="From catch block";
				updateTable(packageName,'E',remarks);

				e.printStackTrace();
			}
			finally {
				CommandExecute.commandExecution("rm "+modifiedApkPath);
				CommandExecute.commandExecution("rm "+modifiedApkPath1);
				CommandExecute.commandExecution("rm -r "+assemblyDirectory);
				System.out.println("Number of apps scanned is :"+(++count));
				CommandExecute.commandExecution(LogAnalysis.pathToadb+" uninstall "+packageName);
			}

		}
	}


	private static void updateTable(String packageName, char c, String remarks) throws SQLException {
		// TODO Auto-generated method stub
		String checkQuery="Select * from ContactDetailsFetch where packageName='"+packageName+"';";
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
			String query="Insert into ContactDetailsFetch values ('"+packageName+"','"+c+"','"+remarks+"');";
			System.out.println(query);

			Statement statement=DataBaseConnect.initialization();
			statement.executeUpdate(query);
		}
		else
		{
			String query="Update ContactDetailsFetch set IsCheckPresent ='"+c+"', Remarks='"+remarks+"' where packageName='"+packageName+"';";
			System.out.println(query);
			Statement statement=DataBaseConnect.initialization();
			statement.executeUpdate(query);
		}

	}


	private static void updateManifestFile(String pathToManifest) throws FileNotFoundException {
		// TODO Auto-generated method stub
		File file=new File(pathToManifest);
		Scanner scanner=new Scanner(file);
		String output="";
		int count=0;
		while(scanner.hasNext())
		{
			
		}

	}


	public static boolean checkContactsPermission(String apkPath) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		String aaptstr = "/home/nikhil/Android/Sdk/build-tools/27.0.3/aapt dump badging ";
		aaptstr=aaptstr.concat(apkPath);
		//aaptstr=aaptstr.concat("  | /bin/grep \"launchable\"");
		System.out.println(aaptstr);

		Process pr=CommandExecute.commandExecution(aaptstr);
		//Process pr = run.exec(cmd);
		BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
		String line = "";
		line=buf.readLine();
		int flagRead=0,flagWrite=0;
		//	System.out.println(line);
		while(line!=null)
		{
			if(line.contains("uses-permission: name='android.permission.READ_CONTACTS'"))
			{
				return true;
			}
			
			line=buf.readLine();
		}


		return false;
	}


	public static void zipalign(String input, String output) throws IOException, InterruptedException {

		String command="/usr/bin/zipalign -p -f 4 "+input+" "+output;
		CommandExecute.commandExecution(command);
	}


	private static  String fetchLaunchableActivityName(String apkPath) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		int i=15;
		System.out.println("Filename path:"+apkPath);
		String aaptstr = "/home/nikhil/Android/Sdk/build-tools/27.0.3/aapt dump badging ";
		aaptstr=aaptstr.concat(apkPath);
		//aaptstr=aaptstr.concat("  | /bin/grep \"launchable\"");
		System.out.println(aaptstr);

		Process pr=CommandExecute.commandExecution(aaptstr);
		//Process pr = run.exec(cmd);
		BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
		String line = "";
		line=buf.readLine();
		System.out.println(line);
		int flag=0;
		while(line!=null)
		{
			//System.out.println("Inside the loop");
			if(line.contains("launchable-activity"))
			{
				System.out.println(line);
				flag=1;
				break;
			}
			line=buf.readLine();
			
		}
		if(flag==0)
			System.out.println("Unable to fetch the launcher activity name");
		else
		{
			System.out.println("Value of the line is :"+line);
			int startingPoint=line.indexOf('=');

			/*System.out.println("Value of line:"+line);
			 */
			for( i=startingPoint+2;line.charAt(i)!=' ';i++)
			{

			}
			return line.substring(startingPoint+2,i-1);
		}

		return null;
	}


	private static void updateFileByInsertingCode(String filePath,  String launcherActivityName) throws IOException {
		// TODO Auto-generated method stub
		String filePathLoadContactsSmali="/home/nikhil/Documents/apps/loadContacts.smali";

		File file=new File(filePath);
		Scanner scanner=new Scanner(file);
		String output="";

		String methodInvoke="invoke-virtual {p0}, L"+replaceDotWithSlash(launcherActivityName)+";->loadContacts()V";
		System.out.println("Method to be invoked is :"+methodInvoke);
		String keywordForOnCreate="onCreate(Landroid/os/Bundle;)V";

		while(scanner.hasNext())
		{
			String line=scanner.nextLine();

			/**
			 * Assuming that there will be only one Such OnCreate and the scope will be protected..
			 */
			if(line.contains(keywordForOnCreate) && line.contains(".method"))
			{

				String fileContents=fetchFileContents(filePathLoadContactsSmali,launcherActivityName);//new String(Files.readAllBytes(Paths.get(filePathLoadContactsSmali)));
				output=output+fileContents+"\n";
				/**
				 * Inserted the method definition
				 */

				output=output+line+"\n"; //the Oncreate line
				//line=scanner.nextLine();
				String localsString=scanner.nextLine();
				output=output+localsString+"\n";
				if(localsString.length()==0)
				{
					while(localsString.length()==0)
					{
						localsString=scanner.nextLine();
						output=output+localsString+"\n";

					}

				}

				output=output+methodInvoke+"\n";

				/**
				 * 
				 */
			}
			else
				output=output+line+"\n";

		}
		Main.writeToTheFile(output, filePath);

	}


	private static String fetchFileContents(String filePathLoadContactsSmali,String launcherActivityName) throws FileNotFoundException {
		// TODO Auto-generated method stub
		File file=new File(filePathLoadContactsSmali);
		String launcherActivityNameWithSlash=replaceDotWithSlash(launcherActivityName);
		String forGetContentResolver="invoke-virtual {p0}, L"+launcherActivityNameWithSlash+";->getContentResolver()Landroid/content/ContentResolver;";
		Scanner scanner=new Scanner(file);
		String output="";
		while(scanner.hasNext())
		{
			String line=scanner.nextLine();
			if(line.contains("getContentResolver()"))
			{
				output=output+forGetContentResolver+"\n";
			}
			else
				output=output+line+"\n";


		}

		return output;
	}


	private static String fetchTheFileName(String assemblyDirectory, String launcherActivityName) {
		// TODO Auto-generated method stub
		if(checkForBaseSmali(assemblyDirectory,launcherActivityName))
		{
			String filePath=assemblyDirectory+"/smali/"+replaceDotWithSlash(launcherActivityName)+".smali";
			System.out.println(filePath);
			return filePath;
		}
		else
		{

			int i=2;
			while(true)
			{
				if(checkFileExists(assemblyDirectory,launcherActivityName,i))
				{
					System.out.println("Yes, we got the file name:");
					String filePath=assemblyDirectory+"/smali_classes"+i+"/"+replaceDotWithSlash(launcherActivityName)+".smali";
					System.out.println(filePath);
					return filePath;

				}
				i++;
				if(i>10)
				{
					System.out.println("Something is missing. We could not find the file yet.");
					break;
				}
			}
			return null;
		}
	}

	private static boolean checkFileExists(String assemblyDirectory, String launcherActivityName, int i) {

		String filePath=assemblyDirectory+"/smali_classes"+i+"/"+replaceDotWithSlash(launcherActivityName)+".smali";

		try
		{
			File file=new File(filePath);
			if(file.exists())
			{
				return true;
			}
			return false;

		}
		catch (Exception e) {
			return false;
		}
	}

	private static boolean checkForBaseSmali(String assemblyDirectory, String launcherActivityName) {
		/**
		 * Generate the possible path
		 */
		String filePath=assemblyDirectory+"/smali/"+replaceDotWithSlash(launcherActivityName)+".smali";

		//	System.out.println(filePath);

		try
		{
			File file=new File(filePath);
			if(file.exists())
			{
				return true;
			}
			return false;

		}
		catch (Exception e) {
			return false;
		}

	}

	private static String replaceDotWithSlash(String input) {
		// TODO Auto-generated method stub
		char ch[]=input.toCharArray();
		for(int i=0;i<input.length();i++)
		{
			if(ch[i]=='.')
				ch[i]='/';

		}
		return new String(ch);

	}

}
