package signatureAddition;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class StartingPoint {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		int i=0;
		//String fileNamePath="/home/nikhil/Documents/apks/equitas.apk";
		//basicGrepCommand();
		//String pathToApk="/home/nikhil/Documents/apps/pathToApps_1.txt";
		String pathToApk="/home/nikhil/Documents/apks/appsHaveAntiRepackagingCheck/fileName.txt";
		File file=new File(pathToApk);
		Scanner scanner=new Scanner(file);
		while(scanner.hasNext())
		{
			try
			{
				String fileNamePath=scanner.next();
				//below few lines are the code which is used to generate the final package name
				String packageName=getPackageName(fileNamePath);

				
				String pathToDisAssembleCode="/home/nikhil/Documents/apps/"+packageName;
				
				System.out.println(packageName);

				//package name is retrieved using aapt 
				disassembleApk(fileNamePath,packageName);

				//Successfully disassmeble the apk with ignoring resource
				String fullRSAfetch= fetchRSAName(packageName);

				String signCertificateKey=fetchCertificateKey.getCertificateInHex(fullRSAfetch, packageName);
				System.out.println(signCertificateKey);
				
				FileNamesForSignatureAddition.codeInjectionProcess(signCertificateKey, pathToDisAssembleCode);
				//fetchCertificateKey.codeInjection();
				String modifiedApkPath=buildApk(packageName);
				signApk(packageName, modifiedApkPath);
				fileNameFetch(packageName);
				removeDirectory(pathToDisAssembleCode);

			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
		}
			}

	private static void removeDirectory(String pathToDisAssembleCode) throws IOException, InterruptedException {
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

	private static void basicGrepCommand() throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		//String grep="/bin/sh -c grep -rwn -l \"ICSE\" /home/nikhil/Documents/testGrep/";
		//String grep="/bin/grep -rwn -l \"ICSE\" /home/nikhil/Documents/testGrep/";
	    String[] cmd = { "/bin/sh", "-c", "grep -r \"Niranjan\" /home/nikhil/Documents/testGrep" };

	    Process pr = Runtime.getRuntime().exec(cmd);
		pr.waitFor();
		
		BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getErrorStream()));
		String line = "";
		while((line=buf.readLine())!=null)
		{
			System.out.println(line);
		}
		}

	private static void fileNameFetch(String packageName) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		String pathToDisAssembleCode="/home/nikhil/Documents/"+packageName;
		String commandToFindPatterns="grep -rwn -l \"Landroid/content/pm/Signature;->toByteArray\" "+pathToDisAssembleCode;
		Process pr=commandExecution(commandToFindPatterns);
		BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
		String line = "";
		while((line=buf.readLine())!=null)
		{
			System.out.println(line);
		}

	}

	private static String fetchRSAName(String packageName) throws InterruptedException, IOException {
		// TODO Auto-generated method stub
		String fullRSAfetch="/home/nikhil/Documents/apps/"+packageName+"/original/META-INF/";
		System.out.println(fullRSAfetch);
		Process pr=commandExecution("ls "+fullRSAfetch);
		BufferedReader buf1 = new BufferedReader(new InputStreamReader(pr.getInputStream()));

		String line = "";
		int count=0;
		int flag=0;
		// to fetch the name of exact rsa file
		while ((line=buf1.readLine())!=null) {
			count++;
			if(line.contains(".RSA"))
			{
				flag=1;
				System.out.println(line);
				break;
			}
		}

		fullRSAfetch=fullRSAfetch.concat(line);
		System.out.println(fullRSAfetch);

		return fullRSAfetch;
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

	private static String getPackageName(String fileNamePath) throws IOException, InterruptedException {
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

	private static void disassembleApk(String fileNamePath, String packageName) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		String apktoolCommand="apktool d -r "+fileNamePath+" -f -o /home/nikhil/Documents/apps/"+packageName;
		System.out.println(apktoolCommand);
		commandExecution(apktoolCommand);
		//Process pr1 = Runtime.getRuntime().exec(apktoolCommand);
		//pr1.waitFor();

	}
	private static String buildApk(String packageName) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		
		System.out.println("Inside the buildAPK process");
		String buildApkPath="/home/nikhil/Documents/apps/modified_"+packageName+".apk";
		String apktoolBuildCommand="apktool b /home/nikhil/Documents/apps/"+packageName+" -o /home/nikhil/Documents/apps/modified_"+packageName+".apk";
		System.out.println(apktoolBuildCommand);
		Process pr=commandExecution(apktoolBuildCommand);
		pr.waitFor();
		BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(pr.getErrorStream()));
		String line="";
		while((line=bufferedReader.readLine())!=null)
		{
			System.out.println(line);
		}
		return buildApkPath;
		//Process pr1 = Runtime.getRuntime().exec(apktoolCommand);
		//pr1.waitFor();

	}

	private static void signApk(String packageName, String apkPath) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		String pathToJks="/home/nikhil/Documents/key/hello.jks ";
		//String passwordPath="/home/nikhil/Documents/key/password";
		String apksigner="apksigner sign --ks "+pathToJks + "--ks-pass pass:123456 " + apkPath;
		//String apktoolBuildCommand="apktool b /home/nikhil/Documents/apps/"+packageName+" -o /home/nikhil/Documents/apps/modified_"+packageName+".apk";
		//System.out.println(apktoolBuildCommand);
		Process pr=commandExecution(apksigner);
		BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getErrorStream()));
		String line = "";
		while ((line=buf.readLine())!=null) {
			//as in the first line only we can get the package name.That's why immeditate break;
			System.out.println(line);
		}

		//Process pr1 = Runtime.getRuntime().exec(apktoolCommand);
		//pr1.waitFor();

	}

	private static String input() {
		// TODO Auto-generated method stub

		System.out.println("Enter the absolute path to the apk:");

		Scanner scanner=new Scanner(System.in);
		String fileNamePath=scanner.next();

		return fileNamePath;
	}
}


