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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bucket == null) ? 0 : bucket.hashCode());
		result = prime * result + ((endpoint == null) ? 0 : endpoint.hashCode());
		result = prime * result + ((port == null) ? 0 : port.hashCode());
		result = prime * result + ((prefix == null) ? 0 : prefix.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OssInfo other = (OssInfo) obj;
		if (bucket == null) {
			if (other.bucket != null)
				return false;
		} else if (!bucket.equals(other.bucket))
			return false;
		if (endpoint == null) {
			if (other.endpoint != null)
				return false;
		} else if (!endpoint.equals(other.endpoint))
			return false;
		if (port == null) {
			if (other.port != null)
				return false;
		} else if (!port.equals(other.port))
			return false;
		if (prefix == null) {
			if (other.prefix != null)
				return false;
		} else if (!prefix.equals(other.prefix))
			return false;
		return true;
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
