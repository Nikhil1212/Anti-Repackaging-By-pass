package signatureAddition;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommandExecute {

	static Process commandExecution(String string) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		Process pr = Runtime.getRuntime().exec(string);
		
		pr.waitFor();
		BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getErrorStream()));
	//BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
		String line = "";
		while ((line=buf.readLine())!=null) {
			//as in the first line only we can get the package name.That's why immeditate break;
			System.out.println(line);
		}
		buf.close();
		return pr;
	}
}
