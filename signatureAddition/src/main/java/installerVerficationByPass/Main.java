package installerVerficationByPass;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {

	public static void modifyInstallerVerificationClass(String filePath) throws IOException {
		String pattern="Landroid/content/pm/PackageManager;->getInstallerPackageName(Ljava/lang/String;)";
		String ans="";
		File  file=new File(filePath);
		Scanner scanner=new Scanner(file);
		String prev="";
		while(scanner.hasNext())
		{
			String temp=scanner.nextLine();
			if(temp.contains(pattern))
			{
				String string=codeInjection(temp);
				ans=ans+string+"\n";
			}
			ans=ans+temp+"\n";
			//prev=scanner.
		}
		scanner.close();
		writeToFile(ans,filePath);
	
	}
	
	private static void writeToFile(String ans, String destiantionPath) throws IOException {
		// TODO Auto-generated method stub
		FileWriter fileWriter=new FileWriter(destiantionPath);
		fileWriter.write(ans);
		fileWriter.close();
	}

	public static String codeInjection(String temp) {
		// TODO Auto-generated method stub
	String string="\nconst-string"+ fetchRegisterNumber(temp)+", \"com.android.vending\"";
	return string;
		
	}
	public static String fetchRegisterNumber(String line) {
		int deadEnd=line.indexOf("}");
		int startEnd=line.indexOf(",");
		System.out.println(line);
		
		return line.substring(startEnd+1, deadEnd);
		
	}
	

}
