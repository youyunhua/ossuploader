package org.st.aliyun.ossuploader.task;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import org.apache.log4j.Logger;
import org.st.aliyun.ossuploader.AbstractOssUploader;
import org.st.aliyun.ossuploader.UploadResultManager;
import org.st.aliyun.ossuploader.AbstractOssUploader.UploadObjectStatus;
import org.st.aliyun.ossuploader.constant.UploadConstants;
import org.st.aliyun.ossuploader.model.DbInfo;
import org.st.aliyun.ossuploader.model.OssInfo;
import org.st.aliyun.ossuploader.model.UploadObject;
import org.st.aliyun.ossuploader.model.UploadResult;

public class SqliteReadTask implements Callable<Integer> {

	private static Logger logger = Logger.getLogger(SqliteReadTask.class.getName());

//	public SqliteReadTask(DbInfo dbInfo, UploadResult uploadResult, 
//			ExecutorService uploadExecutor, Class<?> uploadTaskClass) {
//		this(dbInfo, uploadResult, uploadExecutor, uploadTaskClass, null);
//	}

	public SqliteReadTask(DbInfo dbInfo, UploadResult uploadResult, 
			ExecutorService uploadExecutor, Class<?> uploadTaskClass, OssInfo ossInfo) {
		this.dbInfo = dbInfo;
		this.uploadResult = uploadResult;
		this.uploadExecutor = uploadExecutor;
		this.uploadTaskClass = uploadTaskClass;
		this.ossInfo = ossInfo;
	}

	@Override
	public Integer call() throws Exception {
		logger.info("ReadSqliteTask begin.");
		Connection connection = null;
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:" + this.dbInfo.getName());
			Statement statement = connection.createStatement();
			String TABLE_NAME = this.dbInfo.getTable();
			String KEY_FIELD = this.dbInfo.getKeyField();
			String DATA_FIELD = this.dbInfo.getValueField();
			ResultSet rs = statement.executeQuery("select " + KEY_FIELD + 
					 ", " + DATA_FIELD + " from " + TABLE_NAME);
			int id = 0;
			int beginReadId = UploadResultManager.getBeginReadId(uploadResult);
			int endReadId = UploadResultManager.getEndReadId(uploadResult);

			for (; id < beginReadId; ++id) {
				if (!rs.next()) {
					logger.warn("firstReadId too large.");
					throw new Exception("firstReadId too large.");
				}
			}
			UploadObject uploadObject = null;
			Long totalReadSize = 0L;
			int totalReadCount = 0;
			while (rs.next() && (id < endReadId || endReadId < 0)) {
				try {
					String tileKey = rs.getString(KEY_FIELD);
					byte[] tileData = rs.getBytes(DATA_FIELD);
					totalReadSize += tileData.length;
					uploadObject = new UploadObject(id, tileKey, tileData);
					AbstractOssUploader.uploadObjectStatusMap.put(id, UploadObjectStatus.ReadNotUpload);
					
					UploadTask uploadTask = UploadTasks.newUploadTask(uploadObject, this.ossInfo,
							this.uploadTaskClass);
					this.uploadExecutor.submit(uploadTask);
				} catch (SQLException e) {
					logger.error(id + ": ReadSqliteTask get data error.", e);
					AbstractOssUploader.uploadObjectStatusMap.put(id, UploadObjectStatus.ReadFailed);
				} finally {
					++id;
					++totalReadCount;
				}
			} ;

			UploadTask poisonPillTask = UploadTasks.newUploadTask(UploadConstants.UPLOAD_OBJECT_POISON_PILL, 
					this.ossInfo, this.uploadTaskClass);
			this.uploadExecutor.submit(poisonPillTask);

			this.uploadResult.setCurrentReadId(id);
			this.uploadResult.setTotalReadSize(totalReadSize);
			logger.info("ReadSqliteTask over. currentReadId=" + id + ", totalReadSize=" + totalReadSize);
			return totalReadCount;
		} catch (SQLException e) {
			logger.error("ReadSqliteTask error. errorCode=" + e.getErrorCode() + 
					", msg=" + e.getMessage());
			throw e;
		}
		finally {
			if (connection != null) {
				connection.close();				
			}
		}
	}

	private DbInfo dbInfo;
	private UploadResult uploadResult;
	private ExecutorService uploadExecutor;
	private Class<?> uploadTaskClass;
	private OssInfo ossInfo;
}
