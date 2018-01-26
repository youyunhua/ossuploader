package org.st.aliyun.ossuploader.task;

import org.st.aliyun.ossuploader.Context;
import org.st.aliyun.ossuploader.model.OssInfo;
import org.st.aliyun.ossuploader.model.UploadObject;
import org.st.aliyun.ossuploader.task.UploadTask;

public class EmptyUploadTask extends UploadTask {

	public EmptyUploadTask(UploadObject uploadObject, Context context) {
		super(uploadObject, context);
	}

	@Override
	public Object call() throws Exception {
		return null;
	}

}
