package signatureAddition.pastHardwork;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseRegisters {

	public static String patternStr = "[v][0-9]";
	
	/**
	 * This function will take two arguements, one is the array and other is the current line that we will be traversing.
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		String curentLine="invoke-virtual {v8, v9, v10}, Landroid/app/AlertDialog;->show()V";
		String curentLine="  aget-object v6, v1, v14";

		int registers[]=new int[16];
		
	    Pattern pattern = Pattern.compile(patternStr);
	    Matcher matcher = pattern.matcher(curentLine);
	    if(matcher.find()){
	    	int indexStart=matcher.start();
	    	curentLine=curentLine.substring(indexStart);
	    	 String split[]=curentLine.split(", ");
	    	 for(int i=0;i<split.length;i++)
	    	 {
	    		 System.out.println(split[i]);
	    		 String string=split[i];
	    		 if(split[i].charAt(0)!='v')
	    			 break;
	    		 int j;
	    		 for( j=1;j<string.length();j++)
	    		 {
	    			 if(string.charAt(j)>='0' && string.charAt(j)<='9')
	    				 continue;
	    			 else
	    				 break;
	    		 }
	    		 Integer registerNumber=Integer.parseInt(string.substring(1,j));
	    		 if(registerNumber < 15)
	    			registers[registerNumber]=1;
	    	 }
	    		
	    System.out.println(matcher.start());//this will give you index
	    }
	    for(int i=0;i<registers.length;i++)
	    System.out.print(registers[i]+" ");
/*		Pattern p = Pattern.compile(regx);
		 Matcher m = p.matcher(val);
		 boolean b = m.matches();
		 System.out.println(b);
		if(nikhil.matches(regx))
			System.out.println("Yes, you got it");
		else
			System.out.println("Thoda aur try kaar mil jaayegaa");
		*/
	}

	/**
	 * 
	 *  For each line, we have to check is there any pattern of the form v[0-9]. if it is marked as use in the reigsters array
	 * @param registers
	 * @param currentLine
	 */
	public static void main(int[] registers, String currentLine) {
		// For each line, we have to check is there any pattern of the form v[0-9]. if it is marked as use in the reigsters array
		
	    Pattern pattern = Pattern.compile(patternStr);
	    Matcher matcher = pattern.matcher(currentLine);
	    if(matcher.find()){
	    	int indexStart=matcher.start();
	    	currentLine=currentLine.substring(indexStart);
	    	System.out.println(currentLine);
	    	 String split[]=currentLine.split(", ");
	    	 for(int i=0;i<split.length;i++)
	    	 {
	    		 System.out.println(split[i]);
	    		 String string=split[i];
	    		 if(split[i].charAt(0)!='v')
	    			 break;
	    		 int j;
	    		 for( j=1;j<string.length();j++)
	    		 {
	    			 if(string.charAt(j)>='0' && string.charAt(j)<='9')
	    				 continue;
	    			 else
	    				 break;
	    		 }
	    		 Integer registerNumber=Integer.parseInt(string.substring(1,j));
	    		 System.out.println("Register number is :"+registerNumber);
	    		 if(registerNumber <= 15)
	    			registers[registerNumber]=1;
	    	 }
	    		
	    System.out.println(indexStart);//this will give you index
	    }
	    else
	    {
	    	System.out.println("No match found for the line:"+currentLine);
	    }
	    for(int i=0;i<registers.length;i++)
	    System.out.print(registers[i]+" ");
	}

}
