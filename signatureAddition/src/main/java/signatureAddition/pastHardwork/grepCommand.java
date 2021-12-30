package signatureAddition.pastHardwork;

import java.util.HashSet;
import java.util.Set;

public class grepCommand {
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
	
	public static void hashSetInitialize()
	{
		//hello();
		hashSetAppPackageName=new HashSet<String>();
		hashSetAppPackageName.add("com.koushikdutta.rommanager");
		hashSetAppPackageName.add("com.koushikdutta.rommanager.license");
		hashSetAppPackageName.add("com.dimonvideo.luckypatcher");
		hashSetAppPackageName.add("com.chelpus.lackypatch");
		hashSetAppPackageName.add("com.ramdroid.appquarantine");	
		hashSetAppPackageName.add("com.ramdroid.appquarantinepro");
		hashSetAppPackageName.add("com.android.vending.billing.InAppBillingService.COIN");
		hashSetAppPackageName.add("com.chelpus.luckypatcher");
		
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
            
		hashSetAppPackageName.add("com.thirdparty.superuser");
		hashSetAppPackageName.add("eu.chainfire.supersu");
		hashSetAppPackageName.add("com.noshufou.android.su");
		hashSetAppPackageName.add("com.koushikdutta.superuser");
		hashSetAppPackageName.add("com.zachspong.temprootremovejb");
		hashSetAppPackageName.add("com.ramdroid.appquarantine");
		hashSetAppPackageName.add("com.topjohnwu.magisk");
		hashSetAppPackageName.add("com.yellowes.su");
		hashSetAppPackageName.add("com.noshufou.android.su.elite");
		
		
		hashSetAppPackageName.add("com.devadvance.rootcloak");
		hashSetAppPackageName.add("com.devadvance.rootcloakplus");
		hashSetAppPackageName.add("de.robv.android.xposed.installer");
		
		hashSetAppPackageName.add("com.formyhm.hideroot");
		hashSetAppPackageName.add("com.formyhm.hiderootPremium");
		hashSetAppPackageName.add("com.amphoras.hidemyrootadfree");
		
		hashSetAppPackageName.add("com.amphoras.hidemyroot");
		hashSetAppPackageName.add("com.saurik.substrate");
		
		
		hashSetSuPossiblePath=new HashSet<String>();
		hashSetSuPossiblePath.add("/data/local/bin/su");
		hashSetSuPossiblePath.add("/sbin/su");
		hashSetSuPossiblePath.add("/system/bin/su");
		hashSetSuPossiblePath.add("/system/bin/failsafe/su");
		hashSetSuPossiblePath.add("/system/xbin/su");
		
		hashSetSuPossiblePath.add("/system/xbin/busybox");
		hashSetSuPossiblePath.add("/system/sd/xbin/su");
		hashSetSuPossiblePath.add("/data/local/su");
		hashSetSuPossiblePath.add("/data/local/xbin/su");
		hashSetSuPossiblePath.add("/data/local/bin/su");

		hashSetSuPossiblePath.add("/data/local/");
		hashSetSuPossiblePath.add("/data/local/bin/");
		hashSetSuPossiblePath.add("/data/local/xbin/");
		hashSetSuPossiblePath.add("/sbin/");
		hashSetSuPossiblePath.add("/su/bin/");
		hashSetSuPossiblePath.add("/data");
		hashSetSuPossiblePath.add("/dev");
		hashSetSuPossiblePath.add("/cache");
		hashSetSuPossiblePath.add("/system/usr/we-need-root/");
		hashSetSuPossiblePath.add("/system/sd/xbin/");
		hashSetSuPossiblePath.add("/system/bin/.ext/");
		hashSetSuPossiblePath.add("/system/xbin/");
		hashSetSuPossiblePath.add("/system/bin/");

		hashSetSuPossiblePath.add("/system/bin/failsafe/");
//		"/system/bin/.ext/", "/system/bin/failsafe/", "/system/sd/xbin/", "/system/usr/we-need-root/", "/system/xbin/", "/cache", "/data", "/dev"}
		

		hashSetSuPossiblePath.add("/data/local/bin/su");
		hashSetHarmfulFiles=new HashSet<String>();
		hashSetHarmfulFiles.add("/system/app/Superuser.apk");
		hashSetHarmfulFiles.add("/system/etc/init.d/99SuperSUDaemon");
		hashSetHarmfulFiles.add("/dev/com.koushikdutta.superuser.daemon");
		hashSetHarmfulFiles.add("/system/xbin/daemonsu");
		//	System.out.println(universalHashSet.length);
		HashSet<String> hashSetBinaries=new HashSet<String>();
		String suPattern="su";
		hashSetBinaries.add("su");  //because we want the pattern to be "su"
		hashSetBinaries.add("magisk");
		hashSetBinaries.add("daemonsu");
		hashSetBinaries.add("busybox");
		hashSetBinaries.add("frida");

		hashSetBinaries.add("fridaserver");

		hashSetBinaries.add("frida-server");

		hashSetInitialize();
		

		HashSet<String> hashSetSensitiveDirectory=new HashSet<String>();
		hashSetSensitiveDirectory.add("/system");
		hashSetSensitiveDirectory.add("/system/bin");
		hashSetSensitiveDirectory.add("/system/sbin");
		hashSetSensitiveDirectory.add("/system/xbin");
		hashSetSensitiveDirectory.add("/vendor/bin");
		hashSetSensitiveDirectory.add("/sbin");
		hashSetSensitiveDirectory.add("/etc");
		
		hs[0]=hashSetAppPackageName;
		hs[1]=hashSetBinaries;
		hs[2]=hashSetSuPossiblePath;
		hs[3]=hashSetHarmfulFiles;
		hs[4]=hashSetSensitiveDirectory;

	
	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String pattern="su";
		String grepCommand="grep -r -l \\\""+pattern+"\\\"";

		System.out.println(grepCommand);
	}

}
