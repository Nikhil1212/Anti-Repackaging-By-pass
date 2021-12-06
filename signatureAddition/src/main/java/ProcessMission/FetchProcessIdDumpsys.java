package ProcessMission;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import Logs.LogAnalysis;
import signatureAddition.CommandExecute;

public class FetchProcessIdDumpsys {
	
	public static void main(String args[]) throws IOException, InterruptedException {
		String command=LogAnalysis.pathToadb+" -s 14011JEC202909 shell dumpsys meminfo com.cradleewrwise.nini.app | grep pid";
		Process process= CommandExecute.commandExecution(command);
		BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(process.getInputStream()));
		String output=bufferedReader.readLine();
		if(output==null)
			System.out.println("App crashed");
		System.out.println(output);

	}

}
