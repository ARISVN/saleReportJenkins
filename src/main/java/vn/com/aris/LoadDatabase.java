package vn.com.aris;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import vn.com.aris.constant.ApplicationType;

public class LoadDatabase {
	public static Properties getInfomationDatabase() {
		Properties prop = new Properties();
		InputStream input = null;

		try {

			StringBuilder fileName = new StringBuilder("config.properties");
			input = ApplicationType.class.getClassLoader().getResourceAsStream(
					fileName.toString());
			if (input == null) {
				System.out.println("Sorry, unable to find "
						+ fileName.toString());
				return prop;
			}

			// load a properties file from class path, inside static method
			prop.load(input);

			// get the property value and print it out
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return prop;
	}
}
