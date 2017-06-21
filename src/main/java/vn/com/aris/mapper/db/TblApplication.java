package vn.com.aris.mapper.db;

public class TblApplication {
    
    
    // Field
    public static final String ID               = "id";
    public static final String APP_CODE         = "app_code";
    public static final String APP_NAME         = "app_name";
    public static final String TYPE             = "type";
    public static final String RELEASE_DATE     = "release_date";
    public static final String UPDATE_STATUS    = "update_status";
    public static final String UPDATE_DATE      = "update_date";
    public static final String REVENUE_ITEM     = "revenue_item";
    public static final String REVENUE          = "revenue";
    
    // Table
    public static String getTableName(){
        return  "tbl_application";
    }
}
