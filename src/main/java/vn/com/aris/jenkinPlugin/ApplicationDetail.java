package vn.com.aris.jenkinPlugin;

import hudson.Extension;

import java.beans.PropertyVetoException;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;
import org.kohsuke.stapler.WebMethod;

import vn.com.aris.constant.ApplicationType;
import vn.com.aris.constant.Constant;
import vn.com.aris.dao.ApplicationItemDao;
import vn.com.aris.dto.ApplicationDTO;
import vn.com.aris.dto.DataTablesResponse;
import vn.com.aris.model.Application;
import vn.com.aris.model.Item;
import vn.com.aris.service.TblApplicationItemService;
import vn.com.aris.service.TblApplicationService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author nghiant
 * @date Nov 18, 2015
 */
@Extension
public class ApplicationDetail extends AbstractAction {
	private ApplicationDTO application;
	private TblApplicationService appService = new TblApplicationService();

	public ApplicationDTO getApplication() {
		return application;
	}

	@Override
	public String getUrl() {
		return "application-detail";
	}

	/**
	 * @method doInfoApplication
	 * @param String,idApp
	 * @description get Info Application by idApp
	 * @author nghiant
	 * @date Nov 18, 2015
	 */
	@WebMethod(name = "detail")
	public void doInfoApplication(@QueryParameter String idApp, StaplerRequest request, StaplerResponse response) throws ServletException, IOException {
		vn.com.aris.model.Application app = appService.findApplicationByIdApp(idApp);
		this.application = convertFromApplicationToApplicationDTO(app);
		response.forward(this, "index", request);
	}

	/**
	 * @method convertFromApplicationToApplicationDTO
	 * @param app
	 * @description convert Application to ApplicationDTO
	 * @author nghiant
	 * @date Nov 18, 2015
	 */
	private ApplicationDTO convertFromApplicationToApplicationDTO(Application app) {
		ApplicationDTO appDTO = new ApplicationDTO();
		appDTO.setAppName(app.getAppName());
		if (app.getType() != null) {
			if (app.getType().equals(ApplicationType.ANDROID.getValue())) {
				appDTO.setTypeStr("ANDROID");
			} else {
				appDTO.setTypeStr("IOS");
			}
		} else {
			appDTO.setTypeStr("");
		}
		appDTO.setAppCode(app.getAppCode());
		appDTO.setRevenue(app.getRevenue());
		appDTO.setRevenueItem(app.getRevenueItem());
		appDTO.setAppId(app.getAppId());
		return appDTO;
	}

	/**
	 * @method doData
	 * @param request,response
	 * @description get List Item by IdApp have paging
	 * @author nghiant
	 * @date Nov 18, 2015
	 */
	@WebMethod(name = { "data" })
	public String doData(StaplerRequest request, StaplerResponse response) throws IOException, PropertyVetoException {
		Long idApp = 1L;
		ApplicationItemDao service = new ApplicationItemDao();

		DataTablesResponse<Item> tables = new DataTablesResponse<>();
		List<Item> ls = new ArrayList<Item>();
		try {
			tables = parseDataRequest(request, response);
			idApp = tables.appId;
			Integer pageNumber = 1;
			long displayStart = tables.displayStart;
			Integer limit = Constant.TOTAL_RECORD_PER_PAGE;
			pageNumber = (int) (displayStart / limit + 1);
			Integer offset = (pageNumber - 1) * limit;
			tables.totalRecords = service.getTotalItemByIdApp(idApp);
			tables.recordsFiltered = tables.totalRecords;
			ls = service.getListItemByAppID(idApp, offset, tables.orderColumn,tables.orderDirection);
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

	/**
	 * @method parseDataRequest
	 * @param request,response
	 * @description map request to DataTablesResponse
	 * @author nghiant
	 * @date Nov 18, 2015
	 * @return DataTablesResponse<Item>
	 */
	@SuppressWarnings("unchecked")
	protected DataTablesResponse<Item> parseDataRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

	/**
	 * @method doUpdateItem
	 * @param idItem,itemName
	 * @description update item app by idItem
	 * @author nghiant
	 * @date Nov 18, 2015
	 * @return String
	 */
	@WebMethod(name = { "updateItem.json" })
	public String doUpdateItem(@QueryParameter Long idItem, @QueryParameter String itemName) throws IOException, PropertyVetoException {
		TblApplicationItemService service = new TblApplicationItemService();
		return service.updateItem(idItem, itemName);
	}

	/**
	 * @method doDeleteItem
	 * @param idItem
	 * @description delete item app by idItem
	 * @author nghiant
	 * @date Nov 18, 2015
	 * @return String
	 */
	@WebMethod(name = { "deleteItem" })
	public String doDeleteItem(@QueryParameter Long idItem) throws IOException, PropertyVetoException {
		TblApplicationItemService service = new TblApplicationItemService();
		return service.doDeleteItem(idItem);
	}
}
