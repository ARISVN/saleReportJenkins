package vn.com.aris.model;
/**
 * @author nghiant
 * @date Nov 18, 2015
 */
public class RowSaleReport {
	private String itemCode;
	private String itemName;
	private String currency;
	private Double sale;
	private int boughtNumber;
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
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public Double getSale() {
		return sale;
	}
	public void setSale(Double sale) {
		this.sale = sale;
	}
	public int getBoughtNumber() {
		return boughtNumber;
	}
	public void setBoughtNumber(int boughtNumber) {
		this.boughtNumber = boughtNumber;
	}
	
}
