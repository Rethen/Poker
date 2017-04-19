package com.changdupay.net.netengine;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Stack;

import com.changdupay.encrypt.DesUtils;
import com.changdupay.encrypt.MD5Utils;
import com.changdupay.protocol.pay.PayInfoEncryptType;
import com.changdupay.util.PayConfigReader;


/**
 * @author csy
 */
public class NetReader {
	private static final String ERROR_LENGTH = "binary length error!";
	private static final String ERROR_DATA_LENGTH = "data length error!";
	private static final String ERROR_DATA_EMPTY = "input binary data is empty!";
	
	private Stack<RecordInfo> recordInfoStack;
	private int position;
	private byte[] data;
	
	private int resultCode;
	private String errorMsg;
	private int actionId = 0;
	private boolean debugMode;
	private int signType;
	private String signValue;
	private long merchantId;
	private long appId;
	private String ver;
	private short dataFormat;
	private short hasCompress;

	private int rmId;
	private String strStime;
	
	/**
	 * Create
	 * @param data:  binary data
	 */
	public NetReader(byte[] data) {
		recordInfoStack = new Stack<RecordInfo>();
		this.data = data;
		reset();
	}
	
	public void reset() {
		position = 0;
		debugMode = false;
	}
	
	/**
	 * debug mode will print string data
	 */
	public void openDebugMode() {
		debugMode = true;
	}
	
	public boolean getResult() {
		return resultCode == 1;
	}
	
	public String getErrorMsg() {
		return errorMsg;
	}
	
	public long getAppId() {
		return appId;
	}
	
	  //将byte数组写入文件  
    public void createFile(String path, byte[] content) throws IOException {  
  
        FileOutputStream fos = new FileOutputStream(path);  
  
        fos.write(content);  
        fos.close();  
    }
	
	
	public boolean checkSign(int count) {
		byte[] appkeyBytes = PayConfigReader.getInstance().getPayConfigs().LocalKey.getBytes();		
		byte[] signBytes = new byte[count - 42 + appkeyBytes.length];
        
        System.arraycopy(data, 42, signBytes, 0, signBytes.length - appkeyBytes.length);
        System.arraycopy(appkeyBytes, 0, signBytes, signBytes.length - appkeyBytes.length, appkeyBytes.length);
        
        String signString = new String(signBytes);
        
        String tmpSign = "";
		switch(signType)
		{
		case PayInfoEncryptType.MD5:
			MD5Utils md5 = new MD5Utils();
			tmpSign = md5.MD5_Encode(signBytes);
			md5 = null;
			break;
		case PayInfoEncryptType.RSA:
			//TODO
			break;
		case PayInfoEncryptType.THREEDES:
			tmpSign = DesUtils.encryptMode(signString.getBytes(), null).toString();
			break;
		}

		return tmpSign.equals(signValue);
	}
		
	/*
	public boolean checkSign(String strContent) {
		StringBuilder sb = new StringBuilder();
		sb.append(resultCode);
		sb.append(errorMsg);
		sb.append(actionId);
		sb.append(merchantId);
		sb.append(appId);
		sb.append(ver);
		sb.append(dataFormat);
		sb.append(hasCompress);
		sb.append(strContent);
		sb.append(PayConst.APPKEY);
		String tmpValue = sb.toString();
		String tmpSign = "";
		switch(signType)
		{
		case PayInfoEncryptType.MD5:
			MD5Utils md5 = new MD5Utils();
			tmpSign = md5.MD5_Encode(tmpValue);
			md5 = null;
			break;
		case PayInfoEncryptType.RSA:
			//TODO
			break;
		case PayInfoEncryptType.THREEDES:
			tmpSign = DesUtils.encryptMode(tmpValue.getBytes()).toString();
			break;
		}
		
		return tmpSign.equals(signValue);
	}
	*/
	
	public boolean headCheck() {
		if (data == null) {
			debugOutput(ERROR_DATA_EMPTY);
			return false;
		}
		//包长度（4）
		int count = readInt();
		if (count != data.length) {
			errorMsg = ERROR_LENGTH;
			debugOutput(errorMsg);
			return false;
		}
		//sign type
		signType = readShort();
		//sign
		signValue = readString();

		resultCode = readInt();
		errorMsg = readString();
		//if (result != 0x2710) {
		if (resultCode != 1) {
			debugOutput(errorMsg);
			return false;
		}
		
		actionId = readInt();	// action id
		merchantId = readInt64();
		appId = readInt64();
		ver = readString();
		dataFormat = readShort();
		hasCompress = readShort();
		
		return checkSign(count);
	}

	public boolean headCheck_ChangduReader() {
		if (data == null) {
			debugOutput(ERROR_DATA_EMPTY);
			return false;
		}
		int count = readInt();
		if (count != data.length) {
			errorMsg = ERROR_LENGTH;
			debugOutput(errorMsg);
			return false;
		}
		resultCode = readInt();
		rmId = readInt(); 	// rmid
		errorMsg = readString();
		if (resultCode != 0x2710) {
			debugOutput(errorMsg);
			return false;
		}
		actionId = readInt();	// action id
		strStime = readString();
		return true;
	}
	
	public int getActionId() {
		return actionId;
	}
	
	/**
	 * find and jump to next struct 
	 */
	public void recordBegin() {
		if (errorLength(4)) {
			return;
		}
		int recordLength = readInt();
		RecordInfo recordInfo = new RecordInfo(position - 4, recordLength);
		recordInfoStack.push(recordInfo);
	}
	
	/**
	 * find and jump to current struct end
	 */
	public void recordEnd() {
		RecordInfo recordInfo = recordInfoStack.pop();
		if (recordInfo != null) {
			position = recordInfo.getRecordPositionEnd();
		}
	}
	
	public long readInt64() {
		if (errorLength(8)) {
			return 0;
		}
		long result = (data[position + 7] & 0xFF) << (8*7);
		result += (data[position + 6] & 0xFF) << (8*6);
		result += (data[position + 5] & 0xFF) << (8*5);
		result += (data[position + 4] & 0xFF) << (8*4);
		result += (data[position + 3] & 0xFF) << (8*3);
		result += (data[position + 2] & 0xFF) << (8*2);
		result += (data[position + 1] & 0xFF) << (8*1);
		result += data[position] & 0xFF;
		position += 8;
		debugOutput("" + result);
		return result;
	}
	
	public int readInt() {
		if (errorLength(4)) {
			return 0;
		}

		int result = (data[position + 3] & 0xFF) << 24;
		result += (data[position + 2] & 0xFF) << 16;
		result += (data[position + 1] & 0xFF) << 8;
		result += data[position] & 0xFF;
		position += 4;
		debugOutput("" + result);
		return result;
	}
	
	public float readByte() {
		if (errorLength(1)) {
			return 0;
		}

		int result = data[position];
		position += 1;
		debugOutput("" + result);
		return result;
	}
	
	public char readChar() {
		return (char)readByte();
	}
	
	public short readShort() {
		if (errorLength(2)) {
			return 0;
		}

		byte[] b = {data[position + 1], data[position + 0]};
		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(b));
		short result = 0;
		try {
			result = dis.readShort();
			dis.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		position += 2;
		debugOutput("" + result);
		return result;
	}
	
	
	public String readString() {
		int stringLength = readInt();
		if (stringLength > 0) {
			String string;
			try {
				string = new String(data, position, stringLength, "utf8");
				position += stringLength;
				debugOutput(string);
				return string;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			position += stringLength;
		}
		debugOutput("");
		return "";
	}
	
	public float readFloat() {
		if (errorLength(4)) {
			return 0;
		}

		byte[] b = {data[position + 3], data[position + 2], data[position + 1], data[position + 0]};
		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(b));
		float result = 0;
		try {
			result = dis.readFloat();
			dis.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		position += 4;
		debugOutput("" + result);
		return result;
	}
	
	public double readDouble() {
		if (errorLength(8)) {
			return 0;
		}

		byte[] b = {data[position + 7], data[position + 6], data[position + 5], data[position + 4], data[position + 3], data[position + 2], data[position + 1], data[position + 0]};
		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(b));
		double result = 0;
		try {
			result = dis.readDouble();
			dis.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		position += 8;
		debugOutput("" + result);
		return result;
	}
	
	private void debugOutput(String stringData) {
		if (stringData == null || stringData.equals("")) {
			stringData = "Data NULL";
		}
		if (debugMode) {
			for (int i = 0; i < recordInfoStack.size(); i++) {
				stringData = "    " + stringData;
			}
			stringData = "[" + stringData;
			System.out.println(stringData);
		}
	}
	
	private boolean errorLength(int needLength) {
		if (position + needLength > data.length) {
			debugOutput(ERROR_DATA_LENGTH);
			return true;
		}
		return false;
	}
	
	
	
	private class RecordInfo {
		private int position;
		private int lentgth;
		
		public RecordInfo(int position, int recordLength) {
			this.position = position;
			this.lentgth = recordLength;
		}
		
		public void setPositon(int position) {
			this.position = position;
		}
		
		public void setLength(int recordLength) {
			lentgth = recordLength;
		}
		
		public int getRecordPositionBegin() {
			return position;
		}
		
		public int getRecordPositionEnd() {
			return position + lentgth;
		}
	}
}
