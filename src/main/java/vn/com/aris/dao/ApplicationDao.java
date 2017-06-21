package vn.com.aris.dao;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.sql.PreparedStatement;

import vn.com.aris.DataSource;
import vn.com.aris.mapper.db.TblApplication;
import vn.com.aris.model.Application;

/**
 * 
 * @author hung.td
 * @data 19/11/2015
 *
 */
public class ApplicationDao {
	static Connection connection = null;
	static Statement statement = null;
	static PreparedStatement preparedStatement = null;
	
	/**
	 * Get application by app_id
	 * 
	 * @param id: Application id
	 * hung.td add new 
	 * @return null if not found
	 * @throws IOException
	 * @throws PropertyVetoException
	 */
	public Application getAppByID(int id) throws IOException, PropertyVetoException {
		ResultSet resultSet = null;
		Application appReturn = new Application();
		try {
			connection = DataSource.getInstance().getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM tbl_application where id = " + id);
			if( resultSet.isBeforeFirst())
			{
				while(resultSet.next()) {
					appReturn.setAppName(resultSet.getString("app_name"));
					appReturn.setType(resultSet.getInt("type"));
					appReturn.setAppCode(resultSet.getString("app_code"));
					appReturn.setRevenueItem(resultSet.getLong("revenue_item"));
					appReturn.setRevenue(resultSet.getLong("revenue"));
					appReturn.setAppId(resultSet.getString(TblApplication.ID));
				}
				return appReturn;
			}
		} catch(SQLException e) {
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
		return appReturn;
	}
	
	
	/**
	 * 
	 * @method selectAppIdFromName
	 * @description Get app Name from App ID
	 * @author User
	 * @date Nov 29, 2015
	 * @param
	 * @return Integer
	 */
    public Integer selectAppIdFromName(String appName) throws IOException, PropertyVetoException {
        ResultSet resultSet = null;
        Integer appId = null;
        try {
            connection = DataSource.getInstance().getConnection();
            preparedStatement = connection.prepareStatement("SELECT " + TblApplication.ID + " FROM tbl_application WHERE " + TblApplication.APP_NAME + " =? ");
            preparedStatement.setString(1, appName);

            resultSet = preparedStatement.executeQuery();
            if( resultSet.isBeforeFirst())
            {
                while(resultSet.next()) {
                    appId= resultSet.getInt(TblApplication.ID);
                }
            }
        } catch(SQLException e) {
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
        return appId;
    }
	
	public static void main(String[] args) throws IOException, PropertyVetoException{
		Application ap = new Application();
		ApplicationDao dao = new ApplicationDao();
		ap = dao.getAppByID(1);
		if ( ap != null) {
		System.out.println("Name: " + ap.getAppName());
		System.out.println("Type: " + ap.getType());
		System.out.println("Revenue: " + ap.getRevenue());
		System.out.println("Bought items: " + ap.getRevenueItem());
		}
	}

}
