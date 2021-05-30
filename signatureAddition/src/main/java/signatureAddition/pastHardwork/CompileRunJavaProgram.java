package signatureAddition.pastHardwork;


import javax.tools.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CompileRunJavaProgram {

    public static void main(String[] args) {
        try {
            //runProcess("pwd");
            String fileToCompile="~/Documents/java/hello.java";
    		String fileNamePath="/home/nikhil/Documents/apks/equitas.apk";
    		runProcess("ls " + fileNamePath);
            JavaCompiler compiler=ToolProvider.getSystemJavaCompiler();
            int compilationResult=compiler.run(null, null, null, fileToCompile);
            if(compilationResult==0)
            {
            	System.out.println("Compilation is successful");
            }
            else
            	System.out.println("Compilation failed");
            System.out.println("**********");
           //runProcess("javac -cp src src/main/java/signatureAddition/basicCommandLine.java");
            runProcess("apktool d "+fileNamePath + " -o /home/nikhil/Documents/equitas_2");
            System.out.println("**********");
            runProcess("apktool b");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    private static void printLines(String cmd, InputStream ins) throws Exception {
        String line = null;
        BufferedReader in = new BufferedReader(
            new InputStreamReader(ins));
        while ((line = in.readLine()) != null) {
            System.out.println(cmd + " " + line);
        }
      }

      private static void runProcess(String command) throws Exception {
        Process pro = Runtime.getRuntime().exec(command);
       // printLines(command + " stdout:", pro.getInputStream());
        //printLines(command + " stderr:", pro.getErrorStream());
        pro.waitFor();
        System.out.println(command + " exitValue() " + pro.exitValue());
      }

}
