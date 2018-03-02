package org.st.aliyun.ossuploader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.st.aliyun.ossuploader.model.UploadResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class UploadResultManager {
	private static Logger logger = Logger.getLogger(UploadResultManager.class.getName());

	public static UploadResult readUploadResult(String uploadResultPath) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		String shortFileName = "uploadResult.json";
		File file = new File(uploadResultPath + shortFileName);
		if (!file.exists()) {
			return null;
		}
		try {
			return mapper.readValue(file, UploadResult.class);
		} catch (IOException e) {
			logger.error("readUploadResult failed!", e);
			throw e;
		}
	}

	public static void writeUploadResult(UploadResult uploadResult, String uploadResultPath) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		String shortFileName = "uploadResult.json";
		File file = new File(uploadResultPath + shortFileName);
		try {
			mapper.writeValue(file, uploadResult);
		} catch (IOException e) {
			logger.error("writeUploadResult failed!", e);
			throw e;
		}
		logger.info("writeUploadResult ok! uploadResult=" + uploadResult);
	}
	
	// read ids range is [BeginRead, EndReadId)
	public static int getBeginReadId(UploadResult uploadResult) {
		if (uploadResult == null) {
			return 0;
		}
		
		if (uploadResult.getReadOvered()) {
			return -1;
		}
		
		TreeSet<Integer> ids = uploadResult.getReadButNotUploadIds();
		if (ids == null || ids.isEmpty()) {
			Integer curReadId = uploadResult.getCurrentReadId();
			return curReadId == null ? 0 : curReadId;
		}
		
		return ids.first();
	}

	// read ids range is [BeginRead, EndReadId)
	public static int getEndReadId(UploadResult uploadResult) {
		if (uploadResult == null) {
			return -1;
		}

		// first read db
		TreeSet<Integer> ids = uploadResult.getReadButNotUploadIds();
		if (ids == null) {
			return -1;
		}
		
		TreeSet<Integer> readFailedIds = uploadResult.getReadFailedIds();
		if (uploadResult.getReadOvered()) {
			if (readFailedIds.isEmpty()) {
				return 0;
			} else {
				return uploadResult.getReadFailedIds().last();				
			}
		}
		
		return -1;
	}
	
}
