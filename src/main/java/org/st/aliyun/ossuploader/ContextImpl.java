package org.st.aliyun.ossuploader;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

import org.apache.log4j.Logger;
import org.st.aliyun.ossuploader.model.DbInfo;
import org.st.aliyun.ossuploader.model.OssInfo;
import org.st.aliyun.ossuploader.model.UploadObject;
import org.st.aliyun.ossuploader.model.UploadResult;
import org.st.aliyun.ossuploader.task.OssUploadTask;
import org.st.aliyun.ossuploader.task.UploadTask;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;

public class ContextImpl implements Context {
	
	private static Logger logger = Logger.getLogger(ContextImpl.class.getName());

	private DbInfo dbInfo;
	private OssInfo ossInfo;
	private UploadResult uploadResult;
	private ExecutorService readExecutor;
	private ExecutorService uploadExecutor;
	private OSSClient ossClient;

	public ContextImpl(ExecutorService readExecutor, ExecutorService uploadExecutor) {
		init();
		this.readExecutor = readExecutor;
		this.uploadExecutor = uploadExecutor;
	}
	
	public void init() {
		dbInfo = new DbInfo();
		dbInfo.setName(App.config.getString("db.name"));
		dbInfo.setTable(App.config.getString("db.table"));
		dbInfo.setKeyField(App.config.getString("db.keyField"));
		dbInfo.setValueField(App.config.getString("db.valueField"));
		
		ossInfo = new OssInfo();
		ossInfo.setEndpoint(App.config.getString("oss.endpoint"));
		ossInfo.setPort(App.config.getInt("oss.port"));
		ossInfo.setConnectionTimeOut(App.config.getInt("oss.connectionTimeOut"));
		ossInfo.setSocketTimeOut(App.config.getInt("oss.socketTimeOut"));
		ossInfo.setBucket(App.config.getString("oss.bucket"));
		ossInfo.setPrefix(App.config.getString("oss.prefix"));
		ossInfo.setKey(App.config.getString("oss.key"));
		ossInfo.setSecret(App.config.getString("oss.secret"));
		ossInfo.setMaxConnections(App.config.getInteger("oss.maxConnections", 100));
		ossInfo.setMaxErrorRetry(App.config.getInteger("oss.maxErrorRetry", 10));
		
        ClientConfiguration conf = new ClientConfiguration();
        conf.setMaxConnections(ossInfo.getMaxConnections());
        conf.setConnectionTimeout(ossInfo.getConnectionTimeOut());
        conf.setMaxErrorRetry(ossInfo.getMaxErrorRetry());
        conf.setSocketTimeout(ossInfo.getSocketTimeOut());
        ossClient = new OSSClient(ossInfo.getEndpoint(), ossInfo.getKey(), 
        		ossInfo.getSecret(), conf);

		try {
			uploadResult = UploadResultManager.readUploadResult("./");
		} catch (IOException e) {
			logger.warn("readUploadResult file error, ignore it. msg=" + e.getMessage());
			uploadResult = null;
		}
	}
	
	@Override
	public DbInfo getDbInfo() {
		return this.dbInfo;
	}

	@Override
	public OssInfo getOssInfo() {
		return this.ossInfo;
	}

	@Override
	public ExecutorService getReadExecutor() {
		return this.readExecutor;
	}

	@Override
	public ExecutorService getUploadExecutor() {
		return this.uploadExecutor;
	}

	@Override
	public UploadResult getUploadResult() {
		return this.uploadResult;
	}

	@Override
	public Class<?> getUploadTaskClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UploadTask newUploadTask(UploadObject uploadObject) {
		return new OssUploadTask(uploadObject, this);
	}

	@Override
	public OSSClient getOssClient() {
		return this.ossClient;
	}

}
