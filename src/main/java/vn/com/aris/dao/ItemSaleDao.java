package vn.com.aris.dao;

import vn.com.aris.DataSource;
import vn.com.aris.mapper.db.TblApplicationSaleDaily;
import vn.com.aris.mapper.db.TblApplicationSaleYearly;
import vn.com.aris.mapper.db.TblItemSaleDaily;
import vn.com.aris.mapper.db.TblItemSaleYearly;
import vn.com.aris.model.ItemSaleDaily;
import vn.com.aris.model.ItemSaleYearly;
import vn.com.aris.utils.DateUtils;

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

/**
 * @author nghiant
 * @date Nov 18, 2015
 */
public class ItemSaleDao {
	static Connection connection = null;
	static PreparedStatement preparedStatement = null;

	public static void saveLstItemSaleDaily(List<ItemSaleDaily> lstItemSaleDaily) throws IOException, PropertyVetoException {
		Calendar calender;
		for (ItemSaleDaily itemReport : lstItemSaleDaily) {
			calender = DateUtils.getCalenderFromDate(itemReport.getDate());
			itemReport.setDay(calender.get(Calendar.DAY_OF_MONTH));
			itemReport.setWeek(calender.get(Calendar.WEEK_OF_YEAR));
			itemReport.setMonth(calender.get(Calendar.MONTH));
			itemReport.setYear(calender.get(Calendar.YEAR));
			//
			List<Date> lstDate=getLstItemSaleDailyByItemId(itemReport.getItemId());
			if(!lstDate.contains(itemReport.getDate())){
				saveItemSaleDaily(itemReport);
			}
		}
	}
//
	private static List<Date> getLstItemSaleDailyByItemId(Long itemId) throws IOException, PropertyVetoException {
		List<Date> lstRs = new ArrayList<>();
		ResultSet resultSet = null;
		String querySQL = "SELECT * FROM " + TblItemSaleDaily.getTableName()+" WHERE "+TblItemSaleDaily.ITEM_ID+" =?";
		try {
			connection = DataSource.getInstance().getConnection();
			preparedStatement=connection.prepareStatement(querySQL);
			preparedStatement.setLong(1, itemId);
			resultSet = preparedStatement.executeQuery();
			if (resultSet != null) {
				while (resultSet.next()) {
					lstRs.add(resultSet.getDate(TblApplicationSaleDaily.DATE));
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

	private static void saveItemSaleDaily(ItemSaleDaily itemReport) throws IOException, PropertyVetoException {
		String insertTableSQL = "INSERT INTO " + TblItemSaleDaily.getTableName() + "(" + TblItemSaleDaily.ITEM_ID + "," + TblItemSaleDaily.DATE + ","

//		+ TblItemSaleDaily.DAY + "," + TblItemSaleDaily.WEEK + "," + TblItemSaleDaily.MONTH + "," + TblItemSaleDaily.YEAR + ","

		+ TblItemSaleDaily.BOUGHT_NUMBER + "," + TblItemSaleDaily.SALE + "," + TblItemSaleDaily.CURRENCY + "," + TblItemSaleDaily.SALE_CURRENCY + ") VALUES"
				+ "(?,?,?,?,?,?)";
		ResultSet resultSet = null;
		try {
			connection = DataSource.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(insertTableSQL);
			preparedStatement.setLong(1, itemReport.getItemId());
			preparedStatement.setDate(2, itemReport.getDate());

//			preparedStatement.setInt(3, itemReport.getDay());
//			preparedStatement.setInt(4, itemReport.getWeek());
//			preparedStatement.setInt(5, itemReport.getMonth());
//			preparedStatement.setInt(6, itemReport.getYear());

			preparedStatement.setInt(3, itemReport.getBoughtNumber());
			preparedStatement.setDouble(4, itemReport.getSale());
			preparedStatement.setString(5, itemReport.getCurrency());
			preparedStatement.setDouble(6, itemReport.getSaleCurrency());

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

	// Can kiem tra itemreport co idCode null
	public static void saveLstItemSaleYearly(List<ItemSaleYearly> lstItemSaleYear) throws IOException, PropertyVetoException {
		for (ItemSaleYearly itemReport : lstItemSaleYear) {
			List<Integer> lstRs=getListItemSaleYearlyByItemId(itemReport.getItemId());
			if(lstRs.contains(itemReport.getYear())){
				saveItemSaleYearly(itemReport);
			}
		}
	}

	private static List<Integer> getListItemSaleYearlyByItemId(Long itemId) throws IOException, PropertyVetoException {
		List<Integer> lstRs = new ArrayList<>();
		ResultSet resultSet = null;
		String querySQL = "SELECT * FROM " + TblItemSaleYearly.getTableName()+" WHERE "+TblItemSaleYearly.ITEM_ID+" =?";
		try {
			connection = DataSource.getInstance().getConnection();
			preparedStatement=connection.prepareStatement(querySQL);
			preparedStatement.setLong(1, itemId);
			resultSet = preparedStatement.executeQuery();
			if (resultSet != null) {
				while (resultSet.next()) {
					lstRs.add(resultSet.getInt(TblItemSaleYearly.YEAR));
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
	private static void saveItemSaleYearly(ItemSaleYearly itemReport) throws IOException, PropertyVetoException {

		String insertTableSQL = "INSERT INTO " + TblItemSaleYearly.getTableName() + "(" + TblItemSaleYearly.ITEM_ID + "," + TblItemSaleYearly.YEAR + ","
				+ TblItemSaleYearly.BOUGHT_NUMBER + "," + TblItemSaleYearly.SALE + "," + TblItemSaleYearly.CURRENCY + "," + TblItemSaleYearly.SALE_CURRENCY
				+ ") VALUES" + "(?,?,?,?,?,?)";
		ResultSet resultSet = null;
		try {
			connection = DataSource.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(insertTableSQL);
			preparedStatement.setLong(1, itemReport.getItemId());
			preparedStatement.setInt(2, itemReport.getYear());
			preparedStatement.setInt(3, itemReport.getBoughtNumber());
			preparedStatement.setDouble(4, itemReport.getSale());
			preparedStatement.setString(5, itemReport.getCurrency());
			preparedStatement.setDouble(6, itemReport.getSaleCurrency());
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

	public void deleteAllItemSaleDailyByIdItem(Long idItem) throws IOException, PropertyVetoException {
		String deleteTableSQL = "DELETE FROM " + TblItemSaleDaily.getTableName() + " WHERE " + TblItemSaleDaily.ITEM_ID + "=?";
		try {
			connection = DataSource.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(deleteTableSQL);
			preparedStatement.setLong(1, idItem);
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

	public void deleteItemSaleDailyById(Long idItemSale) throws IOException, PropertyVetoException {
		String deleteTableSQL = "DELETE FROM " + TblItemSaleDaily.getTableName() + " WHERE " + TblItemSaleDaily.ID + "=?";
		try {
			connection = DataSource.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(deleteTableSQL);
			preparedStatement.setLong(1, idItemSale);
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

	public List<Long> getListIdItemSaleDailyByIdItem(Long id) throws IOException, PropertyVetoException {
		List<Long> lstRs = new ArrayList<Long>();
		ResultSet resultSet = null;
		String querySQL = " select  *  FROM " + TblItemSaleDaily.getTableName() + "  where " + TblItemSaleDaily.ITEM_ID + "=?";
		try {
			connection = DataSource.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(querySQL);
			preparedStatement.setLong(1, id);
			resultSet = preparedStatement.executeQuery();
			if (resultSet != null) {
				while (resultSet.next()) {
					lstRs.add(resultSet.getLong(TblItemSaleDaily.ID));
				}
			}
			return lstRs;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				if (connection != null) {
					try {
						connection.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}

		}
		return lstRs;
	}

	public void deleteAllItemSaleYearly(Long idItem) throws IOException, PropertyVetoException {
		String deleteTableSQL = "DELETE FROM " + TblItemSaleYearly.getTableName() + " WHERE " + TblItemSaleYearly.ITEM_ID + "=?";
		try {
			connection = DataSource.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(deleteTableSQL);
			preparedStatement.setLong(1, idItem);
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

	public void deleteItemSaleYearlyById(Long idItemSaleYear) throws IOException, PropertyVetoException {
		String deleteTableSQL = "DELETE FROM " + TblItemSaleYearly.getTableName() + " WHERE " + TblItemSaleYearly.ID + "=?";
		try {
			connection = DataSource.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(deleteTableSQL);
			preparedStatement.setLong(1, idItemSaleYear);
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

	public List<Long> getListIdItemSaleYearlyByIdItem(Long idItem) throws IOException, PropertyVetoException {
		List<Long> lstRs = new ArrayList<Long>();
		ResultSet resultSet = null;
		String querySQL = " select  *  FROM " + TblItemSaleYearly.getTableName() + "  where " + TblItemSaleYearly.ITEM_ID + "=?";
		try {
			connection = DataSource.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(querySQL);
			preparedStatement.setLong(1, idItem);
			resultSet = preparedStatement.executeQuery();
			if (resultSet != null) {
				while (resultSet.next()) {
					lstRs.add(resultSet.getLong(TblItemSaleYearly.ID));
				}
			}
			return lstRs;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				if (connection != null) {
					try {
						connection.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}

		}
		return lstRs;
	}
	public static void main(String[] args) throws IOException, PropertyVetoException {
		List<Date> lstItemSaleDailyByItemId = getLstItemSaleDailyByItemId(74L);
		System.out.println(lstItemSaleDailyByItemId);
	}

}
