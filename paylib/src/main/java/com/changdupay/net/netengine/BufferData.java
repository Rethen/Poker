package com.changdupay.net.netengine;


/**
 * 本地缓存数据
 * @author Administrator
 *
 */
public class BufferData {
	
	private byte[] byteBuffer; 
	
	private String fileName;

	
	public String getFileName() {
		return fileName;
	}


	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	/**
	 * 获取缓存数据
	 * @return
	 */
	public byte[] getByteBuffer() {
		return byteBuffer;
	}

	
	/**
	 * 设置缓存数据
	 * @param byteArrayBuffer
	 */
	public void setByteBuffer(byte[] byteBuffer) {
		this.byteBuffer = byteBuffer;
	}
 
	
}
