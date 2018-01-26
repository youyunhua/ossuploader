package org.st.aliyun.ossuploader.task;

import java.lang.reflect.InvocationTargetException;

import org.apache.log4j.Logger;
import org.st.aliyun.ossuploader.UploadResultManager;
import org.st.aliyun.ossuploader.model.OssInfo;
import org.st.aliyun.ossuploader.model.UploadObject;

public class UploadTasks {
	private static Logger logger = Logger.getLogger(UploadResultManager.class.getName());

	public static UploadTask newUploadTask(UploadObject uploadObject, OssInfo ossInfo, 
			Class<?> uploadTaskClass) {
		Object task = null;
		try {
			task = uploadTaskClass.getConstructor(UploadObject.class, OssInfo.class)
					.newInstance(uploadObject, ossInfo);			
		} catch(NoSuchMethodException e) {
			throw new RuntimeException(uploadTaskClass + " has no valid constructor");
		} catch (Exception e) {
			logger.error(uploadTaskClass + " new Instance failed", e);
			throw new RuntimeException(uploadTaskClass + " new Instance failed");
		}
		if (!(task instanceof UploadTask)) {
			throw new RuntimeException(uploadTaskClass + " is not a UploadTask");
		}
		return (UploadTask)task;
	}
}
