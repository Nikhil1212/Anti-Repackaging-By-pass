package uiautomator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

import signatureAddition.DataBaseConnect;

public class ParseXmlFiles {

	public static void main(String[] args) throws Exception {

		String filePath="/home/nikhil/Documents/apps/FilteredApps.txt";

		File file=new File(filePath);
		Scanner scanner=new Scanner(file);
		String FileContents="";
		String filePathres="/home/nikhil/Documents/apps/resourceIdAnalysis_1.txt";
		
		while(scanner.hasNext())
		{
			try
			{
				String packageName=scanner.next();
			//	packageName="com.puratech.hdfc";
	//			packageName="com.ausmallfinancebank.amb";
				String filePath1="/home/nikhil/output_yesfromuiautomator";
				String filePath2="/home/nikhil/Documents/apps/uiautomator/AppsAnalysis/"+packageName+"_repackaged.xml";

				HashSet<String> hashSet1=new HashSet<String>();
				HashSet<String> hashSet2=new HashSet<String>();
				String pattern="resource-id=\"";
				List<String> list=new ArrayList<String>();
				list.add("checkable");
				list.add("checked");
				list.add("clickable");
				list.add("enabled");	
				list.add("focuasble");
				list.add("focused");
				list.add("scrollable");
				list.add("long_clickable");
				list.add("selected");
				list.add("Password");
				hashSet1=parseXMLDumpFile(filePath1,hashSet1,pattern);
				hashSet2=parseXMLDumpFile(filePath2,hashSet2,pattern);
			//	hashSet1=parseXMLDumpFile(filePath1,hashSet1,list);
				//hashSet2=parseXMLDumpFile(filePath2,hashSet2,list);

				System.out.println(hashSet1.equals(hashSet2));
				//System.out.println(hashMap.size());
				boolean result=hashSet1.equals(hashSet2);
				int val;
				if(result)
					val=1;
				else
					val=0;
				
				String str1=Arrays.toString(hashSet1.toArray())+"\n";
				String str2=Arrays.toString(hashSet2.toArray())+"\n";
				FileContents=FileContents+packageName+"::"+result+ "\n\n"+str1+str2+"\n***\n";
				System.out.println("\n***\n");
				System.out.println(Arrays.toString(hashSet1.toArray()));
				System.out.println(Arrays.toString(hashSet2.toArray()));
				
//				Thread.sleep(5000);
			}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		//	break;
		}
		FileWriter fileWriter=new FileWriter(filePathres);
		fileWriter.write(FileContents);
		fileWriter.close();

	}
	private static void processingQuery(HashMap<String, Integer> hashMap1, HashMap<String, Integer> hashMap2, String packageName, List<String> list) throws SQLException {

		String query="Insert into UIautomator values ('"+packageName+"'";
		int arr[]=new int [10];
		for(int i=0;i<list.size();i++)
		{
			int val=Math.abs(hashMap2.get(list.get(i))-hashMap1.get(list.get(i)));
			arr[i]=val;
		}
		for(int i=0;i<10;i++)
			query=query+","+arr[i];
		query=query+");";
		System.out.println(query);
		Statement statement=	DataBaseConnect.initialization();
		statement.executeUpdate(query);
	}
	public static HashMap<String, Integer> parseXMLDumpFile(String filePath, HashMap<String, Integer> hashMap,
			List<String> list) throws Exception {
		String fileContents=new String(Files.readAllBytes(Paths.get(filePath)));

		String temp=fileContents;
		for(int i=0;i<list.size();i++)
		{
			String pattern=list.get(i)+"=\"true";
			int count=0;
			while(temp.contains(pattern))
			{
				temp=temp.substring(temp.indexOf(pattern)+pattern.length());
				count++;
			}
			if(count!=0)
			hashMap.put(list.get(i),count);
		}
		return hashMap;

	}

	public static HashMap<String, Integer> parseXMLDumpFile(String filePath,
			HashMap<String, Integer> hashMap, String pattern) throws IOException {
		String fileContents=new String(Files.readAllBytes(Paths.get(filePath)));

		String temp=fileContents;
		while(temp.contains(pattern))
		{
			int startingPoint=temp.indexOf(pattern);
			startingPoint+=pattern.length();
			int i;
			for( i=startingPoint;temp.charAt(i)!=34;i++)
			{

			}
			String className=temp.substring(startingPoint, i);
			System.out.println(className);
			if(className.length()!=0)
			hashMap=updateHashMap(className,hashMap);
			temp=temp.substring(i+1);
		}
		return hashMap;
	}
	private static HashMap<String, Integer> updateHashMap(String className, HashMap<String, Integer> hashMap) {

		if(hashMap.containsKey(className))
		{
			int val=hashMap.get(className);
			hashMap.put(className, (val+1));
		}
		else
			hashMap.put(className, 1);
		return hashMap;
	}
	public static HashSet<String> parseXMLDumpFile(String filePath, HashSet<String> hashSet,
			String pattern) throws IOException {
		// TODO Auto-generated method stub
		String fileContents=new String(Files.readAllBytes(Paths.get(filePath)));

		String temp=fileContents;
		while(temp.contains(pattern))
		{
			int startingPoint=temp.indexOf(pattern);
			startingPoint+=pattern.length();
			int i;
			for( i=startingPoint;temp.charAt(i)!=34;i++)
			{

			}
			String resourceId=temp.substring(startingPoint, i);
			System.out.println(resourceId);
			if(resourceId.length()!=0)
			hashSet.add(resourceId);
			temp=temp.substring(i+1);
		}
		return hashSet;
	}

}
