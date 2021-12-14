package signatureAddition;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class apkGeneration {

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		String pathToApk="/home/nikhil/Documents/apps/RootCheckNotPresent.txt";
		File file=new File(pathToApk);
		Scanner scanner=new Scanner(file);
		String packageName="";
		List<String> list=new ArrayList<String>();
		//String packageNamePath="/home/nikhil/Documents/apps/modified_";

		while(scanner.hasNext())
		{
			try
			{
				packageName=scanner.next();

				String packageNamePath="/home/nikhil/Documents/apps/modified_"+packageName+".apk";
				Process process=CommandExecute.commandExecution(AppLaunchAndDump.pathToLS+" "+packageNamePath);
				char success='Y';
				BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(process.getInputStream()));
				String line=bufferedReader.readLine();
				if(line==null)
				{
					list.add(packageName);
				}
			}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		
		}
		for(int i=0;i<list.size();i++)
			System.out.println(list.get(i));

	}

}
