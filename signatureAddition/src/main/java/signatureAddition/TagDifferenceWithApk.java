/**
 * Purpose of this class is to find out if there is any tag difference when the original and the repackaged app is run.
 * The idea is run the original app get the logs, run the repackaged app get the logs and for each of the logs generated, find whether the tag is the part of the apk.
 */

package signatureAddition;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import signatureAddition.pastHardwork.printLogsThroughPID;

public class TagDifferenceWithApk {

	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub

		String FilePath="/home/nikhil/Documents/apps/packageNames_2.txt";
		File file=new File(FilePath);
		Scanner scanner=new Scanner(file);
		int count=0;
		//ExecutePython.downloadApks(FilePath);
		while(scanner.hasNext())
		{
			String packageName="";
			String pathToDisAssembleCodeDirectory="";
			try
			{
				packageName=scanner.next();
				String pathToOriginalApk="/home/nikhil/Downloads/googleplay-api-master/"+packageName+".apk";



				StartingPoint.disassembleApk(pathToOriginalApk, packageName);

				pathToDisAssembleCodeDirectory="/home/nikhil/Documents/apps/"+packageName;


				/**
				 * Generate the resigned apk (i.e repackaged apk)
				 */
				String pathToResignedApk="/home/nikhil/Documents/apps/ReSignedApks/"+packageName+".apk";

				/**
				 * Creating resigned version
				 */

				resignedApp.signApk(packageName, pathToOriginalApk, pathToResignedApk);

				String logPathForOriginalApp="/home/nikhil/Documents/apps/logcatOutput/original_"+packageName+".txt";
				String logPathForResignedApp="/home/nikhil/Documents/apps/logcatOutput/resigned_"+packageName+".txt";

				//HashSet<String>hashSet=	LogAnalysis_sameApp.sameAppTwoTimesLogAnalysis(pathToOriginalApk);

				String fileContentsOriginal=	LogAnalysis.appLogGeneration(pathToOriginalApk,logPathForOriginalApp);
				printLogsThroughPID.initializationADB();
				//	restartSmartphone.restart();
				String fileContentsRepackaged=	LogAnalysis.appLogGeneration(pathToResignedApk,logPathForResignedApp);

				FileWriter fileWriter=new FileWriter(logPathForOriginalApp);
				fileWriter.write(fileContentsOriginal);
				fileWriter.close();


				fileWriter=new FileWriter(logPathForResignedApp);
				fileWriter.write(fileContentsRepackaged);
				fileWriter.close();


				String orignalLogJSONPath=removeDuplicateLogsStatement.removeduplicateLogs(logPathForOriginalApp);
				String repackagedLogJSONPath=removeDuplicateLogsStatement.removeduplicateLogs(logPathForResignedApp);

				/**
				 * So, we are actually analysing the JSON files of original and repackaged which we created in the past.
				 */

				String originalTags=new String (Files.readAllBytes(Paths.get(orignalLogJSONPath)));
				String repackagedTags=new String (Files.readAllBytes(Paths.get(repackagedLogJSONPath)));

				JSONObject jsonOriginal = new JSONObject(originalTags);  
				JSONObject jsonRepackaged = new JSONObject(repackagedTags);  

				String outputOriginalTagsAppPath="/home/nikhil/Documents/apps/TagsLogcatAPK/"+packageName+"_original.txt";
				String outputRepackagedTagsAppPath="/home/nikhil/Documents/apps/TagsLogcatAPK/"+packageName+"_repackaged.txt";

				filterTagsFromLogcatAndApk(jsonOriginal,outputOriginalTagsAppPath,pathToDisAssembleCodeDirectory);
				filterTagsFromLogcatAndApk(jsonRepackaged,outputRepackagedTagsAppPath,pathToDisAssembleCodeDirectory);

				boolean result=finalComparisonLogAnalysis(outputOriginalTagsAppPath,outputRepackagedTagsAppPath);
				if(result)
				{
					System.out.println("There is difference in the orignal and repackaged logs after we make sure that the tags which are coming in the logs are also the part of the apk");
					/**
					 * Update the table with the Remarks "Really there is a tag difference"
					 */
					updateTagDifference(packageName,1);
				}
				else
				{
					System.out.println("Please proceed for the screen capture analysis");
					updateTagDifference(packageName,0);
				}
			}

			catch (Exception e) {
				// TODO: handle exception

				updateTagDifference(packageName,-1);
				CommandExecute.commandExecution(LogAnalysis.pathToadb+" uninstall "+packageName);


				e.printStackTrace();
			}
			StartingPoint.removeDirectory(pathToDisAssembleCodeDirectory);
			CommandExecute.commandExecution(LogAnalysis.pathToadb+" uninstall "+packageName);

		}
	}

	private static void updateTagDifference(String packageName, int i) throws Exception{
		// TODO Auto-generated method stub
		String checkQuery="Select * from tagDifference where packageName='"+packageName+"';";
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
			String query="Insert into tagDifference values ('"+packageName+"',"+i+");";//+numberOfLogsExexcuted+");";
			System.out.println(query);

			Statement statement=DataBaseConnect.initialization();
			statement.executeUpdate(query);
		}
		else
		{
			String query="Update tagDifference set IsTagDifference ="+i+" where packageName='"+packageName+"';";
			System.out.println(query);

			Statement statement=DataBaseConnect.initialization();
			statement.executeUpdate(query);
		}
	}

	public static boolean finalComparisonLogAnalysis(String outputOriginalTagsAppPath,
			String outputRepackagedTagsAppPath) throws Exception {

		/**
		 * Finds whether we both the files contains the same tags after filtering process from the apk
		 */

		File fileOriginal=new File(outputOriginalTagsAppPath);
		File fileRepackaged=new File(outputRepackagedTagsAppPath);

		HashSet<String> hashSet1=new HashSet<String>();
		HashSet<String> hashSet2=new HashSet<String>();

		Scanner scanner=new Scanner(fileOriginal);
		while (scanner.hasNext()) {
			String string =  scanner.nextLine();
			hashSet1.add(string);
		}

		scanner=new Scanner(fileRepackaged);
		while (scanner.hasNext()) {
			String string =  scanner.nextLine();
			hashSet2.add(string);
		}
		if(hashSet1.containsAll(hashSet2) && hashSet2.containsAll(hashSet1))
			return false;
		return true;


	}

	public static void filterTagsFromLogcatAndApk(JSONObject json, String outputTagsPath, String pathToDisAssembleCodeDirectory) throws Exception{

		/**
		 * This takes original JSON file and we are retrieving the tags as a 'key' from the JSON object.
		 */

		JSONArray keys = json.names ();
		String fileContents="";
		for (int i = 0; i < keys.length (); i++) {

			/**
			 * Now for each of the tags retrieved, we have to execute the grep command and find out whether that tag
			 * is also the part of the apk.
			 */
			String tag = keys.getString (i);
			if(tagFound(tag,pathToDisAssembleCodeDirectory))
			{
				fileContents=fileContents+tag+"\n";
			}

		}
		File file=new File(outputTagsPath);
		file.createNewFile();
		FileWriter fileWriter=new FileWriter(file);
		fileWriter.write(fileContents);
		fileWriter.close();

	}

	public static boolean tagFound(String tag, String destinationFolder) throws Exception{

		/**
		 * This function finds out whether the given "tag" is present inside the apk by 
		 * executing grep command.
		 */

		if(tag==null || tag.length()==0)
			return false;
		String grepCommand="\"\\\""+tag+"\\\"\"";
		System.out.println("Value of the tag is :"+tag);
		Process process=Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", "grep -r -l "+grepCommand+" "+ destinationFolder});///home/nikhil/Documents/apps/com.mbanking.aprb.aprb"});

		process.waitFor();
		BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line=bufferedReader.readLine();
		if(line==null)
		{
			System.out.println("Grep command gave nothing for the tag :"+tag);
			return false;
		}

		System.out.println("Output from grep command:"+line);
		return true;
	}

}
