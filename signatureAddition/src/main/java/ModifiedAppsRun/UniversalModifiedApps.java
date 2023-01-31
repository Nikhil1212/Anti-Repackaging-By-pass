package ModifiedAppsRun;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import signatureAddition.AntiTampering;
import signatureAddition.AppLaunchAndDump;
import signatureAddition.CommandExecute;
import signatureAddition.StartingPoint;
import signatureAddition.PullApps.AppsPull;

public class UniversalModifiedApps {

	public static void main(String[] args) throws FileNotFoundException {

		String filePath="/home/nikhil/Documents/apps/HookingAppsCritical.txt";

		File file=new File(filePath);
		Scanner scanner=new Scanner(file);
		int packageCount=0;
		String modifiedApkPath="";
		String localBaseApkPath="";
		while(scanner.hasNext())
		{
			try
			{
				String packageName=scanner.next();
				//Refer the anti-tampering class
				System.out.println("package Count is :"+(++packageCount));
		//		if(packageCount < 51)
			//		continue;

				String originalApkDirectory=AppsPull.appDirectoryPrefix +packageName+"/";

				String modifiedAppsDirectory=AppsPull.modifiedAppsDirectoryPath+"RootEmulator/OnlyRoot/";
				String baseApkPathHD=originalApkDirectory+"base.apk";
				localBaseApkPath="/home/nikhil/Documents/apps/"+packageName+".apk";

				String	copyCommand="cp "+baseApkPathHD+" "+localBaseApkPath;

				CommandExecute.commandExecutionSh(copyCommand);

				modifiedApkPath=StartingPoint.modifiedApkGenerationRootDetection(packageName,localBaseApkPath);

				//	CommandExecute.commandExecution("rm -r /home/nikhil/Documents/apps/"+packageName);

				//copy the modified apk to the desired directory in the Hard disk

				copyCommand="cp "+modifiedApkPath+" "+modifiedAppsDirectory;
				CommandExecute.commandExecutionSh(copyCommand);
				CommandExecute.commandExecution("rm " + modifiedApkPath);

				CommandExecute.commandExecution("rm " + localBaseApkPath);

				CommandExecute.commandExecution("rm "+modifiedApkPath+".idsig");

			}
			catch (Exception e) {
				try
				{
					CommandExecute.commandExecution("rm " + modifiedApkPath);

					CommandExecute.commandExecution("rm " + localBaseApkPath);

					CommandExecute.commandExecution("rm "+modifiedApkPath+".idsig");
				}
				catch (Exception e1) {
					// TODO: handle exception
					e1.printStackTrace();
				}

				e.printStackTrace();
			}
			//break;
		}

	}
}
