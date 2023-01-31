package databaseUpdate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import signatureAddition.DataBaseConnect;

public class IsAppInDataset {

	public static void main(String[] args) throws IOException  {

		String inputFilePath="/home/nikhil/Documents/apps/SmallFinanceBanksApps.txt";
		String outputFilePath="/home/nikhil/Documents/apps/SmallFinanceBanksApps_FinalDataset.txt";
		String outputContents="";
		File file=new File(inputFilePath);
		Scanner scanner=new Scanner(file);
		try
		{
			while(scanner.hasNext())
			{
				String packageName=scanner.next();
				boolean res= checkAppPresent(packageName);
				if(res)
					outputContents=outputContents+packageName+"\n";
			}

		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		FileWriter fileWriter=new FileWriter(outputFilePath);
		fileWriter.write(outputContents);
		fileWriter.close();

	}

	public static boolean checkAppPresent(String packageName) throws SQLException {
		
		String checkQuery="Select * from FinalDataset where packageName = '"+packageName+"'";
		System.out.println(checkQuery);
		Statement statement1=DataBaseConnect.initialization();

		ResultSet resultSet=statement1.executeQuery(checkQuery);

		while(resultSet.next())
		{
			return true;

		}
		return false;
	}

}
