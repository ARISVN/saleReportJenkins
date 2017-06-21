package vn.com.aris.dao;

import org.apache.commons.lang.StringUtils;
import vn.com.aris.DataSource;
import vn.com.aris.dao.adapter.ApplicationManagerAdapter;
import vn.com.aris.mapper.db.TblApplication;
import vn.com.aris.mapper.db.TblApplicationItem;
import vn.com.aris.model.Application;
import vn.com.aris.model.Item;
import vn.com.aris.utils.DateUtils;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author nghiant
 * @date Nov 18, 2015
 */
public class ApplicationManagementDao {
	static Connection connection = null;
	static Statement statement = null;
	static PreparedStatement preparedStatement = null;
	ApplicationItemDao itemDao = new ApplicationItemDao();
	ApplicationSaleDao applicationSaleDao = new ApplicationSaleDao();

	/**
	 * 
	 * @param itemCode
	 * @param appId
	 * @return
	 * @throws IOException
	 * @throws PropertyVetoException
	 */

	// tam.ph added - 2015/11/19
	public List<Application> getListApplication(int limit, int offset, String sortColumn, String direction) throws IOException, PropertyVetoException {
		List<Application> app = new ArrayList<>();
		ResultSet resultSet = null;
		ApplicationManagerAdapter appAdap = new ApplicationManagerAdapter();
		String querySQL = "SELECT * FROM " + TblApplication.getTableName();

		try {
			connection = DataSource.getInstance().getConnection();
			if (StringUtils.isNotEmpty(sortColumn)) {
				querySQL += " ORDER BY " + sortColumn + " " + direction + " LIMIT ? OFFSET ?";
				preparedStatement = connection.prepareStatement(querySQL);
				preparedStatement.setInt(1, limit);
				preparedStatement.setInt(2, offset);
			} else {
				querySQL +=" ORDER BY update_date desc" + " LIMIT ? OFFSET ?";
				preparedStatement = connection.prepareStatement(querySQL);
				preparedStatement.setInt(1, limit);
				preparedStatement.setInt(2, offset);
			}
			System.out.println(preparedStatement);
			resultSet = preparedStatement.executeQuery();
			if (resultSet != null) {
				while (resultSet.next()) {
					app.add(appAdap.convertApplicationObject(resultSet));
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (resultSet != null)
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if (statement != null)
				try {
					statement.close();
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
		return app;
	}

	// tam.ph ending

	// tam.ph added - 2015/11/20
	public Boolean updateApplication(Application app) throws IOException, PropertyVetoException {
		ResultSet resultSet = null;
		Boolean UpdateOk = false;
		try {
			if (app != null) {
				String updateTableSQL = "UPDATE " + TblApplication.getTableName() + " SET ";
				if (app.getAppCode() != null) {
					updateTableSQL += TblApplication.APP_CODE + " = '" + app.getAppCode() + "',";
				}
				if (app.getAppName() != null) {
					updateTableSQL += TblApplication.APP_NAME + " = '" + app.getAppName() + "',";
				}
				if (app.getType() != null) {
					updateTableSQL += TblApplication.TYPE + " = " + app.getType() + " ,";
				}
				if (app.getReleaseDate() != null) {
					String date = "'" + DateUtils.ConvertDateToString(app.getReleaseDate()) + "'";

					updateTableSQL += TblApplication.RELEASE_DATE + " = " + DateUtils.combineDateSql(date) + ",";
				}
				if (app.getUpdateStatus() != null)

				{
					updateTableSQL += TblApplication.UPDATE_STATUS + " = '" + app.getUpdateStatus() + "',";
				}
				if (app.getUpdateDate() != null) {
					String date = "'" + DateUtils.ConvertDateToString(app.getUpdateDate()) + "'";
					updateTableSQL += TblApplication.UPDATE_DATE + " = " + DateUtils.combineDateSql(date) + ",";
				}
				if (app.getRevenueItem() > -1) {
					updateTableSQL += TblApplication.REVENUE_ITEM + " = " + app.getRevenueItem() + " ,";
				}
				if (app.getRevenue() > -1) {
					updateTableSQL += TblApplication.REVENUE + " = " + app.getRevenue();
				}

				updateTableSQL += " WHERE id = " + app.getAppId();

				connection = DataSource.getInstance().getConnection();
				preparedStatement = connection.prepareStatement(replaceComma(updateTableSQL));
				preparedStatement.executeUpdate();
				System.out.println(preparedStatement.toString());
				UpdateOk = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (resultSet != null)
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if (statement != null)
				try {
					statement.close();
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
		return UpdateOk;
	}

	// tam.ph ending

	// tam.ph added - 2015/11/20
	private String replaceComma(String sqlQuery) {
		String whereStr = ", WHERE";
		int index = sqlQuery.indexOf(whereStr);
		if (index > 0) {
			sqlQuery = sqlQuery.replace(whereStr, " WHERE");
		}
		return sqlQuery;
	}

	// tam.ph ending

	// tam.ph added - 2015/11/20
	public Application getApplicationByID(int id) throws IOException, PropertyVetoException {
		ResultSet resultSet = null;
		Application app = new Application();
		ApplicationManagerAdapter appAdap = new ApplicationManagerAdapter();
		String querySQL = "SELECT * FROM " + TblApplication.getTableName() + " WHERE Id = ?";
		try {
			connection = DataSource.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(querySQL);
			preparedStatement.setInt(1, id);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				if (resultSet != null) {
					app = appAdap.convertApplicationObject(resultSet);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (resultSet != null)
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if (statement != null)
				try {
					statement.close();
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
		return app;
	}

	// tam.ph ending

	// tam.ph added - 2015/11/23
	public Item getApplicationItemByID(int id) throws IOException, PropertyVetoException {
		ResultSet resultSet = null;
		Item item = new Item();
		ApplicationManagerAdapter appAdap = new ApplicationManagerAdapter();
		String querySQL = "SELECT * FROM " + TblApplicationItem.getTableName() + " WHERE Id = ?";
		try {
			connection = DataSource.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(querySQL);
			preparedStatement.setInt(1, id);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				if (resultSet != null) {
					item = appAdap.convertItemObject(resultSet);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (resultSet != null)
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if (statement != null)
				try {
					statement.close();
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
		return item;
	}

	// tam.ph ending

	// tam.ph added - 2015/11/20
	public Boolean saveApplication(Application app) throws IOException, PropertyVetoException, ParseException {
		Boolean InsertedOk = false;
		String insertTableSQL = "INSERT INTO " + TblApplication.getTableName() + "(" + TblApplication.APP_CODE + "," + TblApplication.APP_NAME + ","
				+ TblApplication.TYPE + "," + TblApplication.RELEASE_DATE + "," + TblApplication.UPDATE_STATUS + "," + TblApplication.UPDATE_DATE + ","
				+ TblApplication.REVENUE_ITEM + "," + TblApplication.REVENUE + ") VALUES" + "(?,?,?,?,?,?,?,?)";
		ResultSet resultSet = null;
		try {
			connection = DataSource.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(insertTableSQL);
			preparedStatement.setString(1, app.getAppCode());
			preparedStatement.setString(2, app.getAppName());
			preparedStatement.setInt(3, app.getType());
			SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd");
			preparedStatement.setString(4, sf.format(app.getReleaseDate()));
			if (app.getUpdateStatus() != null) {
				preparedStatement.setString(5, app.getUpdateStatus());
			} else {
				preparedStatement.setString(5, "0");
			}
			if (app.getUpdateDate() != null) {
				preparedStatement.setString(6, app.getUpdateDate().toString());
			} else {
				preparedStatement.setString(6, null);
			}
			if (app.getRevenueItem() != 0) {
				preparedStatement.setDouble(7, app.getRevenueItem());
			} else {
				preparedStatement.setString(7, null);
			}
			if (app.getRevenue() != 0) {
				preparedStatement.setDouble(8, app.getRevenue());
			} else {
				preparedStatement.setString(8, null);
			}

			preparedStatement.executeUpdate();
			InsertedOk = true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (resultSet != null)
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
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
		return InsertedOk;
	}

	// tam.ph ending

	// tam.ph added - 2015/11/23
	public Boolean saveItem(Item item) throws IOException, PropertyVetoException {

		Boolean InsertedOk = false;
		String insertTableSQL = "INSERT INTO " + TblApplicationItem.getTableName() + "(" + TblApplicationItem.APP_ID + "," + TblApplicationItem.ITEM_CODE + ","
				+ TblApplicationItem.ITEM_NAME + "," + TblApplicationItem.REVENUE + "," + TblApplicationItem.BOUGHT_ITEMS + ") VALUES" + "(?,?,?,?,?)";
		ResultSet resultSet = null;
		try {
			connection = DataSource.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(insertTableSQL);
			preparedStatement.setLong(1, item.getAppId());
			preparedStatement.setString(2, item.getItemCode());
			preparedStatement.setString(3, item.getItemName());
			if (item.getRevenue() != 0) {
				preparedStatement.setDouble(4, item.getRevenue());
			} else {
				preparedStatement.setDouble(4, 0);
			}
			if (item.getBoughtItems() != 0) {
				preparedStatement.setInt(5, item.getBoughtItems());
			} else {
				preparedStatement.setInt(5, 0);
			}
			preparedStatement.executeUpdate();
			InsertedOk = true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (resultSet != null)
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
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
		return InsertedOk;
	}

	// tam.ph ending

	// tam.ph added - 2015/11/23
	public Boolean updateItem(Item item) throws IOException, PropertyVetoException {

		ResultSet resultSet = null;
		Boolean UpdateOk = false;
		try {
			if (item != null) {
				String updateTableSQL = "UPDATE " + TblApplicationItem.getTableName() + " SET ";
				if (item.getAppId() != null) {
					updateTableSQL += TblApplicationItem.APP_ID + " = " + item.getAppId() + " ,";
				}
				if (item.getItemCode() != null) {
					updateTableSQL += TblApplicationItem.ITEM_CODE + " = '" + item.getItemCode() + "',";
				}
				if (item.getItemCode() != null) {
					updateTableSQL += TblApplicationItem.ITEM_NAME + " = '" + item.getItemName() + "',";
				}
				if (item.getRevenue() > -1) {
					updateTableSQL += TblApplicationItem.REVENUE + " = " + item.getRevenue() + " ,";
				}
				if (item.getBoughtItems() > -1) {
					updateTableSQL += TblApplicationItem.BOUGHT_ITEMS + " = " + item.getBoughtItems() + " ,";
				}

				updateTableSQL += " WHERE id = " + item.getId();

				connection = DataSource.getInstance().getConnection();
				preparedStatement = connection.prepareStatement(replaceComma(updateTableSQL));
				preparedStatement.executeUpdate();
				UpdateOk = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (resultSet != null)
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if (statement != null)
				try {
					statement.close();
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
		return UpdateOk;
	}

	// tam.ph ending

	// tam.ph added 2015/11/19
	public Integer getTotalApplication() throws IOException, PropertyVetoException {
		ResultSet resultSet = null;
		Integer total = 0;
		String querySQL = "SELECT COUNT(" + TblApplication.ID + ") AS total FROM " + TblApplication.getTableName();
		try {
			connection = DataSource.getInstance().getConnection();
			statement = connection.prepareStatement(querySQL);
			resultSet = statement.executeQuery(querySQL);
			while (resultSet.next()) {
				if (resultSet != null) {
					total = resultSet.getInt("total");
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (resultSet != null)
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if (statement != null)
				try {
					statement.close();
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
		return total;
	}

	// tam.ph ending

	// tam.ph added - 2015/11/19
	public List<Application> getListApplication() throws IOException, PropertyVetoException {
		List<Application> app = new ArrayList<>();
		ResultSet resultSet = null;
		ApplicationManagerAdapter appAdap = new ApplicationManagerAdapter();
		String querySQL = "SELECT * FROM " + TblApplication.getTableName() + " ORDER BY " + TblApplication.APP_NAME + " ASC";

		try {
			connection = DataSource.getInstance().getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(querySQL);
			if (resultSet != null) {
				while (resultSet.next()) {
					app.add(appAdap.convertApplicationObject(resultSet));
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (resultSet != null)
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if (statement != null)
				try {
					statement.close();
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
		return app;
	}

	// tam.ph ending

	public List<Application> getAllGettingDataApp() throws IOException, PropertyVetoException {
		ResultSet resultSet = null;
		List<Application> app = new ArrayList<>();
		String querySQL = "SELECT * FROM " + TblApplication.getTableName() + " WHERE update_status = 1";
		try {
			connection = DataSource.getInstance().getConnection();
			statement = connection.prepareStatement(querySQL);
			resultSet = statement.executeQuery(querySQL);
			while (resultSet.next()) {
				if (resultSet != null) {
					ApplicationManagerAdapter appAdap = new ApplicationManagerAdapter();
					app.add(appAdap.convertApplicationObject(resultSet));
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (resultSet != null)
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if (statement != null)
				try {
					statement.close();
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
		return app;
	}

	public List<Application> getAllApplication() throws IOException, PropertyVetoException {
		ResultSet resultSet = null;
		List<Application> app = new ArrayList<>();
		String querySQL = "SELECT * FROM " + TblApplication.getTableName()+" order by "+ TblApplication.APP_NAME;
		try {
			connection = DataSource.getInstance().getConnection();
			statement = connection.prepareStatement(querySQL);
			resultSet = statement.executeQuery(querySQL);
			while (resultSet.next()) {
				if (resultSet != null) {
					ApplicationManagerAdapter appAdap = new ApplicationManagerAdapter();
					app.add(appAdap.convertApplicationObject(resultSet));
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (resultSet != null)
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if (statement != null)
				try {
					statement.close();
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
		return app;
	}

	public String deleteApplication(int idApp) throws IOException, PropertyVetoException {
		String msg = "Delete success!";
		String msgFal = "Delete failed!";
		itemDao.deleteLstItemByIdApp(idApp);// delete list Item
		applicationSaleDao.deleteAllApplicationSaleDailyByIdApp(idApp);
		applicationSaleDao.deleteAllApplicationSaleYearlyByIdApp(idApp);
		String deleteTableSQL = "DELETE FROM " + TblApplication.getTableName() + " WHERE " + TblApplication.ID + "=?";
		try {
			connection = DataSource.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(deleteTableSQL);
			preparedStatement.setLong(1, idApp);
			int executeUpdate = preparedStatement.executeUpdate();
			if (executeUpdate > 0) {
				return msg;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(deleteTableSQL);
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
		return msgFal;
	}
}
