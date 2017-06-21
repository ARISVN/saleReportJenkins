package vn.com.aris.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;

/**
 * @author dang dung
 * @date May 21, 2015
 */
public class DataTablesResponse<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty(value = "draw")
    public int draw;
    @JsonProperty(value = "recordsTotal")
    public long totalRecords;
    @JsonProperty(value = "orderColumn")
    public String orderColumn;
    @JsonProperty(value = "orderDirection")
    public String orderDirection;
    @JsonProperty(value = "displayStart")
    public long displayStart;
    @JsonProperty(value = "recordsFiltered")
    public long recordsFiltered;
    @JsonProperty(value = "pageLength")
    public long pageLength;
    @JsonProperty(value = "searchQuery")
    public long searchQuery;
    
    @JsonProperty(value = "appId")
    public long appId;
    
    //dung.dd: Start
    
    @JsonProperty(value = "appIdStr", defaultValue="")
    public String appIdStr = "";
    
    @JsonProperty(value = "itemIdStr")
    public String itemIdStr = "";
    
    @JsonProperty(value = "statisticType")
    public String statisticType;
    
    @JsonProperty(value = "timeType")
    public String timeType;
    
    @JsonProperty(value = "dateFrom")
    public String dateFrom;
    
    @JsonProperty(value = "dateTo")
    public String dateTo;
    // dung.dd: End
    
    @JsonProperty(value = "data")
    public List<T> data = new ArrayList<T>();

    public DataTablesResponse() {
    }
}
