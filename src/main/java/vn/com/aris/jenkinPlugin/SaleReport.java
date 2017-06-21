package vn.com.aris.jenkinPlugin;

import java.beans.PropertyVetoException;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;
import org.kohsuke.stapler.WebMethod;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import hudson.Extension;
import vn.com.aris.constant.ApplicationType;
import vn.com.aris.constant.Constant;
import vn.com.aris.constant.TimeType;
import vn.com.aris.constant.ViewType;
import vn.com.aris.dao.ApplicationItemDao;
import vn.com.aris.dao.adapter.SaleReportAdapter;
import vn.com.aris.dto.ApplicationDTO;
import vn.com.aris.dto.DataTablesResponse;
import vn.com.aris.dto.ReportStatisticDTO;
import vn.com.aris.dto.SaleReportCsvDto;
import vn.com.aris.model.Application;
import vn.com.aris.model.ApplicationSaleReport;
import vn.com.aris.model.Item;
import vn.com.aris.model.ItemSaleReport;
import vn.com.aris.service.ApplicationManagementService;
import vn.com.aris.service.SaleReportService;
import vn.com.aris.service.TblApplicationService;
import vn.com.aris.utils.DateUtils;

@Extension
public class SaleReport extends AbstractAction {

	private SaleReportService saleReportService;
	
//	private ReportStatisticDTO reportStatisticDTO;
	private List<String> lineChartList;
	private List<String> barChartList;
	private TblApplicationService appService = new TblApplicationService();
//	public ReportStatisticDTO getReportStatisticDTO() {
//		return reportStatisticDTO;
//	}
	public Integer getTotalItemPerPage(ReportStatisticDTO reportStatisticDTO) {
		return Constant.TOTAL_RECORD_PER_PAGE;
	}
	
	public List<String> getLineChartList() {
		return lineChartList;
	}

	public List<String> getBarChartList() {
		return barChartList;
	}

	@Override
	public String getUrl() {
		return "report-statistc";
	}

	public void doSomething(@QueryParameter String name) {
		System.out.println("You will do something!!!");
		System.out.println(name);
	}

	
	/**
	 * 
	 * @method createDateCondition
	 * @description
	 * @author dung.dd
	 * @date Nov 26, 2015
	 * @param
	 * @return void
	 */
	private void createDateCondition(ReportStatisticDTO reportStatisticDTO) {
        String timeType = reportStatisticDTO.getTimeType();
        Calendar calendar = Calendar.getInstance();
        Date dateConditionFrom = null;
        Date dateConditionTo = null;
        
        
        if(BooleanUtils.isTrue(reportStatisticDTO.getExportCsv()) && ( timeType.equals(TimeType.YEAR.getValue()) || timeType.equals(TimeType.MONTH.getValue()))) {
            
            int year = 0;
            int month = 0;
            
            year = NumberUtils.toInt(reportStatisticDTO.getYear());
            if(timeType.equals(TimeType.MONTH.getValue())) {
                month = NumberUtils.toInt(reportStatisticDTO.getMonth()) - 1;
            }
            calendar.set(year, month, 1, 0, 0, 0);
            dateConditionFrom = calendar.getTime();
            
            if(timeType.equals(TimeType.MONTH.getValue())) {
                calendar.add(Calendar.MONTH, 1);
                
            } else {
                calendar.add(Calendar.YEAR, 1);
            }
            
            calendar.add(Calendar.DATE, -1);
            dateConditionTo = calendar.getTime();
            
        }else {
            dateConditionFrom = DateUtils.convertDateFromSql(reportStatisticDTO.getDateFrom());
            dateConditionTo = DateUtils.convertDateFromSql(reportStatisticDTO.getDateTo());
            
        }
        
        reportStatisticDTO.setDateConditionFrom(dateConditionFrom);
        reportStatisticDTO.setDateConditionTo(dateConditionTo);
        
        
    }
	/**
	 * 
	 * @method convertIdStrToList
	 * @description
	 * @author dung.dd
	 * @date Nov 26, 2015
	 * @param
	 * @return List<Integer>
	 */
	public List<Integer> convertIdStrToList(String idStr) {
		List<Integer> result = null;
		if (StringUtils.isNotEmpty(idStr)) {
			result = new ArrayList<Integer>();
			List<String> rawId = new ArrayList<String>();
			rawId = new ArrayList<String>(Arrays.asList(idStr.split(",")));

			if (CollectionUtils.isNotEmpty(rawId)) {
				for (String id : rawId) {
					result.add(NumberUtils.toInt(id));
				}
			}
		}
		return result;
	}
	
	private void processParameter(ReportStatisticDTO reportStatisticDTO) {
	    String timeType = reportStatisticDTO.getTimeType();
	    String viewType = reportStatisticDTO.getViewType();
	    String dateFrom = reportStatisticDTO.getDateFrom();
	    
        String appId = reportStatisticDTO.getAppId();
        String itemId = reportStatisticDTO.getItemId();
        List<Integer> appIdList = convertIdStrToList(appId);
        reportStatisticDTO.setAppIdList(appIdList);
	    
        
        if((StringUtils.equals(timeType, TimeType.YEAR.getValue()) || StringUtils.equals(timeType, TimeType.ALL_TIME.getValue())) 
                && StringUtils.equals(viewType, ViewType.YEARS.getValue())) {
            List<String> appCodes = new ArrayList<>();
            appCodes.add(appId);
            try {
                Date dateConditionFrom = appService.getMinReleaseDate(appIdList);
                // Get begin date of the year
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dateConditionFrom);
                calendar.set(Calendar.MONTH, 0);
                calendar.set(Calendar.DATE, 1);
                dateConditionFrom = calendar.getTime();
                
                dateFrom = DateUtils.ConvertDateToString(dateConditionFrom);
                Date dateConditionTo = new Date();
                // Get end date of the year
                calendar.setTime(dateConditionTo);
                calendar.set(Calendar.MONTH, 12);
                calendar.set(Calendar.DATE, 0);
                dateConditionTo = calendar.getTime();
                String dateTo = DateUtils.ConvertDateToString(dateConditionTo);
                reportStatisticDTO.setDateFrom(dateFrom);
                reportStatisticDTO.setDateTo(dateTo);
                
//                Calendar caledar = Calendar.getInstance();
//                caledar.setTime(dateConditionFrom);
//                if(timeType.equals(TimeType.YEAR.getValue())) {
//                    reportStatisticDTO.setYear(String.valueOf(caledar.get(Calendar.YEAR)));
//                }
//                
//                caledar.setTime(dateConditionTo);
//                if(timeType.equals(TimeType.YEAR.getValue())) {
//                    reportStatisticDTO.setYear(String.valueOf(caledar.get(Calendar.YEAR)));
//                }
                
                
                
                
            } catch (IOException | PropertyVetoException e1) {
                e1.printStackTrace();
            }
        }
	    
	    
	    
	    // process for date condition
        if(timeType != null) {
            if( timeType.equals(TimeType.MONTH.getValue())) {
                String[] arrStr = dateFrom.split(Constant.SPACE_DATE);
                if(arrStr != null && arrStr.length >= 2) {
                    reportStatisticDTO.setYear(arrStr[0]);
                    reportStatisticDTO.setMonth(arrStr[1]);
                }
            }else if(timeType.equals(TimeType.YEAR.getValue())) {
                reportStatisticDTO.setYear(dateFrom);
            }
        }
        
        
        
       

        
        String mode = reportStatisticDTO.getMode();
        
        // Get Item Id List
        List<Integer> itemIdList = new ArrayList<>();
        if(StringUtils.equals(mode, Constant.REPORT_VIEW_MODE_ITEM) && !StringUtils.isNotEmpty(itemId)) {
            
            SaleReportService service = new SaleReportService();
            List<Item> lstItem = service.getListItemByAppID(NumberUtils.toInt(appId));
            if(lstItem != null && lstItem.size() > 0){
                for (Item item : lstItem) {
                    itemIdList.add(item.getId().intValue());
                }
            }
            
        }else {
            itemIdList = convertIdStrToList(itemId);
            
        }
        reportStatisticDTO.setItemIdList(itemIdList);
        
        createDateCondition(reportStatisticDTO);
        System.out.println(reportStatisticDTO.toString());
	}

	/**
	 * 
	 * @method doLstApplication
	 * @description
	 * @author nghia.nt
	 * @date Nov 26, 2015
	 * @param
	 * @return String
	 */
	@WebMethod(name = { "getLstApplication" })
	public String doLstApplication() throws IOException, PropertyVetoException {
		List<Application> lstApplication = appService.getLstApplication();
		StringBuilder rs = new StringBuilder();
		rs.append("{");
		rs.append(" \"data\" ");
		rs.append(":");
		rs.append("[");

		for (int i = 0; i < lstApplication.size(); i++) {
			Application app = lstApplication.get(i);
			String type = "";
			if (app.getType().equals(ApplicationType.ANDROID.getValue())) {
				type = "ANDROID";
			} else {
				type="IOS";
			}
			rs.append("{");
			rs.append(" \"appId\" ");
			rs.append(":");
			rs.append(app.getAppId());
			rs.append(",");
			rs.append(" \"nameApp\" ");
			rs.append(":");
			rs.append(" \"" + app.getAppName()+" - "+type + "\" ");
			rs.append("}");
			if (i < lstApplication.size() - 1) {
				rs.append(",");
			}

		}
		rs.append("]");
		rs.append("}");
		return rs.toString();
	}
	
	
	/**
	 * 
	 * @method doLstItem
	 * @description
	 * @author dung.dd
	 * @date Nov 29, 2015
	 * @param
	 * @return Integer
	 */
	 @WebMethod(name = { "getAppId" })
	 public Integer doAppId(@QueryParameter String appName) throws IOException, PropertyVetoException {
	     TblApplicationService applicationService = new TblApplicationService();
	     Integer appId = applicationService.selectAppIdFromName(appName);
	     return appId;
	 }
	
	
	/**
     * 
     * @method doLstApplication
     * @description
     * @author dung.dd
     * @date Nov 29, 2015
     * @param
     * @return String
     */
    @WebMethod(name = { "getLstItem" })
    public String doLstItem(@QueryParameter Integer appId, @QueryParameter String appName) throws IOException, PropertyVetoException {
        if(StringUtils.isNotEmpty(appName)) {
            TblApplicationService applicationService = new TblApplicationService();
            appId = applicationService.selectAppIdFromName(appName);
        }
        SaleReportService service = new SaleReportService();
        List<Item> lstItem = service.getListItemByAppID(appId);
        StringBuilder rs = new StringBuilder();
        rs.append("{");
        rs.append(" \"data\" ");
        rs.append(":");
        rs.append("[");

        for (int i = 0; i < lstItem.size(); i++) {
            Item item = lstItem.get(i);
            rs.append("{");
            rs.append(" \"itemId\" ");
            rs.append(":");
            rs.append(item.getId());
            rs.append(",");
            rs.append(" \"itemName\" ");
            rs.append(":");
            rs.append(" \"" + item.getItemName()+" \" ");
            rs.append("}");
            if (i < lstItem.size() - 1) {
                rs.append(",");
            }

        }
        rs.append("]");
        rs.append("}");
        return rs.toString();
    }
	
    /**
     * 
     * @method doGetMinAppReleaseDate
     * @description
     * @author dung.dd
     * @date Dec 3, 2015
     * @param
     * @return String
     */
	@WebMethod(name = {"getMinAppReleaseDate"})
	public String doGetMinAppReleaseDate() {
	    Date date = null;
        try {
            date = appService.getMinReleaseDate(null);
        } catch (IOException | PropertyVetoException e) {
            e.printStackTrace();
        }
	    return DateUtils.ConvertDateToString(date);
	}

	// @RequirePOST    
	@WebMethod(name = { "getChartData" })
	public String doStart(@QueryParameter String statisticType, @QueryParameter String viewType, @QueryParameter String timeType,
			@QueryParameter String dateFrom, @QueryParameter String dateTo, @QueryParameter String appId, @QueryParameter String itemId,
			@QueryParameter String appName,
			@QueryParameter Integer chartType, @QueryParameter String mode, StaplerRequest request, StaplerResponse response) {
	    
	    ReportStatisticDTO reportStatisticDTO = new ReportStatisticDTO();
		reportStatisticDTO.setStatisticType(statisticType);
        reportStatisticDTO.setViewType(viewType);
        reportStatisticDTO.setTimeType(timeType);
        reportStatisticDTO.setDateFrom(dateFrom);
        reportStatisticDTO.setDateTo(dateTo);
        reportStatisticDTO.setItemId(itemId);
        reportStatisticDTO.setMode(mode);
        reportStatisticDTO.setExportCsv(false);
        
        // If Item mode, and we only just get appName, get appId
        if(StringUtils.equals(Constant.REPORT_VIEW_MODE_ITEM, mode)) {
            TblApplicationService applicationService = new TblApplicationService();
            try {
                appId = String.valueOf(applicationService.selectAppIdFromName(appName));
            } catch (IOException | PropertyVetoException e) {
                e.printStackTrace();
            }
        }
        reportStatisticDTO.setAppId(appId);
        // Process parameter before do action
        processParameter(reportStatisticDTO);
        
        
        
        
        
		// Check condition parameters
		if (StringUtils.isEmpty(appId) && StringUtils.isEmpty(itemId) && StringUtils.isEmpty(appName)) {
			return "error";
		}
		
		
		//
		saleReportService = new SaleReportService();
		
		//hien get min release date start
		/*List<String> appCodes = new ArrayList<>();
		appCodes.add(appId);
		
		try {
		   
			Date d = appService.getMinReleaseDate(appIdList);
			reportStatisticDTO.setMinAppReleaseDate(DateUtils.ConvertDateToString(d));
		} catch (IOException | PropertyVetoException e1) {
			e1.printStackTrace();
		}*/
		// hien get min release date end
		
		List<Integer> appIdList = reportStatisticDTO.getAppIdList();
		List<String> chartData = new ArrayList<>();
		
		if(StringUtils.equals(Constant.REPORT_VIEW_MODE_ITEM, mode)) {
		    if(Constant.CHART_LINES_TYPE == chartType){
                chartData = saleReportService.getLineChartItemStatistic(reportStatisticDTO);
                
            }else {
                chartData = saleReportService.getBarChartItemStatistic(reportStatisticDTO);
             // add app name list
                try {
                    if(chartData != null && chartData.size() > 0) {
                        List<String> appNameList = saleReportService.getItemNameList(reportStatisticDTO.getItemIdList());
                        chartData.add(appNameList.toString());
                    }
                } catch (IOException | PropertyVetoException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            
        }else { // App statistic
            if(Constant.CHART_LINES_TYPE == chartType){
                chartData = saleReportService.getLineChartAppStatistic(reportStatisticDTO);
                
            }else {
                chartData = saleReportService.getBarChartAppStatistic(reportStatisticDTO);
             // add app name list
                try {
                    if(chartData != null && chartData.size() > 0) {
                        List<String> appNameList = saleReportService.getAppNameList(appIdList);
                        chartData.add(appNameList.toString());
                    }
                } catch (IOException | PropertyVetoException e) {
                    e.printStackTrace();
                }
            }
        }
		   
		
//		reportStatisticDTO.setLineChartList(chartData);
		
		StringBuilder data = new StringBuilder();	
		data.append(chartData.toString());
		
		return data.toString();

	}

	protected DataTablesResponse<ApplicationDTO> parseTableDataRequest(HttpServletRequest request,	HttpServletResponse response) throws ServletException, IOException {
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = request.getReader();
		DataTablesResponse<ApplicationDTO> tables = new DataTablesResponse<>();
		try {
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line).append('\n');
			}
			ObjectMapper mapper = new ObjectMapper();
			String s = sb.toString();
			System.out.println(sb.toString());
			tables = mapper.readValue(s, DataTablesResponse.class);
		} finally {
			reader.close();
		}
		return tables;
	}

	@WebMethod(name = { "application-data" })
	public String doData(StaplerRequest request, StaplerResponse response) {
//		ApplicationManagementService service = new ApplicationManagementService();
	    SaleReportService service = new SaleReportService();

		DataTablesResponse<ApplicationDTO> tables = new DataTablesResponse<>();
		List<ApplicationDTO> ls = new ArrayList<ApplicationDTO>();
		try {
			tables = parseTableDataRequest(request, response);
			Integer pageNumber = 1;
			long displayStart = tables.displayStart;
			Integer limit = Constant.TOTAL_RECORD_PER_PAGE;
			pageNumber = (int) (displayStart / limit + 1);
			Integer offset = (pageNumber - 1) * limit;
			
			
			ReportStatisticDTO reportStatisticDTO = new ReportStatisticDTO();
			
			reportStatisticDTO.setAppId(tables.appIdStr);
			reportStatisticDTO.setTimeType(tables.timeType);
			reportStatisticDTO.setStatisticType(tables.statisticType);
			reportStatisticDTO.setDateFrom(tables.dateFrom);
			reportStatisticDTO.setDateTo(tables.dateTo);
			reportStatisticDTO.setExportCsv(true);
			
			processParameter(reportStatisticDTO);
			List<Integer> appIdList = reportStatisticDTO.getAppIdList();
			Date dateFrom = reportStatisticDTO.getDateConditionFrom();
			Date dateTo = reportStatisticDTO.getDateConditionTo();
			
			if(appIdList != null && appIdList.size() > 0) {
			    tables.totalRecords = appIdList.size();
	            tables.recordsFiltered = appIdList.size();
			}
//			ls = service.getListApplicationDTO(limit, offset, tables.orderColumn, tables.orderDirection);
			ls = service.getListApplicationForSaleReport(appIdList, dateFrom, dateTo, limit, offset, tables.orderColumn, tables.orderDirection);
//			getListApplicationForSaleReport
		} catch (IOException | PropertyVetoException | ServletException e) {
			e.printStackTrace();
		}
		tables.data = ls;
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(tables);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "";
	}

	@WebMethod(name = { "item-data" })
	public String doDataItem(StaplerRequest request, StaplerResponse response) throws IOException, PropertyVetoException {
		Long idApp = 1L;
		SaleReportService service = new SaleReportService();

		DataTablesResponse<Item> tables = new DataTablesResponse<>();
		List<Item> ls = new ArrayList<Item>();
		try {
			tables = parseDataItemRequest(request, response);
			idApp = tables.appId;
			Integer pageNumber = 1;
			long displayStart = tables.displayStart;
			Integer limit = Constant.TOTAL_RECORD_PER_PAGE;
			pageNumber = (int) (displayStart / limit + 1);
			Integer offset = (pageNumber - 1) * limit;
			
			ReportStatisticDTO reportStatisticDTO = new ReportStatisticDTO();
			reportStatisticDTO.setAppId(tables.appIdStr);
			reportStatisticDTO.setItemId(tables.itemIdStr);
            reportStatisticDTO.setTimeType(tables.timeType);
            reportStatisticDTO.setStatisticType(tables.statisticType);
            reportStatisticDTO.setDateFrom(tables.dateFrom);
            reportStatisticDTO.setDateTo(tables.dateTo);
            reportStatisticDTO.setExportCsv(true);
            
            processParameter(reportStatisticDTO);
            List<Integer> itemIdList = reportStatisticDTO.getItemIdList();
            Date dateFrom = reportStatisticDTO.getDateConditionFrom();
            Date dateTo = reportStatisticDTO.getDateConditionTo();
            
            if(itemIdList != null && itemIdList.size() > 0) {
                tables.totalRecords = itemIdList.size();
                tables.recordsFiltered = itemIdList.size();
            }
			
            ls = service.getListItemForSaleReport(itemIdList, dateFrom, dateTo, limit, offset, tables.orderColumn, tables.orderDirection);
            
//			tables.totalRecords = service.getTotalItemByIdApp(idApp);
//			tables.recordsFiltered = tables.totalRecords;
//			ls = service.getListItemByAppID(idApp, offset, tables.orderColumn,tables.orderDirection);
		} catch (IOException | PropertyVetoException | ServletException e) {
			e.printStackTrace();
		}
		tables.data = ls;
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(tables);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "";
	}

	//
	protected DataTablesResponse<Item> parseDataItemRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = request.getReader();
		DataTablesResponse<Item> tables = new DataTablesResponse<>();
		try {
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line).append('\n');
			}
			ObjectMapper mapper = new ObjectMapper();
			String s = sb.toString();
			System.out.println(sb.toString());
			tables = mapper.readValue(s, DataTablesResponse.class);
		} finally {
			reader.close();
		}
		return tables;
	}
	
	@WebMethod(name = { "export-csv" })
	public void doExportCSV(@QueryParameter String statisticType,
	        @QueryParameter String viewType, @QueryParameter String timeType,
			@QueryParameter String dateFrom, @QueryParameter String dateTo,
			@QueryParameter String appId, @QueryParameter String itemId, @QueryParameter String mode,
			@QueryParameter Boolean exportCsv,
			StaplerRequest request, StaplerResponse response) throws IOException, PropertyVetoException {
		
	    String filename = "";
			if(StringUtils.equals(Constant.REPORT_VIEW_MODE_ITEM, mode)) {
				filename = "Item_Report.csv";
			} else {
				filename = "Application_Report.csv";
			}
		    response.setContentType("application/octet-stream");
	        response.setHeader("Content-Disposition","attachment;filename=" + filename);				
			ServletOutputStream out = response.getOutputStream();
			
			// Work with parameters
			ReportStatisticDTO reportStatisticDTO = new ReportStatisticDTO();
	        reportStatisticDTO.setStatisticType(statisticType);
	        reportStatisticDTO.setViewType(viewType);
	        reportStatisticDTO.setTimeType(timeType);
	        reportStatisticDTO.setDateFrom(dateFrom);
	        reportStatisticDTO.setDateTo(dateTo);
	        reportStatisticDTO.setAppId(appId);
	        reportStatisticDTO.setItemId(itemId);
	        reportStatisticDTO.setMode(mode);
	        reportStatisticDTO.setExportCsv(exportCsv);
	        
	        // Process parameter before do action
	        processParameter(reportStatisticDTO);
			
	        // Check condition parameters
			if (StringUtils.isEmpty(appId)) {
			    return ;
			}
	        
			//
			saleReportService = new SaleReportService();
			SaleReportAdapter adapter = new SaleReportAdapter();
			List<SaleReportCsvDto> csvList;
			
			if(StringUtils.equals(Constant.REPORT_VIEW_MODE_ITEM, mode)) {
			    List<ItemSaleReport> saleReports = saleReportService.getAllItemSaleStatistic(reportStatisticDTO);
                csvList = adapter.convertToItemCsvList(saleReports);
			    
			}else {
			    List<ApplicationSaleReport> saleReports = saleReportService.getAllAppSaleStatistic(reportStatisticDTO);
                csvList = adapter.convertToAppCsvList(saleReports);
			}
			
			if(csvList == null) {
			    return ;
			}
			
			String csvFileContent = generateCsvFileBuffer(csvList);
			out.write(csvFileContent.getBytes());
			
			out.flush();
			out.close();
	}
	
   private static String generateCsvFileBuffer(List<SaleReportCsvDto> ls)
   {
	    		// StringBuffer writer = new StringBuffer();
	    		String result = "";
	            // create mapper and schema
	            CsvMapper mapper = new CsvMapper();
	            CsvSchema schema = mapper.schemaFor(SaleReportCsvDto.class).withHeader();
	            schema = schema.withColumnSeparator(',');

	            // output writer
	            try {
	                result = mapper.writer(schema).writeValueAsString(ls);
//					writer.append(usersList);
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            return result;				
	}
   
   
}
