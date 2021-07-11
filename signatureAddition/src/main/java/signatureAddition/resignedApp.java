package signatureAddition;

import java.io.BufferedReader;
import signatureAddition.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Scanner;
public class resignedApp {

	public static void signApk(String packageName, String apkPath, String destinationPath) throws Exception {
		
		/**
		 * Assuming destinationDirectory String ends in /
		 */
		
		String pathToJks="/home/nikhil/Documents/key/hello.jks ";
		//String passwordPath="/home/nikhil/Documents/key/password";
		String apksigner=StartingPoint.apksigner+" sign --ks "+pathToJks + "--ks-pass pass:123456 --in " + apkPath+" --out "+destinationPath;
		//String apktoolBuildCommand="apktool b /home/nikhil/Documents/apps/"+packageName+" -o /home/nikhil/Documents/apps/modified_"+packageName+".apk";
		//System.out.println(apktoolBuildCommand);
		Process pr=CommandExecute.commandExecution(apksigner);
		//int count=0;
		// Print the errors
	/*	BufferedReader buf1 = new BufferedReader(new InputStreamReader(pr.getErrorStream()));
		String line = "";
		while ((line=buf1.readLine())!=null) {
			//as in the first line only we can get the package name.That's why immeditate break;
			System.out.println(line);
			count++;
		}
		if(count!=0)
		{
			LogAnalysis.updateAntiRepackagingCheckPresence(packageName, 'E', "Unable to sign the apk");
			LogAnalysis.updateDatabaseByPassable(packageName, "NA", "Unable to sign the apk");
			throw new Exception("Unable to sign the apk");
			return ;
		}
			*/
		BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
		String line = "";
		while ((line=buf.readLine())!=null) {
			//as in the first line only we can get the package name.That's why immeditate break;
			System.out.println(line);
		}


	}

}
