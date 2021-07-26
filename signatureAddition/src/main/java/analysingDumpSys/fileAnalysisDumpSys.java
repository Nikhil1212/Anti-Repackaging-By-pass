package analysingDumpSys;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import signatureAddition.DataBaseConnect;

public class fileAnalysisDumpSys {

	 public static void dumpSysFileAnalysis(String filePath, String flag,String packageName) throws Exception{
		String fileContents=new String(Files.readAllBytes(Paths.get(filePath)));
		String object_TableName="DumpSysObjectAnalysis"+flag;
		
		String SQL_TableName="SQL_DumpSys_"+flag;
		//String SQL_Repackaged_TableName="DumpSysObjectAnalysisOriginal";
		
		String Memory_TableName="Memory_DumpSys_"+flag;
		//String Memory_Original_TableName="DumpSysObjectAnalysisOriginal";
		
		
		
/**
 * Please update the table Name for each of the 
 */
		updateObjects(fileContents,object_TableName,packageName);
		updateMemory(fileContents,Memory_TableName,packageName);
		updateSQL(fileContents,SQL_TableName,packageName);

	}

	private static void updateSQL(String fileContents, String tableName, String packageName) throws Exception{
		// TODO Auto-generated method stub
		int MEMORY_USED=retreive(fileContents, dataMembers.str_MEMORY_USED);
		int PAGECACHE_OVERFLOW=retreive(fileContents, dataMembers.str_PAGECACHE_OVERFLOW);
		int MALLOC_SIZE=retreive(fileContents, dataMembers.str_MALLOC_SIZE);
	
		String checkQuery="Select * from "+tableName+" where packageName='"+packageName+"';";
		System.out.println(checkQuery);
		Statement statement1=DataBaseConnect.initialization();
		ResultSet  resultSet=statement1.executeQuery(checkQuery);
		int flag=0;
		while(resultSet.next())
		{
			flag=1;
			resultSet.getString(1);
		}
		if(flag==1)
		{
			/**
			 * Check if the entry is already present. Then delete it
			 */
			String deleteQuery="Delete from "+tableName+" where packageName='"+packageName+"';";
			System.out.println(deleteQuery);

			Statement statement=DataBaseConnect.initialization();
			statement.executeUpdate(deleteQuery);
		}
		String query="Insert into "+tableName+" values ('"+packageName+"',"+MEMORY_USED+","+PAGECACHE_OVERFLOW+","+MALLOC_SIZE+");";//+Stack+","+Graphics+","+PrivateOther+","+system+","+TOTAL+","+TOTALSWAP+","+assetAllocation+");";

		System.out.println(query);
		Statement statement=DataBaseConnect.initialization();

		try {
			statement.executeUpdate(query);
			System.out.println("Successfully updated the database");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void updateMemory(String fileContents, String tableName, String packageName) throws Exception{
	
		
		int JavaHeap=retreive(fileContents, dataMembers.str_JavaHeap);
		int NativeHeap=retreive(fileContents, dataMembers.str_NativeHeap);
		int Code=retreive(fileContents, dataMembers.str_Code);
		int Stack=retreive(fileContents, dataMembers.str_Stack);
		int Graphics=retreive(fileContents, dataMembers.str_Graphics);
		int PrivateOther=retreive(fileContents, dataMembers.str_PrivateOther);
		int system=retreive(fileContents, dataMembers.str_System);
		int TOTAL=retreive(fileContents, dataMembers.str_TOTAL);
		int TOTALSWAP=retreive(fileContents, dataMembers.str_TOTALSWAP);
		int assetAllocation=retreive(fileContents, dataMembers.str_Asset_Allocations);
		
		

		String checkQuery="Select * from "+tableName+" where packageName='"+packageName+"';";
		System.out.println(checkQuery);
		Statement statement1=DataBaseConnect.initialization();
		ResultSet  resultSet=statement1.executeQuery(checkQuery);
		int flag=0;
		while(resultSet.next())
		{
			flag=1;
			resultSet.getString(1);
		}
		if(flag==1)
		{
			/**
			 * Check if the entry is already present. Then delete it
			 */
			String deleteQuery="Delete from "+tableName+" where packageName='"+packageName+"';";
			System.out.println(deleteQuery);

			Statement statement=DataBaseConnect.initialization();
			statement.executeUpdate(deleteQuery);
		}
		String query="Insert into "+tableName+" values ('"+packageName+"',"+JavaHeap+","+NativeHeap+","+Code+","+Stack+","+Graphics+","+PrivateOther+","+system+","+TOTAL+","+TOTALSWAP+","+assetAllocation+");";

		System.out.println(query);
		Statement statement=DataBaseConnect.initialization();

		try {
			statement.executeUpdate(query);
			System.out.println("Successfully updated the database");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void updateObjects(String fileContents, String tableName, String packageName) throws Exception{
		// TODO Auto-generated method stub
		/**
		 * Fetching the values so that we can store it on the database
		 */
		int AppContexts=retreive(fileContents, dataMembers.str_AppContexts);
		int Activities=retreive(fileContents, dataMembers.str_Activities);
		int Views=retreive(fileContents, dataMembers.str_Views);
		int ViewRootImpl=   retreive(fileContents, dataMembers.str_ViewRootImpl);
		int Assets=retreive(fileContents, dataMembers.str_Assets);
		int AssetManagers=     retreive(fileContents, dataMembers.str_AssetManagers);   
		int LocalBinders =retreive(fileContents, dataMembers.str_LocalBinders);    
		int ProxyBinders=  retreive(fileContents, dataMembers.str_ProxyBinders);     
		int Parcelmemory=  retreive(fileContents, dataMembers.str_Parcelmemory);    
		int Parcelcount=   retreive(fileContents, dataMembers.str_AppContexts);    
		int DeathRecipients=    retreive(fileContents, dataMembers.str_Parcelcount);
		int OpenSSLSockets=retreive(fileContents, dataMembers.str_OpenSSLSockets);
		int WebViews=retreive(fileContents, dataMembers.str_WebViews);

		String checkQuery="Select * from "+tableName+" where packageName='"+packageName+"';";
		System.out.println(checkQuery);
		Statement statement1=DataBaseConnect.initialization();
		ResultSet  resultSet=statement1.executeQuery(checkQuery);
		int flag=0;
		while(resultSet.next())
		{
			flag=1;
			resultSet.getString(1);
		}
		if(flag==1)
		{
			/**
			 * Check if the entry is already present. Then delete it
			 */
			String deleteQuery="Delete from "+tableName+" where packageName='"+packageName+"';";
			System.out.println(deleteQuery);

			Statement statement=DataBaseConnect.initialization();
			statement.executeUpdate(deleteQuery);
		}
		String query="Insert into "+tableName+" values ('"+packageName+"',"+Views+","+ViewRootImpl+","+AppContexts+","+Activities+","+Assets+","+AssetManagers+","+LocalBinders+","+ProxyBinders+","+Parcelmemory+","+Parcelcount+","+DeathRecipients+","+OpenSSLSockets+","+WebViews+");";

		System.out.println(query);
		Statement statement=DataBaseConnect.initialization();

		try {
			statement.executeUpdate(query);
			System.out.println("Successfully updated the database");

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private static int retreive(String fileContents,String object) {
		// TODO Auto-generated method stub
		int i;
		int index=fileContents.indexOf(object);
		if(index==-1)
			return 0;
		//System.out.println(fileContents.substring(index+object.length()));
		for(i=index+object.length();i<fileContents.length();i++)
		{
			if(fileContents.charAt(i)==':')
				break;
			
		}
		
		for(i=i+1;i<fileContents.length();i++)
		{
			if(fileContents.charAt(i)==' ')
				continue;
			else 
				break;
		}
		int start=i;
		for(i=start;i<fileContents.length();i++)
		{
			if(fileContents.charAt(i)==' ' || fileContents.charAt(i)=='\n' || fileContents.charAt(i)=='K')
				break;
		}
		return Integer.parseInt(fileContents.substring(start, i));
	}

}
