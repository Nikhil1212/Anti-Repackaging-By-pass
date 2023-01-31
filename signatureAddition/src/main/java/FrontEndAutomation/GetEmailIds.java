package FrontEndAutomation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import org.openqa.selenium.WebDriver;

import org.openqa.selenium.chrome.ChromeDriver;

import signatureAddition.DataBaseConnect;

public class GetEmailIds {

	public static void main(String[] args) throws InterruptedException, IOException {
		// TODO Auto-generated method stub
		String filePath="/home/nikhil/Documents/apps/InstallationCount/packagenames.txt";
		File file=new File(filePath);
	 
		Statement statement=DataBaseConnect.initialization();

	
		Scanner scanner=new Scanner(file);
		
		while(scanner.hasNext())
		{
			try
			{
				String packageName=scanner.next();
				String htmlFilePath="/home/nikhil/Documents/apps/InstallationCount/"+packageName+".html";
				
				String fileContents=new String(Files.readAllBytes(Paths.get(htmlFilePath)));
				String emailId=getEmailId(fileContents);
				storeToDB(packageName, emailId, statement);
				
			}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			//break;
		}
		
	}

	
	private static void openAppInstallationPage(WebDriver driver, String packageName, Statement statement) throws InterruptedException, IOException {
		
	//	packageName="com.hdfc.totablet";

		String myURL="https://play.google.com/store/apps/details?id="+packageName;
		driver.get(myURL);
		Thread.sleep(5000);
		String pageSource=driver.getPageSource();
		
		String outputPath="/home/nikhil/Documents/apps/InstallationCount/"+packageName+".html";

		String fileContents=new String(Files.readAllBytes(Paths.get(outputPath)));

		String emailId=getEmailId(fileContents);
		
		storeToDB(packageName,emailId,statement);
		FileWriter fileWriter=new FileWriter(outputPath);
		fileWriter.write(pageSource);
		fileWriter.close();
		System.out.println();

	}

	private static void storeToDB(String packageName, String emailId, Statement statement) {
		// TODO Auto-generated method stub
	//	packageName="com.hdfc.totablet";
		String query="Insert into AppContactDetails values ('"+packageName+"','"+emailId+"');";
		System.out.println(query);

		//Statement statement=DataBaseConnect.initialization();
		try {
			statement.executeUpdate(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private static String getEmailId(String fileContents) {
		
		String mailToPattern="<a href=\"mailto:";
		String endPattern="\"";
		int start=fileContents.indexOf(mailToPattern);
		if(start==-1)
			return "Could not retrieve email id";
		String temp=fileContents.substring(start);
		System.out.println(temp);
		temp=temp.substring(mailToPattern.length());
		
		System.out.println("After filtering: "+temp);
		
		int end=temp.indexOf(endPattern);
		
		
		String emailId=temp.substring(0,end);
		System.out.println(emailId);
		// TODO Auto-generated method stub
		return emailId;
	}

}
