package signatureAddition.pastHardwork;

public class InvokeVirtual {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String command="qwasddddddsgbfvinvoke-virtual {p2} Landroid/content/pm/Signature;->equals(Ljava/lang/Object;)Z";
		String pattern="Landroid/content/pm/Signature;->equals(";
		int index=command.indexOf(pattern);
		int index2=command.lastIndexOf("invoke-virtual");
		System.out.println(command.substring(index2,index));
		String output=command.substring(index2,index);
		System.out.println(output);
		int openingBracketIndex=output.indexOf("{");
		int i=openingBracketIndex;
		while(output.charAt(i)!=',' && output.charAt(i)!='}' )
		{
			i++;
		}
		System.out.println(output.substring(openingBracketIndex+1,i));
	
	}

}
