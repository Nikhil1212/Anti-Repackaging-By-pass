package signatureAddition.pastHardwork;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;

import org.openqa.selenium.WebDriver;

import org.openqa.selenium.chrome.ChromeDriver;;

public class fetchAllTheAppsForaGivenDeveloperId {  

	public static void main(String[] args) throws SQLException, IOException {  

		Statement statement=initialization();

		String filePathDeveloper="D:\\Apps\\developerId.txt";
		File file=new File(filePathDeveloper);
		Scanner scanner=new Scanner(file);
		//String fetchDeveloperId="Select Distinct developerId from appsFinanceCategory;";

		System.setProperty("webdriver.chrome.driver","D:\\Softwares\\chromedriver_win32_90\\chromedriver.exe");  
		// Instantiate a ChromeDriver class.      
	WebDriver driver=new ChromeDriver();  


		//connecting to the database;
		

		//trying for a basic CEdge Techno	
		while(scanner.hasNext())
		{
			String devID=scanner.next();

			String url = ("https://play.google.com/store/apps/developer?id="+devID);//CedgeTechno");  

			//checkWhether the apk is already fetched or not by querying the database

			driver.get(url);
			String pageSource=driver.getPageSource();
			//String content = new String(Files.readAllBytes(Paths.get(filePathDeveloper)));
			HashSet<String> result=fetchPackageName(pageSource, filePathDeveloper);
			Iterator<String> itr = result.iterator();
			while(itr.hasNext()){
			    String packageName=itr.next();
			    if(packageName.contains("l lang="))
			    	continue;
			    String insertQuery="Insert ignore into appsFinanceCategory values('"+packageName+"',1,'"+devID+"');";
				//String setFlag="Update appsFinanceCategory set Finance=1, DeveloperId ='"+developerId+"' where packageName = '"+packageName_1+"';";
				statement.executeUpdate(insertQuery);
			}

		}
		/*String developerId=fetchDeveloperId(pageSource);
		if(true)
		{
			String insertQuery="Insert ignore into appsFinanceCategory values('"+packageName+"',1,'"+developerId+"');";
			//String setFlag="Update appsFinanceCategory set Finance=1, DeveloperId ='"+developerId+"' where packageName = '"+packageName_1+"';";
			statement.executeUpdate(insertQuery);
		}
*/
	}





	private static HashSet fetchPackageName(String pageSource, String filePathDeveloper) throws IOException {
		// TODO Auto-generated method stub
		//Files.write(Paths.get(filePathDeveloper), (pageSource+"\n").getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);

		HashSet hs = new HashSet();
		String devIdpattern="/store/apps/details";
		if(pageSource.contains(devIdpattern))
			System.out.println("Fine");
		else
			System.out.println("ok this is interesting, we are unable to see the app id");
		//System.out.println(pageSource);
		String packageNames[]=pageSource.split(devIdpattern);
		for(int i=0;i<packageNames.length;i++)
		{
			int index=packageNames[i].indexOf("?id=");
			if(index!=-1)
			{
				int j=0;
				for( j=4;packageNames[i].charAt(j)!='"';j++)
				{
					
				}
				String packageName=packageNames[i].substring(4, j);
				hs.add(packageName);
			}
		}
		//System.out.println(packageNames[1]);
		//System.out.println(packageNames[2]);
		//System.out.println(packageNames[4]);
		System.out.println("Hashset length  is :"+hs.size());
		return hs;
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