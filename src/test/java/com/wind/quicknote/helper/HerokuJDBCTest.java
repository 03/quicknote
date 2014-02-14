package com.wind.quicknote.helper;

import static org.junit.Assert.assertNotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.junit.BeforeClass;
import org.junit.Test;

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
	
	private static final String ORG_POSTGRESQL_DRIVER = "org.postgresql.Driver";
	private static final String FAILED_TO_MAKE_CONNECTION = "Failed to make connection!";
	private static final String CONNECTION_ESTABLISHED = "Cong! Start using your database now!";
	private static final String CONNECTION_FAILED = "Connection Failed! Check output console";

	@BeforeClass
	public static void beforeClass() {
		
		try {
			Class.forName(ORG_POSTGRESQL_DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return;
		}

		System.out.println("PostgreSQL JDBC Driver Registered!");
	}
	
	@Test
	public void testConnWithProps() {

		Connection connection = null;
		try {
			String url = "jdbc:postgresql://ec2-54-197-237-120.compute-1.amazonaws.com:5432/d1nq4hvfcvj4g9";
			Properties props = new Properties();
			props.setProperty("user", "fwhamjopujjwoi");
			props.setProperty("password", "58nIPmzYFXzGChEZAb0l9lOLyr");
			props.setProperty("ssl", "true");
			props.setProperty("sslfactory",
					"org.postgresql.ssl.NonValidatingFactory");
			connection = DriverManager.getConnection(url, props);

		} catch (SQLException e) {
			System.out.println(CONNECTION_FAILED);
			e.printStackTrace();
		}

		assertNotNull(connection);
		if (connection != null) {
			System.out.println(CONNECTION_ESTABLISHED);

			try {
				connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			connection = null;

		} else {
			System.out.println(FAILED_TO_MAKE_CONNECTION);
		}

	}
	
	@Test
	public void testConnWithSSLEnabled() {

		Connection connection = null;
		try {
			connection = DriverManager.getConnection(
					"jdbc:postgresql://ec2-54-197-237-120.compute-1.amazonaws.com:5432/d1nq4hvfcvj4g9?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory", "fwhamjopujjwoi",
					"58nIPmzYFXzGChEZAb0l9lOLyr");
			
		} catch (SQLException e) {
			System.out.println(CONNECTION_FAILED);
			e.printStackTrace();
		}

		assertNotNull(connection);
		if (connection != null) {
			System.out.println(CONNECTION_ESTABLISHED);
			try {
				connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			connection = null;

		} else {
			System.out.println(FAILED_TO_MAKE_CONNECTION);
		}

	}
	
	@Test
	public void testLocalConn() {

		Connection connection = null;
		try {
			
			connection = DriverManager.getConnection(
					"jdbc:postgresql://127.0.0.1:5432/quicknote", "didev",
					"didev123");

		} catch (SQLException e) {
			System.out.println(CONNECTION_FAILED);
			e.printStackTrace();
		}

		assertNotNull(connection);
		if (connection != null) {
			System.out.println(CONNECTION_ESTABLISHED);

			try {
				connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			connection = null;

		} else {
			System.out.println(FAILED_TO_MAKE_CONNECTION);
		}

	}
	
	
	public static void main(String[] argv) {
		
		try {
			Class.forName(ORG_POSTGRESQL_DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return;
		}

		Connection connection = null;
		try {
			connection = DriverManager.getConnection(
					"jdbc:postgresql://ec2-54-197-237-120.compute-1.amazonaws.com:5432/d1nq4hvfcvj4g9?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory", "fwhamjopujjwoi",
					"58nIPmzYFXzGChEZAb0l9lOLyr");
			
		} catch (SQLException e) {
			System.out.println(CONNECTION_FAILED);
			e.printStackTrace();
		}

		if (connection != null) {
			System.out.println(CONNECTION_ESTABLISHED);
		} else {
			System.out.println(FAILED_TO_MAKE_CONNECTION);
		}
	}

}