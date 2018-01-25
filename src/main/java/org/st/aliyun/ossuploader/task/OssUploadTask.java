package org.st.aliyun.ossuploader.task;


import org.st.aliyun.ossuploader.model.UploadObject;

public class OssUploadTask extends UploadTask {
	
	public OssUploadTask(UploadObject uploadObject) {
		super(uploadObject);
	}

	@Override
	public Object call() throws Exception {
		System.out.println("oss upload " + this.getUploadObject().getId());
		return null;
	}


}
