package org.st.aliyun.ossuploader.model;

public class DbInfo {
    private String name;
	private String host;
	private Integer port;
    private String table;
    private String keyField;
    private String valueField;
    
    private Integer beginKeyListIndex;
    private Integer endKeyListIndex;
    private Boolean skipBadRecords;
    
	@Override
	public String toString() {
		return "DbInfo [name=" + name + ", host=" + host + ", port=" + port + ", table=" + table + ", keyField="
				+ keyField + ", valueField=" + valueField + ", beginKeyListIndex=" + beginKeyListIndex
				+ ", endKeyListIndex=" + endKeyListIndex + ", skipBadRecords=" + skipBadRecords + "]";
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getHost() {
		return host;
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	
	public Integer getPort() {
		return port;
	}
	
	public void setPort(Integer port) {
		this.port = port;
	}

	public String getTable() {
		return table;
	}
	
	public void setTable(String table) {
		this.table = table;
	}
	
	public String getKeyField() {
		return keyField;
	}
	
	public void setKeyField(String keyField) {
		this.keyField = keyField;
	}
	
	public String getValueField() {
		return valueField;
	}
	
	public void setValueField(String valueField) {
		this.valueField = valueField;
	}
	
	public Integer getBeginKeyListIndex() {
		return beginKeyListIndex;
	}
	
	public void setBeginKeyListIndex(Integer beginKeyListIndex) {
		this.beginKeyListIndex = beginKeyListIndex;
	}
	
	public Integer getEndKeyListIndex() {
		return endKeyListIndex;
	}
	
	public void setEndKeyListIndex(Integer endKeyListIndex) {
		this.endKeyListIndex = endKeyListIndex;
	}
	
	public Boolean getSkipBadRecords() {
		return skipBadRecords;
	}
	
	public void setSkipBadRecords(Boolean skipBadRecords) {
		this.skipBadRecords = skipBadRecords;
	}


}
