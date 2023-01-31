package signatureAddition;


/**
 * One important scenario is left where we can check the registers post the signature related API is called and the registers which are used here. So, if there is any register [v0-v15] which is not used in the remaining section, then we can use that register also.
	And also, we need to restore the value that we have fetched.
	Assumption: Signature related APIs will only be invoked two times in single file.
 */
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import signatureAddition.pastHardwork.ParseRegisters;

public class FinalCodeInjection {
	
	public static String constRegisterValue=null;
/*	public static void main(String[] args) throws Exception {
		String filePath="/home/nikhil/Documents/apps/dataset/com.khaalijeb.inkdrops/base/smali_classes2/com/khaalijeb/inkdrops/e/h.smali";
		String key="KuchBhi";
		String pattern="Landroid/content/pm/Signature;->equals(";
		codeInjection(filePath, key, pattern);
	}*/

	public static void codeInjection(String filePath, String key, String pattern) throws Exception {

		int i=0;


		File file=new File(filePath);

		//String pattern="Landroid/content/pm/Signature;->toByteArray()";
		String codeInjection=new String(Files.readAllBytes(Paths.get(filePath)));

		/**
		 *Storing the file contents to the String variable
		 **/

		int indextoByteArray=codeInjection.indexOf(pattern);
		if(indextoByteArray==-1)
			return;
		int lastIndextoByteArray=codeInjection.lastIndexOf(pattern);
		if(indextoByteArray != lastIndextoByteArray)
		{
			lastCodeInjection(filePath,key,pattern);
		}
		codeInjection=new String(Files.readAllBytes(Paths.get(filePath)));
		String codeFromStartingTillToByteArray=codeInjection.substring(0,indextoByteArray);

		String patternLocal=".locals ";
		int indexLocal=codeFromStartingTillToByteArray.lastIndexOf(patternLocal);

		/**
		fetched the position where position of locals is there in
		So, that we can modify the count of the registers
		 **/


		String codeToWrite=codeInjection.substring(0,indexLocal+8);
		/**
		 The above code is there as we don't need to modify this part of File 
		 **/

		String localsCount=codeFromStartingTillToByteArray.substring(indexLocal+8, indexLocal+10);
		int localsCountInteger=localsCountToInt(localsCount);

		/**
		 * The above code is written to fetch the number of register so that we can update it
		 */

		System.out.println("Value of localsCount is : "+localsCountInteger);

	//	if(localsCountInteger>14)
		//	localsCountInteger=14;
		codeToWrite=codeToWrite+(localsCountInteger+1)+"\n";

				/**
		 * Modified the code to increase the count of registers
		 */

		//how to fetch the string which is 

		/**
		 * Fetch the position of the string so that we can append it to the codeToWrite
		 */

		for( i=indexLocal+8;i<codeFromStartingTillToByteArray.length();i++)
		{
			if(codeFromStartingTillToByteArray.charAt(i)=='\n')
				break;
		}
		codeToWrite =   codeToWrite + codeFromStartingTillToByteArray.substring(i, codeFromStartingTillToByteArray.length());

		codeToWrite = codeToWrite + codeInjection.substring(indextoByteArray);

		//		System.out.println(codeToWrite);
		/**
		 * codeToWrite is the new string in which we have to do the code injection
		 * return by the modifyingCode
		 */
		String fromLocaltoSignatureMethodInvoke=codeInjection.substring(indexLocal , indextoByteArray);

		/***
		 * Let's fetch the register which is inovking the toByteArray
		 */

		String registerName=registerInvoke1(fromLocaltoSignatureMethodInvoke);
		//	System.out.println(fromLocaltoByteArray.substring(0, fromLocaltoByteArray.length()-22));

		System.out.println("Register name is :"+registerName);
		String signRelatedCode="";
		if(localsCountInteger < 16)
			signRelatedCode=modifyingCode(registerName, localsCountInteger, key);
		else
		{
			int availableRegisterNumber=findRegister(fromLocaltoSignatureMethodInvoke);

			if(availableRegisterNumber>15)
				availableRegisterNumber=15;
			signRelatedCode=modifyingCode(registerName, availableRegisterNumber, key);

			/**
			 * Find the variable name that stores the string from .locals till signature->method(). Once we got, store the contents to the file  and let's scan it line by line. 
			 */

		}
		String finalcode=insertSignCode(codeToWrite,signRelatedCode,pattern);


		//	System.out.println("For the file :"+filePath +" this is the code which we have generated \n ------------------\n");
		//	System.out.println(finalcode);

		FileWriter myWriter = new FileWriter(filePath);

		myWriter.write(finalcode);
		myWriter.close();


		//System.out.println(registerName);
	}

	public static int findRegister(String fromLocaltoByteArray) throws Exception {

		/**
		 * First we are checking whether is there any register from v0-v15 which is not used so far.
		 */
		String currentLine="";
		//Create a file
		String tempFilePath="/home/nikhil/Documents/apps/tempFile.txt";
		FileWriter fileWriter=new FileWriter(tempFilePath);
		
		
		fileWriter.write(fromLocaltoByteArray);
		fileWriter.close();
		File file=new File(tempFilePath);
		Scanner scanner=new Scanner(file);
		int registers[]=new int[16];

		while(scanner.hasNext())
		{
			currentLine=scanner.nextLine();
			ParseRegisters.main(registers,currentLine);
		}
		scanner.close();
		int flag=0;
		int i;
		for(i=0;i<registers.length;i++)
		{
			if(registers[i]==0) //1 means the register is already in use. 0 means the register is not used. So we got this ith register and break.
			{
				flag=1;
				break;
			}
		}
		if(flag==1)
			return i;

		else
		{
			//find the constant string with the keyword const-string and later point of time, we can also do the const/4 or const/16, if required. So, currently, we can do the following
			int arrConstStringMarking[]=new int[16]; // array for finding which register is involved in the const-string instruction
			
			
			for( i=0;i<registers.length;i++)
				arrConstStringMarking[i]=0;
			
			// There is a need to reset the values.
			
			scanner=new Scanner(file);
			String pattern="const-string v";
			while(scanner.hasNext())
			{
				String line=scanner.nextLine();
				if(line.contains(pattern))
				{
					int start=line.indexOf(pattern)+pattern.length();
					for(i=line.indexOf(pattern)+pattern.length();line.charAt(i)!=',';i++)
					{
						
					}
					String numStr=line.substring(start,i);
					System.out.println("Number fetched for const-str is :"+numStr);
					Integer integer=Integer.parseInt(numStr);
					if(integer<16)
						arrConstStringMarking[integer]=1;
					//fetch the register
					
				}
			}
			scanner.close();
			
			String constRegisterValue=null;
			for( i=0;i<arrConstStringMarking.length;i++)
			{
				if(arrConstStringMarking[i]==1)
				{
					String patternRegister="v"+i;
					constRegisterValue=checkPattern(patternRegister,tempFilePath);
					if(constRegisterValue!=null) // It means this register is not involved anywhere else apart from const-string.
						break;
				}
			}
			return i;
		}
		



	}
	/**
	 * This function checks whether a given register is coming somewhere else apart from const-string
	 * @param patternRegister
	 * @param tempFilePath
	 * @return 
	 */

	private static String checkPattern(String patternRegister, String tempFilePath) throws Exception {

		/*
		 * For each of the registers who are the part of const-string instruction, we are traversing the entire file and finding out whether it is involved in some other instruction. 
		 */
		File file=new File(tempFilePath);
		Scanner scanner=new Scanner(file);
		String constStringPattern="const-string v";
		String value="";
		while(scanner.hasNext())
		{
			String line=scanner.nextLine();
			
			if(line.contains(patternRegister))
			{
				if(line.contains(constStringPattern))
				{
					//fetch the value of this string 
					int startIndex=line.indexOf("\"");
					value =line.substring(startIndex, line.lastIndexOf("\"")+1);
					System.out.println("value fetched :"+value);
				}
				else
				{
					//It means that this register is involved in another instruction also. So, we can't use that
					
					value=null;
					break;
				}
			}
			
		}
		return value;
	}

	private static void lastCodeInjection(String filePath, String key, String pattern) throws Exception {
		// TODO Auto-generated method stub
		int i=0;

		//String filePath="/home/nikhil/Documents/apps/com.mbanking.aprb.aprb/smali/com/mbanking/aprb/aprb/utils/j.smali";
		//String key="3082058830820370a0030201020214197f4f619717ccd80a3521cfa836d42151755a65300d06092a864886f70d01010b05003074310b3009060355040613025553311330110603550408130a43616c69666f726e6961311630140603550407130d4d6f756e7461696e205669657731143012060355040a130b476f6f676c6520496e632e3110300e060355040b1307416e64726f69643110300e06035504031307416e64726f69643020170d3230303730333039353430355a180f32303530303730333039353430355a3074310b3009060355040613025553311330110603550408130a43616c69666f726e6961311630140603550407130d4d6f756e7461696e205669657731143012060355040a130b476f6f676c6520496e632e3110300e060355040b1307416e64726f69643110300e06035504031307416e64726f696430820222300d06092a864886f70d01010105000382020f003082020a0282020100b2cef62b266ae5e16ac5febc6b904cfc22043b2b99ea9886ceffdc8116115bf909af88685a67e5abe0b383029174d33a2f5a577ce837ecc94ba51ed9c6ccca7df966cc4b509aa56fa7ef533ce86dc8a5f5ea670b14847083f966a1751a95098a0942082b9a7d28b66f00b527f0a1674ddeb8ef31912f7e7f5ebbf9a3a657017692cb63d6c4761a7e0c8989a89d21de1bc8f1af7124d27518f02674d9865bd12881ddb0ee4c213a2b19199d9309d49fe1aec1196388648a339ed2726eb052f077b38ef36d451c3249ea97c6e43fdbf3930f6a02c3f62c8445e47b3082b7721141428b65664b2bd2a4311844b4fa10967fa306f6ad52fcee1398c9a0ed3c8ff6cbdc21538f5d4509291216527517f13a80a0063c37c7df16556dff3c43624499c949bcb38bd7f7d22d94c177bed76a580dd655b22a59ec79f29848a2435e8d426188659db2985191e629c5f1caa50a167dc65ea7bf272321ca7ff6fd75a03fdbc4d3c6c7070293ea1f545f711a6fcab7801d68e6b5b02ceb9b9133f9ffa504816ec0774d20843502680f7cb66a43360619066170907227a36f3526bf12796a1a54d3f0273221140718fdcdc469300082e2984dbb023ada2297dfb843f86d3c0695481693c411b60a10381b7fa9f00c2b27255f1e67d189e26c919cea458cc34f6ecf9044dab7f242b2affc5b557c507146fa4d7a684cf6696c8db46870ed1c0cab0203010001a310300e300c0603551d13040530030101ff300d06092a864886f70d01010b050003820201000b92b7d6d48f80bdfa0c1d1429b1a9ae6c0f85d424a29b3bc5593c4b337a039a94105e6edbea28096ef5d08b227813ef3ae14eba74985827c6eb739a8cbd1b365572bd93afc65c84ad0abb1c4dfb18b943f36358b932531cc66f614b12d4cc8fdadb9d1afb34c5210eb017471a38ca6d0c71c1225caf31aefbd7781036e4ab346c3e81d1dfb253f9fb9b15fbcee9b363a7c961d6f916ea749e6687a642a688492f388f29d251a72f177b6e6211e4f59a0604df581e96a2b3556ff4a153a3c29f91d5e81d6d84f8878a7dcc0a0411d548e0596165cded8714d409392d3851738bf106350d1ed8df667699297835f48c28936643e526317660462b201563aaabc9ce9bf7a4a28e3719bad9b70b1f974bf59f9bbdae4cdc5fbbfc9b39b773ff96c0c9933fe289c89d25db7096ae8a14887acf41ad2c578b1b5555e33fedfc38e99166218ff50cfa32417fcb26683bd938fcd03f59038ceb1c7f37ca44abc27bed3ba7b4324666b6cbe2c60aa6e473d3cf4f264d97214ab7780228531ec91dd39691af2ed668d016ea267f2b12be1aab9e2364b026723d50854bf7c4371fd2b6fbaa751303d7d0a79708af324147dd413a4d1f56dac987183ee768ccfa1925229ec0617bd227d061af76093037661355b775cf25f2a9723deff40650a8855700594d7e80e73d6c6528daba77b4a8598c16f18a0573d1dee2b9ea0a14fa1c78cd1901";

		File file=new File(filePath);

		//String patternToByte="Landroid/content/pm/Signature;->toByteArray()";
		String codeInjection=new String(Files.readAllBytes(Paths.get(filePath)));

		/**
		 *Storing the file contents to the String variable
		 **/

		int indextoByteArray=codeInjection.lastIndexOf(pattern);

		String codeFromStartingTillToByteArray=codeInjection.substring(0,indextoByteArray);

		String patternLocal=".locals ";
		int indexLocal=codeFromStartingTillToByteArray.lastIndexOf(patternLocal);

		/**
		fetched the position where position of locals is there in
		So, that we can modify the count of the registers
		 **/


		String codeToWrite=codeInjection.substring(0,indexLocal+8);
		/**
		 The above code is there as we don't need to modify this part of File 
		 **/

		String localsCount=codeFromStartingTillToByteArray.substring(indexLocal+8, indexLocal+10);
		int localsCountInteger=localsCountToInt(localsCount);

		/**
		 * The above code is written to fetch the number of register so that we can update it
		 */

		System.out.println("Value of localsCount is : "+localsCountInteger);
		codeToWrite=codeToWrite+(localsCountInteger+1)+"\n";




		/**
		 * Modified the code to increase the count of registers
		 */

		//how to fetch the string which is 

		/**
		 * Fetch the position of the string so that we can append it to the codeToWrite
		 */

		for( i=indexLocal+8;i<codeFromStartingTillToByteArray.length();i++)
		{
			if(codeFromStartingTillToByteArray.charAt(i)=='\n')
				break;
		}
		codeToWrite =   codeToWrite + codeFromStartingTillToByteArray.substring(i, codeFromStartingTillToByteArray.length());

		codeToWrite = codeToWrite + codeInjection.substring(indextoByteArray);

		//		System.out.println(codeToWrite);
		/**
		 * codeToWrite is the new string in which we have to do the code injection
		 * return by the modifyingCode
		 */
		String fromLocaltoSignatureMethodInvoke=codeInjection.substring(indexLocal , indextoByteArray);

		/***
		 * Let's fetch the register which is inovking the toByteArray
		 */
		String registerName=registerInvoke1(fromLocaltoSignatureMethodInvoke);
		System.out.println(fromLocaltoSignatureMethodInvoke.substring(0, fromLocaltoSignatureMethodInvoke.length()-22));

		String signRelatedCode="";
		if(localsCountInteger < 16)
			signRelatedCode=modifyingCode(registerName, localsCountInteger, key);
		else
		{
			int availableRegisterNumber=findRegister(fromLocaltoSignatureMethodInvoke);

			if(availableRegisterNumber>15)
				availableRegisterNumber=15;
			signRelatedCode=modifyingCode(registerName, availableRegisterNumber, key);
			/**
			 * Find the variable name that stores the string from .locals till signature->method(). Once we got, store the contents to the file  and let's scan it line by line. 
			 */

		}

		//	String signRelatedCode=modifyingCode(registerName, localsCountInteger, key);

		String finalcode=insertSignCodeLast(codeToWrite,signRelatedCode,pattern);


		//System.out.println("For the file :"+filePath +" this is the code which we have generated \n ------------------\n");
		//	System.out.println(finalcode);

		FileWriter myWriter = new FileWriter(filePath);

		myWriter.write(finalcode);
		myWriter.close();


		//System.out.println(registerName);

	}

	private static String insertSignCode(String fileContents, String signRelatedCode, String pattern) throws IOException {

		/**
		 * We have updated the locals register count
		 * Now just insert the signature related code
		 */
	/*	
		System.out.println(codeToWrite);
		String filePath="/home/nikhil/Documents/apps/codeToWrite.txt";
		FileWriter fileWriter=new FileWriter(filePath);
		fileWriter.write(codeToWrite);
		fileWriter.close();
		
		filePath="/home/nikhil/Documents/apps/signRelatedCode.txt";
		fileWriter=new FileWriter(filePath);
		fileWriter.write(signRelatedCode);
		fileWriter.close();
		*/
		
		//String fileContentsTillSignatuteInvoke=fileContents.
		
	//	int indexInvokeVirtual=fileContents.lastIndexOf("invoke-virtual");
		
		
		
		System.out.println(fileContents);
		
		int indexPattern=fileContents.indexOf(pattern);

		
		String contentsTillPattern=fileContents.substring(0,indexPattern);
		int indexInvokeVirtual=contentsTillPattern.lastIndexOf("invoke-virtual");
		
		//int indexInvokeVirtual=fileContents.lastIndexOf("invoke-virtual");
		System.out.println("Value of the invoke-virtual is :"+indexInvokeVirtual);
		
		System.out.println("Value of the pattern :"+indexPattern);
		
		
		//int len=fileContents.length();
		//String beforeSignInjection=codeToWrite.substring(0, len-(indexPattern-(indexInvokeVirtual)));
		
		//String beforeSignInjection=fileContents.substring(0, indexInvokeVirtual);
		
		String beforeSignInjection=fileContents.substring(0, indexInvokeVirtual);
		
	/*	
		System.out.println("Index of virtual :"+indexInvokeVirtual);

		//	String patternToByteArray="Landroid/content/pm/Signature;->toByteArray()";

		int indextoByteArray=fileContents.indexOf(pattern);

		System.out.println("Index of toByteArray :"+indextoByteArray);

		String beforeSignInjection=fileContents.substring(0,indextoByteArray-22);*/
		/**
		 * I have a doubt whether this 22 will always works
		 */
		String res=beforeSignInjection+ signRelatedCode +fileContents.substring(indexInvokeVirtual);

		return res;
	}
	/**
	 * 
	 * @param fileContents (the value that contains the file's contents with the modification of the .locals parameter)
	 * @param signRelatedCode
	 * @param pattern
	 * @return
	 */
	private static String insertSignCodeLast(String fileContents, String signRelatedCode, String pattern) {

		/**
		 * We have updated the locals register count
		 * Now just insert the signature related code
		 */
		
		/**
		 * codeToWrite contains the code of the entire file
		 */

		//String patternToByteArray="Landroid/content/pm/Signature;->toByteArray()";

		
		System.out.println(fileContents);
		
		int indexPattern=fileContents.lastIndexOf(pattern);

		
		String contentsTillPattern=fileContents.substring(0,indexPattern);
		int indexInvokeVirtual=contentsTillPattern.lastIndexOf("invoke-virtual");
		
		//int indexInvokeVirtual=fileContents.lastIndexOf("invoke-virtual");
		System.out.println("Value of the invoke-virtual is :"+indexInvokeVirtual);
		
		System.out.println("Value of the pattern :"+indexPattern);
		
		
		//int len=fileContents.length();
		//String beforeSignInjection=codeToWrite.substring(0, len-(indexPattern-(indexInvokeVirtual)));
		
		//String beforeSignInjection=fileContents.substring(0, indexInvokeVirtual);
		
		String beforeSignInjection=fileContents.substring(0, indexInvokeVirtual);
		
		/**
		 * I have a doubt whether this 22 will always works
		 */
		
//		String res=beforeSignInjection+ signRelatedCode +codeToWrite.substring(indextoByteArray -22);

		
		String res=beforeSignInjection+ signRelatedCode +fileContents.substring(indexInvokeVirtual);

		return res;
	}

	private static String registerInvoke(String fromLocaltoByteArray) {

		int len=fromLocaltoByteArray.length();
		System.out.println(fromLocaltoByteArray);
		System.out.println("\n-------------------------------------------------------------------***********------------------------");
		String registerName=fromLocaltoByteArray.substring(fromLocaltoByteArray.length()-7,fromLocaltoByteArray.length());
		System.out.println(registerName);
		for(int i=0;i<registerName.length();i++)
		{
			if(registerName.charAt(i)=='v' || registerName.charAt(i)=='p') //as the register can be either local or the parameterized one
			{
				int startpoint=i;
				while(registerName.charAt(i)!='}')
				{
					i++;
				}
				registerName=registerName.substring(startpoint,i);
				System.out.println(registerName);
				break;
			}
		}
		return registerName;
		//return null;
	}
	
	private static String registerInvoke1(String fromLocaltoByteArray) {

		//return null;
		
		int index=fromLocaltoByteArray.lastIndexOf("invoke-virtual");
		String output=fromLocaltoByteArray.substring(index);
		System.out.println(output);
		int openingBracketIndex=output.indexOf("{");
		int i=openingBracketIndex;
		while(output.charAt(i)!=',' && output.charAt(i)!='}' )
		{
			i++;
		}
		String res=output.substring(openingBracketIndex+1,i);
		System.out.println(res);
		return res;
	
	}

	private static int localsCountToInt(String localsCount) {

		int i;
		for(i=0;i<localsCount.length();i++)
		{
			if(localsCount.charAt(i)=='\n')
				break;
		}

		return Integer.parseInt(localsCount.substring(0,i));
	}
	private static String modifyingCode(String registerInvoke, int localsCount, String keyFetched) {
		// TODO Auto-generated method stub
		//So, this will return a string which will needs to be injected just before calling the signature's toByteArray()
		//String vinvoke="p1";
		//String vlocal="v8";
		//String keyFetched="123456";
		String s1="\nnew-instance "+registerInvoke+", Landroid/content/pm/Signature;\n\n";
		String s2="const-string v"+localsCount+ ", \""+keyFetched+"\"";
		String s3="\ninvoke-direct {"+registerInvoke+", v"+localsCount+"}, Landroid/content/pm/Signature;-><init>(Ljava/lang/String;)V\n";
		System.out.println(s1+s2+s3);
		return s1+s2+s3;
	}

}
