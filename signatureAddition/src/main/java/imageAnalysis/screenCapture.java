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
import signatureAddition.pastHardwork.ExecutePython;
import signatureAddition.pastHardwork.updateTableWithAppDownload;

public class screenCapture {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Nikhil. Time ke saath saab hoga!!");
		String FilePath="/home/nikhil/Documents/apps/packageNames_2.txt";
		File file=new File(FilePath);
		Scanner scanner=new Scanner(file);

		while(scanner.hasNext())
		{
			try
			{
				String packageName=scanner.next();
				String pathToOriginalApk="/home/nikhil/Downloads/googleplay-api-master/"+packageName+".apk";
				DumpSysAnalysis.appLaunch(pathToOriginalApk);
				String image1Path="/home/nikhil/Documents/apps/screenshot/"+packageName+"_original_1";
				String image2Path="/home/nikhil/Documents/apps/screenshot/"+packageName+"_original_2";
				String tempSmartphonePath="/data/local/tmp/"+packageName;
				
				
				captureScreen(image1Path,packageName);

				DumpSysAnalysis.appLaunch(pathToOriginalApk);
				
				captureScreen(image2Path,packageName);

				/**
				 * 2nd time launch
				 */
				
				
				String pythonFilePath="/home/nikhil/Documents/apps/imageDifference.py";
			
				String commandToExecutePythonScript=ExecutePython.PythonPath+" "+pythonFilePath+" "+image1Path+" "+image2Path;
				Process process =	CommandExecute.commandExecution(commandToExecutePythonScript);
				BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(process.getInputStream()));
				String similarityLevel=bufferedReader.readLine();
				updateTableWithAppDownload(packageName,similarityLevel);
				CommandExecute.commandExecution(LogAnalysis.pathToadb+" uninstall "+packageName);
			}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		}
	}

	private static void captureScreen(String destinationPath, String packageName) throws Exception{
		// TODO Auto-generated method stub
		String tempSmartphonePath="/data/local/tmp/"+packageName;
		String commandToCaptureScreenShot=LogAnalysis.pathToadb+" shell screencap -p > "+tempSmartphonePath;

		Process process=CommandExecute.commandExecution(commandToCaptureScreenShot);
		
		String pullCommand=LogAnalysis.pathToadb+" pull "+tempSmartphonePath+" "+destinationPath;
		System.out.println(pullCommand);
		CommandExecute.commandExecution(pullCommand);
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
