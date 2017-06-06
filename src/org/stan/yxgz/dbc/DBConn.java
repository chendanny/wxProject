package org.stan.yxgz.dbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;




public class DBConn {
	
	
	public static Connection getConnection()  {
		Connection conn = null;

		// List<DataDG> list = new ArrayList<DataDG>();

		 //String host = BaeEnv.getBaeHeader(BaeEnv.BAE_ENV_ADDR_SQL_IP);	 
		 //String port = BaeEnv.getBaeHeader(BaeEnv.BAE_ENV_ADDR_SQL_PORT);	 
		 //String useName = BaeEnv.getBaeHeader(BaeEnv.BAE_ENV_AK);
		 //String password = BaeEnv.getBaeHeader(BaeEnv.BAE_ENV_SK);
		 
		 /*String host = "localhost";
		 String port = "3306";
		 String useName = "root";
	     String password = "root";		 

		 String driverName = "com.mysql.jdbc.Driver";
		 String dbUrl = "jdbc:mysql://";
		 String serverName = host + ":" + port + "/";

		 String databaseName = "yixin";
		 String connName = dbUrl + serverName + databaseName;*/
		
		
		 String host = "sqld.duapp.com";
		 String port = "4050";
		 String useName = "YA1wnj4PZ1mcevlDYyhp5WAW";
	     String password = "1XeGnohddh79lkZ2hwluOHDkAxsUGXoQ";		 

		 String driverName = "com.mysql.jdbc.Driver";
		 String dbUrl = "jdbc:mysql://";
		 String serverName = host + ":" + port + "/";

		 String databaseName = "HnmYIndfLAsmdEzqJPSl";
		 String connName = dbUrl + serverName + databaseName;
		 

		try {
			Class.forName(driverName);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			conn = DriverManager.getConnection(connName+"?useUnicode=true&characterEncoding=utf8", useName, password);
			//conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dgsj?useUnicode=true&characterEncoding=utf8","root", "");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}
	
}
