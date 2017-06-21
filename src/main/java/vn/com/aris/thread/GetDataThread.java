package vn.com.aris.thread;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vn.com.aris.constant.Constant;
import vn.com.aris.dao.ApplicationManagementDao;
import vn.com.aris.model.Application;
import vn.com.aris.service.ApplicationManagementService;
import vn.com.aris.service.GetDataService;

public class GetDataThread extends Thread {
	private Thread t;
	private GetDataService service;
	private ApplicationManagementDao dao;

	public GetDataThread() {
		System.out.println("Creating " );
		this.service = new GetDataService();
		this.dao = new ApplicationManagementDao();
	}

	public void run() {
		System.out.println("Running ");
		List<Application> apps = null;
		try {
			apps = dao.getAllGettingDataApp();
		} catch (IOException | PropertyVetoException e1) {
			e1.printStackTrace();
		}
		List<Application> ands = new ArrayList<>();
		List<Application> ios = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		if (apps != null) {
			for (Application app : apps) {
				if (Constant.ANDROID_TYPE.equals(String.valueOf(app.getType()))) {
					ands.add(app);
				} else {
					ios.add(app);
				}
			}
		}

		if(ands.size() > 0) {
			map.put(Constant.ANDROID_TYPE, ands);
		} 
		if(ios.size() > 0) {
			map.put(Constant.APPLE_TYPE, ios);
		}
		try {
			service.importDataToDB(map);
			service.updateStatusGotData(apps);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Thread exiting.");
	}

	public void start() {
		t = new Thread(this);
		t.start();
	}

}
