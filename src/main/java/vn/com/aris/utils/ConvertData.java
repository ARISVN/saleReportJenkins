package vn.com.aris.utils;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;

import vn.com.aris.dao.ApplicationItemDao;
import vn.com.aris.dto.ConfigSaleReportDTO;
import vn.com.aris.model.AndroidItemKey;
import vn.com.aris.model.AppSaleDaily;
import vn.com.aris.model.AppSaleYearly;
import vn.com.aris.model.Config;
import vn.com.aris.model.DataOnRow;
import vn.com.aris.model.ItemSaleDaily;
import vn.com.aris.model.ItemSaleYearly;
import vn.com.aris.model.RowSaleReport;

public class ConvertData {
	public static final String DAY_FORMAT_APPLE = "MM-dd-yyyy";
	public static final String DAY_FORMAT_ANDROID = "yyyy-MM-dd";
	public static final String DAY_FORMAT_APPLE2 = "MM/dd/yyyy";
	public static final Long NOT_FOUND = -1L;

	public static AppSaleDaily convertDataOnRowToAppSaleDaily(DataOnRow data) throws Exception {
		AppSaleDaily appSaleDaily = new AppSaleDaily();
		appSaleDaily.setCurrency(data.getCurrency());
		appSaleDaily.setDate(convertFromStringToDate(data.getBeginDate(), DAY_FORMAT_APPLE));
		appSaleDaily.setNumberInstall(data.getUnit());
		appSaleDaily.setSale(data.getSale());
		return appSaleDaily;
	}

	public static Date convertFromStringToDate(String dateStr, String parten) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat(parten);
		java.util.Date parsed;
		try {
			parsed = format.parse(dateStr);
		} catch (Exception e) {
			format = new SimpleDateFormat(DAY_FORMAT_APPLE2);
			parsed = format.parse(dateStr);
		}
		java.sql.Date sql = new java.sql.Date(parsed.getTime());
		return sql;
	}

	public static AppSaleYearly convertDataOnRowToAppSaleYearly(DataOnRow data) {
		AppSaleYearly appSaleDaily = new AppSaleYearly();
		appSaleDaily.setCurrency(data.getCurrency());
		appSaleDaily.setYear(convertFromStringToYear(data.getBeginDate()));
		appSaleDaily.setNumberInstall(data.getUnit());
		appSaleDaily.setSale(data.getSale());
		return appSaleDaily;
	}

	private static int convertFromStringToYear(String beginDate) {
		String[] split = beginDate.split("/");
		int parseInt = Integer.parseInt(split[2]);
		return parseInt;
	}

	public static ItemSaleDaily convertDataOnRowToItemSaleDaily(DataOnRow item, Long appId) throws Exception {
		ItemSaleDaily itemSaleReport = new ItemSaleDaily();
		itemSaleReport.setBoughtNumber(item.getUnit());
		itemSaleReport.setCurrency(item.getCurrency());
		itemSaleReport.setDate(convertFromStringToDate(item.getBeginDate(), DAY_FORMAT_APPLE));
		// check co trong database chua
		Long itemIdByItemCode = ApplicationItemDao.getItemIdByItemCode(item.getItemCode(), appId);
		if (itemIdByItemCode.equals(NOT_FOUND)) {
			itemIdByItemCode = ApplicationItemDao.saveApplicationItem(item);
		}
		// itemSaleReport.setId();
		itemSaleReport.setItemId(itemIdByItemCode);// chua biet dc
		itemSaleReport.setSale(item.getSale());
		itemSaleReport.setSaleCurrency(1.0);// chua biet
		itemSaleReport.setItemName(item.getItemName());
		itemSaleReport.setItemCode(item.getItemCode());
		return itemSaleReport;
	}

	public static ItemSaleYearly convertDataOnRowToItemSaleYearly(DataOnRow item) throws IOException, PropertyVetoException {
		ItemSaleYearly itemSaleReport = new ItemSaleYearly();
		itemSaleReport.setBoughtNumber(item.getUnit());
		itemSaleReport.setCurrency(item.getCurrency());
		itemSaleReport.setYear(convertFromStringToYear(item.getBeginDate()));
		// itemSaleReport.setId();
		// itemSaleReport.setItemId();//chua biet dc
		itemSaleReport.setSale(item.getSale());
		itemSaleReport.setSaleCurrency(1.0);// chua biet

		// check co trong database chua
		Long itemIdByItemCode = ApplicationItemDao.getItemIdByItemCode(item.getItemCode(), item.getAppId());
		if (itemIdByItemCode.equals(NOT_FOUND)) {
			itemIdByItemCode = ApplicationItemDao.saveApplicationItem(item);
		}
		itemSaleReport.setItemId(itemIdByItemCode);// chua biet dc
		itemSaleReport.setItemName(item.getItemName());
		itemSaleReport.setItemCode(item.getItemCode());
		return itemSaleReport;
	}

	public static ItemSaleDaily convertToItemSaleDaily(AndroidItemKey key, RowSaleReport value, Long appId) throws Exception {
		ItemSaleDaily itemSaleReport = new ItemSaleDaily();
		itemSaleReport.setBoughtNumber(value.getBoughtNumber());
		itemSaleReport.setCurrency(value.getCurrency());
		itemSaleReport.setDate(convertFromStringToDate(key.getOrderChargedDate(), DAY_FORMAT_ANDROID));
		// check co trong database chua
		Long itemIdByItemCode = ApplicationItemDao.getItemIdByItemCode(value.getItemCode(), appId);
		if (itemIdByItemCode.equals(NOT_FOUND)) {
			DataOnRow data = new DataOnRow();
			data.setAppId(appId);
			data.setItemCode(value.getItemCode());
			data.setItemName(value.getItemName());
			itemIdByItemCode = ApplicationItemDao.saveApplicationItem(data);
		}
		// itemSaleReport.setId();
		itemSaleReport.setItemId(itemIdByItemCode);// chua biet dc
		itemSaleReport.setSale(value.getSale());
		itemSaleReport.setSaleCurrency(1.0);// chua biet
		itemSaleReport.setItemName(value.getItemName());
		itemSaleReport.setItemCode(value.getItemCode());
		return itemSaleReport;
	}

	public static void main(String[] args) throws Exception {
		Date convertFromStringToDate = convertFromStringToDate("11/26/2015", "MM/dd/yyyy");
		System.out.println(convertFromStringToDate);
	}

	public static Config convertFromConfigSaleReportDTOToConfig(ConfigSaleReportDTO configDTO) {
		Config config = new Config();
		config.setApplePath(configDTO.getApplePath());
		config.setAppleVendorId(configDTO.getAppleVendorId());
		config.setGoogleDataPath(configDTO.getGoogleDataPath());
		config.setGoogleNumber(configDTO.getGoogleNumber());
		config.setGooglePath(configDTO.getGooglePath());
		return config;
	}

}
