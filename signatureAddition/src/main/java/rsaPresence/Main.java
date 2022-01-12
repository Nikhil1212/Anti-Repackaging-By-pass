package rsaPresence;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import signatureAddition.CommandExecute;
import signatureAddition.DataBaseConnect;
import signatureAddition.StartingPoint;
import signatureAddition.PullApps.AppsPull;

public class Main {

	public static void main(String[] args) throws FileNotFoundException {

		String pathToFilePackageNames="/home/nikhil/Documents/apps/FinalDataset.txt";
		File file=new File(pathToFilePackageNames);
		Scanner scanner=new Scanner(file);
		int count=0;
		while(scanner.hasNext())
		{
			String packageName=scanner.next();
			String pathToApk=AppsPull.appDirectoryPrefix+packageName+"/base.apk";
			try {
			
				count++;
				if(count<1972)
					continue;
				System.out.println("Package count is :"+count);
				disassembleApk(pathToApk, packageName);
				String pathToDisAssembleCode="/home/nikhil/Documents/apps/"+packageName;
				//Execute the find command
				String findCommand="find "+ pathToDisAssembleCode+"/ -name *.RSA";
				System.out.println(findCommand);
				Process process=CommandExecute.commandExecution(findCommand);
				String line=new BufferedReader(new InputStreamReader(process.getInputStream())).readLine();
				
				System.out.println("Value is :"+line);
				if(line!=null)
				{
					updateTable(packageName,'Y',"v1SigningScheme");
					System.out.println("RSA file is present");
				}
				else
				{
					updateTable(packageName,'N',"v1SigningScheme");
					System.out.println("RSA file is not present");
				}
				CommandExecute.commandExecution("rm -r "+pathToDisAssembleCode);
				
			
			}
			catch (Exception e) {
				
				e.printStackTrace();
			
			} 
		}


	}
	public static void disassembleApk(String apkPath, String packageName) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		String apktoolCommand="/usr/local/bin/apktool d -r -s "+apkPath+" -f -o /home/nikhil/Documents/apps/"+packageName;
		System.out.println(apktoolCommand);
		CommandExecute.commandExecutionSh(apktoolCommand);

	}

	private static void apkSignerVerify(String pathToApk, String packageName) {
		// TODO Auto-generated method stub
		String command=StartingPoint.apksigner+" verify --verbose "+pathToApk;
		Process process;
		try {

			process = CommandExecute.commandExecution(command);
			BufferedReader reader=new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line=reader.readLine();
			boolean result=false;
			while(line!=null)
			{
				System.out.println(line);
				if(line.contains("v1"))
				{
					if(line.contains("true"))
					{
						result=true;
						System.out.println("v1 signing is present");
					}
					break;
				}
				line=reader.readLine();

			}
			if(result)
			{
				updateTable(packageName,'Y',"v1SigningScheme");
				System.out.println("RSA file is present");
			}
			else
			{
				updateTable(packageName,'N',"v1SigningScheme");
				System.out.println("RSA file is not present");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 


	}


	public static void updateTable(String packageName, char res, String tableName) {
		// TODO Auto-generated method stub
		String checkQuery="Select packagename from "+tableName+" where packageName ='"+packageName+"';";
		System.out.println(checkQuery);
		Statement statement1=DataBaseConnect.initialization();
		ResultSet resultSet;
		try {
			resultSet = statement1.executeQuery(checkQuery);

			String output="";
			int flag=0;

			while(resultSet.next())
			{
				flag=1;
				output=output+ resultSet.getString(1)+"\n";
			}
			if(flag==0)
			{
				String query="Insert into "+tableName+" values ('"+packageName+"', '"+res+"');";//,'"+remarks+"');";
				System.out.println(query);

				Statement statement=DataBaseConnect.initialization();
				statement.executeUpdate(query);
			}
			else
			{
				String query="Update "+tableName+" set RSA ='"+res+"' where packageName='"+packageName+"';";
				System.out.println(query);

				Statement statement=DataBaseConnect.initialization();
				statement.executeUpdate(query);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



	}

}


