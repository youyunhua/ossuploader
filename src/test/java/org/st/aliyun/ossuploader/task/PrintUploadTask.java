package org.st.aliyun.ossuploader.task;

import org.st.aliyun.ossuploader.model.UploadObject;

public class PrintUploadTask extends EmptyUploadTask {

	public PrintUploadTask(UploadObject uploadObject) {
		super(uploadObject);
	}

	@Override
	public Object call() throws Exception {
		System.out.println("print upload " + this.getUploadObject().getId() + 
				", key=" + this.getUploadObject().getKey());
		return null;
	}

}
