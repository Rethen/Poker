package com.changdupay.net.netengine;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;

/**
 * 网络库对外接口
 * @author Administrator
 *
 */
public class CNetHttpTransfer {
 
	private static CNetHttpTransfer instance = null;
	
	private static HttpTransferService httpTransferService;
	   
    
    public static CNetHttpTransfer getInstance(){ 
    	if(instance == null){
    		instance = new CNetHttpTransfer();
    	}
    	return instance;
    }
     
    
    private CNetHttpTransfer(){ 
    	httpTransferService = new HttpTransferService();
    }
      
    
    /**
     * 获取网络库的版本
     */
    public String getVersion(){
    	return Constant.Version;
    }
    
    /**
     * 设置网络连接时间长
     */
    public void setConnectTimeout(int connectTimeout){
    	ConstantParam.connectTimeout = connectTimeout;
    }
    
    /**
     * 设置读服务端数据超时时间
     * @param readTimeout
     */
    public void setReadTimeout(int readTimeout){
    	ConstantParam.readTimeout = readTimeout;
    }
    
    /**
     * 调用netGetFile时，每个百分点停顿的时间
     * @param sleepTime 毫秒数
     */
    public void setSleepTime(long sleepTime){
    	httpTransferService.setSleepTime(sleepTime);
    }
    
    /**
	  * 根据会话获取已下载的文件路径
	  * @param sessionId
	  * @return
	  */
    public String getFilePath(int sessionId){
		 return httpTransferService.getFilePath(sessionId);
	 }
    
    /**
     * 根据会话获取已下载的文件大小
     * @param sessionId
     * @return
     */
    public long getFileSize(int sessionId){
    	long size = httpTransferService.getFileSize(sessionId);
    	if(size == 0){
    		return -1;
    	}else{
    		return size;
    	}
	 }
    
    /**
     * 根据SessionId获取网络连接
     * @param sessionId
     * @return
     */
    public HttpURLConnection getHttpURLConnectionByMap(int sessionId){
    	return httpTransferService.getHttpURLConnectionByMap(sessionId);
    }
    
    /**
     * 根据SessionId获取响应头信息
     * @param sessionId
     * @return
     */
    public Map<String, List<String>> getResponseHeaderFields(int sessionId){
    	return httpTransferService.getResponseHeaderFields(sessionId);
    }
	
    /**
	 * 下载文件
	 * @param url 
	 * @param filePath 文件保存路径
	 * @param overWrite 是否覆盖文件
	 * @param httpProperty http头参数
	 * @return
	 */
	public int netGetFile(String url, String filePath, boolean overWrite, HashMap<String,Object> httpProperty, MessageHandler mHandler, Context ctx){
		return httpTransferService.netGetFile(url, filePath, overWrite, httpProperty, mHandler, ctx);
	}
	  
	 
	
	/**
	 * 下载数据
	 * @param url
	 * @param bufferData  下载后缓存的数据
	 * @param httpProperty http头参数
	 * @return
	 */
	public int netGetData( String url, BufferData bufferData, HashMap<String,Object> httpProperty, MessageHandler mHandler, Context ctx){
		return httpTransferService.netGetData(url, bufferData, httpProperty, mHandler, ctx);
	}
	 
	 
	
	/**
	 * 上传文件
	 * @param url
	 * @param filePath 待上传的文件路径
	 * @param httpProperty http头参数
	 * @return
	 */
	public int netPostFile( String url, String filePath, HashMap<String,Object> httpProperty, MessageHandler mHandler, Context ctx){
		return httpTransferService.netPostFile(url, filePath, httpProperty, mHandler, ctx);
	}
	 
	
	
	/**
	 * 上传数据
	 * @param url
	 * @param bytesBuffer 待上传的数据
	 * @param httpProperty http头参数
	 * @return
	 */
	public int netPostData( String url, byte[] bytesBuffer, HashMap<String,Object> httpProperty, MessageHandler mHandler, Context ctx){
		return httpTransferService.netPostData(url, bytesBuffer, httpProperty, mHandler, ctx);
	}
	
	 
	
	/**
	 * 上传数据下载文件
	 * @param url
	 * @param bytesBuffer 待上传的数据
	 * @param filePath  下载后所保存的文件路径
	 * @param httpPostProperty http头参数 
	 * @return
	 */
	public int netPostGetFile( String url,  byte[] bytesBuffer,  String filePath,  HashMap<String,Object> httpPostProperty, MessageHandler mHandler, Context ctx){
		return httpTransferService.netPostGetFile(url, bytesBuffer, filePath, httpPostProperty, mHandler, ctx);
	}
	
	
	/**
	 * 上传文件下载数据
	 * @param url
	 * @param filePath 待上传的文件路径
	 * @param bufferData 下载后缓存的数据
	 * @param httpProperty http头参数 
	 * @param mHandler
	 * @param ctx
	 * @return
	 */
	public int netPostFileGetData( String url, String filePath, BufferData bufferData, HashMap<String,Object> httpProperty, MessageHandler mHandler, Context ctx){
		return httpTransferService.netPostFileGetData(url, filePath, bufferData, httpProperty, mHandler, ctx);
	}
	
	 
	
	/**
	 * 上传下载数据
	 * @param url
	 * @param uploadBytesBuffer  待上传的数据
	 * @param bufferData   缓存下载后的数据
	 * @param httpPostProperty http头参数
	 * @return
	 */
	public int netPostGetData( String url,  byte[] uploadBytesBuffer,  BufferData bufferData, HashMap<String,Object> httpPostProperty, MessageHandler mHandler, Context ctx){
		return httpTransferService.netPostGetData(url, uploadBytesBuffer, bufferData, httpPostProperty, mHandler, ctx);
	}
	
	
	 
	/**
	 * HTTP用PUT方法修改数据
	 * @param url
	 * @param uploadBytesBuffer 需要修改的数据
	 * @param bufferData 缓存服务端返回的数据
	 * @param httpPostProperty http头参数
	 * @param mHandler 回调接口
	 * @param ctx
	 * @return
	 */
	public int netPutData( String url,  byte[] uploadBytesBuffer,  BufferData bufferData, HashMap<String,Object> httpPostProperty, MessageHandler mHandler, Context ctx){
		return httpTransferService.netPutData(url, uploadBytesBuffer, bufferData, httpPostProperty, mHandler, ctx);
	}
	
	 
	/**
	 * HTTP用DELETE方法删除数据
	 * @param url
	 * @param uploadBytesBuffer 需要删除的数据参数
	 * @param bufferData 缓存服务端返回的数据
	 * @param httpPostProperty http头参数
	 * @param mHandler 回调接口
	 * @param ctx
	 * @return
	 */
	public int netDeleteData( String url,  byte[] uploadBytesBuffer,  BufferData bufferData, HashMap<String,Object> httpPostProperty, MessageHandler mHandler, Context ctx){
		return httpTransferService.netDeleteData(url, uploadBytesBuffer, bufferData, httpPostProperty, mHandler, ctx);
	}
	
	
	
	/**
	 * 关闭会话
	 * @param sessionId
	 */
	public void netCancelTransfer(int sessionId,MessageHandler mHandler){
		httpTransferService.netCancelTransfer(sessionId, mHandler);
	}
	
	 
	/**
     * 设置是否缓存所有的网络请求头信息
     * @param isCache
     */
    public void setCacheHeaderFields(boolean isCache){
    	httpTransferService.setCacheHeaderFields(isCache);
    }
    
    /**
     * 移除网络请求头信息缓存
     * @param sessionId
     */
    public void remoreCacheHeaderFields(int sessionId){
    	httpTransferService.remoreCacheHeaderFields(sessionId);
    }
    
    /**
     * 清除所有网络请求头信息缓存 
     */
    public void clearCacheHeaderFields(){
    	httpTransferService.clearCacheHeaderFields();
    }
    
    
    /**
     * 设置下载通知粒度
     * @param maxDownPoint
     */
    public void setMaxDownPoint(int maxDownPoint){
    	httpTransferService.setMaxDownPoint(maxDownPoint);
    }
	
}
