package parseXml;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Iterator;

public class Main {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String packageName="net.one97.paytm";
		String filePath="/home/nikhil/Documents/apps/uiautomator/rootEmulator/net.one97.paytm/rooted.xml";
		HashSet<String> hashSet;
		String patterns[]= {"resource-id=\"","text=\"","package=\"","content-desc=\"","class=\""};
		//patterns[0]="resource-id=\"";
		for(int i=0;i<patterns.length;i++)
		{
			 hashSet=analysePatternInXml(packageName, filePath, patterns[i]);
			 Iterator<String> iterator=hashSet.iterator();
			 
			 System.out.println("Pattern :"+patterns[i]);
			 while(iterator.hasNext())
			 {
				 System.out.println(iterator.next());
			 }
		}
	}
	public static HashSet<String>  analysePatternInXml(String packageName, String filePathPackagesXML, String pattern) throws IOException {
		
		HashSet<String>  hashSet=new HashSet<String>();
		
		String fileContents = new String(Files.readAllBytes(Paths.get(filePathPackagesXML)), StandardCharsets.UTF_8);
		
		while(fileContents.indexOf(pattern)!=-1)
		{
			int index=fileContents.indexOf(pattern);
			String temp=fileContents.substring(index+pattern.length());
			int end=temp.indexOf("\"");
			String value=temp.substring(0,end);
			hashSet.add(value);
	//		System.out.println("pattern :"+ pattern + "value :"+value);
			fileContents=temp.substring(end+1);
			//update the fileContents
		}
		return hashSet;

	}
	

}
