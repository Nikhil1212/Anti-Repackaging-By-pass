package signatureAddition.pastHardwork;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import signatureAddition.DataBaseConnect;

public class AnalyseDumpSys {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Statement statement=DataBaseConnect.initialization();
		//statement.close();
		computeDifference("DumpSysObjectAnalysis");
		computeDifference("Memory_DumpSys_");
		computeDifference("SQL_DumpSys_");
	}

	private static void computeDifference(String tableName) throws Exception{
		// TODO Auto-generated method stub
		String original="Original1";
		String repackaged="Original2";

		String queryOriginal="Select * from "+tableName+original+" order by packageName ASC;";
		String queryRepackaged="Select * from "+tableName+repackaged+" order by packageName ASC;";

		Statement statement=DataBaseConnect.initialization();
		Statement statement1=DataBaseConnect.initialization();

		Statement statement2=DataBaseConnect.initialization();
		ResultSet resultSet=statement.executeQuery(queryOriginal);
		ResultSet resultSet1=statement1.executeQuery(queryRepackaged);
		ResultSetMetaData rsmd = resultSet1.getMetaData();

		int columnsNumber = rsmd.getColumnCount();
		while(resultSet.next() && resultSet1.next())
		{
			int ctr=0;
			int diffArr[]=new int[columnsNumber-1];
			
			String packageName=resultSet.getString(1);
			String packageName2=resultSet1.getString(1);
			System.out.println(packageName+","+packageName2);
			if(packageName.equals(packageName2))
			{
				System.out.println("Good going");
				System.out.println("PackageName is :"+packageName);
				for(int i=2;i<=columnsNumber;i++)
				{
					int t1=resultSet.getInt(i);
					int t2=resultSet1.getInt(i);
					diffArr[ctr++]=t1-t2;
				}
				String insertInNewtableQuery="Insert ignore into Original_Difference_"+tableName+" values ('"+packageName+"'";
				for(int i=0;i<columnsNumber-1;i++)
				{
					insertInNewtableQuery=insertInNewtableQuery+","+diffArr[i];
				}
				insertInNewtableQuery=insertInNewtableQuery+");";

				System.out.println(insertInNewtableQuery);	
				statement2.executeUpdate(insertInNewtableQuery);
			}
			else
			{
				System.out.println("Hey package Name is different, we need to find some other alternatives");
				break;
			}
		}


	}



}
