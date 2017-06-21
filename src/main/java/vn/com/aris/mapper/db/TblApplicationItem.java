package vn.com.aris.mapper.db;

public class TblApplicationItem {
    
    
    // Field
    public static final String ID           = "id";
    public static final String APP_ID       = "app_id";
    public static final String ITEM_CODE    = "item_code";
    public static final String ITEM_NAME    = "item_name";
    public static final String REVENUE      = "revenue";
    public static final String BOUGHT_ITEMS = "bought_items";
    public static final String UPDATE_DATE  = "update_date";

    // Table
    public static String getTableName(){
        return  "tbl_application_item";
    }
}
