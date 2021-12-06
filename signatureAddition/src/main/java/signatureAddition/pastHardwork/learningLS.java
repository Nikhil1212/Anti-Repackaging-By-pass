package signatureAddition.pastHardwork;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class learningLS {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String directoryPath="/home/nikhil/Documents/apps/dataset/com.icicibank.uotm/";
		executeLS(directoryPath);
		
	}

	public static void executeLS(String directoryPath) throws Exception{

		Process process=Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", "ls "+directoryPath+"*"});
		process.waitFor();
		
		BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line=bufferedReader.readLine();
		while(line!=null)
		{
			System.out.println(line);
			line=bufferedReader.readLine();
		}
		
	}

}
