package com.changdupay.util;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class LogManager {
	/**
	 * ��¼����
	 * 
	 * @param type
	 * @param time
	 */
	public static void writeLog(String logString) {
		String tempPath = "/sdcard/changdutest/";
		File changduFile = new File(tempPath);
		if (!changduFile.exists()) {
			boolean createSuccess = changduFile.mkdir();
			if (!createSuccess)
				return;
		}
		String logPath = tempPath + "log.txt";
		File logFile = new File(logPath);
		RandomAccessFile out = null;
		try {
			out = new RandomAccessFile(logFile, "rw");
			if (out.length() > 24000) {
				out.close();
				logFile.delete();
				out = new RandomAccessFile(logFile, "rw");
			}
		} catch (Exception e) {
			return;
		}
		String data = logString + '\n';
		try {
			out.seek(out.length());
			byte[] bytes = data.getBytes();
			out.write(bytes);
		} catch (IOException e) {
		}
	}
}
