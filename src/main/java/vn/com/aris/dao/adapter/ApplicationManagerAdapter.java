package vn.com.aris.dao.adapter;

import java.sql.ResultSet;
import java.sql.SQLException;

import vn.com.aris.mapper.db.TblApplication;
import vn.com.aris.mapper.db.TblApplicationItem;
import vn.com.aris.model.Application;
import vn.com.aris.model.Item;

public class ApplicationManagerAdapter {
	
	// tam.ph added - 2015/11/23
	public Item convertItemObject(ResultSet rset) {
		Item item = new Item();
		try {
			item.setId(rset.getLong(TblApplicationItem.ID));
			item.setAppId(rset.getLong(TblApplicationItem.APP_ID));
			item.setItemCode(rset.getString(TblApplicationItem.ITEM_CODE));
			item.setItemName(rset.getString(TblApplicationItem.ITEM_NAME));
			item.setRevenue(rset.getDouble(TblApplicationItem.REVENUE));
			item.setBoughtItems(rset.getInt(TblApplicationItem.BOUGHT_ITEMS));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return item;
	}
	// tam.ph ending
	
	// tam.ph added - 2015/11/19
	public Application convertApplicationObject(ResultSet rset) {
		Application application = new Application();
		try {
			application.setAppId(rset.getString(TblApplication.ID));
			application.setAppCode(rset.getString(TblApplication.APP_CODE));
			application.setAppName(rset.getString(TblApplication.APP_NAME));
			application.setType(rset.getInt(TblApplication.TYPE));
			application.setReleaseDate(rset.getDate(TblApplication.RELEASE_DATE));
			application.setUpdateStatus(rset.getString(TblApplication.UPDATE_STATUS));
			application.setUpdateDate(rset.getDate(TblApplication.UPDATE_DATE));
			application.setRevenueItem(rset.getDouble(TblApplication.REVENUE_ITEM));
			application.setRevenue(rset.getDouble(TblApplication.REVENUE));
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return application;
	}
	// tam.ph ending
	
	
	public Application convertApplicationForSaleRepoprt(ResultSet rset) {
        Application application = new Application();
        try {
            application.setAppId(rset.getString(TblApplication.ID));
            application.setAppCode(rset.getString(TblApplication.APP_CODE));
            application.setAppName(rset.getString(TblApplication.APP_NAME));
            application.setType(rset.getInt(TblApplication.TYPE));
            application.setReleaseDate(rset.getDate(TblApplication.RELEASE_DATE));
            application.setUpdateStatus(rset.getString(TblApplication.UPDATE_STATUS));
            application.setUpdateDate(rset.getDate(TblApplication.UPDATE_DATE));
            application.setRevenueItem(rset.getDouble(TblApplication.REVENUE_ITEM));
            application.setRevenue(rset.getDouble(TblApplication.REVENUE));
            application.setNumberOfInstall(rset.getInt("amount"));
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return application;
    }
}



