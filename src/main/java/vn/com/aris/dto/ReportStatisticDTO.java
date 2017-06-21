package vn.com.aris.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import vn.com.aris.model.ApplicationSaleReport;

/**
 * Created by dung.dd on 11/19/2015.
 */
public class ReportStatisticDTO {

    // param get from Client
    private String statisticType = "";
    private String viewType = "";
    private String timeType = "";
    private String dateFrom = "";
    private String dateTo = "";
    private String appId = "";
    private String itemId = "";
    
    
    private Integer queryAppId = 0;
    private Integer queryItemId = 0;
    private String month = "";
    private String year = "";
    private String mode = "";
    

    private List<Integer> appIdList = new ArrayList<>();
    private List<Integer> itemIdList = new ArrayList<>();
    private String minAppReleaseDate;
    
    private Date dateConditionFrom;
    private Date dateConditionTo;
    private Boolean exportCsv ;

    // Data for Condition
    private List<ApplicationDTO> applicationList = new ArrayList<ApplicationDTO>();
    
    
    // Data to View
    private List<String> lineChartList = null;
    private List<String> barChartValueList = null;
    private List<String> barChartLabelList = null;
    private List <String> appNameList = null;
    
    // Data for Export
    private List<ApplicationSaleReport> dataSaleReport = null;

    // Getter and setter

    public String getStatisticType() {
        return statisticType;
    }

    public void setStatisticType(String statisticType) {
        this.statisticType = statisticType;
    }

    public String getViewType() {
        return viewType;
    }

    public void setViewType(String viewType) {
        this.viewType = viewType;
    }

    public String getTimeType() {
        return timeType;
    }

    public void setTimeType(String timeType) {
        this.timeType = timeType;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public List<Integer> getAppIdList() {
        return appIdList;
    }
    
    public Date getDateConditionFrom() {
        return dateConditionFrom;
    }

    public void setDateConditionFrom(Date dateConditionFrom) {
        this.dateConditionFrom = dateConditionFrom;
    }

    public Date getDateConditionTo() {
        return dateConditionTo;
    }

    public void setDateConditionTo(Date dateConditionTo) {
        this.dateConditionTo = dateConditionTo;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setAppIdList(List<Integer> appIdList) {
        this.appIdList = appIdList;
    }

    public List<ApplicationDTO> getApplicationList() {
        return applicationList;
    }

    public void setApplicationList(List<ApplicationDTO> applicationList) {
        this.applicationList = applicationList;
    }
    
    public void setQueryAppId(Integer queryAppId) {
        this.queryAppId = queryAppId;
    }
    
    public Integer getQueryAppId() {
        return queryAppId;
    }
    
    public Integer getQueryItemId() {
        return queryItemId;
    }

    public void setQueryItemId(Integer queryItemId) {
        this.queryItemId = queryItemId;
    }

    public List<String> getLineChartList() {
        return lineChartList;
    }
    
    public void setLineChartList(List<String> lineChartList) {
        this.lineChartList = lineChartList;
    }
    
    public List<String> getBarChartLabelList() {
        return barChartLabelList;
    }
    
    public void setBarChartLabelList(List<String> barChartLabelList) {
        this.barChartLabelList = barChartLabelList;
    }
    
    public List<String> getBarChartValueList() {
        return barChartValueList;
    }
    
    public void setBarChartValueList(List<String> barChartValueList) {
        this.barChartValueList = barChartValueList;
    }

    public List<String> getAppNameList() {
        return appNameList;
    }
    
    public void setAppNameList(List<String> appNameList) {
        this.appNameList = appNameList;
    }
    
    public List<ApplicationSaleReport> getDataSaleReport() {
        return dataSaleReport;
    }
    
    public void setDataSaleReport(List<ApplicationSaleReport> dataSaleReport) {
        this.dataSaleReport = dataSaleReport;
    }
    
    
    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public List<Integer> getItemIdList() {
        return itemIdList;
    }

    public void setItemIdList(List<Integer> itemIdList) {
        this.itemIdList = itemIdList;
    }
    
    
    

    public Boolean getExportCsv() {
        return exportCsv;
    }

    public void setExportCsv(Boolean exportCsv) {
        this.exportCsv = exportCsv;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("statisticType : " + statisticType).append("\n");
        str.append("viewType : " + viewType).append("\n");
        str.append("timeType : " + timeType).append("\n");
        str.append("dateFrom : " + dateFrom).append("\n");
        str.append("dateTo : " + dateTo).append("\n");
        str.append("appId : " + appId).append("\n");
        return str.toString();
    }

	public String getMinAppReleaseDate() {
		return minAppReleaseDate;
	}

	public void setMinAppReleaseDate(String minAppReleaseDate) {
		this.minAppReleaseDate = minAppReleaseDate;
	}

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
	
	
	

}
