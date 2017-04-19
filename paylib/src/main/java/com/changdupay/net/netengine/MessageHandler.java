package com.changdupay.net.netengine;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * 消息传递
 * @author Administrator
 *
 */
public class MessageHandler extends Handler {
	
	public MessageHandler(Looper looper){
	    super(looper);
	}
	
	public MessageHandler(Looper looper, Callback callback) {
		 super(looper,callback);
    }
	
	public MessageHandler() {
		 super();
   }

	/**
	 * 建立网络连接
	 */
	protected static final int BUILD_CONNECT = 0;
	
	/**
	 * HTTP请求状态
	 */
	protected static final int HTTP_STATUS = 1;
	
	/**
	 * 数据更新
	 */
	protected static final int PROCESS_UPDATE = 2;
	
	/**
	 * 下载完成
	 */
	protected static final int PROCESS_COMPLETE = 3;
	
	/**
	 * 速率
	 */
	protected static final int PROCESS_SPEED = 4;
	
	/**
	 * 错误
	 */
	protected static final int PROCESS_ERROR = -1;
	
	/**
	 * 取消
	 */
	protected static final int PROCESS_CANCEL = -2;
	
	
	/**
	 * 重定向
	 */
	protected static final int PROCESS_REDIRECT = 5;
   

	/**
	 * 处理接收到的消息
	 */
	@Override
	public void handleMessage(Message msg) { 
		switch (msg.what) {
		case BUILD_CONNECT: 
			if (mOnBuildConnectListener != null){
				mOnBuildConnectListener.onBuildConnect(msg.arg1,msg.arg2);//msg.arg1:sessionId
			}
			break;
		case HTTP_STATUS: 
			if (mOnHTTPStatusListener != null){
				mOnHTTPStatusListener.onHTTPStatus(msg.arg1, msg.arg2);//msg.arg1:sessionId, msg.arg2:statusCode
			}
			break;
		case PROCESS_UPDATE:
			if (mOnProcessUpdateListener != null && CNetHttpTransfer.getInstance().getHttpURLConnectionByMap(msg.arg1) != null){
				mOnProcessUpdateListener.onProcessUpdate(msg.arg1, msg.arg2);//msg.arg1:sessionId, msg.arg2:percent(%)
			}	
			break;
		case PROCESS_COMPLETE:
			if (mOnProcessCompleteListener != null){
				mOnProcessCompleteListener.onProcessComplete(msg.arg1,msg.arg2);//msg.arg1:sessionId
			}	
			break;
		case PROCESS_ERROR:
			if (mOnProcessErrorListener != null){
				mOnProcessErrorListener.onProcessError(msg.arg1, msg.arg2, (Exception) msg.obj);//msg.arg1:sessionId,msg.arg2:errorCode,(Exception) msg.obj:Exception
			}	
			break;
		case PROCESS_CANCEL:
			if (mOnProcessCancelListener != null){
				mOnProcessCancelListener.onProcessCancel(msg.arg1);//msg.arg1:sessionId
			}	
			break;	
		case PROCESS_SPEED:
			if (mOnSpeedListener != null && CNetHttpTransfer.getInstance().getHttpURLConnectionByMap(msg.arg1) != null){
				Long[] data = (Long[]) msg.obj;	 
				mOnSpeedListener.onSpeedListener(msg.arg1, data[0].longValue(),data[1].longValue());////msg.arg1:sessionId, msg.obj:speed(byte/s)
			}
			break;
		case PROCESS_REDIRECT:
			if (mOnReDirectListener != null){
				String reDirectUrl = (String) msg.obj;	 
				mOnReDirectListener.onReDirectListener(msg.arg1, reDirectUrl);////msg.arg1:sessionId, msg.obj:speed(byte/s)
			}
			break;	
			
		default:
			break;
		}
	}
	
	// 定义连接事件
	private OnBuildConnectListener mOnBuildConnectListener;

	/**
	 * 监听网络连接
	 * @author Administrator
	 *
	 */
	public interface OnBuildConnectListener {
		/**
		 * 侦听结果回调方法
		 * @param sessionId  会话ID
		 * @param errorCode  0：可连接网络，-1:无连接网络
		 *  
		 */
		void onBuildConnect(int sessionId, int errorCode);
	}

	/**
	 * 设置网络连接侦听
	 * @param listener
	 */
	public void setOnBuildConnectListener(OnBuildConnectListener listener) {
		mOnBuildConnectListener = listener;
	}

	// 定义连接事件
	private OnHTTPStatusListener mOnHTTPStatusListener;

	/**
	 * 监听HTTP请求状态
	 * @author Administrator
	 *
	 */
	public interface OnHTTPStatusListener {
		/**
		 * 侦听结果回调方法
		 * @param sessionId 会话ID
		 * @param statusCode HTTP请求状态
		 *  
		 */
		void onHTTPStatus(int sessionId, int statusCode);
	}

	/**
	 * 设置HTTP请求状态侦听
	 * @param listener
	 */
	public void setOnHTTPStatusListener(OnHTTPStatusListener listener) {
		mOnHTTPStatusListener = listener;
	}

	// 定义进度更新事件
	public OnProcessUpdateListener mOnProcessUpdateListener;

	/**
	 * 监听数据传输过程
	 * @author Administrator
	 *
	 */
	public interface OnProcessUpdateListener {
		/**
		 * 侦听结果回调方法
		 * @param sessionId 会话ID
		 * @param percent 数据传输所占百分比
		 *  
		 */
		void onProcessUpdate(int sessionId, int percent);
	}

	/**
	 * 设置数据传输过程侦听
	 * @param listener
	 */
	public void setOnProcessUpdateListener(OnProcessUpdateListener listener) {
		mOnProcessUpdateListener = listener;
	}

	// 定义完成事件
	private OnProcessCompleteListener mOnProcessCompleteListener;

	/**
	 * 监听数据传输结束事件
	 * @author Administrator
	 *
	 */
	public interface OnProcessCompleteListener {
		/**
		 * 侦听结果回调方法
		 * @param sessionId 会话ID
		 * @param arg 信息 1:已经存在文件，无需续传; 0 : 正确
		 */
		void onProcessComplete(int sessionId, int arg);
	}

	/**
	 * 设置数据传输完成侦听
	 * @param listener
	 */
	public void setOnProcessCompleteListener(
			OnProcessCompleteListener listener) {
		mOnProcessCompleteListener = listener;
	}

	// 定义异常事件
	private OnProcessErrorListener mOnProcessErrorListener;

	/**
	 * 监听错误事件
	 * @author Administrator
	 *
	 */
	public interface OnProcessErrorListener {
		/**
		 * 侦听结果回调方法
		 * @param sessionId 会话ID
		 * @param errorCode 错误编码 0:正确；-1:错误； -2:参数错误；-3:内存错误；-4:文件错误；-7:空间不足；-100:内部未知错误；
		 * @param e 错误信息
		 */
		void onProcessError(int sessionId, int errorCode, Exception e);
	}

	/**
	 * 设置错误侦听
	 * @param listener
	 */
	public void setOnProcessErrorListener(OnProcessErrorListener listener) {
		mOnProcessErrorListener = listener;
	}
	
	
	// 定义中途停止事件
	private OnProcessCancelListener mOnProcessCancelListener;

	/**
	 * 监听取消事件
	 * @author Administrator
	 *
	 */
	public interface OnProcessCancelListener {
		/**
		 * 侦听结果回调方法
		 * @param sessionId 会话ID
		 *  
		 */
		void onProcessCancel(int sessionId);
	}

	/**
	 * 设置取消传输侦听
	 * @param listener
	 */
	public void setOnProcessCancelListener(OnProcessCancelListener listener) {
		mOnProcessCancelListener = listener;
	}
	
	
	//上传，下载速率侦听
	public OnSpeedListener mOnSpeedListener;
	
	/**
	 * 上传，下载速率侦听
	 * @author Administrator
	 */
	public interface OnSpeedListener {
		
		/**
		 * 上传，下载速率侦听回调
		 * @param sessionId  会话ID
		 * @param speed 速率
		 */
		void onSpeedListener(int sessionId, long downCount, long fileSize);
	}
	
	/**
	 * 设置速率侦听
	 * @param mOnSpeedListener
	 */
	public void setOnSpeedListener(OnSpeedListener mOnSpeedListener){
		this.mOnSpeedListener = mOnSpeedListener;
	}
	
	
	
	/**
	 * 重定向侦听
	 */
	public OnReDirectListener mOnReDirectListener;
	
	/**
	 * 重定向侦听
	 * @author Administrator
	 */
	public interface OnReDirectListener {
		
		/**
		 * 重定向侦听
		 * @param sessionId  会话ID
		 * @param speed 速率
		 */
		void onReDirectListener(int sessionId, String reDirectUrl);
	}
	
	/**
	 * 重定向侦听
	 * @param mOnReDirectListener
	 */
	public void setOnReDirectListener(OnReDirectListener mOnReDirectListener){
		this.mOnReDirectListener = mOnReDirectListener;
	}
}
