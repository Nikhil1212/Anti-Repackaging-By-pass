package rsaPresence;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import Logs.LogAnalysis;
import signatureAddition.CommandExecute;
import signatureAddition.StartingPoint;

public class Main {

	public static void main(String[] args) throws IOException, InterruptedException {
		String pathToApk="/home/nikhil/Documents/apps/dataset/com.phonepe.app/base.apk";
		// TODO Auto-generated method stub
		String command=StartingPoint.apksigner+" verify --verbose "+pathToApk;
		Process process= CommandExecute.commandExecution(command);
		BufferedReader reader=new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line=reader.readLine();
		boolean result=false;
		while(line!=null)
		{
			System.out.println(line);
			if(line.contains("v1"))
			{
				if(line.contains("true"))
				{
					result=true;
					System.out.println("v1 signing is present");
					break;
				}
			}
			line=reader.readLine();
			
		}
		if(result)
			System.out.println("RSA file is present");
		
		else
			System.out.println("RSA file is not present");
		
	}

}
