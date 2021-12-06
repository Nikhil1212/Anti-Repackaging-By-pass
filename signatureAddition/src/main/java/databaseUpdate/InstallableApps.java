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

public class InstallableApps {

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		String FilePath="/home/nikhil/Documents/apps/InvalidAppsPackageNames.txt";
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

				
				String pathToOriginalApk="/home/nikhil/Downloads/ATADetector-master/apks/"+packageName+".apk";
				String uninstallCommand=LogAnalysis.pathToadb+" uninstall "+packageName;
				CommandExecute.commandExecution(uninstallCommand);
				
				String installCommand=LogAnalysis.pathToadb+" install "+pathToOriginalApk;
				Process process=CommandExecute.commandExecution(installCommand);
				
				BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(process.getInputStream()));
				String line=bufferedReader.readLine();
				int flag=0;
				while(line!=null)
				{
					System.out.println(line);
					if(line.contains("Success"))
					{
						updateTable(packageName,'Y');
						flag=1;
						CommandExecute.commandExecution(uninstallCommand);
						break;
					}
					line=bufferedReader.readLine();
				}
				if(flag==0)
				{
					updateTable(packageName,'N');
				}
			}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

	}

}

	private static void updateTable(String packageName, char c) throws SQLException {
		// TODO Auto-generated method stub
		
		String checkQuery="Select * from InstallableApps where packageName='"+packageName+"';";
		System.out.println(checkQuery);
		Statement statement1=DataBaseConnect.initialization();
		ResultSet resultSet=statement1.executeQuery(checkQuery);
		int flag=0;
		while(resultSet.next())
		{
			flag=1;
			resultSet.getString(1);
		}
		if(flag==0)
		{
			String query="Insert into InstallableApps values ('"+packageName+"','"+c+"');";
			System.out.println(query);

			Statement statement=DataBaseConnect.initialization();
			statement.executeUpdate(query);
		}
		else
		{
			String query="Update InstallableApps set IsInstallable ='"+c+"' where packageName='"+packageName+"';";
			System.out.println(query);

			Statement statement=DataBaseConnect.initialization();
			statement.executeUpdate(query);
		}


		
	}
}
