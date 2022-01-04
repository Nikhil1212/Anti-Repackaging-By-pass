package finalRun;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import signatureAddition.DataBaseConnect;

public class isDumpGenerated {

	public static void main(String[] args) throws FileNotFoundException {
		
		// TODO Auto-generated method stub
		String pathToApk="/home/nikhil/Documents/apps/tempAntiTamperingApps.txt";
		File file=new File(pathToApk);
		Scanner scanner=new Scanner(file);
		String packageName="";
		
	//	lockUnlockPhone("1995", deviceId[2]);
		
	//	lockUnlockPhone("1234", deviceId[2]);
		
		int count1=0;
		
		while(scanner.hasNext())
		{
			try
			{
				
				packageName=scanner.next();
				String dumpPath="/home/nikhil/Documents/apps/uiautomator/rootEmulator/"+packageName+"/real_BuiltIn.xml";
				String fileContents=new String(Files.readAllBytes(Paths.get(dumpPath)));
				if(!fileContents.contains(packageName))
				{
					updateTable(packageName,"ReRun_AntiTampering");
				}
				
			}
			catch (Exception e) {
				// TODO: handle exception
			}
	}

}

	public static void updateTable(String packageName, String tableName) throws SQLException{

			String checkQuery="Select packagename from "+tableName+" where packageName ='"+packageName+"';";
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
				String query="Insert into "+tableName+" values ('"+packageName+"');";
				System.out.println(query);

				Statement statement=DataBaseConnect.initialization();
				statement.executeUpdate(query);
			}
			else
			{
				return;
			}


			
		}
	
}
