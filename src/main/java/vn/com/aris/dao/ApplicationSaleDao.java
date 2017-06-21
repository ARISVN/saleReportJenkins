package vn.com.aris.dao;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import vn.com.aris.DataSource;
import vn.com.aris.mapper.db.TblApplicationSaleDaily;
import vn.com.aris.mapper.db.TblApplicationSaleYearly;
import vn.com.aris.mapper.db.TblItemSaleDaily;
import vn.com.aris.model.AppSaleDaily;
import vn.com.aris.model.AppSaleYearly;
import vn.com.aris.utils.DateUtils;
/**
* @author  nghiant
* @date Nov 18, 2015
*/
public class ApplicationSaleDao {
	static Connection connection = null;
	static PreparedStatement preparedStatement = null;
	/**
	 * @method saveApplicationSaleDaily
	 * @param AppSaleDaily appSaleDaily of tbl_application_sale_daily
	 * @description insert one record to  tbl_application_sale_daily 
	 * @author nghiant
	 * @date Nov 18, 2015
	 */
	public static void saveApplicationSaleDaily(AppSaleDaily appSaleDaily)
			throws IOException, PropertyVetoException {
		String insertTableSQL = "INSERT INTO "
				+ TblApplicationSaleDaily.getTableName() + "("
				+ TblApplicationSaleDaily.APP_ID + ","
				+ TblApplicationSaleDaily.DATE + ","
//				+ TblApplicationSaleDaily.DAY + ","
//				+ TblApplicationSaleDaily.WEEK + ","
//				+ TblApplicationSaleDaily.MONTH + ","
//				+ TblApplicationSaleDaily.YEAR + ","
				+ TblApplicationSaleDaily.NUMBER_INSTALL + ","
				+ TblApplicationSaleDaily.SALE + ","
				+ TblApplicationSaleDaily.CURRENCY + ","
				+ TblApplicationSaleDaily.SALE_CURRENCY + ") VALUES"
				+ "(?,?,?,?,?,?)";
		ResultSet resultSet = null;
		try {
			connection = DataSource.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(insertTableSQL);
			preparedStatement.setLong(1, appSaleDaily.getAppId());
			preparedStatement.setDate(2, appSaleDaily.getDate());
			
//			preparedStatement.setInt(3,  appSaleDaily.getDay());
//			preparedStatement.setInt(4, appSaleDaily.getWeek());
//			preparedStatement.setInt(5, appSaleDaily.getMonth());
//			preparedStatement.setInt(6, appSaleDaily.getYear());
			
			preparedStatement.setLong(3, appSaleDaily.getNumberInstall());
			preparedStatement.setDouble(4, appSaleDaily.getSale());
			preparedStatement.setString(5, appSaleDaily.getCurrency());
			preparedStatement.setDouble(6, 1.0);

			preparedStatement.executeUpdate();
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
	}

	public static void saveLstApplicationSaleDaily(
			List<AppSaleDaily> lstAppSaleDaily) throws IOException,
			PropertyVetoException {
		Calendar calender;
		for (AppSaleDaily app : lstAppSaleDaily) {
			calender = DateUtils.getCalenderFromDate(app.getDate());
			app.setDay(calender.get(Calendar.DAY_OF_MONTH));
			app.setWeek(calender.get(Calendar.WEEK_OF_YEAR));
			app.setMonth(calender.get(Calendar.MONTH));
			app.setYear(calender.get(Calendar.YEAR));
			//query from table tbl_application_sale_daily by app-id.
			//List<AppSaleDaily> contain ngay nay thi ko luu
			List<Date> lstDateSaved= getListDateByAppId(app.getAppId());
			if(!lstDateSaved.contains(app.getDate())){
				saveApplicationSaleDaily(app);
			}
		}
	}

	private static List<Date> getListDateByAppId(Long appId) throws IOException, PropertyVetoException {
		List<Date> rs = new ArrayList<>();
		ResultSet resultSet = null;
		String querySQL = "SELECT * FROM " + TblApplicationSaleDaily.getTableName()+" WHERE "+TblApplicationSaleDaily.APP_ID+" =?";
		try {
			connection = DataSource.getInstance().getConnection();
			preparedStatement=connection.prepareStatement(querySQL);
			preparedStatement.setLong(1, appId);
			resultSet = preparedStatement.executeQuery();
			if (resultSet != null) {
				while (resultSet.next()) {
					rs.add(resultSet.getDate(TblApplicationSaleDaily.DATE));
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
		
		return rs;
	}

	public static void saveLstApplicationSaleYearly(
			List<AppSaleYearly> lstAppSaleYear) throws IOException,
			PropertyVetoException {
		for (AppSaleYearly app : lstAppSaleYear) {
			List<Integer> lstDate=getLstApplicationSaleYearlyByIdApp(app.getAppId());
			if(!lstDate.contains(app.getYear())){
				saveApplicationSaleYear(app);
			}
		}
	}

	private static List<Integer> getLstApplicationSaleYearlyByIdApp(Long appId) throws IOException, PropertyVetoException {

		List<Integer> lstRs = new ArrayList<>();
		ResultSet resultSet = null;
		String querySQL = "SELECT * FROM " + TblApplicationSaleYearly.getTableName()+" WHERE "+TblApplicationSaleYearly.APP_ID+" =?";
		try {
			connection = DataSource.getInstance().getConnection();
			preparedStatement=connection.prepareStatement(querySQL);
			preparedStatement.setLong(1, appId);
			resultSet = preparedStatement.executeQuery();
			if (resultSet != null) {
				while (resultSet.next()) {
					lstRs.add(resultSet.getInt(TblApplicationSaleYearly.YEAR));
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
		return lstRs;
	
	}

	private static void saveApplicationSaleYear(AppSaleYearly appSaleYearly)
			throws IOException, PropertyVetoException {
		String insertTableSQL = "INSERT INTO "
				+ TblApplicationSaleYearly.getTableName() + "("
				+ TblApplicationSaleYearly.APP_ID + ","
				+ TblApplicationSaleYearly.YEAR + ","
				+ TblApplicationSaleYearly.NUMBER_INSTALL + ","
				+ TblApplicationSaleYearly.SALE + ","
				+ TblApplicationSaleYearly.CURRENCY + ","
				+ TblApplicationSaleYearly.SALE_CURRENCY + ") VALUES"
				+ "(?,?,?,?,?,?)";
		ResultSet resultSet = null;
		try {
			connection = DataSource.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(insertTableSQL);
			preparedStatement.setLong(1, appSaleYearly.getAppId());
			preparedStatement.setInt(2, appSaleYearly.getYear());
			preparedStatement.setLong(3, appSaleYearly.getNumberInstall());
			preparedStatement.setDouble(4, appSaleYearly.getSale());
			preparedStatement.setString(5, appSaleYearly.getCurrency());
			preparedStatement.setDouble(6, 1.0);

			preparedStatement.executeUpdate();
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
	}

	public void deleteAllApplicationSaleDailyByIdApp(int idApp) throws IOException, PropertyVetoException {
		String deleteTableSQL = "DELETE FROM " + TblApplicationSaleDaily.getTableName() + " WHERE " + TblApplicationSaleDaily.APP_ID + "=?";
		try {
			connection = DataSource.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(deleteTableSQL);
			preparedStatement.setLong(1, idApp);
			int executeUpdate = preparedStatement.executeUpdate();
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
	}

	public void deleteAllApplicationSaleYearlyByIdApp(int idApp) throws IOException, PropertyVetoException {
		String deleteTableSQL = "DELETE FROM " + TblApplicationSaleYearly.getTableName() + " WHERE " + TblApplicationSaleYearly.APP_ID + "=?";
		try {
			connection = DataSource.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(deleteTableSQL);
			preparedStatement.setLong(1, idApp);
			int executeUpdate = preparedStatement.executeUpdate();
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
	}
	public static void main(String[] args) throws IOException, PropertyVetoException {
//		List<Date> listDateByAppId = getListDateByAppId(59L);
//		System.out.println(listDateByAppId.size());
		List<Integer> lstApplicationSaleYearlyByIdApp = getLstApplicationSaleYearlyByIdApp(92L);
		System.out.println(lstApplicationSaleYearlyByIdApp);
	}
}
