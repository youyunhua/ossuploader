package org.st.aliyun.ossuploader.task;

import org.apache.log4j.Logger;
import org.st.aliyun.ossuploader.Context;
import org.st.aliyun.ossuploader.UploadResultManager;
import org.st.aliyun.ossuploader.model.UploadObject;

public class UploadTasks {
	private static Logger logger = Logger.getLogger(UploadResultManager.class.getName());

	public static UploadTask newUploadTask(UploadObject uploadObject, Context context) {
		Object task = null;
		try {
			task = context.getUploadTaskClass().getConstructor(UploadObject.class, Context.class)
					.newInstance(uploadObject, context);			
		} catch(NoSuchMethodException e) {
			throw new RuntimeException(context.getUploadTaskClass().getName() + " has no valid constructor");
		} catch (Exception e) {
			logger.error(context.getUploadTaskClass().getName() + " new Instance failed", e);
			throw new RuntimeException(context.getUploadTaskClass().getName() + " new Instance failed");
		}
		if (!(task instanceof UploadTask)) {
			throw new RuntimeException(context.getUploadTaskClass().getName() + " is not a UploadTask");
		}
		return (UploadTask)task;
	}
}
