package signatureAddition;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class PIDofApp {

	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		System.out.println("let's try to fetch the pid of an app from its packagename");
		String devices="adb devices";
		CommandExecute.commandExecution(devices);
		String packageName="com.mbanking.aprb.aprb";
		String command="adb shell pidof "+packageName;
		String directoryLocationForStoringLogs="D:\\Apps\\UPI\\";
		Process process=CommandExecute.commandExecution(command);
		
		BufferedReader buf = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line = "";
		line=buf.readLine();
		if(line==null)
		{
			System.out.println("The app is currently not running");
		}
		else
		{
			//as in the first line only we can get the package name.That's why immeditate break;
			
			System.out.println("pid of the app with package name "+packageName+" is "+line);
			String analysingLogsUsingPID="adb logcat --pid "+line+" -d ";//> "+directoryLocationForStoringLogs+"logs_"+packageName+"_PID.txt";
			System.out.println(analysingLogsUsingPID);
			String clearLogcat="adb shell logcat -c";
			String filePath=directoryLocationForStoringLogs+"logs_"+packageName+"_PID.txt";
			File file=new File(filePath);
			file.createNewFile();
		
			//CommandExecute.commandExecution(clearLogcat);
			/*Process process2=CommandExecute.commandExecution(analysingLogsUsingPID);

			BufferedReader buf1 = new BufferedReader(new InputStreamReader(process2.getInputStream()));
			//BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
				
				
			while ((line=buf1.readLine())!=null) {
					//as in the first line only we can get the package name.That's why immeditate break;
					Files.write(Paths.get(filePath), (line+"\n").getBytes(),  StandardOpenOption.APPEND);
					System.out.println(line);
				}
				buf1.close();
			*/
			String commandToFilterLogsUsingPackageName="adb logcat -d | findstr "+packageName;
			System.out.println(commandToFilterLogsUsingPackageName);
			Process process3=CommandExecute.commandExecution(commandToFilterLogsUsingPackageName);
			
		
				BufferedReader buf2 = new BufferedReader(new InputStreamReader(process3.getInputStream()));
				//BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
					System.out.println("Before while loop");
					while ((line=buf2.readLine())!=null) {
						System.out.println("Inside the while loop");
						//as in the first line only we can get the package name.That's why immeditate break;
						Files.write(Paths.get(filePath), (line+"\n").getBytes(),  StandardOpenOption.APPEND);
						System.out.println(line);
					}
					buf2.close();
			
			System.out.println("Command executed successfully");
		}
		
	
	}


}
