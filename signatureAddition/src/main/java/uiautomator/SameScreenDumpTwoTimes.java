package uiautomator;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

import Logs.LogAnalysis;
import analysingDumpSys.DumpSysAnalysis;
import signatureAddition.CommandExecute;
import signatureAddition.pastHardwork.HelloUiAutomator;

public class SameScreenDumpTwoTimes {

	public static void main(String[] args) throws IOException, InterruptedException, SQLException {
		// TODO Auto-generated method stub
		String filePath="/home/nikhil/Documents/apps/dataset/packageNames_2.txt";
		File file=new File(filePath);
		
		Scanner scanner=new Scanner(file);
		while(scanner.hasNext())
		{
			String packageName=scanner.next();
//			packageName="com.suryodaybank.mobilebanking";
			String directoryPath="/home/nikhil/Documents/apps/uiautomator/rootEmulator/"+packageName;
			String localXmlPath1=directoryPath+"/real_python.xml";
			
			
			//DumpUIAutomatorPython.main(localXmlPath1);
			String localXmlPath2=directoryPath+"/real_python2.xml";
			
			HelloUiAutomator.executeDiff(localXmlPath1,localXmlPath2,packageName);
			
		}
		
	}

}
