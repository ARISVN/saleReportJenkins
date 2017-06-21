package vn.com.aris.model;

import java.util.Date;
import java.util.List;

public class Application {
	private String appCode;
	private String appId;
	private String appName;
	private String updateStatus;
	private Integer type;
	private double revenueItem;
	private double revenue;
	private Date releaseDate;
	private Date updateDate;
	
	private Integer numberOfInstall;
	
	private List<String> fileNames;
	
	
	public Application(String appCode, String appId, String appName, String updateStatus, Integer type,
			double revenueItem, double revenue, Date releaseDate, Date updateDate, List<String> fileNames) {
		super();
		this.appCode = appCode;
		this.appId = appId;
		this.appName = appName;
		this.updateStatus = updateStatus;
		this.type = type;
		this.revenueItem = revenueItem;
		this.revenue = revenue;
		this.releaseDate = releaseDate;
		this.updateDate = updateDate;
	}
	public Application() {
	}


	public String getAppName() {
		return appName;
	}


	public void setAppName(String appName) {
		this.appName = appName;
	}


	public String getUpdateStatus() {
		return updateStatus;
	}


	public void setUpdateStatus(String updateStatus) {
		this.updateStatus = updateStatus;
	}


	public double getRevenueItem() {
		return revenueItem;
	}


	public void setRevenueItem(double revenueItem) {
		this.revenueItem = revenueItem;
	}


	public double getRevenue() {
		return revenue;
	}


	public void setRevenue(double revenue) {
		this.revenue = revenue;
	}





	public Date getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}
	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public boolean equals(Object obj) {
		if (obj instanceof Application) {
			Application pp = (Application) obj;
			return (pp.appCode.equals(this.appCode)
					&& pp.appId.equals(this.appId) && pp.releaseDate
						.equals(this.releaseDate));
		} else {
			return false;
		}
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}


	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public List<String> getFileNames() {
		return fileNames;
	}
	public void setFileNames(List<String> fileNames) {
		this.fileNames = fileNames;
	}
    public Integer getNumberOfInstall() {
        return numberOfInstall;
    }
    public void setNumberOfInstall(Integer numberOfInstall) {
        this.numberOfInstall = numberOfInstall;
    }
	
	
}
