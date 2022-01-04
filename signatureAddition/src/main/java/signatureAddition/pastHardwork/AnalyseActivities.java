package signatureAddition.pastHardwork;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;

import Logs.FetchActivity;

public class AnalyseActivities {

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		String outputFilePath="/home/nikhil/Documents/apps/LogsIssue.txt";
		
		File file=new File(outputFilePath);
		Scanner  scanner=new Scanner(file);
		while(scanner.hasNext())
		{
			String packageName=scanner.next();
			String logPathOriginal="/home/nikhil/Documents/apps/logs/"+packageName+"/original.txt";

			String logPathRepackaged="/home/nikhil/Documents/apps/logs/"+packageName+"/rooted.txt";
			
			
			HashSet<String > hashSet1=	FetchActivity.fetchActivity( logPathOriginal, packageName);
			iterateHashSet(hashSet1);
		//	System.out.println(hashSet);
			HashSet<String > hashSet2=	FetchActivity.fetchActivity(logPathRepackaged,packageName);
			iterateHashSet(hashSet2);
			
			System.out.println(hashSet1);
			
			System.out.println(hashSet2);
			if (hashSet1.containsAll(hashSet2) && hashSet2.containsAll(hashSet1))
			{
				System.out.println("Oh No different activity names has been found on the original and repackaged app");
			}
			
			
			System.out.println("\n***\n");
		}
	}

	public static void iterateHashSet(HashSet<String> hashSet) {
		// TODO Auto-generated method stub
		Iterator<String> iterator=hashSet.iterator();
		String pattern="org.chromium.content.app.SandboxedProcessService0";
		while(iterator.hasNext())
		{
			String val=iterator.next();
			if(val.contains(pattern))
			{
				hashSet.remove(val);
				return;
			}
		}
	}

}
