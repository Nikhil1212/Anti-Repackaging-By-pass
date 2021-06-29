package signatureAddition.pastHardwork;

import java.util.HashSet;

public class LearningHashSet {
public static void main(String args[]) {
	// TODO Auto-generated method stub
HashSet<String> hashSet=new HashSet<String>();
//hashSet.add("Nikhil");
HashSet<String> hashSet2=new HashSet<String>();
hashSet2.add("Nikhil");
hashSet2.add("Agrawal");
System.out.println(hashSet2.containsAll(hashSet));
}
}
