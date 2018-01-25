package org.st.aliyun.ossuploader.task;


import java.io.ByteArrayInputStream;

import org.apache.log4j.Logger;
import org.st.aliyun.ossuploader.OSSClientManager;
import org.st.aliyun.ossuploader.model.OssInfo;
import org.st.aliyun.ossuploader.model.UploadObject;
import org.st.aliyun.ossuploader.util.GZipUtil;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;

public class OssUploadTask extends UploadTask {
	private static Logger logger = Logger.getLogger(OssUploadTask.class.getName());

	public OssUploadTask(UploadObject uploadObject, OssInfo ossInfo) {
		super(uploadObject, ossInfo);
	}

	@Override
	public Object call() throws Exception {
		if (this.getUploadObject() == null) {
			throw new Exception("uploadObject is null");
		}
		logger.info("OssUploadTask begin. id=" + this.getUploadObject().getId()
				+ ", key=" + this.getUploadObject().getKey());
		
		OSSClient client = OSSClientManager.getOSSClient(this.getOssInfo());
		String bucketName = this.getOssInfo().getBucket();
		String urlPrefix = this.getOssInfo().getPrefix();
		String realKey = urlPrefix + this.getUploadObject().getKey();

		// put
		byte[] data = this.getUploadObject().getData();
		ByteArrayInputStream bis = new ByteArrayInputStream(data);
		ObjectMetadata meta = new ObjectMetadata();
		// ContentLength must be set
		meta.setContentLength(data.length);

		if (realKey.endsWith(".json")) {
			meta.setContentType("application/json");
		} else if (realKey.endsWith(".jpg")) {
			meta.setContentType("image/jpeg");
		}

		if (GZipUtil.isGZipped(data)) {
			meta.setContentEncoding("gzip");
		}

		PutObjectResult result = client.putObject(bucketName, realKey, bis, meta);
		
		logger.info("OssUploadTask end. id=" + this.getUploadObject().getId()
				+ ", key=" + this.getUploadObject().getKey()
				+ ", etag=" + result.getETag());
		
		return null;
	}


}
