package tagDifferenceAnalysis;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Scanner;

import signatureAddition.DataBaseConnect;
import signatureAddition.LogAnalysis_sameApp;
import signatureAddition.StartingPoint;

public class TagDifferenceSameApp {

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		String FilePath="/home/nikhil/Documents/apps/pathToApps_1.txt";
		File file=new File(FilePath);
		Scanner scanner=new Scanner(file);
		while(scanner.hasNext())
		{
			try
			{
				String pathToOriginalApk=scanner.next();
				String packageName=StartingPoint.getPackageName(pathToOriginalApk);
				HashSet<String> disjointTagsOriginalApps=LogAnalysis_sameApp.sameAppTwoTimesLogAnalysis(pathToOriginalApk);
				String tagsDifference=""+disjointTagsOriginalApps;
				updateDatabase(packageName,tagsDifference);	
			}
			catch (Exception e) {
				// TODO: handle exception 
				e.printStackTrace();
			}
		}
	}

	public static void updateDatabase(String packageName, String tagsDifference) throws SQLException {
		// TODO Auto-generated method stub
		String query="Insert into LogAnalysisSameAppPID values ('"+packageName+"','"+tagsDifference+"')";
		System.out.println(query);
		Statement statement=DataBaseConnect.initialization();
		statement.executeUpdate(query);
	}

}
