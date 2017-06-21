package vn.com.aris.dto;

public class ConfigSaleReportDTO {
	private String userName;
	private String passName;
	private String port;
	private String host;
	private String nameDb;
	private String driver;
	private String jdbc;
	private String googlePath;
	private String googleDataPath;
	private String googleNumber;
	private String applePath;
	private String appleVendorId;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassName() {
		return passName;
	}

	public void setPassName(String passName) {
		this.passName = passName;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getNameDb() {
		return nameDb;
	}

	public void setNameDb(String nameDb) {
		this.nameDb = nameDb;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getJdbc() {
		return jdbc;
	}

	public void setJdbc(String jdbc) {
		this.jdbc = jdbc;
	}

	public String getGooglePath() {
		return googlePath;
	}

	public void setGooglePath(String googlePath) {
		this.googlePath = googlePath;
	}

	public String getGoogleDataPath() {
		return googleDataPath;
	}

	public void setGoogleDataPath(String googleDataPath) {
		this.googleDataPath = googleDataPath;
	}

	public String getGoogleNumber() {
		return googleNumber;
	}

	public void setGoogleNumber(String googleNumber) {
		this.googleNumber = googleNumber;
	}

	public String getApplePath() {
		return applePath;
	}

	public void setApplePath(String applePath) {
		this.applePath = applePath;
	}

	public String getAppleVendorId() {
		return appleVendorId;
	}

	public void setAppleVendorId(String appleVendorId) {
		this.appleVendorId = appleVendorId;
	}

	public ConfigSaleReportDTO(String userName, String passName, String port, String host, String nameDb, String driver, String jdbc, String googlePath,
			String googleDataPath, String googleNumber, String applePath, String appleVendorId) {
		this.userName = userName;
		this.passName = passName;
		this.port = port;
		this.host = host;
		this.nameDb = nameDb;
		this.driver = driver;
		this.jdbc = jdbc;
		this.googlePath = googlePath;
		this.googleDataPath = googleDataPath;
		this.googleNumber = googleNumber;
		this.applePath = applePath;
		this.appleVendorId = appleVendorId;
	}

	public ConfigSaleReportDTO() {
	}

}
