package signatureAddition;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class DiffActivity {

	public static void main(String[] args) throws IOException {
		
		String FilePath="/home/nikhil/Documents/apps/packageNames.txt";
		String outputFilePath="/home/nikhil/Documents/apps/activityOutput_3.txt";
		File file=new File(FilePath);
		
		Scanner scanner=new Scanner(file);
		String universalContents="";
		while(scanner.hasNext())
		{
			try
			{
				String packageName=scanner.next();
				packageName="com.mobikwik_new";
				String logPathForOriginalApp="/home/nikhil/Documents/apps/logcatOutput/original_"+packageName+".txt";
				String logPathForResignedApp="/home/nikhil/Documents/apps/logcatOutput/resigned_"+packageName+".txt";
				System.out.println(logPathForResignedApp);
				boolean fileContents=LogAnalysis.differenceActiviyNameLogs(packageName,logPathForOriginalApp,logPathForResignedApp);
				universalContents=universalContents+fileContents+"\n\n";				
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			break;
		
	}
	File file2=new File(outputFilePath);
	file2.createNewFile();
		
		FileWriter fileWriter=new FileWriter(outputFilePath);
		fileWriter.write(universalContents);
		fileWriter.close();
		

}
}
