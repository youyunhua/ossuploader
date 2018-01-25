package org.st.aliyun.ossuploader;

import java.util.concurrent.ConcurrentHashMap;

import org.st.aliyun.ossuploader.model.OssInfo;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;

public class OSSClientManager {
	
	private static ConcurrentHashMap<OssInfo, OSSClient> theOSSClientsMap =
			new ConcurrentHashMap<OssInfo, OSSClient>();

	public static OSSClient getOSSClient(OssInfo ossInfo) throws InterruptedException {
		OSSClient ossClient = theOSSClientsMap.get(ossInfo);
		if(ossClient == null) {
			synchronized(ossInfo) {
		        ClientConfiguration conf = new ClientConfiguration();
		        conf.setMaxConnections(ossInfo.getMaxConnections());
		        conf.setConnectionTimeout(ossInfo.getConnectionTimeOut());
		        conf.setMaxErrorRetry(ossInfo.getMaxErrorRetry());
		        conf.setSocketTimeout(ossInfo.getSocketTimeOut());
		        ossClient = new OSSClient(ossInfo.getEndpoint(), ossInfo.getKey(), 
		        		ossInfo.getSecret(), conf);
		        OSSClient client = theOSSClientsMap.putIfAbsent(ossInfo, ossClient);
		        if (client != null) {
		        	ossClient = client;
		        }
			}
		}
		return ossClient;
	}	
}
