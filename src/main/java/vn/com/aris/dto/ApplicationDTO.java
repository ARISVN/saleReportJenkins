package vn.com.aris.dto;

import java.util.Date;
import java.util.List;

public class ApplicationDTO {
	private String appCode;
	private String appId;
	private String appName;
	private String updateStatus;
	private Integer type;
	private Double revenueItem;
	private Double revenue;
	private Date releaseDate;
	private Date updateDate;
	private List<String> fileNames;
	private String typeStr;

	private Integer numberOfInstall;
	
	public ApplicationDTO(String appCode, String appId, String appName, String updateStatus, Integer type, Double revenueItem, Double revenue,
			Date releaseDate, Date updateDate, List<String> fileNames) {
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
		this.fileNames = fileNames;
	}

	public ApplicationDTO() {
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

	public Double getRevenueItem() {
		return revenueItem;
	}

	public void setRevenueItem(Double revenueItem) {
		this.revenueItem = revenueItem;
	}

	public Double getRevenue() {
		return revenue;
	}

	public void setRevenue(Double revenue) {
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
		if (obj instanceof ApplicationDTO) {
			ApplicationDTO pp = (ApplicationDTO) obj;
			return (pp.appCode.equals(this.appCode) && pp.appId.equals(this.appId) && pp.releaseDate.equals(this.releaseDate));
		} else {
			return false;
		}
	}

	public List<String> getFileNames() {
		return fileNames;
	}

	public void setFileNames(List<String> fileNames) {
		this.fileNames = fileNames;
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

	public void setTypeStr(String typeStr) {
		this.typeStr = typeStr;
	}

	public String getTypeStr() {
		return typeStr;
	}

    public Integer getNumberOfInstall() {
        return numberOfInstall;
    }

    public void setNumberOfInstall(Integer numberOfInstall) {
        this.numberOfInstall = numberOfInstall;
    }
	
	
}
