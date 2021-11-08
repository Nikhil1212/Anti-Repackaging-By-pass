package uiautomator;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import signatureAddition.DataBaseConnect;

public class ClassAnalysis {

	public static void main(String[] args) throws Exception {
		
		String orig1FilePath="";
		String orig2FilePath="";
		String repackagedFilePath="";
		
		String FilePath="/home/nikhil/Documents/apps/dataset/packageNames.txt";
		File file=new File(FilePath);
		String outputFilePath="/home/nikhil/Documents/apps/VariousClassAnalysis.txt";
		
		Scanner scanner=new Scanner(file);
		HashSet<String> hashSet;
		int count=0;
		HashMap<String, Integer>hashMapOrig1=new HashMap<String, Integer>();
		HashMap<String, Integer>hashMapOrig2=new HashMap<String, Integer>();
		HashMap<String, Integer>hashMapRepackaged=new HashMap<String, Integer>();
		
		
		System.out.println();
		String text="";
		while(scanner.hasNext())
		{
			String packageName="";
			
			packageName=scanner.next();
			text=text+packageName+"\n\n";

			//text
			try
			{
				String suffixs[]= {"run1","run2","repackaged"};
				//packageName="com.bankofbaroda.crismaclegal";
				String dumpDirectoryPath="/home/nikhil/Documents/pythonUIAutomator/"+packageName+"/";
				orig1FilePath=dumpDirectoryPath+"dump_orig_1.xml";
				orig2FilePath=dumpDirectoryPath+"dump_orig_2.xml";
				repackagedFilePath=dumpDirectoryPath+"dump_repackaged.xml";
				String filePaths[]= {orig1FilePath,orig2FilePath,repackagedFilePath};
			
					hashMapOrig1= startingNode(filePaths[0],packageName,3);
					text=text+suffixs[0]+"\n"+hashMapOrig1+"\n\n";
				
					hashMapOrig2= startingNode(filePaths[1],packageName,3);
					text=text+suffixs[1]+"\n"+hashMapOrig2+"\n\n";
				
					hashMapRepackaged= startingNode(filePaths[2],packageName,3);
					text=text+suffixs[2]+"\n"+hashMapRepackaged+"\n\n";
				
					/**
					 * Store this hashMap into the file.
					 */
					int arg1,arg2;
					if(hashMapOrig1.equals(hashMapOrig2))
						arg1=0;
					else
						arg1=1;
					if(hashMapOrig1.equals(hashMapRepackaged))
						arg2=0;
					else
						arg2=1;
					
				
		updateTable(packageName,arg1,arg2);	
				
			}
			catch (Exception e) {
				e.printStackTrace();
			}
//		break;
		}
		ResourceIDAnalysis.writeToFile(outputFilePath, text);
	}

	private static HashMap<String,Integer> startingNode(String filePath, String packageName, int indexOfPattern) throws Exception {
	
		File file = new File(filePath);  
		DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();  
		Document document = documentBuilder.parse(file);  
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  
		//an instance of builder to parse the specified xml file  
		DocumentBuilder db = dbf.newDocumentBuilder();  
		Document doc = db.parse(file);  
		doc.getDocumentElement().normalize();  
		System.out.println("Root element: " + doc.getDocumentElement().getNodeName());  
		HashMap<String,Integer> hashMap=new HashMap<String,Integer>();
		String pattern="text";
		
		if (document.hasChildNodes())   
		{  
			//printNodeList(document.getChildNodes(),pattern);
			hashMap=signatureAddition.ParsingXML.printNodeList(document.getChildNodes(),hashMap,packageName,indexOfPattern); 
			//16 is the index of the text
			
		        System.out.println(Arrays.asList(hashMap));
		            

		}
		return hashMap;

	}
	private static void updateTable(String packageName, int a, int b) throws Exception{

		String checkQuery="Select * from ClassAnalysis_HashMap where packageName='"+packageName+"';";
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
			String query="Insert into ClassAnalysis_HashMap values ('"+packageName+"',"+a+","+b+");";
			System.out.println(query);

			Statement statement=DataBaseConnect.initialization();
			statement.executeUpdate(query);
		}
		else
		{
			String query="Update ClassAnalysis_HashMap set Both_same ="+a+", Original_Repackaged="+b+" where packageName='"+packageName+"';";
			System.out.println(query);
			Statement statement=DataBaseConnect.initialization();
			statement.executeUpdate(query);
		}
	}


}
