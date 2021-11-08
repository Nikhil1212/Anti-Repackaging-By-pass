package uiautomator;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import signatureAddition.DataBaseConnect;

public class TextAnalysis {

	public static void main(String[] args) throws Exception {
		
		String orig1FilePath="";
		String orig2FilePath="";
		String repackagedFilePath="";
		
		String FilePath="/home/nikhil/Documents/apps/dataset/packageNames.txt";
		String outputFilePath="/home/nikhil/Documents/apps/TextAnalysis.txt";
		
		File file=new File(FilePath);
		Scanner scanner=new Scanner(file);
		HashSet<String> hashSet;
		int count=0;
		String text="";
		while(scanner.hasNext())
		{
			String packageName="";
			
			packageName=scanner.next();
			text=text+"\n\n"+packageName+"\n";
			
			try
			{
				String dumpDirectoryPath="/home/nikhil/Documents/pythonUIAutomator/"+packageName+"/";
				orig1FilePath=dumpDirectoryPath+"dump_orig_1.xml";
				orig2FilePath=dumpDirectoryPath+"dump_orig_2.xml";
				repackagedFilePath=dumpDirectoryPath+"dump_repackaged.xml";
				String filePaths[]= {orig1FilePath,orig2FilePath,repackagedFilePath};
				String suffixs[]= {"run1","run2","repackaged"};
				
				int arr[]=new int[3];
				for(int i=0;i<filePaths.length;i++)
				{
					hashSet= startingNode(filePaths[i],packageName);
					text=text+suffixs[i]+"\n"+hashSet+"\n\n";

					arr[i]=hashSet.size();
				}
				updateTable(packageName, arr[0], arr[1], arr[2]);	
				
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		ResourceIDAnalysis.writeToFile(outputFilePath, text);
	}

	private static HashSet<String> startingNode(String filePath, String packageName) throws Exception {
	
		File file = new File(filePath);  
		DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();  
		Document document = documentBuilder.parse(file);  
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  
		//an instance of builder to parse the specified xml file  
		DocumentBuilder db = dbf.newDocumentBuilder();  
		Document doc = db.parse(file);  
		doc.getDocumentElement().normalize();  
		System.out.println("Root element: " + doc.getDocumentElement().getNodeName());  
		HashSet<String> hashSet=new HashSet<String>();
		String pattern="text";
		
		if (document.hasChildNodes())   
		{  
			//printNodeList(document.getChildNodes(),pattern);
			hashSet=signatureAddition.ParsingXML.printNodeList(document.getChildNodes(),hashSet,packageName,16); 
			//16 is the index of the text
			System.out.println("from main:"+hashSet);

		}
		return hashSet;

	}
	private static void updateTable(String packageName, int a, int b, int c) throws Exception{

		String checkQuery="Select * from Number_Of_Text where packageName='"+packageName+"';";
		System.out.println(checkQuery);
		Statement statement1=DataBaseConnect.initialization();
		ResultSet  resultSet=statement1.executeQuery(checkQuery);
		int flag=0;
		while(resultSet.next())
		{
			flag=1;
			resultSet.getString(1);
		}
		if(flag==0)
		{
			String query="Insert into Number_Of_Text values ('"+packageName+"',"+a+","+b+","+c+");";
			System.out.println(query);

			Statement statement=DataBaseConnect.initialization();
			statement.executeUpdate(query);
		}
		else
		{
			String query="Update Number_Of_Text set Original_1 ="+a+", Original_2="+b+" , Repackaged = "+ c+" where packageName='"+packageName+"';";
			System.out.println(query);
			Statement statement=DataBaseConnect.initialization();
			statement.executeUpdate(query);
		}
	}


}
