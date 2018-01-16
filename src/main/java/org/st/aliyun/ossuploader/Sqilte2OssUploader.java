package org.st.aliyun.ossuploader;

import org.st.aliyun.ossuploader.model.DbInfo;
import org.st.aliyun.ossuploader.model.OssInfo;

public class Sqilte2OssUploader implements OssUploader {
	
	public Sqilte2OssUploader(DbInfo dbInfo, OssInfo ossInfo) {
		this.dbInfo = dbInfo;
		this.ossInfo = ossInfo;
	}

	@Override
	public void run() {
		
	}

    ///////////////////////////////////////////////////////////////
    // private properties and methods
    private DbInfo dbInfo;
    private OssInfo ossInfo;
}
