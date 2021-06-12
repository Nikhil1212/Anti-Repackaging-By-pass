package signatureAddition.pastHardwork;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import signatureAddition.CommandExecute;

public class executingNode {

	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		
		String node="/opt/node-v14.15.0-linux-x64/bin/node ";
		String pathToJS="/home/nikhil/Documents/googlePlayScraper/app.js";
				String nodeJS=node+pathToJS;
		String filePath = "/home/nikhil/Documents/uniqueAppId.txt";
		File file=new File(filePath);
		Scanner scanner=new Scanner(file);
		while(scanner.hasNext())
		{
			
			String packageName=scanner.next();
			
			String fileContents="var gplay = require('google-play-scraper');\n gplay.app({appId: \""+packageName+"\"}).then(console.log);";
			
			writeToFile(pathToJS,fileContents);
			
			Process process=CommandExecute.commandExecution(nodeJS);	
			
			fetchInstallationCount(packageName, process);
			
		}
		
	}

	private static void writeToFile(String filePath, String fileContents) throws IOException {
		// TODO Auto-generated method stub
		FileWriter myWriter = new FileWriter(filePath);

		myWriter.write(fileContents);
		myWriter.close();
	}

	private static void fetchInstallationCount(String packageName, Process process) throws IOException {
		// TODO Auto-generated method stub
		BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(process.getInputStream()));
		String pattern="  minInstalls: ";
		String line="";
		while((line=bufferedReader.readLine())!=null)
		{
			if(line.contains(pattern))
			{
				System.out.println(line);
				break;
			}
		}
		if(line!=null)
		System.out.println("PackageName :"+packageName+" "+line.substring(pattern.length(),line.length()-1));
		
	}

}
