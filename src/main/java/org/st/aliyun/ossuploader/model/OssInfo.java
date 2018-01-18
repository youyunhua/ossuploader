package org.st.aliyun.ossuploader.model;

public class OssInfo {
	private String endpoint;
	private Integer port;
	private Integer connectionTimeOut;
	private Integer socketTimeOut;
	private String bucket;
	private String prefix;
	private String key;
	private String secret;
	
	@Override
	public String toString() {
		return "OssInfo [endpoint=" + endpoint + ", port=" + port + ", connectionTimeOut=" + connectionTimeOut
				+ ", socketTimeOut=" + socketTimeOut + ", bucket=" + bucket + ", prefix=" + prefix + ", key=" + key
				+ ", secret=" + secret + "]";
	}

	public String getEndpoint() {
		return endpoint;
	}
	
	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}
	
	public Integer getPort() {
		return port;
	}
	
	public void setPort(Integer port) {
		this.port = port;
	}
	
	public Integer getConnectionTimeOut() {
		return connectionTimeOut;
	}
	
	public void setConnectionTimeOut(Integer connectionTimeOut) {
		this.connectionTimeOut = connectionTimeOut;
	}
	
	public Integer getSocketTimeOut() {
		return socketTimeOut;
	}
	
	public void setSocketTimeOut(Integer socketTimeOut) {
		this.socketTimeOut = socketTimeOut;
	}
	
	public String getBucket() {
		return bucket;
	}
	
	public void setBucket(String bucket) {
		this.bucket = bucket;
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	public String getKey() {
		return key;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
	public String getSecret() {
		return secret;
	}
	
	public void setSecret(String secret) {
		this.secret = secret;
	}
	
}
