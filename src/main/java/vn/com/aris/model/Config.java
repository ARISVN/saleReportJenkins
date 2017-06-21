package vn.com.aris.model;

public class Config {

    private String googlePath = "";
    private String googleDataPath = "";
    private String googleNumber = "";
    private String applePath = "";
    private String appleVendorId = "";
    private Long id;
    
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
	public void setId(Long id) {
		this.id=id;
	}
	public Long getId() {
		return id;
	}
    
    
    
}
