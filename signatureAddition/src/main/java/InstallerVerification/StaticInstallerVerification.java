package InstallerVerification;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import finalRun.isDumpGenerated;
import installerVerficationByPass.Main;
import signatureAddition.CommandExecute;
import signatureAddition.StartingPoint;
import signatureAddition.StartingPoint_IntsallerVerification;
import signatureAddition.PullApps.AppsPull;

public class StaticInstallerVerification {

	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
	
		String filePath="/home/nikhil/Documents/apps/dataset/output.txt";
		
		File file=new File(filePath);
		Scanner scanner=new Scanner(file);
		int packageCount=0;
		String pathToDisAssembleCode="";
		while(scanner.hasNext())
		{
			try
			{
				String packageName=scanner.next();
				packageName="com.hdfc.toloans";
				String fileNamePath=AppsPull.appDirectoryPrefix+packageName+"/base.apk";

				pathToDisAssembleCode="/home/nikhil/Documents/apps/"+packageName;

				System.out.println(packageName);

				StartingPoint.disassembleApk(fileNamePath,packageName);
				boolean result=codeInjectionByPassIntallerVerification(pathToDisAssembleCode);
				if(result)
				isDumpGenerated.updateTable(packageName, "InstallerVerfication");
				break;
			}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			CommandExecute.commandExecution("rm -r "+pathToDisAssembleCode);
		}
	}
	public static boolean codeInjectionByPassIntallerVerification(String pathToDisAssembleCode) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		
		List<String> patternList=new ArrayList<String>();
		
		StartingPoint_IntsallerVerification.patternListInitialize(patternList);
		for(int i=0;i<patternList.size();i++)
		{
			Process process=Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", "grep -r -l '"+patternList.get(i)+"' "+ pathToDisAssembleCode});///home/nikhil/Documents/apps/com.mbanking.aprb.aprb"});

			process.waitFor();

			HashSet<String> hashSet=StartingPoint_IntsallerVerification.fetchValidFiles(process);

			Iterator<String> iterator = hashSet.iterator();
			while (iterator.hasNext())
			{
				String filePath=iterator.next();
				return true;
			}
		}
		return false;
		
		


	}

}
