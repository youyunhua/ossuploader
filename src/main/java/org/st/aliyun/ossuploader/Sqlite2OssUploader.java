package org.st.aliyun.ossuploader;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.st.aliyun.ossuploader.model.DbInfo;
import org.st.aliyun.ossuploader.model.OssInfo;
import org.st.aliyun.ossuploader.model.UploadResult;
import org.st.aliyun.ossuploader.task.OssUploadTask;
import org.st.aliyun.ossuploader.task.SqliteReadTask;

public class Sqlite2OssUploader extends AbstractOssUploader {
	
	private static Logger logger = Logger.getLogger(App.class.getName());
	
	public Sqlite2OssUploader(Context context) {
		this.context = context;
	}

	@Override
	public void run() {
		logger.info("Sqilte2OssUploader run begin.");
		try {
			Class.forName("org.sqlite.JDBC");
			 
			SqliteReadTask readTask = new SqliteReadTask(context);
			Future<Integer> readCount = context.getReadExecutor().submit(readTask);
			
			logger.info("readCount=" + readCount.get());
			context.getReadExecutor().shutdown();
			context.getReadExecutor().awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
			
			context.getUploadExecutor().shutdown();
			context.getUploadExecutor().awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
			
			logger.info("Sqilte2OssUploader run succeed.");
		} catch (ClassNotFoundException e) {
			logger.warn("Sqlite JDBC driver not found.");
		} catch (InterruptedException e) {
			logger.warn("Interrupted.");
			Thread.currentThread().interrupt();
		} catch (ExecutionException e) {
			logger.error("Sqilte2OssUploader read execute error.", e);
		}
	}

    ///////////////////////////////////////////////////////////////
    // private properties and methods
	private Context context;
}
