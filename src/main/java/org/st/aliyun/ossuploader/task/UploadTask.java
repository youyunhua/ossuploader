package org.st.aliyun.ossuploader.task;

import java.util.concurrent.Callable;

import org.st.aliyun.ossuploader.model.UploadObject;

public abstract class UploadTask implements Callable<Object>{
	
	private final UploadObject uploadObject;
	
	public UploadTask(UploadObject uploadObject) {
		this.uploadObject = uploadObject;
	}

	public UploadObject getUploadObject() {
		return uploadObject;
	}

}
