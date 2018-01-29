package org.st.aliyun.ossuploader;

import java.util.concurrent.ExecutorService;

import org.st.aliyun.ossuploader.model.DbInfo;
import org.st.aliyun.ossuploader.model.OssInfo;
import org.st.aliyun.ossuploader.model.UploadObject;
import org.st.aliyun.ossuploader.model.UploadResult;
import org.st.aliyun.ossuploader.task.UploadTask;

import com.aliyun.oss.OSSClient;

public interface Context {
	public DbInfo getDbInfo();
	public OssInfo getOssInfo();
	public ExecutorService getReadExecutor();
	public ExecutorService getUploadExecutor();
	public UploadResult getUploadResult();
	public Class<?> getUploadTaskClass();
	public UploadTask newUploadTask(UploadObject uploadObject);
	public OSSClient getOssClient();
}
