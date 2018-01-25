package org.st.aliyun.ossuploader.model;

import java.util.TreeSet;

public class UploadResult {

	private DbInfo dbInfo;
	private OssInfo ossInfo;
	
	// uploadIds = [0..currentReadId] - readButNotUploadIds - readFailedIds - uploadFailedIds
	// intersect(readButNotUploadIds, readFailedIds) = {}
	// intersect(readButNotUploadIds, uploadFailedIds) = {}
	// intersect(readFailedIds, uploadFailedIds) = {}
	// interset([0..currentReadId],readButNotUploadIds) = readButNotUploadIds
	// interset([0..currentReadId],readFailedIds) = readFailedIds
	// interset([0..currentReadId],uploadFailedIds) = uploadFailedIds
	private Integer currentReadId;
	private TreeSet<Integer> readButNotUploadIds;
	private TreeSet<Integer> readFailedIds;
	private TreeSet<Integer> uploadFailedIds;
	
	private Boolean readOvered;
	private Long totalReadSize;
	private Long totalUploadSize;
		
	private String lastUpdateTime;

	public UploadResult() {
		readOvered = false;
	}
	
	public DbInfo getDbInfo() {
		return dbInfo;
	}
	
	public void setDbInfo(DbInfo dbInfo) {
		this.dbInfo = dbInfo;
	}
	
	public OssInfo getOssInfo() {
		return ossInfo;
	}
	
	public void setOssInfo(OssInfo ossInfo) {
		this.ossInfo = ossInfo;
	}

	public String getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public Integer getCurrentReadId() {
		return currentReadId;
	}

	public void setCurrentReadId(Integer currentReadId) {
		this.currentReadId = currentReadId;
	}

	public TreeSet<Integer> getReadButNotUploadIds() {
		return readButNotUploadIds;
	}

	public void setReadButNotUploadIds(TreeSet<Integer> readButNotUploadIds) {
		this.readButNotUploadIds = readButNotUploadIds;
	}

	public TreeSet<Integer> getReadFailedIds() {
		return readFailedIds;
	}

	public void setReadFailedIds(TreeSet<Integer> readFailedIds) {
		this.readFailedIds = readFailedIds;
	}

	public TreeSet<Integer> getUploadFailedIds() {
		return uploadFailedIds;
	}

	public void setUploadFailedIds(TreeSet<Integer> uploadFailedIds) {
		this.uploadFailedIds = uploadFailedIds;
	}

	public Long getTotalReadSize() {
		return totalReadSize;
	}

	public void setTotalReadSize(Long totalReadSize) {
		this.totalReadSize = totalReadSize;
	}

	public Long getTotalUploadSize() {
		return totalUploadSize;
	}

	public void setTotalUploadSize(Long totalUploadSize) {
		this.totalUploadSize = totalUploadSize;
	}

	public Boolean getReadOvered() {
		return readOvered;
	}

	public void setReadOvered(Boolean readOvered) {
		this.readOvered = readOvered;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((currentReadId == null) ? 0 : currentReadId.hashCode());
		result = prime * result + ((dbInfo == null) ? 0 : dbInfo.hashCode());
		result = prime * result + ((lastUpdateTime == null) ? 0 : lastUpdateTime.hashCode());
		result = prime * result + ((ossInfo == null) ? 0 : ossInfo.hashCode());
		result = prime * result + ((readButNotUploadIds == null) ? 0 : readButNotUploadIds.hashCode());
		result = prime * result + ((readFailedIds == null) ? 0 : readFailedIds.hashCode());
		result = prime * result + ((readOvered == null) ? 0 : readOvered.hashCode());
		result = prime * result + ((totalReadSize == null) ? 0 : totalReadSize.hashCode());
		result = prime * result + ((totalUploadSize == null) ? 0 : totalUploadSize.hashCode());
		result = prime * result + ((uploadFailedIds == null) ? 0 : uploadFailedIds.hashCode());
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
		UploadResult other = (UploadResult) obj;
		if (currentReadId == null) {
			if (other.currentReadId != null)
				return false;
		} else if (!currentReadId.equals(other.currentReadId))
			return false;
		if (dbInfo == null) {
			if (other.dbInfo != null)
				return false;
		} else if (!dbInfo.equals(other.dbInfo))
			return false;
		if (lastUpdateTime == null) {
			if (other.lastUpdateTime != null)
				return false;
		} else if (!lastUpdateTime.equals(other.lastUpdateTime))
			return false;
		if (ossInfo == null) {
			if (other.ossInfo != null)
				return false;
		} else if (!ossInfo.equals(other.ossInfo))
			return false;
		if (readButNotUploadIds == null) {
			if (other.readButNotUploadIds != null)
				return false;
		} else if (!readButNotUploadIds.equals(other.readButNotUploadIds))
			return false;
		if (readFailedIds == null) {
			if (other.readFailedIds != null)
				return false;
		} else if (!readFailedIds.equals(other.readFailedIds))
			return false;
		if (readOvered == null) {
			if (other.readOvered != null)
				return false;
		} else if (!readOvered.equals(other.readOvered))
			return false;
		if (totalReadSize == null) {
			if (other.totalReadSize != null)
				return false;
		} else if (!totalReadSize.equals(other.totalReadSize))
			return false;
		if (totalUploadSize == null) {
			if (other.totalUploadSize != null)
				return false;
		} else if (!totalUploadSize.equals(other.totalUploadSize))
			return false;
		if (uploadFailedIds == null) {
			if (other.uploadFailedIds != null)
				return false;
		} else if (!uploadFailedIds.equals(other.uploadFailedIds))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UploadResult [dbInfo=" + dbInfo + ", ossInfo=" + ossInfo + ", currentReadId=" + currentReadId
				+ ", readButNotUploadIds=" + readButNotUploadIds + ", readFailedIds=" + readFailedIds
				+ ", uploadFailedIds=" + uploadFailedIds + ", readOvered=" + readOvered + ", totalReadSize="
				+ totalReadSize + ", totalUploadSize=" + totalUploadSize + ", lastUpdateTime=" + lastUpdateTime + "]";
	}


}
