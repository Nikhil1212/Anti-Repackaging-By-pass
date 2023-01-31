package signatureAddition;
/**
 * This class takes the package name as an input and it will insert the original certificate details of the app to by-pass the certificate validation present in the app.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import installerVerficationByPass.*;
import rootDetectionByPass.StartingPoint_RootDetection;
public class StartingPoint {
	public static String apksigner="/home/nikhil/Android/Sdk/build-tools/30.0.2/apksigner";

	public static void main(String[] args) throws Exception {

		int i=0;
		String pathToApk="/home/nikhil/Documents/apps/FridaReRun.txt";
		File file=new File(pathToApk);
		Scanner scanner=new Scanner(file);
		while(scanner.hasNext())
		{
			try
			{

				String packageName=scanner.next();
//				packageName="com.hdfc.totablet";
			//	packageName="com.khaalijeb.inkdrops";
				//packageName="com.YONOMUApp";
				String fileNamePath="/home/nikhil/Documents/apps/dataset/"+packageName+"/base.apk";
//				fileNamePath="/home/nikhil/Documents/apps/MSApps/Teams/base.apk";
				//below few lines are the code which is used to generate the final package name
				//String packageName=getPackageName(fileNamePath);
				//System.out.println("Checking whehther you can see me over github");

			//	modifiedApkGeneration(packageName);
				String pathToDisAssembleCode="/home/nikhil/Documents/apps/"+packageName;

				System.out.println(packageName);

				//package name is retrieved using aapt 
				disassembleApk(fileNamePath,packageName);

				String fullRSAfetch= fetchRSAName(packageName);

				String signCertificateKey=fetchCertificateKey.getCertificateInHex(fullRSAfetch, packageName);
				System.out.println(signCertificateKey);

				FileNamesForSignatureAddition.codeInjectionProcess(signCertificateKey, pathToDisAssembleCode);

				StartingPoint_IntsallerVerification.codeInjectionByPassIntallerVerification(pathToDisAssembleCode);
				//
				StartingPoint_AntiEmulation.codeInjectionByPassAntiEmulation(pathToDisAssembleCode);

				StartingPoint_RootDetection.main(pathToDisAssembleCode);

				String modifiedApkPath=buildApk(packageName);
				signApk(packageName, modifiedApkPath);
				
			//	isModifiedApkGenerated(modifiedApkPath,packageName);
			
				//fileNameFetch(packageName);
				//removeDirectory(pathToDisAssembleCode);
				break;

			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

		}
	}

	private static void isModifiedApkGenerated(String modifiedApkPath, String packageName) throws IOException, InterruptedException, SQLException {
		// TODO Auto-generated method stub
		Process process=CommandExecute.commandExecution("ls "+modifiedApkPath);
		String line=new BufferedReader(new InputStreamReader(process.getInputStream())).readLine();
		if(line==null)
		{
			//Modified apk not generated. Update the table
			
			updateTable(packageName,'N',"Reason unknown","ModifiedApkGeneration");
			
		}
		else
			updateTable(packageName,'Y',"","ModifiedApkGeneration");
		
	}

	public static void updateTable(String packageName, char c, String remarks, String tableName) throws SQLException {
		// TODO Auto-generated method stub
		
		String checkQuery="Select packagename from "+tableName+" where packageName ='"+packageName+"';";
		System.out.println(checkQuery);
		Statement statement1=DataBaseConnect.initialization();
		ResultSet resultSet=statement1.executeQuery(checkQuery);
		int flag=0;
		String output="";
		while(resultSet.next())
		{
			flag=1;
			output=output+ resultSet.getString(1)+"\n";
		}
		if(flag==0)
		{
			String query="Insert into "+tableName+" values ('"+packageName+"','"+c+"','"+remarks+"');";
			System.out.println(query);

			Statement statement=DataBaseConnect.initialization();
			statement.executeUpdate(query);
		}
		else
		{
			String query="Update "+tableName+" set IsApkGenerated ='"+c+"' , remarks ='"+remarks+"' where packageName='"+packageName+"';";
			System.out.println(query);

			Statement statement=DataBaseConnect.initialization();
			statement.executeUpdate(query);
		}


		
	}
	public static String modifiedApkGenerationAntiEmulator(String packageName, String fileNamePath) {
		// TODO Auto-generated method stub
	//	String fileNamePath="/home/nikhil/Documents/apps/dataset/"+packageName+"/base.apk";
		String pathToDisAssembleCode="/home/nikhil/Documents/apps/"+packageName;
		
		
		String modifiedApkPath="";
		try
		{
			System.out.println(packageName);
			
			//package name is retrieved using aapt 
			disassembleApk(fileNamePath,packageName);

		//	String fullRSAfetch= fetchRSAName(packageName);

			//String signCertificateKey=fetchCertificateKey.getCertificateInHex(fullRSAfetch, packageName);
			//System.out.println(signCertificateKey);

		//	FileNamesForSignatureAddition.codeInjectionProcess(signCertificateKey, pathToDisAssembleCode);

			//StartingPoint_IntsallerVerification.codeInjectionByPassIntallerVerification(pathToDisAssembleCode);
			//
			StartingPoint_AntiEmulation.codeInjectionByPassAntiEmulation(pathToDisAssembleCode);

			StartingPoint_RootDetection.main(pathToDisAssembleCode);

			 modifiedApkPath=buildApk(packageName);
			signApk(packageName, modifiedApkPath);
			//fileNameFetch(packageName);
			removeDirectory(pathToDisAssembleCode);
		}
		catch (Exception e) {
			
			e.printStackTrace();
		}
		return modifiedApkPath;
		
	}
	
	public static String modifiedApkGenerationRootDetection(String packageName, String fileNamePath) {
		// TODO Auto-generated method stub
	//	String fileNamePath="/home/nikhil/Documents/apps/dataset/"+packageName+"/base.apk";
		String pathToDisAssembleCode="/home/nikhil/Documents/apps/"+packageName;
		
		
		String modifiedApkPath="";
		try
		{
			System.out.println(packageName);
			
			//package name is retrieved using aapt 
			disassembleApk(fileNamePath,packageName);

			String fullRSAfetch= fetchRSAName(packageName);

			String signCertificateKey=fetchCertificateKey.getCertificateInHex(fullRSAfetch, packageName);
			System.out.println(signCertificateKey);

			FileNamesForSignatureAddition.codeInjectionProcess(signCertificateKey, pathToDisAssembleCode);

			StartingPoint_IntsallerVerification.codeInjectionByPassIntallerVerification(pathToDisAssembleCode);
			//
//			StartingPoint_AntiEmulation.codeInjectionByPassAntiEmulation(pathToDisAssembleCode);

			StartingPoint_RootDetection.main(pathToDisAssembleCode);

			 modifiedApkPath=buildApk(packageName);
				isModifiedApkGenerated(modifiedApkPath,packageName);

			signApk(packageName, modifiedApkPath);
			//fileNameFetch(packageName);
			removeDirectory(pathToDisAssembleCode);
		}
		catch (Exception e) {
			
			e.printStackTrace();
		}
		return modifiedApkPath;
		
	}
	
	public static String modifiedApkGenerationAntiTampering(String packageName, String fileNamePath) {
		// TODO Auto-generated method stub
	//	String fileNamePath="/home/nikhil/Documents/apps/dataset/"+packageName+"/base.apk";
		String pathToDisAssembleCode="/home/nikhil/Documents/apps/"+packageName;
		
		
		String modifiedApkPath="";
		try
		{
			System.out.println(packageName);
			
			//package name is retrieved using aapt 
			disassembleApk(fileNamePath,packageName);

			String fullRSAfetch= fetchRSAName(packageName);

			String signCertificateKey=fetchCertificateKey.getCertificateInHex(fullRSAfetch, packageName);
			System.out.println(signCertificateKey);

			FileNamesForSignatureAddition.codeInjectionProcess(signCertificateKey, pathToDisAssembleCode);

		//	StartingPoint_IntsallerVerification.codeInjectionByPassIntallerVerification(pathToDisAssembleCode);
			//
//			StartingPoint_AntiEmulation.codeInjectionByPassAntiEmulation(pathToDisAssembleCode);

			//StartingPoint_RootDetection.main(pathToDisAssembleCode);

			 modifiedApkPath=buildApk(packageName);
			signApk(packageName, modifiedApkPath);
			//fileNameFetch(packageName);
		//	removeDirectory(pathToDisAssembleCode);
		}
		catch (Exception e) {
			
			e.printStackTrace();
		}
		return modifiedApkPath;
		
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

	public static void fileNameFetch(String packageName) throws IOException, InterruptedException {
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

	public static String fetchRSAName(String packageName) throws InterruptedException, IOException {
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
		if(flag==0)
		{
			//query the database to find the app
			return null;
		}

		fullRSAfetch=fullRSAfetch.concat(line);
		System.out.println(fullRSAfetch);

		return fullRSAfetch;
	}
	
	public static String fetchCertificateFromDatabase(String packageName, String tableName) throws SQLException {
		// TODO Auto-generated method stub
		String checkQuery="Select certificateDetails from "+tableName+" where packageName='"+packageName+"';";
		System.out.println(checkQuery);
		Statement statement1=DataBaseConnect.initialization();
		ResultSet resultSet=statement1.executeQuery(checkQuery);
		int flag=0;
		String output="";
		while(resultSet.next())
		{
			flag=1;
			output=resultSet.getString(1);
		}
		return output;
	

	}


	public static Process commandExecution(String string) throws IOException, InterruptedException {
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
		CommandExecute.commandExecutionSh(apktoolCommand);

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
		//Process pr1 = Runtime.getRuntime().exec(apktoolCommand);
		//pr1.waitFor();

	}

	public static void signApk(String packageName, String apkPath) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		String pathToJks="/home/nikhil/Documents/key/hello.jks ";
		//String passwordPath="/home/nikhil/Documents/key/password";
		String apksigner=StartingPoint.apksigner+" sign --ks "+pathToJks + "--ks-pass pass:123456 " + apkPath;
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

	public static String input() {
		// TODO Auto-generated method stub

		System.out.println("Enter the absolute path to the apk:");

		Scanner scanner=new Scanner(System.in);
		String fileNamePath=scanner.next();

		return fileNamePath;
	}
}


