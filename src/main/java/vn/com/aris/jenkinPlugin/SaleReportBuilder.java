package vn.com.aris.jenkinPlugin;

import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import hudson.util.FormValidation;
import net.sf.json.JSONObject;
import vn.com.aris.constant.Constant;
import vn.com.aris.dto.ConfigSaleReportDTO;
import vn.com.aris.model.Application;
import vn.com.aris.service.GetDataService;
import vn.com.aris.service.ProcessdDataSaveIntoDbService;
import vn.com.aris.service.TblApplicationService;
import vn.com.aris.utils.DateUtils;

/**
 * Sample {@link Builder}.
 *
 * <p>
 * When the user configures the project and enables this builder,
 * {@link DescriptorImpl#newInstance(StaplerRequest)} is invoked and a new
 * {@link SaleReportBuilder } is created. The created instance is persisted to
 * the project configuration XML by using XStream, so this allows you to use
 * instance fields (like {@link #name}) to remember the configuration.
 *
 * <p>
 * When a build is performed, the
 * {@link #perform(AbstractBuild, Launcher, BuildListener)} method will be
 * invoked.
 *
 * @author Kohsuke Kawaguchi
 */
public class SaleReportBuilder extends Builder {

	private final String name;

	// Fields in config.jelly must match the parameter names in the
	// "DataBoundConstructor"
	@DataBoundConstructor
	public SaleReportBuilder(String name) {
		this.name = name;
	}

	/**
	 * We'll use this from the <tt>config.jelly</tt>.
	 */
	public String getName() {
		return name;
	}

	public String getNghia() {
		return "Nghia nghia khin khin";
	}

	@Override
	public boolean perform(AbstractBuild build, Launcher launcher, BuildListener listener) {
		PrintStream logger = listener.getLogger();
		GetDataService service = new GetDataService();
		Calendar appleCalendar = Calendar.getInstance();
		appleCalendar.add(Calendar.DATE, - Constant.APPLE_DAY_AUTO_BUILD);
		TblApplicationService appService = new TblApplicationService();
		List<vn.com.aris.model.Application> apps = new ArrayList<>();
		List<Application> ands = new ArrayList<>();
		List<Application> ios = new ArrayList<>();
//		mapResult.get(Constant.MAP_SALE_REPORT_OBJECT_KEY);
		try {
		apps = appService.getLstApplication();
		// parse ios and android 
		if (apps != null) {
			for (Application app : apps) {
				if (Constant.ANDROID_TYPE.equals(String.valueOf(app.getType()))) {
					ands.add(app);
				} else {
					ios.add(app);
				}
			}
		}
		
		// apple get app data
		SimpleDateFormat reportDateFormat = new SimpleDateFormat(DateUtils.APPLE_REPORT_DATE_FORMAT);
		String dateReport  = reportDateFormat.format(appleCalendar.getTime());
		logger.println("START DOWNLOAD FROM APPLE STORE");
		String appleReportFileName = service.createAppleReportFileName(Constant.APPLE_DAYLY_REPORT_FILE_TYPE_CHARACTER, dateReport);

		if (!new File(appleReportFileName).exists()) {
			
			logger.println("START DOWNLOAD FILE : " + appleReportFileName);
			if (service.excCmdAppleGetData(dateReport, Constant.APPLE_DAILY_TYPE)) {
				logger.println("SUCCESS TO GET APPLE DATA :" + appleReportFileName);
				service.moveAllFileInFolder();
			}else {
				logger.println("FAILED TO GET APPLE DATA :" + appleReportFileName);
			}
		}
		if (new File(appleReportFileName).exists()) {
			//import to DB IOS
			logger.println("START IMPORT APPLE DATA :" + appleReportFileName);
			for (Application app : ios) {
				
				ProcessdDataSaveIntoDbService.processdDataFromFileApple(appleReportFileName, app.getAppCode(), Long.parseLong(app.getAppId()));
			}
			logger.println("END IMPORT APPLE DATA :" + appleReportFileName);
		}
		
		Calendar googleCalendar = Calendar.getInstance();
		googleCalendar.add(Calendar.DATE, - Constant.APPLE_DAY_AUTO_BUILD);
		SimpleDateFormat googleDateFormat = new SimpleDateFormat(DateUtils.GOOGLE_REPORT_DATE_FORMAT);
		String dateParams = googleDateFormat.format(googleCalendar.getTime());
		String salesFileName = service.getGoogleSaleFile(dateParams);
		
		logger.println("START DOWNLOAD FROM GOOGLE PLAY SALES REPORT: " + salesFileName);
		if (!new File(salesFileName).exists()) {
			if (service.excCmdgoogleSalesReportData(dateParams)) {
				System.out.println(String.format("Sales Report-Month: %s false to download", salesFileName));
				logger.println("SUCCESS TO DOWNLOAD SALES GOOGLE DATA :" + salesFileName);
				
			}else {
				logger.println("FAILED TO DOWNLOAD SALES GOOGLE DATA :" + salesFileName);
			}
		}
		
		logger.println("START DOWNLOAD FROM GOOGLE PLAY OVERVIEW");
		for (Application application : ands) {
			String fileName = service.getGoogleOverviewFile(application.getAppCode(), dateParams);
			if (!new File(fileName).exists()) {
				if (service.excCmdgoogleOverviewData(application.getAppCode(), dateParams)) {
					logger.println("SUCCESS TO GET GOOGLE DATA OVERVIEW:" + fileName);
				}else {
					logger.println("FAILED TO GET GOOGLE DATA  OVERVIEW:" + fileName);
				}
			}
			if (new File(salesFileName).exists() && new File(fileName).exists()) {
				String pathSaleSame = service.getFilePathSaleFllowPathFileOverview(fileName);
				if(!salesFileName.equals(pathSaleSame))
				{
					ProcessdDataSaveIntoDbService.processdDataFromFileAndroid(fileName, null, Long.parseLong(application.getAppId()), application.getAppCode());
				}
				else
				{
					ProcessdDataSaveIntoDbService.processdDataFromFileAndroid(fileName, salesFileName, Long.parseLong(application.getAppId()), application.getAppCode());
				}
			}
		}
		
		} catch (Exception e) {
			logger.println("Unknow error.");
			return false;
		}
		
		return true;

	}

	public void test(int i) throws Exception {
		System.out.println("TEST");
		System.out.println(i);
		if (i == 3 || i == 7) {
			throw new Exception();
		}
		// return true;
	}

	// Overridden for better type safety.
	// If your plugin doesn't really define any property on Descriptor,
	// you don't have to do this.
	@Override
	public DescriptorImpl getDescriptor() {
		return (DescriptorImpl) super.getDescriptor();
	}

	/**
	 * Descriptor for {@link SaleReportBuilder}. Used as a singleton. The class
	 * is marked as public so that it can be accessed from views.
	 *
	 * <p>
	 * See
	 * <tt>src/main/resources/hudson/plugins/hello_world/HelloWorldBuilder/*.jelly</tt>
	 * for the actual HTML fragment for the configuration screen.
	 */
	@Extension
	// This indicates to Jenkins that this is an implementation of an extension
	// point.
	public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {
		/**
		 * To persist global configuration information, simply store it in a
		 * field and call save().
		 *
		 * <p>
		 * If you don't want fields to be persisted, use <tt>transient</tt>.
		 */
		private boolean useFrench;
		private ConfigSaleReportDTO configDTO;

		/**
		 * In order to load the persisted global configuration, you have to call
		 * load() in the constructor.
		 */
		public DescriptorImpl() {
			load();
		}

		/**
		 * Performs on-the-fly validation of the form field 'name'.
		 *
		 * @param value
		 *            This parameter receives the value that the user has typed.
		 * @return Indicates the outcome of the validation. This is sent to the
		 *         browser.
		 *         <p>
		 *         Note that returning {@link FormValidation#error(String)} does
		 *         not prevent the form from being saved. It just means that a
		 *         message will be displayed to the user.
		 * @throws PropertyVetoException
		 */
		public FormValidation doCheckName(@QueryParameter String value) throws IOException, ServletException, PropertyVetoException {
			if (value.length() == 0)
				return FormValidation.error("Please set a name");
			if (value.length() < 4)
				return FormValidation.warning("Isn't the name too short?");
			return FormValidation.ok();
		}

		public boolean isApplicable(Class<? extends AbstractProject> aClass) {
			// Indicates that this builder can be used with all kinds of project
			// types
			return true;
		}

		/**
		 * This human readable name is used in the configuration screen.
		 */
		public String getDisplayName() {
			return "Get Sales Data";
		}

		@Override
		public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
			// To persist global configuration information,
			// set that to properties and call save().
			useFrench = formData.getBoolean("useFrench");
			// ^Can also use req.bindJSON(this, formData);
			// (easier when there are many fields; need set* methods for this,
			// like setUseFrench)
			save();
			return super.configure(req, formData);
		}

		/**
		 * This method returns true if the global configuration says we should
		 * speak French.
		 *
		 * The method name is bit awkward because global.jelly calls this method
		 * to determine the initial state of the checkbox by the naming
		 * convention.
		 */
		public boolean getUseFrench() {
			return useFrench;
		}

		public ConfigSaleReportDTO getConfigDTO() {
			return configDTO;
		}

//		public ConfigSaleReportDTO getDataConfigSaleReport() throws PropertyVetoException {
//			ConfigSaleReportDTO config = new ConfigSaleReportDTO();
//			Properties prop = new Properties();
//			InputStream input = null;
//			try {
//				input = new FileInputStream(Constant.PATH_FILE_CONFIG + "outConfig.properties");
//				// load a properties file
//				prop.load(input);
//				// get the property value and print it out
//				config.setApplePath(prop.getProperty("apple_path"));
//				config.setAppleVendorId(prop.getProperty("apple_vendor_id"));
//				config.setDriver(prop.getProperty("driver"));
//				config.setGoogleDataPath(prop.getProperty("google_data_path"));
//				config.setGoogleNumber(prop.getProperty("google_number"));
//				config.setGooglePath(prop.getProperty("google_path"));
//				config.setHost(prop.getProperty("host"));
//				config.setJdbc(prop.getProperty("jdbc"));
//				config.setNameDb(prop.getProperty("name_db"));
//				config.setPassName(prop.getProperty("pass_name"));
//				config.setPort(prop.getProperty("port"));
//				config.setUserName(prop.getProperty("user_name"));
//				this.configDTO = config;
//				DataSource.setInfoDb(configDTO);
//				ConfigService.saveConfig(configDTO);
//				writeDataToFileConfigProperties(configDTO);
//				return config;
//			} catch (IOException ex) {
//				ex.printStackTrace();
//			} finally {
//				if (input != null) {
//					try {
//						input.close();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				}
//			}
//			return config;
//		}

		
	}
	
	private void importData(HashMap<String, Object> map) {
		
	}
}
