package org.st.aliyun.ossuploader.task;

import java.util.concurrent.ExecutorService;

import org.st.aliyun.ossuploader.Context;
import org.st.aliyun.ossuploader.model.DbInfo;
import org.st.aliyun.ossuploader.model.OssInfo;
import org.st.aliyun.ossuploader.model.UploadObject;
import org.st.aliyun.ossuploader.model.UploadResult;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;

public class ContextMock implements Context {

	private DbInfo dbInfo;
	private OssInfo ossInfo;
	private UploadResult uploadResult;
	private ExecutorService readExecutor;
	private ExecutorService uploadExecutor;
	private Class<?> uploadTaskClass;
	private OSSClient ossClient;

	public ContextMock(DbInfo dbInfo, OssInfo ossInfo, UploadResult uploadResult, 
			ExecutorService readExecutor, ExecutorService uploadExecutor, Class<?> uploadTaskClass) {
		this.dbInfo = dbInfo;
		this.ossInfo = ossInfo;
		this.uploadResult = uploadResult;
		this.readExecutor = readExecutor;
		this.uploadExecutor = uploadExecutor;
		this.uploadTaskClass = uploadTaskClass;
		
		if (this.ossInfo != null) {
	        ClientConfiguration conf = new ClientConfiguration();
	        conf.setMaxConnections(ossInfo.getMaxConnections());
	        conf.setConnectionTimeout(ossInfo.getConnectionTimeOut());
	        conf.setMaxErrorRetry(ossInfo.getMaxErrorRetry());
	        conf.setSocketTimeout(ossInfo.getSocketTimeOut());
	        this.ossClient = new OSSClient(ossInfo.getEndpoint(), ossInfo.getKey(), 
	        		ossInfo.getSecret(), conf);			
		} else {
			this.ossClient = null;
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
		return uploadTaskClass;
	}

	@Override
	public UploadTask newUploadTask(UploadObject uploadObject) {
		return UploadTasks.newUploadTask(uploadObject, this);
	}

	@Override
	public OSSClient getOssClient() {
		return this.ossClient;
	}

}
