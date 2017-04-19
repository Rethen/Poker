package com.changdupay.net.netengine;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.apache.http.HttpStatus;

import android.content.Context;
import android.os.Message;
import android.os.StatFs;
import android.os.SystemClock;
import android.util.Log;

import com.changdupay.commonInterface.BaseCommonStruct;
import com.changdupay.commonInterface.CommonConst;
import com.changdupay.commonInterface.CommonInterfaceManager;

/**
 *  
 * @author Administrator
 *
 */
class HttpTransferService{ 
  
    private static Map<Integer,HttpURLConnection> sessionMap = new HashMap<Integer,HttpURLConnection>();   
    private static Map<Integer,String> filePathMap = new HashMap<Integer,String>();
    private static Map<Integer,Long> fileSizeMap = new HashMap<Integer,Long>();  
    private static Map<Integer,Thread> threadMap = new HashMap<Integer,Thread>();
    private static Map<Integer,HashMap<String,Object>> requestPropertyMap = new HashMap<Integer,HashMap<String,Object>>(); 
    private static Map<Integer,Map<String, List<String>>> headerFields = new HashMap<Integer,Map<String, List<String>>>();
    private static long sleepTime = 0; 
    private static boolean isCache = false;
    private static int percent = 100;
    
    NetUrlContent urlContent = new NetUrlContent();
    BaseCommonStruct refreshCoidStruct=null;
     
    protected void setSleepTime(long sleepTime) {
		HttpTransferService.sleepTime = sleepTime;
	}
    
    /**
     * 设置下载通知粒度
     * @param maxDownPoint
     */
    public void setMaxDownPoint(int maxDownPoint){
    	percent = maxDownPoint;
    }

	/**
     * 
     * @param sessionId
     * @return
     */
    public HttpURLConnection getHttpURLConnectionByMap(int sessionId){
    	return sessionMap.get(sessionId);
    }
    
    public Map<String, List<String>> getResponseHeaderFields(int sessionId){
    	return headerFields.get(sessionId);
    }
    
    /**
     * 设置是否缓存所有的网络请求头信息
     * @param isCache
     */
    public void setCacheHeaderFields(boolean isCache){
    	this.isCache = isCache;
    }
    
    /**
     * 移除网络请求头信息缓存
     * @param sessionId
     */
    public void remoreCacheHeaderFields(int sessionId){
    	if(headerFields != null){
    		headerFields.remove(sessionId);
    	}
    }
    
    /**
     * 清除所有网络请求头信息缓存 
     */
    public void clearCacheHeaderFields(){
    	if(headerFields != null){
    		headerFields.clear();
    	}
    }
    
    /**
     * 
     */
    private HashMap<String,Object> combineParamMap(HashMap<String,Object> hashmap){
    	HashMap<String,Object> tempMap = new HashMap<String,Object>();
    	tempMap.put("ACCEPT", "*/*");
    	tempMap.put("CONNECTION", "Keep-Alive");
    	tempMap.put("CONTENT-TYPE", "application/x-www-form-urlencoded");
    	tempMap.put("ACCEPT-LANGUAGE", "zh-cn");
    	tempMap.put("ACCEPT-CHARSET", "UTF-8");
    	
//    	tempMap.put("DOINPUT", false);
//    	tempMap.put("DOOUTPUT", true);
//    	tempMap.put("USECACHES", true);
//    	tempMap.put("ALLOWUSERINTERACTION", true);
    	 
    	
    	if(hashmap != null && !hashmap.isEmpty()){
    		for(Map.Entry<String, Object> entry : hashmap.entrySet()){
    			tempMap.put(entry.getKey().toUpperCase(), entry.getValue());
        	}
    	}
    	return tempMap;
    }
    
    
    
    private String utf8URLEncode(String url){
    	StringBuffer sb = new StringBuffer();
    	for(int i=0;i<url.length();i++){
    		char c = url.charAt(i);
    		if(c >= 0 && c <= 127){ 
    			sb.append(c);
    		}else{
    			byte[] b = new byte[0];
    			try{
    				b = Character.toString(c).getBytes("UTF-8");
    			}catch(Exception e){
    				
    			}
    			for(int j=0;j<b.length;j++){
    				int k = b[j];
    				if(k < 0){
    					k += 256;
    				}
    				b[j] = (byte) k;
//    				sb.append("%"+Integer.toHexString(k).toUpperCase()); 
    			}
    			sb.append(URLEncoder.encode(new String(b)));
    		}
    	}
    	return sb.toString();
    }
    
	/**
	 * 在android里遇到如192.168.1.1:8080取到的host是：192.168.1.1:8080，而port是：-1；会出现链接异常
	 * 该方法就是用于分离host和port。
	 * @return
	 */
    private String[] splitHostAndPort(String host){
    	if(host != null && host.indexOf(":") != -1){
    		return host.split(":");
    	}
    	return null;
    }
    
	private HttpURLConnection initConnection(Context ctx,String strUrl,HashMap<String,Object> hashmap,int sessionId,MessageHandler mHandler) throws ProtocolException,IOException{   
//		sendMessage(-1, sessionId, -999, new Exception("nd....url is" + strUrl), 0,mHandler);
		
		strUrl = strUrl.replace(" ", "%20");  
		strUrl = utf8URLEncode(strUrl);    
		
//		sendMessage(-1, sessionId, -999, new Exception("%20 deal after nd....url is" + strUrl), 0,mHandler);
		
		URL url = new URL(strUrl);
		String host = url.getHost();
		if(host != null && host.indexOf(":") != -1){
			//在android里遇到如192.168.1.1:8080取到的host是：192.168.1.1:8080，而port是：-1；会出现链接异常
			//需要分离host和port
    		String[] hostPort = host.split(":");
    		url = new URL(url.getProtocol(),hostPort[0],Integer.parseInt(hostPort[1]),url.getFile());
    		
    	} 
		
//		sendMessage(-1, sessionId, -999, new Exception("the last nd....url is" + strUrl), 0,mHandler);
		
		HttpURLConnection conn = NetChoose.getAvailableNetwork(ctx,url); 
		if(conn == null){
//			sendMessage(-1, sessionId, -999, new Exception("conn is null"), 0,mHandler);
			
			sendMessage(MessageHandler.BUILD_CONNECT, sessionId, Constant.CONNECT_ERROR, null, 0,mHandler);
			return null;
		}else{
//			sendMessage(-1, sessionId, -999, new Exception("conn is success "), 0,mHandler);
			
			sendMessage(MessageHandler.BUILD_CONNECT, sessionId, Constant.CORRECT, null, 0,mHandler);
		}
		 
		
        conn.setConnectTimeout(ConstantParam.connectTimeout);  
        conn.setReadTimeout(ConstantParam.readTimeout);
        
        
        HashMap<String,Object> hashmapTemp = combineParamMap(hashmap);
        for(Map.Entry<String, Object> entry : hashmapTemp.entrySet()){
        	Object value = entry.getValue();
        	String key = entry.getKey().toUpperCase();
        	if(value instanceof Boolean){
        		if(key.equals("DOINPUT")){
        			conn.setDoInput(Boolean.valueOf(value.toString()));
        		}else if(key.equals("DOOUTPUT")){
        			conn.setDoOutput(Boolean.valueOf(value.toString()));
        		}else if(key.equals("USECACHES")){
        			conn.setUseCaches(Boolean.valueOf(value.toString()));
        		}else if(key.equals("ALLOWUSERINTERACTION")){
        			conn.setAllowUserInteraction(Boolean.valueOf(value.toString()));
        		}
        	}else{
        		conn.setRequestProperty(key, (String)value); 
        	}
//        	conn.setRequestProperty(key.toUpperCase(), (String)value); 
    	} 
        requestPropertyMap.put(sessionId, hashmapTemp);
        
		return conn;
	}
	 
	 
	  
	 private String reDealPath(String url,String filePath){
		 int pathIndex = filePath.lastIndexOf("/");
		 if(pathIndex != -1){ 
			 String lastfilePath = filePath.substring(pathIndex+1);
			 if(lastfilePath.indexOf(".") != -1){// 
				 String file = filePath.substring(0, pathIndex + 1);
				 if(file != null && file.length() > 0){
					 File f = new File(file);
					 if(!f.exists()){
						 f.mkdirs();
					 }
				 }
				 return filePath;
			 }
		 }
		  
		 if(filePath != null && filePath.length() > 0){
			 File f = new File(filePath);
			 if(!f.exists()){
				 f.mkdirs();
			 }
		 }
		 
		 /*String m = conn.getHeaderField("CONTENT-DISPOSITION");
		 if(m != null){
			 int mIndex = m.indexOf(".");
			 if(mIndex != -1){//http 
				 String tempM = m.substring(0, mIndex);
				 int equestM = tempM.indexOf("=");
				 m = m.substring(equestM);
				 return filePath + "/" + m;  
			 } 
		 }*/
		 
		 int urlIndex = url.lastIndexOf("?");
		 if(urlIndex != -1){
			 String lastUrl = url.substring(urlIndex+1);
			 if(lastUrl.indexOf(".") != -1){
				 return filePath + "/" + lastUrl;  
			 }
		 }else{
			 int urlIndex0 = url.lastIndexOf("/");
			 if(urlIndex0 != -1){
				 String lastfilePath = url.substring(urlIndex0+1);
				 return filePath + "/" + lastfilePath;  
			 }
			 
		 }
		 
		 return filePath + "/untitle";  // untitle 
	 }
	 
	 /**
	  *  
	  * @param sessionId
	  * @return
	  */
	 protected String getFilePath(int sessionId){
		 return filePathMap.get(sessionId);
	 }
	 
	 /**
	  * 获取文件大小
	  * @param sessionId
	  * @return
	  */
	 protected long getFileSize(int sessionId){
		 return fileSizeMap.get(sessionId);
	 }
	 
	
	
	/**
	 *  
	 * @param url 
	 * @param filePath  
	 * @param overWrite  
	 * @param httpProperty http 
	 * @return
	 */
	protected int netGetFile(final String url,final String filePath,final boolean overWrite,final HashMap<String,Object> httpProperty,final MessageHandler mHandler,final Context ctx){
		final int sessionId = createSession();
		Thread thread = new Thread(new Runnable() {
            public void run() {  
            	RandomAccessFile oSavedFile = null;  
				HttpURLConnection conn = null;
				
				try { 
					getFile(url, filePath, overWrite, httpProperty, mHandler, ctx, sessionId, oSavedFile, conn);
				} catch (ProtocolException e) {
					// TODO Auto-generated catch block
//					LogDebug.e("CNetHttpTransfer.netGetFile", e.getMessage(),ctx); 
					showException(sessionId, e,Constant.PARM_ERROR,mHandler); 
				} catch (SocketTimeoutException e) {
					// TODO Auto-generated catch block
//					LogDebug.e("CNetHttpTransfer.netGetFile", e.getMessage(),ctx); 
					showException(sessionId, e,Constant.ERROR,mHandler);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					showException(sessionId, e,Constant.ERROR,mHandler);  
				} catch (Exception e) {
					// TODO Auto-generated catch block
					showException(sessionId, e,Constant.INTERNAL_ERROR,mHandler);   
				} finally{
					try {
						if(oSavedFile != null){
							oSavedFile.close();
						} 
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					closeConn(conn, sessionId, null); 
				}
        }});
		
		thread.start();
		threadMap.put(sessionId, thread);
		return sessionId;
	}
	
	
	private void getFile(String url,String filePath,boolean overWrite,HashMap<String,Object> httpProperty,MessageHandler mHandler,
			Context ctx,int sessionId,RandomAccessFile oSavedFile, HttpURLConnection conn) throws ProtocolException, IOException{
		getFile(url,filePath,overWrite,httpProperty,mHandler,ctx,sessionId,oSavedFile, conn,true,null,false); 
	}
	private void getFile(String url,String filePath,boolean overWrite,HashMap<String,Object> httpProperty,MessageHandler mHandler,
			Context ctx,int sessionId,RandomAccessFile oSavedFile, HttpURLConnection conn,
			boolean isRecon,String sHost,boolean isTouchChangeEvent) throws ProtocolException, IOException{
		long readCount = 0;
		String sTmpUrl = url;
		conn = initConnection(ctx,sTmpUrl,httpProperty,sessionId,mHandler); 
		//当连接异常时，替换为Ip重新获取
		if (conn == null) {
			return;
		}
		
		conn.setInstanceFollowRedirects(false);//不自动重定向
		//-------------------------------begin-------------------------
		String path = filePath;
		String filePaths = reDealPath(sTmpUrl,path);
//		bufferData.set
		readCount = initGetFile(filePaths, overWrite, sessionId,"CNetHttpTransfer.netGetFile",mHandler);
		if(readCount == -1){
			return;
		}
		
		oSavedFile = initRandomAccessFile(filePaths, sessionId,"CNetHttpTransfer.netGetFile",mHandler);
		if(oSavedFile == null){
			return;
		} 
		//-------------------------------end----------------------- 
		
		conn.setRequestMethod("GET"); 
		
		if(readCount > 0){ 
			readCount--;
			conn.setRequestProperty("RANGE", "bytes=" + readCount + "-"); 
		} 
		sessionMap.put(sessionId, conn);
		Map<String, List<String>> preProperties =  conn.getRequestProperties();  
		
		int resCode = addSessionId(sessionId,conn,"CNetHttpTransfer.netGetFile",mHandler,isRecon); 
		if(resCode!= 200){
			if(isRecon){
				if (refreshCoidStruct == null)
					refreshCoidStruct = new BaseCommonStruct();
				urlContent.setsOldUrl(sTmpUrl);
				refreshCoidStruct.obj1 = urlContent;
				CommonInterfaceManager.INSTANCE.CommonInterfaceID(CommonConst.MAIN_MODELID, CommonConst.MAIN_CHANGE_URL_10003, refreshCoidStruct);
				sTmpUrl = urlContent.getsNewUrl();
				String sTmpHost = null;
				try {
					sTmpHost = (new URL(url)).getHost();
				} catch (Exception e) {
					sTmpHost = null;
				}
				getFile(sTmpUrl,filePath,overWrite,httpProperty,mHandler,ctx,sessionId,oSavedFile, conn,false,sTmpHost,true);
				return;
			}else if(isTouchChangeEvent){
				// 发送事件
				if (refreshCoidStruct == null)refreshCoidStruct = new BaseCommonStruct();
				urlContent.setsOldUrl(url);
				urlContent.setsHost(sHost);
				refreshCoidStruct.obj1 = urlContent;
				CommonInterfaceManager.INSTANCE.CommonInterfaceID(CommonConst.MAIN_MODELID, CommonConst.MAIN_SEND_UMENG_EVENT_10004, refreshCoidStruct);
				return;
			}
		}else{
			conn = reDirectUrl(ctx,sTmpUrl,conn,resCode,sessionId,mHandler,"GET",preProperties);
			cacheHeaderFields(sessionId, conn.getHeaderFields());
			writeFile(filePaths,oSavedFile,conn,readCount,sessionId, "CNetHttpTransfer.netGetFile",mHandler); 
		}
	}
	
	/**
	 *  
	 * @param url
	 * @param bufferData   
	 * @param httpProperty http 
	 * @return
	 */
	protected int netGetData(final String url,final BufferData bufferData,final HashMap<String,Object> httpProperty,final MessageHandler mHandler,final Context ctx){
		return netGetData(url, bufferData,httpProperty,mHandler,ctx,true,null,false,0);
	}
	protected int netGetData(final String url,final BufferData bufferData,final HashMap<String,Object> httpProperty,final MessageHandler mHandler,
			final Context ctx,final boolean isRecon,final String sHost,final boolean isTouchChangeEvent,final int iSessionId){
		int sid = 0;
		if(iSessionId > 0)
			sid = iSessionId;	
		else	
			sid = createSession();	
		final int sessionId = sid;	
		Thread thread = new Thread(new Runnable() {
			public void run() {
				HttpURLConnection conn = null;
				String sTmpUrl = url;
				try {  
					conn = initConnection(ctx,sTmpUrl,httpProperty,sessionId,mHandler); 
					if (conn == null) {
						return;
					}
					
					conn.setInstanceFollowRedirects(false);//不自动重定向
					conn.setRequestMethod("GET");  
					sessionMap.put(sessionId, conn);
					Map<String, List<String>> preProperties =  conn.getRequestProperties(); 
					int resCode = addSessionId(sessionId,conn,"CNetHttpTransfer.netGetData",mHandler,isRecon); 

					if(resCode!= 200){
						if(isRecon){
							if (refreshCoidStruct == null)
								refreshCoidStruct = new BaseCommonStruct();
							urlContent.setsOldUrl(sTmpUrl);
							refreshCoidStruct.obj1 = urlContent;
							CommonInterfaceManager.INSTANCE.CommonInterfaceID(CommonConst.MAIN_MODELID, CommonConst.MAIN_CHANGE_URL_10003, refreshCoidStruct);
							sTmpUrl = urlContent.getsNewUrl();
							String sTmpHost = null;
							try {
								sTmpHost = (new URL(url)).getHost();
							} catch (Exception e) {
								sTmpHost = null;
							}
							netGetData(sTmpUrl, bufferData,httpProperty,mHandler,ctx,false,sTmpHost,true,sessionId);
							return;
						}else if(isTouchChangeEvent){
							// 发送事件
							if (refreshCoidStruct == null)refreshCoidStruct = new BaseCommonStruct();
							urlContent.setsOldUrl(url);
							urlContent.setsHost(sHost);
							refreshCoidStruct.obj1 = urlContent;
							CommonInterfaceManager.INSTANCE.CommonInterfaceID(CommonConst.MAIN_MODELID, CommonConst.MAIN_SEND_UMENG_EVENT_10004, refreshCoidStruct);
						}
					}else{
//						sendMessage(-1, sessionId, -999, new Exception(" the http code is " + resCode), 0,mHandler);
						conn = reDirectUrl(ctx,sTmpUrl,conn,resCode,sessionId,mHandler,"GET",preProperties);
						long fileSize = conn.getContentLength(); 
						cacheHeaderFields(sessionId, conn.getHeaderFields());
						writeBufferData(resCode, conn, sessionId, bufferData,fileSize, "CNetHttpTransfer.netGetData",mHandler);
					}
				} catch (ProtocolException e) {
					// TODO Auto-generated catch block 
					showException(sessionId, e,Constant.PARM_ERROR,mHandler);   
				} catch (IOException e) {
					// TODO Auto-generated catch block
					showException(sessionId, e,Constant.ERROR,mHandler);    
				} catch (OutOfMemoryError e) {
					// TODO Auto-generated catch block 
					e.printStackTrace();
//					LogDebug.e("CNetHttpTransfer.netGetData", e.getMessage(),ctx);
					sendMessage(MessageHandler.PROCESS_ERROR, sessionId, Constant.MEMORY_ERROR, e, 0,mHandler);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					showException(sessionId, e,Constant.INTERNAL_ERROR,mHandler);     
				} finally{  
					closeConn(conn, sessionId, null); 
				}
			}});
		
		thread.start();
		threadMap.put(sessionId, thread);
		
		return sessionId;
	}
	 
	
	protected int netPostFileGetData(final String url,final String filePath,final BufferData bufferData,final HashMap<String,Object> httpProperty,final MessageHandler mHandler,final Context ctx){
		return netPostFileGetData(url,filePath,bufferData,httpProperty,mHandler,ctx,true,null,false,0);
	}
	protected int netPostFileGetData(final String url,final String filePath,final BufferData bufferData,final HashMap<String,Object> httpProperty,final MessageHandler mHandler,final Context ctx,
			final boolean isRecon,final String sHost,final boolean isTouchChangeEvent,final int iSessionId){
		int sid = 0;
		if(iSessionId > 0)
			sid = iSessionId;	
		else	
			sid = createSession();	
		final int sessionId = sid;	
		Thread thread = new Thread(new Runnable() {
			public void run() {  
				FileInputStream fileInputStream = null; 
				String sTmpUrl = url;
				long fileSize = 0;
				try {
					File file = new File(filePath);
					fileInputStream = new FileInputStream(file);
					fileSize = Long.parseLong(String.valueOf(file.length()));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
//					LogDebug.e("CNetHttpTransfer.netPostFile", Constant.ABNORMAL + e.getMessage(),ctx);
					sendMessage(MessageHandler.PROCESS_ERROR, sessionId, Constant.FILE_ERROR, e, 0,mHandler);
					return;
				} 
				
				HttpURLConnection conn = null;  
				try {
					conn = initConnection(ctx,sTmpUrl,httpProperty,sessionId,mHandler);
					if (conn == null) {
						return;
					}
					
					conn.setRequestMethod("POST"); 
					conn.setRequestProperty("CONTENT-LENGTH", String.valueOf(fileSize));  
					conn.setUseCaches(false);
					conn.setDoOutput(true);
					sessionMap.put(sessionId, conn);
					conn.setChunkedStreamingMode(1024);
					
					OutputStream outputStream = new BufferedOutputStream(conn.getOutputStream());
					
					postFileGetData(outputStream, fileInputStream, sessionId, fileSize, "CNetHttpTransfer.netPostFile",true,mHandler);
					
					int resCode = conn.getResponseCode(); 
					if(resCode!= 200){
						if(isRecon){
							if (refreshCoidStruct == null)
								refreshCoidStruct = new BaseCommonStruct();
							urlContent.setsOldUrl(sTmpUrl);
							refreshCoidStruct.obj1 = urlContent;
							CommonInterfaceManager.INSTANCE.CommonInterfaceID(CommonConst.MAIN_MODELID, CommonConst.MAIN_CHANGE_URL_10003, refreshCoidStruct);
							sTmpUrl = urlContent.getsNewUrl();
							String sTmpHost = null;
							try {
								sTmpHost = (new URL(url)).getHost();
							} catch (Exception e) {
								sTmpHost = null;
							}
							netPostFileGetData(sTmpUrl,filePath,bufferData,httpProperty,mHandler,ctx,false,sTmpHost,true,sessionId);
							return;
						}else if(isTouchChangeEvent){
							// 发送事件
							if (refreshCoidStruct == null)refreshCoidStruct = new BaseCommonStruct();
							urlContent.setsOldUrl(url);
							urlContent.setsHost(sHost);
							refreshCoidStruct.obj1 = urlContent;
							CommonInterfaceManager.INSTANCE.CommonInterfaceID(CommonConst.MAIN_MODELID, CommonConst.MAIN_SEND_UMENG_EVENT_10004, refreshCoidStruct);
						}
					} else{
						cacheHeaderFields(sessionId, conn.getHeaderFields()); 
						sendMessage(MessageHandler.HTTP_STATUS, sessionId, resCode, null, 0,mHandler);
//			        	LogDebug.d("CNetHttpTransfer.netPostGetData", Constant.NET_STATUS+resCode,ctx);
						long dataSize = -1;//下载数据不提供进度通知
						writeBufferData(resCode,conn, sessionId, bufferData,dataSize,"CNetHttpTransfer.netPostFileGetData",mHandler);
//			        	LogDebug.d("CNetHttpTransfer.netPostFile", Constant.NET_STATUS+resCode,ctx); 
//		        		sendMessage(MessageHandler.HTTP_STATUS, sessionId, resCode, null, 0,mHandler);  
					}
				} catch (ProtocolException e) {
					// TODO Auto-generated catch block
					showException(sessionId, e,Constant.PARM_ERROR,mHandler);     
				} catch (IOException e) {
					// TODO Auto-generated catch block
					showException(sessionId, e,Constant.ERROR,mHandler); 
				} catch (Exception e) {
					// TODO Auto-generated catch block
					showException(sessionId, e,Constant.INTERNAL_ERROR,mHandler);  
				} finally {
					// 
					try {
						fileInputStream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
					closeConn(conn, sessionId, null); 
				} 
			}}); 
		
		thread.start();
		threadMap.put(sessionId, thread);
		
		return sessionId;
	}
	 
	
	/**
	 *  
	 * @param url
	 * @param filePath 
	 * @param httpProperty http 
	 * @return
	 */
	protected int netPostFile(final String url,final String filePath,final HashMap<String,Object> httpProperty,final MessageHandler mHandler,final Context ctx){
		return netPostFile(url,filePath,httpProperty, mHandler,ctx,true,null,false,0);
	}
	protected int netPostFile(final String url,final String filePath,final HashMap<String,Object> httpProperty,final MessageHandler mHandler,final Context ctx,
			final boolean isRecon,final String sHost,final boolean isTouchChangeEvent,final int iSessionId){
		int sid = 0;
		if(iSessionId > 0)
			sid = iSessionId;	
		else	
			sid = createSession();	
		final int sessionId = sid;	
		Thread thread = new Thread(new Runnable() {
			public void run() {  
				FileInputStream fileInputStream = null; 
				String sTmpUrl = url;
				long fileSize = 0;
				try {
					File file = new File(filePath);
					fileInputStream = new FileInputStream(file);
					fileSize = Long.parseLong(String.valueOf(file.length()));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
//					LogDebug.e("CNetHttpTransfer.netPostFile", Constant.ABNORMAL + e.getMessage(),ctx);
					sendMessage(MessageHandler.PROCESS_ERROR, sessionId, Constant.FILE_ERROR, e, 0,mHandler);
					return;
				} 
				
				HttpURLConnection conn = null;  
				try {
					conn = initConnection(ctx,sTmpUrl,httpProperty,sessionId,mHandler);
					if (conn == null) {
						return;
					}
					
					conn.setRequestMethod("POST"); 
					conn.setRequestProperty("CONTENT-LENGTH", String.valueOf(fileSize));  
					conn.setUseCaches(false);
					conn.setDoOutput(true);
					sessionMap.put(sessionId, conn);
					OutputStream outputStream = new BufferedOutputStream(conn.getOutputStream());
					
					postData(outputStream, fileInputStream, sessionId, fileSize, "CNetHttpTransfer.netPostFile",true,mHandler);
					
					int resCode = conn.getResponseCode();  
					if(resCode!= 200){
						if(isRecon){
							if (refreshCoidStruct == null)
								refreshCoidStruct = new BaseCommonStruct();
							urlContent.setsOldUrl(sTmpUrl);
							refreshCoidStruct.obj1 = urlContent;
							CommonInterfaceManager.INSTANCE.CommonInterfaceID(CommonConst.MAIN_MODELID, CommonConst.MAIN_CHANGE_URL_10003, refreshCoidStruct);
							sTmpUrl = urlContent.getsNewUrl();
							String sTmpHost = null;
							try {
								sTmpHost = (new URL(url)).getHost();
							} catch (Exception e) {
								sTmpHost = null;
							}
							netPostFile(sTmpUrl,filePath,httpProperty, mHandler,ctx,false,sTmpHost,true,sessionId);
							return;
						}else if(isTouchChangeEvent){
							// 发送事件
							if (refreshCoidStruct == null)refreshCoidStruct = new BaseCommonStruct();
							urlContent.setsOldUrl(url);
							urlContent.setsHost(sHost);
							refreshCoidStruct.obj1 = urlContent;
							CommonInterfaceManager.INSTANCE.CommonInterfaceID(CommonConst.MAIN_MODELID, CommonConst.MAIN_SEND_UMENG_EVENT_10004, refreshCoidStruct);
						}
					} else{
						cacheHeaderFields(sessionId, conn.getHeaderFields());
//			        	LogDebug.d("CNetHttpTransfer.netPostFile", Constant.NET_STATUS+resCode,ctx); 
						sendMessage(MessageHandler.HTTP_STATUS, sessionId, resCode, null, 0,mHandler);  
					}
				} catch (ProtocolException e) {
					// TODO Auto-generated catch block
					showException(sessionId, e,Constant.PARM_ERROR,mHandler);     
				} catch (IOException e) {
					// TODO Auto-generated catch block
					showException(sessionId, e,Constant.ERROR,mHandler); 
				} catch (Exception e) {
					// TODO Auto-generated catch block
					showException(sessionId, e,Constant.INTERNAL_ERROR,mHandler);  
				} finally {
					// 
					try {
						fileInputStream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
					closeConn(conn, sessionId, null); 
				} 
			}}); 
		
		thread.start();
		threadMap.put(sessionId, thread);
		
		return sessionId;
	}
	
	/**
	 *  
	 * @param url
	 * @param bytesBuffer  
	 * @param httpProperty http 
	 * @return
	 */
	protected int netPostData(final String url,final byte[] bytesBuffer,final HashMap<String,Object> httpProperty,final MessageHandler mHandler,final Context ctx){
		return netPostData(url,bytesBuffer,httpProperty,mHandler,ctx,true,null,false,0);
	}
	protected int netPostData(final String url,final byte[] bytesBuffer,final HashMap<String,Object> httpProperty,final MessageHandler mHandler,final Context ctx,
			final boolean isRecon,final String sHost,final boolean isTouchChangeEvent,final int iSessionId){
		int sid = 0;
		if(iSessionId > 0)
			sid = iSessionId;	
		else	
			sid = createSession();	
		final int sessionId = sid;	
		Thread thread = new Thread(new Runnable() {
			public void run() {
				if(bytesBuffer == null){
					sendMessage(MessageHandler.PROCESS_ERROR, sessionId, Constant.PARM_ERROR, null, 0,mHandler);
				}else { 
					InputStream inputStream = new ByteArrayInputStream(bytesBuffer);
					HttpURLConnection conn = null;  
					String sTmpUrl = url;
					try {
						long fileSize = bytesBuffer.length;  
						conn = initConnection(ctx,sTmpUrl,httpProperty,sessionId,mHandler);
						if (conn == null) {
							return;
						}
						
						conn.setRequestMethod("POST"); 
						conn.setUseCaches(false);
						conn.setRequestProperty("CONTENT-LENGTH", String.valueOf(fileSize));   
						conn.setDoOutput(true);
						sessionMap.put(sessionId, conn);
						OutputStream outputStream = new BufferedOutputStream(conn.getOutputStream()); 
						
						postData(outputStream, inputStream, sessionId, fileSize, "CNetHttpTransfer.netPostData",true,mHandler);
						
						int resCode = conn.getResponseCode(); 
						if(resCode!= 200){
							if(isRecon){
								if (refreshCoidStruct == null)
									refreshCoidStruct = new BaseCommonStruct();
								urlContent.setsOldUrl(sTmpUrl);
								refreshCoidStruct.obj1 = urlContent;
								CommonInterfaceManager.INSTANCE.CommonInterfaceID(CommonConst.MAIN_MODELID, CommonConst.MAIN_CHANGE_URL_10003, refreshCoidStruct);
								sTmpUrl = urlContent.getsNewUrl();
								String sTmpHost = null;
								try {
									sTmpHost = (new URL(url)).getHost();
								} catch (Exception e) {
									sTmpHost = null;
								}
								netPostData(sTmpUrl,bytesBuffer,httpProperty,mHandler,ctx,false,sTmpHost,true,sessionId);
								return;
							}else if(isTouchChangeEvent){
								// 发送事件
								if (refreshCoidStruct == null)refreshCoidStruct = new BaseCommonStruct();
								urlContent.setsOldUrl(url);
								urlContent.setsHost(sHost);
								refreshCoidStruct.obj1 = urlContent;
								CommonInterfaceManager.INSTANCE.CommonInterfaceID(CommonConst.MAIN_MODELID, CommonConst.MAIN_SEND_UMENG_EVENT_10004, refreshCoidStruct);
							}
						}  else{
							cacheHeaderFields(sessionId, conn.getHeaderFields());
//				        	LogDebug.d("CNetHttpTransfer.netPostData", Constant.NET_STATUS+resCode,ctx);
//				        	if(resCode != HttpStatus.SC_OK){
							sendMessage(MessageHandler.HTTP_STATUS, sessionId, resCode, null, 0,mHandler); 
//				        	}  
						}
					} catch (ProtocolException e) {
						// TODO Auto-generated catch block
						showException(sessionId, e,Constant.PARM_ERROR,mHandler);   
					} catch (IOException e) {
						// TODO Auto-generated catch block
						showException(sessionId, e,Constant.ERROR,mHandler); 
					} catch (OutOfMemoryError e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
//						LogDebug.e("CNetHttpTransfer.netPostData", e.getMessage(),ctx);
						sendMessage(MessageHandler.PROCESS_ERROR, sessionId, Constant.MEMORY_ERROR, e, 0,mHandler);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						showException(sessionId, e,Constant.INTERNAL_ERROR,mHandler);  
					} finally {
						// 
						try {
							inputStream.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} 
						closeConn(conn, sessionId, null); 
					}
				} 
			}});
		
		thread.start();
		threadMap.put(sessionId, thread);
		
		return sessionId;
	}
	
	/**
	 *  
	 * @param url
	 * @param bytesBuffer  
	 * @param filePath  
	 * @param httpPostProperty http 
	 * @return
	 */
	protected int netPostGetFile(final String url, final byte[] bytesBuffer, final String filePath, final HashMap<String,Object> httpPostProperty,final MessageHandler mHandler,final Context ctx){
		return netPostGetFile(url,bytesBuffer,filePath,httpPostProperty,mHandler,ctx,true,null,false,0);
	}
	protected int netPostGetFile(final String url, final byte[] bytesBuffer, final String filePath, final HashMap<String,Object> httpPostProperty,final MessageHandler mHandler,final Context ctx,
			final boolean isRecon,final String sHost,final boolean isTouchChangeEvent,final int iSessionId){
		int sid = 0;
		if(iSessionId > 0)
			sid = iSessionId;	
		else	
			sid = createSession();	
		final int sessionId = sid;	
		Thread thread = new Thread(new Runnable() {
			public void run() { 
//            	if(bytesBuffer != null){  
				HttpURLConnection conn = null;  
				String sTmpUrl = url;
				try { 
					conn = initConnection(ctx,sTmpUrl,httpPostProperty,sessionId,mHandler); 
					if (conn == null) {
						return;
					}
					
					conn.setRequestMethod("POST"); 
					conn.setUseCaches(false); 
					
					if(bytesBuffer != null && bytesBuffer.length > 0){
						InputStream bufferInputStream = new ByteArrayInputStream(bytesBuffer);
						long bufferSize = bytesBuffer.length;  
						conn.setRequestProperty("CONTENT-LENGTH", String.valueOf(bufferSize)); 
						conn.setDoOutput(true); 
						OutputStream outputStream = new BufferedOutputStream(conn.getOutputStream()); 
						postData(outputStream, bufferInputStream, sessionId, bufferSize, "CNetHttpTransfer.netPostGetFile",false,mHandler);
					} 
					sessionMap.put(sessionId, conn);
					int resCode = conn.getResponseCode(); 
					if(resCode!= 200){
						if(isRecon){
							if (refreshCoidStruct == null)
								refreshCoidStruct = new BaseCommonStruct();
							urlContent.setsOldUrl(sTmpUrl);
							refreshCoidStruct.obj1 = urlContent;
							CommonInterfaceManager.INSTANCE.CommonInterfaceID(CommonConst.MAIN_MODELID, CommonConst.MAIN_CHANGE_URL_10003, refreshCoidStruct);
							sTmpUrl = urlContent.getsNewUrl();
							String sTmpHost = null;
							try {
								sTmpHost = (new URL(url)).getHost();
							} catch (Exception e) {
								sTmpHost = null;
							}
							netPostGetFile(sTmpUrl,bytesBuffer,filePath,httpPostProperty,mHandler,ctx,false,sTmpHost,true,sessionId);
							return;
						}else if(isTouchChangeEvent){
							// 发送事件
							if (refreshCoidStruct == null)refreshCoidStruct = new BaseCommonStruct();
							urlContent.setsOldUrl(url);
							urlContent.setsHost(sHost);
							refreshCoidStruct.obj1 = urlContent;
							CommonInterfaceManager.INSTANCE.CommonInterfaceID(CommonConst.MAIN_MODELID, CommonConst.MAIN_SEND_UMENG_EVENT_10004, refreshCoidStruct);
						}
					}  else{
						cacheHeaderFields(sessionId, conn.getHeaderFields());
						sendMessage(MessageHandler.HTTP_STATUS, sessionId, resCode, null, 0,mHandler); 
						
						//-------------------------------begin-------------------------
						String path = filePath;
						String filePaths = reDealPath(sTmpUrl,path); 
						//-------------------------------end-----------------------
						long count = initGetFile(filePaths, true, sessionId,"CNetHttpTransfer.netPostGetFile",mHandler);
						if(count == -1){
							return;
						} 
						RandomAccessFile oSavedFile = initRandomAccessFile(filePaths, sessionId,"CNetHttpTransfer.netPostGetFile",mHandler);
						if(oSavedFile == null){
							return;
						}    
//						long fileSize = conn.getContentLength();
						writeFile(filePaths,oSavedFile, conn,0, sessionId,"CNetHttpTransfer.netPostGetFile",mHandler);
					}
				} catch (ProtocolException e) {
					// TODO Auto-generated catch block
					showException(sessionId, e,Constant.PARM_ERROR,mHandler); 
				} catch (IOException e) {
					// TODO Auto-generated catch block
					showException(sessionId, e,Constant.ERROR,mHandler);  
				} catch (OutOfMemoryError e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
//						LogDebug.e("CNetHttpTransfer.netPostGetFile", e.getMessage(),ctx);
					sendMessage(MessageHandler.PROCESS_ERROR, sessionId, Constant.MEMORY_ERROR, e, 0,mHandler);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					showException(sessionId, e,Constant.INTERNAL_ERROR,mHandler);   
				} finally { 
					closeConn(conn, sessionId, null); 
				}
//            	}
			}});
		
		thread.start();
		threadMap.put(sessionId, thread);
		
		return sessionId;    	
	}
	
	
	/**
	 * 
	 * @param url
	 * @param uploadBytesBuffer  
	 * @param bufferData  
	 * @param httpPostProperty http 
	 * @return
	 */
	protected int netPostGetData(final String url, final byte[] uploadBytesBuffer, final BufferData bufferData,final HashMap<String,Object> httpPostProperty,final MessageHandler mHandler,final Context ctx){
		return netPostGetData(url,uploadBytesBuffer,bufferData,httpPostProperty,mHandler,ctx,true,null,false,0);
	}
	protected int netPostGetData(final String url, final byte[] uploadBytesBuffer, final BufferData bufferData,final HashMap<String,Object> httpPostProperty,final MessageHandler mHandler,final Context ctx,
			final boolean isRecon,final String sHost,final boolean isTouchChangeEvent,final int iSessionId){
		int sid = 0;
		if(iSessionId > 0)
			sid = iSessionId;	
		else	
			sid = createSession();	
		final int sessionId = sid;	
		Thread thread = new Thread(new Runnable() {
			public void run() { 
//            	if(uploadBytesBuffer != null){  
				HttpURLConnection conn = null;  
				String sTmpUrl = url;
				try { 
					conn = initConnection(ctx,sTmpUrl,httpPostProperty,sessionId,mHandler); 
					if (conn == null) {
						return;
					}
					
					conn.setRequestMethod("POST"); 
					conn.setUseCaches(false);  
					
					if(uploadBytesBuffer != null && uploadBytesBuffer.length > 0){ 
						InputStream bufferInputStream = new ByteArrayInputStream(uploadBytesBuffer);  
						long bufferSize = uploadBytesBuffer.length;  
						conn.setRequestProperty("CONTENT-LENGTH", String.valueOf(bufferSize));  
						conn.setDoOutput(true);
						OutputStream outputStream = new BufferedOutputStream(conn.getOutputStream()); 
						postData(outputStream, bufferInputStream, sessionId, bufferSize, "CNetHttpTransfer.netPostGetData",false,mHandler);
					}
					sessionMap.put(sessionId, conn);
					int resCode = conn.getResponseCode(); 
					if(resCode!= 200){
						if(isRecon){
							if (refreshCoidStruct == null)
								refreshCoidStruct = new BaseCommonStruct();
							urlContent.setsOldUrl(sTmpUrl);
							refreshCoidStruct.obj1 = urlContent;
							CommonInterfaceManager.INSTANCE.CommonInterfaceID(CommonConst.MAIN_MODELID, CommonConst.MAIN_CHANGE_URL_10003, refreshCoidStruct);
							sTmpUrl = urlContent.getsNewUrl();
							String sTmpHost = null;
							try {
								sTmpHost = (new URL(url)).getHost();
							} catch (Exception e) {
								sTmpHost = null;
							}
							netPostGetData(sTmpUrl,uploadBytesBuffer,bufferData,httpPostProperty,mHandler,ctx,false,sTmpHost,true,sessionId);
							return;
						}else if(isTouchChangeEvent){
							// 发送事件
							if (refreshCoidStruct == null)refreshCoidStruct = new BaseCommonStruct();
							urlContent.setsOldUrl(url);
							urlContent.setsHost(sHost);
							refreshCoidStruct.obj1 = urlContent;
							CommonInterfaceManager.INSTANCE.CommonInterfaceID(CommonConst.MAIN_MODELID, CommonConst.MAIN_SEND_UMENG_EVENT_10004, refreshCoidStruct);
						}
					} else{
						cacheHeaderFields(sessionId, conn.getHeaderFields());
						sendMessage(MessageHandler.HTTP_STATUS, sessionId, resCode, null, 0,mHandler);
//				        LogDebug.d("CNetHttpTransfer.netPostGetData", Constant.NET_STATUS+resCode,ctx);
						long fileSize = conn.getContentLength();
						writeBufferData(resCode,conn, sessionId, bufferData,fileSize,"CNetHttpTransfer.netPostGetData",mHandler);
					}
				} catch (ProtocolException e) {
					// TODO Auto-generated catch block
					showException(sessionId, e,Constant.PARM_ERROR,mHandler);    
				} catch (SocketException e) {
					// TODO Auto-generated catch block 
//						disableConnectionReuseIfNecessary();
					showException(sessionId, e,Constant.ERROR,mHandler);  
				} catch (IOException e) {
					// TODO Auto-generated catch block
					showException(sessionId, e,Constant.ERROR,mHandler);  
				} catch (OutOfMemoryError e) {
					// TODO Auto-generated catch block 
					e.printStackTrace();
//						LogDebug.e("CNetHttpTransfer.netPostGetData", e.getMessage(),ctx);
					sendMessage(MessageHandler.PROCESS_ERROR, sessionId, Constant.MEMORY_ERROR, e, 0,mHandler);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					showException(sessionId, e,Constant.INTERNAL_ERROR,mHandler);   
				} finally { 
					closeConn(conn, sessionId, null);
					
				}
//            	}
			}});
		
		thread.start();
		threadMap.put(sessionId, thread);
		
		return sessionId;    	
	}
	
	/**
	 * HTTP 
	 * @param url
	 * @param uploadBytesBuffer 
	 * @param bufferData 
	 * @param httpPostProperty http 
	 * @param mHandler 
	 * @param ctx
	 * @return
	 */
	protected int netPutData(final String url, final byte[] uploadBytesBuffer, final BufferData bufferData,final HashMap<String,Object> httpPostProperty,final MessageHandler mHandler,final Context ctx){
		return netPutData(url,uploadBytesBuffer,bufferData,httpPostProperty,mHandler,ctx,true,null,false,0);
	}
	protected int netPutData(final String url, final byte[] uploadBytesBuffer, final BufferData bufferData,final HashMap<String,Object> httpPostProperty,final MessageHandler mHandler,final Context ctx,
			final boolean isRecon,final String sHost,final boolean isTouchChangeEvent,final int iSessionId){
		int sid = 0;
		if(iSessionId > 0)
			sid = iSessionId;	
		else	
			sid = createSession();	
		final int sessionId = sid;	
		Thread thread = new Thread(new Runnable() {
			public void run() { 
//            	if(uploadBytesBuffer != null){  
				HttpURLConnection conn = null;  
				String sTmpUrl = url;
				try { 
					conn = initConnection(ctx,sTmpUrl,httpPostProperty,sessionId,mHandler); 
					//当连接异常时，替换为Ip重新获取
					if (conn == null) {
						return;
					}
					
					conn.setRequestMethod("PUT"); 
					conn.setUseCaches(false); 
					
					if(uploadBytesBuffer != null){
						InputStream bufferInputStream = new ByteArrayInputStream(uploadBytesBuffer); 
						long bufferSize = uploadBytesBuffer.length;  
						conn.setRequestProperty("CONTENT-LENGTH", String.valueOf(bufferSize));  
						conn.setDoOutput(true);
						OutputStream outputStream = new BufferedOutputStream(conn.getOutputStream()); 
						postData(outputStream, bufferInputStream, sessionId, bufferSize, "CNetHttpTransfer.netPutData",false,mHandler);
					} 
					sessionMap.put(sessionId, conn);
					int resCode = conn.getResponseCode(); 
					if(resCode!= 200){
						if(isRecon){
							if (refreshCoidStruct == null)
								refreshCoidStruct = new BaseCommonStruct();
							urlContent.setsOldUrl(sTmpUrl);
							refreshCoidStruct.obj1 = urlContent;
							CommonInterfaceManager.INSTANCE.CommonInterfaceID(CommonConst.MAIN_MODELID, CommonConst.MAIN_CHANGE_URL_10003, refreshCoidStruct);
							sTmpUrl = urlContent.getsNewUrl();
							String sTmpHost = null;
							try {
								sTmpHost = (new URL(url)).getHost();
							} catch (Exception e) {
								sTmpHost = null;
							}
							netPutData(sTmpUrl,uploadBytesBuffer,bufferData,httpPostProperty,mHandler,ctx,false,sTmpHost,true,sessionId);
							return;
						}else if(isTouchChangeEvent){
							// 发送事件
							if (refreshCoidStruct == null)refreshCoidStruct = new BaseCommonStruct();
							urlContent.setsOldUrl(url);
							urlContent.setsHost(sHost);
							refreshCoidStruct.obj1 = urlContent;
							CommonInterfaceManager.INSTANCE.CommonInterfaceID(CommonConst.MAIN_MODELID, CommonConst.MAIN_SEND_UMENG_EVENT_10004, refreshCoidStruct);
						}
					} else{
						cacheHeaderFields(sessionId, conn.getHeaderFields());
						sendMessage(MessageHandler.HTTP_STATUS, sessionId, resCode, null, 0,mHandler);
//				        LogDebug.d("CNetHttpTransfer.netPostGetData", Constant.NET_STATUS+resCode,ctx);
						long fileSize = conn.getContentLength();
						writeBufferData(resCode,conn, sessionId, bufferData,fileSize,"CNetHttpTransfer.netPutData",mHandler);
					}
				} catch (ProtocolException e) {
					// TODO Auto-generated catch block
					showException(sessionId, e,Constant.PARM_ERROR,mHandler); 
				} catch (IOException e) {
					// TODO Auto-generated catch block
					showException(sessionId, e,Constant.ERROR,mHandler); 
				} catch (OutOfMemoryError e) {
					// TODO Auto-generated catch block  
					e.printStackTrace();
//						LogDebug.e("CNetHttpTransfer.netPostGetData", e.getMessage(),ctx);
					sendMessage(MessageHandler.PROCESS_ERROR, sessionId, Constant.MEMORY_ERROR, e, 0,mHandler);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					showException(sessionId, e,Constant.INTERNAL_ERROR,mHandler); 
				} finally { 
					closeConn(conn, sessionId, null);
					
				}
//            	}
			}});
		
		thread.start();
		threadMap.put(sessionId, thread);
		
		return sessionId;    	
	}
	
	
	/**
	 * HTTP 
	 * @param url
	 * @param uploadBytesBuffer 
	 * @param bufferData 
	 * @param httpPostProperty http 
	 * @param mHandler 
	 * @param ctx
	 * @return
	 */
	protected int netDeleteData(final String url, final byte[] uploadBytesBuffer, final BufferData bufferData,final HashMap<String,Object> httpPostProperty,final MessageHandler mHandler,final Context ctx){
		return netDeleteData(url,uploadBytesBuffer,bufferData,httpPostProperty,mHandler,ctx,true,null,false,0);
	}
	protected int netDeleteData(final String url, final byte[] uploadBytesBuffer, final BufferData bufferData,final HashMap<String,Object> httpPostProperty,final MessageHandler mHandler,final Context ctx,
			final boolean isRecon,final String sHost,final boolean isTouchChangeEvent,final int iSessionId){
		int sid = 0;
		if(iSessionId > 0)
			sid = iSessionId;	
		else	
			sid = createSession();	
		final int sessionId = sid;	
		Thread thread = new Thread(new Runnable() {
			public void run() { 
//            	if(uploadBytesBuffer != null){  
				HttpURLConnection conn = null; 
				String sTmpUrl = url;
				try { 
					conn = initConnection(ctx,sTmpUrl,httpPostProperty,sessionId,mHandler); 
					if (conn == null) {
						return;
					}
					
					conn.setRequestMethod("DELETE"); 
					conn.setUseCaches(false); 
					
					if(uploadBytesBuffer != null){ 
						InputStream bufferInputStream = new ByteArrayInputStream(uploadBytesBuffer);  
						long bufferSize = uploadBytesBuffer.length;  
						conn.setRequestProperty("CONTENT-LENGTH", String.valueOf(bufferSize)); 
						conn.setDoOutput(true);
						OutputStream outputStream = new BufferedOutputStream(conn.getOutputStream()); 
						postData(outputStream, bufferInputStream, sessionId, bufferSize, "CNetHttpTransfer.netDeleteData",false,mHandler);
					} 
					sessionMap.put(sessionId, conn);
					int resCode = conn.getResponseCode(); 
					if(resCode!= 200){
						if(isRecon){
							if (refreshCoidStruct == null)
								refreshCoidStruct = new BaseCommonStruct();
							urlContent.setsOldUrl(sTmpUrl);
							refreshCoidStruct.obj1 = urlContent;
							CommonInterfaceManager.INSTANCE.CommonInterfaceID(CommonConst.MAIN_MODELID, CommonConst.MAIN_CHANGE_URL_10003, refreshCoidStruct);
							sTmpUrl = urlContent.getsNewUrl();
							String sTmpHost = null;
							try {
								sTmpHost = (new URL(url)).getHost();
							} catch (Exception e) {
								sTmpHost = null;
							}
							netDeleteData(sTmpUrl,uploadBytesBuffer,bufferData,httpPostProperty,mHandler,ctx,false,sTmpHost,true,sessionId);
							return;
						}else if(isTouchChangeEvent){
							// 发送事件
							if (refreshCoidStruct == null)refreshCoidStruct = new BaseCommonStruct();
							urlContent.setsOldUrl(url);
							urlContent.setsHost(sHost);
							refreshCoidStruct.obj1 = urlContent;
							CommonInterfaceManager.INSTANCE.CommonInterfaceID(CommonConst.MAIN_MODELID, CommonConst.MAIN_SEND_UMENG_EVENT_10004, refreshCoidStruct);
						}
					} else{
						cacheHeaderFields(sessionId, conn.getHeaderFields());
						sendMessage(MessageHandler.HTTP_STATUS, sessionId, resCode, null, 0,mHandler);
//				        LogDebug.d("CNetHttpTransfer.netPostGetData", Constant.NET_STATUS+resCode,ctx);
						long fileSize = conn.getContentLength();
						writeBufferData(resCode,conn, sessionId, bufferData,fileSize,"CNetHttpTransfer.netDeleteData",mHandler);
					}
				} catch (ProtocolException e) {
					// TODO Auto-generated catch block
					showException(sessionId, e,Constant.PARM_ERROR,mHandler);  
				} catch (IOException e) {
					// TODO Auto-generated catch block
					showException(sessionId, e,Constant.ERROR,mHandler); 
				} catch (OutOfMemoryError e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
//						LogDebug.e("CNetHttpTransfer.netPostGetData", e.getMessage(),ctx);
					sendMessage(MessageHandler.PROCESS_ERROR, sessionId, Constant.MEMORY_ERROR, e, 0,mHandler);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					showException(sessionId, e,Constant.INTERNAL_ERROR,mHandler); 
				} finally { 
					closeConn(conn, sessionId, null);
					
				}
//            	}
			}});
		thread.start();
		threadMap.put(sessionId, thread);
		
		return sessionId;    	
	}
	 
	 private void showException(int sessionId,Exception e,int errorCode,MessageHandler mHandler){
		 if(threadMap.containsKey(sessionId)){ 
			e.printStackTrace();
			sendMessage(MessageHandler.PROCESS_ERROR, sessionId, errorCode, e, 0,mHandler);
		 } 
	 }
	
	/**
	 * 
	 * @param sessionId
	 */
	protected void netCancelTransfer(int sessionId,MessageHandler mHandler){
		HttpURLConnection con = sessionMap.get(sessionId);   
		try{
			if(con != null){
				closeConn(con, sessionId,mHandler);//con.disconnect();  
			}
//			sessionMap.remove(sessionId); 
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void closeConn(HttpURLConnection conn,int sessionId,MessageHandler mHandler){
		if(conn == null){
			return;
		}
		synchronized (conn) {
			try {
				Thread thread = threadMap.get(sessionId);
				if(thread != null){
					thread.interrupt();
					threadMap.remove(sessionId);
				}
				if(conn != null){
					conn.disconnect(); 
				} 
				  
				sessionMap.remove(sessionId);
				if(mHandler != null  ){
					sendMessage(MessageHandler.PROCESS_CANCEL, sessionId, Constant.CORRECT, null, 50,mHandler);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace(); 
			}  
		}
		
	}
	
	/*private void disableConnectionReuseIfNecessary() {
		// Work around pre-Froyo bugs in HTTP connection reuse.
		if (Integer.parseInt(Build.VERSION.SDK) < Build.VERSION_CODES.FROYO) {
			System.setProperty("http.keepAlive", "false");

		}
	}*/
	
	 
	 
	private int createSession(){
		int sessionId = (int)(Math.random() * 1000) ; 
		while(sessionMap.containsKey(sessionId) || sessionId == 0){
			sessionId = sessionId + 1; 
		} 
		return sessionId;
	} 
	 
	
	  
    private void sendMessage(int what, int arg1, int arg2, Object obj,long delayTime,MessageHandler mHandler) {
    	if(mHandler != null){
    		Message msg = mHandler.obtainMessage(what, arg1, arg2, obj);
            if(delayTime == 0){
            	delayTime = 10;
            }
            mHandler.sendMessageDelayed(msg, delayTime);
    	} 
    }  
    
    
    private RandomAccessFile initRandomAccessFile(String filePath,int sessionId,String methodTag,MessageHandler mHandler){
		RandomAccessFile oSavedFile = null;
		try {
			oSavedFile = new RandomAccessFile(filePath,"rw"); 
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
//			e1.printStackTrace();
			sendMessage(MessageHandler.PROCESS_ERROR, sessionId, Constant.FILE_ERROR, e1, 0,mHandler);
//			LogDebug.e(methodTag, e1.getMessage(),ctx);
			return null;
		}
		return oSavedFile;
	}
	
	private long initGetFile(String filePath, boolean overWrite, int sessionId,String methodTag,MessageHandler mHandler){
		long readCount = 0; 
		File savefile = new File(filePath);
		if(savefile.exists()){
			if(overWrite){
				savefile.delete();
				try {
					savefile.createNewFile(); 
				} catch (IOException e) {
					// TODO Auto-generated catch block
//					e.printStackTrace();
					sendMessage(MessageHandler.PROCESS_ERROR, sessionId, Constant.FILE_ERROR, e, 0,mHandler); 
					return -1;
				}
			}else{
				readCount = Long.parseLong(String.valueOf(savefile.length()));  
			}
		}else{
			try {
				File file = new File(savefile.getParent());
				if(!file.exists()){
					file.mkdirs();
				}
				savefile.createNewFile(); 
			} catch (IOException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
				sendMessage(MessageHandler.PROCESS_ERROR, sessionId, Constant.FILE_ERROR, e, 0,mHandler); 
				return -1;
			}
		} 
		return readCount;
	}
	
	private boolean checkMemoryAvailable(String filePath, long mSize ,MessageHandler mHandler, int sessionId){
		try{
			StatFs statfs = new StatFs(filePath);
			if(statfs!=null){
				long available = (long)statfs.getBlockSize() * statfs.getAvailableBlocks(); 
				Log.i("netengine","Memory Available: "+available+" = "+available+"B"); 
				if(statfs.getBlockCount() > 0 && available < mSize){ 
					sendMessage(MessageHandler.PROCESS_ERROR, sessionId, Constant.SPACE_INSUFFICIENT, null, 0,mHandler); 
					return false;
				}
			}
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return true;
		}
		

	}
	
	private void writeFile(String filePath,RandomAccessFile oSavedFile,HttpURLConnection conn,long fileSeek,int sessionId,String methodTag,MessageHandler mHandler) throws IOException{
		requestPropertyMap.remove(sessionId);
		int resCode = conn.getResponseCode();
		Log.d("netengine", "the connect code is " + resCode);		
		if(resCode == HttpStatus.SC_OK || resCode == HttpStatus.SC_PARTIAL_CONTENT){
		   long fileSize = conn.getContentLength();
		   if(!checkMemoryAvailable(filePath, fileSize, mHandler, sessionId)){
			   return ;
		   }
		   Log.d("writeFile", "fileSize is" + fileSize);
		   Log.d("writeFile", "fileSeek is" + fileSeek);
		   fileSize = fileSize + fileSeek; 
//		   int rescodes = conn.getResponseCode();
    	   InputStream inputStream = conn.getInputStream(); 
    	   boolean isGZIP = false;
    	   if(inputStream instanceof GZIPInputStream){
    		   isGZIP = true;
    	   }
    	   String encode = conn.getContentEncoding();
    	   boolean updateFlag = true;
    	   int avilable = inputStream.available();
    	   if(encode != null && encode.toLowerCase().indexOf("gzip") != -1 && avilable > 0){
    		   inputStream = new GZIPInputStream(inputStream);
    		   updateFlag = false;
    		   isGZIP = true;
    	   }
    	   if(fileSize<1){
    		   updateFlag = false;
    	   }else{
    		   fileSizeMap.put(sessionId, fileSize);
    	   }
    	   
			if (fileSeek >= 0) { 
				long size = oSavedFile.length();
				if (size > fileSeek) {
					oSavedFile.setLength(fileSeek);
				} 
				oSavedFile.seek(fileSeek);
			}
 
    	    
    	   byte[] buffer = new byte[1024];
    	   int readNum = 0;
    	   int prevPercent = 0; 
    	   long readCount = fileSeek;
    	     
    	   // 
    	   while ((readNum = inputStream.read(buffer)) > 0) {
    		  oSavedFile.write(buffer,0,readNum);
    		  readCount = readCount + readNum; 
    		  if(updateFlag && (mHandler.mOnProcessUpdateListener != null || mHandler.mOnSpeedListener != null)){ 
    			  long i = readCount * percent; 
                  int percent = (int) (i / fileSize);
                  if (percent > prevPercent) { 
                	  if(HttpTransferService.sleepTime > 0){
                		 SystemClock.sleep(HttpTransferService.sleepTime);
                	  }
                      prevPercent = percent;
                      sendMessage(MessageHandler.PROCESS_UPDATE, sessionId, percent, null, 0,mHandler);
                      
                      //*********cgp begin *********************
                      if(mHandler.mOnSpeedListener != null){
                    	  listenerSpeed(readCount, fileSize, sessionId, mHandler);
//                          preCount = readCount;
                      } 
                      
                  } 
    		  } 
    	   } 
    	   filePathMap.put(sessionId, filePath); 
    	   oSavedFile.close();
    	   inputStream.close(); 
//    	   LogDebug.d(methodTag, Constant.WRITE_END,ctx);
    	   Log.d("writeFile", "readCount is" + readCount + " and fileSize is " + fileSize);
    	   if(readCount != fileSize && fileSize > 0 && !isGZIP){
    		   Log.w("writeFile", "file error!");
    		   sendMessage(MessageHandler.PROCESS_ERROR, sessionId, Constant.FILE_ERROR, null, 0,mHandler);
    	   }else{
    		   Log.d("writeFile", "success!");
    		   sendMessage(MessageHandler.PROCESS_COMPLETE, sessionId, Constant.CORRECT, null, 0,mHandler); 
    		   Log.d("writeFile", "had send finish notify");
    	   }
        } else{
        	sendMessage(MessageHandler.PROCESS_ERROR, sessionId, Constant.ERROR, null, 0,mHandler);
        }
	}
	
	private void listenerSpeed(long readCount, long fileSize,int sessionId,MessageHandler mHandler){ 
		Long[] data = new Long[]{readCount,fileSize};
		sendMessage(MessageHandler.PROCESS_SPEED, sessionId, 0, data, 0,mHandler);
	}
	
	private int addSessionId(int sessionId,HttpURLConnection conn,String methodTag,MessageHandler mHandler,boolean isRecon) throws IOException{
//		sessionMap.put(sessionId, conn);  
		conn.connect();  
        int resCode = conn.getResponseCode();
        if(resCode != -1 && !isRecon){
        	sendMessage(MessageHandler.HTTP_STATUS, sessionId, resCode, null, 0,mHandler);
        }
        return resCode;
	}
	
	
	
	
	/**
	 * 当重定向的URL有空格时处理空格并重新连接
	 * @param conn
	 * @param resCode
	 * @return
	 * @throws IOException
	 */
	private HttpURLConnection reDirectUrl(Context ctx,String oldUrl,HttpURLConnection conn,int resCode,int sessionId,MessageHandler mHandler,String method,Map<String, List<String>> preProperties) throws IOException{
		if(resCode == HttpStatus.SC_MOVED_PERMANENTLY || resCode == HttpStatus.SC_MOVED_TEMPORARILY){ 
			conn.disconnect();   
	    	String reLocation = conn.getHeaderField("location"); 
	    	reLocation = getDirectUrl(reLocation,oldUrl);
	    	reLocation = reLocation.replace(" ", "%20"); 
//	    	reLocation = utf8URLEncode(reLocation); 
//	    	URL url = new URL(reLocation);
	    	
	      
	    	HashMap<String,Object> hashmapTemp = requestPropertyMap.get(sessionId);
	    	  
	    	/*for(Map.Entry<String, Object> entry : hashmapTemp.entrySet()){
	        	Object value = entry.getValue();
	        	String key = entry.getKey();
	        	if(value instanceof Boolean){
	        		if(key.equals("DOINPUT")){
	        			conn.setDoInput(Boolean.valueOf(value.toString()));
	        		}else if(key.equals("DOOUTPUT")){
	        			conn.setDoOutput(Boolean.valueOf(value.toString()));
	        		}else if(key.equals("USECACHES")){
	        			conn.setUseCaches(Boolean.valueOf(value.toString()));
	        		}else if(key.equals("ALLOWUSERINTERACTION")){
	        			conn.setAllowUserInteraction(Boolean.valueOf(value.toString()));
	        		}
	        	} 
	    	}  */
	    	conn = initConnection(ctx, reLocation, hashmapTemp, sessionId, mHandler);//(HttpURLConnection) url.openConnection();
	    	for(Map.Entry<String, List<String>> entry : preProperties.entrySet()){
	    		conn.setRequestProperty(entry.getKey(), entry.getValue().get(0)); 
	    	} 
	    	
	    	sendMessage(MessageHandler.PROCESS_REDIRECT, sessionId, 0, conn.getURL().toString(), 0,mHandler);
	    	
	    	conn.setRequestMethod(method);  
	    	conn.connect();   
//	    	int resCodeReDirect = conn.getResponseCode(); 
	    	
//	        sendMessage(MessageHandler.HTTP_STATUS, sessionId, resCodeReDirect, null, 0,mHandler); 
		}
	 
		
		sessionMap.put(sessionId, conn); 
		return conn;
	}
	
	private String getDirectUrl(String url,String oldUrl){
		if(url == null){
			return url;
		}
		if(url.startsWith("http://") || url.startsWith("https://")){
			return url;
		}else{
			if(oldUrl.startsWith("http://") || oldUrl.startsWith("https://")){
				String[] httpStr = oldUrl.split("//"); 
				String[] oldUrls = httpStr[1].split("/");
				return httpStr[0] + "//" + oldUrls[0] + "/" + url;
			} 
		}
		return url;
	}
	
	private void writeBufferData(int resCode,HttpURLConnection conn,int sessionId,BufferData bufferData,long fileSize,String methodTag,MessageHandler mHandler) throws IOException{
		requestPropertyMap.remove(sessionId);
		if(resCode == HttpStatus.SC_OK || resCode == HttpStatus.SC_PARTIAL_CONTENT || resCode == HttpStatus.SC_MOVED_PERMANENTLY || resCode == HttpStatus.SC_MOVED_TEMPORARILY){
        	// 
    	   InputStream inputStream = conn.getInputStream();  
    	   String encode = conn.getContentEncoding();
    	   boolean updateFlag = true; 
    	   int avilable = inputStream.available();
    	   if(encode != null && encode.toLowerCase().indexOf("gzip") != -1 && avilable > 0){
    		   inputStream = new GZIPInputStream(inputStream);
    		   updateFlag = false;
    	   }
//    	   double fileSize = conn.getContentLength();
    	   if(fileSize<1){
    		   updateFlag = false;
    	   } else{
    		   fileSizeMap.put(sessionId, fileSize);
    	   }
    	   ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    	   int read;
    	   long readCount = 0;
    	   int prevPercent = 0;
    	   byte[] buffer = new byte[8192]; 
    	   // 
//    	   LogDebug.d(methodTag, Constant.WRITEING,ctx);
    	   long preTime = System.currentTimeMillis();// 
//    	   long preCount = 0;// 
    	   
    	   while ((read = inputStream.read(buffer)) > 0) { 
    		   byteArrayOutputStream.write(buffer, 0, read);
//    		   byteArrayBuffer.append(buffer, 0, read);
    		   if(updateFlag && (mHandler.mOnProcessUpdateListener != null || mHandler.mOnSpeedListener != null)){
    			   readCount = readCount + read; 
    			   double i = readCount * percent; 
                   int percent = (int) (i / fileSize);
                   if (percent > prevPercent) {
                      // 
                      prevPercent = percent;
                      sendMessage(MessageHandler.PROCESS_UPDATE, sessionId, percent, null, 0,mHandler);
                      
                    //*********cgp begin *********************
                      if(mHandler.mOnSpeedListener != null){
                    	  listenerSpeed(readCount, fileSize, sessionId, mHandler);
//                          preCount = readCount;
                      } 
                      //***********end********************
                   }  
    		   } 
    	   }   
    	   if(byteArrayOutputStream.size()>0){
    		   bufferData.setByteBuffer(byteArrayOutputStream.toByteArray());
    	   } 
    	   byteArrayOutputStream.flush();
    	   byteArrayOutputStream.close();
    	   inputStream.close();
//    	   LogDebug.d(methodTag, Constant.WRITE_END,ctx);
           sendMessage(MessageHandler.PROCESS_COMPLETE, sessionId, Constant.CORRECT, null, 0,mHandler);
        }  else{
        	sendMessage(MessageHandler.HTTP_STATUS, sessionId, resCode, null, 0,mHandler);
        }
	}
	
	private void postData(OutputStream outputStream,InputStream fileInputStream,int sessionId,long fileSize,String methodTag,boolean notify,MessageHandler mHandler) throws IOException{
		requestPropertyMap.remove(sessionId);
		if(outputStream != null){
			byte[] buffer = new byte[1024];
            // 
            int readNum; 
        	int prevPercent = 0;
        	long currNum = 0;
//        	LogDebug.d(methodTag, Constant.UPING,ctx);
        	long preTime = System.currentTimeMillis();// 
      	    long preCount = 0;// 
            while((readNum=fileInputStream.read(buffer))!=-1){
                // 
            	outputStream.write(buffer,0,readNum);
            	if (mHandler == null)
            		continue;
            	if(mHandler.mOnProcessUpdateListener != null || mHandler.mOnSpeedListener != null){
            		currNum = currNum + readNum; 
                	long i = currNum * percent; 
                    int percent = (int) (i / fileSize);
                    if (percent > prevPercent) {
                        // 
                        prevPercent = percent;
                        if(notify){
                        	sendMessage(MessageHandler.PROCESS_UPDATE, sessionId, percent, null, 0, mHandler);
                        }
                        
                        //*********cgp begin  *********************
                        if(mHandler.mOnSpeedListener != null){
                        	 listenerSpeed(preTime, currNum - preCount, sessionId, mHandler);
                             preCount = currNum;
                        }
                       
                        //***********end********************
                    }
            	} 
            } 
            // 
            outputStream.flush();
            outputStream.close();
            fileInputStream.close();
//            LogDebug.d(methodTag, Constant.UP_END,ctx);
            if(notify){
            	sendMessage(MessageHandler.PROCESS_COMPLETE, sessionId, Constant.CORRECT, null, 0, mHandler);
            } 
		}
	}
	
	
	
	private void postFileGetData(OutputStream outputStream,InputStream fileInputStream,int sessionId,long fileSize,String methodTag,boolean notify,MessageHandler mHandler) throws IOException{
		requestPropertyMap.remove(sessionId);
		if(outputStream != null){
			byte[] buffer = new byte[1024];
            // 
            int readNum; 
        	int prevPercent = 0;
        	long currNum = 0;
//        	LogDebug.d(methodTag, Constant.UPING,ctx);
        	long preTime = System.currentTimeMillis();// 
      	    long preCount = 0;// 
            while((readNum=fileInputStream.read(buffer))!=-1){
                // 
            	outputStream.write(buffer,0,readNum);
            	if(mHandler.mOnProcessUpdateListener != null || mHandler.mOnSpeedListener != null){
            		currNum = currNum + readNum; 
                	long i = currNum * percent; 
                    int percent = (int) (i / fileSize);
                    if (percent > prevPercent) {
                        // 
                        prevPercent = percent;
                        if(notify){
                        	sendMessage(MessageHandler.PROCESS_UPDATE, sessionId, percent, null, 0, mHandler);
                        }
                        
                        //*********cgp begin  *********************
                        if(mHandler.mOnSpeedListener != null){
                        	 listenerSpeed(preTime, currNum - preCount, sessionId, mHandler);
                             preCount = currNum;
                        }
                       
                        //***********end********************
                    }
            	} 
            } 
            // 
            outputStream.flush();
            outputStream.close();
            fileInputStream.close(); 
		}
	}
	
	private void cacheHeaderFields(int sessionId, Map<String, List<String>> fields){
		if(isCache){
			headerFields.put(sessionId, fields);
		}
	}
}

