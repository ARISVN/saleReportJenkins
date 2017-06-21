package vn.com.aris.service;

import java.beans.PropertyVetoException;
import java.io.IOException;

import vn.com.aris.dao.ConfigDao;
import vn.com.aris.dto.ConfigSaleReportDTO;
import vn.com.aris.model.Config;
import vn.com.aris.utils.ConvertData;

public class ConfigService {
	public static void saveConfig(ConfigSaleReportDTO configDTO) throws IOException, PropertyVetoException{
		Config config = ConvertData.convertFromConfigSaleReportDTOToConfig(configDTO);
		ConfigDao dao = new ConfigDao();
		Config configQuery = dao.getConfig();
		if(configQuery!=null)
		{
			config.setId(configQuery.getId());
			ConfigDao.updateConfig(config);
		}
		else
		{
			ConfigDao.saveConfig(config);
		}
		
	}
}
