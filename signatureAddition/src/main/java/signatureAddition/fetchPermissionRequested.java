package signatureAddition;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Iterator;

import Logs.LogAnalysis;
import analysingDumpSys.DumpSysAnalysis;

public class fetchPermissionRequested {
	public static HashSet<String> permissionsHashSet=new HashSet<String>();
	
	public static void grantPermissions(String packageName, String pathToApk) throws Exception{
		
		permissionsHashSet.add("CAMERA");
		permissionsHashSet.add("READ_CONTACTS");
		permissionsHashSet.add("WRITE_CONTACTS");
		permissionsHashSet.add("ACCESS_FINE_LOCATION");
		permissionsHashSet.add("ACCESS_COARSE_LOCATION");
		permissionsHashSet.add("RECORD_AUDIO");
		permissionsHashSet.add("READ_PHONE_STATE");
		permissionsHashSet.add("READ_PHONE_NUMBERS");
		permissionsHashSet.add("SEND_SMS");
		permissionsHashSet.add("RECEIVE_SMS");
		permissionsHashSet.add("READ_SMS");
		permissionsHashSet.add("READ_EXTERNAL_STORAGE");
		permissionsHashSet.add("WRITE_EXTERNAL_STORAGE");
		permissionsHashSet.add("CALENDAR");
		permissionsHashSet.add("PACKAGE_USAGE_STATS");

			
/**
 * This function fetches the various permissions requested by the app in the AndroidManifest.xml through aapt and then grant the permission using adb's pm.
 */
		
		
		Process process=CommandExecute.commandExecutionSh(DumpSysAnalysis.pathToaapt+" dump badging "+pathToApk);
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

				//Traverse the hashSet and find whether any string which is the part of permissions.
				Iterator<String> iterator=permissionsHashSet.iterator();
				while(iterator.hasNext())
				{
					String perm=iterator.next();
					if(permission.contains(perm))
					{
						CommandExecute.commandExecutionSh(LogAnalysis.pathToadb+" shell pm grant "+packageName+" "+permission);
						break;
					}
				}
			}
			line=bufferedReader.readLine();
		}

	}

	public static void grantPermissions(String packageName, String pathToApk, String deviceId) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		Process process=CommandExecute.commandExecutionSh(DumpSysAnalysis.pathToaapt+" dump badging "+pathToApk);
		

		permissionsHashSet.add("CAMERA");
		permissionsHashSet.add("READ_CONTACTS");
		permissionsHashSet.add("WRITE_CONTACTS");
		permissionsHashSet.add("ACCESS_FINE_LOCATION");
		permissionsHashSet.add("ACCESS_COARSE_LOCATION");
		permissionsHashSet.add("RECORD_AUDIO");
		permissionsHashSet.add("READ_PHONE_STATE");
		permissionsHashSet.add("READ_PHONE_NUMBERS");
		permissionsHashSet.add("SEND_SMS");
		permissionsHashSet.add("RECEIVE_SMS");
		permissionsHashSet.add("READ_SMS");
		permissionsHashSet.add("READ_EXTERNAL_STORAGE");
		permissionsHashSet.add("WRITE_EXTERNAL_STORAGE");
		permissionsHashSet.add("CALENDAR");
		permissionsHashSet.add("PACKAGE_USAGE_STATS");

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
				
				Iterator<String> iterator=permissionsHashSet.iterator();
				while(iterator.hasNext())
				{
					String perm=iterator.next();
					if(permission.contains(perm))
					{
						CommandExecute.commandExecutionSh(LogAnalysis.pathToadb+" -s "+ deviceId+" shell pm grant "+packageName+" "+permission);
						break;
					}
				}
			//	System.out.println(permission);
			}
			line=bufferedReader.readLine();
		}
		
	}
}
