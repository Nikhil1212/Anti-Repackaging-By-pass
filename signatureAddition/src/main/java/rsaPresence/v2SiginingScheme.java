package rsaPresence;

/**
 * This class checks whether the apk which we download; is it installable or not. Because in the earlier days, we were using some Google Play API to download the apk, not the front-end automation.
 * 
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import Logs.LogAnalysis;
import finalRun.isDumpGenerated;
import signatureAddition.CommandExecute;
import signatureAddition.DataBaseConnect;

public class v2SiginingScheme {

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		String FilePath="/home/nikhil/Documents/apps/hello2.txt";
		File file=new File(FilePath);
		Scanner scanner=new Scanner(file);
		int count=0;
		String packageNames[]={"aib.ibank.android","air.app.scb.breeze.android.main.my.prod","com.bankofamerica.cashpromobile","com.bitcoin.wallet.btc","com.digitalinsight.cma.fiid01653","com.digitalinsight.cma.fiid01685","com.digitalinsight.cma.fiid01686","com.elrond.maiar.wallet","com.eyedroid.balancecheck","com.eyesdroid.ckbanking","com.ezmcom.softtoken.adcb","com.f1soft.banksmart.bok","com.fivory_prod.register","com.fusionmedia.investing","com.grppl.android.shell.BOS","com.grppl.android.shell.CMBlloydsTSB73","com.grppl.android.shell.halifax","com.ifs.banking.fiid1491","com.ifs.banking.fiid3399","com.ifs.banking.fiid4362","com.ifs.banking.fiid4902","com.ifs.banking.fiid5258","com.ifs.banking.fiid5372","com.ifs.mobilebanking.fiid3542","com.ifs.mobilebanking.fiid7281","com.kraken.trade","com.lighthouse1.mobilebenefits.pncbp","com.lloydsbank.businessmobile","com.manoramaonline.dps.manoramasampadyam","com.mfoundry.mb.android.mb_beb220","com.mobilebrokerage.CIBC","com.pcnc.aib","com.playtoshi","com.safesoftware.alanallur","com.safesoftware.cherpulasseryscb","com.safesoftware.kayiliadscb","com.safesoftware.kuzhuppillyscb","com.safesoftware.manjaliscb","com.safesoftware.nagalasseryscb","com.safesoftware.narikkuniscb","com.safeSoftware.neerikodescb","com.saib.mobile.cib","com.scb.breezebanking.hk","com.smartlogix.intradaypricelevels","com.tradingview.tradingviewapp","com.worldlineindia.pnb.merchant","com.youneedabudget.evergreen.app","fr.societegenerale.privatebanking.android","ftb.ibank.android","in.bankit.bankitPay","io.atomicwallet","kr.co.yjteam.dailypay","no.sparebank1.bm.mobilbank","no.sparebank1.mobilbank","no.SpareBank1.MobileOTP","societe.generale.private.ebanking.lux","uk.co.bankofscotland.businessbank","uk.co.metrobankonline.mobile.android.production","com.cnbc.client","com.devexperts.tdmobile.platform.android.thinkorswim","com.f1soft.banksmart.adbl","com.f1soft.banksmart.icfcfinance","com.f1soft.banksmart.pokharafinance","com.f1soft.banksmart.siddhartha","com.fab.corporate.ebanking","com.ffb.grip","com.ifs.banking.fiid1585","com.ifs.banking.fiid1607","com.ifs.banking.fiid4450","com.ifs.banking.fiid5230","com.ifs.banking.fiid5362","com.ifs.banking.fiid7076","com.ifs.banking.fiid8081","com.ifs.mobilebanking.fiid7061","com.lal.Tap.Calc","com.mipay.in.wallet","com.mkapps.virtualtrading","com.mlc","com.schwab.mobile","com.seekingalpha.webwrapper","com.simplecrypto.applearning.cryptoapp","com.simplestock.julian.aktienapp","com.smartfinancial.mobilebanking","com.taxslayerRFC","com.tdameritrade.mobile3","com.yahoo.mobile.client.android.finance","net.one97.paytm","org.altruist.BajajExperia","com.brisk.jpay","com.etrade.mobilepro.activity","com.expertoption","com.kraken.invest.app","com.mds.canarabankapp","com.mintwalk","com.realresearch.survey","net.gtl.mobile_app","piuk.blockchain.android"};

	//	int count=0;
		//ExecutePython.downloadApks(FilePath);
		while(scanner.hasNext())
		{
			try
			{
				String certificateDetails=scanner.nextLine();
				System.out.println(certificateDetails);
				//updateTable(packageName, 'Y', FilePath, packageName);
				//	updateTable(packageName, 'Y', "Overwrite String values","TempFinalDataset");


				updateTable(packageNames[count++], "appsCertificate", certificateDetails);
				//			updateTable(packageName,'Y',"APK Generated","ModifiedAppsGenerated");

			}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}


		}

	}


	public static void updateTable(String packageName, String tableName, String certificateDetails) throws SQLException {
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
			String query="Insert into "+tableName +" values ('"+packageName+"','"+certificateDetails+"');";
			System.out.println(query);

			Statement statement=DataBaseConnect.initialization();
			statement.executeUpdate(query);
		}
		else
		{
			return ;
		}

	}


	private static void updateTable(String packageName, char c, String remarks, String tableName) throws SQLException {
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
			String query="Insert into "+tableName +" values ('"+packageName+"','"+c+"','"+remarks+"');";
			System.out.println(query);

			Statement statement=DataBaseConnect.initialization();
			statement.executeUpdate(query);
		}
		else
		{
			String query="Update "+tableName+" set IsCheckPresent ='"+c+"' , remarks ='"+remarks+"' where packageName='"+packageName+"';";
			System.out.println(query);

			Statement statement=DataBaseConnect.initialization();
			statement.executeUpdate(query);
		}



	}
}
