package vn.com.aris.demo;

import java.util.ArrayList;
import java.util.List;

public class DemoConfig {

    public static final String PATH_TO_APPLE = "D:\\Apple";
    public List<String> apple = new ArrayList<String>();
    public List<String> appleListFile = new ArrayList<String>();
    public List<String> google = new ArrayList<String>();
    
    public DemoConfig() {
        // Add for apple
        google.add("201508");
        google.add("201509");
        
        // Add for google
        apple.add("appleUtil201508.bat");
        appleListFile.add("S_M_85569551_201508.txt.gz");
        
        apple.add("appleUtil201509.bat");
        appleListFile.add("S_M_85569551_201509.txt.gz");
    }
}
