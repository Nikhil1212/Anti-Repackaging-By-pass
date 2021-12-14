package linuxCommandsExecution;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import signatureAddition.CommandExecute;

public class ProcessBuilderLearn {

	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		ProcessBuilder processBuilder = new ProcessBuilder();

		// -- Linux --

		// Run a shell command
		
		String pathToApk="/home/nikhil/Documents/apps/modified_com.google.android.apps.nbu.paisa.user.apk";

		String command="/home/nikhil/Downloads/platform-tools_r30.0.5-linux/platform-tools/adb install -g /home/nikhil/Documents/apps/modified_com.google.android.apps.nbu.paisa.user.apk";
		
		String commandInstall="adb install -g "+pathToApk;

	//	myMethod(commandInstall);
		//System.exit(0);
		
	
		processBuilder.command("bash", "-c", command);
	//	Process process=CommandExecute.usingBashProcessBuider(command);
		
		try {

Process		process = processBuilder.start();

			StringBuilder output = new StringBuilder();

			process.waitFor();
			System.out.println("Can u see me");
			
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(process.getErrorStream()));

			System.out.println("Output :"+reader.readLine());
			reader = new BufferedReader(
					new InputStreamReader(process.getInputStream()));

			String line;
			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");
			}

			int exitVal = process.waitFor();
			if (exitVal == 0) {
				System.out.println("Success!");
				System.out.println(output);
				System.exit(0);
			} else {
				//abnormal...
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static void myMethod(String command) throws IOException {
		
		Process process=CommandExecute.usingBashProcessBuider(command);
		BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(process.getErrorStream()));
		String line=bufferedReader.readLine();
		while(line!=null)
		{
			System.out.println(line);
			line=bufferedReader.readLine();
		}
	}

}
