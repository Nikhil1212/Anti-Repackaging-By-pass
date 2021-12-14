package overwriteStrings;

/**
 * This class attempts to by-pass the root detection measures 
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import signatureAddition.FileNamesForSignatureAddition;

public class StartingPoint_RootDetection {
	public static HashSet<String> hashSetAppPackageName;
	public static HashSet<String> hashSetSuPossiblePath;
	public static HashSet<String>	hashSetHarmfulFiles;
	public static Set<String> universalHashSet[];
	public static HashSet[] hs = { new HashSet<String>(), 
            new HashSet<String>(), 
            new HashSet<String>(),
            new HashSet<String>(),
            new HashSet<String>()	
	};
	
	
	/*public static void hello()
	{
		System.out.println("Can u see me?");

		Set<String> universalHashSet[] = new HashSet[10];
		for (int i = 0; i < universalHashSet.length; i++) {
		 universalHashSet[i] = new HashSet<String>();
		}
	}*/
	
	
	public static void hashSetInitialize()
	{
		//hello();
		hashSetAppPackageName=new HashSet<String>();
		hashSetAppPackageName.add("\"com.koushikdutta.rommanager\"");
		hashSetAppPackageName.add("\"com.koushikdutta.rommanager.license\"");
		hashSetAppPackageName.add("\"com.dimonvideo.luckypatcher\"");
		hashSetAppPackageName.add("\"com.chelpus.lackypatch\"");
		hashSetAppPackageName.add("\"com.ramdroid.appquarantine\"");	
		hashSetAppPackageName.add("\"com.ramdroid.appquarantinepro\"");
		hashSetAppPackageName.add("\"com.android.vending.billing.InAppBillingService.COIN\"");
		hashSetAppPackageName.add("\"com.chelpus.luckypatcher\"");
		
	//	hashSetAppPackageName.add("frida-server");
		//hashSetAppPackageName.add("frida");
		//hashSetAppPackageName.add("fridaserver");
		hashSetAppPackageName.add("mobi.acpm.inspeckage");
		hashSetAppPackageName.add("com.sudocode.sudohide");
		hashSetAppPackageName.add("com.noshufou.android.su");
		hashSetAppPackageName.add("com.koushikdutta.superuser");
		hashSetAppPackageName.add("com.zachspong.temprootremovejb");
		hashSetAppPackageName.add("mobi.acpm.sslunpinning");
		hashSetAppPackageName.add("com.ramdroid.appquarantine");
		hashSetAppPackageName.add("com.topjohnwu.magisk");
		hashSetAppPackageName.add("eu.chainfire.supersu");
            
		hashSetAppPackageName.add("\"com.thirdparty.superuser\"");
		hashSetAppPackageName.add("\"eu.chainfire.supersu\"");
		hashSetAppPackageName.add("\"com.noshufou.android.su\"");
		hashSetAppPackageName.add("\"com.koushikdutta.superuser\"");
		hashSetAppPackageName.add("\"com.zachspong.temprootremovejb\"");
		hashSetAppPackageName.add("\"com.ramdroid.appquarantine\"");
		hashSetAppPackageName.add("\"com.topjohnwu.magisk\"");
		hashSetAppPackageName.add("\"com.yellowes.su\"");
		hashSetAppPackageName.add("\"com.noshufou.android.su.elite\"");
		
		
		hashSetAppPackageName.add("\"com.devadvance.rootcloak\"");
		hashSetAppPackageName.add("\"com.devadvance.rootcloakplus\"");
		hashSetAppPackageName.add("\"de.robv.android.xposed.installer\"");
		
		hashSetAppPackageName.add("\"com.formyhm.hideroot\"");
		hashSetAppPackageName.add("\"com.formyhm.hiderootPremium\"");
		hashSetAppPackageName.add("\"com.amphoras.hidemyrootadfree\"");
		
		hashSetAppPackageName.add("\"com.amphoras.hidemyroot\"");
		hashSetAppPackageName.add("\"com.saurik.substrate\"");
		
		
		hashSetSuPossiblePath=new HashSet<String>();
		hashSetSuPossiblePath.add("\"/data/local/bin/su\"");
		hashSetSuPossiblePath.add("\"/sbin/su\"");
		hashSetSuPossiblePath.add("\"/system/bin/su\"");
		hashSetSuPossiblePath.add("\"/system/bin/failsafe/su\"");
		hashSetSuPossiblePath.add("\"/system/xbin/su\"");
		
		hashSetSuPossiblePath.add("\"/system/xbin/busybox\"");
		hashSetSuPossiblePath.add("\"/system/sd/xbin/su\"");
		hashSetSuPossiblePath.add("\"/data/local/su\"");
		hashSetSuPossiblePath.add("\"/data/local/xbin/su\"");
		hashSetSuPossiblePath.add("\"/data/local/bin/su\"");

		hashSetSuPossiblePath.add("\"/data/local/\"");
		hashSetSuPossiblePath.add("\"/data/local/bin/\"");
		hashSetSuPossiblePath.add("\"/data/local/xbin/\"");
		hashSetSuPossiblePath.add("\\\"/sbin/\\\"");
		hashSetSuPossiblePath.add("\\\"/su/bin/\\\"");
		hashSetSuPossiblePath.add("\\\"/data\\\"");
		hashSetSuPossiblePath.add("\\\"/dev\\\"");
		hashSetSuPossiblePath.add("\\\"/cache\\\"");
		hashSetSuPossiblePath.add("\"/system/usr/we-need-root/\"");
		hashSetSuPossiblePath.add("\"/system/sd/xbin/\"");
		hashSetSuPossiblePath.add("\"/system/bin/.ext/\"");
		hashSetSuPossiblePath.add("\"/system/xbin/\"");
		hashSetSuPossiblePath.add("\"/system/bin/\"");

		hashSetSuPossiblePath.add("\"/system/bin/failsafe/\"");
//		"/system/bin/.ext/", "/system/bin/failsafe/", "/system/sd/xbin/", "/system/usr/we-need-root/", "/system/xbin/", "/cache", "/data", "/dev"}
		

		hashSetSuPossiblePath.add("\"/data/local/bin/su\"");
		hashSetHarmfulFiles=new HashSet<String>();
		hashSetHarmfulFiles.add("/system/app/Superuser.apk");
		hashSetHarmfulFiles.add("/system/etc/init.d/99SuperSUDaemon");
		hashSetHarmfulFiles.add("/dev/com.koushikdutta.superuser.daemon");
		hashSetHarmfulFiles.add("/system/xbin/daemonsu");
		//	System.out.println(universalHashSet.length);
		
	
	}
	public static void main(String pathToDisAssembleCode) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
	//	String filePath="/home/nikhil/Documents/apps/dataset/com.sbi.upi/base/smali_classes4/com/scottyab/rootbeer/Const.smali";
		
		HashSet<String> hashSetBinaries=new HashSet<String>();
		String suPattern="\"\\\"su\\\"\"";
		hashSetBinaries.add("\"\\\"su\\\"\"");  //because we want the pattern to be "\"su\""
		hashSetBinaries.add("\"magisk\"");
		hashSetBinaries.add("\"daemonsu\"");
		hashSetBinaries.add("\"busybox\"");
		hashSetBinaries.add("\"\\\"frida\\\"\"");

		hashSetBinaries.add("\"\\\"fridaserver\\\"\"");

		hashSetBinaries.add("\"\\\"frida-server\\\"\"");

		hashSetInitialize();


		HashSet<String> hashSetSensitiveDirectory=new HashSet<String>();
		hashSetSensitiveDirectory.add("\\\"/system\\\"");
		hashSetSensitiveDirectory.add("\\\"/system/bin\\\"");
		hashSetSensitiveDirectory.add("\\\"/system/sbin\\\"");
		hashSetSensitiveDirectory.add("\\\"/system/xbin\\\"");
		hashSetSensitiveDirectory.add("\\\"/vendor/bin\\\"");
		hashSetSensitiveDirectory.add("\\\"/sbin\\\"");
		hashSetSensitiveDirectory.add("\\\"/etc\\\"");
		
		hs[0]=hashSetAppPackageName;
		hs[1]=hashSetBinaries;
		hs[2]=hashSetSuPossiblePath;
		hs[3]=hashSetHarmfulFiles;
		hs[4]=hashSetSensitiveDirectory;
//		{"/system", "/system/bin", "/system/sbin", "/system/xbin", "/vendor/bin", "/sbin", "/etc"};

		//String pathToDisAssembleCode="/home/nikhil/Documents/apps/dataset/com.sbi.upi/base/";
		

		for(int i=0;i<=4;i++)
		{
			Iterator<String> iterator=hs[i].iterator();
			while(iterator.hasNext())
			{
				String pattern=iterator.next();
				System.out.println(pattern);
					String grepCommand="grep -r -l "+pattern+" "+ pathToDisAssembleCode;
					//System.out.println(grepCommand);
					Process process=Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", grepCommand});///home/nikhil/Documents/apps/com.mbanking.aprb.aprb"});
					
					process.waitFor();
					BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(process.getInputStream()));
					String filePath1=bufferedReader.readLine();
				//	List<String> filePathsList=new ArrayList<String>();
					if(pattern==suPattern)
						pattern="\"su\"";
					else if (pattern == "\\\"/system\\\"")
					{
						pattern = "\"/system\"";
					}
					else if (pattern == "\\\"/system/bin\\\"")
					{
						pattern = "\"/system/bin\"";
					}
					else if (pattern == "\\\"/system/sbin\\\"")
					{
						pattern = "\"/system/sbin\"";
					}
					else if (pattern == "\\\"/system/xbin\\\"")
					{
						pattern = "\"/system/xbin\"";
					}
					else if (pattern == "\\\"/vendor/bin\\\"")
					{
						pattern = "\"/vendor/bin\"";
					}
					else if (pattern == "\\\"/sbin/\\\"")
					{
						pattern = "\"/sbin/\"";
						
					}
					else if (pattern == "\\\"/etc\\\"")
					{
						pattern = "\"/etc\"";
					}

					else if (pattern == "\\\"/data\\\"")
					{
						pattern = "\"/data\"";
					}
					else if (pattern == "\\\"/cache\\\"")
					{
						pattern = "\"/cache\"";
					}
					else if (pattern == "\\\"/dev\\\"")
					{
						pattern = "\"/dev\"";
					}
					
					else if (pattern == "\\\"/su/bin/\\\"")
					{
						pattern = "\"/su/bin/\"";
					}
					
					else if (pattern == "\\\"/sbin\\\"")
					{
						pattern = "\"/sbin\"";
					}
					else if (pattern == "\\\"frida\\\"")
					{
						pattern = "\"frida\"";
					}
					else if (pattern == "\\\"fridaserver\\\"")
					{
						pattern = "\"fridaserver\"";
					}
					else if (pattern == "\\\"frida-server\\\"")
					{
						pattern = "\"frida-server\"";
					}
					
					
					while(filePath1!=null)
					{
					//	System.out.println(filePath1);
						if(filePath1.contains(".smali"))
							codeInjection(filePath1,pattern);
						filePath1=bufferedReader.readLine();
					}
					
			}
		}
		  

		 
		
		
	}

	private static void codeInjection(String filePath, String pattern) throws IOException {
		
		
		String bugFind="R$string.smali";
		if(filePath.contains(bugFind))
		{
			System.out.println(pattern);
		}
		
		File file=new File(filePath);
		System.out.println(filePath);
		Scanner scanner=new Scanner(file);
		String output="";
		while(scanner.hasNext())
		{
			String contents=scanner.nextLine();
			if(contents.contains(pattern))
			{
				//update contents variable
				System.out.println("Overwriting :"+pattern);
				contents=contents.substring(0,(contents.indexOf("\"")+1))+"SaiBaba"+"\"";
			}
			output=output+contents+"\n";
			
		
		}

		scanner.close();
		writeToFile(output,filePath);
	
		
			
	}
	private static void writeToFile(String contents, String destination) throws IOException {
		// TODO Auto-generated method stub
		FileWriter fileWriter=new FileWriter(destination);
		fileWriter.write(contents);
		fileWriter.close();
	}

}
