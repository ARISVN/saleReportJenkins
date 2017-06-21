package vn.com.aris.dao.adapter;

import vn.com.aris.mapper.db.TblConfig;
import vn.com.aris.model.Config;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ConfigAdapter {
    
    public Config convertConfigObject(ResultSet rset) {
        Config config = new Config();
        try {
            
            config.setGooglePath(rset.getString(TblConfig.GOOGLE_PATH));
            config.setGoogleDataPath(rset.getString(TblConfig.GOOGLE_DATA_PATH));
            config.setGoogleNumber(rset.getString(TblConfig.GOOGLE_NUMBER));
            config.setApplePath(rset.getString(TblConfig.APPLE_PATH));
            config.setAppleVendorId(rset.getString(TblConfig.APPLE_VENDOR_ID));
            config.setId(rset.getLong(TblConfig.ID_CONFIG));
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return config;
    }
}
