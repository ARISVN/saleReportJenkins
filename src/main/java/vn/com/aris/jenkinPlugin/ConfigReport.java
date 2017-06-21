package vn.com.aris.jenkinPlugin;

import hudson.Extension;

import java.beans.PropertyVetoException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;
import org.kohsuke.stapler.WebMethod;

import vn.com.aris.DataSource;
import vn.com.aris.constant.Constant;
import vn.com.aris.dto.ConfigSaleReportDTO;
import vn.com.aris.service.ConfigService;

@Extension
public class ConfigReport extends AbstractAction {

	@Override
	public String getUrl() {
		return "config";
	}

	@WebMethod(name = { "saveOrUpdateInfoconfig" })
	public String doSaveOrUpdateInfoconfig(StaplerRequest request, StaplerResponse response) throws IOException, PropertyVetoException {
		String mess="Update info success!!!";
		String userName = request.getParameter("user_name");
		String passName = request.getParameter("password");
		String host = request.getParameter("host");
		String port = request.getParameter("port");
		String nameDb = request.getParameter("name_db");
		String googlePath = request.getParameter("google_path");
		String ggDataPath = request.getParameter("gg_data_path");
		String ggNumber = request.getParameter("gg_number");
		String appPath = request.getParameter("app_path");
		String appVenderId = request.getParameter("app_vender_id");
		ConfigSaleReportDTO dto = new ConfigSaleReportDTO();
		dto.setApplePath(appPath);
		dto.setAppleVendorId(appVenderId);
		dto.setGoogleDataPath(ggDataPath);
		dto.setGoogleNumber(ggNumber);
		dto.setGooglePath(googlePath);
		dto.setHost(host);
		dto.setNameDb(nameDb);
		dto.setPassName(passName);
		dto.setPort(port);
		dto.setUserName(userName);
		DataSource.setInfoDb(dto);
		writeDataToFileConfigProperties(dto);
		ConfigService.saveConfig(dto);
		return mess;
	}

	@WebMethod(name = { "getDataConfigSaleReport" })
	public String doDataConfigSaleReport() throws PropertyVetoException {
		ConfigSaleReportDTO config = new ConfigSaleReportDTO();
		Properties prop = new Properties();
		InputStream input = null;
		StringBuilder rs = new StringBuilder();
		try {
			input = new FileInputStream(Constant.PATH_FILE_CONFIG + "outConfig.properties");
			// load a properties file
			prop.load(input);
			// get the property value and print it out
			config.setApplePath(prop.getProperty("apple_path"));// 0
			config.setAppleVendorId(prop.getProperty("apple_vendor_id"));// 1
			// config.setDriver(prop.getProperty("driver"));
			config.setGoogleDataPath(prop.getProperty("google_data_path"));// 2
			config.setGoogleNumber(prop.getProperty("google_number"));// 3
			config.setGooglePath(prop.getProperty("google_path"));// 4
			config.setHost(prop.getProperty("host"));// 5
			// config.setJdbc(prop.getProperty("jdbc"));
			config.setNameDb(prop.getProperty("name_db"));// 6
			config.setPassName(prop.getProperty("password"));// 7
			config.setPort(prop.getProperty("port"));// 8
			config.setUserName(prop.getProperty("user_name"));// 9
			// rs.append("{");
			// rs.append(" \"data\" ");
			// rs.append(":");
			// rs.append("[");
			// rs.append("{");

			// rs.append(" \"apple_path\" ");
			// rs.append(":");
			rs.append(prop.getProperty("apple_path"));
			rs.append(",");
			// ---------------------------------------

			// rs.append(" \"apple_vendor_id\" ");
			// rs.append(":");
			rs.append(prop.getProperty("apple_vendor_id"));
			rs.append(",");
			// ---------------------------------------
			// rs.append(" \"google_data_path\" ");
			// rs.append(":");
			rs.append(prop.getProperty("google_data_path"));
			rs.append(",");
			// ---------------------------------------
			// rs.append(" \"google_number\" ");
			// rs.append(":");
			rs.append(prop.getProperty("google_number"));
			rs.append(",");
			// ---------------------------------------

			// rs.append(" \"google_path\" ");
			// rs.append(":");
			rs.append(prop.getProperty("google_path"));
			rs.append(",");
			// ---------------------------------------
			// rs.append(" \"host\" ");
			// rs.append(":");
			rs.append(prop.getProperty("host"));
			rs.append(",");
			// ---------------------------------------

			// rs.append(" \"name_db\" ");
			// rs.append(":");
			rs.append(prop.getProperty("name_db"));
			rs.append(",");
			// ---------------------------------------

			// rs.append(" \"password\" ");
			// rs.append(":");
			rs.append(prop.getProperty("password"));
			rs.append(",");
			// ---------------------------------------

			// rs.append(" \"port\" ");
			// rs.append(":");
			rs.append(prop.getProperty("port"));
			rs.append(",");
			// ---------------------------------------

			// rs.append(" \"user_name\" ");
			// rs.append(":");
			rs.append(prop.getProperty("user_name"));
			// rs.append("}");
			// rs.append("]");
			// rs.append("}");

			// this.configDTO = config;
			
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return rs.toString();
	}
	
	private void writeDataToFileConfigProperties(ConfigSaleReportDTO configDTO) {
		OutputStream output = null;
		Properties prop = new Properties();
		try {

			output = new FileOutputStream(Constant.PATH_FILE_CONFIG + "outConfig.properties");

			// set the properties value
			prop.setProperty("apple_path", configDTO.getApplePath());
			prop.setProperty("apple_vendor_id", configDTO.getAppleVendorId());
			prop.setProperty("google_data_path", configDTO.getGoogleDataPath());
			prop.setProperty("google_number", configDTO.getGoogleNumber());
			prop.setProperty("google_path", configDTO.getGooglePath());
			prop.setProperty("host", configDTO.getHost());
			prop.setProperty("name_db", configDTO.getNameDb());
			prop.setProperty("password", configDTO.getPassName());
			prop.setProperty("port", configDTO.getPort());
			prop.setProperty("user_name", configDTO.getUserName());
			// save properties to project root folder
			prop.store(output, null);
		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}

}
