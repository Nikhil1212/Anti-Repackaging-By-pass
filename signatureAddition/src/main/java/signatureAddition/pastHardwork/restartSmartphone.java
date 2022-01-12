package signatureAddition.pastHardwork;

import java.io.IOException;

import Logs.LogAnalysis;
import signatureAddition.CommandExecute;

public class restartSmartphone {
public static String pathToAdb="/home/nikhil/Downloads/platform-tools_r30.0.5-linux/platform-tools/adb";

public static void main(String[] args) throws Exception{
	String deviceId="14011JEC202909";
	restart(deviceId);
}
	public static void restart (String deviceId) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		String password="1995";
		String commandRestart=pathToAdb+" -s "+deviceId +" reboot ";
		CommandExecute.commandExecution(commandRestart);
		String passwordGive=pathToAdb+" -s "+deviceId +" shell input text "+password;
		Thread.sleep(40000);
		
		CommandExecute.commandExecution(LogAnalysis.pathToadb+ " -s "+deviceId +" shell input swipe 200 900 200 300");
		CommandExecute.commandExecution(LogAnalysis.pathToadb+" -s "+deviceId +" shell input text "+password); //Password
		CommandExecute.commandExecution(LogAnalysis.pathToadb+ " -s "+deviceId + " shell input keyevent 66");  //Enter Key

//		CommandExecute.commandExecution(passwordGive);
		Thread.sleep(10000);
	}

}
