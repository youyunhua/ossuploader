package org.st.aliyun.ossuploader.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

public class GZipUtil {
	/**
	  * Checks if a binary data is gzipped.
	  * 
	  * @param in
	  * @return
	  */
	 public static boolean isGZipped(byte[] data) {
		 if (data.length < 2) {
			return false;
		}
		int magic = data[0] & 0xff | ((data[1] << 8) & 0xff00);
		return magic == GZIPInputStream.GZIP_MAGIC;
	 }
	 
	 public static boolean isGZipped(File file) throws IOException {
		 FileInputStream fis = new FileInputStream(file);
		 try {
			 byte[] b = new byte[2];
			 fis.read(b, 0, 2);
			 return isGZipped(b);			 
		 } finally {
			 fis.close();
		 }
	 }
}
