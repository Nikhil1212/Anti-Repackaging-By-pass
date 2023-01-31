package ModifiedAppsRun;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import finalRun.isDumpGenerated;
import signatureAddition.AppLaunchAndDump;

public class SmaliSyntaxError {

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		String pathToApk="/home/nikhil/Documents/apps/FinalDataset.txt";
		File file=new File(pathToApk);
		Scanner scanner=new Scanner(file);
		String packageName="";

		int count1=0;

		String output="";
		while(scanner.hasNext())
		{
			try
			{
				count1++;
			//	if(count1<1100)
				//	continue;
				packageName=scanner.next();

				String dumpPath="/home/nikhil/Documents/apps/logs/"+packageName+"/hooking.txt";
				if(isDumpGenerated.checkFileExists(dumpPath))
				{
					String fileContents=new String(Files.readAllBytes(Paths.get(dumpPath)));
					//if(fileContents.contains("java.lang.VerifyError"))
						//output=output+packageName+"\n";
				}
				else
				{
				//	System.out.println(count1);
					System.out.println(packageName);
				//	break;
				}
			}

			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		}
		System.out.println(output);
	}

}
