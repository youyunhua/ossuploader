package org.st.aliyun.ossuploader.task;

import java.util.concurrent.Callable;

import org.st.aliyun.ossuploader.Context;
import org.st.aliyun.ossuploader.model.UploadObject;

public abstract class UploadTask implements Callable<Object>{
	
	private final UploadObject uploadObject;
	private final Context context;
	
	public UploadTask(UploadObject uploadObject, Context context) {
		this.uploadObject = uploadObject;
		this.context = context;
	}

	public UploadObject getUploadObject() {
		return uploadObject;
	}

	public Context getContext() {
		return context;
	}	

}
