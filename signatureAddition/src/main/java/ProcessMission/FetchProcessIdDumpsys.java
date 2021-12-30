package ProcessMission;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import Logs.LogAnalysis;
import signatureAddition.CommandExecute;

public class FetchProcessIdDumpsys {
	
	public static void main(String[] args) throws IOException, InterruptedException {
		isAppRunning("com.google.android.youtube");
	}
	
	public static boolean isAppRunning(String packageName) throws IOException, InterruptedException {
		
		String command=LogAnalysis.pathToadb+" shell dumpsys meminfo "+packageName+" | grep pid";
		Process process= CommandExecute.commandExecution(command);
		BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(process.getInputStream()));
		String output=bufferedReader.readLine();
		if(output==null)
		{
			
			System.out.println("App is not running");
			return false;
		}
		String pid=parsePid(output);
		System.out.println(pid);
		return true;
		//	System.out.println("App crashed");
		//System.out.println(output);

	}

	public static String retrievePID(String packageName) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		String command=LogAnalysis.pathToadb+" shell dumpsys meminfo "+packageName+" | grep pid";
		Process process= CommandExecute.commandExecution(command);
		BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(process.getInputStream()));
		String output=bufferedReader.readLine();
		if(output==null)
		{
			return output;
		}
		
		String pid=parsePid(output);
		
		return pid;
	}

	public static String parsePid(String output) {

		int i;
		String pidPattern="** MEMINFO in pid ";
		 int startIndex = output.indexOf(pidPattern)+pidPattern.length();
		 for( i=startIndex;i<output.length();i++)
			 if(output.charAt(i)==' ')
				 break;
		return (output.substring(startIndex,i));
	
	}

}
