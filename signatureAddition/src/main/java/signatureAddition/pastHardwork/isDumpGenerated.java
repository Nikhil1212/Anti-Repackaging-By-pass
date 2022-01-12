package signatureAddition.pastHardwork;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import signatureAddition.PullApps.AppsPull;

public class isDumpGenerated {

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		String filePath="/home/nikhil/Documents/apps/AntiTamperCheckPresent.txt";
		File file=new File(filePath);
		Scanner scanner=new Scanner(file);
		int packageCount=0;
		while(scanner.hasNext())
		{
			try
			{
				String packageName=scanner.next();
				//Refer the anti-tampering class
				
				System.out.println("package Count is :"+(++packageCount));
				
				String dumpPathDirectory="/home/nikhil/Documents/apps/uiautomator/rootEmulator/"+packageName;

			}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
	}

}
}
