package org.st.aliyun.ossuploader.task;

import org.apache.log4j.Logger;
import org.st.aliyun.ossuploader.UploadResultManager;
import org.st.aliyun.ossuploader.model.UploadObject;

public class UploadTasks {
	private static Logger logger = Logger.getLogger(UploadResultManager.class.getName());

	public static UploadTask newUploadTask(UploadObject uploadObject, Class<?> uploadTaskClass) 
			throws Exception {
		Object task = null;
		try {
			task = uploadTaskClass.getConstructor(uploadObject.getClass()).newInstance(uploadObject);			
		} catch(NoSuchMethodException e) {
			throw new RuntimeException(uploadTaskClass + " has no constructor with a UploadObject param");
		}
		if (!(task instanceof UploadTask)) {
			throw new RuntimeException(uploadTaskClass + " is not a UploadTask");
		}
		return (UploadTask)task;
	}
}
