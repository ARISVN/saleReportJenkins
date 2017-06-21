package vn.com.aris.service;

import java.beans.PropertyVetoException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.SystemUtils;

import vn.com.aris.JdbcConnector;
import vn.com.aris.constant.Constant;
import vn.com.aris.dao.ApplicationManagementDao;
import vn.com.aris.dao.ConfigDao;
import vn.com.aris.dao.DataStoreDao;
import vn.com.aris.model.Application;
import vn.com.aris.model.Config;

public class GetDataService {
	private Config config;
	private DataStoreDao dataStoreDao;
	private ApplicationManagementDao appDao;

	public GetDataService(Config config) {
		this.config = config;
		this.appDao = new ApplicationManagementDao();
	}

	public GetDataService() {
		this.config = new ConfigDao().getConfig();
		this.dataStoreDao = new DataStoreDao();
		this.appDao = new ApplicationManagementDao();
	}

	@SuppressWarnings("unchecked")
	public void importDataToDB(Map<String, Object> application)
			throws Exception {

		if (application.containsKey(Constant.ANDROID_TYPE)) {

			List<Application> androids = (List<Application>) application
					.get(Constant.ANDROID_TYPE);
			Map<String, Object> mapResult = getGoogleData(androids);
			List<Application> appCodeList = (List<Application>) mapResult
					.get(Constant.MAP_APPLICATION_OBJECT_KEY);
			List<String> saleReportPathName = (List<String>) mapResult
					.get(Constant.MAP_SALE_REPORT_OBJECT_KEY);

			for (Application app : appCodeList) {
				List<String> fileNames = app.getFileNames();
				for (String path : fileNames) {
					for (String pathSale : saleReportPathName) {
						String pathSaleSame = getFilePathSaleFllowPathFileOverview(path);
						System.out.println("Before" + pathSale);
						if (!pathSale.equals(pathSaleSame)) {
							ProcessdDataSaveIntoDbService
									.processdDataFromFileAndroid(path, null,
											Long.parseLong(app.getAppId()),
											app.getAppCode());
						} else {
							ProcessdDataSaveIntoDbService
									.processdDataFromFileAndroid(path,
											pathSale,
											Long.parseLong(app.getAppId()),
											app.getAppCode());
						}

					}
				}
			}
			System.out.println(saleReportPathName.size());

		}
		if (application.containsKey(Constant.APPLE_TYPE)) {
			List<Application> apples = (List<Application>) application
					.get(Constant.APPLE_TYPE);
			Map<String, Object> mapResult = getAppleData(apples);
			List<Application> appList = (List<Application>) mapResult
					.get(Constant.MAP_APPLICATION_OBJECT_KEY);
			List<String> saleReportfilePath = (List<String>) mapResult
					.get(Constant.MAP_SALE_REPORT_OBJECT_KEY);
			for (Application app : appList) {
				for (String path : saleReportfilePath) {
					ProcessdDataSaveIntoDbService.processdDataFromFileApple(
							path, app.getAppCode(),
							Long.parseLong(app.getAppId()));
				}
				System.out.println(saleReportfilePath.size());
			}

		}

	}

	public String getFilePathSaleFllowPathFileOverview(String path) {
		int lastIndexOf = path.lastIndexOf(File.separator);
		String forder = path.substring(0, lastIndexOf);
		String nameFileOverview = path.substring(lastIndexOf, path.length());
		String[] split = nameFileOverview.split(Constant.UNDERSCORE);
		String pathFileSame = forder + File.separator
				+ Constant.GOOGLE_SALE_REPORT_PREFIX + split[2]
				+ Constant.GOOGLE_SALE_REPORT_EXTENSION;
		System.out.println("After" + pathFileSame);
		return pathFileSame;
	}

	public Map<String, Object> getAppleData(List<Application> appList)
			throws IOException, PropertyVetoException {
		Map<String, Object> mapResult = new HashMap<>();
		mapResult.put(Constant.MAP_APPLICATION_OBJECT_KEY, appList);
		List<String> saleReportfilePath = new ArrayList<>();
		// if (!SystemUtils.IS_OS_WINDOWS) {
		// return mapResult;
		// }
		SimpleDateFormat reportDateFormat = new SimpleDateFormat("yyyyMMdd");
		Date currentDate = new Date();
		long diffDays = (24 * 60 * 60 * 1000);
		List<String> appcode = new ArrayList<>();

		for (Application code : appList) {
			appcode.add(code.getAppCode());
		}

		Date mindate = this.dataStoreDao.getOldestReleaseDateByTypeAndCode(
				Constant.APPLE_TYPE, appcode);
		for (Application app : appList) {

			app.setReleaseDate(mindate);
			long diff = currentDate.getTime() - mindate.getTime();
			long dayDiff = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
			if (dayDiff > 365) {
				for (int i = 0; i <= 366; i++) {
					Date dateReport = new Date(currentDate.getTime()
							- (diffDays * i));
					// check exit file name in temp

					String appleReportFileName = createAppleReportFileName(
							Constant.APPLE_DAYLY_REPORT_FILE_TYPE_CHARACTER,
							reportDateFormat.format(dateReport));

					if (!new File(appleReportFileName).exists()) {
						if (!excCmdAppleGetData(
								reportDateFormat.format(dateReport),
								Constant.APPLE_DAILY_TYPE)) {
							System.out.println(String.format(
									"Date: %s false to download",
									reportDateFormat.format(dateReport)));
						}
					}
					if (new File(appleReportFileName).exists()) {
						saleReportfilePath.add(appleReportFileName);

					}
				}
				// get year report
				Calendar cal = Calendar.getInstance();
				cal.setTime(currentDate);
				int currentYear = cal.get(Calendar.YEAR);
				cal.setTime(mindate);
				int minYear = cal.get(Calendar.YEAR);

				for (int i = currentYear - 1; i >= minYear; i--) {
					String appleReportFileName = createAppleReportFileName(
							Constant.APPLE_YEARLY_REPORT_FILE_TYPE_CHARACTER,
							String.valueOf(i));

					if (!new File(appleReportFileName).exists()) {
						if (!excCmdAppleGetData(String.valueOf(i),
								Constant.APPLE_YEARLY_TYPE)) {
							System.out.println(String.format(
									"Year: %s false to download", i));
						}
					}
					if (new File(appleReportFileName).exists()) {
						saleReportfilePath.add(appleReportFileName);
					}
				}

			} else {
				for (int i = 0; i <= dayDiff + 1; i++) {
					Date dateReport = new Date(currentDate.getTime()
							- (diffDays * i));
					String appleReportFileName = createAppleReportFileName(
							Constant.APPLE_DAYLY_REPORT_FILE_TYPE_CHARACTER,
							reportDateFormat.format(dateReport));

					if (!new File(appleReportFileName).exists()) {
						if (!excCmdAppleGetData(
								reportDateFormat.format(dateReport),
								Constant.APPLE_DAILY_TYPE)) {
							System.out.println(String.format(
									"Date: %s false to download",
									reportDateFormat.format(dateReport)));
						}
					}
					if (new File(appleReportFileName).exists()) {
						saleReportfilePath.add(appleReportFileName);
					}
				}
			}
		}
		mapResult.put(Constant.MAP_SALE_REPORT_OBJECT_KEY, saleReportfilePath);
		// move all file download to temp folder
		moveAllFileInFolder();
		return mapResult;
	}

	public boolean excCmdAppleGetData(String date, String reportType) {
		Runtime commandPrompt = Runtime.getRuntime();
		StringBuilder cmd = new StringBuilder();
		cmd.append(this.config.getApplePath());
		if (SystemUtils.IS_OS_WINDOWS) {
			cmd.append(Constant.WIN_APPLE_BATCH_FILE);
		} else {
			cmd.append(Constant.UNIX_APPLE_BATCH_FILE);
		}
		cmd.append(Constant.SPACE);
		cmd.append(this.config.getAppleVendorId());
		cmd.append(Constant.SPACE);
		cmd.append(reportType);
		cmd.append(Constant.SPACE);
		cmd.append(date);

		try {
			System.out.println(cmd);
			Process process = commandPrompt.exec(cmd.toString());
			System.out.println(cmd);
			System.out.println("the output stream is "
					+ process.getErrorStream());
			String s;
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					process.getErrorStream()));
			while ((s = reader.readLine()) != null) {
				System.out.println("The inout stream is " + s);
			}
			process.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public Map<String, Object> getGoogleData(List<Application> appCodeList) {
		Map<String, Object> mapResult = new HashMap<>();
		List<String> saleReportPathName = new ArrayList<>();
		mapResult.put(Constant.MAP_APPLICATION_OBJECT_KEY, appCodeList);
		// if (!SystemUtils.IS_OS_WINDOWS) {
		// return mapResult;
		// }
		try {
			SimpleDateFormat reportDateFormat = new SimpleDateFormat("yyyyMM");
			Date currentDate = new Date();
			// get overview data in google by application packages
			for (Application app : appCodeList) {
				List<String> filePath = new ArrayList<>();
				Date releaseDate = app.getReleaseDate();
				// this.dataStoreDao.getOldestReleaseDateByTypeAndCode(Constant.ANDROID_TYPE,
				// app.getAppCode());
				app.setReleaseDate(releaseDate);
				int diffMonth = getDiffMonth(currentDate, releaseDate);
				Calendar cal = Calendar.getInstance();
				cal.setTime(releaseDate);
				for (int i = 0; i <= diffMonth; i++) {
					if (i != 0) {
						int mm = cal.get(Calendar.MONTH);
						cal.set(Calendar.MONTH, ++mm);
					}
					String dateParams = reportDateFormat.format(cal.getTime());
					String fileName = this.getGoogleOverviewFile(
							app.getAppCode(), dateParams);
					if (!new File(fileName).exists()) {
						if (!excCmdgoogleOverviewData(app.getAppCode(),
								dateParams)) {
							System.out
									.println(String
											.format("Overview report - Month: %s false to download",
													reportDateFormat
															.format(cal)));
						}
					}

					if (new File(fileName).exists()) {

						filePath.add(fileName);
					}
				}
				app.setFileNames(filePath);
			}
			List<String> appcode = new ArrayList<>();
			for (Application app : appCodeList) {
				appcode.add(app.getAppCode());
			}
			// get sales report data in google
			Date releaseDate = this.dataStoreDao
					.getOldestReleaseDateByTypeAndCode(Constant.ANDROID_TYPE,
							appcode);
			int diffMonth = getDiffMonth(currentDate, releaseDate);
			Calendar cal = Calendar.getInstance();
			cal.setTime(releaseDate);
			for (int i = 0; i <= diffMonth; i++) {

				int mm = cal.get(Calendar.MONTH);
				cal.set(Calendar.MONTH, ++mm);
				String dateParams = reportDateFormat.format(cal.getTime());
				String fileName = this.getGoogleSaleFile(dateParams);
				System.out.println(fileName);
				if (!new File(fileName).exists()) {
					if (!excCmdgoogleSalesReportData(dateParams)) {
						System.out.println(String.format(
								"Sales Report-Month: %s false to download",
								reportDateFormat.format(cal)));
					}
				}

				if (new File(fileName).exists()) {
					saleReportPathName.add(fileName);
				}
			}
			mapResult.put(Constant.MAP_SALE_REPORT_OBJECT_KEY,
					saleReportPathName);
		} catch (IOException | PropertyVetoException e) {
			e.printStackTrace();
		}
		return mapResult;

	}

	public int getDiffMonth(Date currentDate, Date releaseDate) {
		Calendar cal = Calendar.getInstance();
		// default will be Gregorian in US Locales
		cal.setTime(currentDate);
		int minuendMonth = cal.get(Calendar.MONTH);
		int minuendYear = cal.get(Calendar.YEAR);
		cal.setTime(releaseDate);
		int subtrahendMonth = cal.get(Calendar.MONTH);
		int subtrahendYear = cal.get(Calendar.YEAR);

		// the following will work okay for Gregorian but will not
		// work correctly in a Calendar where the number of months
		// in a year is not constant
		return ((minuendYear - subtrahendYear) * cal.getMaximum(Calendar.MONTH))
				+ (minuendMonth - subtrahendMonth);
	}

	public boolean excCmdgoogleOverviewData(String appPackage, String monthYear) {
		Runtime commandPrompt = Runtime.getRuntime();
		String fileName = Constant.GOOGLE_INSTALLS_OVERVIEW_REPORT_PACKAGE
				+ appPackage + Constant.UNDERSCORE + monthYear
				+ Constant.GOOGLE_OVERVIEW_EXTENSION;
		try {
			String cmd = Constant.CMD_PYTHON + this.config.getGooglePath()
					+ " cp -r " + Constant.CMD_GSUTIL_PREFIX
					+ this.config.getGoogleNumber()
					+ Constant.CMD_GSUTIL_OVERVIEW_TYPE_SUFFIX + fileName + " "
					+ this.config.getGoogleDataPath();
			System.out.println(cmd);
			Process process = commandPrompt.exec(cmd);
			System.out.println(cmd);
			System.out.println("the output stream is "
					+ process.getErrorStream());
			String s;
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					process.getErrorStream()));
			while ((s = reader.readLine()) != null) {
				System.out.println("The inout stream is " + s);
			}
			process.waitFor();
			return JdbcConnector.addUpdateInfo(fileName, 0);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean excCmdgoogleSalesReportData(String monthYear) {
		Runtime commandPrompt = Runtime.getRuntime();
		String fileName = Constant.GOOGLE_SALE_REPORT_PREFIX + monthYear
				+ Constant.GOOGLE_SALE_REPORT_EXTENSION + " ";
		try {
			String cmd = Constant.CMD_PYTHON + this.config.getGooglePath()
					+ " cp -r " + Constant.CMD_GSUTIL_PREFIX
					+ this.config.getGoogleNumber()
					+ Constant.CMD_GSUTIL_SALE_TYPE_PATH + fileName
					+ this.config.getGoogleDataPath();
			System.out.println(cmd);
			Process process = commandPrompt.exec(cmd);
			System.out.println(cmd);
			System.out.println("the output stream is "
					+ process.getErrorStream());
			String s;
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					process.getErrorStream()));
			while ((s = reader.readLine()) != null) {
				System.out.println("The inout stream is " + s);
			}
			process.waitFor();
			return JdbcConnector.addUpdateInfo(fileName, 0);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}
	}

	public void moveAllFileInFolder() {
		File sourceLocation = new File(this.config.getApplePath());
		File targetLocation = new File(this.config.getApplePath()
				+ Constant.APPLE_MOVE_ALL_FILE_DIR_NAME);

		try {
			copyDirectory(sourceLocation, targetLocation);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String createAppleReportFileName(String type, String date) {
		StringBuilder rs = new StringBuilder();
		rs.append(this.config.getApplePath());
		rs.append(File.separator);
		rs.append(Constant.APPLE_MOVE_ALL_FILE_DIR_NAME);
		rs.append(File.separator);
		rs.append(Constant.APPLE_SALE_REPORT_TYPE);
		rs.append(type);
		rs.append(this.config.getAppleVendorId());
		rs.append(Constant.UNDERSCORE);
		rs.append(date);
		rs.append(Constant.APPLE_REPORT_FILE_NAME_EXTENSION);
		return rs.toString();
	}

	public String getGoogleSaleFile(String date) {
		return this.config.getGoogleDataPath() + File.separator
				+ Constant.GOOGLE_SALE_REPORT_PREFIX + date
				+ Constant.GOOGLE_SALE_REPORT_EXTENSION;
	}

	public String getGoogleOverviewFile(String appCode, String date) {
		return this.config.getGoogleDataPath() + File.separator
				+ Constant.GOOGLE_INSTALLS_OVERVIEW_REPORT_PACKAGE + appCode
				+ Constant.UNDERSCORE + date
				+ Constant.GOOGLE_OVERVIEW_EXTENSION;
	}

	public void updateStatusGotData(List<Application> apps)
			throws ParseException, IOException, PropertyVetoException {

		for (Application application : apps) {
			Application model = new Application();
			model.setAppId(application.getAppId());
			model.setUpdateStatus(Constant.UPDATED_DATA);
			model.setUpdateDate(new Date());
			appDao.updateApplication(model);
		}

	}

	public void copyDirectory(File sourceLocation, File targetLocation)
			throws IOException {

		if (sourceLocation.isDirectory()) {
			if (!targetLocation.exists()) {
				targetLocation.mkdir();
			}
			GenericExtFilter filter = new GenericExtFilter(
					Constant.APPLE_REPORT_FILE_NAME_EXTENSION);
			String[] children = sourceLocation.list(filter);
			for (int i = 0; i < children.length; i++) {
				File fileSrc = new File(sourceLocation, children[i]);
				File fileTarget = new File(targetLocation, children[i]);
				copyDirectory(fileSrc, fileTarget);
				fileSrc.delete();
			}
		} else {
			InputStream in = new FileInputStream(sourceLocation);
			OutputStream out = new FileOutputStream(targetLocation);

			// Copy the bits from instream to outstream
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
		}
	}

	// inner class, generic extension filter
	private final class GenericExtFilter implements FilenameFilter {

		private String ext;

		public GenericExtFilter(String ext) {
			this.ext = ext;
		}

		public boolean accept(File dir, String name) {
			return (name.endsWith(ext));
		}
	}

	public static void main(String[] args) throws Exception {
		GetDataService sv = new GetDataService();
		List<Application> apple = new ArrayList<>();
		Application a1 = new Application();
		a1.setAppCode("20140819.com.aris-vn.kidsbooks.saintgiong");
		a1.setReleaseDate(new Date(2013, 10, 01));
		apple.add(a1);
		Map<String, Object> application = new HashMap<String, Object>();
		application.put(Constant.APPLE_TYPE, apple);
		sv.importDataToDB(application);
	}

}
