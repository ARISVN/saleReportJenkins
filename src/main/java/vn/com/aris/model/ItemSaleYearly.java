package vn.com.aris.model;

public class ItemSaleYearly {
	private Long id;
	private Long itemId;
	private Integer year;
	private Integer boughtNumber;
	private Double sale;
	private String currency;
	private Double saleCurrency;
	private String itemName;
	private String itemCode;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getBoughtNumber() {
		return boughtNumber;
	}

	public void setBoughtNumber(Integer boughtNumber) {
		this.boughtNumber = boughtNumber;
	}

	public Double getSale() {
		return sale;
	}

	public void setSale(Double sale) {
		this.sale = sale;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Double getSaleCurrency() {
		return saleCurrency;
	}

	public void setSaleCurrency(Double saleCurrency) {
		this.saleCurrency = saleCurrency;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemCode() {
		return itemCode;
	}

	public String getItemName() {
		return itemName;
	}

}
