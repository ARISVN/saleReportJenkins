package vn.com.aris.constant;

/**
 * Created by User on 11/19/2015.
 */
public enum StatisticType {

    SALE_STATISTIC("1", "Sale Report"),
    INSTALL_STATISTIC("2", "Install Report");

    private String value;
    private String name;

    private StatisticType(String value, String name) {
        this.value = value;
        this.name = name;
    }
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
