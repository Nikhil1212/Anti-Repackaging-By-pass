package signatureAddition.pastHardwork;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class AnalysingJSON {

	public static void main(String[] args) throws IOException, InterruptedException {


		String FilePath="/home/nikhil/Documents/apps/logOutputNew/filteredLogsRemoveDuplicate/json/packageNames.txt";
		String prefixPath="/home/nikhil/Documents/apps/logOutputNew/filteredLogsRemoveDuplicate/json/";
		File file=new File(FilePath);
		String starDesign="\n*************************************\n";
		Scanner scanner=new Scanner(file);
		while(scanner.hasNext())
		{
			String packageName=scanner.next();
			
			String orignalLogJSONPath=prefixPath+"original_"+packageName+".json";
			String resignedLogJSONPath=prefixPath+"resigned_"+packageName+".json";
			String modifiedLogJSONPath=prefixPath+"modifed_"+packageName+".json";
			//String filePath1="/home/nikhil/Documents/apps/logOutputNew/filteredLogsRemoveDuplicate/json/modifed_com.bankofbaroda.mconnect.json";
			//String filePath2="/home/nikhil/Documents/apps/logOutputNew/filteredLogsRemoveDuplicate/json/original_com.bankofbaroda.mconnect.json";

			String original=new String (Files.readAllBytes(Paths.get(orignalLogJSONPath)));
			String resigned=new String (Files.readAllBytes(Paths.get(resignedLogJSONPath)));
			String modified=new String (Files.readAllBytes(Paths.get(modifiedLogJSONPath)));
			//String packageName=getPackageName(filePath1);
			String outputFilePath="/home/nikhil/Documents/apps/logOutputNew/logAnalysis/"+packageName+".txt";
			File fileOutput=new File(outputFilePath);
			fileOutput.createNewFile();
			JSONObject jsonOriginal = new JSONObject(original);  
			JSONObject jsonResigned = new JSONObject(resigned);  
			JSONObject jsonModifed = new JSONObject(modified);  
			String str1=analyseJSONObject(jsonOriginal,jsonResigned);
			String str2=analyseJSONObject(jsonOriginal,jsonModifed);
			
			String fileContents=starDesign+"For the combination orignal and the resigned:\n"+str1+starDesign+"\n For the combination of the original and the modified:"+str2+starDesign;
			
			FileWriter fileWriter=new FileWriter(outputFilePath);
			fileWriter.write(fileContents);
			fileWriter.close();
			/*JSONArray keys = json1.names ();

			for (int i = 0; i < keys.length (); i++) {

				String key = keys.getString (i); // Here's your key
				String value = json1.getString (key); // Here's your value
				System.out.println(key+" : "+value);
				//   Thread.sleep(1000);
			}*/
		}
	}

	private static String analyseJSONObject(JSONObject json1, JSONObject json2) throws IOException {
		// TODO Auto-generated method stub

		String countSame="";
		String countDiff="";
		String componentDifferent="";

		String starDesign="\n*************************************\n";
		JSONArray keys = json1.names ();

		for (int i = 0; i < keys.length (); i++) {

			String key = keys.getString (i); // Here's your key
			String value = json1.getString (key); // Here's your value

			/**
			 * Check if the key is present or not
			 */
			if(json2.has(key))
			{
				String value2=json2.getString(key);
				int v1=Integer.parseInt(value);
				int v2=Integer.parseInt(value2);

				if(v1==v2)
				{
					countSame=countSame+"\n"+key;
				}
				else
				{
					int diff=v1-v2;
					countDiff=countDiff+"\n"+key+" : "+diff;
				}
			}
			else
			{
				componentDifferent=componentDifferent+"\n"+key;
			}
			System.out.println(key+" : "+value);
			//Thread.sleep(1000);
		}
		keys=json2.names();
		for (int i = 0; i < keys.length (); i++) {

			String key = keys.getString (i);
			if(!json2.has(key))
			{
				componentDifferent=componentDifferent+"\n"+key;
			}
		}
		String fileContents=starDesign+"\nComponents having same count :"+countSame+starDesign+"\nComponents having different count :\n"+countDiff+starDesign+"\nComponents not present in other versions :\n"+componentDifferent+starDesign;
		return fileContents;
		/*FileWriter fileWriter=new FileWriter(outputPath);
		fileWriter.write(fileContents);
		fileWriter.close();*/
	}

	private static String getPackageName(String filePath) {
		// TODO Auto-generated method stub
		int index=filePath.lastIndexOf('_');
		return filePath.substring(index+1,filePath.length()-5);
	}

}
