package InstallerVerification;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import Logs.LogAnalysis;
import analysingDumpSys.DumpSysAnalysis;
import signatureAddition.CommandExecute;

public class fetchPermissionRequested {
	public static void grantPermissions(String packageName, String pathToApk ) throws Exception{
		
		/**
		 * This function fetches the various permissions requested by the app in the AndroidManifest.xml through aapt and then grant the permission using adb's pm.
		 */
		Process process=CommandExecute.commandExecution(DumpSysAnalysis.pathToaapt+" dump badging "+pathToApk);
		BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line=bufferedReader.readLine();
		String pattern="uses-permission: name='";
		
		System.out.println("Granting the permission");
		//String packageName="in.org.npci.upiapp";
		while(line!=null)
		{
			//System.out.println(line);
			if(line.contains(pattern))
			{
				String permission=line.substring(pattern.length(),line.length()-1);
			//	System.out.println(permission);
				CommandExecute.commandExecution(LogAnalysis.pathToadb+" shell pm grant "+packageName+" "+permission);
			}
			line=bufferedReader.readLine();
		}

	}
}
