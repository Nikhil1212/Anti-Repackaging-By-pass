package databaseUpdate;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

import signatureAddition.DataBaseConnect;

public class BankCateogry {
	public static void main(String[] args) throws FileNotFoundException {
		
		/**
		 * Public Sector Bank various app's name
		 */
		String sbiBank[]= {"com.sbi.aadhaarpay","com.sbi.SBIFreedomPlus","com.sbi.SBAnywhereCorporate","com.sbi.SBISecure","com.sbi.upi","com.sbi.bharatqr.wl","com.sbi.apps.sbi_loans","com.sbi.home_loans","com.sbi.bharatqr","com.moneyware.SBIExclusif","com.SbiBwAnywhere","com.sbi.myhrms","com.sbi.noqueue","com.SbiNpAnywhere","com.sbi.sbiquick","com.SGFinacleMobileApp","com.YONOBDMobileApp","com.YONOBHMobileApp","com.YONOCAMobileApp","com.YONOLKMobileApp","com.YONOMUApp","com.YONOMVMobileApp","com.YONOUKMobileApp","com.YONOZAMobileApp"};
		String bobBank[]= {"com.bankofbaroda.merchant.tcs.bob","com.bankofbaroda.upi","com.newgen.nemf.client.bob.app","com.bankofbaroda.crismaclegal","com.bankofbaroda.mconnect","com.finwizard.minvest","com.bob.intra"};
		String canaraBank[]= {"com.mds.canarabankapp","com.canarabank.CanaraSaathi","com.mobile.canaraepassbook","com.canarabank.mobility"};
		String bankOfIndia[]= {"com.boi.mpay","com.uniken.r2fa4a.boi","com.infra.boiupi","com.wl.cardprofiler.boi","com.tcs.merchant.cags.boi","com.uniken.r2fa4a.StarGlobal"};
		String maharastra[]= {"com.infrasofttech.MahaBank","com.uniken.r2fa4a.bom","com.infrasofttech.mahaupi","com.infrasoft.mahamerchant"};
		String centralBank[]= {"com.infrasofttech.CentralBank","com.mobile.cbiepassbook","com.infrasofttech.centralbankupi","com.bharatqr.cbi_new_reg","com.cent_mpos"};
		String indianBank[]= {"com.IndianBank.IndOASIS","worldline.indianbanksoftpos","integra.indianbank.aadhaarpay","com.infrasoft.indianbankmerchant","com.infrasoft.ibcorporatemerchant"};
		String overseas[]= {"com.fss.iob","com.euronet.iobupi","com.iobth.mobile","com.tcs.merchant.cags.iob"};
		String punjabSind[]={"com.tcs.merchant.bank.psb","com.psb.omnicorporate","com.psb.omniretail"};
		String pnb[]= {"in.pnbindia.mpassbook","com.Version1","in.pnb.pnbpa","com.isg.mobile.creditcard.pnb","com.ionicframework.pnbfl441705","com.uniken.pnb.verify","in.pnb.fastag","com.mgs.pnbupi","com.worldlineindia.pnb.merchant","com.tcs.merchant.cags.pnb","com.mgs.pnbmerchantpay"};
		String ucoBank[]= {"com.lcode.ucombook","com.lcode.ucosecure","com.lcode.ucopay","com.lcode.ucomobilebanking","com.lcode.corporateucomobile","com.lcode.ucombooksi","integra.uco.ipay","com.lcode.ucombookhk","com.ucobankmgalla","com.lcode.ucoupi"};
		String unionBank[]= {"com.unionbankofindia.ecircular","com.worldlineindia.ubi.merchantbqr","com.infrasoft.uboi","com.ubi.mobile","com.iz.zmt","com.infra.uatm","integra.ubi.aadhaarpay"};

		HashMap<String, String[]> hashMapBankNames=new HashMap<String, String[]>();
		hashMapBankNames.put("SBI", sbiBank);
		hashMapBankNames.put("Bank of Baroda", bobBank);
		hashMapBankNames.put("Canara Bank", canaraBank);
		hashMapBankNames.put("Bank of India", bankOfIndia);
		hashMapBankNames.put("bank of Maharastra", maharastra);
		hashMapBankNames.put("Central Bank of India", centralBank);
		hashMapBankNames.put("Indian Bank", indianBank);
		hashMapBankNames.put("Overseas Bank", overseas);
		hashMapBankNames.put("Punjab and Sind", punjabSind);
		hashMapBankNames.put("Punjab National Bank", pnb);
		hashMapBankNames.put("UCO", ucoBank);
		hashMapBankNames.put("union Bank", unionBank);
		
		
		/**
		 * Private Sector Bank
		 */
		
		String axisBank[]= {"com.axis.mobile","com.upi.axispay","com.axismerchant","com.axis.fxconnect","com.tcs.merchant.cags.axis","com.axisbankok","com.axisidp.mobile"};
		String bandhanBank[]= {"com.bandhan.mBandhan","com.fisglobal.bandhanupi.app"};
		String cSBBank[]= {"com.lcode.csbepassbook","com.lcode.csbupi","com.csb.merchant","com.maktechserve.csbmobileplus","com.bandhan.mBandhan"};
		String cityUnionBank[]= {"com.cub.wallet.gui","com.cub_upi.cubupi","com.cub.corporate","com.cub.plus.gui"};
		String DCBBank[]= {"com.dcbbank.dcbedsr","com.dcb","com.puratech.dcb_ids","com.nucleus.finnone.mobile.mserve.dcbneo.eng","com.abhi.techno.gst"};
		String dhanLaxmiBank[]= {"com.dhanlaxmi.dhansmart.mtc","com.lcode.dlbupi"};
		String federalBank[]= {"com.fedmobile","com.upi.federalbank.org.lotza","com.idemia.android.federal","co.in.federalbank.fednet","com.digiledge.web","com.intellect.cbx","com.federalbank.bqr.bharat_qr","com.corporatefedmobile","integra.federal.aadhaarpay"};
		String hdfcBank[]= {"com.hdfc.tomobile","com.hdfc.totablet","com.hdfcbank.iha.cpsms","com.hdfcbank.VIDC","com.snapwork.hdfcbank.smartaccountapp","com.snapwork.hdfcbankir","com.snapwork.hdfc","com.pfms.hdfcbank.iha","com.puratech.hdfc","otl.snkl.HDFCSnorkelOTP","com.indigo.hdfcloans","com.girnarsoft.hdfc","com.snapwork.HdfcBankIRMobile","com.hdfc.cbx","com.ionicframework.payzaap483385"};
		String idbiBank[]= {"com.worldlineindia.idbi.merchant","com.idbi.mpassbook","com.idbibank.abhay_card","com.idbibank.paywiz","com.idbibank.bcp","com.idbi.softtoken"};
		String iciciBank[]= {"com.icicibank.m2i","com.icicibank.pockets","com.icicibank.fastag","com.icici.museumapp","icici.imobile.app","com.icici.ismartcity","com.icici.connected","com.icicibank.allempatmaudit","com.icicibank.atmaudit","com.icicibank.iMcanada","com.icicibank.ourbranchourpride","com.icicibank.uotm","com.servo.icici.oapnxt.assisted","com.csam.icici.bank.mimobile","com.ibank.icici_secure_app","com.auth.icici","com.icicibank.icicidigitaltoken","com.icici.mas","com.icicibank.m2iEuropenew","com.newgen.nemf.icici.app","com.icicibank.ibizz"};
		String yesBank[]= {"com.atomyes","com.YesBank","com.YESMSME","com.credentek","com.nexussafe.truid.yessecure","com.worldlineindia.yesbank.merchant"};
		String idfcBank[]= {"com.idfcfirstbank.optimus","com.idfcfirstbank.fastag","com.idfcfirstbank.csapp","com.idfcbank.mobileBanking","com.idfc.etoll","com.fss.idfcpsp"};
		String indusIndBank[]= {"com.fss.indus","com.snapwork.indusindbank","com.indusland.merchantapp","com.indusforex","com.mgs.induspsp","com.indusindbank.IndusDirect"};
		String karnatakaBank[]= {"com.lcode.kblmobileplus","com.lcode.kblmbook","com.lcode.smartz"};
		String karurVysaBank[]= {"com.kvb.mobilebanking","kvbank.kvb_ebook","com.mycompany.kvb","com.kvb.merchant"};
		String kotakBank[]= {"com.msf.kbank.mobile","com.payswiff.kotakpos.app","com.onlinebankaccount.zerobalanceaccount","com.miles.kotakwealth","com.matchmove.multitenant.kotak","com.snapwork.kotak","com.kotakprime","com.worldlineindia.kotak.merchantbqr","myapp.winsofttech.com.kotak","com.mobicule.kotak_lms_fi"};
		String rblBank[]= {"com.rbl.rblmycard","com.rblbank.mobank"};
		String southIndiaBank[]= {"com.SIBMobile","com.sib.mpos","com.worldlineindia.sib.merchant","com.tcs.merchant.cags.sib"};
		String TamilnadMercantile[]= {"com.tmb.mbank","com.mobile.tmbepassbook"};
		
		hashMapBankNames.put("Axis", axisBank);
		hashMapBankNames.put("Bandhan", bandhanBank);
		hashMapBankNames.put("CSB", cSBBank);
		hashMapBankNames.put("City Union", cityUnionBank);
		hashMapBankNames.put("DCB", DCBBank);
		hashMapBankNames.put("DhanLaxmi", dhanLaxmiBank);
		hashMapBankNames.put("Federal", federalBank);
		hashMapBankNames.put("HDFC", hdfcBank);
		hashMapBankNames.put("IDBI", idbiBank);
		hashMapBankNames.put("icici", iciciBank);
		hashMapBankNames.put("yesBank", yesBank);
		hashMapBankNames.put("IDFC", idfcBank);
		hashMapBankNames.put("IndusInd", indusIndBank);
		hashMapBankNames.put("Karnataka", karnatakaBank);
		hashMapBankNames.put("Kasrur Vysa", karurVysaBank);
		hashMapBankNames.put("Kotak", kotakBank);
		hashMapBankNames.put("RBL", rblBank);
		hashMapBankNames.put("South India", southIndiaBank);
		hashMapBankNames.put("Tamilnad Mercantile", TamilnadMercantile);
		
		File file=new File("/home/nikhil/Documents/apps/BankAppsNotPresent.txt");
		Scanner scanner=new Scanner(file);
		while(scanner.hasNext())
		{
			updateTable1(scanner.next(),'N',"BankCategoryApps");
		}
	//	iterateOverHashMap(hashMap);
		
	}
	
	private static void iterateOverHashMap(HashMap<String, String[]> hashMap) {
		// TODO Auto-generated method stub
		 for (Map.Entry<String,String[]> entry : hashMap.entrySet())
		 {
			   String bankName=entry.getKey();
			   String packageNames[]= entry.getValue();
			   for(int i=0;i<packageNames.length;i++)
			   {
				   String packageName=packageNames[i];
				   try {
					updateTable(packageName, bankName, 'N', "BankCategoryApps");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			   }

		 }
	         
	}

	private static void updateTable(String packageName,  String bankName, char c,String tableName) throws SQLException {
		// TODO Auto-generated method stub

		String checkQuery="Select * from "+tableName+" where packageName='"+packageName+"';";
		System.out.println(checkQuery);
		Statement statement1=DataBaseConnect.initialization();
		ResultSet resultSet=statement1.executeQuery(checkQuery);
		int flag=0;
		String output="";
		while(resultSet.next())
		{
			flag=1;
			output=output+ resultSet.getString(1)+"\n";
		}
		if(flag==0)
		{
			String query="Insert into "+tableName +" values ('"+packageName+"','"+bankName+"','"+c+"');";
			System.out.println(query);

			Statement statement=DataBaseConnect.initialization();
			statement.executeUpdate(query);
		}
		else
		{
			String query="Update "+tableName+" set IsAppPresent ='"+c+"' , BankName ='"+bankName+"' where packageName='"+packageName+"';";
			System.out.println(query);

			Statement statement=DataBaseConnect.initialization();
			statement.executeUpdate(query);
		}



	}
	
public static void	updateTable1(String packageName, char c, String tableName)
{
	String query="Update "+tableName+" set IsAppPresent ='"+c+"' where packageName='"+packageName+"';";
	System.out.println(query);

	Statement statement=DataBaseConnect.initialization();
	try {
		statement.executeUpdate(query);
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

}
	
}
