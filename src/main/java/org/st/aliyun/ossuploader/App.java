package org.st.aliyun.ossuploader;

import java.io.File;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.log4j.Logger;
import org.st.aliyun.ossuploader.model.DbInfo;
import org.st.aliyun.ossuploader.model.OssInfo;
import org.st.aliyun.ossuploader.util.Utils;

/**
 * App class
 * 
 * @author youyunhua
 * @date 2018/01/16
 */
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
		
		if (Objects.equals(dbType, "sqlite")) {
			ExecutorService readExecutor = new ThreadPoolExecutor(1, 1,
                    0L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<Runnable>());
			int nThreads = App.config.getInt("thread.upload.count");
			int nWorkQueueCapacity = App.config.getInt("thread.upload.queuecapacity");
			ExecutorService uploadExecutor = new ThreadPoolExecutor(nThreads, nThreads,
                    0L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<Runnable>(nWorkQueueCapacity));
			Context context = new ContextImpl(readExecutor, uploadExecutor);
			uploader = new Sqilte2OssUploader(context);
		}
		
		return uploader;
	}
}
