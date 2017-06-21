package vn.com.aris;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class JdbcConnector {

	private static String getUrl() {
		StringBuilder builder = new StringBuilder();
		builder.append(DataSource.getJdbc());
		builder.append("://");
		builder.append(DataSource.getHost());
		builder.append(":");
		builder.append(DataSource.getPort());
		builder.append("/");
		builder.append(DataSource.getNameDb());
		return builder.toString();
	}

	/**
	 *
	 * @method getLastestFileName
	 * @description
	 * @author User
	 * @date Oct 28, 2015
	 * @param type
	 *            : 0 => play store, 1 => appstore
	 * @return String
	 */
	public static String getLastestFileName(int type) {

		callClassForName();
		try (

				Connection conn = DriverManager.getConnection(getUrl(),
				DataSource.getUserName(), DataSource.getPassName()); // jenkinsPlugin
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt
						.executeQuery("SELECT * FROM update_info WHERE type = "
								+ type + " ORDER BY timestamp DESC limit 1;");

				) {

			if (rs.last()) {
				System.out.println(rs.getString("file_name"));
				return rs.getString("file_name");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 *
	 * @method addUpdateInfo
	 * @description
	 * @author User
	 * @date Oct 28, 2015
	 * @param type
	 *            : 0 => play store, 1 => appstore
	 * @return boolean
	 */
	public static boolean addUpdateInfo(String fileName, int type) {
		callClassForName();
		try (Connection conn = DriverManager.getConnection(getUrl(),
				DataSource.getUserName(), DataSource.getPassName()); // jenkinsPlugin
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt
						.executeQuery("SELECT * FROM update_info WHERE type = 0 ORDER BY timestamp DESC");) {

			String sqlQuery = "INSERT INTO update_info (file_name, type ) VALUES (";
			sqlQuery += "'" + fileName + "',";
			sqlQuery += type + ")";
			stmt.executeUpdate(sqlQuery);
			return true;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void callClassForName() {
		try {
			Class.forName(DataSource.getDriver()); // for MS Access/Excel
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
