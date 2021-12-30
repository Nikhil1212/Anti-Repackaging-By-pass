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

/**
 * This is for the finding the accuracy for some past work.
 * @author nikhil
 *
 */
public class AccuracyFind {

	public static void main(String[] args) throws IOException, InterruptedException, SQLException {
		// TODO Auto-generated method stub
		//Retrieve the packageNames and store it in a file.
		Statement statement=DataBaseConnect.initialization();
	/*	fetchPackageNamesFromDataBase(statement);
		
		System.exit(0);*/
		File file=new File("/home/nikhil/Documents/apps/AntiTamperingCheckPresent.txt");

		//Thread.sleep(10000);

		String falseResultsPath="/home/nikhil/Documents/apps/falseResults.txt";
		String falsePositiveResultsPath="/home/nikhil/Documents/apps/falsePositiveResultsRoot.txt";
		String falseNegativeResultsPath="/home/nikhil/Documents/apps/falseNegativeResults.txt";

		Scanner scanner=new Scanner(file);
		int accuracyCount=0;
		File falseResults=new File(falseResultsPath);
		falseResults.createNewFile();
		Statement statement2=DataBaseConnect.initialization();
		Statement statement3=DataBaseConnect.initialization();
		int falsePositive=0,falseNegative=0;
		String appFalsePositive="";
		String appFalseNegative="";
		while(scanner.hasNext())
		{
			try
			{
				String packageName=scanner.nextLine();
				String queryantiEmulatorCheckModified="Select IsBypassable from ByPass_AntiTampering_Automation where packageName='"+packageName+"';";
				String queryManualResults="Select IsBypassable from Manual_ByPass_AntiTampering_Automation where packageName='"+packageName+"';";
				System.out.println(queryantiEmulatorCheckModified);
				ResultSet resultSet2=	statement2.executeQuery(queryantiEmulatorCheckModified);
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
						appFalseNegative=appFalseNegative+packageName+"\n";
					//	Files.write(Paths.get(falseNegativeResultsPath), (packageName+"\n").getBytes(), StandardOpenOption.APPEND);
					}
					else if(valManual.contains("N") && valTool.contains("Y"))
					{
						
						appFalsePositive=appFalsePositive+packageName+"\n";
					//	Files.write(Paths.get(falsePositiveResultsPath), (packageName+"\n").getBytes(), StandardOpenOption.APPEND);
						falsePositive++;
					}

				}

			}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		
		FileWriter fileWriter=new FileWriter(falsePositiveResultsPath);
		fileWriter.write(appFalsePositive);
		fileWriter.close();
		
		fileWriter=new FileWriter(falseNegativeResultsPath);
		fileWriter.write(appFalseNegative);
		fileWriter.close();
		
		System.out.println("\n***********\n Accuracy count is :"+accuracyCount);
		System.out.println("False poisitve:"+falsePositive);
		System.out.println("False neg:"+falseNegative);
		System.out.println("Total false results : "+(falseNegative+falsePositive));

	}

	private static File fetchPackageNamesFromDataBase(Statement statement) throws IOException, SQLException {
		// TODO Auto-generated method stub
		String query ="Select EmulatorDetection_Automation.packageName  from EmulatorDetection_Automation where remarks ='Differnce in :class';";
		ResultSet resultSet=statement.executeQuery(query);
		String FileContents="";
		while (resultSet.next()) {
			String packageName=resultSet.getString(1);
			System.out.println(packageName);
			FileContents=FileContents+packageName+"\n";
		}
		String FilePath="/home/nikhil/Documents/apps/FalsePositiveUsingClass.txt";

		File file=new File(FilePath);
		file.createNewFile();

		FileWriter fileWriter=new FileWriter(FilePath);
		fileWriter.write(FileContents);
		fileWriter.close();

		return file;
	}

}
