package signatureAddition;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DiffDump {

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub

		String pathToApk="/home/nikhil/Documents/apps/dataset/packageNames_2.txt";
		File file=new File(pathToApk);
		Scanner scanner=new Scanner(file);
		String packageName="";
		List<String> list=new ArrayList<String>();
		// /usr/bin/diff destinationPath destinationPath
		while(scanner.hasNext())
		{
			try
			{

				packageName=scanner.next();
				String directoryPath="/home/nikhil/Documents/apps/uiautomator/rootEmulator/"+packageName;
				
				String destinationPath1=directoryPath+"/emulator_1.xml";


				String destinationPath2=directoryPath+"/emulator_2.xml";

				String command= "/usr/bin/diff "+destinationPath1+ " "+destinationPath2;

				Process process= CommandExecute.commandExecution(command);

				BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(process.getInputStream()));

				String line=bufferedReader.readLine();
				if(line!=null)
				{
					//System.out.println("package Name is :"+packageName);
					list.add(packageName);
				}


			}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		System.out.println("Conclusion");
		
		for(int i=0;i<list.size();i++)
			System.out.println(list.get(i));

	}
}
