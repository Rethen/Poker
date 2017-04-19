package com.changdupay.app;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.changdupay.channel.zhangyue.ZhangYueHelper;
import com.changdupay.util.Const;
import com.changdupay.util.MResource;
import com.changdupay.web.ChangduPayWebViewClient;

public class WebViewActivity extends BaseActivity {
	protected WebView mWebView;
	protected WebViewClient mWebViewClient;
	protected String mCurrentUrl;
	protected String mTitle;
	protected byte[] mPostData;
	protected boolean mCanGoBack = true;
	protected boolean mHideWaitCursor = false;
	protected ZhangYueHelper mZhangYueHelper = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(MResource.getIdByName(getApplication(), "layout", "ipay_webview_page"));
		
		mTitle = getIntent().getStringExtra(Const.ParamType.TypeTitle);
		if (mTitle == null || mTitle.length() == 0) {
			mTitle = getString(MResource.getIdByName(getApplication(), "string", "ipay_appname_title"));
		}
		
		initView();
		initData();

		navigate();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume(); 
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy(); 
		BaseActivity.hideWaitCursor();
		if (mZhangYueHelper != null)
		{
			mZhangYueHelper.unRegisterReceiver();
		}
	}
	
	private void initView() {
		mWebView = (WebView)findViewById(MResource.getIdByName(getApplication(), "id", "webview"));
		
		initWebView();
		setTitle(mTitle);
		showBackBtn();
	}

    private void initWebView() {
    	if (mWebView != null) {
    		mWebViewClient = new ChangduPayWebViewClient();
			mWebView.setWebViewClient(mWebViewClient);

	        mWebView.getSettings().setJavaScriptEnabled(true);
		}
    	
    	if (TextUtils.equals(mTitle, getString(MResource.getIdByName(getApplication(), "string", "ipay_unicom_quick"))) && mZhangYueHelper == null)
    	{
    		mZhangYueHelper = new ZhangYueHelper(this.getApplication(), this, mWebView);
    		((ChangduPayWebViewClient)mWebViewClient).setZhangYueHelper(mZhangYueHelper);
    	}
    }
    
    protected void initData() {
    	mCurrentUrl = getIntent().getStringExtra(Const.ParamType.TypeUrl);
    	mPostData = getIntent().getByteArrayExtra(Const.ParamType.TypePostData);
    	mCanGoBack = getIntent().getBooleanExtra(Const.ParamType.TypeCanGoBack, true);
    	mHideWaitCursor = getIntent().getBooleanExtra(Const.ParamType.TypeHideWaitCursor, false);
		
		if (mWebViewClient != null)
		{
			((ChangduPayWebViewClient)mWebViewClient).setParams(this, mNeedQuitWhenFinish, mHideWaitCursor);
		}
    }
    
    protected void navigate() {
		if (mCurrentUrl != null && mCurrentUrl.length() > 0) {
			if (mPostData != null) {
				mWebView.postUrl(mCurrentUrl, mPostData);
			}
			else {
		        mWebView.loadUrl(mCurrentUrl); 
			}
		}
	}
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	// TODO Auto-generated method stub
        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
        	if (mCanGoBack && mWebView != null && mWebView.canGoBack()) {
        		mWebView.goBack();
    			return true;
			}  
        }
    	return super.onKeyDown(keyCode, event);
    }

    private void gotoWaitActivity()
	{
		Intent intent = new Intent(this, PayResultActivity.class);
		intent.putExtra(Const.ParamType.TypeNeedQuitOrNot, mNeedQuitWhenFinish);
		startActivityForResult(intent, 1000);
	}
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		BaseActivity.hideWaitCursor();
		if (data != null) {
			boolean quit = data.getBooleanExtra(Const.ParamType.TypeNeedQuitOrNot, false);
			if (quit) {
				mNeedQuitWhenFinish = quit;
			}
		}
		if (mNeedQuitWhenFinish) {
			if (data == null) {
				data = new Intent();
			}
			data.putExtra(Const.ParamType.TypeNeedQuitOrNot, true);
			setResult(RESULT_OK, data);
			finish();
			return;
		}
		
		if (requestCode == Const.RequestCode.SendSms) {
			finish();
			//gotoWaitActivity();
			return;
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
}
