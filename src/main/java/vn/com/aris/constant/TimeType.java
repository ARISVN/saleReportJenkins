package vn.com.aris.constant;

/**
 * Created by User on 11/19/2015.
 */
public enum TimeType {
    YEAR("1", "Year"),
    MONTH("2", "Month"),
    WEEK("3", "Week"),
    RANGE("4", "Range"),
    ALL_TIME("5", "All Time");

    private String value;
    private String name;

    private TimeType(String value, String name) {
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
