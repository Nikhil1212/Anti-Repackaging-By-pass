	package uiautomator;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Scanner;

import analysingDumpSys.DumpSysAnalysis;
import signatureAddition.CommandExecute;
import signatureAddition.DataBaseConnect;
import signatureAddition.LogAnalysis;
import signatureAddition.resignedApp;
/**
 * This class analyses whether an app has anti-tampering check present or not using the front-end automation tool named "uiautomator" by analysing the different widgets on the screen
 * when the original and the repackaged app is run separately.
 *  
 * @author nikhil
 *
 */
public class Main {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String FilePath="/home/nikhil/Documents/apps/packageNames_2.txt";
		File file=new File(FilePath);
		Scanner scanner=new Scanner(file);
		int count=0;
		while(scanner.hasNext())
		{
			String packageName="";
			String pathToDisAssembleCodeDirectory="";
			String pathToResignedApk="";

			try
			{	
				packageName=scanner.next();
				lockUnlockPhone();
				String pathToOriginalApk="/home/nikhil/Downloads/googleplay-api-master/"+packageName+".apk";
				String uiDump_orignal1Path="/home/nikhil/Documents/apps/uiautomator/"+packageName+"_original_1.xml";
				String uiDump_orignal2Path="/home/nikhil/Documents/apps/uiautomator/"+packageName+"_original_2.xml";
				String uiDump_orignal3Path="/home/nikhil/Documents/apps/uiautomator/"+packageName+"_original_3.xml";

				String uiDump_repackagedPath="/home/nikhil/Documents/apps/uiautomator/"+packageName+"_repackaged.xml";


				DumpSysAnalysis.appLaunch(pathToOriginalApk);


				dumpScreenXml(uiDump_orignal1Path,packageName);
				//	screenCapture.captureScreen(image_orignal1Path, packageName);


				//	Thread.sleep(5000);
				dumpScreenXml(uiDump_orignal1Path,packageName);

				/**
				 * 2nd time launch
				 */

				DumpSysAnalysis.appLaunch(pathToOriginalApk);

				dumpScreenXml(uiDump_orignal2Path,packageName);
				dumpScreenXml(uiDump_orignal2Path,packageName);


				//screenCapture.captureScreen(image_orignal2Path, packageName);

				boolean result=	checkTwoUI_XMLSame_ResourceId_Analysis(uiDump_orignal1Path,uiDump_orignal2Path);
				System.out.println(result);
				//	Thread.sleep(20000);
				// result=checkTwoUI_XMLSame(uiDump_orignal1Path,uiDump_orignal2Path);
				if(result)
				{
					/**
					 * Proceed in launching the repackaged version and see the difference
					 */
					pathToResignedApk="/home/nikhil/Documents/apps/ReSignedApks/"+packageName+".apk";

					/**
					 * Creating resigned version
					 */

					resignedApp.signApk(packageName, pathToOriginalApk, pathToResignedApk);

					/**
					 * 
					 * Launch the repackaged app
					 */
					DumpSysAnalysis.appLaunch(pathToResignedApk);

					dumpScreenXml(uiDump_repackagedPath, packageName);
					dumpScreenXml(uiDump_repackagedPath, packageName);

					//screenCapture.captureScreen(image_repackagedPath, packageName);

					result=checkTwoUI_XMLSame_ResourceId_Analysis(uiDump_orignal1Path,uiDump_repackagedPath);
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
						 * It's 100 percent that check is present because we observe different widgets.
						 */

						updateTable(packageName,'Y',remarks);

					}


				}
				else
				{
					String remarks="(new code)Difference in the two UI xml dump files when the same App was run two times";
					/**
					 * Need to change our way of analysis
					 */	 

					updateTable(packageName,'E',remarks);

				}


			}	
			catch (Exception e) {

				e.printStackTrace();
				updateTable(packageName,'E'," Caught in the catch block");

			}
			finally {
				CommandExecute.commandExecution("rm "+pathToResignedApk);
				CommandExecute.commandExecution("rm /home/nikhil/Documents/apps/ReSignedApks/"+packageName+".idsig");
				CommandExecute.commandExecution(LogAnalysis.pathToadb+" uninstall "+packageName);

			}


			count++;
			System.out.println("Number of packages Scanned is :"+count);
		}
	}

	public static void lockUnlockPhone() throws IOException, InterruptedException {

		CommandExecute.commandExecution(LogAnalysis.pathToadb+" shell input keyevent 26");
		CommandExecute.commandExecution(LogAnalysis.pathToadb+" shell input keyevent 26");
		CommandExecute.commandExecution(LogAnalysis.pathToadb+" shell input text 1995");
		CommandExecute.commandExecution(LogAnalysis.pathToadb+" shell input keyevent 66");

	}

	public static boolean checkTwoUI_XMLSame_ClassAnalysis(String uiDump_orignal1Path, String uiDump_orignal2Path) throws IOException {

		HashMap<String, Integer> hashMap1=new HashMap<String, Integer>();
		HashMap<String, Integer> hashMap2=new HashMap<String, Integer>();
		String pattern="class=\"";
		hashMap1= parseXMLDumpFile(uiDump_orignal1Path,hashMap1,pattern);
		hashMap2= parseXMLDumpFile(uiDump_orignal2Path,hashMap2,pattern);

		return hashMap1.equals(hashMap2);
		//return checkTwoHashMapSame(hashMap1,hashMap2);

	}
	public static boolean checkTwoUI_XMLSame_ResourceId_Analysis(String uiDump_orignal1Path, String uiDump_orignal2Path) throws IOException {

		HashMap<String, Integer> hashMap1=new HashMap<String, Integer>();
		HashMap<String, Integer> hashMap2=new HashMap<String, Integer>();
		String pattern="resource-id=\"";
		hashMap1= parseXMLDumpFile(uiDump_orignal1Path,hashMap1,pattern);
		hashMap2= parseXMLDumpFile(uiDump_orignal2Path,hashMap2,pattern);

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

		String checkQuery="Select * from FrontEnd_4 where packageName='"+packageName+"';";
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
			String query="Insert into FrontEnd_4 values ('"+packageName+"','"+c+"','"+remarks+"');";
			System.out.println(query);

			Statement statement=DataBaseConnect.initialization();
			statement.executeUpdate(query);
		}
		else
		{
			String query="Update FrontEnd_4 set IsCheckPresent ='"+c+"', remarks='"+remarks+"' where packageName='"+packageName+"';";
			System.out.println(query);
			Statement statement=DataBaseConnect.initialization();
			statement.executeUpdate(query);
		}
	}

	public static boolean checkTwoUI_XMLSame(String uiDump_orignal1Path, String uiDump_orignal2Path) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
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

		String pullCommand=LogAnalysis.pathToadb+" pull "+tempSmartphonePath+" "+uiDump_Path;
		System.out.println(pullCommand);
		CommandExecute.commandExecution(pullCommand);

		String commandToExecute=LogAnalysis.pathToadb+" shell rm -f "+tempSmartphonePath;
		//CommandExecute.commandExecution(commandToExecute);

	}

}
