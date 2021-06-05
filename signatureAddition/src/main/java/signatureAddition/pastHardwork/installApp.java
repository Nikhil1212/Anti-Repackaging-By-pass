package apkDownload;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class installApp {

	public static void main(String[] args) throws InterruptedException, IOException {
		// TODO Auto-generated method stub
		String filePathRePackageNameList="D:\\Apps\\newApps\\repackaged\\newAppsList.txt";
		File file=new File(filePathRePackageNameList);
		Scanner scanner= new Scanner(file);
		while(scanner.hasNext())
		{
			String apkPath=scanner.next();
			System.out.println(apkPath);
			installApp(apkPath);
		}
	}
	private static void installApp(String filePath) throws InterruptedException, IOException {
		// TODO Auto-generated method stub
			String adbInstall="adb install ";
			adbInstall=adbInstall.concat(filePath);
			commandExecution(adbInstall);
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
