package org.st.aliyun.ossuploader;

import static org.junit.Assert.*;

import java.io.IOException;
import java.time.LocalDateTime;

import org.junit.Test;
import org.st.aliyun.ossuploader.model.UploadResult;

public class UploadResultManagerTest {


	@Test
	public void testWriteUploadResult() {
		UploadResult uploadResult = new UploadResult();
		uploadResult.setLastUpdateTime(LocalDateTime.now().toString());
		try {
			UploadResultManager.writeUploadResult(uploadResult, "./");
		} catch (IOException e) {
			assertTrue("write error", false);
			e.printStackTrace();
		}
	}

	@Test
	public void testReadUploadResult() {
		UploadResult uploadResult = new UploadResult();
		uploadResult.setLastUpdateTime(LocalDateTime.now().toString());
		try {
			UploadResultManager.writeUploadResult(uploadResult, "./");
			UploadResult resultRead = UploadResultManager.readUploadResult("./");
			assertEquals(uploadResult, resultRead);
		} catch (IOException e) {
			assertTrue("read error", false);
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetBeginReadId() {
		UploadResult uploadResult = new UploadResult();
		assertEquals(0, UploadResultManager.getBeginReadId(uploadResult));
	}
	
	@Test
	public void testGetBeginReadId_setCurrentReadId() {
		UploadResult uploadResult = new UploadResult();
		final int CURRENT_ID = 10;
		uploadResult.setCurrentReadId(CURRENT_ID);
		assertEquals(CURRENT_ID, UploadResultManager.getBeginReadId(uploadResult));
	}

	@Test
	public void testGetEndReadId() {
		UploadResult uploadResult = new UploadResult();
		assertEquals(-1, UploadResultManager.getEndReadId(uploadResult));
	}

}
