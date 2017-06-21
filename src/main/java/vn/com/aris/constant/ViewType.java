package vn.com.aris.constant;

/**
 * Created by User on 11/19/2015.
 */
public enum ViewType {
    DAYS("1", "Days"),
    WEEKS("2", "Weeks"),
    MONTHS("3", "Months"),
    YEARS("4", "Years");

    private String value;
    private String name;

    private ViewType(String value, String name) {
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
