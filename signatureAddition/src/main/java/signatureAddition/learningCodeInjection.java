package signatureAddition;
/*
 * new-instance vinvoke, Landroid/content/pm/Signature;

    const-string vlocal, "3082058830820370a0030201020214197f4f619717ccd80a3521cfa836d42151755a65300d06092a864886f70d01010b05003074310b3009060355040613025553311330110603550408130a43616c69666f726e6961311630140603550407130d4d6f756e7461696e205669657731143012060355040a130b476f6f676c6520496e632e3110300e060355040b1307416e64726f69643110300e06035504031307416e64726f69643020170d3230303730333039353430355a180f32303530303730333039353430355a3074310b3009060355040613025553311330110603550408130a43616c69666f726e6961311630140603550407130d4d6f756e7461696e205669657731143012060355040a130b476f6f676c6520496e632e3110300e060355040b1307416e64726f69643110300e06035504031307416e64726f696430820222300d06092a864886f70d01010105000382020f003082020a0282020100b2cef62b266ae5e16ac5febc6b904cfc22043b2b99ea9886ceffdc8116115bf909af88685a67e5abe0b383029174d33a2f5a577ce837ecc94ba51ed9c6ccca7df966cc4b509aa56fa7ef533ce86dc8a5f5ea670b14847083f966a1751a95098a0942082b9a7d28b66f00b527f0a1674ddeb8ef31912f7e7f5ebbf9a3a657017692cb63d6c4761a7e0c8989a89d21de1bc8f1af7124d27518f02674d9865bd12881ddb0ee4c213a2b19199d9309d49fe1aec1196388648a339ed2726eb052f077b38ef36d451c3249ea97c6e43fdbf3930f6a02c3f62c8445e47b3082b7721141428b65664b2bd2a4311844b4fa10967fa306f6ad52fcee1398c9a0ed3c8ff6cbdc21538f5d4509291216527517f13a80a0063c37c7df16556dff3c43624499c949bcb38bd7f7d22d94c177bed76a580dd655b22a59ec79f29848a2435e8d426188659db2985191e629c5f1caa50a167dc65ea7bf272321ca7ff6fd75a03fdbc4d3c6c7070293ea1f545f711a6fcab7801d68e6b5b02ceb9b9133f9ffa504816ec0774d20843502680f7cb66a43360619066170907227a36f3526bf12796a1a54d3f0273221140718fdcdc469300082e2984dbb023ada2297dfb843f86d3c0695481693c411b60a10381b7fa9f00c2b27255f1e67d189e26c919cea458cc34f6ecf9044dab7f242b2affc5b557c507146fa4d7a684cf6696c8db46870ed1c0cab0203010001a310300e300c0603551d13040530030101ff300d06092a864886f70d01010b050003820201000b92b7d6d48f80bdfa0c1d1429b1a9ae6c0f85d424a29b3bc5593c4b337a039a94105e6edbea28096ef5d08b227813ef3ae14eba74985827c6eb739a8cbd1b365572bd93afc65c84ad0abb1c4dfb18b943f36358b932531cc66f614b12d4cc8fdadb9d1afb34c5210eb017471a38ca6d0c71c1225caf31aefbd7781036e4ab346c3e81d1dfb253f9fb9b15fbcee9b363a7c961d6f916ea749e6687a642a688492f388f29d251a72f177b6e6211e4f59a0604df581e96a2b3556ff4a153a3c29f91d5e81d6d84f8878a7dcc0a0411d548e0596165cded8714d409392d3851738bf106350d1ed8df667699297835f48c28936643e526317660462b201563aaabc9ce9bf7a4a28e3719bad9b70b1f974bf59f9bbdae4cdc5fbbfc9b39b773ff96c0c9933fe289c89d25db7096ae8a14887acf41ad2c578b1b5555e33fedfc38e99166218ff50cfa32417fcb26683bd938fcd03f59038ceb1c7f37ca44abc27bed3ba7b4324666b6cbe2c60aa6e473d3cf4f264d97214ab7780228531ec91dd39691af2ed668d016ea267f2b12be1aab9e2364b026723d50854bf7c4371fd2b6fbaa751303d7d0a79708af324147dd413a4d1f56dac987183ee768ccfa1925229ec0617bd227d061af76093037661355b775cf25f2a9723deff40650a8855700594d7e80e73d6c6528daba77b4a8598c16f18a0573d1dee2b9ea0a14fa1c78cd1901"

    invoke-direct {vname, vlocal}, Landroid/content/pm/Signature;-><init>(Ljava/lang/String;)V
        invoke-virtual {v2}, Landroid/content/pm/Signature;->toByteArray()[B

    For the above scenario, vname is that register from which toByteArray is called and the vlocal is the one from which is the current count of the local
     
 */
public class learningCodeInjection {
	public static void main(String args[]) {
		// TODO Auto-generated method stub
		
		
		//String original="HelloNikhil. Kaisa hai bhai. Nikhil, time ke saath saab hoga";
		String original="NikhilNikhilNikhil";
		String pattern="Nikhil";
		String wordToBeInserted="Agrawal ";
		/**
		 * Insert Agrawal prefix to Nikhil
		 */
		
		
		
		int index=original.indexOf(pattern);
		//String duplicate=original;
		String str="";
		int startingPoint=0;
		int len=original.length();
		//int pos=
		while(index!=-1)
		{
			str=original.substring(index);
			str=str.concat(wordToBeInserted);
			str=str.concat(pattern);
			if(index+pattern.length()==original.length())
				break;
			original=original.substring(index+pattern.length());
			index=original.indexOf(pattern);
		}
		//System.out.println(str);
		/*while(index!=-1)
		{
			String temp=original.substring(startingPoint,index);
			res=res.concat(temp); 
			res=res.concat(wordToBeInserted);
			startingPoint=index;
			String temp_1=original.substring(index+pattern.length());
			index=temp_1.indexOf(pattern);
		}*/
		
		
		
		str=str.concat(original.substring(startingPoint));
		System.out.println(str);
		
		
		
		
		
		
		
		
		
		
		
		/*
		String vinvoke="p1";
		String vlocal="v8";
		String keyFetched="123456";
		String s1="new-instance "+vinvoke+", Landroid/content/pm/Signature;\n\n";
		String s2="const-string "+vlocal+ ", \""+keyFetched+"\"";
		String s3="invoke-direct {"+vinvoke+", "+vlocal+"}, Landroid/content/pm/Signature;-><init>(Ljava/lang/String;)V\n";
		System.out.println(s1+s2+s3);*/
				
	}

}
