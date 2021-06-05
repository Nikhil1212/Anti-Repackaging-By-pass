package signatureAddition.pastHardwork;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class appInstallationPlayStore {  

	public static void main(String[] args) throws FileNotFoundException {  

		//String FilePath="D:\\Softwares\\uniqueAppId.txt";
		String filePathDeveloper="D:\\Apps\\uniqueEntries.txt";
		File file=new File(filePathDeveloper);
		Scanner scanner=new Scanner(file);
		String fetchDeveloperId="Select Distinct developerId from appsFinanceCategory;";
		//String query="Select appId from packageNames;";
		//String queryForAppFinanceTrue="Update appsFinanceCategory set Finance=1 where packageName = ";

		System.setProperty("webdriver.chrome.driver","D:\\Softwares\\chromedriver_win32_90\\chromedriver.exe");  
		// Instantiate a ChromeDriver class.      
		WebDriver driver=new ChromeDriver();  


		//connecting to the database;
		Statement statement=initialization();


		//ResultSet resultSet=statement.executeQuery(query);
		int count=0;
			String packageName="in.startv.hotstar";
			String url = ("https://play.google.com/store/apps/details?id="+packageName);//in.org.npci.upiapp");  
			String buttonId="//button[@class='LkLjZd ScJHi HPiPcc IfEcue  ']";
			String signInId="//a[@class='gb_ae gb_4 gb_4c']";
			String emailId="//input[@type='email']";
			
			//checkWhether the apk is already fetched or not by querying the database

			driver.get(url);
			driver.findElement(By.xpath(signInId)).click();

			String pageSource=driver.getPageSource();
			//so now let's split the string.
			/*try {
				boolean result=fetchCategory(pageSource);
				//String developerId=fetchDeveloperId(pageSource);
				if(!result)
				{
					String insertQuery="Update appsFinanceCategory set Finance = 0 where packageName ='"+packageName+"';";
					//String setFlag="Update appsFinanceCategory set Finance=1, DeveloperId ='"+developerId+"' where packageName = '"+packageName_1+"';";
					statement.executeUpdate(insertQuery);
					System.out.println(++count);
				}

			}
			catch (Exception e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}*/

		

	}

	private static String fetchDeveloperId(String pageSource) {
		//System.out.println("Inside the fetchDeveloper Id");
		// TODO Auto-generated method stub
		String className="hrTbp R8zArc";
		String developerIdPattern="/store/apps/developer?id";
		//String financeKeyword="FINANCE";
		String splitarr[]=pageSource.split(className);
		String developerId=null;
		int index=0;
		int flag=0;
		int i;
		for( i=0;i<splitarr.length;i++)
		{
			//System.out.println(splitarr[i]);
			if(splitarr[i].contains(developerIdPattern))
			{
				flag=1;
				//	 System.out.println("yes, we can see the developer id pattern ");
				index=splitarr[i].indexOf(developerIdPattern);
				break;
			}
		}
		if(flag==0)
			System.out.println("We could not find the developer id pattern");
		else
		{
			int j;
			for( j=index;splitarr[i].charAt(j)!=' ';j++)
			{
				System.out.print(splitarr[i].charAt(j));
			}
			developerId=splitarr[i].substring(index+25, j-1);
			//System.out.println("Developer id is :"+developerId);
		}
		return developerId;
	}


	private static boolean fetchCategory(String pageSource) {
		// TODO Auto-generated method stub
		String className="hrTbp R8zArc";
		String financeKeyword="FINANCE";
		String splitarr[]=pageSource.split(className);
		String string=splitarr[splitarr.length-2];
		//System.out.println("Value of the split array:" + string);
		if(string.contains(financeKeyword))
		{
			return true;
		}
		return false;
	}


	private static Statement initialization() {
		// TODO Auto-generated method stub
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String sqlurl="jdbc:mysql://10.192.38.90:3306/apps";
			Connection cn=DriverManager.getConnection(sqlurl,"nikhil","nikhil");
			Statement st=cn.createStatement();
			return st;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	} 
}