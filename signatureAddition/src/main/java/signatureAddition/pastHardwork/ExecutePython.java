package signatureAddition.pastHardwork;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import signatureAddition.CommandExecute;

public class ExecutePython {
public static String PythonPath="/usr/bin/python3";
	public static void downloadApk(String packageName) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		
		String pathToDownloadApk="/home/nikhil/Downloads/googleplay-api-master/downloadApkFromPackageName.py";
	Process process=CommandExecute.commandExecution(PythonPath+" "+pathToDownloadApk+" "+packageName);
	BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(process.getInputStream()));
	String line=bufferedReader.readLine();
	while((line=bufferedReader.readLine())!=null)
	{	
		System.out.println(line);
	}
	
	}
	public static void downloadApks() throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		String PythonPath="/usr/bin/python3";
		String pathToDownloadApk="/home/nikhil/Downloads/googleplay-api-master/downloadApk.py";
	Process process=CommandExecute.commandExecution(PythonPath+" "+pathToDownloadApk);
	BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(process.getInputStream()));
	String line=bufferedReader.readLine();
	while((line=bufferedReader.readLine())!=null)
	{	
		System.out.println(line);
	}
	
	}
	public static void downloadApks(String fileName) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		String PythonPath="/usr/bin/python3";
		String pathToDownloadApk="/home/nikhil/Downloads/googleplay-api-master/downloadApkFileName.py";
		String command=PythonPath+" "+pathToDownloadApk+" "+fileName;
		System.out.println(command);
	Process process=CommandExecute.commandExecution(command);
	BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(process.getInputStream()));
	String line="";
	while((line=bufferedReader.readLine())!=null)
	{	
		System.out.println(line);
		System.out.println("Hello");
	}
	
	}

}
