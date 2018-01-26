package org.st.aliyun.ossuploader.task;

import org.st.aliyun.ossuploader.Context;
import org.st.aliyun.ossuploader.model.OssInfo;
import org.st.aliyun.ossuploader.model.UploadObject;

public class PrintUploadTask extends EmptyUploadTask {

	public PrintUploadTask(UploadObject uploadObject, Context context) {
		super(uploadObject, context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object call() throws Exception {
		System.out.println("print upload " + this.getUploadObject().getId() + 
				", key=" + this.getUploadObject().getKey());
		return null;
	}

}
