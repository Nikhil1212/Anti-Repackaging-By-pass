package rootDetectionByPass;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Scanner;

import databaseUpdate.TableUpdate;
import signatureAddition.CommandExecute;
import signatureAddition.StartingPoint;
import signatureAddition.PullApps.AppsPull;

public class SafetyNetPattern {

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		String pathToFilePackageNames="/home/nikhil/Documents/apps/SafetyNetApps.txt";
		File file=new File(pathToFilePackageNames);
		Scanner scanner=new Scanner(file);
		int count=0;
		while(scanner.hasNext())
		{
			String packageName=scanner.next();
		//	packageName="in.org.npci.upiapp";
			
			String pathToApk=AppsPull.appDirectoryPrefix+packageName+"/base.apk";
			try {
				count++;
				System.out.println("Package count is :"+count);
				StartingPoint.disassembleApk(pathToApk, packageName);
				String pathToDisAssembleCode="/home/nikhil/Documents/apps/"+packageName;
				//Execute the find command
				String basicIntegrityPattern="'basicIntegrity:Ljava/lang/Boolean;'";
				String ctsProfileMatchPattern="'ctsProfileMatch:Ljava/lang/Boolean;'";
				
				String grepCommand="grep -r -l "+ basicIntegrityPattern + " "+ pathToDisAssembleCode;
				System.out.println(grepCommand);
				Process process=CommandExecute.commandExecutionSh(grepCommand);
				String line=new BufferedReader(new InputStreamReader(process.getInputStream())).readLine();
				
				System.out.println("Value is :"+line);
				if(line!=null)
				{
					TableUpdate.updateTable(packageName, "SafetyNetApps");
				}
				else
				{
					grepCommand="grep -r -l "+ ctsProfileMatchPattern + " "+ pathToDisAssembleCode;
					process=CommandExecute.commandExecution(grepCommand);
					line=new BufferedReader(new InputStreamReader(process.getInputStream())).readLine();
					if(line!=null)
					{
						TableUpdate.updateTable(packageName, "SafetyNetApps");
					}

				}
				
				CommandExecute.commandExecution("rm -r "+pathToDisAssembleCode);
				
			
			}
			catch (Exception e) {
				
				e.printStackTrace();
			
			} 
			//break;
		}



	}

}
