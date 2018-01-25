package org.st.aliyun.ossuploader.task;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Assert;
import org.junit.Test;
import org.st.aliyun.ossuploader.model.DbInfo;
import org.st.aliyun.ossuploader.model.OssInfo;
import org.st.aliyun.ossuploader.model.UploadObject;
import org.st.aliyun.ossuploader.model.UploadResult;
import org.st.aliyun.ossuploader.task.OssUploadTask;
import org.st.aliyun.ossuploader.task.SqliteReadTask;
import org.st.aliyun.ossuploader.task.UploadTask;

public class SqliteReadTaskTest {

	private static final String TEST_SQLITE = "test.sqlite";
	private static final int TEST_SQLITE_DATA_COUNT = 270;

	@Test(expected = SQLException.class)
	public void testCall_dbNotExist() throws Exception {
		String notExistDbFileName = "xx:\\_xxx\\__xxx.sqlite";
		DbInfo dbInfo = new DbInfo();
		dbInfo.setName(notExistDbFileName);
		SqliteReadTask task = new SqliteReadTask(dbInfo, null, null, null, null);
		task.call();
	}

	@Test(expected = SQLException.class)
	public void testCall_dbNotExistMayCreate() throws Exception {
		String notExistDbFileName = "c:\\_xxx\\__xxx.sqlite";
		try {
			Files.deleteIfExists(Paths.get(notExistDbFileName));
			assertFalse(new File(notExistDbFileName).exists());
			DbInfo dbInfo = new DbInfo();
			dbInfo.setName(notExistDbFileName);
			SqliteReadTask task = new SqliteReadTask(dbInfo, null, null, null, null);
			task.call();
		} finally {
			Files.deleteIfExists(Paths.get(notExistDbFileName));
		}
	}

	@Test(expected = SQLException.class)
	public void testCall_tableNotExist() throws Exception {
		String testDbFileName = "empty.sqlite";
		assertTrue(new File(testDbFileName).exists());
		DbInfo dbInfo = new DbInfo();
		dbInfo.setName(testDbFileName);
		SqliteReadTask task = new SqliteReadTask(dbInfo, null, null, null, null);
		task.call();
	}

	@Test(expected = SQLException.class)
	public void testCall_keyFieldNotExist() throws Exception {
		String testDbFileName = TEST_SQLITE;
		assertTrue(new File(testDbFileName).exists());
		DbInfo dbInfo = new DbInfo();
		dbInfo.setTable("GMAP_Data");
		dbInfo.setKeyField("Id_xxx");
		dbInfo.setValueField("Data");
		dbInfo.setName(testDbFileName);
		SqliteReadTask task = new SqliteReadTask(dbInfo, null, null, null, null);
		task.call();
	}
	
	@Test(expected = SQLException.class)
	public void testCall_valueFieldNotExist() throws Exception {
		String testDbFileName = TEST_SQLITE;
		assertTrue(new File(testDbFileName).exists());
		DbInfo dbInfo = new DbInfo();
		dbInfo.setTable("GMAP_Data");
		dbInfo.setKeyField("Id");
		dbInfo.setValueField("Data_xxx");
		dbInfo.setName(testDbFileName);
		SqliteReadTask task = new SqliteReadTask(dbInfo, null, null, null, null);
		task.call();
	}

	@Test
	public void testCall_traverse() throws Exception {
		String testDbFileName = TEST_SQLITE;
		assertTrue(new File(testDbFileName).exists());
		DbInfo dbInfo = new DbInfo();
		dbInfo.setTable("GMAP_Data");
		dbInfo.setKeyField("Id");
		dbInfo.setValueField("Data");
		dbInfo.setName(testDbFileName);
		ExecutorService uploadExecutor = Executors.newSingleThreadExecutor();
		SqliteReadTask task = new SqliteReadTask(dbInfo, new UploadResult(), uploadExecutor, 
				PrintUploadTask.class, null);
		task.call();
	}

	@Test
	public void testCall_readCount() throws Exception {
		String testDbFileName = TEST_SQLITE;
		assertTrue(new File(testDbFileName).exists());
		DbInfo dbInfo = new DbInfo();
		dbInfo.setTable("GMAP_Data");
		dbInfo.setKeyField("Id");
		dbInfo.setValueField("Data");
		dbInfo.setName(testDbFileName);
		ExecutorService uploadExecutor = Executors.newSingleThreadExecutor();
		SqliteReadTask task = new SqliteReadTask(dbInfo, new UploadResult(), uploadExecutor, 
				EmptyUploadTask.class, null);
		Integer readCount = task.call();
		Assert.assertEquals(readCount.intValue(), TEST_SQLITE_DATA_COUNT);
	}

	@Test
	public void testCall_readCountSkipSome() throws Exception {
		String testDbFileName = TEST_SQLITE;
		assertTrue(new File(testDbFileName).exists());
		DbInfo dbInfo = new DbInfo();
		dbInfo.setTable("GMAP_Data");
		dbInfo.setKeyField("Id");
		dbInfo.setValueField("Data");
		dbInfo.setName(testDbFileName);
		final int SKIP_COUNT = 10;
		UploadResult uploadResult = new UploadResult();
		uploadResult.setCurrentReadId(SKIP_COUNT);
		ExecutorService uploadExecutor = Executors.newSingleThreadExecutor();
		SqliteReadTask task = new SqliteReadTask(dbInfo, uploadResult, uploadExecutor, 
				EmptyUploadTask.class, null);
		Integer readCount = task.call();
		Assert.assertEquals(readCount.intValue(), TEST_SQLITE_DATA_COUNT - SKIP_COUNT);
	}

//	@Test
//	public void testSqliteJdbc() {
//		Connection connection = null;
//		try {
//			// create a database connection
//			connection = DriverManager.getConnection("jdbc:sqlite:test.sqlite");
//			Statement statement = connection.createStatement();
//			statement.setQueryTimeout(30); // set timeout to 30 sec.
//
//			ResultSet rs = statement.executeQuery("select * from GMAP_Data");
//			while (rs.next()) {
//				// read the result set
//				System.out.println("key = " + rs.getString("Id"));
//				byte[] data = rs.getBytes("data");
//				System.out.println("value.size = " + data.length);
//			}
//		} catch (SQLException e) {
//			// if the error message is "out of memory",
//			// it probably means no database file is found
//			System.err.println(e.getMessage());
//		} finally {
//			try {
//				if (connection != null)
//					connection.close();
//			} catch (SQLException e) {
//				// connection close failed.
//				System.err.println(e);
//			}
//		}
//	}

}
