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

public class Sqilte2OssUploader extends AbstractOssUploader {
	
	private static Logger logger = Logger.getLogger(App.class.getName());
	
	public Sqilte2OssUploader(DbInfo dbInfo, OssInfo ossInfo) {
		this.dbInfo = dbInfo;
		this.ossInfo = ossInfo;
	}

	@Override
	public void run() {
		logger.info("Sqilte2OssUploader run begin.");
		try {
			UploadResult uploadResult = UploadResultManager.readUploadResult("./");
			
			ExecutorService readExecutor = Executors.newSingleThreadExecutor();
			int nThreads = App.config.getInt("thread.upload.count");
			ExecutorService uploadExecutor = Executors.newFixedThreadPool(nThreads);
			
			SqliteReadTask readTask = new SqliteReadTask(this.dbInfo, uploadResult
					, uploadExecutor, OssUploadTask.class, this.ossInfo);
			Future<Integer> readCount = readExecutor.submit(readTask);
			
			logger.info("readCount=" + readCount.get());
			readExecutor.shutdown();
			readExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
			
			uploadExecutor.shutdown();
			uploadExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
			
			logger.info("Sqilte2OssUploader run succeed.");
		} catch (IOException e) {
			logger.error("Sqilte2OssUploader run error.", e);
		} catch (InterruptedException e) {
			logger.warn("Interrupted.");
			Thread.currentThread().interrupt();
		} catch (ExecutionException e) {
			logger.error("Sqilte2OssUploader read execute error.", e);
		}
	}

    ///////////////////////////////////////////////////////////////
    // private properties and methods
    private DbInfo dbInfo;
    private OssInfo ossInfo;
}
