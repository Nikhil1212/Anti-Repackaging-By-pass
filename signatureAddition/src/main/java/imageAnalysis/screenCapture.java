package imageAnalysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import analysingDumpSys.DumpSysAnalysis;
import signatureAddition.CommandExecute;
import signatureAddition.DataBaseConnect;
import signatureAddition.LogAnalysis;
import signatureAddition.resignedApp;
import signatureAddition.pastHardwork.ExecutePython;
import signatureAddition.pastHardwork.updateTableWithAppDownload;

public class screenCapture {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Nikhil. Time ke saath saab hoga!!");
		String FilePath="/home/nikhil/Documents/apps/DataSet_ImageAnalysis_PackageNames.txt";
		String pathToResignedApk="";
		File file=new File(FilePath);
		Scanner scanner=new Scanner(file);
		
		while(scanner.hasNext())
		{
			String packageName="";
			try
			{
				packageName=scanner.next();
//				packageName="com.sbi.upi";
				String pathToOriginalApk="/home/nikhil/Downloads/googleplay-api-master/"+packageName+".apk";
				String image_orignal1Path="/home/nikhil/Documents/apps/screenshot/"+packageName+"_original_1";
				String image_orignal2Path="/home/nikhil/Documents/apps/screenshot/"+packageName+"_original_2";
				String image_repackagedPath="/home/nikhil/Documents/apps/screenshot/"+packageName+"_repackaged";

				//String tempSmartphonePath="/data/local/tmp/"+packageName;

				DumpSysAnalysis.appLaunch(pathToOriginalApk);


				captureScreen(image_orignal1Path,packageName);

				DumpSysAnalysis.appLaunch(pathToOriginalApk);

				captureScreen(image_orignal2Path,packageName);

				/**
				 * 2nd time launch
				 */


				String pythonFilePath="/home/nikhil/Documents/apps/imageDifference.py";

				String commandToExecutePythonScript=ExecutePython.PythonPath+" "+pythonFilePath+" "+image_orignal1Path+" "+image_orignal2Path;
				Process process =	CommandExecute.commandExecution(commandToExecutePythonScript);
				BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(process.getInputStream()));
				String similarityLevel=bufferedReader.readLine();
				double value=Double.parseDouble(similarityLevel);
				if(value<=1)
				{
					System.out.println("Please proceed for the screen capture analysis");

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

					captureScreen(image_repackagedPath, packageName);


					/**
					 * Execute the python Script
					 */

					commandToExecutePythonScript=ExecutePython.PythonPath+" "+pythonFilePath+" "+image_orignal1Path+" "+image_repackagedPath;
					process =	CommandExecute.commandExecution(commandToExecutePythonScript);
					bufferedReader=new BufferedReader(new InputStreamReader(process.getInputStream()));
					similarityLevel=bufferedReader.readLine();
					double val1=Double.parseDouble(similarityLevel);

					commandToExecutePythonScript=ExecutePython.PythonPath+" "+pythonFilePath+" "+image_orignal2Path+" "+image_repackagedPath;
					process =	CommandExecute.commandExecution(commandToExecutePythonScript);
					bufferedReader=new BufferedReader(new InputStreamReader(process.getInputStream()));
					similarityLevel=bufferedReader.readLine();
					double val2=Double.parseDouble(similarityLevel);
					double min=Math.min(val1, val2);
					if(min > 1)
					{
						updateAntiTamperingCheckPresence_Local(packageName, 'Y', "Huge Difference in screen captured :"+min);
					}
					else
					{
						updateAntiTamperingCheckPresence_Local(packageName, 'N', "No difference even in images captured from screenshot. Value :"+min);
					}

					/**
					 * Check the similarity level is same or not
					 */
				}
				else
				{
					System.out.println("For original apps only, there is a huge difference in the images");
					System.out.println(" So, for a very black-box apk, we won't be knowing shall we proceed or not");
					System.out.println("Since, we reached this last step, hence we are concluding that no anti-tampering check is present");
					updateAntiTamperingCheckPresence_Local(packageName, 'N',"Original app screenshot has a huge difference");
				}
				updateTableWithAppDownload(packageName,similarityLevel);
				CommandExecute.commandExecution(LogAnalysis.pathToadb+" uninstall "+packageName);
			}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			CommandExecute.commandExecution(LogAnalysis.pathToadb+" uninstall "+packageName);


		}
	}

	private static void updateAntiTamperingCheckPresence_Local(String packageName, char c, String remarks) throws Exception{
		String checkQuery="Select * from antiTamperingCheckModified_v2_ImageAnalysis where packageName='"+packageName+"';";
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
			String query="Insert into antiTamperingCheckModified_v2_ImageAnalysis values ('"+packageName+"','"+c+"','"+remarks+"');";
			System.out.println(query);

			Statement statement=DataBaseConnect.initialization();
			statement.executeUpdate(query);
		}
		else
		{
			String query="Update antiTamperingCheckModified_v2_ImageAnalysis set IsCheckPresent ='"+c+"', Remarks='"+remarks+"' where packageName='"+packageName+"';";
			System.out.println(query);
			Statement statement=DataBaseConnect.initialization();
			statement.executeUpdate(query);
		}

	}



	public static void captureScreen(String destinationPath, String packageName) throws Exception{
		// TODO Auto-generated method stub
		String tempSmartphonePath="/data/local/tmp/"+packageName;
		String commandToCaptureScreenShot=LogAnalysis.pathToadb+" shell screencap -p > "+tempSmartphonePath;

		Process process=CommandExecute.commandExecution(commandToCaptureScreenShot);

		String pullCommand=LogAnalysis.pathToadb+" pull "+tempSmartphonePath+" "+destinationPath;
		System.out.println(pullCommand);
		CommandExecute.commandExecution(pullCommand);
		
		String commandToExecute=LogAnalysis.pathToadb+" shell rm -f "+tempSmartphonePath;
		CommandExecute.commandExecution(commandToExecute);
		
	}

	private static void updateTableWithAppDownload(String packageName, String similarityLevel) throws SQLException {
		// TODO Auto-generated method stub

		String checkQuery="Select * from ImageAnalysis_Original where packageName='"+packageName+"';";
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
			/**
			 * Check if the entry is already present. Then delete it
			 */
			String updateQuery="Update ImageAnalysis_Original set similarityLevel="+similarityLevel+"  where packageName='"+packageName+"';";
			System.out.println(updateQuery);

			Statement statement=DataBaseConnect.initialization();
			statement.executeUpdate(updateQuery);
		}
		else
		{
			String query="Insert into ImageAnalysis_Original values ('"+packageName+"',"+similarityLevel+");";
			System.out.println(query);
			Statement statement=DataBaseConnect.initialization();
			statement.executeUpdate(query);			
		}


	}

}
