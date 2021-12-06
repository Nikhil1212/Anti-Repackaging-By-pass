package signatureAddition.pastHardwork;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import signatureAddition.StartingPoint;

public class ResignApks {

	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		String filePath="/home/nikhil/Documents/apps/cradlewise/com.cradlewise.nini.app-ifRVrBrdtpvR0oLT7nnr6w==/apkPath.txt";
		File file=new File(filePath);
		Scanner scanner=new Scanner(file);
		int ctr=1;
		while(scanner.hasNext())
		{
			String apkPath=scanner.next();
			StartingPoint.signApk("packageName"+ctr, apkPath);
			ctr++;
		}
	}

}
