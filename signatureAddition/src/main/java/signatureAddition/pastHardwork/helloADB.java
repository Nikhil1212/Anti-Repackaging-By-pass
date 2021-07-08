package signatureAddition.pastHardwork;

import java.io.IOException;

import signatureAddition.CommandExecute;

public class helloADB {

	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		CommandExecute.commandExecution("adb kill-server");
	}

}
