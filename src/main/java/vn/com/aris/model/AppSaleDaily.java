package vn.com.aris.model;

import java.sql.Date;

public class AppSaleDaily {
	private Long id;
	private Long appId;
	private Date date;
	private Integer day;
	private Integer week;
	private Integer month;
	private Integer year;
	private Integer numberInstall;
	private Double sale;
	private String currency;
	private Double saleCurrency;

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

    public Integer getNumberInstall() {
		return numberInstall;
	}

	public void setNumberInstall(Integer numberInstall) {
		this.numberInstall = numberInstall;
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

	public void setSaleCurrency(Double saleCurrency) {
		this.saleCurrency = saleCurrency;
	}

	public Double getSaleCurrency() {
		return saleCurrency;
	}

}