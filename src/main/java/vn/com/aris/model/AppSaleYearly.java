package vn.com.aris.model;

public class AppSaleYearly {
	private Long id;
	private Long appId;
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
	public Double getSaleCurrency() {
		return saleCurrency;
	}
	public void setSaleCurrency(Double saleCurrency) {
		this.saleCurrency = saleCurrency;
	}

}
