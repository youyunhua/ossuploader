package org.st.aliyun.ossuploader;

import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractOssUploader {
	
	public static ConcurrentHashMap<Integer, UploadObjectStatus> uploadObjectStatusMap = 
			new ConcurrentHashMap<Integer, UploadObjectStatus>();
	
	public enum UploadObjectStatus {
		ReadNotUpload, ReadFailed, Uploaded, UploadFailed, 
		UploadError,	// means context is invalid, should not try upload again
	}

	abstract void run();
		
}
