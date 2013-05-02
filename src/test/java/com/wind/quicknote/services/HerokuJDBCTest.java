package com.wind.quicknote.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * deploy java app to heroku
 * 
 * http://books.zkoss.org/wiki/ZK%20Installation%20Guide/Setting%20up%20Servers/Heroku
 * https://devcenter.heroku.com/articles/heroku-postgresql#connecting-in-java
 * http://blog.csdn.net/xianqiang1/article/details/8589998
 * http://jdbc.postgresql.org/documentation/head/connect.html
 * 
 */
public class HerokuJDBCTest {
	
	public static void main(String[] argv) {
		

		System.out.println("-------- PostgreSQL JDBC Connection Testing ------------");

		try {

			Class.forName("org.postgresql.Driver");

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return;

		}

		System.out.println("PostgreSQL JDBC Driver Registered!");

		Connection connection = null;

		try {
			
			// Fetch Connection Way-1
			/*String url = "jdbc:postgresql://ec2-54-235-152-226.compute-1.amazonaws.com:5432/d5be8rfblcscgv";
			Properties props = new Properties();
			props.setProperty("user","lqvnjqgoybunrx");
			props.setProperty("password","0hl9SRZ5K4ZgqRdtk21Eb4mmMq");
			props.setProperty("ssl","true");
			props.setProperty("sslfactory","org.postgresql.ssl.NonValidatingFactory");
			connection = DriverManager.getConnection(url, props);*/
			
			// Fetch Connection Way-2
			//ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory
			/*connection = DriverManager.getConnection(
					"jdbc:postgresql://ec2-54-235-152-226.compute-1.amazonaws.com:5432/d5be8rfblcscgv", "lqvnjqgoybunrx",
					"0hl9SRZ5K4ZgqRdtk21Eb4mmMq");*/
			connection = DriverManager.getConnection(
					"jdbc:postgresql://ec2-54-235-152-226.compute-1.amazonaws.com:5432/d5be8rfblcscgv?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory", "lqvnjqgoybunrx",
					"0hl9SRZ5K4ZgqRdtk21Eb4mmMq");
			
			/*connection = DriverManager.getConnection(
					"jdbc:postgresql://127.0.0.1:5432/quicknote", "didev",
					"didev123");*/

		} catch (SQLException e) {

			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return;

		}

		if (connection != null) {
			System.out.println("Cong! start using your database now!");
		} else {
			System.out.println("Failed to make connection!");
		}
	}

}