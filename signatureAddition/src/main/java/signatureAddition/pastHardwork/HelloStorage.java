package signatureAddition.pastHardwork;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import Logs.LogAnalysis;
import signatureAddition.CommandExecute;

public class HelloStorage {

	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub

		String command="/home/nikhil/Documents/M\\ Tech\\ Res/";
		Process process=Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", "ls "+command});///home/nikhil/Documents/apps/com.mbanking.aprb.aprb"});
		
		process.waitFor();
		
//		Process process=CommandExecute.commandExecution("ls "+command);
		
		
		
		BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line=bufferedReader.readLine();
		while(line!=null)
		{
			System.out.println(line);
			line=bufferedReader.readLine();
		}
		
	}

}
