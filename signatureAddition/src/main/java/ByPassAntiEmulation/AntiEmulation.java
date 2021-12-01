package ByPassAntiEmulation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

/**
 * By taking the file path as an input, it will overwrite the build properties with the values of the Google Pixel.
 * @author nikhil
 *
 */
public class AntiEmulation {
	
	public static HashMap<String, String> hashMap;
	public static void initializeHashMap()
	{
		hashMap=new HashMap<String, String>();
		hashMap.put("FINGERPRINT",   "google/sunfish/sunfish:11/RQ3A.210605.005/7349499:user/release-keys");
		hashMap.put("MODEL", "Pixel 4a");//,"android-build");
		hashMap.put("MANUFACTURER", "Google");//,"android-build");
		hashMap.put("PRODUCT","sunfish");
		hashMap.put("ID","RQ3A.210605.005");
		hashMap.put("SERIAL","unknown");
		hashMap.put("TAGS","release-keys");
		hashMap.put("USER","android-build");
		hashMap.put("HARDWARE","sunfish");
		hashMap.put("BOARD","sunfish");
		hashMap.put("BRAND","google");
		hashMap.put("DEVICE","sunfish");
		hashMap.put("HOST","vpea3.mtv.corp.google.com");

		hashMap.put("RADIO","M8994F-2.6.22.0.56");
	}
	public  static void writeToFile(String ans, String destiantionPath) throws IOException {
		
		FileWriter fileWriter=new FileWriter(destiantionPath);
		fileWriter.write(ans);
		fileWriter.close();
	}
	public static void codeInjection(String filePath) throws IOException {

		initializeHashMap();
		String pattern="Landroid/os/Build;->";
		
		/**
		 * For various file in which build related info is fetched, this function will overwrite the build info with the values of the Google Pixel
		 */
		
		System.out.println("File name is :"+filePath);
		
			String outputContents="";
			File file=new File(filePath);
			Scanner scanner=new Scanner(file);
			while(scanner.hasNext())
			{
				String line=scanner.nextLine();
				outputContents=outputContents+line+"\n";
				if(line.contains(pattern) && (line.contains("getSerial") || line.contains("getRadioVersion")))
				{
					//fetch register number
					line=scanner.nextLine();
					if(line.length()>1)
					{
						String registerNumber=line.substring(line.lastIndexOf(" ")+1);
						String key="SERIAL";
						if(line.contains("getRadioVersion"))
							key="RADIO";
						String codeInject="\nconst-string "+registerNumber+", "+"\""+hashMap.get(key)+"\"";
						System.out.println(codeInject);
						outputContents=outputContents+codeInject+line;
					}
					else
					{
						outputContents=outputContents+line;
						line=scanner.nextLine();
						String key="SERIAL";
						if(line.contains("getRadioVersion"))
							key="RADIO";
						String registerNumber=line.substring(line.lastIndexOf(" ")+1);
						String codeInject="\nconst-string "+registerNumber+", "+"\""+hashMap.get(key)+"\"";
						System.out.println(codeInject);
						outputContents=outputContents+codeInject+line;
					}
				}
				else if(line.contains(pattern))
				{
					outputContents=outputContents+codeInjection(line, pattern)+"\n";
				}
			}
			scanner.close();
			
			writeToFile(outputContents, filePath);
			
			
		}
		
	
	public static String codeInjection(String currentLine, String patternBuild) {
		// TODO Auto-generated method stub
	String string="\nconst-string"+ fetchRegisterNumberWithSpace(currentLine, patternBuild)+",\""+hashMap.get(fetchKey(currentLine,patternBuild))+"\"";
	System.out.println(string);
	return string;
		
	}
	private static String fetchKey(String currentLine, String pattern) {
		
		if(currentLine.contains("getSerial"))
			return "SERIAL";
		
		int start=currentLine.indexOf(pattern);
		start=start+pattern.length();
		int end=currentLine.indexOf(":");
		if(end==-1)
			return "Alternative to null";
		String ans=currentLine.substring(start,end);
		System.out.println("Key is :"+ans);
		return ans;
	}

	public static String fetchRegisterNumberWithSpace(String line, String pattern) {
		String temp=line.substring(0,line.indexOf(','));
		String temp1=temp.substring(temp.lastIndexOf(" "));
		System.out.println(temp1);
		if(temp1==null)
			return "AlternativeToNull";
		return temp1;
		
	}

}
