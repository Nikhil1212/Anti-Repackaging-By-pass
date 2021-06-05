/**
 * It requires the smartphone to have the app installed. SO, then we are fetching the apk. For simplicity, we are assuming there are no split apks.
 */


package signatureAddition.pastHardwork;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;

public class fetchApkFromSmartphone {

	public static void main(String[] args) throws InterruptedException, IOException {
		// TODO Auto-generated method stub
		String filePathPackageNameList="D:\\Apps\\txtFilesDataset\\newApks.txt";
		
		String rePackageNamePath="D:\\Apps\\modifiedApks.txt";
//		installApp(rePackageNamePath);
		/**
		 * In future, we will take the file name as an input
		 */
		
		
		String pathToBase_apk=null;
		String baseApk="base.apk";
		String pullCommand="adb pull ";
		File file=new File(filePathPackageNameList);
		File fileForRepackaged=new File(rePackageNamePath);
		fileForRepackaged.createNewFile();
		Scanner scanner=new Scanner(file);
		while(scanner.hasNext())
		{
			String packageName=scanner.next();
			System.out.println("Currently for the package :"+packageName);
			String pathToapkCommand="adb shell pm path ";
		//	addingPathForRePackaged(rePackageNamePath,packageName);
			
			pathToapkCommand=pathToapkCommand.concat(packageName);
			Process pr=commandExecution(pathToapkCommand);
			
			
			System.out.println(pathToapkCommand);
			/*
			 * Let's fetch the directory in which base.apk
			 * file is present
			 */
			printErrors(pr);
			BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
			//String pathToBase_apk = "";
			pathToBase_apk =buf.readLine();
			if(pathToBase_apk==null)
			{
				System.out.println("for package name :"+packageName+" , it is returning null");
				continue;
			}
				
			int pos=pathToBase_apk.indexOf(baseApk); 
			/*
			 * We are fetching the entire folder as there is a possibility of split.apks
			 * So the above code trims the base.apk string
			 */
			String commandToPullDirectory=pullCommand+pathToBase_apk.substring(8,pos)+" D:\\Apps\\newApps";
			
			String commandToPullApk=pullCommand+pathToBase_apk.substring(8)+" D:\\Apps\\newApps\\"+packageName+".apk";
			System.out.println(commandToPullApk);
			commandExecution(commandToPullApk);
		
		}
		/*
		 * Let's remove the app as
		 * simply it's occupying the space in the smartphone
		 */
		
		//uninstallApp(filePathPackageNameList);
		
		String repackagedFilePath = null;
		/**
		 * Install the repackaged apks.
		 */
		//installApp(rePackageNamePath);
	}
	private static void addingPathForRePackaged(String rePackageNamePath, String packageName) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		
		String path="D:\\Apps\\SBI_Apps\\repackaged\\"+packageName+".apk";
		signApk(packageName, path);
		Files.write(Paths.get(rePackageNamePath), (path+"\n").getBytes(),  StandardOpenOption.APPEND);
	}
	private static void installApp(String filePath) throws InterruptedException, IOException {
		// TODO Auto-generated method stub
		File file=new File(filePath);
		Scanner scanner=new Scanner(file);
		while(scanner.hasNext())
		{
			String adbInstall="adb install ";
			
			adbInstall=adbInstall.concat(scanner.next());
			commandExecution(adbInstall);
		}
		
	}
	private static void printErrors(Process pr) throws IOException {
		//// TODO Auto-generated method stub
		BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getErrorStream()));
		String line = "";
		while ((line=buf.readLine())!=null) {
			System.out.println(line);
		}
		
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
	private static void signApk(String packageName, String apkPath) throws  InterruptedException, IOException {
		// TODO Auto-generated method stub
		String pathToJks="D:\\keyFolder\\v1.jks ";
		//String passwordPath="/home/nikhil/Documents/key/password";
		String apksigner="D:\\AndroidStudio\\build-tools\\28.0.3\\apksigner.bat sign --ks "+pathToJks + "--ks-pass pass:123456 " + apkPath ;
		
		System.out.println(apksigner);//String apktoolBuildCommand="apktool b /home/nikhil/Documents/apps/"+packageName+" -o /home/nikhil/Documents/apps/modified_"+packageName+".apk";
		//System.out.println(apktoolBuildCommand);
		Process pr=commandExecution(apksigner);
		BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getErrorStream()));
		String line = "";
		while ((line=buf.readLine())!=null) {
			//as in the first line only we can get the package name.That's why immeditate break;
			System.out.println(line);
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
