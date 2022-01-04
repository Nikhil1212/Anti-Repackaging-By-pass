package parseXml;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;

import Logs.LogAnalysis;
import signatureAddition.AppLaunchAndDump;

/**
 * This class with the help of other method defined in another class, compares the dump of two files executed in two different environments. It also analyses the logs. Currently, it is designed for all the three different environments; anti-tampering, anti-emulation, root-detection. 
 * @author nikhil
 *
 */
public class RunAnalysis {
	
	public static String tableName[]= {"EmulatorDetection_Automation","RootDetection_Automation","AntiTampering_Automation"};

	public static void main(String[] args) throws IOException {

		String filePath="/home/nikhil/Documents/apps/LogsIssue.txt";
		File file=new File(filePath);
		Scanner scanner=new Scanner(file);

		int count=0;
		while(scanner.hasNext())
		{
			count++;
			try
			{
				int flag=0;
				String packageName=scanner.next();
				System.out.println("Package count:"+count);
				
				String dumpdirectoryPath="/home/nikhil/Documents/apps/uiautomator/rootEmulator/"+packageName;
				String logDirectoryPath="/home/nikhil/Documents/apps/logs/"+packageName;

				for(int i=1;i<=3;i++)
				{
				
					boolean result = isCheckPresentDump(packageName,dumpdirectoryPath,AppLaunchAndDump.deviceIdSynonym[i],tableName[i-1]);
					
					if (result)
						continue;
					
					String dumpPathReal=dumpdirectoryPath+"/real_BuiltIn.xml";

					String dumpPathDiffEnvironment=dumpdirectoryPath+"/"+AppLaunchAndDump.deviceIdSynonym[i]+"_BuiltIn.xml";

					
					result=isDifferentLogs(packageName, dumpPathReal, dumpPathDiffEnvironment,logDirectoryPath+"/original.txt" , logDirectoryPath+ "/"+AppLaunchAndDump.deviceIdSynonym[i]+".txt",tableName[i-1]) ;
					
					if(!result)
						Main.updateTable(packageName, 'N', "No difference from log and uiautomator dump", tableName[i-1]);
					
				}
				
			}
			catch (Exception e) {
				// TODO: handle exception
				
				e.printStackTrace();
			}

		}
	}
	public static boolean isDifferentLogs(String packageName, String filePathReal, String filePathRunTime,
			String logPathOriginal, String logPathRepackaged, String tableName) throws SQLException, IOException {
		
		String fileContents=new String(Files.readAllBytes(Paths.get(filePathReal)));
		if(fileContents.contains(packageName))
		{
			fileContents=new String(Files.readAllBytes(Paths.get(filePathRunTime)));
			if(!fileContents.contains(packageName))
			{
				Main.updateTable(packageName, 'Y', "App crashed Log Analysis", tableName);
				return true;
			}
				
		}
		
		
		if 	(LogAnalysis.differenceActiviyNameLogs(packageName, logPathOriginal, logPathRepackaged))
		{
			Main.updateTable(packageName, 'Y', "Difference Activity Observed Log Analysis", tableName);
			return true;
		}
			
		
		if (LogAnalysis.checkDifferenceToastLogs(packageName, logPathOriginal, logPathRepackaged))
		{
			Main.updateTable(packageName, 'Y', "Difference Toast Message observed Log Analysis", tableName);
			return true;
		}
			
		return false;
	}

	private static boolean isCheckPresentDump(String packageName, String dumpDirectoryPath, String environment, String tableName) throws SQLException, IOException {
		// TODO Auto-generated method stub

		boolean result=	isDifferentResourceId(packageName,dumpDirectoryPath,"resource-id",environment);

		if(result)
		{
			Main.updateTable(packageName, 'Y', "resource-id", tableName);
			return true;
		}

	/*	result=	isDifferentClass(packageName,dumpDirectoryPath,"class",environment);
		if(result)
		{
			Main.updateTable(packageName, 'Y', "class", tableName);
			return true;
		}		

		result=	isDifferentText(packageName,dumpDirectoryPath,"text",environment);

		if(result)
		{
			Main.updateTable(packageName, 'Y', "text", tableName);
			return true;		
		}
		result=	isDifferentContentDesc(packageName,dumpDirectoryPath,"content-desc",environment);

		if(result)
		{
			Main.updateTable(packageName, 'Y', "content-desc", tableName);
			return true;
		}*/

		return false;
	}

	private static boolean isDifferentContentDesc(String packageName, String directoryPath, String pattern,
			String runVersion) throws IOException {
		// TODO Auto-generated method stub

		String filePathReal=directoryPath+"/real_BuiltIn.xml";

		String filePathDiffEnvironment=directoryPath+"/"+runVersion+"_BuiltIn.xml";

		HashSet<String> contentDescReal=TextAnalysis.nonEmptyContentDesc(packageName, filePathReal ,pattern);

		//	pattern="content-desc";
		HashSet<String> contentDescDiffEnvironment=TextAnalysis.nonEmptyContentDesc(packageName, filePathDiffEnvironment,pattern);

		if(contentDescReal.size()!=contentDescDiffEnvironment.size())
		{
			return true;
		}
		return false;
	}

	private static boolean isDifferentText(String packageName, String directoryPath, String pattern, String runVersion) {
		String filePathReal=directoryPath+"/real_BuiltIn.xml";

		String filePathDiffEnvironment=directoryPath+"/"+runVersion+"_BuiltIn.xml";

		HashSet<String> textReal=TextAnalysis.nonEmptyText(packageName, filePathReal ,pattern);

		//	pattern="content-desc";
		HashSet<String> textDiffEnvironment=TextAnalysis.nonEmptyText(packageName, filePathDiffEnvironment,pattern);

		
		if(textReal.size()!=textDiffEnvironment.size())
		{
			return true;
		}

		Iterator<String> iterator=textDiffEnvironment.iterator();
		
		while(iterator.hasNext())
		{
			String item=iterator.next().toLowerCase();
			if(item.contains("root") || item.contains("not trusted") || item.contains("jailbroken") || item.contains("jail broken") || item.contains("insecure") || item.contains("not secure") )
				return true;
		}
		//let's iterate and analyse the apps for the popular error messages.

		return false;
	}

	private static boolean isDifferentClass(String packageName, String directoryPath, String pattern, String runVersion) throws IOException {
		// TODO Auto-generated method stub
		String filePathReal=directoryPath+"/real_BuiltIn.xml";

		String filePathDiffEnvironment=directoryPath+"/"+runVersion+"_BuiltIn.xml";

		HashSet<String> hashSet1= Main.analysePatternInXml(packageName, filePathReal, pattern);
		HashSet<String> hashSet;

		hashSet= Main.analysePatternInXml(packageName, filePathDiffEnvironment, pattern);
		System.out.println(hashSet1);

		System.out.println(hashSet);
		if(!hashSet.equals(hashSet1))
		{
			return true;
		}
		return false;
	}

	private static boolean isDifferentResourceId(String packageName, String directoryPath, String pattern,
			String runVersion) throws IOException {
		// TODO Auto-generated method stub

		String filePathReal=directoryPath+"/real_BuiltIn.xml";

		String filePathDiffEnvironment=directoryPath+"/"+runVersion+"_BuiltIn.xml";

		HashSet<String> hashSet1= Main.analysePatternInXml(packageName, filePathReal, pattern);
		HashSet<String> hashSet;

		hashSet= Main.analysePatternInXml(packageName, filePathDiffEnvironment, pattern);
		System.out.println(hashSet1);

		System.out.println(hashSet);
		if(!hashSet.equals(hashSet1))
		{
			return true;
		}
		return false;

	}
}
