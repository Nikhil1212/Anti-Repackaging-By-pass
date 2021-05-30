package signatureAddition;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

public class fetchCertificateKey {

	/*public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String pathToRSA="/home/nikhil/Documents/com.jio.myjio/original/META-INF/BNDLTOOL.RSA";
		String packageName="com.jio.myjio";
		String pathToCert="/home/nikhil/"+packageName+".pem";
		String commandToFetchCertificateFromRSA="openssl pkcs7 -in "+ pathToRSA+ " -inform DER -print_certs -out "+pathToCert;
		Process pr = Runtime.getRuntime().exec(commandToFetchCertificateFromRSA);
		String content = new String(Files.readAllBytes(Paths.get(pathToCert)));
		//	byte[] decoded = Base64.decodeBase64(content);
			 String[] arrOfStr = content.split("CERTIFICATE-----");
			 String[] strings=content.split("-----END CERTIFICATE-----");
			 String[] str=arrOfStr[1].split("-----END");
			byte[] decoded = Base64.decodeBase64(str[0]);	
				String hexString = Hex.encodeHexString(decoded);
				System.out.println(hexString);

	}*/
	public static String getCertificateInHex(String pathToRSA, String packageName) throws IOException, InterruptedException {
		//	String certificate="";
		String pathToCert="/home/nikhil/Documents/apps/certificates/"+packageName+"_cert.pem";
		//file creation is required otherwise it will throw no file found exception
		File file=new File(pathToCert);
		file.createNewFile();
		System.out.println("path to RSA is :"+pathToRSA);
		String commandToFetchCertificateFromRSA="openssl pkcs7 -in "+ pathToRSA+ " -inform DER -print_certs -out "+pathToCert;

		//let's execute the command

		Process pr = Runtime.getRuntime().exec(commandToFetchCertificateFromRSA);
		pr.waitFor();
		BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getErrorStream()));
		String line = "";
		while ((line=buf.readLine())!=null) {
			//as in the first line only we can get the package name.That's why immeditate break;
			System.out.println(line);
		}

		String content = new String(Files.readAllBytes(Paths.get(pathToCert)));
		if(content==null)
		{
			System.out.println("File is getting empty");
			System.exit(0);
		}
		//	byte[] decoded = Base64.decodeBase64(content);

		String[] arrOfStr = content.split("CERTIFICATE-----");
		//	 String[] strings=content.split("-----END CERTIFICATE-----");
		String[] str=arrOfStr[1].split("-----END");
		byte[] decoded = Base64.decodeBase64(str[0]);	
		String hexString = Hex.encodeHexString(decoded);
		//System.out.println(hexString);
		return hexString;

	}
	public static void codeInjection() throws Exception{
		String codeInjectionFileName="/home/nikhil/Documents/apps/com.mbanking.aprb.aprb/smali/com/mbanking/aprb/aprb/utils/j.smali";

		String fileReadPath="/home/nikhil/Downloads/logInsertion.smali";
		File file=new File(codeInjectionFileName);
		file.createNewFile();

		FileReader fr=new FileReader(codeInjectionFileName);   //reads the file  
		BufferedReader br=new BufferedReader(fr);  //creates a buffering character input stream  
		StringBuffer sb=new StringBuffer();    //constructs a string buffer with no characters  
		String line;  
		String codeInjection=new String(Files.readAllBytes(Paths.get(fileReadPath)));
		while((line=br.readLine())!=null)  
		{  
			int flag=0;
			if(line.contains(".locals 3")||line.contains(".locals 8"))
			{
				flag=1;
				//int index=line.indexOf(".locals");
				/*char ch=line.charAt(index+8);
				if(ch=='0'|| ch=='1')
				{
					char chArr[]=line.toCharArray();
					chArr[index+8]='2';
					line=new String(chArr);
				}*/
				sb.append(line);      //appends line to string buffer  
				sb.append("\n");     //line feed   
				sb.append(codeInjection);
			}
			if(flag==0)
			{
				sb.append(line);      //appends line to string buffer  
				sb.append("\n");  
			}
		}  
		fr.close();    //closes the stream and release the resources  
		System.out.println("Contents of File: ");  
		System.out.println(sb.toString());   //returns a string that textually represents the object  
		FileWriter myWriter = new FileWriter(codeInjectionFileName);

		myWriter.write(sb.toString());
		myWriter.close();
	}

}
