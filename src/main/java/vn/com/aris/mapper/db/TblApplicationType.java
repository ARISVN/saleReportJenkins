package vn.com.aris.mapper.db;

/**
 * Created by hoangpvm on 11/17/2015.
 */
public class TblApplicationType {
    
    
    // Field
    public static final String TYPE_CODE = "type_code";
    public static final String TYPE_NAME = "type_name";
    
    // Table
    public static String getTableName(){
        return  "tbl_application_type";
    }
}
