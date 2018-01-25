package org.st.aliyun.ossuploader;

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
import org.st.aliyun.ossuploader.task.OssUploadTask;
import org.st.aliyun.ossuploader.task.SqliteReadTask;
import org.st.aliyun.ossuploader.task.UploadTask;

public class ReadSqliteTaskTest {

	@Test(expected = SQLException.class)
	public void testCall_dbNotExist() throws Exception {
		String notExistDbFileName = "xx:\\_xxx\\__xxx.sqlite";
		DbInfo dbInfo = new DbInfo();
		dbInfo.setName(notExistDbFileName);
		SqliteReadTask task = new SqliteReadTask(dbInfo, null, null, null);
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
			SqliteReadTask task = new SqliteReadTask(dbInfo, null, null, null);
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
		SqliteReadTask task = new SqliteReadTask(dbInfo, null, null, null);
		task.call();
	}

	@Test(expected = SQLException.class)
	public void testCall_keyFieldNotExist() throws Exception {
		String testDbFileName = "test.sqlite";
		assertTrue(new File(testDbFileName).exists());
		DbInfo dbInfo = new DbInfo();
		dbInfo.setTable("GMAP_Data");
		dbInfo.setKeyField("Id_xxx");
		dbInfo.setValueField("Data");
		dbInfo.setName(testDbFileName);
		SqliteReadTask task = new SqliteReadTask(dbInfo, null, null, null);
		task.call();
	}
	
	@Test(expected = SQLException.class)
	public void testCall_valueFieldNotExist() throws Exception {
		String testDbFileName = "test.sqlite";
		assertTrue(new File(testDbFileName).exists());
		DbInfo dbInfo = new DbInfo();
		dbInfo.setTable("GMAP_Data");
		dbInfo.setKeyField("Id");
		dbInfo.setValueField("Data_xxx");
		dbInfo.setName(testDbFileName);
		SqliteReadTask task = new SqliteReadTask(dbInfo, null, null, null);
		task.call();
	}

	@Test
	public void testCall_traverse() throws Exception {
		String testDbFileName = "test.sqlite";
		assertTrue(new File(testDbFileName).exists());
		DbInfo dbInfo = new DbInfo();
		dbInfo.setTable("GMAP_Data");
		dbInfo.setKeyField("Id");
		dbInfo.setValueField("Data");
		dbInfo.setName(testDbFileName);
		ExecutorService uploadExecutor = Executors.newSingleThreadExecutor();
		SqliteReadTask task = new SqliteReadTask(dbInfo, null, uploadExecutor, PrintUploadTask.class);
		task.call();
	}

	@Test
	public void testCall_readCount() throws Exception {
		String testDbFileName = "test.sqlite";
		assertTrue(new File(testDbFileName).exists());
		DbInfo dbInfo = new DbInfo();
		dbInfo.setTable("GMAP_Data");
		dbInfo.setKeyField("Id");
		dbInfo.setValueField("Data");
		dbInfo.setName(testDbFileName);
		ExecutorService uploadExecutor = Executors.newSingleThreadExecutor();
		SqliteReadTask task = new SqliteReadTask(dbInfo, null, uploadExecutor, EmptyUploadTask.class);
		Integer readCount = task.call();
		Assert.assertEquals(readCount.intValue(), 271);
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
