/**
 * This take the already generated log files (we have removed the repeated statements) as an input and finds out the number of times the 
 * particular component like ActivityTaskManager, ActivityManager, NetworkSecurityConfig has 
 * been called. 
 */

package LogInsertion;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import org.json.JSONObject;

public class componentCount_v2 {

	public static String tagCount(String logFilePath) throws InterruptedException, IOException {
		// TODO Auto-generated method stub
	/*	String filePath="/home/nikhil/Documents/apps/logOutputNew/filteredLogsRemoveDuplicate/absoluteFilePath.txt";
		File file=new File(filePath);
		Scanner scanner=new Scanner(file);
		while (scanner.hasNext()) {
			String logFilePath=scanner.next();*/
			HashMap<String, String> hashMap=new HashMap<String, String>();
			File file2=new File(logFilePath);
			Scanner scanner2=new Scanner(file2);
			while (scanner2.hasNext()) {
				String line =  scanner2.nextLine();
				if(line.charAt(0)=='-')
					continue;
				int start=line.indexOf('/');
				int spaceIndex=line.indexOf(' ');

				int bracketIndex=line.indexOf('(');
				int end;
				if(spaceIndex<bracketIndex)
					end=spaceIndex;
				else
					end=bracketIndex;
				line=line.substring(start+1, end);
				hashMap=updateHashMap(hashMap,line);
			}
			JSONObject json = new JSONObject(hashMap);
			System.out.println(json);
			String jsonFilePath="/home/nikhil/Documents/apps/json/"+logFilePath.substring(logFilePath.lastIndexOf('/')+1,logFilePath.length()-3)+"json";
			System.out.println(jsonFilePath);
			writingJSONObjectToFile(json,jsonFilePath);
			return jsonFilePath;
			//Thread.sleep(1000);
		}
	

	private static void writingJSONObjectToFile(JSONObject json, String filePath) throws IOException {
		// TODO Auto-generated method stub
		//String filePath="";
		File file=new File(filePath);
		file.createNewFile();
		FileWriter fileWriter=new FileWriter(filePath);
		fileWriter.write(json.toString());
		fileWriter.close();
		System.out.println("Successfully wrote to the file");
	}

	private static HashMap<String, String> updateHashMap(HashMap<String, String> hashMap, String line) {
		// TODO Auto-generated method stub
		if(hashMap.containsKey(line))
		{
			String count=hashMap.get(line);
			int temp=Integer.parseInt(count)+1;
			String updateCount=""+temp;
			hashMap.put(line, updateCount);
		}
		else
			hashMap.put(line, ""+1);
		return hashMap;

	}

}
