package signatureAddition.pastHardwork;

import java.io.FileWriter;
import java.io.IOException;

public class fileWriterLearn {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String filePath="/home/nikhil/Desktop/helloNikhil.txt";
		String myboy=" my boy";
		FileWriter fileWriter=new FileWriter(filePath);
		fileWriter.write(myboy);
		fileWriter.close();
	}

}
