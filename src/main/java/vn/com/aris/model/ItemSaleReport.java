package vn.com.aris.model;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import vn.com.aris.constant.Constant;
import vn.com.aris.utils.DateUtils;


public class ItemSaleReport implements Cloneable  {

    private int itemId;
    private String itemCode;
    private String itemName;
    private Integer numberInstall;
    private Date reportDate;
    
    private Integer week;
    private Integer month;
    private Integer year;
    
    public int getItemId() {
        return itemId;
    }
    public void setItemId(int itemId) {
        this.itemId = itemId;
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
        str.append("'").append(itemName).append("'");
        str.append("]");
        return str;
    }
    
    public String getDateRange(){
        String dateStr = "";
        if(week != null && week > 0 && reportDate != null) {
         // Get Start date and End date of every week
            Date EndDate = DateUtils.getEndDateOfWeek(reportDate);
            dateStr = DateUtils.ConvertDateToString(reportDate) + " - " + DateUtils.ConvertDateToString(EndDate);
            
            
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
    public ItemSaleReport clone() {
        try {
            return (ItemSaleReport) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
