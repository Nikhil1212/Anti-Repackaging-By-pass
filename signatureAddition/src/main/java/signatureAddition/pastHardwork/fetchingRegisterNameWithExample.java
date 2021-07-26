package signatureAddition;

public class fetchingRegisterNameWithExample {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("From the eclipse. let's see whether we can see this command");
		String learn="invoke-virtual {p17}, ";
		String registerName=learn.substring(learn.length()-7,learn.length()-2);
		System.out.println(registerName);
		for(int i=0;i<registerName.length();i++)
		{
			if(registerName.charAt(i)=='v' || registerName.charAt(i)=='p')
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
		
	}

}
