package ModifiedAppsRun;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.json.JSONObject;

import signatureAddition.AppLaunchAndDump;
import signatureAddition.CommandExecute;
import signatureAddition.StartingPoint;
import signatureAddition.PullApps.AppsPull;

public class DisassembleModifiedApk {

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		String filePath="/home/nikhil/Documents/apps/verifyErrors.txt";
		File file=new File(filePath);
		Scanner scanner=new Scanner(file);
		int packageCount=0;
		while(scanner.hasNext())
		{
			
			try
			{
				String packageName=scanner.next();
				//Refer the anti-tampering class
				
				System.out.println("package Count is :"+(++packageCount));
				if(packageCount==6)
					break;
			//	if(packageCount<960)
				//	continue;
				
				String originalApkDirectory=AppsPull.appDirectoryPrefix+packageName+"/";

				String dumpPathDirectory="/home/nikhil/Documents/apps/uiautomator/rootEmulator/"+packageName;

			//	Process process=CommandExecute.commandExecutionSh(AntiTampering.pathToLS+" "+originalApkDirectory);
				
				String logPathModifed="/home/nikhil/Documents/apps/logs/"+packageName+"/"+AppLaunchAndDump.deviceIdSynonym[6]+".txt";
				
			//	int count=isSplitApk(packageName,originalApkDirectory);
				
				String modifiedAppsDirectory=AppsPull.modifiedAppsDirectoryPath+"RootEmulator/";
				String localBaseModifiedApkPath="/home/nikhil/Documents/apps/"+packageName+".apk";
				
			//	String modifiedApkLocalPath=
				String modifiedApkPathHD=modifiedAppsDirectory+"modified_"+packageName+".apk";

				
				
				String	copyCommand="cp "+modifiedApkPathHD+" "+localBaseModifiedApkPath;
				
				CommandExecute.commandExecutionSh(copyCommand);
				StartingPoint.disassembleApk(localBaseModifiedApkPath, packageName);
				
			}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		

}
}
