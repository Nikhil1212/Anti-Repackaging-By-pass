package signatureAddition.pastHardwork;

import java.io.IOException;

import signatureAddition.CommandExecute;
import signatureAddition.LogAnalysis;

public class restartSmartphone {
public static String pathToAdb="/home/nikhil/Downloads/platform-tools_r30.0.5-linux/platform-tools/adb";
	public static void restart () throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		String password="1995";
		String commandRestart=pathToAdb+" reboot";
		CommandExecute.commandExecution(commandRestart);
		String passwordGive=pathToAdb+" shell input text "+password;
		Thread.sleep(40000);
		CommandExecute.commandExecution(passwordGive);
		Thread.sleep(10000);
	}

}
