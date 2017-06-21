package vn.com.aris.dao;

import vn.com.aris.DataSource;
import vn.com.aris.constant.Constant;
import vn.com.aris.mapper.db.TblApplicationItem;
import vn.com.aris.model.DataOnRow;
import vn.com.aris.model.Item;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author nghiant
 * @date Nov 18, 2015
 */
public class ApplicationItemDao {
	static Connection connection = null;
	static Statement statement = null;
	static PreparedStatement preparedStatement = null;
	ItemSaleDao itemSaleDao = new ItemSaleDao();
	/**
	 * @method getItemIdByItemCode
	 * @param itemCode
	 *            of tbl_application_item
	 * @description Get id of tbl_application_item by itemCode
	 * @author nghiant
	 * @param appId
	 * @date Nov 18, 2015
	 * @return -1 if not find,else return id of tbl_application_item
	 */
	public static Long getItemIdByItemCode(String itemCode, Long appId) throws IOException, PropertyVetoException {
		Long itemId = -1L;
		String querySQL = "select " + TblApplicationItem.ID + " from " + TblApplicationItem.getTableName() + " where " + TblApplicationItem.ITEM_CODE
				+ "=? AND " + TblApplicationItem.APP_ID + "=?";
		ResultSet resultSet = null;
		try {
			connection = DataSource.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(querySQL);
			preparedStatement.setString(1, itemCode);
			preparedStatement.setLong(2, appId);
			resultSet = preparedStatement.executeQuery();
			if (resultSet != null) {
				while (resultSet.next()) {
					itemId = resultSet.getLong(TblApplicationItem.ID);
				}
			}

			return itemId;

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

			if (statement != null) {
				try {
					statement.close();
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
		return null;
	}

	
	/**
	 * 
	 * @method getItemNameByItemId
	 * @description Get item name from item id
	 * @author dung,dd
	 * @date Nov 30, 2015
	 * @param
	 * @return String
	 */
	public String getItemNameByItemId(Integer itemId) throws IOException, PropertyVetoException {
	    String itemName = "";
        String querySQL = "SELECT " + TblApplicationItem.ITEM_NAME + " FROM " + TblApplicationItem.getTableName() + " WHERE " + TblApplicationItem.ID
                + "=?";
        ResultSet resultSet = null;
        try {
            connection = DataSource.getInstance().getConnection();
            preparedStatement = connection.prepareStatement(querySQL);
            preparedStatement.setInt(1, itemId);
            resultSet = preparedStatement.executeQuery();
            if (resultSet != null) {
                while (resultSet.next()) {
                    itemName = resultSet.getString(TblApplicationItem.ITEM_NAME);
                }
            }
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

            if (statement != null) {
                try {
                    statement.close();
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
        return itemName;
    }
	/**
	 * hung.td Get list application_item by appID
	 * 
	 * @param appID
	 *            : ApplicationID
	 * @return
	 * @throws IOException
	 * @throws PropertyVetoException
	 */
	public List<Item> getListItemByAppID(int appID) throws IOException, PropertyVetoException {
		List<Item> list = new ArrayList<Item>();
		ResultSet resultSet = null;
		try {
			connection = DataSource.getInstance().getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT item_code, item_name, revenue, bought_items, id FROM tbl_application_item WHERE app_id = " + appID);
			if (resultSet.isBeforeFirst()) {
				while (resultSet.next()) {
					Item item = new Item();
					item.setItemCode(resultSet.getString("item_code"));
					item.setItemName(resultSet.getString("item_name"));
					item.setRevenue(resultSet.getDouble("revenue"));
					item.setBoughtItems(resultSet.getInt("bought_items"));
					item.setId(resultSet.getLong(TblApplicationItem.ID));
					list.add(item);
				}
				return list;
			}
			return list;
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
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return list;

	}

	/**
	 * @method saveApplicationItem
	 * @param DataOnRow
	 *            item
	 * @description insert object to tbl_application_item
	 * @author nghiant
	 * @date Nov 18, 2015
	 * @return -1 if save faild,else return id of record save success.
	 */
	public static Long saveApplicationItem(DataOnRow item) throws IOException, PropertyVetoException {

		String insertTableSQL = "INSERT INTO " + TblApplicationItem.getTableName() + "(" + TblApplicationItem.APP_ID + "," + TblApplicationItem.ITEM_CODE + ","
				+ TblApplicationItem.ITEM_NAME + ") VALUES" + "(?,?,?)";
		try {
			connection = DataSource.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(insertTableSQL);
			preparedStatement.setLong(1, item.getAppId());
			preparedStatement.setString(2, item.getItemCode());
			preparedStatement.setString(3, item.getItemName());
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
		return getItemIdByItemCode(item.getItemCode(), item.getAppId());

	}

	/**
	 * @method getTotalItemByIdApp
	 * @param Long
	 *            idApp
	 * @description get Total Item By IdApp
	 * @author nghiant
	 * @date Nov 20, 2015
	 * @return number record
	 */
	public long getTotalItemByIdApp(Long idApp) throws IOException, PropertyVetoException {
		String querySQL = "select count(*) from " + TblApplicationItem.getTableName() + " where " + TblApplicationItem.APP_ID + "=?";
		ResultSet resultSet = null;
		try {
			connection = DataSource.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(querySQL);
			preparedStatement.setLong(1, idApp);
			resultSet = preparedStatement.executeQuery();
			if (resultSet != null) {
				while (resultSet.next()) {
					return resultSet.getInt(1);
				}
			}
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
		return 0;
	}

	/**
	 * @method getListItemByAppID
	 * @param Long
	 *            idApp, Integer offset, String sortColumn
	 * @description get List Item By App ID
	 * @author nghiant
	 * @date Nov 20, 2015
	 * @return List Item
	 */
	public List<Item> getListItemByAppID(Long idApp, Integer offset, String sortColumn,String orderDirection) throws IOException, PropertyVetoException {
		List<Item> lstRs = new ArrayList<Item>();
		ResultSet resultSet = null;
		if (sortColumn.equals("")) {
			sortColumn= TblApplicationItem.UPDATE_DATE;
		} 
		String querySQL = " select  *  FROM " + TblApplicationItem.getTableName() + "  where " + TblApplicationItem.APP_ID + "=? ORDER BY "+sortColumn+" "+orderDirection+" limit ? offset ? ";
		try {
			connection = DataSource.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(querySQL);
			preparedStatement.setLong(1, idApp);
			preparedStatement.setInt(2, Constant.TOTAL_RECORD_PER_PAGE);
			preparedStatement.setInt(3, offset);

			resultSet = preparedStatement.executeQuery();
			System.out.println("SQL lst Item By ID: "+preparedStatement);
			if (resultSet != null) {
				while (resultSet.next()) {
					Item item = new Item();
					item.setAppId(resultSet.getLong(TblApplicationItem.APP_ID));
					item.setBoughtItems(resultSet.getInt(TblApplicationItem.BOUGHT_ITEMS));
					item.setId(resultSet.getLong(TblApplicationItem.ID));
					item.setItemCode(resultSet.getString(TblApplicationItem.ITEM_CODE));
					item.setItemName(resultSet.getString(TblApplicationItem.ITEM_NAME));
					item.setRevenue(resultSet.getDouble(TblApplicationItem.REVENUE));
					lstRs.add(item);
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
	
	
	
	public Item getItemById(Integer itemId) throws IOException, PropertyVetoException {
	    Item item = new Item();
        ResultSet resultSet = null;
        String querySQL = " SELECT  *  FROM " + TblApplicationItem.getTableName() + "  WHERE " + TblApplicationItem.ID + "=?";
        try {
            connection = DataSource.getInstance().getConnection();
            preparedStatement = connection.prepareStatement(querySQL);
            preparedStatement.setInt(1, itemId);

            resultSet = preparedStatement.executeQuery();
            System.out.println(preparedStatement);
            if (resultSet != null) {
                while (resultSet.next()) {
                    
                    item.setAppId(resultSet.getLong(TblApplicationItem.APP_ID));
                    item.setBoughtItems(resultSet.getInt(TblApplicationItem.BOUGHT_ITEMS));
                    item.setId(resultSet.getLong(TblApplicationItem.ID));
                    item.setItemCode(resultSet.getString(TblApplicationItem.ITEM_CODE));
                    item.setItemName(resultSet.getString(TblApplicationItem.ITEM_NAME));
                    item.setRevenue(resultSet.getDouble(TblApplicationItem.REVENUE));
                }
            }
            return item;

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
        return item;
    }

	public static void main(String[] args) throws IOException, PropertyVetoException {
		ApplicationItemDao app = new ApplicationItemDao();
		long totalItemByIdApp = app.getTotalItemByIdApp(1L);
		System.out.println(totalItemByIdApp);

		List<Item> listItemByAppID = app.getListItemByAppID(1L, 5, TblApplicationItem.ITEM_NAME,"ASC");
		for (Item item : listItemByAppID) {
			System.out.println(item);
		}
	}

	public String updateItem(Long idItem, String itemName) throws IOException, PropertyVetoException {
		String msg = "Cập nhật thành công!";
		String msgFal = "Cập nhật thất bại!";
		String insertTableSQL = "UPDATE " + TblApplicationItem.getTableName() + " SET " + TblApplicationItem.ITEM_NAME + "=? WHERE " + TblApplicationItem.ID
				+ "=?";
		try {
			connection = DataSource.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(insertTableSQL);
			preparedStatement.setString(1, itemName);
			preparedStatement.setLong(2, idItem);
			int executeUpdate = preparedStatement.executeUpdate();
			if (executeUpdate > 0) {
				return msg;
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
		return msgFal;

	}

	public String deleteItem(Long idItem) throws IOException, PropertyVetoException {
		String msg = "Delete success!";
		String msgFal = "Delete failed!";
		String deleteTableSQL = "DELETE FROM " + TblApplicationItem.getTableName() + " WHERE " + TblApplicationItem.ID + "=?";
		try {
			connection = DataSource.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(deleteTableSQL);
			preparedStatement.setLong(1, idItem);
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

	public void deleteLstItemByIdApp(int idApp) throws IOException, PropertyVetoException {
		//tbl_item_sale_daily
		List<Item> listItemByAppID = getListItemByAppID(idApp);		
		for(Item item:listItemByAppID){
			itemSaleDao.deleteAllItemSaleDailyByIdItem(item.getId());//Xoa sale daily
			itemSaleDao.deleteAllItemSaleYearly(item.getId());//Xoa sale yearly
			deleteItem(item.getId());
		}
	}
}
