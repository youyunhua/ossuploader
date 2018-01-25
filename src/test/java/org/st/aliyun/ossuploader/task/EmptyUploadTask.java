package org.st.aliyun.ossuploader.task;

import org.st.aliyun.ossuploader.model.OssInfo;
import org.st.aliyun.ossuploader.model.UploadObject;
import org.st.aliyun.ossuploader.task.UploadTask;

public class EmptyUploadTask extends UploadTask {


	public EmptyUploadTask(UploadObject uploadObject, OssInfo ossInfo) {
		super(uploadObject, ossInfo);
	}

	@Override
	public Object call() throws Exception {
		return null;
	}

}
