package uiautomator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import signatureAddition.DataBaseConnect;

public class ResourceIDAnalysis {

	public static void main(String[] args) throws Exception {

		String orig1FilePath="";
		String orig2FilePath="";
		String repackagedFilePath="";

		String FilePath="/home/nikhil/Documents/apps/dataset/packageNames_2.txt";
		String outputFilePath="/home/nikhil/Documents/apps/ResourceIdAnalysis.txt";
		File file=new File(FilePath);
		Scanner scanner=new Scanner(file);
		HashSet<String> hashSet;
		int count=0;
		HashMap<String, Integer>hashMap=new HashMap<String, Integer>();
		hashMap.put("text", 16);
		hashMap.put("resource-id", 13);
		hashMap.put("class", 3);
		hashMap.put("package", 11);
		hashMap.put("content-desc", 5);
		hashMap.put("checkable", 1);
		hashMap.put("checked", 2);
		hashMap.put("clickable", 4);
		hashMap.put("enabled", 6);
		hashMap.put("focusable", 7);
		hashMap.put("focused", 8);
		hashMap.put("scrollable", 14);
		hashMap.put("long-clickable", 10);
		hashMap.put("password", 12);
		hashMap.put("selected", 15);
		hashMap.put("visible-to-user", 17);
		String text="";
		String packageNameCheckPresent="";
		String packageNameCheckNotPresent="";
		List<String> list=new ArrayList<>();
		
		
		while(scanner.hasNext())
		{
			String packageName="";

			packageName=scanner.next();
			text=text+packageName+"\n\n";

			try
			{
				String dumpDirectoryPath="/home/nikhil/Documents/apps/uiautomator/rootEmulator/"+packageName+"/";
				orig1FilePath=dumpDirectoryPath+"real.xml";
				orig2FilePath=dumpDirectoryPath+"rooted.xml";
				//repackagedFilePath=dumpDirectoryPath+"dump_repackaged.xml";
				String filePaths[]= {orig1FilePath,orig2FilePath};//,repackagedFilePath};
				//String suffixs[]= {"run1","run2","repackaged"};
				String suffixs[]= {"real","rooted"};//,"repackaged"};
				int arr[]=new int[3];
				HashSet[] hs = { new HashSet<String>(), 
			            new HashSet<String>() 
				};
				
				for(int i=0;i<filePaths.length;i++)
				{
					hashSet= startingNode(filePaths[i],packageName,13);

					/**
					 * Store this hashSet into the file.
					 */
					
					text=text+suffixs[i]+"\n"+hashSet+"\n\n";
					hs[i]=hashSet;
					
					/*if(i==0)
						hashSetRun1=hashSet;
					else if (i==1)
						hashSetRun2=hashSet;*/
					arr[i]=hashSet.size();
				}
				if(!(hs[0].containsAll(hs[1]) && hs[1].containsAll(hs[0])))
				{
					packageNameCheckNotPresent=packageNameCheckNotPresent+packageName+"\n";
					updateTable(packageName, 'Y', "Resource-id analysis");
					list.add(packageName);
				}
				else
				{
					packageNameCheckPresent=packageNameCheckPresent+packageName+"\n";
					updateTable(packageName, 'N', "Resource-id analysis");
				}
				text=text+"\n******\n";

				//updateTable(packageName, arr[0], arr[1], arr[2]);	

			}
			catch (Exception e) {
				e.printStackTrace();
			}
			//	break;
		}
		System.out.println("Conclusion");
		for(int j=0;j<list.size();j++)
			System.out.println(list.get(j));
		writeToFile(outputFilePath,text);

	}

	public static void writeToFile(String outputFilePath, String text) throws Exception{
		File file=new File(outputFilePath);
		file.createNewFile();
		FileWriter fileWriter=new FileWriter(outputFilePath);
		fileWriter.write(text);
		fileWriter.close();
	}

	private static HashSet<String> startingNode(String filePath, String packageName, int indexOfPattern) throws Exception {

		File file = new File(filePath);  
		DocumentBuilder documentBuilder = DocumentBuilderFactory .newInstance().newDocumentBuilder();  
		Document document = documentBuilder.parse(file);  
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  
		//an instance of builder to parse the specified xml file  
		DocumentBuilder db = dbf.newDocumentBuilder();  
		Document doc = db.parse(file);  
		doc.getDocumentElement().normalize();  
		System.out.println("Root element: " + doc.getDocumentElement().getNodeName());  
		HashSet<String> hashSet=new HashSet<String>();

		if (document.hasChildNodes())   
		{  
			//printNodeList(document.getChildNodes(),pattern);
			hashSet=signatureAddition.ParsingXML.printNodeList(document.getChildNodes(),hashSet,packageName,indexOfPattern); 
			//16 is the index of the text
			System.out.println("from main:"+hashSet);
			Iterator<String> iterator = hashSet.iterator();
			List<String> list=new ArrayList<String>();
			while (iterator.hasNext())
			{
				String temp=iterator.next();
				if(!temp.contains(":id"))
					list.add(temp);
			}
			for(int i=0;i<list.size();i++)
			{
				hashSet.remove(list.get(i));
			}
			System.out.println(hashSet);


		}
		return hashSet;

	}
	private static void updateTable(String packageName, char c, String remarks) throws Exception{

		String checkQuery="Select packagename from Automation_RootDetectionAnalysis where packageName='"+packageName+"';";
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
			String query="Insert into Automation_RootDetectionAnalysis values ('"+packageName+"','"+c+"','"+remarks+"');";
			System.out.println(query);

			Statement statement=DataBaseConnect.initialization();
			statement.executeUpdate(query);
		}
		else
		{
	
			String query="Update Automation_RootDetectionAnalysis set IsCheckPresent ='"+c+"' where packageName='"+packageName+"';";
			System.out.println(query);

			Statement statement=DataBaseConnect.initialization();
			statement.executeUpdate(query);
		}

	}

	private static void updateTable(String packageName, int a, int b, int c) throws Exception{

		String checkQuery="Select * from Number_Of_ResourceId where packageName='"+packageName+"';";
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
			String query="Insert into Number_Of_ResourceId values ('"+packageName+"',"+a+","+b+","+c+");";
			System.out.println(query);

			Statement statement=DataBaseConnect.initialization();
			statement.executeUpdate(query);
		}
		else
		{
			String query="Update Number_Of_ResourceId set Original_1 ="+a+", Original_2="+b+" , Repackaged = "+ c+" where packageName='"+packageName+"';";
			System.out.println(query);
			Statement statement=DataBaseConnect.initialization();
			statement.executeUpdate(query);
		}
	}


}
