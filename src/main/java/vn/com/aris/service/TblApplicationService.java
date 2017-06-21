package vn.com.aris.service;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import vn.com.aris.dao.ApplicationDao;
import vn.com.aris.dao.ApplicationManagementDao;
import vn.com.aris.dao.DataStoreDao;
import vn.com.aris.model.Application;

public class TblApplicationService {
	private ApplicationDao applicationDao = new ApplicationDao();
	private ApplicationManagementDao appDao = new ApplicationManagementDao();
	private DataStoreDao data = new DataStoreDao();
	public Application findApplicationByIdApp(String idApp) {
		Application app = new Application();
		int id=Integer.parseInt(idApp);
		try {
			app= applicationDao.getAppByID(id);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
		return app;
	}
	public List<Application> getLstApplication() throws IOException, PropertyVetoException {
		List<Application> lstRs=appDao.getAllApplication();
		return lstRs;
	}
	
	public Date getMinReleaseDate(List<Integer> appCodes) throws IOException, PropertyVetoException {
		return data.getMinReleaseDateByCode(appCodes);
	}
	
	
	/**
	 * 
	 * @method selectAppIdFromName
	 * @description
	 * @author dung.dd
	 * @date Nov 29, 2015
	 * @param
	 * @return Integer
	 */
	public Integer selectAppIdFromName(String appName) throws IOException, PropertyVetoException {
	    return applicationDao.selectAppIdFromName(appName);
	}

}
