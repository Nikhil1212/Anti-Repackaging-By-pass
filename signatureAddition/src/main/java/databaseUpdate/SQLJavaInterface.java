package databaseUpdate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import signatureAddition.CommandExecute;
import signatureAddition.DataBaseConnect;
import signatureAddition.StartingPoint;

public class SQLJavaInterface {

	public static void main(String[] args) throws IOException, Exception {
		// TODO Auto-generated method stub
		//changeApkNameToPackageName();
		System.out.println("Hi you called me");
	//	Thread.sleep(10000);
		Statement statement=DataBaseConnect.initialization();
		fetchPackageNamesFromDataBase(statement);
		
	}
	private static void changeApkNameToPackageName() throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		String filePath="/home/nikhil/Downloads/ATADetector-master/apks/pathToApps.txt";
		File file=new File(filePath);
		Scanner scanner=new Scanner(file);
		while(scanner.hasNext())
		{
			String apkPath=scanner.next();
			String packageName=StartingPoint.getPackageName(apkPath);
			String command="mv "+apkPath+" "+apkPath.substring(0,(apkPath.lastIndexOf("/")+1))+packageName+".apk";
			System.out.println(command);
			CommandExecute.commandExecution(command);
			//Thread.sleep(5000);
		}
	}
	public static void fetchPackageNamesFromDataBase(Statement statement) throws IOException, SQLException  {
		// TODO Auto-generated method stub
	
		int arr[]=new int[4];
	//	String filePath="/home/nikhil/Documents/apps/few.txt";
	//	.File fileInput=new File(filePath);
		//Scanner scanner=new Scanner(fileInput);
		
		String query ="Select packageName from ManualResults_RootDetectionAnalysis where IsCheckPresent ='Y' and packageName IN (Select packageName from Manual_Emulator_With_Google_API);";
		
		ResultSet resultSet=statement.executeQuery(query);
		
		
		String FileContents="";

		
		while(resultSet.next())
		{
			FileContents=FileContents +resultSet.getString(1)+"\n";
		}
/*		while(scanner.hasNext())
		{
			try
			{
				String packageName=scanner.next();
			//	String query ="Select * from PlayStoreAppDownload where packageName='"+packageName+"'";
				
				//ResultSet resultSet=statement.executeQuery(query);
				if(resultSet.next()) {
					
					String remarks=resultSet.getString(3);

					if(remarks.contains("removed"))
						arr[0]++;
					
					else if(remarks.contains("Premium"))
						arr[1]++;
					
					else if(remarks.contains("country"))
						arr[2]++;
					
					else if(remarks.contains("Hope"))
						arr[3]++;
					
					String res=resultSet.getString(2);
					System.out.println(res);
			
					
					
				if(res.contains("Y"))
					{
						FileContents=FileContents+"\n"+packageName;
					}
					System.out.println(packageName);
					
				}
			}
			catch (Exception e) {
	
				e.printStackTrace();
			}
			
		}*/
		
		
		String outputFilePath="/home/nikhil/Documents/apps/RootCheckPresentTool.txt";
		
		FileWriter fileWriter=new FileWriter(outputFilePath);
		fileWriter.write(FileContents);
		fileWriter.close();

	}

}
