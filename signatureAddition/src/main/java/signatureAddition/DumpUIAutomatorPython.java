package signatureAddition;

import java.io.IOException;

public class DumpUIAutomatorPython {

	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		
		String packageName="justPackage";
		String dumpDestinationPath="/home/nikhil/Documents/pythonUIAutomator/"+packageName+"/dump_1.txt";
	//	main(dumpDestinationPath);
		
	}

	public static void main(String destinationPath) throws IOException, InterruptedException {
		String pathToFile="/home/nikhil/Documents/pythonUIAutomator/helloUIA.py";
		//CommandExecute.commandExecution("mkdir "+directoryPath);
		//String destinationPath=directoryPath+suffix;
		CommandExecute.commandExecution("/usr/bin/python "+pathToFile+" "+destinationPath);
		
	}

	public static void main(String destinationPath, String deviceId) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		String pathToFile="/home/nikhil/Documents/pythonUIAutomator/helloUIADevice.py";
		
		CommandExecute.commandExecution("/usr/bin/python "+pathToFile+" "+destinationPath+" "+deviceId);

	}

}
