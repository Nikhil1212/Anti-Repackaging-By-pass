package FrontEndAutomation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import org.openqa.selenium.WebDriver;

import org.openqa.selenium.chrome.ChromeDriver;

import signatureAddition.DataBaseConnect;

public class InstallationCount {

	public static void main(String[] args) throws InterruptedException, IOException {
		// TODO Auto-generated method stub
		String filePath="/home/nikhil/Documents/apps/FinalDataset.txt";
		File file=new File(filePath);
		
		System.setProperty("webdriver.chrome.driver","/home/nikhil/Downloads/chromedriver_linux64/chromedriver");  
		// Instantiate a ChromeDriver class.      
		WebDriver driver=new ChromeDriver();  
		Statement statement=DataBaseConnect.initialization();

	
		Scanner scanner=new Scanner(file);
		
		while(scanner.hasNext())
		{
			String packageName=scanner.next();
			openAppInstallationPage(driver, packageName,statement);
			
		}
		
	}

	
	private static void openAppInstallationPage(WebDriver driver, String packageName, Statement statement) throws InterruptedException, IOException {
		
	//	packageName="com.hdfc.totablet";

		String myURL="https://play.google.com/store/apps/details?id="+packageName;
		driver.get(myURL);
		Thread.sleep(5000);
		String pageSource=driver.getPageSource();
		int num=getInstallationCount(pageSource);
		String outputPath="/home/nikhil/Documents/apps/InstallationCount/"+packageName+".html";
		storeToDB(packageName,num,statement);
		FileWriter fileWriter=new FileWriter(outputPath);
		fileWriter.write(pageSource);
		fileWriter.close();
		System.out.println();

	}

	private static void storeToDB(String packageName, int num, Statement statement) {
		// TODO Auto-generated method stub
	//	packageName="com.hdfc.totablet";
		String query="Insert into AppInstallationCounts values ('"+packageName+"','"+num+"');";
		System.out.println(query);

		//Statement statement=DataBaseConnect.initialization();
		try {
			statement.executeUpdate(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private static int getInstallationCount(String pageSource) {
		
		String downloadPattern="\"Downloaded ";
		String endPattern=" times";
		int start=pageSource.indexOf(downloadPattern);
		if(start==-1)
			return start;
		String temp=pageSource.substring(start);
		System.out.println(temp);
		temp=temp.substring(downloadPattern.length());
		
		System.out.println("After filtering: "+temp);
		
		int end=temp.indexOf(endPattern);
		
		
		String string_in_num=temp.substring(0,end);
		System.out.println(string_in_num);
		// TODO Auto-generated method stub
		int x=Integer.parseInt(string_in_num);
		return x;
	}

}
