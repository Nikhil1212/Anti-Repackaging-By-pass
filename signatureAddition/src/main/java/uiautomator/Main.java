package uiautomator;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.Statement;
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
		String FilePath="/home/nikhil/Downloads/googleplay-api-master/packageNames.txt";
		File file=new File(FilePath);
		Scanner scanner=new Scanner(file);
//		ExecutePython.downloadApks(FilePath);
		int count=0;
		while(scanner.hasNext())
		{
			String packageName="";
			String pathToDisAssembleCodeDirectory="";
			String pathToResignedApk="";

			try
			{
				packageName=scanner.next();
				//packageName="com.icici.ismartcity";
				//String passwordGive=LogAnalysis.pathToadb+" shell input text 1995";//+password;
				
				//CommandExecute.commandExecution(passwordGive);
				
				String pathToOriginalApk="/home/nikhil/Downloads/googleplay-api-master/"+packageName+".apk";
				String uiDump_orignal1Path="/home/nikhil/Documents/apps/uiautomator/"+packageName+"_original_1.xml";
				String uiDump_orignal2Path="/home/nikhil/Documents/apps/uiautomator/"+packageName+"_original_2.xml";
				String uiDump_repackagedPath="/home/nikhil/Documents/apps/uiautomator/"+packageName+"_repackaged.xml";
				/*String image_orignal1Path="/home/nikhil/Documents/apps/screenshot/"+packageName+"_original_1";
				String image_orignal2Path="/home/nikhil/Documents/apps/screenshot/"+packageName+"_original_2";
				String image_repackagedPath="/home/nikhil/Documents/apps/screenshot/"+packageName+"_repackaged";
				 */

				DumpSysAnalysis.appLaunch(pathToOriginalApk);


				dumpScreenXml(uiDump_orignal1Path,packageName);
				//	screenCapture.captureScreen(image_orignal1Path, packageName);

				/**
				 * 2nd time launch
				 */


				DumpSysAnalysis.appLaunch(pathToOriginalApk);

				dumpScreenXml(uiDump_orignal2Path,packageName);

				//screenCapture.captureScreen(image_orignal2Path, packageName);


				boolean result=checkTwoUI_XMLSame(uiDump_orignal1Path,uiDump_orignal2Path);
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

					//screenCapture.captureScreen(image_repackagedPath, packageName);

					 result=checkTwoUI_XMLSame(uiDump_orignal1Path,uiDump_repackagedPath);
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
					String remarks="Difference in the two UI xml dump files when the same App was run two times";
					/**
					 * Need to change our way of analysis
				*/	 

					updateTable(packageName,'E',remarks);

				}
					

			}	
			catch (Exception e) {

				e.printStackTrace();
				updateTable(packageName,'E',"Caught in the catch block");

			}
			finally {
				CommandExecute.commandExecution("rm "+pathToResignedApk);
				CommandExecute.commandExecution("rm /home/nikhil/Documents/apps/ReSignedApks/*.idsig");
				CommandExecute.commandExecution(LogAnalysis.pathToadb+" uninstall "+packageName);

			}
			

			count++;
		System.out.println("Number of packages Scanned is :"+count);
		}
	}

	private static void updateTable(String packageName, char c, String remarks) throws Exception{

		String checkQuery="Select * from pixelAnalysis where packageName='"+packageName+"';";
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
			String query="Insert into pixelAnalysis values ('"+packageName+"','"+c+"','"+remarks+"');";
			System.out.println(query);

			Statement statement=DataBaseConnect.initialization();
			statement.executeUpdate(query);
		}
		else
		{
			String query="Update pixelAnalysis set IsCheckPresent ='"+c+"', Remarks='"+remarks+"' where packageName='"+packageName+"';";
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

	private static void dumpScreenXml(String uiDump_Path, String packageName) throws Exception{
		String tempSmartphonePath="/data/local/tmp/"+packageName+".txt";
		String commandToCaptureScreenShot=LogAnalysis.pathToadb+" shell uiautomator dump "+tempSmartphonePath;///data/local/tmp/"+packageName+".txt";

		Process process=CommandExecute.commandExecution(commandToCaptureScreenShot);

		String pullCommand=LogAnalysis.pathToadb+" pull "+tempSmartphonePath+" "+uiDump_Path;
		System.out.println(pullCommand);
		CommandExecute.commandExecution(pullCommand);

		String commandToExecute=LogAnalysis.pathToadb+" shell rm -f "+tempSmartphonePath;
		CommandExecute.commandExecution(commandToExecute);

	}

}
