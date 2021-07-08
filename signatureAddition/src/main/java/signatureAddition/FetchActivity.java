/**
 * It fetches the different names of the Activity by analysing the logs of the app,
 * given in the filePath.
 */
package signatureAddition;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Scanner;

public class FetchActivity {

/*	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		System.out.println("Hello Nikhil");
//		System.out.println("Enter the file path: ");
		//Scanner scanner=new Scanner(System.in);
		String filePath="/home/nikhil/Documents/apps/logcatOutput/original_com.bankofbaroda.upi.txt";//scanner.next();
		String packageName="com.bankofbaroda.upi";
		fetchActivity(filePath,packageName);
	}
*/
	public static HashSet<String> fetchActivity(String filePath, String packageName) throws FileNotFoundException {
		 String pattern=packageName+"/";
		 File file=new File(filePath);    //creates a new file instance  
		 FileReader fr=new FileReader(file);   //reads the file  
		 BufferedReader br=new BufferedReader(fr);  //creates a buffering character input stream  
		 String line;  
		 HashSet<String> hs=new HashSet<String>();
		 try {
			while((line=br.readLine())!=null)  
			 {  
				if(!(line.contains("ActivityTaskManager") || line.contains("ActivityManager")))
					continue;
				 int index=line.indexOf(pattern);
				 if(index!=-1)
				 {
					 int startingPoint=index+pattern.length();
					 int i=startingPoint;
					  while(!(i==line.length()|| line.charAt(i)=='}' || line.charAt(i)==' ' || line.charAt(i)==':' || line.charAt(i)=='-' ))
					 {
						 i++;
					 }
					 if(i==line.length())
						 continue;
					 if(line.charAt(startingPoint)=='.')
					 {
						 String finalContents=packageName+"/"+packageName+line.substring(startingPoint,i);
						 if( finalContents.contains("files")||finalContents.contains("shared_prefs")||finalContents.contains("u0"))
							 continue;
						 if(checkPresenceOfNumber(finalContents))
						 hs.add(finalContents);
					 }
					 else
					 {
						 
						 String finalContents=line.substring(index,i);
						 if( finalContents.contains("files")||finalContents.contains("shared_prefs")||finalContents.contains("u0"))
							 continue;
						 if(checkPresenceOfNumber(finalContents))
						 hs.add(finalContents);
					 }
					 
				 }
			 }
		//	System.out.println(hs);
			return hs;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hs;
		 
		 
	}

private static boolean checkPresenceOfNumber(String finalContents) {
	// TODO Auto-generated method stub
	System.out.println("Inside the checkPresence of Number");
	 int indexSlash=finalContents.indexOf('/');
	 if(finalContents.charAt(indexSlash+1)>=48 && finalContents.charAt(indexSlash+1)<=57)
		 return false;
	
	return true;
}

}
