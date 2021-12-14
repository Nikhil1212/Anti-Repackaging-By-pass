package signatureAddition.pastHardwork;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import Logs.LogAnalysis;
import signatureAddition.CommandExecute;
import signatureAddition.RestartADB;

public class NativeErrorApkInstall {

	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		
		ProcessBuilder processBuilder = new ProcessBuilder();

		// -- Linux --

		// Run a shell command
		
		String command="/home/nikhil/Downloads/platform-tools_r30.0.5-linux/platform-tools/adb install -g /home/nikhil/Documents/apps/modified_com.google.android.apps.nbu.paisa.user.apk";
		
		
		System.out.println("Can u see me");
		
		Process process	=CommandExecute.commandExecution(command);
		BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line=bufferedReader.readLine();
		System.out.println(line);
		if(line==null)
		{
			System.out.println("We have to do something");
			System.exit(x);
		}
		if(line.contains("failed"))
		{
			System.out.println("Unable to install the app");
			System.exit(0);
		}
			
	}

}
