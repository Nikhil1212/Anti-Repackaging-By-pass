package signatureAddition;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ackExecution {

	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		String ackString="/usr/bin/ack -r -l 'ackExecution'";
		Process process=Runtime.getRuntime().exec(ackString);
		process.waitFor();
		BufferedReader buf1 = new BufferedReader(new InputStreamReader(process.getErrorStream()));
		String line="";
		while((line=buf1.readLine())!=null)
		{
			System.out.println(line);
		}
	}

}
