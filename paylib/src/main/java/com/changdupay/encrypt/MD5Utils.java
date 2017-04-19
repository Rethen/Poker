package com.changdupay.encrypt;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.text.TextUtils;
import android.util.Log;

public class MD5Utils {
    private MessageDigest md5 = null;

    public MD5Utils() {
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            Log.e("md5", e.getMessage());
        }
    };

    public String MD5_Encode(String string) {
        return getStringHash(string);
    }
    
    public String MD5_Encode(byte[] source) {
        String hash = null;
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(source);
            hash = getStreamHash(in);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hash;
    }
    
    public String getStringHash(String source) {
        String hash = null;
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(source.getBytes());
            hash = getStreamHash(in);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hash;
    }

    public String getFileHash(String filePath) {
        String hash = "";
        try {
        	if(!TextUtils.isEmpty(filePath)){
        		File file=new File(filePath);
        		if(file.exists() && file.isFile()){
        			hash = getStreamHash(new FileInputStream(file));//
        		}
        	}
        } catch (Exception e) {
           Log.e("getFileHash", e.getMessage());
        }
        return hash;
    }
    
    public String getStreamHash(InputStream stream) {
        if (md5 == null) {
            return "";
        }
        String hash = null;
        byte[] buffer = new byte[1024*8];
        BufferedInputStream in = null;
        try {
            in = new BufferedInputStream(stream);
            int numRead = 0;
            while ((numRead = in.read(buffer)) > 0) {
                md5.update(buffer, 0, numRead);
            }
           
            hash = toHexString(md5.digest());
        } catch (Exception e) {
            Log.e("getStreamHash", e.getMessage());
        }finally{
            if (in != null){
            	try {
                    in.close();
                } catch (Exception e) {
                	 Log.v("getStreamHash",e.getMessage());
                }
            }
        }
        
        return hash;
    }

    private String toHexString(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(hexChar[(b[i] & 0xf0) >>> 4]);
            sb.append(hexChar[b[i] & 0x0f]);
        }
        return sb.toString();
    }

    private char[] hexChar = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
            '9', 'a', 'b', 'c', 'd', 'e', 'f' };
}
