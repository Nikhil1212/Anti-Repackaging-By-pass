package signatureAddition.pastHardwork;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;

public class librariesPrint {

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		HashSet <String> libraryList = new HashSet<String>();
		String listOfLibrariesPath="/home/nikhil/Documents/apps/listOfLibraries.txt";
		File file=new File(listOfLibrariesPath);
	      Scanner scanner=new Scanner(file);
	      while(scanner.hasNext())
	      {
	    	  libraryList.add(scanner.next());
	      }
	     System.out.println(libraryList);
	}

}
