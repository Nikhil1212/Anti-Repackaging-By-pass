package ProcessMission;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import Logs.LogAnalysis;
import signatureAddition.CommandExecute;

public class FetchProcessIdDumpsys {
	
	public static boolean isAppRunning(String packageName) throws IOException, InterruptedException {
		String command=LogAnalysis.pathToadb+" shell dumpsys meminfo "+packageName+" | grep pid";
		Process process= CommandExecute.commandExecution(command);
		BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(process.getInputStream()));
		String output=bufferedReader.readLine();
		if(output==null)
		{
			System.out.println("App is running");
			return false;
		}
			
		return true;
		//	System.out.println("App crashed");
		//System.out.println(output);

	}

}
