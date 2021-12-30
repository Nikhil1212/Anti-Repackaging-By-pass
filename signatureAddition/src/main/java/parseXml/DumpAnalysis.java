package parseXml;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Scanner;

/**
 * This class with the help of other method defined in another class, compares the dump of two files executed in two different environments. The parameters considered is resource-id, class. 
 * @author nikhil
 *
 */
public class DumpAnalysis {

	public static void main(String[] args) throws IOException {

		String filePath="/home/nikhil/Documents/apps/dataset/packageNames_2.txt";
		File file=new File(filePath);
		Scanner scanner=new Scanner(file);
		
		while(scanner.hasNext())
		{
			int flag=0;
			String packageName=scanner.next();
			String directoryPath="/home/nikhil/Documents/apps/uiautomator/rootEmulator/"+packageName;
			
			String patterns[]= {"resource-id","class"};//,"text"};
			for(int i=0;i<patterns.length;i++)
			{
				HashSet<String> hashSet1= Main.analysePatternInXml(packageName, directoryPath+"/real_python.xml", patterns[i]);
				HashSet<String> hashSet;
				
				hashSet= Main.analysePatternInXml(packageName, directoryPath+"/rooted_python.xml", patterns[i]);
				System.out.println(hashSet1);
				
				System.out.println(hashSet);
				if(!hashSet.equals(hashSet1))
				{
				try {
					Main.updateTable(packageName,'Y',patterns[i],"RootDetection_Automation");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				flag=1;
				break;
				}
			
				
			}
			if(flag==0)
			{
				try {
					Main.updateTable(packageName, 'N', "No difference in resource-id and distinct classes", "RootDetection_Automation");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			

		}
					
	}

}
