package vn.com.aris.model;

/**
 * 
 * @author tbl_application_item in database
 *
 */
public class Item {
	private Long id;
	private Long appId;
	private String itemCode;
	private String itemName;
	private int boughtItems;
	private double revenue;

	public Item(Long id, Long appId, String itemCode, String itemName, int revenue, int boughtItems) {
		super();
		this.id = id;
		this.appId = appId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.revenue = revenue;
		this.boughtItems = boughtItems;
	}

	public Item() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public int getBoughtItems() {
		return boughtItems;
	}

	public void setBoughtItems(int boughtItems) {
		this.boughtItems = boughtItems;
	}

	public double getRevenue() {
		return revenue;
	}

	public void setRevenue(double revenue) {
		this.revenue = revenue;
	}

	public String toString() {
		return "id: " + id + "appId: " + appId + "itemCode: " + itemCode + "itemName: " + itemName + "boughtItems: " + boughtItems + "revenue: " + revenue;
	}
}
