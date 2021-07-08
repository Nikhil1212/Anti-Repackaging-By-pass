package signatureAddition;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;

public class UpdateTagDifference {

	public static void main(String[] args) throws SQLException, IOException {
		// TODO Auto-generated method stub
		Statement statement=DataBaseConnect.initialization();
		String filePath="/home/nikhil/Documents/apps/VariousTags.txt";
		String fileContents=new String (Files.readAllBytes(Paths.get(filePath)));
		String query="Select * from antiTamperingCheckModified where Remarks LIKE 'Tag Difference:%';";
		System.out.println(query);
		ResultSet resultSet= statement.executeQuery(query);
		String pattern="Tag Difference: ,";
		while(resultSet.next())
		{
			String packageName=resultSet.getString(1);
			String tagDifference=resultSet.getString(3);
			tagDifference=tagDifference.substring(pattern.length());
			HashSet<String> hashSet=new HashSet<String>();
			//System.out.println(tagDifference);
			String string[]=tagDifference.split("[,]", 0);
			int flag=0;
			for(int i=0;i<string.length;i++)
			{
				if(fileContents.contains(string[i]))
					continue;
				else
				{
					flag=1;
					break;
				}
			}
			if(flag==0)
			{
				String updateQuery="Update antiTamperingCheckModified set IsCheckPresent='N', Remarks= 'No Tag Difference on 2nd Pass' where packageName='"+packageName+"';";
				System.out.println(updateQuery);
				Statement statement2=DataBaseConnect.initialization();
				statement2.executeUpdate(updateQuery);
			}
		}
	}
	

}
