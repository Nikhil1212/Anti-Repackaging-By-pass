package signatureAddition;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DataBaseConnect {

	public static Statement initialization() {
		// TODO Auto-generated method stub
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String sqlurl="jdbc:mysql://localhost:3306/apps";
			Connection cn=DriverManager.getConnection(sqlurl,"nikhil","nikhil");
			Statement st=cn.createStatement();
			return st;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
