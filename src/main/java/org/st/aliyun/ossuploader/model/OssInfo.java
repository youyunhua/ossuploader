package org.st.aliyun.ossuploader.model;

public class OssInfo {
	private String endpoint;
	private Integer port;
	private Integer connectionTimeOut;
	private Integer socketTimeOut;
	private Integer maxConnections;
	private Integer maxErrorRetry;
	private String bucket;
	private String prefix;
	private String key;
	private String secret;
	
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
	
	public Integer getMaxConnections() {
		return maxConnections;
	}

	public void setMaxConnections(Integer maxConnections) {
		this.maxConnections = maxConnections;
	}

	public Integer getMaxErrorRetry() {
		return maxErrorRetry;
	}

	public void setMaxErrorRetry(Integer maxErrorRetry) {
		this.maxErrorRetry = maxErrorRetry;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bucket == null) ? 0 : bucket.hashCode());
		result = prime * result + ((connectionTimeOut == null) ? 0 : connectionTimeOut.hashCode());
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
		if (connectionTimeOut == null) {
			if (other.connectionTimeOut != null)
				return false;
		} else if (!connectionTimeOut.equals(other.connectionTimeOut))
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

	@Override
	public String toString() {
		return "OssInfo [endpoint=" + endpoint + ", port=" + port + ", connectionTimeOut=" + connectionTimeOut
				+ ", socketTimeOut=" + socketTimeOut + ", maxConnections=" + maxConnections + ", maxErrorRetry="
				+ maxErrorRetry + ", bucket=" + bucket + ", prefix=" + prefix + ", key=" + key + ", secret=" + secret
				+ "]";
	}
	
}
