package vn.com.aris.utils;

import java.io.IOException;
import java.io.PrintStream;

import vn.com.aris.JdbcConnector;
import vn.com.aris.demo.DemoConfig;
import vn.com.aris.dto.AppleChartDTO;

public class GSUtil {
//
//	public static boolean getData(String monthYear) {
//		Runtime commandPrompt = Runtime.getRuntime();
//		try {
//			System.out.println("START");
//			Process process = commandPrompt
//					.exec("python C:\\gsutil\\gsutil cp -r gs://pubsite_prod_rev_00133586237694225323/stats/installs/installs_com.arislab.sp_" + monthYear + "_overview.csv D:\\");
//			// Process process =
//			// commandPrompt.exec("sD:\\semina\\Autoingestion\\autoHien.bat");
//			System.out.println("PROCESSING");
//			process.waitFor();
//			System.out.println("SUCCESS");
//
//		} catch (IOException e) {
//			e.printStackTrace();
//			return false;
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//			return false;
//		}
//		return true;
//	}
	
	
    /**
     * 
     * @method getData
     * @description
     * PlayStore get data
     * @author User
     * @date Oct 28, 2015
     * @param
     * @return boolean
     */
	public static boolean getData(String monthYear) {
        Runtime commandPrompt = Runtime.getRuntime();
        String fileName = "installs_com.arisvn.arissmarthiddenbox_" + monthYear + "_overview.csv";
        try {
            System.out.println("START");
            Process process = commandPrompt
                    .exec("python C:\\gsutil\\gsutil cp -r gs://pubsite_prod_rev_00133586237694225323/stats/installs/" + fileName + " D:\\");
            // Process process =
            // commandPrompt.exec("sD:\\semina\\Autoingestion\\autoHien.bat");
            System.out.println("PROCESSING");
            process.waitFor();
            System.out.println("SUCCESS");
            return JdbcConnector.addUpdateInfo(fileName, 0);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }
	
	
	public static boolean appStoreGetData(PrintStream logger, String batFile, String fileName) {
	    Runtime commandPrompt = Runtime.getRuntime();
        try {
            System.out.println("START => " + "cmd /c call " + DemoConfig.PATH_TO_APPLE + batFile);
            logger.println("START WITH COMAMND => " + "cmd /c call " + DemoConfig.PATH_TO_APPLE + batFile);
            Process process = commandPrompt.exec("cmd /c call " + DemoConfig.PATH_TO_APPLE + batFile);
//            logger.println("START WITH COMAMND => " + "cmd /c start D:\\Apple\\GetData201508.bat");
//            Process process = commandPrompt.exec("cmd /c start D:\\Apple\\GetData201508.bat");
            process.waitFor();
            System.out.println("PROCESSING");
            
            System.out.println("SUCCESS");
            return JdbcConnector.addUpdateInfo(fileName, 1);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
	}
	

	public static String getDataReport(String fileName) {

		String dataFromCSV = ReadCSV.getDataFromCSV("D:\\" + fileName);
		return dataFromCSV;
	}

	public static void main(String[] args) {
		
	}
}
