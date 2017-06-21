package vn.com.aris.model;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import vn.com.aris.constant.Constant;
import vn.com.aris.utils.DateUtils;

public class ApplicationSaleReport implements Cloneable {

    private int appId;
    private String appCode;
    private String appName;
    private Integer numberInstall;
    private Date reportDate;
    
    private Integer week;
    private Integer month;
    private Integer year;
    
    
    public int getAppId() {
        return appId;
    }
    public void setAppId(int appId) {
        this.appId = appId;
    }
    public String getAppCode() {
        return appCode;
    }
    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }
    public String getAppName() {
        return appName;
    }
    public void setAppName(String appName) {
        this.appName = appName;
    }
    public Integer getNumberInstall() {
        return numberInstall;
    }
    public void setNumberInstall(Integer numberInstall) {
        this.numberInstall = numberInstall;
    }
    public Date getReportDate() {
        return reportDate;
    }
    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
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
    
    public StringBuilder getSArrayStrForJqplot(){
        
        String dateStr = getDateRange();
        
        StringBuilder str = new StringBuilder();
        str.append("[");
        str.append("").append(dateStr).append(",");
        str.append(numberInstall.toString()).append(","); // .append(",")
        str.append("'").append(appName).append("'");
        str.append("]");
        return str;
    }
    
    
    public String getDateRange(){
        String dateStr = "";
        if(week != null && week > 0 && reportDate != null) {
            // Get Start date and End date of every week
            Date endDate = DateUtils.getEndDateOfWeek(reportDate);
            dateStr = DateUtils.ConvertDateToString(reportDate) + " - " + DateUtils.ConvertDateToString(endDate);
            
            
        } else if(reportDate != null) {
            dateStr = DateUtils.ConvertDateToString(reportDate);
            
        }else if(month != null && month > 0 && year != null && year > 0) {
            dateStr = year + Constant.SPACE_DATE +  month;
            
        }else if(year != null && year > 0) {
            dateStr = year.toString();
        }
        
        if(StringUtils.isNotEmpty(dateStr)) {
            dateStr = "'" + dateStr + "'";
        }
        return dateStr;
    }
    
    
    @Override
    public ApplicationSaleReport clone() {
        try {
            return (ApplicationSaleReport) super.clone();
        } catch (CloneNotSupportedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    
}
