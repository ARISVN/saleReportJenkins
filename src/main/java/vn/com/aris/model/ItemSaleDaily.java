package vn.com.aris.model;

import java.sql.Date;

public class ItemSaleDaily {
	private Long id;
	private Long itemId;
	private Date date;
	private Integer day;
    private Integer week;
    private Integer month;
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getWeek() {
        return week;
    }

    public void setWeek(Integer week) {
        this.week = week;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
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
