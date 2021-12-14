package databaseUpdate;

/**
 * This class checks whether the apk which we download; is it installable or not. Because in the earlier days, we were using some Google Play API to download the apk, not the front-end automation.
 * 
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import Logs.LogAnalysis;
import signatureAddition.CommandExecute;
import signatureAddition.DataBaseConnect;

public class TableUpdate {

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		String FilePath="/home/nikhil/Documents/apps/rootCheckPresent.txt";
		File file=new File(FilePath);
		Scanner scanner=new Scanner(file);
		int count=0;
		//ExecutePython.downloadApks(FilePath);
		while(scanner.hasNext())
		{
			try
			{
				String packageName=scanner.next();
				System.out.println(packageName);
				updateTable(packageName, 'Y', "from 2nd Run live observation");
				

			}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
				

	}

}

	private static void updateTable(String packageName, char c, String remarks) throws SQLException {
		// TODO Auto-generated method stub
		
		String checkQuery="Select packagename from ManualResults_RootDetectionAnalysis where isCheckPresent='N';";
		System.out.println(checkQuery);
		Statement statement1=DataBaseConnect.initialization();
		ResultSet resultSet=statement1.executeQuery(checkQuery);
		int flag=0;
		String output="";
		while(resultSet.next())
		{
			flag=1;
			output=output+ resultSet.getString(1)+"\n";
		}
		if(flag==0)
		{
			String query="Insert into ManualResults_RootDetectionAnalysis values ('"+packageName+"','"+c+"','"+remarks+"');";
			System.out.println(query);

			Statement statement=DataBaseConnect.initialization();
			statement.executeUpdate(query);
		}
		else
		{
			String query="Update ManualResults_RootDetectionAnalysis set IsCheckPresent ='"+c+"' , remarks ='"+remarks+"' where packageName='"+packageName+"';";
			System.out.println(query);

			Statement statement=DataBaseConnect.initialization();
			statement.executeUpdate(query);
		}


		
	}
}
