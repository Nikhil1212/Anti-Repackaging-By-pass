package apkDownload;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class updateTableWithAppDownload {
	public static void main(String[] args) throws SQLException, IOException {  

		String filePathDeveloper="D:\\Apps\\txtFilesDataset\\appsNotDownloaded.txt";
		File file=new File(filePathDeveloper);
		Scanner scanner=new Scanner(file);
		String queryForAppsNotDownloaded="Select packageName from appsFinanceCategory where downloaded='N' and Finance=1;";
		Statement statement=initialization();
		ResultSet resultSet=statement.executeQuery(queryForAppsNotDownloaded);

		//now resultSet contains the set of the packagenames which has not been fetched yet
		try {
			while(resultSet.next())
			{
				String devName=resultSet.getString(1);
				Files.write(Paths.get(filePathDeveloper), (devName+"\n").getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*while(scanner.hasNext())
		{
			String packageName=scanner.next();
			String updateQuery="Update appsFinanceCategory set Downloaded='Y' where packageName='";
			updateQuery=updateQuery.concat(packageName+"';");//"Insert ignore into appsFinanceCategory values('"+packageName+"',1,'"+devID+"');";
			System.out.println(updateQuery);
			statement.executeUpdate(updateQuery);
		}*/

	}

	private static Statement initialization() {
		// TODO Auto-generated method stub
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String sqlurl="jdbc:mysql://10.192.38.90:3306/apps";
			Connection cn=DriverManager.getConnection(sqlurl,"nikhil","nikhil");
			Statement st=cn.createStatement();
			return st;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	} 
}

