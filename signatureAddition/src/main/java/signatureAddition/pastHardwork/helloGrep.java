package signatureAddition.pastHardwork;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class helloGrep {

	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		String filePath="/home/nikhil/Documents/apps/logOutput/modifed_com.mbanking.aprb.aprb.txt";
		File file=new File(filePath);
		Scanner scanner=new Scanner(file);
		int count=0;
		while(scanner.hasNext())
		{
			String line=scanner.next();
			if(line.contains("ActivityTaskManager"))
			{
				count++;
			}
		}
		System.out.println(count);
		/*String command="/bin/grep -wc \"ActivityTaskManager\" /home/nikhil/Documents/apps/logOutput/modifed_com.mbanking.aprb.aprb.txt";
		Process process=CommandExecute.commandExecution(command);
		BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line="";
		while((line=bufferedReader.readLine())!=null)
		{
			System.out.println(line);
		}*/
	}

}
