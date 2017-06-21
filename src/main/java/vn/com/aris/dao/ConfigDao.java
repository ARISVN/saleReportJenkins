package vn.com.aris.dao;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import vn.com.aris.DataSource;
import vn.com.aris.dao.adapter.ConfigAdapter;
import vn.com.aris.mapper.db.TblApplicationItem;
import vn.com.aris.mapper.db.TblConfig;
import vn.com.aris.model.Config;

public class ConfigDao {
	static Connection connection = null;
	static PreparedStatement preparedStatement = null;

	public Config getConfig() {
		ConfigAdapter configAdapter = new ConfigAdapter();
		Config config = null;
		String sql = "SELECT * FROM " + TblConfig.getTableName() + " LIMIT 1";
		try (Connection connection = DataSource.getInstance().getConnection();
				PreparedStatement stmt = connection.prepareStatement(sql);
				ResultSet rset = stmt.executeQuery();) {
			if (rset.last()) {
				config = configAdapter.convertConfigObject(rset);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return config;
	}

	public static void saveConfig(Config config) throws IOException, PropertyVetoException {
		String insertTableSQL = "INSERT INTO " + TblConfig.getTableName() + "(" + TblConfig.GOOGLE_PATH + "," + TblConfig.GOOGLE_DATA_PATH + ","
				+ TblConfig.GOOGLE_NUMBER + "," + TblConfig.APPLE_PATH + "," + TblConfig.APPLE_VENDOR_ID + ") VALUES" + "(?,?,?,?,?)";
		try {
			connection = DataSource.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(insertTableSQL);
			preparedStatement.setString(1, config.getGooglePath());
			preparedStatement.setString(2, config.getGoogleDataPath());
			preparedStatement.setString(3, config.getGoogleNumber());
			preparedStatement.setString(4, config.getApplePath());
			preparedStatement.setString(5, config.getAppleVendorId());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (preparedStatement != null)
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if (connection != null)
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	}

	public static void main(String[] args) {

	}

	public static void updateConfig(Config config) throws IOException, PropertyVetoException {
		String insertTableSQL = "UPDATE " + TblConfig.getTableName() + " SET " + TblConfig.APPLE_PATH + "=?," + TblConfig.APPLE_VENDOR_ID + "=?,"
				+ TblConfig.GOOGLE_DATA_PATH + "=?," + TblConfig.GOOGLE_NUMBER + "=?," + TblConfig.GOOGLE_PATH + "=? WHERE " + TblApplicationItem.ID + "=?";
		try {
			connection = DataSource.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(insertTableSQL);
			preparedStatement.setString(1, config.getApplePath());
			preparedStatement.setString(2, config.getAppleVendorId());
			preparedStatement.setString(3, config.getGoogleDataPath());
			preparedStatement.setString(4, config.getGoogleNumber());
			preparedStatement.setString(5, config.getGooglePath());
			preparedStatement.setLong(6, config.getId());
			int executeUpdate = preparedStatement.executeUpdate();
			System.out.println(preparedStatement);
			if (executeUpdate > 0) {
				System.out.println("Update config success!!!");
			} else {
				System.out.println("Update config failed!!!");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(insertTableSQL);
		} finally {
			if (preparedStatement != null)
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if (connection != null)
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}

	}

}
