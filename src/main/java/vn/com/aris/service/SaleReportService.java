package vn.com.aris.service;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import vn.com.aris.constant.ViewType;
import vn.com.aris.dao.ApplicationDao;
import vn.com.aris.dao.ApplicationItemDao;
import vn.com.aris.dao.SaleReportDao;
import vn.com.aris.dto.ApplicationDTO;
import vn.com.aris.dto.ReportStatisticDTO;
import vn.com.aris.model.Application;
import vn.com.aris.model.ApplicationSaleReport;
import vn.com.aris.model.Item;
import vn.com.aris.model.ItemSaleReport;
import vn.com.aris.utils.DateUtils;

public class SaleReportService {
    
    SaleReportDao saleReportDao = new SaleReportDao();
    
    public void getAppSaleStatistic(ReportStatisticDTO reportStatisticDto){
        List<String> lineChartList = new ArrayList<>();
        List<String> barChartValueList  = new ArrayList<>();
        
        List<Integer> appIdList = reportStatisticDto.getAppIdList();
        
        for (Integer id : appIdList) {
            // Get Report for every application
            reportStatisticDto.setQueryAppId(id);
            List<ApplicationSaleReport> saleReports = getAppSaleStatisticByAppId(reportStatisticDto);
            saleReports = fillFullDataForAppSatistic(saleReports, reportStatisticDto);
            modifyAppSaleYearly(saleReports, reportStatisticDto.getViewType(), reportStatisticDto.getDateConditionFrom(), reportStatisticDto.getDateConditionTo(), id);
            
            // Get Line chart for every application
            String lineChartStr = getLineChartAppSaleStatistic(saleReports);
            if(StringUtils.isNotEmpty(lineChartStr)) {
                lineChartList.add(lineChartStr);
            }
            
            // Get Bar chart for every application
            String barChartStr = getBarChartValueStr(saleReports);
            if(StringUtils.isNotEmpty(barChartStr)) {
                barChartValueList.add("[" + barChartStr + "]");
            }
        }
        
        reportStatisticDto.setLineChartList(lineChartList);
        reportStatisticDto.setBarChartValueList(barChartValueList);
    }
    
    
    /**
     * 
     * @method getAllAppSaleStatistic
     * @description Get Data Statistic for export CSV
     * @author dung.dd
     * @date Nov 27, 2015
     * @param
     * @return List<ApplicationSaleReport>
     */
    public List<ApplicationSaleReport> getAllAppSaleStatistic(ReportStatisticDTO reportStatisticDto){
        List<ApplicationSaleReport> allAppStatistcReport = new ArrayList<>();
        List<ApplicationSaleReport> saleReports = null;
        
        List<Integer> appIdList = reportStatisticDto.getAppIdList();
        
        for (Integer id : appIdList) {
            // Get Report for every application
            reportStatisticDto.setQueryAppId(id);
            saleReports = getAppSaleStatisticByAppId(reportStatisticDto);
            saleReports = fillFullDataForAppSatistic(saleReports, reportStatisticDto);
            modifyAppSaleYearly(saleReports, reportStatisticDto.getViewType(), reportStatisticDto.getDateConditionFrom(), reportStatisticDto.getDateConditionTo(), id);
            if(saleReports != null) {
                allAppStatistcReport.addAll(saleReports);
            }
            
        }
        
        return allAppStatistcReport;
    }
    
    
    
    /**
     * 
     * @method getAllItemSaleStatistic
     * @description Get Data Statistic for export CSV
     * @author dung.dd
     * @date Nov 27, 2015
     * @param
     * @return List<ItemSaleReport>
     */
    public List<ItemSaleReport> getAllItemSaleStatistic(ReportStatisticDTO reportStatisticDto){
        List<ItemSaleReport> allItemStatistcReport = new ArrayList<>();
        List<ItemSaleReport> saleReports = null;
        
        List<Integer> itemIdList = reportStatisticDto.getItemIdList();
        
        for (Integer id : itemIdList) {
            // Get Report for every application
            reportStatisticDto.setQueryItemId(id);
            saleReports = getItemSaleStatisticByAppId(reportStatisticDto);
            saleReports = fillFullDataForItemSatistic(saleReports, reportStatisticDto);
            modifyItemSaleYearly(saleReports, reportStatisticDto.getViewType(), reportStatisticDto.getDateConditionFrom(), reportStatisticDto.getDateConditionTo(), id);
            if(saleReports != null) {
                allItemStatistcReport.addAll(saleReports);
            }
        }
        return allItemStatistcReport;
    }
    
    /**
     * 
     * @method getLineChartAppStatistic
     * @description
     * @author dung.dd
     * @date Nov 26, 2015
     * @param
     * @return List<String>
     */
    public List<String> getLineChartAppStatistic(ReportStatisticDTO reportStatisticDto) {
        List<String> lineChartList = new ArrayList<>();
        
        
        List<Integer> appIdList = reportStatisticDto.getAppIdList();
        
        for (Integer id : appIdList) {
            // Get Report for every application
            reportStatisticDto.setQueryAppId(id);
            List<ApplicationSaleReport> saleReports = getAppSaleStatisticByAppId(reportStatisticDto);
            saleReports = fillFullDataForAppSatistic(saleReports, reportStatisticDto);
            modifyAppSaleYearly(saleReports, reportStatisticDto.getViewType(), reportStatisticDto.getDateConditionFrom(), reportStatisticDto.getDateConditionTo(), id);
            
            // Get Line chart for every application
            String lineChartStr = getLineChartAppSaleStatistic(saleReports);
            if(StringUtils.isNotEmpty(lineChartStr)) {
                lineChartList.add(lineChartStr);
            }
            
        }
        
        return lineChartList;
    }
    
    
    /**
     * 
     * @method getBarChartAppStatistic
     * @description
     * @author dung.dd
     * @date Nov 26, 2015
     * @param
     * @return List<String>
     */
    public List<String> getBarChartAppStatistic(ReportStatisticDTO reportStatisticDto) {
        List<String> barChartValueList = new ArrayList<>();
        
        List<Integer> appIdList = reportStatisticDto.getAppIdList();
        List<String> titleBarChart = new ArrayList<>();
        boolean getTitle = false;
        
        for (Integer id : appIdList) {
            
            // Get Report for every application
            reportStatisticDto.setQueryAppId(id);
            List<ApplicationSaleReport> saleReports = getAppSaleStatisticByAppId(reportStatisticDto);
            saleReports = fillFullDataForAppSatistic(saleReports, reportStatisticDto);
            modifyAppSaleYearly(saleReports, reportStatisticDto.getViewType(), reportStatisticDto.getDateConditionFrom(), reportStatisticDto.getDateConditionTo(), id);
            
            if(!getTitle && saleReports != null) {
                for (ApplicationSaleReport applicationSaleReport : saleReports) {
                    titleBarChart.add(applicationSaleReport.getDateRange());
                }
                getTitle = true;
                
            }
            
            // Get Bar chart for every application
            String barChartStr = getBarChartValueStr(saleReports);
            if(StringUtils.isNotEmpty(barChartStr)) {
                barChartValueList.add("[" + barChartStr + "]");
            }
        }
        
        barChartValueList.add(titleBarChart.toString());
        
        return barChartValueList;
    }
    
    
    /**
     * 
     * @method modifyAppSaleYearly
     * @description
     * @author dung.dd
     * @date Nov 27, 2015
     * @param
     * @return void
     */
    public void modifyAppSaleYearly(List<ApplicationSaleReport> saleReports, String viewType, Date dateFrom, Date dateTo, Integer appId) {
        if(saleReports == null || saleReports.size() == 0){
            return ;
        }
        
        if(viewType.equals(ViewType.YEARS.getValue())) {
            Calendar calendar = Calendar.getInstance();
            
            calendar.setTime(dateFrom);            
            int yearFrom = calendar.get(Calendar.YEAR);
            
            calendar.setTime(dateTo);            
            int yearTo = calendar.get(Calendar.YEAR);
            List<ApplicationSaleReport> numberInstallList = saleReportDao.selectInstallNumberByYear(yearFrom, yearTo, appId);
            
            for (ApplicationSaleReport applicationSaleReport : saleReports) {
                for (ApplicationSaleReport numberInstallReport : numberInstallList) {
                    if(applicationSaleReport.getYear().equals(numberInstallReport.getYear())) {
                        applicationSaleReport.setNumberInstall(numberInstallReport.getNumberInstall());
                    }
                }
            }
        }
    }
    
    /**
     * 
     * @method getLineChartItemStatistic
     * @description
     * @author dung.dd
     * @date Nov 26, 2015
     * @param
     * @return List<String>
     */
    public List<String> getLineChartItemStatistic(ReportStatisticDTO reportStatisticDto) {
        List<String> lineChartList = new ArrayList<>();
        
        List<Integer> itemIdList = reportStatisticDto.getItemIdList();
        
        for (Integer id : itemIdList) {
            // Get Report for every application
            reportStatisticDto.setQueryItemId(id);
            List<ItemSaleReport> saleReports = getItemSaleStatisticByAppId(reportStatisticDto);
            saleReports = fillFullDataForItemSatistic(saleReports, reportStatisticDto);
            modifyItemSaleYearly(saleReports, reportStatisticDto.getViewType(), reportStatisticDto.getDateConditionFrom(), reportStatisticDto.getDateConditionTo(), id);
            
            // Get Line chart for every application
            String lineChartStr = getLineChartItemSaleStatistic(saleReports);
            if(StringUtils.isNotEmpty(lineChartStr)) {
                lineChartList.add(lineChartStr);
            }
        }
        return lineChartList;
    }
    
    
    /**
     * 
     * @method getBarChartItemStatistic
     * @description
     * @author dung.dd
     * @date Nov 30, 2015
     * @param
     * @return List<String>
     */
    public List<String> getBarChartItemStatistic(ReportStatisticDTO reportStatisticDto) {
        List<String> barChartList = new ArrayList<>();
        List<String> titleBarChart = new ArrayList<>();
        boolean getTitle = false;
        
        List<Integer> itemIdList = reportStatisticDto.getItemIdList();
        
        for (Integer id : itemIdList) {
            // Get Report for every application
            reportStatisticDto.setQueryItemId(id);
            List<ItemSaleReport> saleReports = getItemSaleStatisticByAppId(reportStatisticDto);
            saleReports = fillFullDataForItemSatistic(saleReports, reportStatisticDto);
            modifyItemSaleYearly(saleReports, reportStatisticDto.getViewType(), reportStatisticDto.getDateConditionFrom(), reportStatisticDto.getDateConditionTo(), id);
            
            if(!getTitle && saleReports != null) {
                for (ItemSaleReport itemSaleReport : saleReports) {
                    titleBarChart.add(itemSaleReport.getDateRange());
                }
                getTitle = true;
            }
            
            // Get Bar chart for every item
            StringBuilder barChartStr = new StringBuilder();
            
            if(saleReports != null) {
                Integer length = saleReports.size();
                if(length > 0) {
                    barChartStr.append(saleReports.get(0).getNumberInstall());
                    if(length > 1) {
                        for (int i = 1; i < saleReports.size(); i++) {
                            ItemSaleReport itemSaleReport = saleReports.get(i);
                            barChartStr.append(",").append(itemSaleReport.getNumberInstall());
                        }
                    }
                }
            }
            
            
            if(StringUtils.isNotEmpty(barChartStr.toString())) {
                barChartList.add("[" + barChartStr.toString() + "]");
            }
        }
        barChartList.add(titleBarChart.toString());
        return barChartList;
        
        
        
    }
    
    /**
     * 
     * @method modifyAppSaleYearly
     * @description
     * @author dung.dd
     * @date Nov 27, 2015
     * @param
     * @return void
     */
    public void modifyItemSaleYearly(List<ItemSaleReport> saleReports, String viewType, Date dateFrom, Date dateTo, Integer itemId) {
        if(saleReports == null || saleReports.size() == 0) {
            return;
        }
        
        if(viewType.equals(ViewType.YEARS.getValue())) {
            Calendar calendar = Calendar.getInstance();
            
            calendar.setTime(dateFrom);            
            int yearFrom = calendar.get(Calendar.YEAR);
            
            calendar.setTime(dateTo);            
            int yearTo = calendar.get(Calendar.YEAR);
            List<ItemSaleReport> boughtNumberList = saleReportDao.selectBoughtNumberByYear(yearFrom, yearTo, itemId);
            
            for (ItemSaleReport itemSaleReport : saleReports) {
                for (ItemSaleReport boughtNumberReport : boughtNumberList) {
                    if(itemSaleReport.getYear().equals(boughtNumberReport.getYear())) {
                        itemSaleReport.setNumberInstall(boughtNumberReport.getNumberInstall());
                    }
                }
            }
        }
    }
    
    
    
    /**
     * 
     * @method getItemSaleStatisticByAppId
     * @description
     * @author dung.dd
     * @date Nov 26, 2015
     * @param
     * @return List<ItemSaleReport>
     */
    public List<ItemSaleReport> getItemSaleStatisticByAppId(ReportStatisticDTO reportStatisticDto) {
        List<ItemSaleReport> saleReports = saleReportDao.getItemSaleStatistic(reportStatisticDto);
        return saleReports;
    }
    
    
    /**
     * 
     * @method getLineChartItemSaleStatistic
     * @description
     * @author dung.dd
     * @date Nov 26, 2015
     * @param
     * @return String [['label', value],['label', value],['label', value]]
     */
    public String getLineChartItemSaleStatistic(List<ItemSaleReport> saleReports) {
        if(saleReports == null || saleReports.size() == 0) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        String appStatisticStr = getLineChartItemSaleStatisticStr(saleReports);
        
        if(StringUtils.isNotEmpty(appStatisticStr)) {
            result.append("[").append(appStatisticStr).append("]");
        }
        return result.toString();
    }
    
    /**
     * 
     * @method getLineChartItemSaleStatisticStr
     * @description
     * @author dung.dd
     * @date Nov 26, 2015
     * @param
     * @return String
     */
    public String getLineChartItemSaleStatisticStr(List<ItemSaleReport> saleReports) {
        StringBuilder result = new StringBuilder();
        
        Integer length = saleReports.size();
        if(saleReports != null && length > 0) {
            result.append(saleReports.get(0).getSArrayStrForJqplot());
            if(length > 1) {
                for (int i = 1; i < saleReports.size(); i++) {
                    ItemSaleReport applicationSaleReport = saleReports.get(i);
                    result.append(",").append(applicationSaleReport.getSArrayStrForJqplot());
                }
            }
        }
        return result.toString();
    }
    
    /**
     * 
     * @method getBarChartAppSaleStatistic
     * @description
     * @author dung.dd
     * @date Nov 24, 2015
     * @param
     * @return String
     */
    public String getBarChartValueStr(List<ApplicationSaleReport> saleReports) {
        StringBuilder result = new StringBuilder();
        Integer length = saleReports.size();
        
        if(saleReports != null && length > 0) {
            result.append(saleReports.get(0).getNumberInstall());
            if(length > 1) {
                for (int i = 1; i < saleReports.size(); i++) {
                    ApplicationSaleReport applicationSaleReport = saleReports.get(i);
                    result.append(",").append(applicationSaleReport.getNumberInstall());
                }
            }
        }
        return result.toString();
    }
    
    /**
     * 
     * @method getBarChartAppSaleStatistic
     * @description
     * @author dung.dd
     * @date Nov 24, 2015
     * @param
     * @return String
     */
    public String getBarChartLabelStr(List<ApplicationSaleReport> saleReports) {
        StringBuilder result = new StringBuilder();
        Integer length = saleReports.size();
        
        if(saleReports != null && length > 0) {
            result.append(saleReports.get(0).getNumberInstall());
            if(length > 1) {
                for (int i = 1; i < saleReports.size(); i++) {
                    ApplicationSaleReport applicationSaleReport = saleReports.get(i);
                    result.append(",").append(DateUtils.ConvertDateToString(applicationSaleReport.getReportDate()));
                }
            }
        }
        return result.toString();
    }
    
    /**
     * 
     * @method getLineChartAppSaleStatistic
     * @description
     * @author dung.dd
     * @date Nov 24, 2015
     * @param
     * @return String [['label', value],['label', value],['label', value]]
     */
    public String getLineChartAppSaleStatistic(List<ApplicationSaleReport> saleReports) {
        if(saleReports == null || saleReports.size() == 0) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        String appStatisticStr = getAppSaleStatisticStr(saleReports);
        
        if(StringUtils.isNotEmpty(appStatisticStr)) {
            result.append("[").append(appStatisticStr).append("]");
        }
        return result.toString();
    }
    
    
    
    /**
     * 
     * @method getAppSaleStatisticByAppId
     * @description
     * @author dung.dd
     * @date Nov 23, 2015
     * @param
     * @return String ['label', value],['label', value],['label', value]
     */
    public String getAppSaleStatisticStr(List<ApplicationSaleReport> saleReports) {
        StringBuilder result = new StringBuilder();
        
        Integer length = saleReports.size();
        if(saleReports != null && length > 0) {
            result.append(saleReports.get(0).getSArrayStrForJqplot());
            if(length > 1) {
                for (int i = 1; i < saleReports.size(); i++) {
                    ApplicationSaleReport applicationSaleReport = saleReports.get(i);
                    result.append(",").append(applicationSaleReport.getSArrayStrForJqplot());
                }
            }
        }
        return result.toString();
    }
    
    /**
     * 
     * @method getAppSaleStatisticByAppId
     * @description
     * @author dung.dd
     * @date Nov 26, 2015
     * @param
     * @return List<ApplicationSaleReport>
     */
    public List<ApplicationSaleReport> getAppSaleStatisticByAppId(ReportStatisticDTO reportStatisticDto) {
        List<ApplicationSaleReport> saleReports = saleReportDao.getAppSaleStatistic(reportStatisticDto);
        return saleReports;
    }
    
    
    /**
     * 
     * @method fillFullDataForAppSatistic
     * @description fill full data
     * @author dung.dd
     * @date Nov 26, 2015
     * @param
     * @return List<ApplicationSaleReport>
     */
    public List<ApplicationSaleReport> fillFullDataForAppSatistic(List<ApplicationSaleReport> saleReports, ReportStatisticDTO reportStatisticDto) {
//        if(saleReports == null || saleReports.size() == 0 ){
//            return saleReports;
//        }
        String viewType = reportStatisticDto.getViewType();
        
        // insert point (start and End) for list statistic
        saleReports = insertFrontBackAppStatisticList(saleReports, reportStatisticDto);
        
        int length = saleReports.size();
        ApplicationSaleReport applicationSaleReport;
        Date reportDate;
        
        ApplicationSaleReport applicationSaleReport2;
        Date reportDateFrom;
        List<ApplicationSaleReport> missingAppSaleReport = null;
        
        int numOfElements = 0;
        
        // Fill full data
        for (int i = length - 1; i > 0; i--) {
            
            applicationSaleReport = saleReports.get(i);
            applicationSaleReport2 = saleReports.get(i-1);
            
            if(viewType.equals(ViewType.MONTHS.getValue())) {
                reportDate = DateUtils.getDate(applicationSaleReport.getYear(), applicationSaleReport.getMonth() - 1, 1);
                reportDateFrom = DateUtils.getDate(applicationSaleReport2.getYear(), applicationSaleReport2.getMonth() - 1, 1);
                
                
                numOfElements = DateUtils.calculateNumberOfMonths(reportDateFrom, reportDate) - 1;
                // missingAppSaleReport = generateMissingAppSaleReport(numOfElements, applicationSaleReport, viewType);
                
            } else if(viewType.equals(ViewType.YEARS.getValue())) {
                numOfElements = applicationSaleReport.getYear() - applicationSaleReport2.getYear() - 1;
                // missingAppSaleReport = generateMissingAppSaleReport(numOfElements, applicationSaleReport, viewType);
                
            } else if(viewType.equals(ViewType.DAYS.getValue())) {
                reportDate = applicationSaleReport.getReportDate();
                reportDateFrom = applicationSaleReport2.getReportDate();
                
                numOfElements = DateUtils.calculateNumberOfDaysBetween(reportDateFrom, reportDate) - 1;
                // missingAppSaleReport = generateMissingAppSaleReport(numOfElements, applicationSaleReport, viewType);
                
            } else if(viewType.equals(ViewType.WEEKS.getValue())) {
                reportDate = applicationSaleReport.getReportDate();
                reportDateFrom = applicationSaleReport2.getReportDate();
                
                numOfElements = DateUtils.calculateNumberOfWeeks(reportDateFrom, reportDate) - 1;
                // missingAppSaleReport = generateMissingAppSaleReport(numOfElements, applicationSaleReport, viewType);
            }
            
            missingAppSaleReport = generateMissingAppSaleReport(numOfElements, applicationSaleReport, viewType);
            saleReports.addAll(i, missingAppSaleReport);
        }
        return saleReports;
    }
    
   /**
    * 
    * @method insertFrontBackAppStatisticList
    * @description Add start and End point for list
    * @author dung.dd
    * @date Nov 27, 2015
    * @param
    * @return void
    */
    private List<ApplicationSaleReport> insertFrontBackAppStatisticList(List<ApplicationSaleReport> saleReports, ReportStatisticDTO reportStatisticDto){
        String viewType = reportStatisticDto.getViewType();
        
        Date dateConditionFrom = reportStatisticDto.getDateConditionFrom();
        Date dateConditionTo = reportStatisticDto.getDateConditionTo();
        Integer appId = reportStatisticDto.getQueryAppId();
        
        if(saleReports == null || saleReports.size() == 0) {
            saleReports = new ArrayList<>();
            // Add a beginning element
            try {
                ApplicationDao applicationDao = new ApplicationDao();
                Application application = applicationDao.getAppByID(appId);
                ApplicationSaleReport appSaleReport;
                appSaleReport = new ApplicationSaleReport();
                appSaleReport.setAppId(NumberUtils.toInt(application.getAppId()));
                appSaleReport.setAppCode(application.getAppCode());
                appSaleReport.setAppName(application.getAppName());
                appSaleReport.setNumberInstall(0);
                
                if(viewType.equals(ViewType.DAYS.getValue())) {
                    appSaleReport.setReportDate(dateConditionFrom);
                    
                } else if(viewType.equals(ViewType.WEEKS.getValue())) {
                    appSaleReport.setReportDate(dateConditionFrom);
                    appSaleReport.setWeek(2000);
                    
                } else if(viewType.equals(ViewType.MONTHS.getValue()) || viewType.equals(ViewType.YEARS.getValue())) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(dateConditionFrom);
                    appSaleReport.setYear(calendar.get(Calendar.YEAR) + 1);  
                    if(viewType.equals(ViewType.MONTHS.getValue())) {
                        appSaleReport.setMonth(calendar.get(Calendar.MONTH) + 1);
                    }
                }
                saleReports.add(appSaleReport);
                
            } catch (IOException | PropertyVetoException e) {
                e.printStackTrace();
            }
        }
        
        
        
        ApplicationSaleReport appStatisticFrom = saleReports.get(0);
        ApplicationSaleReport appStatisticTo = saleReports.get(saleReports.size() - 1);
        ApplicationSaleReport appSaleReport;
        
        
        // Date Type
        if(viewType.equals(ViewType.DAYS.getValue())) {
            // Add Date from and Date To
            
            Date dateStatisticFrom = appStatisticFrom.getReportDate(); 
            
            // Add Front
            if(DateUtils.calculateNumberOfDaysBetween(dateConditionFrom, dateStatisticFrom) > 0) {
                appSaleReport = appStatisticFrom.clone();
                appSaleReport.setReportDate(dateConditionFrom);
                appSaleReport.setNumberInstall(0);
                saleReports.add(0, appSaleReport);
            }
            
            Date dateStatisticTo = appStatisticTo.getReportDate(); 
            // Add back
            if(DateUtils.calculateNumberOfDaysBetween(dateStatisticTo, dateConditionTo) > 0) {
                appSaleReport = appStatisticTo.clone();
                appSaleReport.setReportDate(dateConditionTo);
                appSaleReport.setNumberInstall(0);
                saleReports.add(appSaleReport);
            }
            
        } else if(viewType.equals(ViewType.MONTHS.getValue()) || viewType.equals(ViewType.YEARS.getValue())) {
            
            // Add Date from and Date To
            Calendar calendar = Calendar.getInstance();
            Integer month = appStatisticFrom.getMonth();
            if(month == null || month <= 0 ) {
                month = 1;
            }
            // month of Mysql is greater than month of Calendar Java
            Date dateStatisticFrom = DateUtils.getDate(appStatisticFrom.getYear(), month-1, 1); 
            
            // Add Front
            if(DateUtils.calculateNumberOfDaysBetween(dateConditionFrom, dateStatisticFrom) > 0) {
                calendar.setTime(dateConditionFrom);
                
                appSaleReport = appStatisticFrom.clone();
                if(viewType.equals(ViewType.MONTHS.getValue())){
                    // month of Mysql is greater than month of Calendar Java
                    appSaleReport.setMonth(calendar.get(Calendar.MONTH) + 1);
                }
                appSaleReport.setYear(calendar.get(Calendar.YEAR));
                appSaleReport.setNumberInstall(0);
                saleReports.add(0, appSaleReport);
            }
            
            month = appStatisticTo.getMonth();
            if(month == null || month <= 0 ) {
                month = 12;
            }
            // month of Mysql is greater than month of Calendar Java
            Date dateStatisticTo = DateUtils.getDate(appStatisticTo.getYear(), month, 1);; 
            dateStatisticTo = DateUtils.addDate(dateStatisticTo, -1);
            
            // Add back
            if(DateUtils.calculateNumberOfDaysBetween(dateStatisticTo, dateConditionTo) > 0) {
                calendar.setTime(dateConditionTo);
                
                appSaleReport = appStatisticTo.clone();
                if(viewType.equals(ViewType.MONTHS.getValue())){
                    appSaleReport.setMonth(calendar.get(Calendar.MONTH) + 1);
                }
                
                appSaleReport.setYear(calendar.get(Calendar.YEAR));
                appSaleReport.setNumberInstall(0);
                saleReports.add(appSaleReport);
            }
        } else if(viewType.equals(ViewType.WEEKS.getValue())) {
            // Add Date from and Date To
            Date dateStatisticFrom = appStatisticFrom.getReportDate(); 
            
            int numberOfWeeks = DateUtils.calculateNumberOfWeeks(dateConditionFrom, dateStatisticFrom);
            
            // Add Front
            if(numberOfWeeks > 0) {
                
                appSaleReport = appStatisticFrom.clone();
                appSaleReport.setReportDate(dateConditionFrom);
                int week = appStatisticFrom.getWeek() - numberOfWeeks;
                appSaleReport.setWeek(week);
                appSaleReport.setNumberInstall(0);
                saleReports.add(0, appSaleReport);
            }
            
            
            Date dateStatisticTo = appStatisticTo.getReportDate(); 
            
            numberOfWeeks = DateUtils.calculateNumberOfWeeks(dateStatisticTo, dateConditionTo);
            
            // Add back
            if(numberOfWeeks > 0) {
                appSaleReport = appStatisticTo.clone();
                appSaleReport.setReportDate(DateUtils.addWeek(dateConditionTo, -1));
                int week = appStatisticTo.getWeek() + numberOfWeeks;
                appSaleReport.setWeek(week);
                appSaleReport.setNumberInstall(0);
                saleReports.add(appSaleReport);
            }
        }
        return saleReports;
    }
    
    
    /**
     * 
     * @method generateMissingAppSaleReport
     * @description create empty data to fill full List
     * @author dung.dd
     * @date Nov 27, 2015
     * @param
     * @return List<ApplicationSaleReport>
     */
    private List<ApplicationSaleReport> generateMissingAppSaleReport(int length, ApplicationSaleReport sample, String viewType) {
        
        List<ApplicationSaleReport> missingAppSaleReport = new ArrayList<>();
        Date fromDate = sample.getReportDate();
        
        if(length > 0) {
            ApplicationSaleReport appSaleReport;
            for (int i = 0; i < length; i++) {
                appSaleReport = sample.clone();
                appSaleReport.setNumberInstall(0);
                missingAppSaleReport.add(0, appSaleReport);
                
                if(viewType.equals(ViewType.DAYS.getValue())) {
                    fromDate = DateUtils.addDate(fromDate, -1);
                    appSaleReport.setReportDate(fromDate);
                    
                }else if(viewType.equals(ViewType.MONTHS.getValue())) {
                    fromDate = DateUtils.getDate(sample.getYear(), sample.getMonth() - 1, 1);
                    fromDate = DateUtils.addMonth(fromDate, -1);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(fromDate);
                    appSaleReport.setMonth(calendar.get(Calendar.MONTH) + 1);
                    appSaleReport.setYear(calendar.get(Calendar.YEAR));
                    
                    sample = appSaleReport;
                }else if(viewType.equals(ViewType.YEARS.getValue())) {
                    fromDate = DateUtils.getDate(sample.getYear(), 0, 1);
                    fromDate = DateUtils.addYear(fromDate, -1);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(fromDate);
                    appSaleReport.setYear(calendar.get(Calendar.YEAR));
                    
                    sample = appSaleReport;
                    
                }else if(viewType.equals(ViewType.WEEKS.getValue())) {
                    fromDate = DateUtils.addDate(fromDate, -7);
                    appSaleReport.setWeek(appSaleReport.getWeek() - 1);
                    appSaleReport.setReportDate(fromDate);
                    
                    sample = appSaleReport;
                }
            }
        }
        return missingAppSaleReport;
    }
    
    /**
     * 
     * @method fillFullDataForAppSatistic
     * @description fill full data
     * @author dung.dd
     * @date Nov 26, 2015
     * @param
     * @return List<ApplicationSaleReport>
     */
    public List<ItemSaleReport> fillFullDataForItemSatistic(List<ItemSaleReport> saleReports, ReportStatisticDTO reportStatisticDto) {
//        if(saleReports == null || saleReports.size() == 0 ){
//            return null;
//        }
        String viewType = reportStatisticDto.getViewType();
        
        // insert point (start and End) for list statistic
        saleReports = insertFrontBackItemStatisticList(saleReports, reportStatisticDto);
        
        int length = saleReports.size();
        ItemSaleReport itemSaleReport;
        Date reportDate;
        
        ItemSaleReport itemSaleReport2;
        Date reportDateFrom;
        List<ItemSaleReport> missingItemSaleReport = null;
        
        int numOfElements = 0;
        
        // Fill full data
        for (int i = length - 1; i > 0; i--) {
            
            itemSaleReport = saleReports.get(i);
            itemSaleReport2 = saleReports.get(i-1);
            
            if(viewType.equals(ViewType.MONTHS.getValue())) {
                reportDate = DateUtils.getDate(itemSaleReport.getYear(), itemSaleReport.getMonth() - 1, 1);
                reportDateFrom = DateUtils.getDate(itemSaleReport2.getYear(), itemSaleReport2.getMonth() - 1, 1);
                
                
                numOfElements = DateUtils.calculateNumberOfMonths(reportDateFrom, reportDate) - 1;
                // missingItemSaleReport = generateMissingItemSaleReport(numOfElements, itemSaleReport, viewType);
                
            } else if(viewType.equals(ViewType.YEARS.getValue())) {
                numOfElements = itemSaleReport.getYear() - itemSaleReport2.getYear() - 1;
                // missingItemSaleReport = generateMissingItemSaleReport(numOfElements, itemSaleReport, viewType);
                
            } else if(viewType.equals(ViewType.DAYS.getValue())) {
                reportDate = itemSaleReport.getReportDate();
                reportDateFrom = itemSaleReport2.getReportDate();
                
                numOfElements = DateUtils.calculateNumberOfDaysBetween(reportDateFrom, reportDate) - 1;
                // missingItemSaleReport = generateMissingItemSaleReport(numOfElements, itemSaleReport, viewType);
                
            } else if(viewType.equals(ViewType.WEEKS.getValue())) {
                reportDate = itemSaleReport.getReportDate();
                reportDateFrom = itemSaleReport2.getReportDate();
                
                 numOfElements = DateUtils.calculateNumberOfWeeks(reportDateFrom, reportDate) - 1 ;
                
            }
            missingItemSaleReport = generateMissingItemSaleReport(numOfElements, itemSaleReport, viewType);
            saleReports.addAll(i, missingItemSaleReport);
        }
        return saleReports;
    }
    
   /**
    * 
    * @method insertFrontBackItemStatisticList
    * @description Add start and End point for list
    * @author dung.dd
    * @date Nov 27, 2015
    * @param
    * @return List<ItemSaleReport> 
    */
    private List<ItemSaleReport>  insertFrontBackItemStatisticList(List<ItemSaleReport> saleReports, ReportStatisticDTO reportStatisticDto){
        String viewType = reportStatisticDto.getViewType();
        
        Date dateConditionFrom = reportStatisticDto.getDateConditionFrom();
        Date dateConditionTo = reportStatisticDto.getDateConditionTo();
        Integer itemId = reportStatisticDto.getQueryItemId();
        
        if(saleReports == null || saleReports.size() == 0) {
            saleReports = new ArrayList<>();
            // Add a beginning element
            try {
                ApplicationItemDao itemDao = new ApplicationItemDao();
                Item item = itemDao.getItemById(itemId);
                ItemSaleReport itemSaleReport = new ItemSaleReport();
                itemSaleReport.setItemId(item.getId().intValue());
                itemSaleReport.setItemCode(item.getItemCode());
                itemSaleReport.setItemName(item.getItemName());
                itemSaleReport.setNumberInstall(0);
                
                if(viewType.equals(ViewType.DAYS.getValue())) {
                    itemSaleReport.setReportDate(dateConditionFrom);
                    
                } else if(viewType.equals(ViewType.WEEKS.getValue())) {
                    itemSaleReport.setReportDate(dateConditionFrom);
                    itemSaleReport.setWeek(2000);
                    
                } else if(viewType.equals(ViewType.MONTHS.getValue()) || viewType.equals(ViewType.YEARS.getValue())) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(dateConditionFrom);
                    itemSaleReport.setYear(calendar.get(Calendar.YEAR) + 1);  
                    if(viewType.equals(ViewType.MONTHS.getValue())) {
                        itemSaleReport.setMonth(calendar.get(Calendar.MONTH) + 1);
                    }
                }
                saleReports.add(itemSaleReport);
                
            } catch (IOException | PropertyVetoException e) {
                e.printStackTrace();
            }
        }
        
        
        
        ItemSaleReport itemStatisticFrom = saleReports.get(0);
        ItemSaleReport itemStatisticTo = saleReports.get(saleReports.size() - 1);
        ItemSaleReport itemSaleReport;
        
        // Date Type
        if(viewType.equals(ViewType.DAYS.getValue())) {
            // Add Date from and Date To
            Date dateStatisticFrom = itemStatisticFrom.getReportDate(); 
            
            // Add Front
            if(DateUtils.calculateNumberOfDaysBetween(dateConditionFrom, dateStatisticFrom) > 0) {
                itemSaleReport = itemStatisticFrom.clone();
                itemSaleReport.setReportDate(dateConditionFrom);
                itemSaleReport.setNumberInstall(0);
                saleReports.add(0, itemSaleReport);
            }
            
            Date dateStatisticTo = itemStatisticTo.getReportDate(); 
            // Add back
            if(DateUtils.calculateNumberOfDaysBetween(dateStatisticTo, dateConditionTo) > 0) {
                itemSaleReport = itemStatisticTo.clone();
                itemSaleReport.setReportDate(dateConditionTo);
                itemSaleReport.setNumberInstall(0);
                saleReports.add(itemSaleReport);
            }
            
        } else if(viewType.equals(ViewType.MONTHS.getValue()) || viewType.equals(ViewType.YEARS.getValue())) {
            
            // Add Date from and Date To
            Calendar calendar = Calendar.getInstance();
            Integer month = itemStatisticFrom.getMonth();
            if(month == null || month <= 0 ) {
                month = 1;
            }
            // month of Mysql is greater than month of Calendar Java
            Date dateStatisticFrom = DateUtils.getDate(itemStatisticFrom.getYear(), month-1, 1); 
            
            // Add Front
            if(DateUtils.calculateNumberOfDaysBetween(dateConditionFrom, dateStatisticFrom) > 0) {
                calendar.setTime(dateConditionFrom);
                
                itemSaleReport = itemStatisticFrom.clone();
                if(viewType.equals(ViewType.MONTHS.getValue())){
                    // month of Mysql is greater than month of Calendar Java
                    itemSaleReport.setMonth(calendar.get(Calendar.MONTH) + 1);
                }
                itemSaleReport.setYear(calendar.get(Calendar.YEAR));
                itemSaleReport.setNumberInstall(0);
                saleReports.add(0, itemSaleReport);
            }
            
            month = itemStatisticTo.getMonth();
            if(month == null || month <= 0 ) {
                month = 12;
            }
            // month of Mysql is greater than month of Calendar Java
            Date dateStatisticTo = DateUtils.getDate(itemStatisticTo.getYear(), month, 1); 
            dateStatisticTo = DateUtils.addDate(dateStatisticTo, -1);
            
            // Add back
            if(DateUtils.calculateNumberOfDaysBetween(dateStatisticTo, dateConditionTo) > 0) {
                calendar.setTime(dateConditionTo);
                
                itemSaleReport = itemStatisticTo.clone();
                if(viewType.equals(ViewType.MONTHS.getValue())){
                    itemSaleReport.setMonth(calendar.get(Calendar.MONTH) + 1);
                }
                
                itemSaleReport.setYear(calendar.get(Calendar.YEAR));
                itemSaleReport.setNumberInstall(0);
                saleReports.add(itemSaleReport);
            }
        } else if(viewType.equals(ViewType.WEEKS.getValue())) {
            // Add Date from and Date To
            Date dateStatisticFrom = itemStatisticFrom.getReportDate(); 
            
            int numberOfWeeks = DateUtils.calculateNumberOfWeeks(dateConditionFrom, dateStatisticFrom);
            
            // Add Front
            if(numberOfWeeks > 0) {
                
                itemSaleReport = itemStatisticFrom.clone();
                itemSaleReport.setReportDate(dateConditionFrom);
                int week = itemStatisticFrom.getWeek() - numberOfWeeks;
                itemSaleReport.setWeek(week);
                itemSaleReport.setNumberInstall(0);
                saleReports.add(0, itemSaleReport);
            }
            
            Date dateStatisticTo = itemStatisticTo.getReportDate(); 
            
            numberOfWeeks = DateUtils.calculateNumberOfWeeks(dateStatisticTo, dateConditionTo);
            
            // Add back
            if(numberOfWeeks > 0) {
                itemSaleReport = itemStatisticTo.clone();
                itemSaleReport.setReportDate(DateUtils.addWeek(dateConditionTo, -1));
                int week = itemStatisticTo.getWeek() + numberOfWeeks;
                itemSaleReport.setWeek(week);
                itemSaleReport.setNumberInstall(0);
                saleReports.add(itemSaleReport);
            }
        }
        return saleReports;
    }
    
    
    /**
     * 
     * @method generateMissingItemSaleReport
     * @description create empty data to fill full List
     * @author dung.dd
     * @date Nov 27, 2015
     * @param
     * @return List<ItemSaleReport>
     */
    private List<ItemSaleReport> generateMissingItemSaleReport(int length, ItemSaleReport sample, String viewType) {
        
        List<ItemSaleReport> missingItemSaleReport = new ArrayList<>();
        Date fromDate = sample.getReportDate();
        
        if(length > 0) {
            ItemSaleReport itemSaleReport;
            for (int i = 0; i < length; i++) {
                itemSaleReport = sample.clone();
                itemSaleReport.setNumberInstall(0);
                missingItemSaleReport.add(0, itemSaleReport);
                
                if(viewType.equals(ViewType.DAYS.getValue())) {
                    fromDate = DateUtils.addDate(fromDate, -1);
                    itemSaleReport.setReportDate(fromDate);
                    
                }else if(viewType.equals(ViewType.MONTHS.getValue())) {
                    fromDate = DateUtils.getDate(sample.getYear(), sample.getMonth() - 1, 1);
                    fromDate = DateUtils.addMonth(fromDate, -1);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(fromDate);
                    itemSaleReport.setMonth(calendar.get(Calendar.MONTH) + 1);
                    itemSaleReport.setYear(calendar.get(Calendar.YEAR));
                    
                    sample = itemSaleReport;
                }else if(viewType.equals(ViewType.YEARS.getValue())) {
                    fromDate = DateUtils.getDate(sample.getYear(), 0, 1);
                    fromDate = DateUtils.addYear(fromDate, -1);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(fromDate);
                    itemSaleReport.setYear(calendar.get(Calendar.YEAR));
                    
                    sample = itemSaleReport;
                    
                }else if(viewType.equals(ViewType.WEEKS.getValue())) {
                    fromDate = DateUtils.addDate(fromDate, -7);
                    itemSaleReport.setWeek(itemSaleReport.getWeek() - 1);
                    itemSaleReport.setReportDate(fromDate);
                    
                    sample = itemSaleReport;
                }
            }
        }
        return missingItemSaleReport;
    }
    
    
    /**
     * 
     * @method getListItemByAppID
     * @description
     * @author dung.dd
     * @date Nov 29, 2015
     * @param
     * @return List<Item>
     */
    public List<Item> getListItemByAppID(int appID) {
        List<Item> result = null;
        ApplicationItemDao dao = new ApplicationItemDao();
        try {
            result = dao.getListItemByAppID(appID);
        } catch (IOException | PropertyVetoException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }
    
    
    /**
     * 
     * @method getAppNameList
     * @description
     * @author User
     * @date Nov 29, 2015
     * @param
     * @return List<String>
     */
    public List<String> getAppNameList(List<Integer> appIdList) throws IOException, PropertyVetoException {
        ApplicationDao dao = new ApplicationDao();
        List<String> appNameList = new ArrayList<>();
        Application app = null;
        
        for (Integer id : appIdList) {
            app = dao.getAppByID(id);
            if(app != null) {
                appNameList.add("'" + app.getAppName() + "'");
            }
        }
        return appNameList;
        
    }
    
    /**
     * 
     * @method getItemNameList
     * @description
     * @author dung.dd
     * @date Nov 30, 2015
     * @param
     * @return List<String>
     */
    public List<String> getItemNameList(List<Integer> itemIdList) throws IOException, PropertyVetoException {
        ApplicationItemDao dao = new ApplicationItemDao();
        List<String> appNameList = new ArrayList<>();
        
        for (Integer id : itemIdList) {
            appNameList.add("'" + dao.getItemNameByItemId(id) + "'");
        }
        return appNameList;
        
    }
    
    /**
     * 
     * @method getListApplicationForSaleReport
     * @description
     * @author dung.dd
     * @date Dec 4, 2015
     * @param
     * @return List<ApplicationDTO>
     */
    public List<ApplicationDTO> getListApplicationForSaleReport(List<Integer> appIdList, Date dateFrom, Date dateTo, int limit, int offset, String sortColumn, String orderDirection) throws IOException, PropertyVetoException {
       if(appIdList == null || appIdList.size() == 0) {
           return new ArrayList<ApplicationDTO>();
       }
        SaleReportDao dao = new SaleReportDao();
       List<ApplicationDTO> rs = new ArrayList<>();
       List<Application> app = dao.getListApplicationForSaleReport(appIdList, dateFrom, dateTo, limit, offset, sortColumn, orderDirection);
       if(CollectionUtils.isNotEmpty(app)) {
           for (Application application : app) {
               ApplicationDTO dto = convertModelToApplictionDTO(application);
               rs.add(dto);
           }
       }
       return rs;
   }
    
    
    public List<Item> getListItemForSaleReport(List<Integer> itemIdList, Date dateFrom, Date dateTo, int limit, int offset, String sortColumn, String orderDirection) throws IOException, PropertyVetoException {
        if(itemIdList == null || itemIdList.size() == 0) {
            return new ArrayList<Item>();
        }
        SaleReportDao dao = new SaleReportDao();
        List<Item> itemList = dao.getListItemForSaleReport(itemIdList, dateFrom, dateTo, limit, offset, sortColumn, orderDirection);
//        if(CollectionUtils.isNotEmpty(itemList)) {
//            for (Application application : app) {
//                ApplicationDTO dto = convertModelToApplictionDTO(application);
//                rs.add(dto);
//            }
//        }
        return itemList;
    }
   
   public ApplicationDTO convertModelToApplictionDTO(Application appliction) {
       if (appliction == null) {
           return new ApplicationDTO();
       }
       ApplicationDTO dto = new ApplicationDTO();
       dto.setAppCode(appliction.getAppCode());
       dto.setAppId(appliction.getAppId());
       dto.setAppName(appliction.getAppName());
       dto.setReleaseDate(appliction.getReleaseDate());
       dto.setRevenue(appliction.getRevenue());
       dto.setRevenueItem(appliction.getRevenueItem());
       dto.setType(appliction.getType());
       dto.setUpdateStatus(appliction.getUpdateStatus());
       dto.setUpdateDate(appliction.getUpdateDate());
       dto.setNumberOfInstall(appliction.getNumberOfInstall());
       return dto;
   }
    
}
