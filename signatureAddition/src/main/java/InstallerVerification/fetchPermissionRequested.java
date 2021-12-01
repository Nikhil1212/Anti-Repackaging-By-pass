package InstallerVerification;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import analysingDumpSys.DumpSysAnalysis;
import signatureAddition.CommandExecute;
import signatureAddition.LogAnalysis;

public class fetchPermissionRequested {
	public static void grantPermissions(String packageName, String pathToApk ) throws Exception{
		Process process=CommandExecute.commandExecution(DumpSysAnalysis.pathToaapt+" dump badging "+pathToApk);
		BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line=bufferedReader.readLine();
		String pattern="uses-permission: name='";
		
		System.out.println("Granting the permission");
		//String packageName="in.org.npci.upiapp";
		while(line!=null)
		{
			System.out.println(line);
			if(line.contains(pattern))
			{
				String permission=line.substring(pattern.length(),line.length()-1);
				System.out.println(permission);
				CommandExecute.commandExecution(LogAnalysis.pathToadb+" shell pm grant "+packageName+" "+permission);
			}
			line=bufferedReader.readLine();
		}

	}
}
