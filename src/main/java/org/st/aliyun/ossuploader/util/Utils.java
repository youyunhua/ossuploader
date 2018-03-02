package org.st.aliyun.ossuploader.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.log4j.Logger;

public class Utils {

	public static final String URL_COMPONENT_PATTERN = "(([a-z]|[A-Z]|\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(%[\\da-f]{2})|[!\\$&'\\(\\)\\*\\+,;=]|:)*";
	private static Logger logger = Logger.getLogger(Utils.class
			.getName());
	
	public static List<String> getAllShortDirs(String filePath) {
		File file = new File(filePath);
		String[] directories = file.list(new FilenameFilter() {
		  @Override
		  public boolean accept(File current, String name) {
		    return new File(current, name).isDirectory();
		  }
		});
		return Arrays.asList(directories);
	}
	
	// get all files recursively
	public static List<File> getFiles(List<File> files, File dir)
	{
	    if (files == null) {
			files = new ArrayList<File>();
		}

	    if (!dir.isDirectory())
	    {
	    	if (dir.exists()) {
	    		files.add(dir);
			}
	        return files;
	    }

	    for (File file : dir.listFiles()) {
			getFiles(files, file);
		}
	    return files;
	}
	
	public static boolean renameDir(String fromDir, String toDir) {
		File from = new File(fromDir);
		File to = new File(toDir);
		
		if ( !from.exists() || !from.isDirectory() ) {
			logger.warn("renameDir failed: fromDir not exist or not a dir. fromDir=" + fromDir);
			return false;
		}
		if (to.exists()) {
			logger.warn("renameDir failed: toDir exist. toDir=" + toDir);
			return false;
		}
		
		boolean result = from.renameTo(to);
		if ( !result) {
			logger.warn("renameDir failed. fromDir=" + fromDir + ", toDir=" + toDir);
		} else {
			logger.warn("renameDir success. fromDir=" + fromDir + ", toDir=" + toDir);
		}
		
		return result;
	}
	
	public static boolean checkDirAndCreateIfNotExist(String dirName) {
		File dir = new File(dirName);
		if (!dir.exists() && !dir.mkdirs()) {
			logger.error("Couldn't create dir: " + dirName);
			return false;
		}
		if (!dir.isDirectory()) {
			logger.error("it's not a directory: " + dirName);
			return false;			
		}

		return true;
	}
	
	public static void writeTextToFile(String text, String fileName) {
//		Path target = Paths.get(fileName.toString());
//		if (!Files.isReadable(target)) {
//			logger.warn("writeTextToFile: file exist, skipped. fileName=" + fileName);
//			return;
//		}
		
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
			bw.write(text);
		} catch (IOException e) {
			logger.warn("write text file failed. fileName=" + fileName, e);
		}
	}

	public static void downloadBinaryToFile(HttpClient httpclient, 
			String url, String fileName) throws Exception {
//		Path target = Paths.get(fileName.toString());
//		if (!Files.isReadable(target)) {
//			logger.warn("downloadBinaryToFile: file exist, skipped. fileName=" + fileName);
//			return;
//		}
		
        HttpGet httpget = new HttpGet(url);
        HttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        InputStream in = entity.getContent();

        File file = new File(fileName.toString());
        try {
            FileOutputStream fout = new FileOutputStream(file);
            int l = -1;
            byte[] tmp = new byte[1024];
            while ((l = in.read(tmp)) != -1) {
                fout.write(tmp,0,l);
            }
            fout.flush();
            fout.close();
        } finally {
            in.close();
        }
	}
	
	public static void downloadBinaryWithRetry(HttpClient httpclient, 
			String url, String fileName, int retryTimes, int sleepTime) {
		int retry = 0;
		do {
			try {
				Utils.downloadBinaryToFile(httpclient, url, fileName);
				retry = 0;
			} catch (Exception e) {
				++retry;
				if (retry < retryTimes) {
					logger.warn("write file failed. retry=" + retry 
							+ ", url=" + url + ", fileName=" + fileName
							+ ", msg=" + e.getMessage());
					try {
						Thread.sleep(sleepTime);
					} catch (InterruptedException e1) {
						logger.error("write file failed for sleep error. retry=" 
								+ retry + ", url=" + url 
								+ ", fileName=" + fileName
								+ ", msg=" + e.getMessage());
						retry = 0;
					}
				} else {
					logger.error("write file failed after retry " + retry 
							+ ", url=" + url + ", fileName=" + fileName
							+ ", msg=" + e.getMessage());
					retry = 0;
				}
			}							
		} while (retry != 0);
	}

	
	public static String[] addPrefixToList(String urlPrefix, List<String> list) {
		String[] resultArray = new String[list.size()];
		for (int i = 0; i < list.size(); ++i) {
			resultArray[i] = urlPrefix + list.get(i);
		}
		return resultArray;
	}
	
	public static int exec(String cmdLine) throws IOException, InterruptedException 
	{
        Process pr = Runtime.getRuntime().exec(cmdLine);

        String line;
        
        {
            BufferedReader in = new BufferedReader(new
                    InputStreamReader(pr.getInputStream()));
            while ((line = in.readLine()) != null) {
            	logger.info(line);
            }
            in.close();       	
        }
        
        {
            BufferedReader errorIn = new BufferedReader(new
                    InputStreamReader(pr.getErrorStream()));
            while ((line = errorIn.readLine()) != null) {
            	logger.warn(line);
            }
            errorIn.close();        	
        }

        pr.waitFor();
        
        return pr.exitValue();
	}
}
