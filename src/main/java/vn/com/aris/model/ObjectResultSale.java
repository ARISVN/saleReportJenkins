package vn.com.aris.model;

import java.util.Map;
/**
 * @author nghiant
 * @date Nov 18, 2015
 */
public class ObjectResultSale {
	private Map<AndroidAppKey, RowSaleReport> mapSaleApp;
	private Map<AndroidItemKey, RowSaleReport> mapSaleItem;

	public Map<AndroidAppKey, RowSaleReport> getMapSaleApp() {
		return mapSaleApp;
	}

	public void setMapSaleApp(Map<AndroidAppKey, RowSaleReport> mapSaleApp) {
		this.mapSaleApp = mapSaleApp;
	}

	public Map<AndroidItemKey, RowSaleReport> getMapSaleItem() {
		return mapSaleItem;
	}

	public void setMapSaleItem(Map<AndroidItemKey, RowSaleReport> mapSaleItem) {
		this.mapSaleItem = mapSaleItem;
	}
}
