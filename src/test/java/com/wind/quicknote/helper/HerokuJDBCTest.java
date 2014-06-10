package com.wind.quicknote.helper;

import static org.junit.Assert.assertNotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * How to deploy java app to heroku
 * 
 * http://books.zkoss.org/wiki/ZK%20Installation%20Guide/Setting%20up%20Servers/
 * 
 * Heroku
 * https://devcenter.heroku.com/articles/heroku-postgresql#connecting-in-java
 * http://blog.csdn.net/xianqiang1/article/details/8589998
 * http://jdbc.postgresql.org/documentation/head/connect.html
 * 
 */
public class HerokuJDBCTest {

	private static final String ORG_POSTGRESQL_DRIVER = "org.postgresql.Driver";
	private static final String _DB_URL = "jdbc:postgresql://ec2-54-197-237-120.compute-1.amazonaws.com:5432/d1nq4hvfcvj4g9";
	private static final String _DB_USER = "fwhamjopujjwoi";
	private static final String _DB_PASS = "58nIPmzYFXzGChEZAb0l9lOLyr";
	
	private static HerokuJDBCTest _self = new HerokuJDBCTest();

	@BeforeClass
	public static void beforeClass() {

		try {
			Class.forName(ORG_POSTGRESQL_DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return;
		}
	}

	@Test
	public void testConnWithProps() {

		Connection connection = null;
		try {
			String url = _DB_URL;
			Properties props = new Properties();
			props.setProperty("user", _DB_USER);
			props.setProperty("password", _DB_PASS);
			props.setProperty("ssl", "true");
			props.setProperty("sslfactory",
					"org.postgresql.ssl.NonValidatingFactory");
			connection = DriverManager.getConnection(url, props);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		assertNotNull(connection);
		try {
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testConnWithSSLEnabled() {

		Connection connection = null;
		try {
			connection = DriverManager
					.getConnection(
							_DB_URL + "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory",
							_DB_USER, _DB_PASS);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		assertNotNull(connection);
		try {
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
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
			e.printStackTrace();
		}

		assertNotNull(connection);
		try {
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] argv) throws ClassNotFoundException {

		Class.forName(ORG_POSTGRESQL_DRIVER);
		_self.testConnWithSSLEnabled();
		
	}

}