package signatureAddition;
import javax.xml.parsers.DocumentBuilderFactory;  
import javax.xml.parsers.DocumentBuilder;  
import org.w3c.dom.Document;  
import org.w3c.dom.NodeList;  
import org.w3c.dom.Node;  
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;  
public class ParsingXML {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try   
		{  
			String packageName="in.org.npci.upiapp";
			File file = new File("/home/nikhil/Documents/pythonUIAutomator/dump_1.xml");  
			DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();  
			Document document = documentBuilder.parse(file);  
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  
			//an instance of builder to parse the specified xml file  
			DocumentBuilder db = dbf.newDocumentBuilder();  
			Document doc = db.parse(file);  
			doc.getDocumentElement().normalize();  
			System.out.println("Root element: " + doc.getDocumentElement().getNodeName());  
			HashSet<String> hashSet=new HashSet<String>();
			String pattern="text";
			String ans[]= {"text","resource-id","class","package","content-desc","checkable","checked","clickable","enabled","focusable","focused","scrollable","long-clickable","password","selected","visible-to-user"};
			int indexStore[]=new int[ans.length];
			HashMap<String, Integer> hashMap=new HashMap<>();
			hashMap=printNodeList(document.getChildNodes(),hashMap,packageName, 4,"");
			System.out.println(hashMap);
			for(int i=0;i<ans.length;i++)
			{
				indexStore[i]= printNodeList(document.getChildNodes(),ans[i]);
				//hashSet= printNodeList(document.getChildNodes(),hashSet,packageName,16); 
				//System.out.println("from main:"+hashSet);

			}
			for(int i=0;i<indexStore.length;i++)
			{
				System.out.println(ans[i]+":"+indexStore[i]);
			}





		}   
		catch (Exception e)   
		{  
			e.printStackTrace();  
		}  
	}  

	public static HashSet<String> printNodeList(NodeList nodeList, HashSet<String> hashSet, String packageName, int patternIndex) throws InterruptedException  
	{  
		//System.out.println("Number of the nodes is :"+nodeList.getLength());
		//Thread.sleep(10000);
		for (int count = 0; count < nodeList.getLength(); count++)   
		{  
			Node elemNode = nodeList.item(count);  
			if (elemNode.getNodeType() == Node.ELEMENT_NODE)   
			{  
				// get node name and value  
				//	System.out.println("\nNode Name =" + elemNode.getNodeName()+ " [OPEN]");  
				//System.out.println("Node Content =" + elemNode.getTextContent());  

				if (elemNode.hasAttributes())   
				{  
					String value="";
					//String packageName="in.org.npci.upiapp";
					NamedNodeMap nodeMap = elemNode.getAttributes();  
					//System.out.println("Nikhil. You are not that bad too!!"+nodeMap.getLength());
					if(nodeMap.getLength()<10)
					{
						if (elemNode.hasChildNodes())   
						{  
							//recursive call if the node has child nodes  
							hashSet=printNodeList(elemNode.getChildNodes(),hashSet,packageName,patternIndex);  
						}  

					}
					else
					{
						Node node = nodeMap.item(11);
						if(node.getNodeName().equals("package"))
						{
							//System.out.println("package name :"+node.getNodeValue());
							if(node.getNodeValue().equals(packageName))
							{
								node=nodeMap.item(patternIndex);
								value=node.getNodeValue();
								if(value.length()!=0)
									hashSet.add(value);
							}
						}
						if (elemNode.hasChildNodes())   
						{  
							//recursive call if the node has child nodes  
							hashSet=printNodeList(elemNode.getChildNodes(),hashSet,packageName,patternIndex);  
						}  
					}
				}



			}  
		}
		return hashSet;
	}

	public static HashMap<String, Integer> printNodeList(NodeList nodeList, HashMap<String, Integer> hashMap, String packageName, int patternIndex) throws InterruptedException  
	{  
		/**
		 * For counting the count of various View classes.
		 */
		//System.out.println("Number of the nodes is :"+nodeList.getLength());
		//Thread.sleep(10000);
		for (int count = 0; count < nodeList.getLength(); count++)   
		{  
			Node elemNode = nodeList.item(count);  
			if (elemNode.getNodeType() == Node.ELEMENT_NODE)   
			{  
				// get node name and value  
				//	System.out.println("\nNode Name =" + elemNode.getNodeName()+ " [OPEN]");  
				//System.out.println("Node Content =" + elemNode.getTextContent());  

				if (elemNode.hasAttributes())   
				{  
					String value="";
					//String packageName="in.org.npci.upiapp";
					NamedNodeMap nodeMap = elemNode.getAttributes();  
					//System.out.println("Nikhil. You are not that bad too!!"+nodeMap.getLength());
					if(nodeMap.getLength()<10)
					{
						if (elemNode.hasChildNodes())   
						{  
							//recursive call if the node has child nodes  
							hashMap=printNodeList(elemNode.getChildNodes(),hashMap,packageName,patternIndex);  
						}  

					}
					else
					{
						Node node = nodeMap.item(11);
						if(node.getNodeName().equals("package"))
						{
							//System.out.println("package name :"+node.getNodeValue());
							if(node.getNodeValue().equals(packageName))
							{
								node=nodeMap.item(patternIndex);
								value=node.getNodeValue();
								if(value.length()!=0)
									hashMap=updateHashMap(hashMap,value);
							}
						}
						if (elemNode.hasChildNodes())   
						{  
							//recursive call if the node has child nodes  
							hashMap=printNodeList(elemNode.getChildNodes(),hashMap,packageName,patternIndex);  
						}  
					}
				}



			}  
		}
		return hashMap;
	}

	public static HashMap<String, Integer> updateHashMap(HashMap<String, Integer> hashMap, String value) {
		// TODO Auto-generated method stub
		if(hashMap.containsKey(value))
		{
			int val=hashMap.get(value);
			hashMap.put(value, val+1);
		}
		else
			hashMap.put(value, 1);
		return hashMap;
	}

	private static int printNodeList(NodeList nodeList, String pattern) throws InterruptedException  
	{  
		for (int count = 0; count < nodeList.getLength(); count++)   
		{  
			Node elemNode = nodeList.item(count);  
			if (elemNode.getNodeType() == Node.ELEMENT_NODE)   
			{  
				// get node name and value  
				//	System.out.println("\nNode Name =" + elemNode.getNodeName()+ " [OPEN]");  
				//System.out.println("Node Content =" + elemNode.getTextContent());  
				if (elemNode.hasAttributes())   
				{  
					NamedNodeMap nodeMap = elemNode.getAttributes();  
					for (int i = 0; i < nodeMap.getLength(); i++)   
					{  
						Node node = nodeMap.item(i);  
						//	System.out.println("attr name : " + node.getNodeName());  
						if(node.getNodeName().equals(pattern))
						{

							return i;
							//Thread.sleep(30000);
							//	System.out.println("attr value : " + node.getNodeValue());  
						}  
					}  
					if (elemNode.hasChildNodes())   
					{  
						//recursive call if the node has child nodes  
						return printNodeList(elemNode.getChildNodes(),pattern);  
					}  
					//System.out.println("Node Name =" + elemNode.getNodeName()+ " [CLOSE]");  
				}  
			}  
		}
		return -1;
	}

	public static HashMap<String, Integer> printNodeList(NodeList nodeList, HashMap<String, Integer> hashMap,
			String packageName, int patternIndex, String emptyString) {
		for (int count = 0; count < nodeList.getLength(); count++)   
		{  
			Node elemNode = nodeList.item(count);  
			if (elemNode.getNodeType() == Node.ELEMENT_NODE)   
			{  
				if (elemNode.hasAttributes())   
				{  
					String value="";
					NamedNodeMap nodeMap = elemNode.getAttributes();  
					if(nodeMap.getLength()<10)
					{
						if (elemNode.hasChildNodes())   
						{  
							hashMap=printNodeList(elemNode.getChildNodes(),hashMap,packageName,patternIndex,emptyString);  
						}  

					}
					else
					{
						Node node = nodeMap.item(11);
						if(node.getNodeName().equals("package"))
						{
							//System.out.println("package name :"+node.getNodeValue());
							if(node.getNodeValue().equals(packageName))
							{
								node=nodeMap.item(patternIndex);
								value=node.getNodeValue();
								if(value.equals("true"))
									hashMap=updateHashMap(hashMap,node.getNodeName());
							}
						}
						if (elemNode.hasChildNodes())   
						{  
							//recursive call if the node has child nodes  
							hashMap=printNodeList(elemNode.getChildNodes(),hashMap,packageName,patternIndex,emptyString);  
						}  
					}
				}



			}  
		}
		return hashMap;

	}  
}




