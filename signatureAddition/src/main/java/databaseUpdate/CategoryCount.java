package databaseUpdate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import signatureAddition.DataBaseConnect;

public class CategoryCount {

	public static void main(String[] args) throws IOException  {

		int SDCPresent[][]=new int[6][4];
		int SDCByPass[][]=new int[6][4];

		Statement statement=DataBaseConnect.initialization();
		String arrFilePath[]= {"/home/nikhil/Documents/apps/PublicSectorBankApps.txt","/home/nikhil/Documents/apps/PrivateSectorBankApps.txt","/home/nikhil/Documents/apps/SmallFinanceBanksApps.txt","/home/nikhil/Documents/apps/RRB_FinalDatset.txt","/home/nikhil/Documents/apps/ForeignBanks_FinalDatset.txt","/home/nikhil/Documents/apps/Others.txt"};
		for(int i=0;i<arrFilePath.length;i++)
		{
			String filePath=arrFilePath[i];
			int count_AT=0,count_RD=0,count_ED=0,count_DI=0;
			File file=new File(filePath);
			Scanner scanner=new Scanner(file);
			while(scanner.hasNext())
			{
				try
				{
					String packageName=scanner.next();

					String queryRoot="Select IsByPassable from ToolResult_ByPass_RootDetection where packageName='"+packageName+"';";
					String queryEmulator="Select IsByPassable from ToolResult_ByPass_AntiEmulation where packageName='"+packageName+"';";
					String queryAT="Select IsByPassable from ToolResult_ByPass_AntiTampering where packageName='"+packageName+"';";
					String queryDIF="Select IsByPassable from ToolResult_ByPass_AntiHooking where packageName='"+packageName+"';";
					
					boolean res=parseQuery(statement,queryRoot);
					if(res)
						count_RD++;

					res=parseQuery(statement,queryEmulator);
					if(res)
						count_ED++;

					res=parseQuery(statement,queryAT);
					if(res)
						count_AT++;

					res=parseQuery(statement,queryDIF);
					if(res)
						count_DI++;

				}
				catch (Exception e) {

					e.printStackTrace();
				}

			}
			SDCPresent[i][0]=count_AT;
			SDCPresent[i][1]=count_RD;
			SDCPresent[i][2]=count_ED;
			SDCPresent[i][3]=count_DI;
			//break;
		}

		System.out.println("Here comes the matrix..");
		printMatrix(SDCPresent);

	}

	private static boolean parseQuery(Statement statement, String query) throws SQLException {

		ResultSet resultSet=statement.executeQuery(query);
		System.out.println(query);
		if(resultSet.next())
		{
			String res=resultSet.getString(1);
			if(res.equals("Y"))
				return true;
		}
		
		return false;

	}


	private static void printMatrix(int[][] SDCPresent) {
		// TODO Auto-generated method stub
		for(int i=0;i<SDCPresent.length;i++)
		{
			for(int j=0;j<SDCPresent[i].length;j++)
			{
				System.out.print(SDCPresent[i][j]+" ");
			}
			System.out.println();
		}

	}

	public static void executequery(String packageName, String tableName) throws SQLException {

		String checkQuery="Insert into "+tableName+" values ('"+packageName+"');";
		System.out.println(checkQuery);
		Statement statement1=DataBaseConnect.initialization();

		statement1.executeUpdate(checkQuery);

	}

}
