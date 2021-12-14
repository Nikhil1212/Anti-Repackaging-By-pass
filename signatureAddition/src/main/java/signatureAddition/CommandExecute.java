package signatureAddition;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommandExecute {

	public static Process commandExecution(String string) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		System.out.println("command to be executed is :"+string);
		Process pr = Runtime.getRuntime().exec(string);
		
		pr.waitFor();
		BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getErrorStream()));
	//BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
		String line = "";
		while ((line=buf.readLine())!=null) {
			System.out.println("Inside the while loop for command execute");
			//as in the first line only we can get the package name.That's why immeditate break;
			System.out.println(line);
		}
		buf.close();
		return pr;
	}

	public static Process usingBashProcessBuider(String command) {
		// TODO Auto-generated method stub
		
		ProcessBuilder processBuilder = new ProcessBuilder();
		processBuilder.command("bash", "-c", command);
		Process process = null;
		try {

			process = processBuilder.start();

			System.out.println("usingBashProcessBuider try block");
			int x=process.waitFor();
			System.out.println("Return value is :"+x);
			
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(process.getErrorStream()));

			String line=reader.readLine();
			while(line!=null)
			{
				System.out.println(line);
				line=reader.readLine();
			}
			}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return process;
	}
}
