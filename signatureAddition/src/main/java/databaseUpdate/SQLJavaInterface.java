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
	public static File fetchPackageNamesFromDataBase(Statement statement) throws IOException, SQLException {
		// TODO Auto-generated method stub
		String query ="Select packageName from antiTamperingCheckModified_v2 where isCheckPresent='N'";
		
		ResultSet resultSet=statement.executeQuery(query);
		String FileContents="";
		while (resultSet.next()) {
			String packageName=resultSet.getString(1);
			System.out.println(packageName);
			FileContents=FileContents+packageName+"\n";
		}
		String FilePath="/home/nikhil/Documents/apps/DataSet_ImageAnalysis_PackageNames.txt";

		File file=new File(FilePath);
		file.createNewFile();

		FileWriter fileWriter=new FileWriter(FilePath);
		fileWriter.write(FileContents);
		fileWriter.close();

		return file;
	}

}
