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
		String FilePath="/home/nikhil/Documents/apps/AppsTransfer.txt";
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
				//updateTable(packageName, 'Y', FilePath, packageName);
			//	updateTable(packageName, 'Y', "Overwrite String values","TempFinalDataset");
				updateTable(packageName,"FinalDataset");

			}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
				

	}

}


	private static void updateTable(String packageName, String tableName) throws SQLException {
		// TODO Auto-generated method stub
		String checkQuery="Select * from "+tableName+" where packageName='"+packageName+"';";
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
			String query="Insert into "+tableName +" values ('"+packageName+"');";
			System.out.println(query);

			Statement statement=DataBaseConnect.initialization();
			statement.executeUpdate(query);
		}
		else
		{
			return ;
		}

	}


	private static void updateTable(String packageName, char c, String remarks, String tableName) throws SQLException {
		// TODO Auto-generated method stub
		
		String checkQuery="Select * from "+tableName+" where packageName='"+packageName+"';";
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
			String query="Insert into "+tableName +" values ('"+packageName+"','"+c+"','"+remarks+"');";
			System.out.println(query);

			Statement statement=DataBaseConnect.initialization();
			statement.executeUpdate(query);
		}
		else
		{
			String query="Update "+tableName+" set IsCheckPresent ='"+c+"' , remarks ='"+remarks+"' where packageName='"+packageName+"';";
			System.out.println(query);

			Statement statement=DataBaseConnect.initialization();
			statement.executeUpdate(query);
		}


		
	}
}
