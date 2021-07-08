package signatureAddition;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class ResignApks {

	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		String filePath="/home/nikhil/Downloads/googleplay-api-master/com.icicibank.fastag-WegEvBG1dfVI-BfqNCUa4g==/repackagedApk/filePath.txt";
		File file=new File(filePath);
		Scanner scanner=new Scanner(file);
		while(scanner.hasNext())
		{
			String apkPath=scanner.next();
			StartingPoint.signApk("packageName", apkPath);
		}
	}

}
