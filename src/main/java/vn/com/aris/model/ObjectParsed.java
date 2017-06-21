package vn.com.aris.model;

import java.util.Map;

import vn.com.aris.constant.DateType;
/**
 * @author nghiant
 * @date Nov 18, 2015
 */
public class ObjectParsed {
	private Map<RowUnique, DataOnRow> dataApp;
	private DateType dateType;
	private Map<ItemUnique, DataOnRow> dataItem;

	public Map<RowUnique, DataOnRow> getDataApp() {
		return dataApp;
	}

	public void setDataApp(Map<RowUnique, DataOnRow> data) {
		this.dataApp = data;
	}

	public DateType getDateType() {
		return dateType;
	}

	public void setDateType(DateType dateType) {
		this.dateType = dateType;
	}

	public Map<ItemUnique, DataOnRow> getDataItem() {
		return dataItem;
	}

	public void setDataItem(Map<ItemUnique, DataOnRow> dataItem) {
		this.dataItem = dataItem;
	}

}
