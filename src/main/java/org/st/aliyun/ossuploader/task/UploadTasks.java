package org.st.aliyun.ossuploader.task;

import org.apache.log4j.Logger;
import org.st.aliyun.ossuploader.UploadResultManager;
import org.st.aliyun.ossuploader.model.OssInfo;
import org.st.aliyun.ossuploader.model.UploadObject;

public class UploadTasks {
	private static Logger logger = Logger.getLogger(UploadResultManager.class.getName());

	public static UploadTask newUploadTask(UploadObject uploadObject, OssInfo ossInfo, 
			Class<?> uploadTaskClass) throws Exception {
		Object task = null;
		try {
			task = uploadTaskClass.getConstructor(UploadObject.class, OssInfo.class)
					.newInstance(uploadObject, ossInfo);			
		} catch(NoSuchMethodException e) {
			throw new RuntimeException(uploadTaskClass + " has no valid constructor");
		}
		if (!(task instanceof UploadTask)) {
			throw new RuntimeException(uploadTaskClass + " is not a UploadTask");
		}
		return (UploadTask)task;
	}
}
