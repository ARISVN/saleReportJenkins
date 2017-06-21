package vn.com.aris.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"beginDate", "endDate", "name", "numberOfInstall"})
public class SaleReportCsvDto {
    
    private String beginDate;
    private String endDate;
    private String name;
    private int numberOfInstall;
    
    @JsonProperty("Begin Date")
    public String getBeginDate() {
        return beginDate;
    }
    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }
    @JsonProperty("End Date")
    public String getEndDate() {
        return endDate;
    }
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    @JsonProperty("Name")
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @JsonProperty("Number of Install")
    public int getNumberOfInstall() {
        return numberOfInstall;
    }
    public void setNumberOfInstall(int numberOfInstall) {
        this.numberOfInstall = numberOfInstall;
    }
    
}
