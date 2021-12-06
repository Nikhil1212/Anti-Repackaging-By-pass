package uiautomator;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

import InstallerVerification.fetchPermissionRequested;
import Logs.LogAnalysis;
import analysingDumpSys.DumpSysAnalysis;
import signatureAddition.CommandExecute;
import signatureAddition.DataBaseConnect;
import signatureAddition.DumpUIAutomatorPython;
import signatureAddition.RestartADB;
import signatureAddition.resignedApp;
import signatureAddition.pastHardwork.AnalysingJSON;
import signatureAddition.pastHardwork.ExecutePython;
import signatureAddition.pastHardwork.ResignApks;
/**
 * This class analyses whether an app has anti-tampering check present or not using the front-end automation tool named "uiautomator" by analysing the different UI Components on the screen
 * when the original and the repackaged app is run separately.
 *  
 * @author nikhil
 *
 */
public class Main {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String FilePath="/home/nikhil/Documents/apps/dataset/packageNames.txt";
		
		/**
		 * Contains the list of apps that we are analysing.
		 */
		
		File file=new File(FilePath);
		Scanner scanner=new Scanner(file);
		int count=0;
		while(scanner.hasNext())
		{
			String packageName="";
			String pathToResignedApk="";

			try
			{	
				packageName=scanner.next();
				lockUnlockPhone();
				String directoryPath="/home/nikhil/Documents/apps/dataset/"+packageName+"/";
				
				/**
				 * The directory path contains the apk (base as well as split apk) fetched from the Play Store. 
				 */
				
				String dumpDirectoryPath="/home/nikhil/Documents/pythonUIAutomator/"+packageName+"/";
				String uiDump_orignal1Path="/home/nikhil/Documents/pythonUIAutomator/"+packageName+"_original1.xml";
				String uiDump_orignal2Path="/home/nikhil/Documents/pythonUIAutomator/"+packageName+"_original2.xml";
				
				CommandExecute.commandExecution("mkdir "+dumpDirectoryPath);
				
				/**
				 * Used for creating directory so that app specifc dump can be stored inside the folder. 
				 */
				
				String uiDump_repackagedPath="/home/nikhil/Documents/pythonUIAutomator/"+packageName+"_repackaged.xml";
				String outputFilePath="";
				
				appLaunch(packageName,directoryPath);
				
				/**
				 * Once the app has been launched, after some duration of time say 40 seconds, dump the screen using python script that uses uiautomator.
				 */
				
				DumpUIAutomatorPython.main(dumpDirectoryPath+"dump_orig_1.xml");
				HashSet<String>hashSet_original1=new HashSet<String>();
				HashSet<String>hashSet_original2=new HashSet<String>();
			
			//	hashSet_original1=uiautomatorDump(packageName, uiDump_orignal1Path,hashSet_original1); 

				
				/**
				 * 2nd time launch
				 * We want to make sure that the original app when being run two times, it should give the same results.
				 */

				appLaunch(packageName,directoryPath);

				DumpUIAutomatorPython.main(dumpDirectoryPath+"dump_orig_2.xml");
				
				
				resignedApkAndInstall(packageName,directoryPath);
			
				/**
				 * 
				 * Launch the repackaged app
				 */
				DumpSysAnalysis.launchTheApp(packageName);

				DumpUIAutomatorPython.main(dumpDirectoryPath+"dump_repackaged.xml");
				
				/*hashSet_original2=uiautomatorDump(packageName, uiDump_orignal2Path,hashSet_original2); 

				boolean result=	hashSet_original1.equals(hashSet_original2);
				System.out.println(result);
				resultAnalysis(result,packageName,hashSet_original1,uiDump_repackagedPath,directoryPath);
				*/

			}	
			catch (Exception e) {

				e.printStackTrace();
				updateTable(packageName,'E'," Caught in the catch block");

			}
			finally {
				//CommandExecute.commandExecution("rm "+pathToResignedApk);
				CommandExecute.commandExecution("rm -r /home/nikhil/Documents/apps/resigned/"+packageName);
				CommandExecute.commandExecution(LogAnalysis.pathToadb+" uninstall "+packageName);

			}


			count++;
			System.out.println("Number of packages Scanned is :"+count);
		//	break;
		}
	}

	private static void resultAnalysis(boolean result, String packageName, HashSet<String> hashSet_original1, String uiDump_repackagedPath, String directoryPath ) throws Exception {

		if(result)
		{
			/**           
			 * Proceed in launching the repackaged version and see the difference
			 */
			resignedApkAndInstall(packageName,directoryPath);
			HashSet<String>hashSet_repackaged=new HashSet<String>();

			/**
			 * 
			 * Launch the repackaged app
			 */
			DumpSysAnalysis.launchTheApp(packageName);

			hashSet_repackaged=uiautomatorDump(packageName, uiDump_repackagedPath,hashSet_repackaged); 

			//screenCapture.captureScreen(image_repackagedPath, packageName);

			result=hashSet_original1.equals(hashSet_repackaged);
			if(result)
			{
				String remarks="No difference in the behaviour of the original and repackaged app atleast from front-end";
				System.out.println(remarks);
				/**
				 * Declaring that no anti-tampering check is present
				 * 
				 */
				updateTable(packageName,'N',remarks);
			}
			else
			{
				String remarks="Difference in the behaviour of the original and the repackaged app from the front-end";
				System.out.println(remarks);

				/**
				 * It's 100 percent that check is present because we observe different widgets or resource id
				 */

				updateTable(packageName,'Y',remarks);

			}


		}
		else
		{
			String remarks="Difference in the two UI xml dump files when the same App was run two times";
			/**
			 * Need to change our way of analysis
			 */	 

			updateTable(packageName,'E',remarks);

		}

	}

	private static void resignedApkAndInstall(String packageName, String directoryPath) throws Exception {
		// TODO Auto-generated method stub
		String destinationResignedDirectory="/home/nikhil/Documents/apps/resigned/"+packageName+"/";
		CommandExecute.commandExecution("mkdir "+destinationResignedDirectory);
		
		//String pathToResignedApk="/home/nikhil/Documents/apps/ReSignedApks/"+packageName+".apk";
		
		Process process=Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", "ls "+directoryPath+"*"});
		process.waitFor();
		
		BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(process.getInputStream()));
		
		String line=bufferedReader.readLine();
		String apksPath="";
		while(line!=null)
		{
			System.out.println(line);
			String apkName=line.substring(line.lastIndexOf('/')+1);
			
			//Parsing the apk name so that the same name can be used to save the resigned version.
			
			System.out.println(destinationResignedDirectory+"/"+apkName);
			resignedApp.signApk(packageName, line, destinationResignedDirectory+apkName);
			
			apksPath=apksPath+" "+destinationResignedDirectory+apkName;
			line=bufferedReader.readLine();
		}
		appInstallation(packageName, destinationResignedDirectory);

		/**
		 * Creating resigned version
		 */

//		resignedApp.signApk(packageName, pathToOriginalApk, pathToResignedApk);

	}

	private static void appLaunch(String packageName,String directoryPath) throws Exception {
		/**
		 * It installs the app using adb (handles the android bundle also i.e split apks) and then we launch the installed app.
		 */
		
		appInstallation(packageName,directoryPath);
		DumpSysAnalysis.launchTheApp(packageName);
	}

	public static void appInstallation(String packageName, String directoryPath) throws Exception{

		/**
		 * This Directory (pointed by directoryPath) contains the apk fetched from Play Store.
		 */
		CommandExecute.commandExecution(LogAnalysis.pathToadb+" uninstall "+packageName);
		
		/**
		 * uninstall the exisiting app.
		 */
		
		/**
		 * For finding whether the app has been pushed to the PlayStore as *.apk or *.aab
		 */
		
		
		/**
		 * Executing ls through sh (shell)
		 */
		Process process=Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", "ls "+directoryPath+"*"});
		process.waitFor();
		
		
		
		BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(process.getInputStream()));
		
		int ctr=0;
		String line=bufferedReader.readLine();
		String apksPath="";
		while(line!=null)
		{
			if(line.contains(".idsig"))  //apksigner leaves the traces of the idsig file.
			{
				line=bufferedReader.readLine();
				continue;
			}
			System.out.println(line);
			apksPath=apksPath+" "+line;
			line=bufferedReader.readLine();
			ctr++;
		}
		String installCommand="";
		if(ctr==1)
		{
			System.out.println("value of apkPath is :"+apksPath);
			installCommand=LogAnalysis.pathToadb+" install -g "+apksPath;
			System.out.println(installCommand);
		}
		else 
		{
			installCommand=LogAnalysis.pathToadb+" install-multiple -g "+apksPath;
			System.out.println(installCommand);
		}
		CommandExecute.commandExecution(installCommand);
		String pathToApk=directoryPath+"base.apk";
		
		/**
		 * For multiple apks you have to individually give the permission for each of the permission requested.
		 */
		fetchPermissionRequested.grantPermissions(packageName, pathToApk);

	}

	private static HashSet<String> uiautomatorDump(String packageName, String destinationPath,
			HashSet<String> hashSet) throws Exception {
		String uiDump_orignal1Path="/home/nikhil/Documents/apps/uiautomator/"+packageName+"_original_1.xml";
		String uiDump_orignal2Path="/home/nikhil/Documents/apps/uiautomator/"+packageName+"_original_2.xml";
	/*	String uiDump_orignal3Path="/home/nikhil/Documents/apps/uiautomator/"+packageName+"_original_3.xml";
		String uiDump_orignal4Path="/home/nikhil/Documents/apps/uiautomator/"+packageName+"_original_4.xml";
		String uiDump_orignal5Path="/home/nikhil/Documents/apps/uiautomator/"+packageName+"_original_5.xml";
*/
		HashSet<String> hashSet1=new HashSet<String>();//, Integer>();
		HashSet<String> hashSet2=new HashSet<String>();
	/*	HashSet<String> hashSet3=new HashSet<String>();
		HashSet<String> hashSet4=new HashSet<String>();
		HashSet<String> hashSet5=new HashSet<String>();

		/*

		List<String> list=new ArrayList<String>();
		list.add("checkable");
		list.add("checked");
		list.add("clickable");
		list.add("enabled");	
		list.add("focuasble");
		list.add("focused");
		list.add("scrollable");
		list.add("long_clickable");
		list.add("selected");
		list.add("Password");*/

		String filePaths[]= {uiDump_orignal1Path,uiDump_orignal2Path};

		for(int i=0;i<filePaths.length;i++)
		{
			dumpScreenXml(filePaths[i], packageName);
			//RestartADB.main();
			//lockUnlockPhone();
		}


		String idClass[]= {"resource-id=\""+packageName};//,"class=\""};


		for(int i=0;i<idClass.length;i++)
		{
			hashSet1= ParseXmlFiles.parseXMLDumpFile(uiDump_orignal1Path,hashSet1,idClass[i]);
			hashSet2= ParseXmlFiles.parseXMLDumpFile(uiDump_orignal2Path,hashSet2,idClass[i]);
		}

		CommandExecute.commandExecution("mv "+ uiDump_orignal2Path+" "+destinationPath);
		System.out.println("mv "+ uiDump_orignal2Path+" "+destinationPath);
		System.out.println(Arrays.toString(hashSet1.toArray()));
		System.out.println(Arrays.toString(hashSet2.toArray()));
		//		System.out.println(Arrays.toString(hashSet3.toArray()));
		return hashSet2;
		//		throw new Exception("The hashset are coming different");

	}


	public static void lockUnlockPhone() throws IOException, InterruptedException {

		CommandExecute.commandExecution(LogAnalysis.pathToadb+" shell input keyevent 26"); //Power button (locked)
		CommandExecute.commandExecution(LogAnalysis.pathToadb+" shell input keyevent 26"); //Power button (again pressed) to see the screen.
		CommandExecute.commandExecution(LogAnalysis.pathToadb+" shell input text 1995"); //Password
		CommandExecute.commandExecution(LogAnalysis.pathToadb+" shell input keyevent 66");  //Enter Key

	}

	public static boolean checkTwoUI_XMLSame_ClassAnalysis(String uiDump_orignal1Path, String uiDump_orignal2Path) throws IOException {

		HashMap<String, Integer> hashMap1=new HashMap<String, Integer>();
		HashMap<String, Integer> hashMap2=new HashMap<String, Integer>();
		String pattern="class=\"";
		hashMap1= parseXMLDumpFile(uiDump_orignal1Path,hashMap1,pattern);
		hashMap2= parseXMLDumpFile(uiDump_orignal2Path,hashMap2,pattern);

		return hashMap1.equals(hashMap2);
	}
	public static boolean checkTwoUI_XMLSame_ResourceId_Analysis(String uiDump_orignal1Path, String uiDump_orignal2Path) throws Exception {

		HashMap<String, Integer> hashMap1=new HashMap<String, Integer>();
		HashMap<String, Integer> hashMap2=new HashMap<String, Integer>();


		String pattern="resource-id=\"";


		hashMap1= ParseXmlFiles.parseXMLDumpFile(uiDump_orignal1Path,hashMap1,pattern);
		hashMap2= ParseXmlFiles.parseXMLDumpFile(uiDump_orignal2Path,hashMap2,pattern);

		return hashMap1.equals(hashMap2);
		//return checkTwoHashMapSame(hashMap1,hashMap2);

	}

	private static HashMap<String, Integer> parseXMLDumpFile(String filePath,
			HashMap<String, Integer> hashMap, String pattern) throws IOException {
		//String 
		String fileContents=new String(Files.readAllBytes(Paths.get(filePath)));

		String temp=fileContents;
		while(temp.contains(pattern))
		{
			int startingPoint=temp.indexOf(pattern);
			startingPoint+=pattern.length();
			int i;
			for( i=startingPoint;temp.charAt(i)!=34;i++) //34 is the ASCII for double quotes 
			{

			}
			String valueId=temp.substring(startingPoint, i);//temp.substring(temp.indexOf(pattern)+pattern.length(),)str.indexOf(tempstr));
			System.out.println(valueId);
			hashMap=updateHashMap(valueId,hashMap);
			temp=temp.substring(i+1);
		}
		return hashMap;
	}
	private static HashMap<String, Integer> updateHashMap(String className, HashMap<String, Integer> hashMap) {

		if(className.length()==0)
			return hashMap;
		if(hashMap.containsKey(className))
		{
			int val=hashMap.get(className);
			hashMap.put(className, (val+1));
		}
		else
			hashMap.put(className, 1);
		return hashMap;
	}



	private static void updateTable(String packageName, char c, String remarks) throws Exception{

		String checkQuery="Select * from ResourceId_UIAutomator_packageName_2 where packageName='"+packageName+"';";
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
			String query="Insert into ResourceId_UIAutomator_packageName_2 values ('"+packageName+"','"+c+"','"+remarks+"');";
			System.out.println(query);

			Statement statement=DataBaseConnect.initialization();
			statement.executeUpdate(query);
		}
		else
		{
			String query="Update ResourceId_UIAutomator_packageName_2 set IsCheckPresent ='"+c+"', Remarks='"+remarks+"' where packageName='"+packageName+"';";
			System.out.println(query);
			Statement statement=DataBaseConnect.initialization();
			statement.executeUpdate(query);
		}
	}

	public static boolean checkTwoUI_XMLSame(String uiDump_orignal1Path, String uiDump_orignal2Path) throws IOException, InterruptedException {
		String pathToDiff="/usr/bin/diff";
		String commandToExecute=pathToDiff+" "+uiDump_orignal1Path+" "+uiDump_orignal2Path;
		Process process=CommandExecute.commandExecution(commandToExecute);
		BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(process.getInputStream()));
		String output=bufferedReader.readLine();
		if(output==null || output.length()==0)
			return true;
		return false;
	}

	public static void dumpScreenXml(String uiDump_Path, String packageName) throws Exception{
		String tempSmartphonePath="/data/local/tmp/"+packageName+".txt";
		String commandToCaptureScreenShot=LogAnalysis.pathToadb+" shell uiautomator dump "+tempSmartphonePath;///data/local/tmp/"+packageName+".txt";

		Process process=CommandExecute.commandExecution(commandToCaptureScreenShot);
		BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line=bufferedReader.readLine();
		//	System.out.println(line);
		while(line!=null)
		{
			System.out.println(line);
			line=bufferedReader.readLine();
		}
		String pullCommand=LogAnalysis.pathToadb+" pull "+tempSmartphonePath+" "+uiDump_Path;
		System.out.println(pullCommand);
		CommandExecute.commandExecution(pullCommand);

		String commandToExecute=LogAnalysis.pathToadb+" shell rm -f "+tempSmartphonePath;
		//CommandExecute.commandExecution(commandToExecute);

	}

}
