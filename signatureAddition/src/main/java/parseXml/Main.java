package parseXml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;

import signatureAddition.AppLaunchAndDump;
import signatureAddition.DataBaseConnect;
import signatureAddition.pastHardwork.GettingConstantDumps;

public class Main {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		String filePath="/home/nikhil/Documents/apps/dataset/packageNames_2.txt";
		File file=new File(filePath);
		
		//String outputArr[]= {};
		String outputResources="";
		
		String outputClasses="";
		String outputText="";
		String outputArr[]= {outputResources,outputClasses};//,outputText};
		String outputResourcesDisjoint="";
		String outputClassesDisjoint="";
		String outputArrDisjoint[]= {outputResourcesDisjoint,outputClassesDisjoint};//,outputText};
		
		//String outputResourcesDisjointPath="/home/nikhil/Documents/apps/disJointResources.txt";
		//String outputClassesDisjointPath="/home/nikhil/Documents/apps/disJointClasses.txt";
		
		//String outputDisjointFilePaths[]= {outputResourcesDisjointPath,outputClassesDisjointPath};//,outputTextPath};
		
		String outputClassPath="/home/nikhil/Documents/apps/classDumpAnalysisAntiTampering.txt";
	//	String outputTextPath="/home/nikhil/Documents/apps/TextDumpAnalysis.txt";
		String outputResourcesPath="/home/nikhil/Documents/apps/ResourceIdAnalysisAntiTampering.txt";
		
		
		String outputClassPathEmulator="/home/nikhil/Documents/apps/classDumpAnalysisEmulator.txt";
		//String outputTextPathEmulator="/home/nikhil/Documents/apps/TextDumpAnalysisEmulator.txt";
		String outputResourcesPathEmulator="/home/nikhil/Documents/apps/ResourceIdAnalysisEmulator.txt";
		
		String outputFilePaths[]= {outputResourcesPath,outputClassPath};//,outputTextPath};
		Scanner scanner=new Scanner(file);
		while(scanner.hasNext())
		{
			String packageName=scanner.next();
			try
			{

				String filePathRooted="/home/nikhil/Documents/apps/uiautomator/rootEmulator/"+packageName+"/rooted_python.xml";
				String filePathReal="/home/nikhil/Documents/apps/uiautomator/rootEmulator/"+packageName+"/real_BuiltIn.xml";
				
				String filePathPSApp="/home/nikhil/Documents/apps/uiautomator/rootEmulator/"+packageName+"/ps_BuiltIn.xml";
				
			//	GettingConstantDumps.main(packageName, filePathPSApp, AppLaunchAndDump.deviceId[0]);

				String filePathSideLoad="/home/nikhil/Documents/apps/uiautomator/rootEmulator/"+packageName+"/sideLoad_BuiltIn.xml";

				String filePathRepackaged="/home/nikhil/Documents/apps/uiautomator/rootEmulator/"+packageName+"/repackaged_BuiltIn.xml";
				
				String filePathReal2="/home/nikhil/Documents/apps/uiautomator/rootEmulator/"+packageName+"/real_python2.xml";
				String filePathEmulator="/home/nikhil/Documents/test/rootEmulator/"+packageName+"/python.xml";
				
				
				boolean result=isDumpDifferent(filePathPSApp,AppLaunchAndDump.deviceIdSynonym[0],filePathSideLoad,AppLaunchAndDump.deviceIdSynonym[4], packageName,"AntiTampering_Automation",outputArr);
				
				
				//boolean resultIsEmulation=isDumpDifferent(filePathReal,filePathEmulator, packageName,"EmulatorDetection_Automation");
				
				System.out.println(packageName+" : "+result);
				
			}
			catch (Exception e) {
				
				e.printStackTrace();
			}
		
		}
		
		for(int i=0;i<2;i++)
		{
			FileWriter fileWriter=new FileWriter(outputFilePaths[i]);
			fileWriter.write(outputArr[i]);
			fileWriter.close();
			
		}
		
		
		

	}

	public static boolean isDumpDifferent(String filePathDev1, String deviceIdSynonym1, String filePathDev2, String deviceIdSynonym2, String packageName, String tableName, String[] outputArr) throws SQLException, IOException {
		// TODO Auto-generated method stub
		
		String patterns[]= {"resource-id","class"};//,"text"};

		//patterns[0]="resource-id=\"";
		int flag=0;
		for(int i=0;i<patterns.length;i++)
		{
			
			HashSet<String>	hashSetDev1=analysePatternInXml(packageName, filePathDev1, patterns[i]);
			
			HashSet<String>	hashSetDev2=analysePatternInXml(packageName, filePathDev2, patterns[i]);
			
			filterHashSet(hashSetDev1);
			filterHashSet(hashSetDev2);
			
			outputArr[i]=outputArr[i]+"\n"+packageName +"\n\n"+deviceIdSynonym1 + "\n"+hashSetDev1+"\n"+deviceIdSynonym2+"\n"+hashSetDev2+"\n******";
		
	//		outputArrDisjoint[i]=outputArrDisjoint[i]+"\n"+packageName+"\n";
			//let's print disjoint keys
			
			//outputArrDisjoint[i]=outputArrDisjoint[i]+	printDisjointKeys(hashSetDev1,hashSetDev2,outputArrDisjoint[i])+printDisjointKeys(hashSetDev2,hashSetDev1,outputArrDisjoint[i])+"\n\n";
			
			
			
		
			 
		/*	temp1=analysePatternInXml(packageName, filePathDev1, patterns[i]);
			 
			temp2=analysePatternInXml(packageName, filePathReal2, patterns[i]);
			*/
			
			 if(!(hashSetDev2.equals(hashSetDev1)))
			 {
				 System.out.println("Not equal");
				 updateTable(packageName, 'Y', "Differnce in :"+patterns[i],tableName);
				 flag=1;
				 return true; 
				 
			 }
			 
		}
		updateTable(packageName, 'N', "No difference in the resource-id, class", tableName);

		return false;
	}
	
	private static void filterHashSet(HashSet<String> hashSet) {
		// TODO Auto-generated method stub
		if(hashSet.contains("android.view.View") && hashSet.contains("android.view.ViewGroup"))
			hashSet.remove("android.view.View");
	
	}

	private static String printDisjointKeys(HashSet<String> hashSetDev1, HashSet<String> hashSetDev2, String outputDisjoint) {
		// TODO Auto-generated method stub
		Iterator<String> iterator=hashSetDev1.iterator();
		while(iterator.hasNext())
		{
			String key=iterator.next();
			if(!hashSetDev2.contains(key))
			{
				outputDisjoint=outputDisjoint+"\n"+key;
				System.out.println(key);
			}
				
		}
		return outputDisjoint;
	}

	/**
	 * This functions assumes following things:
	 * the XML file follows a pattern like package will come before class, text, resource id.
	 * @param packageName
	 * @param filePathPackagesXML
	 * @param pattern
	 * @return
	 * @throws IOException
	 */

	public static HashSet<String>  analysePatternInXml(String packageName, String filePathPackagesXML, String pattern) throws IOException {
		
		pattern=pattern+"=\"";
		HashSet<String>  hashSet=new HashSet<String>();
		System.out.println("Pattern value is :"+pattern);
		
		String fileContents = new String(Files.readAllBytes(Paths.get(filePathPackagesXML)), StandardCharsets.UTF_8);
		
		while(fileContents.indexOf(pattern)!=-1)
		{
			int index=fileContents.indexOf(pattern);
			String temp=fileContents.substring(index+pattern.length());
			int end=temp.indexOf("\"");
			String value=temp.substring(0,end);
			//		System.out.println("pattern :"+ pattern + "value :"+value);
			fileContents=temp.substring(end+1);
			
			String patternPackage="package=\""+packageName+"\"";
			String onlyPackageEqual="package=\"";
			
			
			/**
			 * Below code we got the idea while cycling. We want to make sure that the resource-id, class is fetched only for the node 
			 * whose package attribute is set to the package name of the app.
			 */
			int index1=fileContents.indexOf(patternPackage);
			int index2=fileContents.indexOf(onlyPackageEqual);
			
			
	
			if(index1==index2 && index1!=-1 && value.length()!=0 && (! (value.contains("navigationBarBackground") || value.contains("statusBarBackground") )))
				hashSet.add(value);
			
			//update the fileContents
		}
		return hashSet;

	}
	public static void updateTable(String packageName, char c, String remarks, String tableName) throws SQLException {
		// TODO Auto-generated method stub
		
		String checkQuery="Select packagename from "+tableName+" where packageName ='"+packageName+"';";
		System.out.println(checkQuery);
		Statement statement1=DataBaseConnect.initialization();
		ResultSet resultSet=statement1.executeQuery(checkQuery);
		int flag=0;
		String output="";
		while(resultSet.next())
		{
			flag=1;
			output=output+ resultSet.getString(1)+"\n";
		}
		if(flag==0)
		{
			String query="Insert into "+tableName+" values ('"+packageName+"','"+c+"','"+remarks+"');";
			System.out.println(query);

			Statement statement=DataBaseConnect.initialization();
			statement.executeUpdate(query);
		}
		else
		{
			String query="Update "+tableName+" set IsCheckPresent ='"+c+"' , remarks ='"+remarks+"' where packageName='"+packageName+"';";
			System.out.println(query);

			Statement statement=DataBaseConnect.initialization();
			statement.executeUpdate(query);
		}


		
	}
	

}
