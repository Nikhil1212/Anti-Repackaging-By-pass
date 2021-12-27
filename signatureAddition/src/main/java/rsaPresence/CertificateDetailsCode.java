package rsaPresence;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import signatureAddition.CommandExecute;
import signatureAddition.StartingPoint;
import signatureAddition.fetchCertificateKey;

public class CertificateDetailsCode {

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub

		String pathToFilePackageNames="/home/nikhil/Documents/apps/AppNoApkInMachine.txt";
		File file=new File(pathToFilePackageNames);
		Scanner scanner=new Scanner(file);
		while(scanner.hasNext())
		{
			String packageName=scanner.next();
			String pathToApk="/home/nikhil/Documents/apps/dataset/"+packageName+"/base.apk";
			try
			{
				StartingPoint.disassembleApk(pathToApk, packageName);
				String pathToDisAssembleCode="/home/nikhil/Documents/apps/"+packageName;
				String fullRSAfetch= StartingPoint.fetchRSAName(packageName);
				
				String signCertificateKey=fetchCertificateKey.getCertificateInHex(fullRSAfetch, packageName);
			
				System.out.println(signCertificateKey);
				
				if(checkCertificateInsideCode(signCertificateKey,pathToDisAssembleCode))
				{
					Main.updateTable(packageName, 'Y', "CertificateInCode");
				}
				else
					Main.updateTable(packageName, 'N', "CertificateInCode");
				
				CommandExecute.commandExecution("rm -r "+pathToDisAssembleCode);

			}
			catch (Exception e) {
			
				e.printStackTrace();

			}
			
			break;
		}

	}

	public static boolean checkCertificateInsideCode(String signCertificateKey, String pathToDisAssembleCode) {
		Process process;
		try {
			
			process = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", "grep -r -l "+ signCertificateKey +" "+pathToDisAssembleCode});
			process.waitFor();
			String line=new BufferedReader(new InputStreamReader(process.getInputStream())).readLine();
			System.out.println("Value is :"+line);
			if(line==null)
				return false;
			return true;
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}///home/nikhil/Documents/apps/com.mbanking.aprb.aprb"});
		
	
		
		return false;
	}
}
