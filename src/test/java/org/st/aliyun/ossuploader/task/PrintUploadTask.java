package org.st.aliyun.ossuploader.task;

import org.st.aliyun.ossuploader.model.OssInfo;
import org.st.aliyun.ossuploader.model.UploadObject;

public class PrintUploadTask extends EmptyUploadTask {


	public PrintUploadTask(UploadObject uploadObject, OssInfo ossInfo) {
		super(uploadObject, ossInfo);
	}

	@Override
	public Object call() throws Exception {
		System.out.println("print upload " + this.getUploadObject().getId() + 
				", key=" + this.getUploadObject().getKey());
		return null;
	}

}
