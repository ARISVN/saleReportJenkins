package vn.com.aris.dao;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import jnr.ffi.annotations.In;
import vn.com.aris.DataSource;
import vn.com.aris.constant.Constant;

public class DataStoreDao {
	 Connection connection = null;
     Statement statement = null;
     public Date getOldestReleaseDateByTypeAndCode(String type, List<String> appCode) throws IOException, PropertyVetoException
     {
    	 ResultSet resultSet = null;
    	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
         try {
             connection = DataSource.getInstance().getConnection();
             statement = connection.createStatement();
             
             String sql = "select min(release_date) as release_date from tbl_application where type = "+ type;
             if(appCode != null && appCode.size() > 0) {
            	 sql += " AND app_code in (";	
            	 for (String code : appCode) {
					sql += "'" + code + "',";		
				}
            	 sql = sql.substring(0, sql.length() - 1);
            	 sql += ")";
             }
              		
             resultSet = statement.executeQuery(sql);
              while (resultSet.next()) {
            	 return StringUtils.isNotEmpty(resultSet.getString("release_date"))? 
            			  formatter.parse(resultSet.getString("release_date")) : null;
              }
         } catch (SQLException e) {
             e.printStackTrace();
         }catch (ParseException e) {
             e.printStackTrace();
         } finally {
             if (resultSet != null) try { resultSet.close(); } catch (SQLException e) {e.printStackTrace();}
             if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
             if (connection != null) try { connection.close(); } catch (SQLException e) {e.printStackTrace();}
         }
         return null;
     }
     
     public Date getMinReleaseDateByCode(List<Integer> appCode) throws IOException, PropertyVetoException
     {
    	 ResultSet resultSet = null;
    	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
         try {
             connection = DataSource.getInstance().getConnection();
             statement = connection.createStatement();
             
             String sql = "select min(release_date) as release_date from tbl_application ";
             if(appCode != null && appCode.size() > 0) {
            	 sql += "where id in (";	
            	 for (Integer code : appCode) {
					sql +=code + ",";		
				}
            	 sql = sql.substring(0, sql.length() - 1);
            	 sql += ")";
             }
              		
             resultSet = statement.executeQuery(sql);
              while (resultSet.next()) {
            	 return StringUtils.isNotEmpty(resultSet.getString("release_date"))? 
            			  formatter.parse(resultSet.getString("release_date")) : null;
              }
         } catch (SQLException e) {
             e.printStackTrace();
         }catch (ParseException e) {
             e.printStackTrace();
         } finally {
             if (resultSet != null) try { resultSet.close(); } catch (SQLException e) {e.printStackTrace();}
             if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
             if (connection != null) try { connection.close(); } catch (SQLException e) {e.printStackTrace();}
         }
         return null;
     }
     
     public List<String> getAdroidAppCode() throws IOException, PropertyVetoException
     {
    	 List<String> appCodeList = new ArrayList<>();
    	 ResultSet resultSet = null;
         try {
             connection = DataSource.getInstance().getConnection();
             statement = connection.createStatement();
             resultSet = statement.executeQuery("select app_code from tbl_application where type = " + Constant.ANDROID_TYPE);
              while (resultSet.next()) {
            	 appCodeList.add(resultSet.getString("app_code"));
              }
         } catch (SQLException e) {
             e.printStackTrace();
         } finally {
             if (resultSet != null) try { resultSet.close(); } catch (SQLException e) {e.printStackTrace();}
             if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
             if (connection != null) try { connection.close(); } catch (SQLException e) {e.printStackTrace();}
         }
         return appCodeList;
     }
     public static void main(String[] args) throws IOException, PropertyVetoException {
    	 List<String> appCode= new ArrayList<>();
    	 appCode.add("a");
    	 appCode.add("a");
    	 appCode.add("a");
    	 appCode.add("a");
    	 appCode.add("a");
    	 String sql = "select release_date from tbl_application where type = "+ 1;
         if(appCode != null && appCode.size() > 0) {
        	 sql += " AND app_code in (";	
        	 for (String code : appCode) {
				sql += "'" + code + "',";		
			}
        	 sql = sql.substring(0, sql.length() - 2);
        	 sql += "')";
         }
         System.out.println(sql);
	}
     
}
