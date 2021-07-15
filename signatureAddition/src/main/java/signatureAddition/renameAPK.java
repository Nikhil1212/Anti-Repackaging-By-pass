package signatureAddition;

import java.io.File;
import java.util.Scanner;

public class renameAPK {

	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		String FilePath="/home/nikhil/Downloads/ValidApk.txt";
		File file=new File(FilePath);
		Scanner scanner=new Scanner(file);
		int count=0;
		//ExecutePython.downloadApks(FilePath);
		while(scanner.hasNext())
		{
			try
			{
				String pathToApk=scanner.next();
				String packageName=StartingPoint.getPackageName(pathToApk);
				String destinationPath="/home/nikhil/Downloads/googleplay-api-master/"+packageName+".apk";
				modifyApkName(pathToApk,packageName,destinationPath);
				
			}
			catch (Exception e) {

				e.printStackTrace();
			}
			
	}
	}

	public static void modifyApkName(String pathToApk, String packageName, String destinationPath) throws Exception {
		// TODO Auto-generated method stub
		CommandExecute.commandExecution("mv "+pathToApk+" "+destinationPath);
	}

}
