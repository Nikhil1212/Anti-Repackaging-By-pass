package databaseUpdate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import signatureAddition.DataBaseConnect;

public class AccuracyFind {

	public static void main(String[] args) throws SQLException, IOException, InterruptedException {
		// TODO Auto-generated method stub
		//Retrieve the packageNames and store it in a file.
		Statement statement=DataBaseConnect.initialization();
		//statement.close();
		File file=fetchPackageNamesFromDataBase(statement);
		Thread.sleep(10000);
		
		String falseResultsPath="/home/nikhil/Documents/apps/falseResults.txt";
		String falsePositiveResultsPath="/home/nikhil/Documents/apps/falsePositiveResults.txt";
		String falseNegativeResultsPath="/home/nikhil/Documents/apps/falseNegativeResults.txt";
		
		Scanner scanner=new Scanner(file);
		int accuracyCount=0;
		File falseResults=new File(falseResultsPath);
		falseResults.createNewFile();
		Statement statement2=DataBaseConnect.initialization();
		Statement statement3=DataBaseConnect.initialization();
		int falsePositive=0,falseNegative=0;
		while(scanner.hasNext())
		{
			String packageName=scanner.nextLine();
			String queryantiTamperingCheckModified="Select ischeckPresent from antiTamperingCheckModified where packageName='"+packageName+"';";
			String queryManualResults="Select ischeckPresent from ManualResults where packageName='"+packageName+"';";
			System.out.println(queryantiTamperingCheckModified);
			ResultSet resultSet2=	statement2.executeQuery(queryantiTamperingCheckModified);
			resultSet2.next();
			System.out.println(queryManualResults);
			ResultSet resultSet3=  statement3.executeQuery(queryManualResults);
			resultSet3.next();
			String valTool=resultSet2.getString(1);
			String valManual=resultSet3.getString(1);
			//System.out.println(val1);
			if(valTool.equals(valManual))
				accuracyCount++;
			else
			{
				if(valManual.contains("Y") && valTool.contains("N"))
				{
					falseNegative++;
					Files.write(Paths.get(falseNegativeResultsPath), (packageName+"\n").getBytes(), StandardOpenOption.APPEND);

				}
				else 
				{
					Files.write(Paths.get(falsePositiveResultsPath), (packageName+"\n").getBytes(), StandardOpenOption.APPEND);
					falsePositive++;
				}

			}
		}
		System.out.println("\n***********\n Accuracy count is :"+accuracyCount);
		System.out.println("False poisitve:"+falsePositive);
		System.out.println("False neg:"+falseNegative);
		System.out.println("Total false results : "+(falseNegative+falsePositive));

	}

	private static File fetchPackageNamesFromDataBase(Statement statement) throws IOException, SQLException {
		// TODO Auto-generated method stub
		String query ="Select ResourceId_UIAutomator_packageName_2.packageName from ResourceId_UIAutomator_packageName_2,ManualResults where ManualResults.packageName = ResourceId_UIAutomator_packageName_2.packageName and ManualResults.IsCheckPresent ='N' and ManualResults.IsCheckPresent != ResourceId_UIAutomator_packageName_2.IsCheckPresent;";
		ResultSet resultSet=statement.executeQuery(query);
		String FileContents="";
		while (resultSet.next()) {
			String packageName=resultSet.getString(1);
			System.out.println(packageName);
			FileContents=FileContents+packageName+"\n";
		}
		String FilePath="/home/nikhil/Documents/apps/FilteredApps.txt";

		File file=new File(FilePath);
		file.createNewFile();

		FileWriter fileWriter=new FileWriter(FilePath);
		fileWriter.write(FileContents);
		fileWriter.close();

		return file;
	}

}
