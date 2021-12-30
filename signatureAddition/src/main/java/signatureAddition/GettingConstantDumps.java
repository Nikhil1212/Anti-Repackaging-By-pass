package signatureAddition;

import java.io.IOException;
import java.util.HashSet;

import parseXml.Main;
import parseXml.TextAnalysis;
/**
 * For a given packageName and on the given filePath, we are taking the dump and storing it on the given filePath. Since the uiautomator does not give the same dump for the same screen, we are taking the dump
 * The purpose of this class is to get as much resource id as possible. Since python wrapper does not capture the entire resource id, we are generating the dump atmost 10 times or untill the time, dump is constant.
 * @author nikhil
 *
 */
public class GettingConstantDumps {
/**
 * 
 * @param args
 * @throws IOException
 * @throws InterruptedException
 */
	public static void main(String[] args) throws IOException, InterruptedException {
	
		String packageName="com.icicibank.iMcanada";
		String filePath="/home/nikhil/Documents/apps/hello.txt";
		
		String deviceId="0248f4221b4ca0ee";
		
		main(packageName,filePath,deviceId);
		
	}

	public static void main(String packageName, String filePath, String deviceId) {
	// TODO Auto-generated method stub
		HashSet hashSetSecondArr[]= {new HashSet<String>(),new HashSet<String>(),new HashSet<String>(),new HashSet<String>()};
		
		HashSet hashSetFirstArr[]= {new HashSet<String>(),new HashSet<String>(),new HashSet<String>(),new HashSet<String>()};
		
		try {
			
			
			RootEmulation.dumpTheAppScreen(packageName, filePath, deviceId);
			
			hashSetFirstArr[0]=Main.analysePatternInXml(packageName, filePath, "resource-id");
			hashSetFirstArr[1]=TextAnalysis.nonEmptyText(packageName, filePath, "text");
			hashSetFirstArr[2]=TextAnalysis.nonEmptyContentDesc(packageName, filePath, "content-desc");
			hashSetFirstArr[3]=Main.analysePatternInXml(packageName, filePath, "class");
	
			
		} catch (Exception e) {

			e.printStackTrace();
		
		}
		for(int j=2;j<=10;j++)
		{
			
			//function that will generate the dump again and storing it to a given file path
			
			try {
				
				RootEmulation.dumpTheAppScreen(packageName, filePath, deviceId);
				
				hashSetSecondArr[0]=Main.analysePatternInXml(packageName, filePath, "resource-id");
				hashSetSecondArr[1]=TextAnalysis.nonEmptyText(packageName, filePath, "text");
				hashSetSecondArr[2]=TextAnalysis.nonEmptyContentDesc(packageName, filePath, "content-desc");
				hashSetSecondArr[3]=Main.analysePatternInXml(packageName, filePath, "class");
			
				boolean result = isHashSetsEqual(hashSetFirstArr,hashSetSecondArr);
				if(result)
				{
					System.out.println(j);
					break;
				}
			
			copy(hashSetFirstArr,hashSetSecondArr);
				
			} catch (Exception e) {

				e.printStackTrace();
			}
			
		}
}

	public static boolean isHashSetsEqual(HashSet[] hashSetFirstArr, HashSet[] hashSetSecondArr) {
		// TODO Auto-generated method stub
		for(int i=0;i<hashSetFirstArr.length;i++)
		{
			if(hashSetFirstArr[i].size()!=hashSetSecondArr[i].size())
				return false;
		}
		return true;
	}

	public static void copy(HashSet[] hashSetFirstArr, HashSet[] hashSetSecondArr) {
		// TODO Auto-generated method stub
		for(int i=0;i<hashSetFirstArr.length;i++)
		{
			hashSetFirstArr[i]=hashSetSecondArr[i];
		}
		
	}

}
