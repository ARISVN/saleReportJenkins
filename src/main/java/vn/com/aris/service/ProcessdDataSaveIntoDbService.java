package vn.com.aris.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import vn.com.aris.constant.DateType;
import vn.com.aris.dao.ApplicationSaleDao;
import vn.com.aris.dao.ItemSaleDao;
import vn.com.aris.model.AndroidItemKey;
import vn.com.aris.model.AppSaleDaily;
import vn.com.aris.model.AppSaleYearly;
import vn.com.aris.model.DataOnRow;
import vn.com.aris.model.DataReportAndroid;
import vn.com.aris.model.ItemSaleDaily;
import vn.com.aris.model.ItemSaleYearly;
import vn.com.aris.model.ItemUnique;
import vn.com.aris.model.ObjectParsed;
import vn.com.aris.model.RowSaleReport;
import vn.com.aris.model.RowUnique;
import vn.com.aris.utils.ConvertData;
import vn.com.aris.utils.ReadCSV;

/**
 * @author nghiant
 * @date Nov 18, 2015
 */
public class ProcessdDataSaveIntoDbService {
	/**
	 * @method processdDataFromFileApple
	 * @param path,appCode,appId
	 * @description processd data for file txt from apple, save data to database
	 * @author nghiant
	 * @date Nov 18, 2015
	 */
	public static void processdDataFromFileApple(String path, String appCode, Long appId) throws Exception {
		ObjectParsed dataRs = ReadCSV.getDataFromFile(path, appCode);
		Map<RowUnique, DataOnRow> dataFromFile = dataRs.getDataApp();
		List<AppSaleDaily> lstAppSaleDaily = new ArrayList<AppSaleDaily>();
		List<AppSaleYearly> lstAppSaleYear = new ArrayList<AppSaleYearly>();
		Map<ItemUnique, DataOnRow> dataItem = dataRs.getDataItem();
		List<ItemSaleDaily> lstItemSaleDaily = new ArrayList<ItemSaleDaily>();
		List<ItemSaleYearly> lstItemSaleYear = new ArrayList<ItemSaleYearly>();

		if (dataRs.getDateType().equals(DateType.DAILY)) {// type report daily
			for (Map.Entry<RowUnique, DataOnRow> entry : dataFromFile.entrySet()) {
				AppSaleDaily appSaleDaily = ConvertData.convertDataOnRowToAppSaleDaily(entry.getValue());
				appSaleDaily.setAppId(appId);
				appSaleDaily.setSaleCurrency(1.0);// Hien tai chua biet lam sao

				lstAppSaleDaily.add(appSaleDaily);
			}
			
			ApplicationSaleDao.saveLstApplicationSaleDaily(lstAppSaleDaily);
			for (Map.Entry<ItemUnique, DataOnRow> entry : dataItem.entrySet()) {
				DataOnRow item = entry.getValue();
				item.setAppId(appId);
				ItemSaleDaily itemSaleDaily = ConvertData.convertDataOnRowToItemSaleDaily(item, appId);

				lstItemSaleDaily.add(itemSaleDaily);
			}
			ItemSaleDao.saveLstItemSaleDaily(lstItemSaleDaily);

		} else {// type report yearly
			for (Map.Entry<RowUnique, DataOnRow> entry : dataFromFile.entrySet()) {
				AppSaleYearly appSaleYear = ConvertData.convertDataOnRowToAppSaleYearly(entry.getValue());
				appSaleYear.setAppId(appId);
				appSaleYear.setSaleCurrency(1.0);// Hien tai chua biet lam sao
				lstAppSaleYear.add(appSaleYear);
			}
			ApplicationSaleDao.saveLstApplicationSaleYearly(lstAppSaleYear);

			for (Map.Entry<ItemUnique, DataOnRow> entry : dataItem.entrySet()) {
				DataOnRow item = entry.getValue();
				item.setAppId(appId);
				ItemSaleYearly itemSaleYearly = ConvertData.convertDataOnRowToItemSaleYearly(item);
				lstItemSaleYear.add(itemSaleYearly);
			}
			ItemSaleDao.saveLstItemSaleYearly(lstItemSaleYear);
		}
	}
	/**
	 * @method processdDataFromFileApple
	 * @param pathOverview,pathSale,appId,appCode
	 * @description processd data for file csv from google, save data to database
	 * @author nghiant
	 * @date Nov 18, 2015
	 */
	public static void processdDataFromFileAndroid(String pathOverview, String pathSale, Long appId, String appCode) throws Exception {
		List<ItemSaleDaily> lstItemSaleDaily = new ArrayList<ItemSaleDaily>();
		DataReportAndroid dataAndroidFromFile = ReadCSV.getDataAndroidFromFile(pathOverview, pathSale, appId, appCode);
		List<AppSaleDaily> lstAppSaleDaily = dataAndroidFromFile.getLstAppSale();
		ApplicationSaleDao.saveLstApplicationSaleDaily(lstAppSaleDaily);
		Map<AndroidItemKey, RowSaleReport> mapSaleItem = dataAndroidFromFile.getMapSaleItem();
		for (Map.Entry<AndroidItemKey, RowSaleReport> entry : mapSaleItem.entrySet()) {

			ItemSaleDaily itemSaleDaily = ConvertData.convertToItemSaleDaily(entry.getKey(), entry.getValue(), appId);
			lstItemSaleDaily.add(itemSaleDaily);
		}
		ItemSaleDao.saveLstItemSaleDaily(lstItemSaleDaily);

	}

}
