package vn.com.aris.model;

import java.util.List;
import java.util.Map;
/**
 * @author nghiant
 * @date Nov 18, 2015
 */
public class DataReportAndroid {
	private List<AppSaleDaily> lstAppSale;
	private Map<AndroidItemKey, RowSaleReport> mapSaleItem;
	public List<AppSaleDaily> getLstAppSale() {
		return lstAppSale;
	}
	public void setLstAppSale(List<AppSaleDaily> lstAppSale) {
		this.lstAppSale = lstAppSale;
	}
	public Map<AndroidItemKey, RowSaleReport> getMapSaleItem() {
		return mapSaleItem;
	}
	public void setMapSaleItem(Map<AndroidItemKey, RowSaleReport> mapSaleItem) {
		this.mapSaleItem = mapSaleItem;
	}
	public DataReportAndroid(List<AppSaleDaily> lstAppSale,
			Map<AndroidItemKey, RowSaleReport> mapSaleItem) {
		this.lstAppSale = lstAppSale;
		this.mapSaleItem = mapSaleItem;
	}
	
}
