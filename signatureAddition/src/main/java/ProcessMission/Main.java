package ProcessMission;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import signatureAddition.CommandExecute;
import signatureAddition.LogAnalysis;

public class Main {

	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		String filePath="/home/nikhil/Documents/process/procFolder_1.txt";
		File file=new File(filePath);
		Scanner scanner=new Scanner(file);
		String fileOutputPath="/home/nikhil/Documents/process/procFolderOutput_1.txt";
		
		String fileContents="";
		while(scanner.hasNext())
		{
			String processId=scanner.next();
			String command=LogAnalysis.pathToadb+" shell cat /proc/"+processId+"/cmdline";
			Process process= CommandExecute.commandExecution(command);
			BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(process.getInputStream()));
			String output=bufferedReader.readLine();
			System.out.println(output);
			if(output!=null)
			fileContents=fileContents+"\n"+output;
			
		}
		FileWriter fileWriter=new FileWriter(fileOutputPath);
		fileWriter.write(fileContents);
		fileWriter.close();
	}

}
