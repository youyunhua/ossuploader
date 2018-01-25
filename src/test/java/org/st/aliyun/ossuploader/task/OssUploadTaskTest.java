package org.st.aliyun.ossuploader.task;

import static org.junit.Assert.*;

import java.io.File;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.st.aliyun.ossuploader.model.OssInfo;
import org.st.aliyun.ossuploader.model.UploadObject;

public class OssUploadTaskTest {
	private static OssInfo ossInfo = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
        Configurations configs = new Configurations(); 
        Configuration config = null;
        try { 
        	config = configs.properties(new File("config.properties")); 
        }
        catch (ConfigurationException cex) { 
        }
        
		ossInfo = new OssInfo();
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
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test(expected = Exception.class)
	public void testCall_uploadNullObject() throws Exception {
		OssUploadTask ossUploadTask = new OssUploadTask(null, null);
		ossUploadTask.call();
	}

	@Test
	public void testCall_uploadASimpleObject() throws Exception {
		UploadObject uploadObject = new UploadObject(0, "test", new byte[] {'a', 'b', 'd'});
		OssUploadTask ossUploadTask = new OssUploadTask(uploadObject, ossInfo);
		ossUploadTask.call();
	}

}
