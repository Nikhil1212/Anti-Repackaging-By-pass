package parseXml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Scanner;

import com.mysql.cj.jdbc.result.UpdatableResultSet;

import signatureAddition.AppLaunchAndDump;
import signatureAddition.DataBaseConnect;

public class TextAnalysis {

	public static void main(String[] args) throws IOException {
		
		
		String filePath="/home/nikhil/Documents/apps/falsePositiveResultsEmulator.txt";
		File file=new File(filePath);
		String outputFilePath="/home/nikhil/Documents/apps/TextAnalysis.txt";
		
		String outputContents="";
		Scanner scanner=new Scanner(file);
		while(scanner.hasNext())
		{
			
			String packageName=scanner.next();
			
			String filePathReal="/home/nikhil/Documents/apps/uiautomator/rootEmulator/"+packageName+"/real_BuiltIn.xml";
			
			String filePathRooted="/home/nikhil/Documents/apps/uiautomator/rootEmulator/"+packageName+"/emulator_BuiltIn.xml";
			String pattern="text";
			HashSet<String> countRealtext=nonEmptyText(packageName, filePathReal ,pattern);

		//	pattern="content-desc";
			HashSet<String> countRootedText=nonEmptyText( packageName, filePathRooted,pattern);
			System.out.println(countRootedText);
			if(countRealtext==countRootedText)
				System.out.println("Good to see that");
			else
				System.out.println("Ok, this is not working");
			pattern="content-desc";
			HashSet<String>	countRealDesc=nonEmptyContentDesc (packageName,filePathReal ,pattern);
			HashSet<String>	countRootedDesc=nonEmptyContentDesc( packageName, filePathRooted,pattern);
			
			outputContents=outputContents+"\n"
					+ ""+packageName+"\n"+AppLaunchAndDump.deviceIdSynonym[0]+"\n"+countRealtext+"\n"+AppLaunchAndDump.deviceIdSynonym[2]+"\n"+countRootedText;// + "\n"+AppLaunchAndDump.deviceIdSynonym[0]+"\n"+countRealDesc+"\n"+AppLaunchAndDump.deviceIdSynonym[2]+"\n"+countRootedDesc;
			try
			{
				if(countRealtext.size()!=countRootedText.size())
				{
					Main.updateTable(packageName, 'Y', "Text", "TextAnalysis_rootDetection");
					
				}
				else if (countRealDesc.size()!=countRootedDesc.size())
				{
					Main.updateTable(packageName, 'Y', "Content-Desc", "TextAnalysis_rootDetection");
				}
				else
					Main.updateTable(packageName, 'N', "No difference in text, content-desc", "TextAnalysis_rootDetection");
				
			}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
		
		}
		FileWriter fileWriter=new FileWriter(outputFilePath);
		fileWriter.write(outputContents);
		fileWriter.close();
		
		
	}

	private static void updateTable(String packageName, int countRealtext, int countRootedText, int countRealDesc,
			int countRootedDesc, String tableName) throws SQLException {
		// TODO Auto-generated method stub
		String checkQuery="Select packagename from "+tableName+" where packageName ='"+packageName+"';";
		System.out.println(checkQuery);
		Statement statement1=DataBaseConnect.initialization();
		ResultSet resultSet=statement1.executeQuery(checkQuery);
		int flag=0;
		String output="";
		while(resultSet.next())
		{
			flag=1;
			output=output+ resultSet.getString(1)+"\n";
		}
		if(flag==0)
		{
			String query="Insert into "+tableName+" values ('"+packageName+"',"+countRealtext+","+countRootedText+","+countRealDesc+","+countRootedDesc+");";
			System.out.println(query);

			Statement statement=DataBaseConnect.initialization();
			statement.executeUpdate(query);
		}
		else
		{
			String query="Update "+tableName+" set Text_Real ="+countRealtext+", Text_Root="+countRootedText+", ContentDesc_Real="+countRealDesc+", ContentDesc_Root ="+ countRootedDesc+" where packageName='"+packageName+"';";
			System.out.println(query);

			Statement statement=DataBaseConnect.initialization();
			statement.executeUpdate(query);
		}


		
		
	}

	public static HashSet<String> nonEmptyContentDesc(String packageName, String filePath, String pattern) {
		// TODO Auto-generated method stub
		pattern=pattern+"=\"";
		HashSet<String>  hashSet=new HashSet<String>();
		System.out.println("Pattern value is :"+pattern);
		
		String fileContents="";
		try {
			fileContents = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while(fileContents.indexOf(pattern)!=-1)
		{
			int indexOfPattern=fileContents.indexOf(pattern);
			String temp=fileContents.substring(indexOfPattern+pattern.length());
			int end=temp.indexOf("\"");
			String value=temp.substring(0,end);
			//		System.out.println("pattern :"+ pattern + "value :"+value);
			
			String packageKeywordWithPackageName="package=\""+packageName+"\"";
			
			
			/**
			 * Below code we got the idea while cycling. We want to make sure that the resource-id, class is fetched only for the node 
			 * whose package attribute is set to the package name of the app.
			 */
			int index1=fileContents.indexOf(packageKeywordWithPackageName);
			
			
			
	
			if(index1!=-1 && value.length()!=0 && (index1+packageKeywordWithPackageName.length()+1)==indexOfPattern)
				hashSet.add(value);
			fileContents=temp.substring(end+1);
			
			//update the fileContents
		}
		System.out.println(hashSet);
		return hashSet;
	}

	public static HashSet<String> nonEmptyText(String packageName, String filePath, String pattern) {
		// TODO Auto-generated method stub
		pattern=pattern+"=\"";
		HashSet<String>  hashSet=new HashSet<String>();
		System.out.println("Pattern value is :"+pattern);
		
		String fileContents="";
		try {
			fileContents = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while(fileContents.indexOf(pattern)!=-1)
		{
			int index=fileContents.indexOf(pattern);
			String temp=fileContents.substring(index+pattern.length());
			int end=temp.indexOf("\"");
			String value=temp.substring(0,end);
			//		System.out.println("pattern :"+ pattern + "value :"+value);
			fileContents=temp.substring(end+1);
			
			String patternPackage="package=\""+packageName+"\"";
			String onlyPackageEqual="package=\"";
			
			
			/**
			 * Below code we got the idea while cycling. We want to make sure that the resource-id, class is fetched only for the node 
			 * whose package attribute is set to the package name of the app.
			 */
			int index1=fileContents.indexOf(patternPackage);
			int index2=fileContents.indexOf(onlyPackageEqual);
			
			
	
			if(index1==index2 && index1!=-1 && value.length()!=0 && (! (value.contains("navigationBarBackground") || value.contains("statusBarBackground") )))
				hashSet.add(value);
			
			//update the fileContents
		}
		return hashSet;
		
		
	}

}
