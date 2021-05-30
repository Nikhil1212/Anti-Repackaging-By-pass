package signatureAddition;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class learningGrep {

	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		String cmd[]= {"/bin/s","r","l","Niranjan"};
		Process process=Runtime.getRuntime().exec("/bin/grep -r -l 'Landroid/content/pm/Signature->toByteArray' /home/nikhil/Documents/apps/com.mbanking.aprb.aprb");
		process.waitFor();
		BufferedReader buf1 = new BufferedReader(new InputStreamReader(process.getErrorStream()));
		String line="";
		while((line=buf1.readLine())!=null)
		{
			System.out.println(line);
		}
	}

}
