package vn.com.aris.jenkinPlugin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hudson.Extension;
import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;
import org.kohsuke.stapler.WebMethod;
import vn.com.aris.constant.Constant;
import vn.com.aris.dao.ApplicationManagementDao;
import vn.com.aris.dto.ApplicationDTO;
import vn.com.aris.dto.DataTablesResponse;
import vn.com.aris.model.Application;
import vn.com.aris.service.ApplicationManagementService;
import vn.com.aris.thread.GetDataThread;
import vn.com.aris.utils.ConvertData;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.PropertyVetoException;
import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Extension
public class ApplicationManagement extends AbstractAction {

	private List<ApplicationDTO> appList;
	private int getType;

	@Override
	public String getUrl() {
		return "application-management";
	}

	public List<ApplicationDTO> getAppList() {
		return this.appList;
	}

	public int getAppType() {
		return this.getType;
	}

	public void doSomething(@QueryParameter String name) {
		System.out.println("You will do something!!!");
		System.out.println(name);
	}

	public Integer getTotalItemPerPage() {
		return Constant.TOTAL_RECORD_PER_PAGE;
	}

	@WebMethod(name = { "data" })
	public String doData(StaplerRequest request, StaplerResponse response) {
		ApplicationManagementService service = new ApplicationManagementService();

		DataTablesResponse<ApplicationDTO> tables = new DataTablesResponse<>();
		List<ApplicationDTO> ls = new ArrayList<ApplicationDTO>();
		try {
			tables = parseTableDataRequest(request, response);
			Integer pageNumber = 1;
			long displayStart = tables.displayStart;
			Integer limit = Constant.TOTAL_RECORD_PER_PAGE;
			pageNumber = (int) (displayStart / limit + 1);
			Integer offset = (pageNumber - 1) * limit;
			tables.totalRecords = service.getTotalApp();
			tables.recordsFiltered = tables.totalRecords;
			ls = service.getListApplicationDTO(limit, offset, tables.orderColumn, tables.orderDirection);
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

	@WebMethod(name = { "add-item" })
	public void doAddData(@QueryParameter String id, StaplerRequest request, StaplerResponse response) {
		ApplicationManagementService service = new ApplicationManagementService();
		List<ApplicationDTO> ls = new ArrayList<ApplicationDTO>();
		int type = 0;
		try {
			ls = service.getListApplicationDTO();
			this.appList = ls;
			if (id != null) {
				type = service.getApplicationByID(Integer.parseInt(id));
			}
			this.getType = type;
			response.forward(this, "additem", request);
		} catch (ServletException | PropertyVetoException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@WebMethod(name = { "addnew" })
	public void doNewApplication(StaplerRequest request, StaplerResponse response) {
		try {
			response.forward(this, "new", request);
		} catch (ServletException | IOException e) {
			e.printStackTrace();
		}
	}

	// @RequirePOST
	@WebMethod(name = { "save-application" })
	public void doSaveApplication(StaplerRequest request, StaplerResponse response) throws Exception {
		try {

			String name = request.getParameter("appName");
			ApplicationManagementDao dao = new ApplicationManagementDao();
			Application app = new Application();

			app.setAppCode(request.getParameter("packages"));
			app.setAppName(name);
			String typeName = request.getParameter("type").toString();
			
			app.setType(Integer.parseInt(typeName));
			
			app.setReleaseDate(ConvertData.convertFromStringToDate(request.getParameter("releaveDate"), "yyyy/MM/dd"));
			app.setUpdateStatus("0");
			app.setRevenueItem(0);
			app.setRevenue(0);
			Boolean result = false;
			try {
				result = dao.saveApplication(app);
				if (result) {
					response.sendRedirect("index");
				}
			} catch (PropertyVetoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}

		} catch (ServletException | IOException e) {
			e.printStackTrace();
		}
	}

	@WebMethod(name = { "save-item" })
	public void doSaveData(@QueryParameter String appName, @QueryParameter String packages, @QueryParameter String itemName, StaplerRequest request,
			StaplerResponse response) {
		ApplicationManagementService service = new ApplicationManagementService();
		Boolean saveOk = false;
		try {
			saveOk = service.saveItem(appName, packages, itemName);
			if (saveOk) {
				response.sendRedirect("index");
			}
		} catch (PropertyVetoException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected DataTablesResponse<ApplicationDTO> parseTableDataRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
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

	@WebMethod(name = { "gettingData" })
	public void doGettingDataReport(StaplerResponse response, StaplerRequest request, @QueryParameter String data) {

		if (StringUtils.isNotEmpty(data)) {
			ApplicationManagementService service = new ApplicationManagementService();
			String[] apps = data.split(Constant.KEY_APPLICATION_TO_SPLIT);
			List<ApplicationDTO> applist = new ArrayList<>();
			Map<String, Object> rs = new HashMap<>();
			for (int i = 0; i < apps.length; i++) {
				try {
					applist.add(service.convertStringArrayToApplictionDTO(apps[i].split("@")));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			try {
				service.updateStatusGettingData(applist);

			} catch (ParseException | IOException | PropertyVetoException e) {
				e.printStackTrace();
			}

			GetDataThread getDataThread = new GetDataThread();
			getDataThread.start();
			try {
				response.forward(this, "index", request);
			} catch (ServletException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// @RequirePOST
	@WebMethod(name = {"update-application" })
	public String doUpdateApplication(@QueryParameter String appId
			, @QueryParameter String appName
			, @QueryParameter String packages
			, @QueryParameter String type,
		@QueryParameter String releaseDate) throws Exception {
	
		ApplicationManagementDao dao = new ApplicationManagementDao();
		Application app = null;

		if (StringUtils.isNotEmpty(appId)) {
			app = dao.getApplicationByID(Integer.parseInt(appId));
		}

		if (app != null) {
			app.setAppName(appName);
			app.setType(Integer.parseInt(type));
			app.setAppCode(packages);
			if (StringUtils.isNotEmpty(releaseDate)) {
				app.setReleaseDate(ConvertData.convertFromStringToDate(releaseDate, "yyyy-MM-dd"));
				app.setUpdateStatus("0");
			}

			Boolean result = false;
			try {
				result = dao.updateApplication(app);
				if (result) {
					return "Success";
				}
			} catch (PropertyVetoException e) {
				e.printStackTrace();
			}
		}
		return "";
	}

	@WebMethod(name = { "delete-application" })
	public String doDeleteApplication(@QueryParameter int idApp) throws Exception {
		ApplicationManagementDao dao = new ApplicationManagementDao();
		return dao.deleteApplication(idApp);
	}

	public static void main(String[] args) throws Exception {
		ApplicationManagement a = new ApplicationManagement();
		System.out.println(a.doDeleteApplication(1));
	}
}
