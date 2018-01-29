package org.st.aliyun.ossuploader.task;

import java.util.concurrent.ExecutorService;

import org.st.aliyun.ossuploader.Context;
import org.st.aliyun.ossuploader.model.DbInfo;
import org.st.aliyun.ossuploader.model.OssInfo;
import org.st.aliyun.ossuploader.model.UploadObject;
import org.st.aliyun.ossuploader.model.UploadResult;

public class ContextMock implements Context {

	private DbInfo dbInfo;
	private OssInfo ossInfo;
	private UploadResult uploadResult;
	private ExecutorService readExecutor;
	private ExecutorService uploadExecutor;
	private Class<?> uploadTaskClass;

	public ContextMock(DbInfo dbInfo, OssInfo ossInfo, UploadResult uploadResult, 
			ExecutorService readExecutor, ExecutorService uploadExecutor, Class<?> uploadTaskClass) {
		this.dbInfo = dbInfo;
		this.ossInfo = ossInfo;
		this.uploadResult = uploadResult;
		this.readExecutor = readExecutor;
		this.uploadExecutor = uploadExecutor;
		this.uploadTaskClass = uploadTaskClass;
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

}
