package vn.com.aris.mapper.db;

public class TblItemSaleDaily {
    
    
    // Field
    public static final String ID               = "id";
    public static final String ITEM_ID           = "item_id";
    public static final String DATE             = "date";
    
    public static final String DAY             = "day";
    public static final String WEEK             = "week";
    public static final String MONTH             = "month";
    public static final String YEAR             = "year";
    

    public static final String BOUGHT_NUMBER   = "bought_number";
    public static final String SALE             = "sale";
    public static final String CURRENCY         = "currency";
    public static final String SALE_CURRENCY    = "sale_currency";
    
    // Table
    public static String getTableName(){
        return  "tbl_item_sale_daily";
    }

}
