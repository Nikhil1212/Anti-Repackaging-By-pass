package signatureAddition.pastHardwork;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import Logs.LogAnalysis;
import signatureAddition.CommandExecute;

public class DeviceStatus {
	public static void main(String[] args) throws IOException, InterruptedException {
	if(isDeviceAvailable())
		System.out.println("Good to go");
	else
		System.out.println("Device not available");
	}

	public static boolean isDeviceAvailable() throws IOException, InterruptedException {
		String str=LogAnalysis.pathToadb+ " devices";
		// TODO Auto-generated method stub
		Process process=CommandExecute.commandExecution(str);
		BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line=bufferedReader.readLine();
		line=bufferedReader.readLine();
		if(line==null || line.length()==0)
			return false;
		while(line!=null)
		{
			if(line.contains("offline") || line.contains("authoriz"))
			{
				System.out.println("This device is not available");
				return false;
			}
			else
			{
				System.out.println(line);
			} 
			line=bufferedReader.readLine();
		}
		return true;
		
		
	}

}
