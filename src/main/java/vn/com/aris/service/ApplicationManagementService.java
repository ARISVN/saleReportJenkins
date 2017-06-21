package vn.com.aris.service;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import vn.com.aris.constant.Constant;
import vn.com.aris.dao.ApplicationManagementDao;
import vn.com.aris.dto.ApplicationDTO;
import vn.com.aris.model.Application;
import vn.com.aris.model.Item;

public class ApplicationManagementService {
	ApplicationManagementDao dao = new ApplicationManagementDao();

	public ApplicationManagementService() {
		super();
		this.dao = new ApplicationManagementDao();
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
		return dto;
	}

	// tam.ph added - 2015/11/23
		public Item convertItemObject(Item item) {
			if (item == null) {
				return new Item();
			}
			Item newitem = new Item();
			item.setId(item.getId());
			item.setAppId(item.getAppId());
			item.setItemCode(item.getItemCode());
			item.setItemName(item.getItemName());
			item.setRevenue(item.getRevenue());
			item.setBoughtItems(item.getBoughtItems());	
			return newitem;
		}
		// tam.ph ending
	
	public List<ApplicationDTO> getListApplicationDTO(int limit, int offset, String sortColumn, String orderDirection)
			throws IOException, PropertyVetoException {
		List<ApplicationDTO> rs = new ArrayList<>();
		List<Application> app = dao.getListApplication(limit, offset, sortColumn, orderDirection);
		if(CollectionUtils.isNotEmpty(app)) {
			for (Application application : app) {
				ApplicationDTO dto = convertModelToApplictionDTO(application);
				rs.add(dto);
			}
		}
		return rs;
	}
	
	// tam.ph added 2015/11/23
	public List<ApplicationDTO> getListApplicationDTO()
			throws IOException, PropertyVetoException {
		List<ApplicationDTO> rs = new ArrayList<>();
		List<Application> app = dao.getListApplication();
		if(CollectionUtils.isNotEmpty(app)) {
			for (Application application : app) {
				ApplicationDTO dto = convertModelToApplictionDTO(application);
				rs.add(dto);
			}
		}
		return rs;
	}
	// tam.ph ending
	
	// tam.ph added 2015/11/23
		public int getApplicationByID(int id) throws IOException, PropertyVetoException {
			ApplicationDTO app = new ApplicationDTO();
			if(app != null) {
				app = convertModelToApplictionDTO(dao.getApplicationByID(id));
			}
			return app.getType();
		}
	// tam.ph ending
	
    // tam.ph added 2015/11/23
		public Boolean saveItem(String appName, String packages, String itemName)throws IOException, PropertyVetoException {
			Item item =new Item();
			item.setAppId(Long.parseLong(appName));
			item.setItemName(itemName);
			item.setItemCode(packages);
			return dao.saveItem(item);
		}
	// tam.ph ending	
		
	public Integer getTotalApp()
			throws IOException, PropertyVetoException {
		Integer total = dao.getTotalApplication();
		
		return total;
	}
	
	public ApplicationDTO convertStringArrayToApplictionDTO(String [] appliction) throws ParseException {
		if (appliction == null || appliction.length < 4) {
			return new ApplicationDTO();
		}
		SimpleDateFormat reportDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		ApplicationDTO dto = new ApplicationDTO();
		dto.setAppCode(appliction[0]);
		dto.setAppId(appliction[1]);
		dto.setReleaseDate(reportDateFormat.parse(appliction[3]));
		dto.setType(2);
		return dto;
	}
	public void updateStatusGettingData(List<ApplicationDTO> dtos) throws ParseException, IOException, PropertyVetoException {
		for (ApplicationDTO applicationDTO : dtos) {
			Application model = new Application();
			model.setAppId(applicationDTO.getAppId());
			model.setUpdateStatus(Constant.UPDATING_DATA);
			dao.updateApplication(model);
		}
		
	}
	
}
