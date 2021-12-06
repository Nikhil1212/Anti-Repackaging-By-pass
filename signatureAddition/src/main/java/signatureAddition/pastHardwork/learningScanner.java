package signatureAddition.pastHardwork;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class learningScanner {

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		String FilePath="/home/nikhil/Documents/apks/pathToApps_3.txt";
		File file=new File(FilePath);
		Scanner scanner=new Scanner(file);
		while(scanner.hasNext())
		{
			System.out.println(scanner.next());
		}
	}

}
