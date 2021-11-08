
/**
 * This class takes the apk as an input and inserts the log inside the method whether signature related API is used.
 * It also finds out the number of the logs which we have inserted and how many of them are getting executed.
 */

package LogInsertion;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import signatureAddition.CommandExecute;
import signatureAddition.DataBaseConnect;
import signatureAddition.FileNamesForSignatureAddition;
import signatureAddition.LogAnalysis;
import signatureAddition.StartingPoint;
import signatureAddition.ValidFilesForCodeInjection;

public class InsertLogs {

	public static void main(String[] args) throws Exception{
		
		/**
		 * Take the packageName as an input from the file
		 */
		String FilePath="/home/nikhil/Documents/apps/packageNames_3.txt";
		File file=new File(FilePath);
		Scanner scanner=new Scanner(file);
		//		ExecutePython.downloadApks(FilePath);
		int count=0;
		while(scanner.hasNext())
		{
			String packageName="";
			int numberOfLogsInserted = -1;
			String buildApkPath="";
			String pathToDisAssembleCodeDirectory="";
			try
			{
				 packageName=scanner.next();
				 String pathToOriginalApk="/home/nikhil/Documents/apps/dataset/"+packageName+"/base_3_logInserted.apk";

				 count++;
				/**
				 * Disassemble the apk
				 */
				 
				StartingPoint.disassembleApk(pathToOriginalApk, packageName);
				
				 pathToDisAssembleCodeDirectory="/home/nikhil/Documents/apps/"+packageName;


				 numberOfLogsInserted =executeGrepForEachPattern(packageName,pathToDisAssembleCodeDirectory);

				 buildApkPath= StartingPoint.buildApk(packageName);

				StartingPoint.signApk(packageName, buildApkPath);

				/**
				 * Install the app and fetch the logs
				 */
				String logPathForApp="/home/nikhil/Documents/apps/logsInsertionOutput/"+packageName+".txt";

				String logOutput=LogAnalysis.appLogGeneration(buildApkPath, logPathForApp);

				//System.out.println(logOutput);
				/**
				 * Find the number of times Caught you!! has come
				 */
				String pattern="Caught you!!";
				if(logOutput==null)
				{
					updateLogInsertionTable(packageName,numberOfLogsInserted,-1);
					throw new Exception("Could not retrieve the logs!!");

				}
				int numberOfLogsExexcuted=findNumberOfOccurences(logOutput,pattern);

				/**
				 * Insert into the database
				 */
				updateLogInsertionTable(packageName,numberOfLogsInserted,numberOfLogsExexcuted);
				
			}
			catch (Exception e) {
				// TODO: handle exception
				System.out.println("Caught inside the catch block");
				updateLogInsertionTable(packageName,numberOfLogsInserted,-1);
				e.printStackTrace();
			}
			finally {
			//	CommandExecute.commandExecution("rm "+buildApkPath);
				//CommandExecute.commandExecution(LogAnalysis.pathToadb+" uninstall "+packageName);
				//StartingPoint.removeDirectory(pathToDisAssembleCodeDirectory); 
				System.out.println("Number of apps scanned is :"+count);
			}
			
			
		}

	}

	public static void updateLogInsertionTable(String packageName, int numberOfLogsInserted,
			int numberOfLogsExexcuted) throws Exception {

		String checkQuery="Select * from logsInsertion where packageName='"+packageName+"';";
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
			String query="Insert into logsInsertion values ('"+packageName+"',"+numberOfLogsInserted+","+numberOfLogsExexcuted+");";
			System.out.println(query);

			Statement statement=DataBaseConnect.initialization();
			statement.executeUpdate(query);
		}
		else
		{
			String query="Update logsInsertion set NumberOfLogsInserted ="+numberOfLogsInserted+", NumberOfLogsExecuted="+numberOfLogsExexcuted+" where packageName='"+packageName+"';";
			System.out.println(query);

			Statement statement=DataBaseConnect.initialization();
			statement.executeUpdate(query);
		}
	}

	private static int findNumberOfOccurences(String logOutput, String pattern) {
		// TODO Auto-generated method stub
		String temp=logOutput;
		int count=0;
		while(temp.indexOf(pattern)!=-1)
		{
			count++;
			int index= temp.indexOf(pattern);
			temp=temp.substring(index+pattern.length());
		}
		return count;
	}

	public static int executeGrepForEachPattern(String packageName, String pathToDisAssembleCodeDirectory) throws Exception{

		List<String> signatureMethodList=new ArrayList<String>();
		signatureMethodList=FileNamesForSignatureAddition.listInitializationForSignaturePattern(signatureMethodList);

		//for each of the signature method fetch the files and then insert the method
		int count=0;
		for(int i=0;i<signatureMethodList.size();i++)
		{
			Process process=Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", "grep -r -l '"+signatureMethodList.get(i)+"' "+ pathToDisAssembleCodeDirectory});

			process.waitFor();
			HashSet<String> variousFilesPath=fetchFileNames(process);
			System.out.println(variousFilesPath);
			
			Iterator<String> iterator = variousFilesPath.iterator();
			
			while (iterator.hasNext())
			{
				String filePath=iterator.next();

				count+=ClassMethodLogInsertion.findingMultipleOccurencesOfSamePattern(filePath, signatureMethodList.get(i), count);
			}	 
		}
		return count;
	}

	private static HashSet<String> fetchFileNames(Process process) throws Exception{

		HashSet <String> validFiles=new HashSet<String>();
		BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(process.getInputStream()));

		String line=bufferedReader.readLine();
		System.out.println(line);
		while(line!=null)
		{
			//System.out.println(line);
			/*if(ValidFilesForCodeInjection.fileCheck(line))
			{
				validFiles.add(line);
			}*/
			validFiles.add(line);
	
			line=bufferedReader.readLine();
		}
		System.out.println("List of valid files :"+validFiles);
		return validFiles;

		//return null;
	}
}
