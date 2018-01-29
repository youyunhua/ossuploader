package org.st.aliyun.ossuploader.task;


import java.io.ByteArrayInputStream;
import java.util.concurrent.ExecutorService;

import org.apache.log4j.Logger;
import org.st.aliyun.ossuploader.AbstractOssUploader;
import org.st.aliyun.ossuploader.AbstractOssUploader.UploadObjectStatus;
import org.st.aliyun.ossuploader.Context;
import org.st.aliyun.ossuploader.model.OssInfo;
import org.st.aliyun.ossuploader.model.UploadObject;
import org.st.aliyun.ossuploader.util.GZipUtil;

import com.aliyun.oss.ClientErrorCode;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSErrorCode;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;

public class OssUploadTask extends UploadTask {

	private static Logger logger = Logger.getLogger(OssUploadTask.class.getName());

	public OssUploadTask(UploadObject uploadObject, Context context) {
		super(uploadObject, context);
	}
	
	@Override
	public Object call() throws Exception {
		if (this.getUploadObject() == null) {
			throw new RuntimeException("uploadObject is null");
		}
		logger.info("OssUploadTask begin. id=" + this.getUploadObject().getId()
				+ ", key=" + this.getUploadObject().getKey());
		
		String bucketName = this.getContext().getOssInfo().getBucket();
		String urlPrefix = this.getContext().getOssInfo().getPrefix();
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

		try {
			PutObjectResult result = this.getContext().getOssClient().putObject(
					bucketName, realKey, bis, meta);
			
			logger.info("OssUploadTask end. id=" + this.getUploadObject().getId()
					+ ", key=" + this.getUploadObject().getKey()
					+ ", etag=" + result.getETag());			
		} catch (OSSException e) {
			logger.error("OssUploadTask error: OSSException caught. id=" + this.getUploadObject().getId()
					+ ", msg=" + e.getErrorMessage() + ", code=" + e.getErrorCode());
			switch (e.getErrorCode()) {
				case OSSErrorCode.NO_SUCH_BUCKET:
					ExecutorService exe = this.getContext().getUploadExecutor();
					if (exe != null) {
						exe.shutdownNow();
						logger.error("Shut down UploadExecutor.");
						// make work threads fast-fail 
						this.getContext().getOssClient().shutdown();
					}
					AbstractOssUploader.uploadObjectStatusMap.put(this.getUploadObject().getId(), 
							UploadObjectStatus.UploadError);
					return null;
				case OSSErrorCode.ACCESS_DENIED:
				default:
					throw e;
			}
		} catch (ClientException e) {
			logger.error("OssUploadTask error: ClientException caught. id=" + this.getUploadObject().getId()
					+ ", msg=" + e.getErrorMessage() + ", code=" + e.getErrorCode());
			switch (e.getErrorCode()) {
				case ClientErrorCode.UNKNOWN_HOST:	// when host not exit or not connected
					AbstractOssUploader.uploadObjectStatusMap.put(this.getUploadObject().getId(), 
							UploadObjectStatus.UploadFailed);
					return null;
				case ClientErrorCode.UNKNOWN:	// show this exception when OSSClient has shut down
				default:
					throw e;
			}

		}

		return null;
	}
}
