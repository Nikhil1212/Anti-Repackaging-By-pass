package Logs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

public class ActivityCountAnalysis {

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		String filePath="/home/nikhil/Documents/apps/logsRemovedDuplicates/original_com.mbanking.aprb.aprb.txt";
		String packageName="com.mbanking.aprb.aprb";
		System.out.println(fetchActivity(filePath, packageName));

	}
	public static HashSet<String> fetchActivity(String filePath, String packageName) throws FileNotFoundException {
		 String pattern=packageName+"/";
		// BufferedReader bufferedReader=new BufferedReader((Reader) Paths.get(filePath));
		 File file=new File(filePath);    //creates a new file instance  
		 FileReader fr=new FileReader(file);   //reads the file  
		 BufferedReader br=new BufferedReader(fr);  //creates a buffering character input stream  
		 String line;  
		 HashSet<String> hs=new HashSet<String>();
		 try {
			while((line=br.readLine())!=null)  
			 {  
				 int index=line.indexOf(pattern);
				 if(index!=-1)
				 {
					 int startingPoint=index+pattern.length();
					 int i=startingPoint;
					 if(line.contains("u0")) //because it does not contains the activity name, I think its just uid.
						 continue;
					 while(!(i==line.length()|| line.charAt(i)=='}' || line.charAt(i)==' ' || line.charAt(i)==':' || line.charAt(i)=='-'))
					 {
						 i++;
					 }
					 if(i==line.length())
						 continue;
					 if(line.charAt(startingPoint)=='.')
					 {
						 String finalContents=packageName+"/"+packageName+line.substring(startingPoint,i);
						 System.out.println("Tried this "+finalContents);
						 hs.add(finalContents);
					 }
					 else
					 hs.add(line.substring(index,i));
					 
				 }
			 }
			System.out.println(hs);
			return hs;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hs;
		 
		 
	}

}
