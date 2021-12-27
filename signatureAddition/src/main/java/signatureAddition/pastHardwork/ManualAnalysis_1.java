package signatureAddition.pastHardwork;

/**
 * One can ignore this class.
 */
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import signatureAddition.DataBaseConnect;

public class ManualAnalysis_1 {

	public static void main(String[] args) throws SQLException, IOException {
		// TODO Auto-generated method stub
		String query="SELECT antiTamperingCheck.packageName FROM antiTamperingCheck LEFT OUTER JOIN ManualResults ON antiTamperingCheck.packageName = ManualResults.packageName WHERE ManualResults.packageName IS NULL;";
		Statement statement=DataBaseConnect.initialization();
		ResultSet resultSet=statement.executeQuery(query);
		String FileContents="";
		while(resultSet.next())
		{
			String packageName=resultSet.getString(1);
			FileContents=FileContents+packageName+"\n";
		}
		String FilePath="/home/nikhil/Documents/apps/packageNames_1.txt";
		FileWriter fileWriter=new FileWriter(FilePath);
		fileWriter.write(FileContents);
		fileWriter.close();
	}

}
