package databaseUpdate;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import signatureAddition.DataBaseConnect;

/**
 * This is for the finding the accuracy for some past work.
 * @author nikhil
 *
 */
public class Evaluation {

	public static void main(String[] args) throws IOException, InterruptedException, SQLException {
	
		Statement statement=DataBaseConnect.initialization();
		/*	fetchPackageNamesFromDataBase(statement);

		System.exit(0);*/
		File file=new File("/home/nikhil/Documents/apps/AppInstallation_10Million.txt");

		Scanner scanner=new Scanner(file);
		int outputRes[]=new int[16];
		//Thread.sleep(10000);
		int[][] matrixSDCBypass = new int[102][4];
		int[][] matrixSDCCheck = new int[102][4];
	
		List<String> list=new ArrayList<String>();
	String appsListName=fillTheMatrixSDCCheck(matrixSDCCheck, statement,scanner);
	scanner=new Scanner(file);
	fillTheMatrixSDCByPass(matrixSDCBypass, statement, scanner, matrixSDCCheck);
//	System.exit(0);
	/*FileWriter fileWriter=new FileWriter("/home/nikhil/Documents/apps/AppInstallation_Million_oneCheck.txt");
	fileWriter.write(appsListName);
	fileWriter.close();
		*/
		//scanner=new Scanner(file);
		/*for(int i=0;i<matrixSDCBypass.length;i++)
		{
			for(int j=0;j<4;j++)
				System.out.print(matrixSDCBypass[i][j]+" ");
			//System.out.println();
		}
	*/
	//	System.exit(0);
	/*for(int i=0;i<matrixSDCCheck.length;i++)
	{
		for(int j=0;j<4;j++)
			System.out.print(matrixSDCCheck[i][j]+" ");
		System.out.println();
	}
		Statement statement2=DataBaseConnect.initialization();
		
		for(int i=0;i<matrixSDCBypass.length;i++)
		{
			for(int j=0;j<4;j++)
				matrixSDCBypass[i][j]=0;
		}
		int count=0;
		String val="";
		
		scanner=new Scanner(file);
		while(scanner.hasNext())
		{
			try
			{
				
				//System.out.println(count);
				String packageName=scanner.nextLine();
				String queryRoot="Select IsByPassable from ToolResult_ByPass_RootDetection where packageName='"+packageName+"';";
				String queryEmulator="Select IsByPassable from ToolResult_ByPass_AntiEmulation where packageName='"+packageName+"';";
				String queryAT="Select IsByPassable from ToolResult_ByPass_AntiTampering where packageName='"+packageName+"';";
				String queryDIF="Select IsByPassable from ToolResult_ByPass_AntiHooking where packageName='"+packageName+"';";
				//
				parseQuery(queryAT,statement2,matrixSDCBypass,0, count);
				parseQuery(queryDIF,statement2,matrixSDCBypass,1, count);
				parseQuery(queryRoot,statement2,matrixSDCBypass,2, count);
				parseQuery(queryEmulator,statement2,matrixSDCBypass,3, count);

				
				if(matrixSDCCheck[count][0]!=0 || matrixSDCCheck[count][1]!=0 ||matrixSDCCheck[count][2]!=0 ||matrixSDCCheck[count][3]!=0)
				{
					if(isMatrixEntrySame(matrixSDCBypass, matrixSDCCheck,count))
					{
						list.add(packageName);
					}
				}
				count++;


			}

			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		
		postMatrixAnalysis(matrixSDCBypass, outputRes);
		
		System.out.println("Apps for which all the SDCs was bypassed, that it has.:"+list.size());
		for(int i=0;i<list.size();i++)
			System.out.println(list.get(i));

		System.out.println("------SDC bypassed matrix");
		for(int i=0;i<matrixSDCBypass.length;i++)
		{
			for(int j=0;j<4;j++)
				System.out.print(matrixSDCBypass[i][j]+" ");
			System.out.println();
		}
		
		for(int i=0;i<outputRes.length;i++)
		{
			if(i%4==0)
			{
				System.out.println("");
			}
			System.out.print(outputRes[i]+",");
			//totalCountCheck+=outputRes[i];
		}
		
	/*	for(int i=0;i<matrixSDCBypass.length;i++)
		{
			if(matrixSDCCheck[i][0]!=0 || matrixSDCCheck[i][1]!=0 ||matrixSDCCheck[i][2]!=0 ||matrixSDCCheck[i][3]!=0)
			{
				if(isMatrixEntrySame(matrixSDCBypass, matrixSDCCheck,i))
				{
					list.add(val);
				}
			}
		}
		int outputRes[]=new int[16];
		for(int i=0;i<outputRes.length;i++)
			outputRes[i]=0;
		postMatrixAnalysis(matrixSDCBypass,outputRes);
		int totalCountCheck=0;
		for(int i=0;i<outputRes.length;i++)
		{
			if(i%4==0)
			{
				System.out.println("");
			}
			System.out.print(outputRes[i]+",");
			totalCountCheck+=outputRes[i];
		}
		System.out.println("\n"+totalCountCheck); */
	}

	private static void fillTheMatrixSDCByPass(int[][] matrix, Statement statement, Scanner scanner, int [][] matrixSDCCheck ) {
		// TODO Auto-generated method stub
		
		int outputRes[]=new int[16];
		Statement statement2=DataBaseConnect.initialization();
				for(int i=0;i<matrix.length;i++)
		{
			for(int j=0;j<4;j++)
				matrix[i][j]=0;
		}
		int count=0;
		String val="";
		String packageNameRes="";
		List<String> list=new ArrayList<String>();
		while(scanner.hasNext())
		{
			try
			{
				String packageName=scanner.nextLine();
				
				//System.out.println(count);
				String queryRoot="Select IsByPassable from ToolResult_ByPass_RootDetection where packageName='"+packageName+"';";
				String queryEmulator="Select IsByPassable from ToolResult_ByPass_AntiEmulation where packageName='"+packageName+"';";
				String queryAT="Select IsByPassable from ToolResult_ByPass_AntiTampering where packageName='"+packageName+"';";
				String queryDIF="Select IsByPassable from ToolResult_ByPass_AntiHooking where packageName='"+packageName+"';";
				//
				parseQuery(queryAT,statement2,matrix,0, count);
				parseQuery(queryDIF,statement2,matrix,1, count);
				parseQuery(queryRoot,statement2,matrix,2, count);
				parseQuery(queryEmulator,statement2,matrix,3, count);

				
				if(matrixSDCCheck[count][0]!=0 || matrixSDCCheck[count][1]!=0 ||matrixSDCCheck[count][2]!=0 ||matrixSDCCheck[count][3]!=0)
				{
					if(isMatrixEntrySame(matrix, matrixSDCCheck,count))
					{
						list.add(packageName);
					}
				}
				count++;

			
			}

			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		System.out.println(list);
		postMatrixAnalysis(matrix, outputRes);
		
		for(int i=0;i<outputRes.length;i++)
		{
			if(i%4==0)
				System.out.println();
			System.out.print(outputRes[i]+", ");
			
		}
		

	}
	

	private static String fillTheMatrixSDCCheck(int[][] matrix, Statement statement, Scanner scanner) {
		// TODO Auto-generated method stub
		int outputRes[]=new int[16];
		Statement statement2=DataBaseConnect.initialization();
				for(int i=0;i<matrix.length;i++)
		{
			for(int j=0;j<4;j++)
				matrix[i][j]=0;
		}
		int count=0;
		String val="";
		String packageNameRes="";
		while(scanner.hasNext())
		{
			try
			{
				
				//System.out.println(count);
				String packageName=scanner.nextLine();
				String queryRoot="Select isCheckPresent from RootDetection_Automation where packageName='"+packageName+"';";
				String queryEmulator="Select isCheckPresent from EmulatorDetection_Automation where packageName='"+packageName+"';";
				String queryAT="Select isCheckPresent from AntiTampering_Automation where packageName='"+packageName+"';";
				String queryDIF="Select isCheckPresent from HookingDetection_Automation where packageName='"+packageName+"';";
				//System.out.println(queryRoot);
				//ResultSet resultSet2=	statement2.executeQuery(queryRoot);
				boolean res1= parseQuery(queryAT,statement2,matrix,0, count);
				boolean res2=parseQuery(queryDIF,statement2,matrix,1, count);
				boolean res3=parseQuery(queryRoot,statement2,matrix,2, count);
				boolean res4=parseQuery(queryEmulator,statement2,matrix,3, count);

				if(res1 || res2 || res3 || res4)
				{
					packageNameRes=packageNameRes+packageName+"\n";
				}
				count++;

			}

			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		postMatrixAnalysis(matrix, outputRes);
		
		for(int i=0;i<outputRes.length;i++)
		{
			if(i%4==0)
				System.out.println();
			System.out.print(outputRes[i]+", ");
			
		}
		return packageNameRes;

	}

	private static boolean parseQuery(String query, Statement statement, int[][] matrix, int i, int count) throws SQLException {
		// TODO Auto-generated method stub
		ResultSet resultSet=statement.executeQuery(query);

		if(resultSet.next())
		{
			String val=resultSet.getString(1);
			if(val.contains("Y"))
			{
				matrix[count][i]++;
				return true;
			}

		}
		return false;

	}

	private static void postMatrixAnalysis(int[][] matrix, int[] outputRes) {
		// TODO Auto-generated method stub
		for(int i=0;i<matrix.length;i++)
		{
			int val=matrix[i][0]*8+matrix[i][1]*4+matrix[i][2]*2+matrix[i][3]*1;
			outputRes[val]++;
		}

	}

	private static boolean isMatrixEntrySame(int[][] matrix1, int[][] matrix2, int index) {
		// TODO Auto-generated method stub
		return (matrix1[index][0]==matrix2[index][0] && matrix1[index][1]==matrix2[index][1] && matrix1[index][2]==matrix2[index][2] && matrix1[index][3]==matrix2[index][3]);

	}



}
