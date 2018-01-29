package org.st.aliyun.ossuploader.task;

import static org.junit.Assert.*;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.st.aliyun.ossuploader.AbstractOssUploader;
import org.st.aliyun.ossuploader.Context;
import org.st.aliyun.ossuploader.AbstractOssUploader.UploadObjectStatus;
import org.st.aliyun.ossuploader.model.OssInfo;
import org.st.aliyun.ossuploader.model.UploadObject;
import org.st.aliyun.ossuploader.model.UploadResult;

public class OssUploadTaskTest {
	private static final OssInfo ossInfo = new OssInfo();
	private static Configuration config = null;

	private static final int MOCK_READ_ONE_UPLOADOBJECT_TIME_MS = 10;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
        Configurations configs = new Configurations(); 
        config = configs.properties(new File("config.properties"));         
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		ossInfo.setEndpoint(config.getString("oss.endpoint"));
		ossInfo.setPort(config.getInt("oss.port"));
		ossInfo.setConnectionTimeOut(config.getInt("oss.connectionTimeOut"));
		ossInfo.setSocketTimeOut(config.getInt("oss.socketTimeOut"));
		ossInfo.setBucket(config.getString("oss.bucket"));
		ossInfo.setPrefix(config.getString("oss.prefix"));
		ossInfo.setKey(config.getString("oss.key"));
		ossInfo.setSecret(config.getString("oss.secret"));
		ossInfo.setMaxConnections(config.getInteger("oss.maxConnections", 100));
		ossInfo.setMaxErrorRetry(config.getInteger("oss.maxErrorRetry", 5));
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test(expected = RuntimeException.class)
	public void testCall_uploadNullObject() throws Exception {
		OssUploadTask task = new OssUploadTask(null, null);
		task.call();
	}

	@Test
	public void testCall_uploadASimpleObject() throws Exception {
		UploadObject uploadObject = new UploadObject(0, "test", new byte[] {'a', 'b', 'd'});
		Context context = new ContextMock(null, ossInfo, new UploadResult(), null, 
				null, null);
		OssUploadTask task = new OssUploadTask(uploadObject, context);
		task.call();
	}

	@Test
	public void testCall_UnknownHost() throws Exception {
		Integer objectId = 0;
		UploadObject uploadObject = new UploadObject(objectId, "test", new byte[] {'a', 'b', 'd'});
		ossInfo.setEndpoint("57a80b96-4e89-4735-92b4-85f08f5482d7.com");
		ossInfo.setMaxErrorRetry(1);
		Context context = new ContextMock(null, ossInfo, new UploadResult(), null, 
				null, null);
		OssUploadTask task = new OssUploadTask(uploadObject, context);
		task.call();
		assertEquals(UploadObjectStatus.UploadFailed, AbstractOssUploader.uploadObjectStatusMap.get(objectId));
	}

	@Test
	public void testCall_NoSuchBucket() throws Exception {
		Integer objectId = 0;
		UploadObject uploadObject = new UploadObject(objectId, "test", new byte[] {'a', 'b', 'd'});
		ossInfo.setBucket("57a80b96-4e89-4735-92b4-85f08f5482d7");
		Context context = new ContextMock(null, ossInfo, new UploadResult(), null, 
				null, null);
		OssUploadTask task = new OssUploadTask(uploadObject, context);
		task.call();
		assertEquals(UploadObjectStatus.UploadError, AbstractOssUploader.uploadObjectStatusMap.get(objectId));
	}

	@Test
	public void testCallByExecutor_NoSuchBucket() throws Exception {
		Integer objectId = 0;
		UploadObject uploadObject = new UploadObject(objectId, "test", new byte[] {'a', 'b', 'd'});
		ossInfo.setBucket("57a80b96-4e89-4735-92b4-85f08f5482d7");
		ExecutorService uploadExecutor = Executors.newSingleThreadExecutor();
		Context context = new ContextMock(null, ossInfo, new UploadResult(), null, 
				uploadExecutor, null);
		OssUploadTask task = new OssUploadTask(uploadObject, context);
		uploadExecutor.submit(task);
		assertTrue(uploadExecutor.awaitTermination(10000L, TimeUnit.MILLISECONDS));
		
		assertTrue(uploadExecutor.isShutdown()); 
		assertEquals(UploadObjectStatus.UploadError, AbstractOssUploader.uploadObjectStatusMap.get(objectId));
	}

	@Test
	public void testCallBySingleExecutor_NoSuchBucket() throws Exception {
		System.out.println("testCallBySingleExecutor_NoSuchBucket begin.");
		ossInfo.setBucket("57a80b96-4e89-4735-92b4-85f08f5482d7");
		ExecutorService uploadExecutor = Executors.newSingleThreadExecutor();
		Context context = new ContextMock(null, ossInfo, new UploadResult(), null, 
				uploadExecutor, null);
		Integer i = 0;
		final int TASK_COUNT = 10000;
		for (; i < TASK_COUNT; ++i) {
			UploadObject uploadObject = new UploadObject(i, i.toString(), new byte[] {'a', 'b', 'd'});
			Thread.sleep(MOCK_READ_ONE_UPLOADOBJECT_TIME_MS);
			OssUploadTask task = new OssUploadTask(uploadObject, context);
			try {
				uploadExecutor.submit(task);							
			} catch (RejectedExecutionException e) {
				System.out.println("uploadExecutor has shutdown. i=" + i);
				assertTrue(uploadExecutor.isShutdown());
				break;
			}
		}
		System.out.println("submit tasks over. taskCount=" + i);
		assertTrue(uploadExecutor.awaitTermination(10000L, TimeUnit.MILLISECONDS));
		
		assertTrue(uploadExecutor.isShutdown()); 
		assertEquals(1, AbstractOssUploader.uploadObjectStatusMap.size());
		assertEquals(UploadObjectStatus.UploadError, AbstractOssUploader.uploadObjectStatusMap.get(0));
		
		assertTrue(1 <= i);
		assertTrue(i <= 1000);	// 
	}

	@Test
	public void testCallByFixedExecutor_NoSuchBucket() throws Exception {
		System.out.println("testCallByFixedExecutor_NoSuchBucket begin.");
		ossInfo.setBucket("57a80b96-4e89-4735-92b4-85f08f5482d7");
		int nThreads = 200;
		ExecutorService uploadExecutor = Executors.newFixedThreadPool(nThreads);
		Context context = new ContextMock(null, ossInfo, new UploadResult(), null, 
				uploadExecutor, null);
		Integer i = 0;
		final int TASK_COUNT = 10000;
		for (; i < TASK_COUNT; ++i) {
			UploadObject uploadObject = new UploadObject(i, i.toString(), new byte[] {'a', 'b', 'd'});
			// time of read uploadObject from db
			Thread.sleep(MOCK_READ_ONE_UPLOADOBJECT_TIME_MS);
			OssUploadTask task = new OssUploadTask(uploadObject, context);
			try {
				uploadExecutor.submit(task);							
			} catch (RejectedExecutionException e) {
				System.out.println("uploadExecutor has shutdown. i=" + i);
				assertTrue(uploadExecutor.isShutdown());
				break;
			}
		}
		System.out.println("submit tasks over. taskCount=" + i);
		assertTrue(uploadExecutor.awaitTermination(10000L, TimeUnit.MILLISECONDS));
		
		assertTrue(uploadExecutor.isShutdown()); 
		int mapSize = AbstractOssUploader.uploadObjectStatusMap.size();
		assertTrue(mapSize >= 1);
		assertTrue(mapSize <= nThreads);
	}

}
