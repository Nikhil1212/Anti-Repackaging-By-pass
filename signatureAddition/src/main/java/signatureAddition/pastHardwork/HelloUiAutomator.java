package signatureAddition.pastHardwork;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Scanner;

import Logs.LogAnalysis;
import analysingDumpSys.DumpSysAnalysis;
import parseXml.Main;
import signatureAddition.CommandExecute;
import signatureAddition.DumpUIAutomatorPython;

public class HelloUiAutomator {

	public static void main(String[] args) throws IOException, InterruptedException, SQLException {
		// TODO Auto-generated method stub
		//Thread.sleep(60000);
		String filePath="/home/nikhil/Documents/apps/dataset/packageNames_2.txt";
		File file=new File(filePath);
		
		Scanner scanner=new Scanner(file);
		while(scanner.hasNext())
		{
			String packageName=scanner.next();
//			packageName="com.suryodaybank.mobilebanking";
			DumpSysAnalysis.launchTheApp(packageName);
			String tapCommand=LogAnalysis.pathToadb+" shell input tap 0 0";
			
			CommandExecute.commandExecution(tapCommand);
			String dumpCommand=LogAnalysis.pathToadb+" shell uiautomator dump";
			String localXmlPath1="/home/nikhil/Documents/experiments/dump1.xml";
			
			
			//DumpUIAutomatorPython.main(localXmlPath1);
			CommandExecute.commandExecution(dumpCommand);
			String pullFile=LogAnalysis.pathToadb+" pull /sdcard/window_dump.xml " + localXmlPath1;
			
			CommandExecute.commandExecution(pullFile);
			String localXmlPath2="/home/nikhil/Documents/experiments/dump2.xml";
			//Curiousity is are the two dumps generated different
			
			
			CommandExecute.commandExecution(tapCommand);
			
		//	DumpUIAutomatorPython.main(localXmlPath2);
			CommandExecute.commandExecution(dumpCommand);

			pullFile=LogAnalysis.pathToadb+" pull /sdcard/window_dump.xml " + localXmlPath2;
			
			CommandExecute.commandExecution(pullFile);
			HashSet<String> hashSet= Main.analysePatternInXml(packageName, localXmlPath1, "resource-id");
			
			HashSet<String> hashSet2= Main.analysePatternInXml(packageName, localXmlPath2, "resource-id");
			
			if(hashSet.equals(hashSet2))
				System.out.println("Yes, there is no difference in hashset.");
			else
				System.out.println("We need to improve our methodology");
			
			System.out.println(hashSet+"\n****\n");
			
			System.out.println(hashSet2+"\n****\n");
			
			
			executeDiff(localXmlPath1,localXmlPath2,packageName);
			

		//break;
		
		}
			}

	public static void executeDiff(String localXmlPath1, String localXmlPath2, String packageName) throws IOException, InterruptedException, SQLException {
		// TODO Auto-generated method stub
		String diffCommand="/usr/bin/diff "+localXmlPath1+" "+localXmlPath2;
		Process process= CommandExecute.commandExecution(diffCommand);
		
		BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line=bufferedReader.readLine();
		int count=0;
		if(line==null)
		{
			//No difference found. //update the table
			Main.updateTable(packageName, 'Y', "No difference when the dump taken two times","PythonWrapperUIAutomator");
		}
		else
			Main.updateTable(packageName, 'N', "Difference when the dump taken two times","PythonWrapperUIAutomator");
		
	}

}
