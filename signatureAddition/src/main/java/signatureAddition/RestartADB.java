package signatureAddition;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RestartADB {
	public static void main() throws IOException, InterruptedException
	{
		CommandExecute.commandExecution(LogAnalysis.pathToadb+" kill-server");
	Process process=CommandExecute.commandExecution(LogAnalysis.pathToadb+" devices");
		BufferedReader  bufferedReader=new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line=bufferedReader.readLine();
	//	System.out.println(line);
		/*while(line!=null)
		{
			System.out.println(line);
			line=bufferedReader.readLine();
		}*/
	}

}
