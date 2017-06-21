package vn.com.aris.dao.adapter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.codehaus.groovy.runtime.dgmimpl.arrays.IntegerArrayGetAtMetaMethod;

import vn.com.aris.constant.Constant;
import vn.com.aris.constant.ViewType;
import vn.com.aris.dto.SaleReportCsvDto;
import vn.com.aris.mapper.db.TblApplication;
import vn.com.aris.mapper.db.TblApplicationItem;
import vn.com.aris.mapper.db.TblApplicationSaleYearly;
import vn.com.aris.model.ApplicationSaleReport;
import vn.com.aris.model.ItemSaleReport;
import vn.com.aris.utils.DateUtils;

public class SaleReportAdapter {

    /**
     * 
     * @method convertApplicationSaleReportList
     * @description
     * @author dung.dd
     * @date Nov 27, 2015
     * @param
     * @return List<ApplicationSaleReport>
     */
    public List<ApplicationSaleReport> convertApplicationSaleReportList(ResultSet resultSet, String viewType) {
        List<ApplicationSaleReport> applicationSaleReportList = new ArrayList<ApplicationSaleReport>();
        
        ApplicationSaleReport applicationSaleReport = null;
        String reportDateStr = "";
        try {
            while(resultSet.next()){
                applicationSaleReport = new ApplicationSaleReport();
                try {
                    
                    applicationSaleReport.setAppId(resultSet.getInt(TblApplication.ID));
                    applicationSaleReport.setAppCode(resultSet.getString(TblApplication.APP_CODE));
                    applicationSaleReport.setAppName(resultSet.getString(TblApplication.APP_NAME));
                    applicationSaleReport.setNumberInstall(resultSet.getInt("numInstall"));
                    
                    if(viewType.equals(ViewType.DAYS.getValue())) {
                        reportDateStr = resultSet.getString("reportDate");
                        applicationSaleReport.setReportDate(DateUtils.convertDateFromSql(reportDateStr));
                        
                    } else if(viewType.equals(ViewType.WEEKS.getValue())) {
                        applicationSaleReport.setWeek(resultSet.getInt("week"));
                        reportDateStr = resultSet.getString("reportDate");
                        applicationSaleReport.setReportDate(DateUtils.convertDateFromSql(reportDateStr));
                        
                    }else if(viewType.equals(ViewType.MONTHS.getValue())) {
                        applicationSaleReport.setMonth(resultSet.getInt("month"));
                        applicationSaleReport.setYear(resultSet.getInt("year"));
                        
                    }else if(viewType.equals(ViewType.YEARS.getValue())) {
                        applicationSaleReport.setYear(resultSet.getInt("year"));
                    }
                    
                    
                    
                    applicationSaleReportList.add(applicationSaleReport);
                    
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return applicationSaleReportList;
    }
    
    
    /**
     * 
     * @method convertApplicationSaleReportYearList
     * @description
     * @author dung.dd
     * @date Nov 27, 2015
     * @param
     * @return List<ApplicationSaleReport>
     */
    public List<ApplicationSaleReport> convertApplicationSaleReportYearList(ResultSet resultSet) {
        List<ApplicationSaleReport> applicationSaleReportList = new ArrayList<ApplicationSaleReport>();
        
        ApplicationSaleReport applicationSaleReport = null;
        try {
            while(resultSet.next()){
                applicationSaleReport = new ApplicationSaleReport();
                try {
                    applicationSaleReport.setYear(resultSet.getInt(TblApplicationSaleYearly.YEAR));
                    applicationSaleReport.setNumberInstall(resultSet.getInt(TblApplicationSaleYearly.NUMBER_INSTALL));
                    applicationSaleReportList.add(applicationSaleReport);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return applicationSaleReportList;
    }
      
    
    /**
     * 
     * @method convertItemSaleReportList
     * @description
     * @author dung.dd
     * @date Nov 27, 2015
     * @param
     * @return List<ItemSaleReport>
     */
    public List<ItemSaleReport> convertItemSaleReportList(ResultSet resultSet, String viewType) {
        List<ItemSaleReport> itemSaleReportList = new ArrayList<ItemSaleReport>();
        
        ItemSaleReport itemSaleReport = null;
        String reportDateStr = "";
        try {
            while(resultSet.next()){
                itemSaleReport = new ItemSaleReport();
                try {
                    
                    itemSaleReport.setItemId(resultSet.getInt(TblApplicationItem.ID));
                    itemSaleReport.setItemCode(resultSet.getString(TblApplicationItem.ITEM_CODE));
                    itemSaleReport.setItemName(resultSet.getString(TblApplicationItem.ITEM_NAME));
                    itemSaleReport.setNumberInstall(resultSet.getInt("boughtNumber"));
                    
                    if(viewType.equals(ViewType.DAYS.getValue())) {
                        reportDateStr = resultSet.getString("reportDate");
                        itemSaleReport.setReportDate(DateUtils.convertDateFromSql(reportDateStr));
                        
                    } else if(viewType.equals(ViewType.WEEKS.getValue())) {
                        itemSaleReport.setWeek(resultSet.getInt("week"));
                        reportDateStr = resultSet.getString("reportDate");
                        itemSaleReport.setReportDate(DateUtils.convertDateFromSql(reportDateStr));
                        
                    }else if(viewType.equals(ViewType.MONTHS.getValue())) {
                        itemSaleReport.setMonth(resultSet.getInt("month"));
                        itemSaleReport.setYear(resultSet.getInt("year"));
                        
                    }else if(viewType.equals(ViewType.YEARS.getValue())) {
                        itemSaleReport.setYear(resultSet.getInt("year"));
                    }
                    
                    
                    
                    itemSaleReportList.add(itemSaleReport);
                    
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return itemSaleReportList;
    }
    
    /**
     * 
     * @method convertItemSaleReportYearList
     * @description
     * @author dung.dd
     * @date Nov 27, 2015
     * @param
     * @return List<ItemSaleReport>
     */
    public List<ItemSaleReport> convertItemSaleReportYearList(ResultSet resultSet) {
        List<ItemSaleReport> itemSaleReportList = new ArrayList<ItemSaleReport>();
        
        ItemSaleReport itemSaleReport = null;
        try {
            while(resultSet.next()){
                itemSaleReport = new ItemSaleReport();
                try {
                    itemSaleReport.setYear(resultSet.getInt(TblApplicationSaleYearly.YEAR));
                    itemSaleReport.setNumberInstall(resultSet.getInt(TblApplicationSaleYearly.NUMBER_INSTALL));
                    itemSaleReportList.add(itemSaleReport);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return itemSaleReportList;
    }
    
    
    
    /**
     * 
     * @method convertToCsvList
     * @description
     * @author User
     * @date Nov 29, 2015
     * @param
     * @return List<SaleReportCsvDto>
     */
    public List<SaleReportCsvDto> convertToAppCsvList(List<ApplicationSaleReport> saleReports) {
        List<SaleReportCsvDto> saleReportCsvDtoList = new ArrayList<>();
        SaleReportCsvDto saleReportCsv;
        for (ApplicationSaleReport applicationSaleReport : saleReports) {
            saleReportCsv = new SaleReportCsvDto();
            saleReportCsv.setName(applicationSaleReport.getAppName());
            saleReportCsv.setNumberOfInstall(applicationSaleReport.getNumberInstall());
            
            Integer week = applicationSaleReport.getWeek();
            Integer month = applicationSaleReport.getMonth();
            Integer year = applicationSaleReport.getYear();
            Date reportDate = applicationSaleReport.getReportDate();
            
            Date beginDate = reportDate;
            Date endDate = null;
            
            if(week != null && week > 0 && reportDate != null) { // Weeks
                // Get Start date and End date of every week
                endDate = DateUtils.getEndDateOfWeek(reportDate);
                
                
            } else if(reportDate != null) { // Days
                endDate= beginDate;
                
            }else if(month != null && month > 0 && year != null && year > 0) { // Months
                beginDate = DateUtils.getDate(year, month - 1, 1);
                endDate = DateUtils.addDate(DateUtils.addMonth(beginDate, 1), -1);
                
            }else if(year != null && year > 0) { // Years
                beginDate = DateUtils.getDate(year, 0, 1);
                endDate = DateUtils.addDate(DateUtils.addYear(beginDate, 1), -1);
            }
            
            saleReportCsv.setBeginDate(DateUtils.convertDateToCsvFormat(beginDate));
            saleReportCsv.setEndDate(DateUtils.convertDateToCsvFormat(endDate));
            
            
            saleReportCsvDtoList.add(saleReportCsv);
        }
        
        return saleReportCsvDtoList;
    }
    
    
    
    /**
     * 
     * @method convertToCsvList
     * @description
     * @author User
     * @date Nov 29, 2015
     * @param
     * @return List<SaleReportCsvDto>
     */
    public List<SaleReportCsvDto> convertToItemCsvList(List<ItemSaleReport> saleReports) {
        if(saleReports == null || saleReports.size() == 0) {
            return null;
        }
        List<SaleReportCsvDto> saleReportCsvDtoList = new ArrayList<>();
        SaleReportCsvDto saleReportCsv;
        for (ItemSaleReport itemSaleReport : saleReports) {
            saleReportCsv = new SaleReportCsvDto();
            saleReportCsv.setName(itemSaleReport.getItemName());
            saleReportCsv.setNumberOfInstall(itemSaleReport.getNumberInstall());
            
            Integer week = itemSaleReport.getWeek();
            Integer month = itemSaleReport.getMonth();
            Integer year = itemSaleReport.getYear();
            Date reportDate = itemSaleReport.getReportDate();
            
            Date beginDate = reportDate;
            Date endDate = null;
            
            if(week != null && week > 0 && reportDate != null) { // Weeks
                // Get Start date and End date of every week
                endDate = DateUtils.getEndDateOfWeek(reportDate);
                
                
            } else if(reportDate != null) { // Days
                endDate= beginDate;
                
            }else if(month != null && month > 0 && year != null && year > 0) { // Months
                beginDate = DateUtils.getDate(year, month - 1, 1);
                endDate = DateUtils.addDate(DateUtils.addMonth(beginDate, 1), -1);
                
            }else if(year != null && year > 0) { // Years
                beginDate = DateUtils.getDate(year, 0, 1);
                endDate = DateUtils.addDate(DateUtils.addYear(beginDate, 1), -1);
            }
            
            saleReportCsv.setBeginDate(DateUtils.convertDateToCsvFormat(beginDate));
            saleReportCsv.setEndDate(DateUtils.convertDateToCsvFormat(endDate));
            
            
            saleReportCsvDtoList.add(saleReportCsv);
        }
        
        return saleReportCsvDtoList;
    }
    
    
    
//    private List<>
    
//    private boolean isExistColumnLabel(ResultSet resultSet, String label) {
//        try {
//            resultSet.findColumn("reportDate");
//        } catch (SQLException e) {
//            return false;
//        }
//        return true;
//    }
//    
//    private String getStringFromLabel(ResultSet resultSet, String label){
//        String result = "";
//        try {
//            result = resultSet.getString(label);
//        } catch (SQLException e) {
//            return result;
//        }
//        return result;
//    }
}
