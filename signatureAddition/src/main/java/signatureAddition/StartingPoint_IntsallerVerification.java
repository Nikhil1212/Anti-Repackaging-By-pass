package signatureAddition;
/**
 * This class takes the package name as an input and it will insert the original certificate details of the app to by-pass the certificate validation present in the app.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import installerVerficationByPass.*;



public class StartingPoint_IntsallerVerification {
	public static String apksigner="/home/nikhil/Android/Sdk/build-tools/30.0.2/apksigner";

	public static void main(String[] args) throws Exception {

		String pathToApk="/home/nikhil/Documents/apps/InstallerVerificationApps.txt";
		File file=new File(pathToApk);
		Scanner scanner=new Scanner(file);
		while(scanner.hasNext())
		{
			try
			{

				String packageName=scanner.next();
				String fileNamePath="/home/nikhil/Documents/apps/dataset/"+packageName+"/base.apk";


				String pathToDisAssembleCode="/home/nikhil/Documents/apps/"+packageName;

				System.out.println(packageName);

				disassembleApk(fileNamePath,packageName);
				codeInjectionByPassIntallerVerification(pathToDisAssembleCode);

				String modifiedApkPath=buildApk(packageName);
				signApk(packageName, modifiedApkPath);

				removeDirectory(pathToDisAssembleCode);

			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

		}
	}

	public static void codeInjectionByPassIntallerVerification(String pathToDisAssembleCode) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		
		List<String> patternList=new ArrayList<String>();
		
		patternListInitialize(patternList);
		for(int i=0;i<patternList.size();i++)
		{
			Process process=Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", "grep -r -l '"+patternList.get(i)+"' "+ pathToDisAssembleCode});///home/nikhil/Documents/apps/com.mbanking.aprb.aprb"});

			process.waitFor();

			HashSet<String> hashSet=fetchValidFiles(process);

			Iterator<String> iterator = hashSet.iterator();
			while (iterator.hasNext())
			{
				//count++;
				String filePath=iterator.next();
			if(filePath.contains(".smali"))
				Main.modifyInstallerVerificationClass(filePath);
			}
		}
		
		


	}
	public static void patternListInitialize(List<String> patternList) {
		// TODO Auto-generated method stub
		String pattern="Landroid/content/pm/PackageManager;->getInstallerPackageName(Ljava/lang/String;)";
		patternList.add(pattern);
		pattern="Landroid/content/pm/PackageManager;->getInstallSourceInfo(Ljava/lang/String;)";
		patternList.add(pattern);
	}

	public static HashSet<String> fetchValidFiles(Process process) throws IOException, InterruptedException {

		/*
		 * This code will 
		 */
		HashSet <String> validFiles=new HashSet<String>();
		BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(process.getInputStream()));

		String line=bufferedReader.readLine();
		while(line!=null)
		{
			//System.out.println(line);
			if(ValidFilesForCodeInjection.fileCheck(line))
			{
				validFiles.add(line);
			}
			line=bufferedReader.readLine();
		}
		System.out.println("List of valid files :"+validFiles);
		return validFiles;

	}

	public static void removeDirectory(String pathToDisAssembleCode) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		String command="rm -r "+pathToDisAssembleCode;
		Process pr=commandExecution(command);
		BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getErrorStream()));
		String line = "";
		while((line=buf.readLine())!=null)
		{
			System.out.println(line);
		}
	}





	public static Process commandExecution(String string) throws IOException, InterruptedException {

		Process pr = Runtime.getRuntime().exec(string);

		pr.waitFor();

		return pr;

	}

	public static String getPackageName(String fileNamePath) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		int i=15;
		System.out.println("Filename path:"+fileNamePath);
		String aaptstr = "/home/nikhil/Android/Sdk/build-tools/27.0.3/aapt dump badging ";
		aaptstr=aaptstr.concat(fileNamePath);
		aaptstr=aaptstr.concat("  | grep package:\\ name");
		Process pr=commandExecution(aaptstr);
		//Process pr = run.exec(cmd);
		BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
		String line = "";
		line=buf.readLine();
		if(line==null)
			System.out.println("Interesting");
		else
		{
			System.out.println("Value of line:"+line);
			for( i=15;i<line.length();i++)
			{
				if(line.charAt(i)==39)
				{
					break;
				}
			}
		}

		return line.substring(15, i);

	}

	public static void disassembleApk(String apkPath, String packageName) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		String apktoolCommand="apktool d -r "+apkPath+" -f -o /home/nikhil/Documents/apps/"+packageName;
		System.out.println(apktoolCommand);
		commandExecution(apktoolCommand);

	}
	public static String buildApk(String packageName) throws IOException, InterruptedException {
		// TODO Auto-generated method stub

		System.out.println("Inside the buildAPK process");
		String buildApkPath="/home/nikhil/Documents/apps/modified_"+packageName+".apk";
		String apktoolBuildCommand="apktool b /home/nikhil/Documents/apps/"+packageName+" -o /home/nikhil/Documents/apps/modified_"+packageName+".apk";
		System.out.println(apktoolBuildCommand);
		Process pr=commandExecution(apktoolBuildCommand);
		//pr.waitFor();
		BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(pr.getErrorStream()));
		String line="";
		while((line=bufferedReader.readLine())!=null)
		{
			System.out.println(line);
		}
		return buildApkPath;
		
	}

	public static void signApk(String packageName, String apkPath) throws IOException, InterruptedException {
		
		String pathToJks="/home/nikhil/Documents/key/hello.jks ";
		String apksigner=StartingPoint_IntsallerVerification.apksigner+" sign --ks "+pathToJks + "--ks-pass pass:123456 " + apkPath;
	
		Process pr=commandExecution(apksigner);
		BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getErrorStream()));
		String line = "";
		while ((line=buf.readLine())!=null) {
			System.out.println(line);
		}

	}


}


