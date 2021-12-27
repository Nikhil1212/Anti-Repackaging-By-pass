package signatureAddition.pastHardwork;

/**
 * One can ignore this class.
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import databaseUpdate.SQLJavaInterface;
import signatureAddition.DataBaseConnect;
import signatureAddition.StartingPoint;

public class ManualAnalysis {

	public static void main(String[] args) throws IOException, Exception {
		// TODO Auto-generated method stub
		String FilePath="/home/nikhil/Documents/apps/packageNames_1.txt";
		String hello[]= {"12","34"};
		SQLJavaInterface.main(hello);
		File file=new File(FilePath);
		Scanner scanner=new Scanner(file);
		Statement statement=DataBaseConnect.initialization();

		while(scanner.hasNext())
		{
			try
			{
				
				//Statement statement=DataBaseConnect.initialization();
				
				String content=scanner.nextLine();
				String string[]=content.split("[,]", 0);
				String packageName=string[0];
				String booleanValue=string[1];
			//	String packageName=StartingPoint.getPackageName(pathToApk);
				String query="Insert into ManualResults values ('"+packageName+"','"+booleanValue+"');";
				
				//updateTable(packageName,booleanValue);
				statement.executeUpdate(query);
				
			}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
	}
		statement.close();
		

}

	private static void updateTable(String packageName, String booleanValue) {
		// TODO Auto-generated method stub
	//	statement.close();
	}
}
