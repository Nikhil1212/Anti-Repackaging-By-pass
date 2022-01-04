package signatureAddition.pastHardwork;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.Scanner;

import finalRun.isDumpGenerated;

public class BasicGet {





	private static final String USER_AGENT = "Mozilla/5.0";

	
	public static void main(String[] args) throws IOException {
		String pathToApk="/home/nikhil/Documents/apps/FinalDataset.txt";
		File file=new File(pathToApk);
		Scanner scanner=new Scanner(file);
		String packageName="";
		
	//	lockUnlockPhone("1995", deviceId[2]);
		
	//	lockUnlockPhone("1234", deviceId[2]);
		
		int count1=0;
		
		while(scanner.hasNext())
		{
			packageName=scanner.next();
			//packageName="www.paymonk.com.payzonmerchant";
			try
			{
				sendGET(packageName);
			}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			//break;
		}
		
		System.out.println("GET DONE");
		
	}

	private static void sendGET(String packageName) throws IOException, SQLException {
		
		String url="https://play.google.com/store/apps/details?id="+packageName;
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", USER_AGENT);
		int responseCode = con.getResponseCode();
		if(responseCode==404)
			isDumpGenerated.updateTable(packageName, "AppsRemovedFromPlayStore");
		System.out.println("GET Response Code :: " + responseCode);
		if (responseCode != HttpURLConnection.HTTP_OK)  {
			System.out.println("GET request not worked");
		}

	}



}
