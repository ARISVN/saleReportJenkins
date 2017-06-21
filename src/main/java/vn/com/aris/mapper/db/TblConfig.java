package vn.com.aris.mapper.db;

public class TblConfig {
    
        
    // Field
    public static final String GOOGLE_PATH      = "google_path";
    public static final String GOOGLE_DATA_PATH = "google_data_path";
    public static final String GOOGLE_NUMBER    = "google_number";
    public static final String APPLE_PATH       = "apple_path";
    public static final String APPLE_VENDOR_ID  = "apple_vendor_id";
    public static final String ID_CONFIG  = "id";
    
    
    // Table
    public static String getTableName(){
        return  "tbl_config";
    }
}
