package signatureAddition.pastHardwork;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class uninstallApps {

	public static void main(String[] args) throws InterruptedException, IOException {
		// TODO Auto-generated method stub
		String filePathPackageNameList="D:\\Apps\\txtFilesDataset\\newApks.txt";
		
		uninstallApp(filePathPackageNameList);
	}
	private static void uninstallApp(String filePath) throws InterruptedException, IOException {
		// TODO Auto-generated method stub
		File source=new File(filePath);
		Scanner scanner=new Scanner(source);
		while(scanner.hasNext())
		{
			String packageName=scanner.next();
			commandExecution("adb uninstall "+packageName);
			System.out.println("Package name :"+packageName+" successfully uninstalled");
		}

	}
	private static Process commandExecution(String string) throws  InterruptedException, IOException {
		// TODO Auto-generated method stub
		Process pr = Runtime.getRuntime().exec(string);
		
		pr.waitFor();
		//BufferedReader buf1 = new BufferedReader(new InputStreamReader(pr.getInputStream()));
	//BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
		/*String line = "";
		while ((line=buf.readLine())!=null) {
			//as in the first line only we can get the package name.That's why immeditate break;
			System.out.println(line);
		}
		buf.close();*/
		return pr;

	}

}
