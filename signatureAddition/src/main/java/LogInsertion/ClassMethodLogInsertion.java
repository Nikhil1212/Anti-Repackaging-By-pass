package LogInsertion;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ClassMethodLogInsertion {

		public static int findingMultipleOccurencesOfSamePattern(String filePath, String pattern, int count) throws Exception {
			// TODO Auto-generated method stub
			System.out.println("File path is :"+filePath);
			String text=new String(Files.readAllBytes(Paths.get(filePath)));
				String temp=text;
			String ans="";
			String tag=fetchTagForInsertionClassName(filePath);
		//	int count=0;
			while(temp.indexOf(pattern)!=-1)
			{
				count++;
				int index=temp.indexOf(pattern);
				
				
				String methodName=fetchMethodName(temp, pattern);
				
				String codeToBeInserted=logGenerationCode(tag,methodName);
				
				System.out.println(codeToBeInserted); //now this is our wordToBeInserted

				temp=logInsertion(temp,codeToBeInserted,pattern);
			
				ans=ans+temp.substring(0,temp.indexOf(pattern)+pattern.length());
				
				index=temp.indexOf(pattern);
				
				temp=temp.substring(index+pattern.length());
			}
		//	System.out.println("value for the count is :"+count);
			ans=ans+temp;
			/**
			 * Write to the file as well as to stdout
			 */
			fileWrite(ans,filePath);
			//System.out.println(ans);
			return count;
		}

		public static void fileWrite(String ans, String destinationPath) throws IOException {
			// TODO Auto-generated method stub
			File file=new File(destinationPath);
			file.createNewFile();
			FileWriter fileWriter=new FileWriter(file);
			fileWriter.write(ans);
			fileWriter.close();
		}

		public static String logInsertion(String temp, String codeToBeInserted, String pattern) {
			// TODO Auto-generated method stub
			
			System.out.println("Inside the Log insertion !!");
			int index=temp.indexOf(pattern);
			
			index=temp.substring(0,index).lastIndexOf(".locals");
			index=index+8;
			int initial=index;
			while(temp.charAt(index)!='\n') 
			{
				index++;
				/**
				To reach the end of the line
				**/
			}
			int end=index;
			int registerCount=Integer.parseInt(temp.substring(initial, end));
			if(registerCount<2)
			{
				/**
				 * We need to look 
				 */	
				
				temp=temp.substring(0,initial)+"2"+temp.substring(end);
			}
			String remainigPartOfTemp=temp.substring(index,temp.length());

			String res=temp.substring(0,index)+codeToBeInserted+remainigPartOfTemp;
			//System.out.println(res);
			return res;
		}

		public static String logGenerationCode(String tag, String methodName) {
			// TODO Auto-generated method stub
			
			
			String insertCodeLine1="\nconst-string v0, \""+tag+"\"";
			String insertCodeLine2="\nconst-string v1, \""+methodName+" Caught you!!\"";
			String insertCodeLine3="\ninvoke-static {v0, v1}, Landroid/util/Log;->d(Ljava/lang/String;Ljava/lang/String;)I\n";
			
			return insertCodeLine1+insertCodeLine2+insertCodeLine3;
			
			//return null;
		}

		public static String fetchMethodName(String fileContents, String pattern) {

			/**
			 * Assuming there is only one pattern in the filecontents Given
			 */

			//	String fileContents=new String(Files.readAllBytes(Paths.get(filePath)));
			int index=fileContents.indexOf(pattern);
			
				String tempStr=fileContents.substring(0, index);
				index=tempStr.lastIndexOf(".method");
				int i;
				for(i=index;tempStr.charAt(i)!='\n';i++)
				{
					if(tempStr.charAt(i)=='(')
						break;
				}
				int endPoint=i;

				for(;tempStr.charAt(i)!=' ';i--)
				{
					System.out.print(tempStr.charAt(i));
				}
				int startPoint=i+1;
				return tempStr.substring(startPoint,endPoint);
			

		}

		public static String fetchTagForInsertionClassName(String filePath) throws Exception {

			int indexOfPoint=filePath.lastIndexOf('.');
			int indexOfSlash=filePath.lastIndexOf('/');
			if(indexOfPoint==-1 || indexOfSlash==-1)
			{
				throw new Exception("There is something wrong with the filePath");
			}

			return filePath.substring(indexOfSlash+1, indexOfPoint);

		}

	}


