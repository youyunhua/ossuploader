package org.st.aliyun.ossuploader.task;

import java.util.concurrent.Callable;

import org.st.aliyun.ossuploader.model.OssInfo;
import org.st.aliyun.ossuploader.model.UploadObject;

public abstract class UploadTask implements Callable<Object>{
	
	private final UploadObject uploadObject;
	private final OssInfo ossInfo;
	
	public UploadTask(UploadObject uploadObject, OssInfo ossInfo) {
		this.uploadObject = uploadObject;
		this.ossInfo = ossInfo;
	}

	public UploadObject getUploadObject() {
		return uploadObject;
	}

	public OssInfo getOssInfo() {
		return ossInfo;
	}

}
