package vn.com.aris;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;

import vn.com.aris.dto.ConfigSaleReportDTO;

public class DataSource {
	private static DataSource datasource;
	private static String userName;
	private static String passName;
	private static String port;
	private static String host;
	private static String nameDb;
	private static String driver;
	private static String jdbc;
	private BasicDataSource ds;

	private DataSource() throws IOException, SQLException,
	PropertyVetoException {
		Properties database = LoadDatabase.getInfomationDatabase();
		ConfigSaleReportDTO configDTO = new ConfigSaleReportDTO();
		configDTO.setUserName(database.getProperty("userName"));
		configDTO.setPassName(database.getProperty("passName"));
		configDTO.setPort(database.getProperty("port"));
		configDTO.setHost(database.getProperty("host"));
		configDTO.setNameDb(database.getProperty("nameDb"));
		configDTO.setDriver(database.getProperty("driver"));
		configDTO.setJdbc(database.getProperty("jdbc"));
		setInfoDb(configDTO);
		ds = new BasicDataSource();
		ds.setDriverClassName(driver);
		ds.setUsername(userName);
		ds.setPassword(passName);
		ds.setUrl(jdbc + "://" + host + ":" + port + "/" + nameDb);

		// the settings below are optional -- dbcp can work with defaults
		ds.setMinIdle(5);
		ds.setMaxIdle(20);
		ds.setMaxOpenPreparedStatements(180);

	}

	public static DataSource getInstance() throws IOException, SQLException,
	PropertyVetoException {
		if (datasource == null) {
			datasource = new DataSource();
			return datasource;
		} else {
			return datasource;
		}
	}

	public Connection getConnection() throws SQLException {
		return this.ds.getConnection();
	}

	public static void setInfoDb(ConfigSaleReportDTO configDTO) {
		userName = configDTO.getUserName();
		passName = configDTO.getPassName();
		port = configDTO.getPort();
		host = configDTO.getHost();
		nameDb = configDTO.getNameDb();
		driver = configDTO.getDriver();
		jdbc = configDTO.getJdbc();
	}

	public static String getDriver() {
		return driver;
	}

	public static String getJdbc() {
		return jdbc;
	}

	public static String getUserName() {
		return userName;
	}

	public static String getPassName() {
		return passName;
	}

	public static DataSource getDatasource() {
		return datasource;
	}

	public static String getPort() {
		return port;
	}

	public static String getHost() {
		return host;
	}

	public static String getNameDb() {
		return nameDb;
	}

	public BasicDataSource getDs() {
		return ds;
	}

}
