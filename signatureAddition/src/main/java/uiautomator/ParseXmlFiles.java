package uiautomator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;

public class ParseXmlFiles {

	public static void main(String[] args) throws IOException {
		
		String filePath1="/home/nikhil/Documents/apps/uiautomator/InstallerVerification/com.freecharge.android_original_1.xml";
		String filePath2="/home/nikhil/Documents/apps/uiautomator/InstallerVerification/com.freecharge.android_original_sidedload.xml";
		
		HashMap<String, Integer> hashMap1=new HashMap<String, Integer>();
		HashMap<String, Integer> hashMap2=new HashMap<String, Integer>();
		String pattern="resource-id=\"";
		
		hashMap1=parseXMLDumpFile(filePath1, hashMap1,pattern);
		hashMap2=parseXMLDumpFile(filePath2, hashMap2,pattern);
		System.out.println(hashMap1.equals(hashMap2));
		//System.out.println(hashMap.size());
		System.out.println(Arrays.toString(hashMap1.entrySet().toArray()));
		System.out.println(Arrays.toString(hashMap2.entrySet().toArray()));

	}
	private static HashMap<String, Integer> parseXMLDumpFile(String filePath,
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
			String className=temp.substring(startingPoint, i);//temp.substring(temp.indexOf(pattern)+pattern.length(),)str.indexOf(tempstr));
			System.out.println(className);
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

}
