package com.changdupay.util;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;

import com.changdupay.net.netengine.BufferData;
import com.changdupay.net.netengine.CNetHttpTransfer;
import com.changdupay.protocol.base.PayConst;
import com.changdupay.protocol.base.PostStruct;
import com.changdupay.protocol.base.RequestHandlerManager;
import com.changdupay.protocol.base.ResponseHandler.OnDownloadIconListener;
import com.changdupay.protocol.base.SessionManager;

public class ImageIconDownLoadManager {
	private static ImageIconDownLoadManager instance = null;
	private ArrayList<ImageViewWrapper> mWrapperList = null;
	private ImageViewWrapper mUpdateImageViewWrapper = null;
	private Bitmap mNetBitmap = null;
	
	private ImageIconDownLoadManager()
	{
		mWrapperList = new ArrayList<ImageViewWrapper>();
//		LoginResponseManager.getInstance().getResponseHandler().setOnDownloadIconListener(mDownloadIconListener);
	}
	
    public static ImageIconDownLoadManager getInstance()
    { 
    	if(instance == null){
    		instance = new ImageIconDownLoadManager();
    	}
    	return instance;
    }
    
    public void pushToDownloadQueue(ImageView target, String iconUrl, ICallback callBack, int nTag)
    {
    	if (target == null || iconUrl == null)
    	{
    		return;
    	}
    	ImageViewWrapper wrapper = new ImageViewWrapper(target, iconUrl, callBack, nTag);
    	mWrapperList.add(wrapper);
    	startDownload(wrapper);
    }
    
    private int startDownload(ImageViewWrapper wrapper)
    {
    	BufferData bufferData = new BufferData();
    	PostStruct ps = new PostStruct(PayConst.PREFERENTIAL_ICON_ACTION, bufferData);	
    	ps.mTag = wrapper;
    	int sessionid = CNetHttpTransfer.getInstance().netGetData(wrapper.mIconHref, bufferData, null, RequestHandlerManager.getInstance().getMessageHandler(), ContextUtil.getContext());
    	SessionManager.getInstance().addSession(sessionid, ps);		
		return sessionid;
    }
    
    private OnDownloadIconListener mDownloadIconListener = new OnDownloadIconListener()
    {

		@Override
		public void OnDownloadIcon(Object icon) {
			// TODO Auto-generated method stub
			if (icon == null)
				return;
			if (icon instanceof PostStruct)
			{
				PostStruct ps = (PostStruct)icon;
				byte[] data = ((BufferData)ps.mData).getByteBuffer();
				if (data != null)
				{
					Bitmap bitmap = BitmapFactory.decodeByteArray(((BufferData)ps.mData).getByteBuffer(), 0, data.length);
					ImageViewWrapper wrapper = (ImageViewWrapper)ps.mTag;
					mUpdateImageViewWrapper = wrapper;
					mNetBitmap = bitmap;
					getInnerHandler().post(updateIcon);
				}
			}
		}
    	
    };
    
	private static final class InnerHandler extends Handler
	{
		public InnerHandler(Looper looper)
		{
			super(looper);
		}
	}
	
	private static InnerHandler handler = null;
    private static InnerHandler getInnerHandler(){
		if(handler==null){
			synchronized(InnerHandler.class){
				if(handler==null){
					handler=new InnerHandler(Looper.getMainLooper());
				}
			}
		}
		
		return handler;
	}
    
    private Runnable updateIcon = new Runnable()
    {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (mUpdateImageViewWrapper != null && mNetBitmap != null)
			{
				if (mUpdateImageViewWrapper.mArrowView != null)
				{
					mUpdateImageViewWrapper.mArrowView.setVisibility(View.VISIBLE);
					mUpdateImageViewWrapper.mArrowView.setImageBitmap(mNetBitmap);
				}
				if (mUpdateImageViewWrapper.mCallback != null)
				{
					mUpdateImageViewWrapper.mCallback.onFinish(mNetBitmap, mUpdateImageViewWrapper.mTag);
				}
				mUpdateImageViewWrapper = null;
				mNetBitmap = null;
			}
		}
    	
    };
    
    public class ImageViewWrapper
	{
		ImageView mArrowView = null;
		String mIconHref = "";
		ICallback mCallback = null;
		int mTag = 0;
		public ImageViewWrapper(ImageView view, String url, ICallback callBack, int nTag)
		{
			mArrowView = view;
			mIconHref = url;
			mCallback = callBack;
			mTag = nTag;
		}
	}
}
