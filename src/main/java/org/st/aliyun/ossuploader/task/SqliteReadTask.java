package org.st.aliyun.ossuploader.task;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.Callable;
import java.util.concurrent.RejectedExecutionException;

import org.apache.log4j.Logger;
import org.st.aliyun.ossuploader.AbstractOssUploader;
import org.st.aliyun.ossuploader.UploadResultManager;
import org.st.aliyun.ossuploader.AbstractOssUploader.UploadObjectStatus;
import org.st.aliyun.ossuploader.Context;
import org.st.aliyun.ossuploader.model.UploadObject;

public class SqliteReadTask implements Callable<Integer> {

	private static Logger logger = Logger.getLogger(SqliteReadTask.class.getName());

	public SqliteReadTask(Context context) {
		this.context = context;
	}

	@Override
	public Integer call() throws Exception {
		logger.info("ReadSqliteTask begin.");
		Connection connection = null;
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:" + this.context.getDbInfo().getName());
			Statement statement = connection.createStatement();
			String TABLE_NAME = this.context.getDbInfo().getTable();
			String KEY_FIELD = this.context.getDbInfo().getKeyField();
			String DATA_FIELD = this.context.getDbInfo().getValueField();
			ResultSet rs = statement.executeQuery("select " + KEY_FIELD + 
					 ", " + DATA_FIELD + " from " + TABLE_NAME);
			int id = 0;
			int beginReadId = UploadResultManager.getBeginReadId(this.context.getUploadResult());
			int endReadId = UploadResultManager.getEndReadId(this.context.getUploadResult());

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
					
					UploadTask uploadTask = this.context.newUploadTask(uploadObject);
					this.context.getUploadExecutor().submit(uploadTask);
				} catch (SQLException e) {
					logger.error(id + ": ReadSqliteTask get data error.", e);
					AbstractOssUploader.uploadObjectStatusMap.put(id, UploadObjectStatus.ReadFailed);
				} catch (RejectedExecutionException e) {
					logger.error(id + ": UploadExecutor has shutdown.", e);
					break;
				}
				finally {
					++id;
					++totalReadCount;
				}
			} ;

//			UploadTask poisonPillTask = this.context.newUploadTask(UploadConstants.UPLOAD_OBJECT_POISON_PILL);
//			this.context.getUploadExecutor().submit(poisonPillTask);

			this.context.getUploadResult().setCurrentReadId(id);
			this.context.getUploadResult().setTotalReadSize(totalReadSize);
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

	private Context context;
}
