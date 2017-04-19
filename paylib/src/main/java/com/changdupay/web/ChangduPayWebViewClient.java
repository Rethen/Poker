package com.changdupay.web;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.changdupay.app.BaseActivity;
import com.changdupay.channel.zhangyue.ZhangYueHelper;
import com.changdupay.protocol.action.ActionManager;
import com.changdupay.protocol.action.IAction;
import com.changdupay.util.MResource;
import com.changdupay.util.ToastHelper;

public class ChangduPayWebViewClient extends WebViewClient{
	private boolean mNeedQuitWhenFinish = false;
	private boolean mHideWaitCursor = false;
	private Activity mActivity;
	private Timer mTimer;
	private int nTimeOut = 30*1000;
	private boolean mIsFirstTime = true;
	
	private int mUnfinishCount = 0;
	protected ZhangYueHelper mZhangYueHelper = null;
	public void setParams(Activity activity, boolean bQuit, boolean bHideWaitCursor)
	{
		mActivity = activity;
		mNeedQuitWhenFinish = bQuit;
		mHideWaitCursor = bHideWaitCursor;
	}
	
	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		// TODO Auto-generated method stub		
		if (mUnfinishCount == 0) {
			mIsFirstTime = false;
		}
		mUnfinishCount = 0;
		
		IAction action = ActionManager.getInstance().getAction(url);
		if (action  != null)
		{
			action.doAction(mActivity, mNeedQuitWhenFinish);
		}
		else
		{
			view.loadUrl(url);
		}       
        return true;  
	}
	
	@Override
	public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
		// TODO Auto-generated method stub
		return super.shouldOverrideKeyEvent(view, event);
	}
	
	@Override
	public void onPageFinished(WebView view, String url) {
		// TODO Auto-generated method stub
		mUnfinishCount = 0;
		super.onPageFinished(view, url);
		if (mActivity instanceof BaseActivity) {
			((BaseActivity) mActivity).hideLoading();
		}
		if (mZhangYueHelper != null)
		{
			mZhangYueHelper.addInputClickListner();
		}
		
        mTimer.cancel();
        mTimer.purge();
	}
	
	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		// TODO Auto-generated method stub
		mUnfinishCount++;
		super.onPageStarted(view, url, favicon);
		if ((!mHideWaitCursor || mIsFirstTime) && mActivity instanceof BaseActivity) {
			((BaseActivity) mActivity).showLoading();
		}
		startTimer(view);
	}
	
	@Override
	public void onReceivedError(WebView view, int errorCode,
			String description, String failingUrl) {
		// TODO Auto-generated method stub
		mUnfinishCount = 0;
		super.onReceivedError(view, errorCode, description, failingUrl);
		if (mActivity instanceof BaseActivity) {
			((BaseActivity) mActivity).hideLoading();
		}
        mTimer.cancel();
        mTimer.purge();
	}
	
	public void setZhangYueHelper(ZhangYueHelper zhangYueHelper)
	{
		mZhangYueHelper = zhangYueHelper;
	}
	
	private void startTimer(final WebView webview) {
		if (mTimer != null) {
			try {
				mTimer.cancel();
                mTimer.purge();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
        mTimer = new Timer();
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                /*
                 * 超时后,首先判断页面加载进度,超时并且进度小于100,就执行超时后的动作
                 */
                if (webview.getProgress() < 100) {
                	ToastHelper.shortDefaultToast(MResource.getIdByName(mActivity.getApplication(), "string", "ipay_load_timeout"));
                	mHandler.sendEmptyMessage(0);
                    mTimer.cancel();
                    mTimer.purge();
                }
            }
        };
        mTimer.schedule(tt, nTimeOut, 1);
	}

	private Handler mHandler = new Handler(new Callback() {
		
		@Override
		public boolean handleMessage(Message arg0) {
			// TODO Auto-generated method stub
			switch (arg0.what) {
			case 0:
        		if (mActivity instanceof BaseActivity) {
        			((BaseActivity) mActivity).hideLoading();
        		}
				break;
			default:
				break;
			}
			return false;
		}
	});
}
