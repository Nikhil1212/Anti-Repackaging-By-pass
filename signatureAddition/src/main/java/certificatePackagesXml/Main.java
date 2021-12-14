package certificatePackagesXml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Assuming the packages.xml file is already residing in the machine, this class is able to fetch the certificate details of a given app (part of packages.xml)
 * @author nikhil
 *
 */

public class Main {

	public static void main(String[] args) throws IOException {
	
		String filePathPackagesXML="/home/nikhil/Documents/apps/packages.xml";
		
		String packageName="com.icicibank.fastag";
		
		
		String certificateDetails=fetchCertificateDetailsFromPackagesXml(packageName,filePathPackagesXML);
		System.out.println(certificateDetails);
		
	}

	public static String fetchCertificateDetailsFromPackagesXml(String packageName, String filePathPackagesXML) throws IOException {
		String fileContents = new String(Files.readAllBytes(Paths.get(filePathPackagesXML)), StandardCharsets.UTF_8);
		
		
		String pattern="<package name=\""+packageName+"\"";
		System.out.println(pattern);
		
		String keyPrefixPattern="key=\"";
		System.out.println(fileContents.indexOf(pattern));
		
		String packageDetails=fileContents.substring(fileContents.indexOf(pattern));
		//System.out.println(packageDetails);
		
		String certRem=packageDetails.substring(packageDetails.indexOf(keyPrefixPattern)+keyPrefixPattern.length());
		int end=certRem.indexOf("\"");
		
		//System.out.println(certRem);
		System.out.println(certRem.substring(0,end));
		return certRem.substring(0,end);
	}
	

}
