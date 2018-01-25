package org.st.aliyun.ossuploader.model;

public class UploadObject {
    private int id;	
    private String key;
    private byte[] data;
    
    public UploadObject(int id, String key, byte[] data) {
        this.setId(id);
        this.setKey(key);
        this.setData(data);
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

}
