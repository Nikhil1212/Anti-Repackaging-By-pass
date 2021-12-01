package signatureAddition;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
public class uninstallReinstall {

	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		
		uninstallApp();
		System.out.println("Uninstallation has happened. Now let's install the repackaged version");
		installApps();
		//installApks();
	}

	private static void installApps() throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		String filePathRepackaged="/home/nikhil/Documents/apps/InstallerVerification.txt";
		File file=new File(filePathRepackaged);
		Scanner scanner=new Scanner(file);
		while(scanner.hasNext())
		{
			String packageName=scanner.next();
			String prefixPath="/home/nikhil/Documents/apps/modified_";
			String installCommand="/home/nikhil/Android/Sdk/platform-tools/adb install -r "+prefixPath+packageName+".apk";
			commandExecution(installCommand);
			System.out.println("App installation for the package name: "+packageName.substring(packageName.indexOf("com"))+" happened successfully");
		}
	}

	private static void uninstallApp() throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		String filePathRepackaged="/home/nikhil/Documents/apps/InstallerVerification.txt";
		File file=new File(filePathRepackaged);
		Scanner scanner=new Scanner(file);
		while(scanner.hasNext())
		{
			String packageName=scanner.next();
			String uninstallCommand="/home/nikhil/Android/Sdk/platform-tools/adb uninstall "+packageName;
			commandExecution(uninstallCommand);
			System.out.println("App uninstallation for the package name: "+packageName+" happened successfully");
		}
	}

	private static void installApks() throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		String filePathRepackaged="/home/nikhil/Documents/apps/sbiRepackagedApks.txt";
		File file=new File(filePathRepackaged);
		Scanner scanner=new Scanner(file);
		while(scanner.hasNext())
		{
			String apkPath=scanner.next();
			String installCommand="/home/nikhil/Android/Sdk/platform-tools/adb install -r "+apkPath;
			commandExecution(installCommand);
			System.out.println("App installation for the package name: "+apkPath.substring(apkPath.indexOf("com"))+" happened successfully");
		}
	}
	private static Process commandExecution(String string) throws IOException, InterruptedException {
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
