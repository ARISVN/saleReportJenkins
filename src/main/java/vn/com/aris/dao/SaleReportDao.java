package vn.com.aris.dao;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import vn.com.aris.DataSource;
import vn.com.aris.constant.Constant;
import vn.com.aris.constant.ViewType;
import vn.com.aris.dao.adapter.ApplicationManagerAdapter;
import vn.com.aris.dao.adapter.SaleReportAdapter;
import vn.com.aris.dto.ReportStatisticDTO;
import vn.com.aris.mapper.db.TblApplication;
import vn.com.aris.mapper.db.TblApplicationItem;
import vn.com.aris.mapper.db.TblApplicationSaleDaily;
import vn.com.aris.mapper.db.TblApplicationSaleYearly;
import vn.com.aris.mapper.db.TblItemSaleDaily;
import vn.com.aris.mapper.db.TblItemSaleYearly;
import vn.com.aris.model.Application;
import vn.com.aris.model.ApplicationSaleReport;
import vn.com.aris.model.Item;
import vn.com.aris.model.ItemSaleReport;
import vn.com.aris.utils.DateUtils;

public class SaleReportDao {

    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    
    /*public List<ApplicationSaleReport> getAppSaleStatistic(ReportStatisticDTO reportStatisticDto){
        List<ApplicationSaleReport> applicationSaleReportList = new ArrayList<>();
        
        StringBuilder sqlBuilder = new StringBuilder();
        ResultSet resultSet = null;
        // SELECT
        sqlBuilder.append("SELECT app." + TblApplication.ID).append(", app." + TblApplication.APP_CODE);
        sqlBuilder.append(", app." + TblApplication.APP_NAME).append(", SUM(appSale." + TblApplicationSaleDaily.NUMBER_INSTALL + ") as numInstall");
        sqlBuilder.append(", " + DateUtils.combineDateSql("appSale." + TblApplicationSaleDaily.DATE) +  " as reportDate");
//        sqlBuilder.append(", DATE_FORMAT( appSale." + TblApplicationSaleDaily.DATE + ", '%Y/%m/%d') as reportDate");
        // FROM
        sqlBuilder.append(" FROM " + TblApplication.getTableName() + " app INNER JOIN " + TblApplicationSaleDaily.getTableName() + " appSale");
        sqlBuilder.append("     ON app." + TblApplication.ID + " = appSale." + TblApplicationSaleDaily.APP_ID);
        // WHERE
        sqlBuilder.append(" WHERE app." + TblApplication.ID + " = " + reportStatisticDto.getQueryAppId());
         sqlBuilder.append(" AND appSale." + TblApplicationSaleDaily.DATE + " BETWEEN " + DateUtils.combineDateSql("?"));
         sqlBuilder.append(" AND " +  DateUtils.combineDateSql("?"));
        // GROUP BY
        sqlBuilder.append(" GROUP BY app." + TblApplication.ID + ", appSale." + TblApplicationSaleDaily.DATE);
        
        try {
            System.out.println(sqlBuilder.toString());
            connection = DataSource.getInstance().getConnection();
            preparedStatement = connection.prepareStatement(sqlBuilder.toString());
            preparedStatement.setString(1, reportStatisticDto.getDateFrom());
            preparedStatement.setString(2, reportStatisticDto.getDateTo());
            resultSet = preparedStatement.executeQuery();
            SaleReportAdapter saleAdapter = new SaleReportAdapter();
            applicationSaleReportList = saleAdapter.convertApplicationSaleReportList(resultSet);
            
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null){
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                } 
            }
            
            if (preparedStatement != null){
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
               
            if (connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
                
        }
        return applicationSaleReportList;
    }*/
    
    /**
     * 
     * @method getAppSaleStatistic
     * @description
     * @author dung.dd
     * @date Nov 27, 2015
     * @param
     * @return List<ApplicationSaleReport>
     */
    public List<ApplicationSaleReport> getAppSaleStatistic(ReportStatisticDTO reportStatisticDto){
        List<ApplicationSaleReport> applicationSaleReportList = new ArrayList<>();
        
        String timeType = reportStatisticDto.getTimeType();
        String viewType = reportStatisticDto.getViewType();
        String firstDayOfWeek = Constant.WEEK_START_MONDAY_FIRSTDAY;
        int yearWeekSql = Constant.WEEK_START_MONDAY;
        
        StringBuilder selectBuilder = new StringBuilder();
        StringBuilder whereBuilder = new StringBuilder();
        StringBuilder groupByBuilder = new StringBuilder();
        StringBuilder orderByBuilder = new StringBuilder();
        StringBuilder sqlBuilder = new StringBuilder();
        ResultSet resultSet = null;
        
        whereBuilder.append(" WHERE app." + TblApplication.ID + " = " + reportStatisticDto.getQueryAppId());
        groupByBuilder.append(" GROUP BY app." + TblApplication.ID);
        orderByBuilder.append(" ORDER BY ");
        
        // SELECT
        selectBuilder.append("SELECT app." + TblApplication.ID).append(", app." + TblApplication.APP_CODE);
        selectBuilder.append(", app." + TblApplication.APP_NAME).append(", SUM(appSale." + TblApplicationSaleDaily.NUMBER_INSTALL + ") as numInstall");
        
        // SELECT, GROUP, ORDER follow view type condition
        if(viewType.equals(ViewType.DAYS.getValue())){
            selectBuilder.append(", " + DateUtils.combineDateSql("appSale." + TblApplicationSaleDaily.DATE) +  " as reportDate" );
            groupByBuilder.append(", reportDate");
            orderByBuilder.append(" reportDate asc");
            
        }else if(viewType.equals(ViewType.WEEKS.getValue())){
            selectBuilder.append(", YEARWEEK(" + TblApplicationSaleDaily.DATE +  ", " + yearWeekSql + ") as week" );
            String reportDate = "STR_TO_DATE(CONCAT(YEARWEEK(" + TblApplicationSaleDaily.DATE +  ", " + yearWeekSql + "), '" + firstDayOfWeek + "'),'%X%V %W')" ;
            selectBuilder.append(", " + DateUtils.combineDateSql(reportDate) + " as reportDate");
            groupByBuilder.append(", week");
            orderByBuilder.append(" week asc");
            
        }else if(viewType.equals(ViewType.MONTHS.getValue())){
            selectBuilder.append(", month(" + TblApplicationSaleDaily.DATE + ") as month" );
            selectBuilder.append(", year(" + TblApplicationSaleDaily.DATE +  ") as year" );
            groupByBuilder.append(", month, year");
            orderByBuilder.append(" year asc, month asc");
            
        }else {//if(viewType.equals(ViewType.YEARS.getValue())){
            selectBuilder.append(", year(" + TblApplicationSaleDaily.DATE +  ") as year" );
            groupByBuilder.append(", year");
            orderByBuilder.append(" year asc");
        }
        
        // sqlBuilder.append(", DATE_FORMAT( appSale." + TblApplicationSaleDaily.DATE + ", '%Y/%m/%d') as reportDate");
        // FROM
        selectBuilder.append(" FROM " + TblApplication.getTableName() + " app INNER JOIN " + TblApplicationSaleDaily.getTableName() + " appSale");
        selectBuilder.append("     ON app." + TblApplication.ID + " = appSale." + TblApplicationSaleDaily.APP_ID);
        
        // WHERE
        // WHERE follow time type condition
        whereBuilder.append(" AND appSale." + TblApplicationSaleDaily.DATE + " BETWEEN " + DateUtils.combineDateSql("?"));
        whereBuilder.append(" AND " +  DateUtils.combineDateSql("?"));
        
        
//        if(timeType.equals(TimeType.WEEK.getValue()) || timeType.equals(TimeType.RANGE.getValue()) || timeType.equals(TimeType.ALL_TIME.getValue())) {
//            whereBuilder.append(" AND appSale." + TblApplicationSaleDaily.DATE + " BETWEEN " + DateUtils.combineDateSql("?"));
//            whereBuilder.append(" AND " +  DateUtils.combineDateSql("?"));
//            
//        }else if(timeType.equals(TimeType.MONTH.getValue())) {
//            whereBuilder.append(" AND year(appSale.date) = ?");
//            whereBuilder.append(" AND month(appSale.date) = ?");
//            
//        }else if(timeType.equals(TimeType.YEAR.getValue())) {
//            whereBuilder.append(" AND year(appSale.date) = ?");
//        }
       
        
        sqlBuilder.append(selectBuilder).append(whereBuilder).append(groupByBuilder).append(orderByBuilder);
        
        try {
            
            connection = DataSource.getInstance().getConnection();
            
            preparedStatement = connection.prepareStatement(sqlBuilder.toString());
            preparedStatement.setString(1, DateUtils.ConvertDateToString(reportStatisticDto.getDateConditionFrom()));
            preparedStatement.setString(2, DateUtils.ConvertDateToString(reportStatisticDto.getDateConditionTo()));
            
//            if(timeType.equals(TimeType.WEEK.getValue()) || timeType.equals(TimeType.RANGE.getValue()) || timeType.equals(TimeType.ALL_TIME.getValue())) {
//                preparedStatement.setString(1, reportStatisticDto.getDateFrom());
//                preparedStatement.setString(2, reportStatisticDto.getDateTo());
//                
//            }else if(timeType.equals(TimeType.MONTH.getValue())) {
//                preparedStatement.setString(1, reportStatisticDto.getYear());
//                preparedStatement.setString(2, reportStatisticDto.getMonth());
//                
//                
//            }else if(timeType.equals(TimeType.YEAR.getValue())) {
//                preparedStatement.setString(1, reportStatisticDto.getDateFrom());
//            }
            System.out.println(preparedStatement.toString());
            resultSet = preparedStatement.executeQuery();
            SaleReportAdapter saleAdapter = new SaleReportAdapter();
            applicationSaleReportList = saleAdapter.convertApplicationSaleReportList(resultSet, viewType);
            
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null){
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                } 
            }
            
            if (preparedStatement != null){
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
               
            if (connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
                
        }
        return applicationSaleReportList;
    }
    
    /**
     * 
     * @method selectInstallNumberByYear
     * @description
     * @author dung.dd
     * @date Nov 27, 2015
     * @param
     * @return Integer
     */
    public List<ApplicationSaleReport> selectInstallNumberByYear(int yearFrom, int YearTo, int appId){
        
        StringBuilder sqlBuilder = new StringBuilder();
        ResultSet resultSet = null;
        List<ApplicationSaleReport> appSaleReportYearList = new ArrayList<>();
        
        sqlBuilder.append(" SELECT " + TblApplicationSaleYearly.YEAR + ", " + TblApplicationSaleYearly.NUMBER_INSTALL);
        sqlBuilder.append(" FROM   " + TblApplicationSaleYearly.getTableName());
        sqlBuilder.append(" WHERE " + TblApplicationSaleYearly.APP_ID +  " = ?  AND " + TblApplicationSaleYearly.YEAR + " >= ? AND " + TblApplicationSaleYearly.YEAR + " <=?  ");
        sqlBuilder.append(" ORDER BY " + TblApplicationSaleYearly.YEAR + " ASC");
        
        try {
            
            connection = DataSource.getInstance().getConnection();
            preparedStatement = connection.prepareStatement(sqlBuilder.toString());
            preparedStatement.setInt(1, appId);
            preparedStatement.setInt(2, yearFrom);
            preparedStatement.setInt(3, YearTo);
            
            System.out.println(preparedStatement.toString());
            resultSet = preparedStatement.executeQuery();
            SaleReportAdapter adapter = new SaleReportAdapter();
            appSaleReportYearList = adapter.convertApplicationSaleReportYearList(resultSet);
            
            
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null){
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                } 
            }
            
            if (preparedStatement != null){
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
               
            if (connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
                
        }
        return appSaleReportYearList;
    }
    
    
    /**
     * 
     * @method getItemSaleStatistic
     * @description
     * @author dung.dd
     * @date Nov 27, 2015
     * @param
     * @return List<ItemSaleReport>
     */
    public List<ItemSaleReport> getItemSaleStatistic(ReportStatisticDTO reportStatisticDto){
        List<ItemSaleReport> itemSaleReportList = new ArrayList<>();
        
        String timeType = reportStatisticDto.getTimeType();
        String viewType = reportStatisticDto.getViewType();
        String firstDayOfWeek = Constant.WEEK_START_MONDAY_FIRSTDAY;
        int yearWeekSql = Constant.WEEK_START_MONDAY;
        
        StringBuilder selectBuilder = new StringBuilder();
        StringBuilder whereBuilder = new StringBuilder();
        StringBuilder groupByBuilder = new StringBuilder();
        StringBuilder orderByBuilder = new StringBuilder();
        StringBuilder sqlBuilder = new StringBuilder();
        ResultSet resultSet = null;
        
        whereBuilder.append(" WHERE item." + TblApplicationItem.ID + " = " + reportStatisticDto.getQueryItemId());
        groupByBuilder.append(" GROUP BY item." + TblApplicationItem.ID);
        orderByBuilder.append(" ORDER BY ");
        
        // SELECT
        selectBuilder.append("SELECT item." + TblApplication.ID).append(", item." + TblApplicationItem.ITEM_CODE);
        selectBuilder.append(", item." + TblApplicationItem.ITEM_NAME).append(", SUM(itemSale." + TblItemSaleDaily.BOUGHT_NUMBER + ") as boughtNumber");
        
        // SELECT, GROUP, ORDER follow view type condition
        if(viewType.equals(ViewType.DAYS.getValue())){
            selectBuilder.append(", " + DateUtils.combineDateSql("itemSale." + TblItemSaleDaily.DATE) +  " as reportDate" );
            groupByBuilder.append(", reportDate");
            orderByBuilder.append(" reportDate asc");
            
        }else if(viewType.equals(ViewType.WEEKS.getValue())){
            selectBuilder.append(", YEARWEEK(" + TblItemSaleDaily.DATE +  ", " + yearWeekSql + ") as week" );
            String reportDate = "STR_TO_DATE(CONCAT(YEARWEEK(" + TblItemSaleDaily.DATE +  ", " + yearWeekSql + "), '" + firstDayOfWeek + "'),'%X%V %W')" ;
            selectBuilder.append(", " + DateUtils.combineDateSql(reportDate) + " as reportDate");
            groupByBuilder.append(", week");
            orderByBuilder.append(" week asc");
            
        }else if(viewType.equals(ViewType.MONTHS.getValue())){
            selectBuilder.append(", month(" + TblItemSaleDaily.DATE + ") as month" );
            selectBuilder.append(", year(" + TblItemSaleDaily.DATE +  ") as year" );
            groupByBuilder.append(", month, year");
            orderByBuilder.append(" year asc, month asc");
            
        }else if(viewType.equals(ViewType.YEARS.getValue())){
            selectBuilder.append(", year(" + TblItemSaleDaily.DATE +  ") as year" );
            groupByBuilder.append(", year");
            orderByBuilder.append(" year asc");
        }
        
        // sqlBuilder.append(", DATE_FORMAT( appSale." + TblApplicationSaleDaily.DATE + ", '%Y/%m/%d') as reportDate");
        // FROM
        selectBuilder.append(" FROM " + TblApplicationItem.getTableName() + " item INNER JOIN " + TblItemSaleDaily.getTableName() + " itemSale");
        selectBuilder.append("     ON item." + TblApplicationItem.ID + " = itemSale." + TblItemSaleDaily.ITEM_ID);
        
        // WHERE
        // WHERE follow time type condition
        whereBuilder.append(" AND itemSale." + TblApplicationSaleDaily.DATE + " BETWEEN " + DateUtils.combineDateSql("?"));
        whereBuilder.append(" AND " +  DateUtils.combineDateSql("?"));
        /*if(timeType.equals(TimeType.WEEK.getValue()) || timeType.equals(TimeType.RANGE.getValue()) || timeType.equals(TimeType.ALL_TIME.getValue())) {
            whereBuilder.append(" AND itemSale." + TblApplicationSaleDaily.DATE + " BETWEEN " + DateUtils.combineDateSql("?"));
            whereBuilder.append(" AND " +  DateUtils.combineDateSql("?"));
            
        }else if(timeType.equals(TimeType.MONTH.getValue())) {
            whereBuilder.append(" AND year(itemSale.date) = ?");
            whereBuilder.append(" AND month(itemSale.date) = ?");
            
        }else if(timeType.equals(TimeType.YEAR.getValue())) {
            whereBuilder.append(" AND year(itemSale.date) = ?");
        }*/
       
        
        sqlBuilder.append(selectBuilder).append(whereBuilder).append(groupByBuilder).append(orderByBuilder);
        
        try {
            System.out.println(sqlBuilder.toString());
            connection = DataSource.getInstance().getConnection();
            
            preparedStatement = connection.prepareStatement(sqlBuilder.toString());
            preparedStatement.setString(1, DateUtils.ConvertDateToString(reportStatisticDto.getDateConditionFrom()));
            preparedStatement.setString(2, DateUtils.ConvertDateToString(reportStatisticDto.getDateConditionTo()));
            /*if(timeType.equals(TimeType.WEEK.getValue()) || timeType.equals(TimeType.RANGE.getValue()) || timeType.equals(TimeType.ALL_TIME.getValue())) {
                preparedStatement.setString(1, reportStatisticDto.getDateFrom());
                preparedStatement.setString(2, reportStatisticDto.getDateTo());
                
            }else if(timeType.equals(TimeType.MONTH.getValue())) {
                preparedStatement.setString(1, reportStatisticDto.getYear());
                preparedStatement.setString(2, reportStatisticDto.getMonth());
                
                
            }else if(timeType.equals(TimeType.YEAR.getValue())) {
                preparedStatement.setString(1, reportStatisticDto.getDateFrom());
            }*/
            
            resultSet = preparedStatement.executeQuery();
            SaleReportAdapter saleAdapter = new SaleReportAdapter();
            itemSaleReportList = saleAdapter.convertItemSaleReportList(resultSet, viewType);
            
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null){
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                } 
            }
            
            if (preparedStatement != null){
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
               
            if (connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
                
        }
        return itemSaleReportList;
    }
    
    
    /**
     * 
     * @method selectBoughtNumberByYear
     * @description
     * @author dung.dd
     * @date Nov 27, 2015
     * @param
     * @return Integer
     */
    public List<ItemSaleReport> selectBoughtNumberByYear(int yearFrom, int YearTo, int itemId){
        
        StringBuilder sqlBuilder = new StringBuilder();
        ResultSet resultSet = null;
        List<ItemSaleReport> itemSaleReportYearList = new ArrayList<>();
        
        sqlBuilder.append(" SELECT " + TblItemSaleYearly.YEAR + ", " + TblItemSaleYearly.BOUGHT_NUMBER);
        sqlBuilder.append(" FROM   " + TblItemSaleYearly.getTableName());
        sqlBuilder.append(" WHERE " + TblItemSaleYearly.ITEM_ID +  " = ?  AND " + TblItemSaleYearly.YEAR + " >= ? AND " + TblItemSaleYearly.YEAR + " <=?  ");
        sqlBuilder.append(" ORDER BY " + TblItemSaleYearly.YEAR + " ASC");
        
        try {
            
            connection = DataSource.getInstance().getConnection();
            preparedStatement = connection.prepareStatement(sqlBuilder.toString());
            preparedStatement.setInt(1, itemId);
            preparedStatement.setInt(2, yearFrom);
            preparedStatement.setInt(3, YearTo);
            
            System.out.println(preparedStatement.toString());
            resultSet = preparedStatement.executeQuery();
            SaleReportAdapter adapter = new SaleReportAdapter();
            itemSaleReportYearList = adapter.convertItemSaleReportYearList(resultSet);
            
            
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null){
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                } 
            }
            
            if (preparedStatement != null){
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
               
            if (connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
                
        }
        return itemSaleReportYearList;
    }
    
    /**
     * 
     * @method getListApplicationForSaleReport
     * @description
     * @author dung.dd
     * @date Dec 4, 2015
     * @param
     * @return List<Application>
     */
    public List<Application> getListApplicationForSaleReport(List<Integer> appIdList, Date dateFrom, Date dateTo, int limit, int offset, String sortColumn, String direction) throws IOException, PropertyVetoException {
        List<Application> app = new ArrayList<>();
        ResultSet resultSet = null;
        ApplicationManagerAdapter appAdap = new ApplicationManagerAdapter();
        String querySQL = "SELECT app.*, SUM(appSale.number_install) AS amount FROM " + TblApplication.getTableName() + " app ";
        querySQL += " LEFT JOIN " + TblApplicationSaleDaily.getTableName() + " appSale ON (";
        querySQL += " app." + TblApplication.ID + " = " + TblApplicationSaleDaily.APP_ID;
        querySQL += " AND appSale." + TblApplicationSaleDaily.DATE + " BETWEEN " + DateUtils.combineDateSql("?");
        querySQL += " AND "  + DateUtils.combineDateSql("?");
        querySQL +=" ) ";
        querySQL += " WHERE app." + TblApplication.ID + " IN (";
         
        Integer length = appIdList.size();
        String idParam = "";
        for (int i = 0; i < length; i++) {
            idParam += ",?";
        }
        if(idParam != "") {
            idParam = idParam.substring(1);
        }
        
        
        
        querySQL += idParam + ")";
        
        querySQL += " GROUP BY app." + TblApplication.ID;
        
        try {
            connection = DataSource.getInstance().getConnection();
            if (StringUtils.isNotEmpty(sortColumn)) {
                querySQL += " ORDER BY " + sortColumn + " " + direction + " LIMIT ? OFFSET ?";
                
            } else {
                querySQL +=" ORDER BY update_date desc" + " LIMIT ? OFFSET ?";
            }

            preparedStatement = connection.prepareStatement(querySQL);
            preparedStatement.setString(1, DateUtils.ConvertDateToString(dateFrom));
            preparedStatement.setString(2, DateUtils.ConvertDateToString(dateTo));
            
            for (int i = 0; i < length; i++) {
                preparedStatement.setInt(i + 3, appIdList.get(i));
            }
            
            
            
            preparedStatement.setInt(length + 3, limit);
            preparedStatement.setInt(length + 4, offset);

            
            System.out.println(preparedStatement);
            resultSet = preparedStatement.executeQuery();
            if (resultSet != null) {
                while (resultSet.next()) {
                    app.add(appAdap.convertApplicationForSaleRepoprt(resultSet));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null)
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            if (preparedStatement != null)
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            if (connection != null)
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return app;
    }
    
    
    
    public List<Item> getListItemForSaleReport(List<Integer> itemIdList, Date dateFrom, Date dateTo, int limit, int offset, String sortColumn, String direction) throws IOException, PropertyVetoException {
        List<Item> itemList = new ArrayList<Item>();
        ResultSet resultSet = null;
        
        String querySQL = "SELECT item.*, SUM(itemSale.bought_number) AS amount FROM " + TblApplicationItem.getTableName() + " item ";
        querySQL += " LEFT JOIN " + TblItemSaleDaily.getTableName() + " itemSale ON (";
        querySQL += " item." + TblApplicationItem.ID + " = " + TblItemSaleDaily.ITEM_ID;
        querySQL += " AND itemSale." + TblItemSaleDaily.DATE + " BETWEEN " + DateUtils.combineDateSql("?");
        querySQL += " AND "  + DateUtils.combineDateSql("?");
        querySQL +=" ) ";
        querySQL += " WHERE item." + TblApplicationItem.ID + " IN (";
         
        Integer length = itemIdList.size();
        String idParam = "";
        for (int i = 0; i < length; i++) {
            idParam += ",?";
        }
        if(idParam != "") {
            idParam = idParam.substring(1);
        }
        
        querySQL += idParam + ")";
        
        querySQL += " GROUP BY item." + TblApplicationItem.ID;
        
        try {
            connection = DataSource.getInstance().getConnection();
            if (StringUtils.isNotEmpty(sortColumn)) {
                querySQL += " ORDER BY " + sortColumn + " " + direction + " LIMIT ? OFFSET ?";
                
            } else {
                querySQL +=" ORDER BY update_date desc" + " LIMIT ? OFFSET ?";
            }

            preparedStatement = connection.prepareStatement(querySQL);
            preparedStatement.setString(1, DateUtils.ConvertDateToString(dateFrom));
            preparedStatement.setString(2, DateUtils.ConvertDateToString(dateTo));
            
            for (int i = 0; i < length; i++) {
                preparedStatement.setInt(i + 3, itemIdList.get(i));
            }
            
            
            
            preparedStatement.setInt(length + 3, limit);
            preparedStatement.setInt(length + 4, offset);

            
            System.out.println(preparedStatement);
            resultSet = preparedStatement.executeQuery();
           
            if (resultSet != null) {
                Item item;
                while (resultSet.next()) {
                    item = new Item();
                    item.setItemCode(resultSet.getString("item_code"));
                    item.setItemName(resultSet.getString("item_name"));
                    item.setRevenue(resultSet.getDouble("revenue"));
                    item.setBoughtItems(resultSet.getInt("amount"));
                    item.setId(resultSet.getLong(TblApplicationItem.ID));
                    itemList.add(item);
                }
            }
            

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null)
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            if (preparedStatement != null)
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            if (connection != null)
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return itemList;
    }
    
    public static void main(String[] args) {
        ReportStatisticDTO reportStatisticDto = new ReportStatisticDTO();
        reportStatisticDto.setDateFrom("2015/11/18");
        
        SaleReportDao saleReportDao = new SaleReportDao();
        List<ApplicationSaleReport> applicationSaleReportList = saleReportDao.getAppSaleStatistic(reportStatisticDto);
        
        for (ApplicationSaleReport applicationSaleReport : applicationSaleReportList) {
            System.out.println(applicationSaleReport.getAppName());
            System.out.println(applicationSaleReport.getNumberInstall());
        }
        
    }
}
