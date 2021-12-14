package signatureAddition.pastHardwork;

import java.io.IOException;

import signatureAddition.CommandExecute;

public class copyCommand {

	public static void main(String[] args) throws IOException, InterruptedException {
		String sourcePath="/home/nikhil/Documents/apps/generatedModifedApks.txt";
		String destinationPath="/home/nikhil/Documents/apps/com.icicibank.fastag/";
		// TODO Auto-generated method stub
		String command="cp "+sourcePath+" "+destinationPath;
		CommandExecute.commandExecution(command);
		
	}

}
