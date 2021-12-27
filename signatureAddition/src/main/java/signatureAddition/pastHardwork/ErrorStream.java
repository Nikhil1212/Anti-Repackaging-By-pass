package signatureAddition.pastHardwork;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import signatureAddition.CommandExecute;

public class ErrorStream {

	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		String command="ls ";
		Process process=Runtime.getRuntime().exec(command);
		BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line=bufferedReader.readLine();
		System.out.println("From main:"+line);
		
		BufferedReader bufferedReader1=new BufferedReader(new InputStreamReader(process.getInputStream()));
		 line=bufferedReader1.readLine();
		System.out.println(line);
		
	}

}
