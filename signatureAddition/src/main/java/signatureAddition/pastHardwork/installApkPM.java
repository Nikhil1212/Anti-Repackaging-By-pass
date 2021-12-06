package signatureAddition.pastHardwork;

import java.io.IOException;
import java.util.Scanner;

import signatureAddition.CommandExecute;

/**
 * Don't know about it; the file path contains double slash
 * @author nikhil
 *
 */
public class installApkPM {

	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
	//let's push the apk to the /data/local/tmp/path_to_apk
		/**
		 * ApkPath to be given as an input
		 */
		
		String adbDevices="adb devices";
		
		
		Scanner scanner=new Scanner(System.in);
		//String apkPath=scanner.next();
		String apkPath="D:\\Apps\\dataset\\in.org.npci.upiapp.apk";
		String packageName=fetchPakcageNamefromapkPath(apkPath);
		
		String uninstallCommand="adb uninstall "+packageName;
		
		String apkPathOnDevice=" /data/local/tmp/"+packageName+".apk";
		String pushApkCommand="adb push "+apkPath+apkPathOnDevice;
		String installThroughPMCommand="adb shell pm install -g"+apkPathOnDevice;
		String removeApkFromDevice="adb shell rm"+apkPathOnDevice;
		
		
		String commandToFilterLogsUsingPackageName="adb shell logcat | findstr "+packageName;//in.org.npci.upiapp";
		System.out.println(pushApkCommand);
		System.out.println(installThroughPMCommand);
		System.out.println(removeApkFromDevice);
		
		CommandExecute.commandExecution(uninstallCommand);
		CommandExecute.commandExecution(adbDevices);

		
		CommandExecute.commandExecution(pushApkCommand);
		
		CommandExecute.commandExecution(installThroughPMCommand);
		
		CommandExecute.commandExecution(removeApkFromDevice);
		Process pr=CommandExecute.commandExecution(commandToFilterLogsUsingPackageName);
		
			
	}

	private static String fetchPakcageNamefromapkPath(String apkPath) {
		
		String packageName="";
		int len=apkPath.length();
		/**
		 * fetch the position of the last / or \
		 */
		int index=apkPath.lastIndexOf('/');
		if(index==-1)
		{
			index=apkPath.lastIndexOf('\\');
		}
		packageName=apkPath.substring(index+1, len-4);
		return packageName;
	}

}
