package apkDownload;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class creatingRepackagedVersion {

	public static void main(String[] args) throws InterruptedException, IOException {
		// TODO Auto-generated method stub
		String filePathRePackageNameList="/home/Nikhil/Downloads/Softwares/googleplay-api-master/dataset/snapwork.txt";
		File file=new File(filePathRePackageNameList);
		Scanner scanner= new Scanner(file);
		while(scanner.hasNext())
		{
			String apkPath=scanner.next();
			signApk(apkPath);
		}
		
	}
	private static void signApk(String apkPath) throws  InterruptedException, IOException {
		// TODO Auto-generated method stub
		String pathToJks="/home/Nikhil/Documents/keystore/hello.jks";
		String pattern="/home/Nikhil/Downloads/Softwares/googleplay-api-master/dataset/";
		String packageNameApk=apkPath.substring(pattern.length());
		//String passwordPath="/home/nikhil/Documents/key/password";
		String apksigner="/usr/bin/apksigner sign --ks "+pathToJks + "--ks-pass pass:123456 --in" + apkPath+" --out /home/Nikhil/Downloads/Softwares/googleplay-api-master/repackaged/"+packageNameApk;		
		System.out.println(apksigner);//String apktoolBuildCommand="apktool b /home/nikhil/Documents/apps/"+packageName+" -o /home/nikhil/Documents/apps/modified_"+packageName+".apk";
		
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
