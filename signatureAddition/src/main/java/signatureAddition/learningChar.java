package signatureAddition;

public class learningChar {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String hello="12945";
		char chArr[]=hello.toCharArray();
		for(int i=0;i<hello.length();i++)
		{
			int temp=Integer.parseInt(""+hello.charAt(i));
			temp++;
		}
	}

}
