package org.st.aliyun.ossuploader;

import java.io.File;
import java.util.Objects;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.log4j.Logger;
import org.st.aliyun.ossuploader.model.DbInfo;
import org.st.aliyun.ossuploader.model.OssInfo;
import org.st.aliyun.ossuploader.util.Utils;

public class App 
{
	public static Configuration config = null;
	private static Logger logger = Logger.getLogger(App.class.getName());
	
    public static void main( String[] args ) {
        logger.info("ossuploader begin.");
        
        Configurations configs = new Configurations(); 
        try { 
        	config = configs.properties(new File("config.properties")); 
        }
        catch (ConfigurationException cex) { 
        	logger.error("read config.properties failed!", cex);
        	return;
        }
        
    	String dirName = config.getString("dir.name");
    	Utils.checkDirAndCreateIfNotExist(dirName);

        String dbType = config.getString("db.type");
        logger.info("db.type=" + dbType);
        if (dbType == null) {
        	logger.error("db.type is null!");
        	return;
        }
        
        AbstractOssUploader uploader = App.getUploader(dbType);
        uploader.run();
        
        logger.info("ossuploader end.");
    }

	private static AbstractOssUploader getUploader(String dbType) {
		AbstractOssUploader uploader = null;
		
		DbInfo dbInfo = new DbInfo();
		dbInfo.setName(config.getString("db.name"));
		dbInfo.setTable(config.getString("db.table"));
		dbInfo.setKeyField(config.getString("db.keyField"));
		dbInfo.setValueField(config.getString("db.valueField"));
		
		OssInfo ossInfo = new OssInfo();
		ossInfo.setEndpoint(config.getString("oss.endpoint"));
		ossInfo.setPort(config.getInt("oss.port"));
		ossInfo.setConnectionTimeOut(config.getInt("oss.connectionTimeOut"));
		ossInfo.setSocketTimeOut(config.getInt("oss.socketTimeOut"));
		ossInfo.setBucket(config.getString("oss.bucket"));
		ossInfo.setPrefix(config.getString("oss.prefix"));
		ossInfo.setKey(config.getString("oss.key"));
		ossInfo.setSecret(config.getString("oss.secret"));
		ossInfo.setMaxConnections(config.getInteger("oss.maxConnections", 100));
		ossInfo.setMaxErrorRetry(config.getInteger("oss.maxErrorRetry", 10));
		
		if (Objects.equals(dbType, "sqlite")) {
			uploader = new Sqilte2OssUploader(dbInfo, ossInfo);
		}
		
		return uploader;
	}
}
