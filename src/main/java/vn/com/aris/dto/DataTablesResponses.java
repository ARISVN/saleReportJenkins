package vn.com.aris.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dang dung
 * @date May 21, 2015
 */
public class DataTablesResponses<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty(value = "draw")
    public int draw;

    @JsonProperty(value = "recordsTotal")
    public long totalRecords;

    @JsonProperty(value = "sortColumn")
    public String sortColumn;
    
    @JsonProperty(value = "orderDirection")
    public String orderDirection;
    
    @JsonProperty(value = "displayStart")
    public String displayStart;
    
    @JsonProperty(value = "recordsFiltered")
    public long recordsFiltered;
    
    @JsonProperty(value = "data")
    public List<T> data = new ArrayList<T>();

    public DataTablesResponses() {
    }
}
