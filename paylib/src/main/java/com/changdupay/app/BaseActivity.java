package com.changdupay.app;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import com.changdupay.net.netengine.Constant;
import com.changdupay.open.iChangduPay;
import com.changdupay.open.iChangduPay.ResultCode;
import com.changdupay.util.Const;
import com.changdupay.util.ContextUtil;
import com.changdupay.util.FlingExit;
import com.changdupay.util.MResource;
import com.changdupay.util.SkinChangeManager;
import com.changdupay.util.ToastHelper;
import com.changdupay.util.Utils;
import com.changdupay.widget.CustomDialog;

public class BaseActivity extends Activity {
	
	protected TextView mTitleTextView;
	protected boolean mNeedQuitWhenFinish = false;
	protected static boolean mQuitNow = false;
	
	/****右滑手势关闭activity相关参数****/
	protected static boolean isInteruptOperation = false;
	private FlingExit flingExitDetector;
	private boolean isFilingExitEnable = true;
	private Handler mFilingExitHandler = new Handler();
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Utils.setNoTitle(this);
		super.onCreate(savedInstanceState);
		ContextUtil.setContext(this);
		
		flingExitDetector = new FlingExit(this, callBack);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		PayOrderInfoManager.getPayOrderInfoManager().saveInstanceState(outState);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
		PayOrderInfoManager.getPayOrderInfoManager().restoreInstanceState(savedInstanceState);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		ContextUtil.setContext(this);
		SkinChangeManager.getInstance().onChangeSkin(this, false);
	}

	@Override
	protected void onRestart() {
		Log.e("BaseActivity ", "onRestart");
		super.onRestart();
	}
	
	protected void startActivity(String action) {
		Intent i = new Intent(action);
		startActivity(i);
	}

	protected void startActivity(Class<?> cls) {
		Intent i = new Intent(this, cls);
		startActivity(i);
	}
	
	protected void startActivity(String action, int requestCode) {
		Intent i = new Intent(action);
		startActivityForResult(i, requestCode);
	}
	
	protected TextView getTitleTextView() {
		if (mTitleTextView == null) {
			View titleView = findViewById(MResource.getIdByName(getApplication(), "id", "title_textview"));
			if (titleView instanceof TextView) {
				mTitleTextView = (TextView)titleView;
			}
		}

		return mTitleTextView;
	}
	
	protected void setTitle(String strTitle) {
		TextView titleView = getTitleTextView();
		if (titleView != null) {
			titleView = (TextView)titleView;
			titleView.setText(strTitle);
		}
	}
	
	protected boolean needQuitWhenFinish() {
		return mNeedQuitWhenFinish;
	}
	
	protected void doWhenFinish() {
		
	}
	
	protected void enableCloseBtn() {
		View titleView = findViewById(MResource.getIdByName(getApplication(), "id", "title_close"));
		if (titleView != null) {
			titleView.setVisibility(View.VISIBLE);
			titleView.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					mQuitNow = true;
					mNeedQuitWhenFinish = true;
					if (needQuitWhenFinish()) {
						//System.exit(0);
						Intent intent = new Intent();
						intent.putExtra(Const.ParamType.TypeNeedQuitOrNot, true);
						setResult(Const.ResultCode.RESULT_CLOSE, intent);
						finish();
					}
					else {
						setResult(Const.ResultCode.RESULT_CLOSE);
						finish();
					}
					doWhenFinish();
				}
			});
		}
	}
	
	protected void enableBackBtn() {
		View titleView = findViewById(MResource.getIdByName(getApplication(), "id", "title_back"));
		if (titleView != null) {
			titleView.setVisibility(View.VISIBLE);
			titleView.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if (needQuitWhenFinish()) {
						//System.exit(0);
						Intent intent = new Intent();
						intent.putExtra(Const.ParamType.TypeNeedQuitOrNot, true);
						setResult(RESULT_OK, intent);
						finish();
					}
					else {
						finish();
					}
					doWhenFinish();
				}
			});
		}
	}
	
	protected void showBackBtn() {
		enableBackBtn();
		enableCloseBtn();
	}

	protected void gotoWebViewActivity(String url, String title) {
		Intent i = new Intent(this, WebViewActivity.class);
		i.putExtra("url", url);
		i.putExtra("title", title);
		startActivity(i);
	}

	protected void hideTopbar() {
		View topbar = findViewById(MResource.getIdByName(getApplication(), "id", "topbar"));
		if (topbar != null) {
			topbar.setVisibility(View.GONE);
		}
	}

	protected static CustomDialog mWaitCursorDialog = null;
	private static Context mWaitContext = null;
    //展示等待光标
	public static void showWaitCursor(String strTitle, String strMsg) {
		isInteruptOperation = true;
		
		if (mWaitContext != null && mWaitContext == ContextUtil.getContext() && mWaitCursorDialog != null) {
			mWaitCursorDialog.dismiss();
			mWaitCursorDialog = null;
		}
				
		mWaitContext = ContextUtil.getContext();
		View rootView = View.inflate(mWaitContext, MResource.getIdByName(((Activity)ContextUtil.getContext()).getApplication(), "layout", "ipay_net_loading_layout"), null);
		if (!TextUtils.isEmpty(strMsg)) {
			TextView textView = (TextView)rootView.findViewById(MResource.getIdByName(((Activity)ContextUtil.getContext()).getApplication(), "id", "identify_label"));
			if (textView != null) {
				textView.setText(strMsg);
			}
		}
		mWaitCursorDialog = new CustomDialog(mWaitContext, rootView);
		mWaitCursorDialog.setCanceledOnTouchOutside(false);
		
		mWaitCursorDialog.show();
	}
	
	public static void showWaitCursor(int idTitle, int idMsg) {
		showWaitCursor(idTitle > 0 ? ContextUtil.getContext().getString(idTitle) : null, idMsg > 0? ContextUtil.getContext().getString(idMsg) : null);
	}
	
	public static void hideWaitCursor() {
		isInteruptOperation = false;
		
		try {
			if (mWaitContext != null && mWaitContext == ContextUtil.getContext() && mWaitCursorDialog != null) {
				mWaitCursorDialog.dismiss();
				mWaitCursorDialog = null;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	
	protected View mLoadingView;
	protected View mLoadingImage;
	protected AnimationDrawable mLoadingAnimationDrawable;
	public void showLoading() {
		if (mLoadingView == null) {
			mLoadingView = View.inflate(this, MResource.getIdByName(getApplication(), "layout", "ipay_small_waiting_layout"), null);
			ViewGroup.LayoutParams layoutParams = mLoadingView.getLayoutParams();
			if (layoutParams == null) {
				layoutParams = new ViewGroup.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
			}
			addContentView(mLoadingView, layoutParams);
		}
		if (mLoadingImage == null) {
			mLoadingImage = mLoadingView.findViewById(MResource.getIdByName(getApplication(), "id", "loading_image"));
		}
		mLoadingView.setVisibility(View.VISIBLE);
		mLoadingAnimationDrawable = (AnimationDrawable)mLoadingImage.getBackground();
		mLoadingAnimationDrawable.start();
	}
	
	public void hideLoading() {
		if (mLoadingAnimationDrawable != null){
			mLoadingAnimationDrawable.stop();
			mLoadingView.setVisibility(View.GONE);
        }
	}
	
	public void  showResponseInfo(int errorCode)
	{
		int id = MResource.getIdByName(getApplication(), "string", "ipay_connect_to_server_failed");
		switch(errorCode)
		{
		case Constant.ERROR:
			id = MResource.getIdByName(getApplication(), "string", "ipay_connect_to_server_failed");
			break;
		
		case Constant.PARM_ERROR:
			id = MResource.getIdByName(getApplication(), "string", "ipay_net_param_error");
			break;
			
		case Constant.MEMORY_ERROR:
			id = MResource.getIdByName(getApplication(), "string", "ipay_net_memory_error");
			break;
			
		case Constant.FILE_ERROR:
			id = MResource.getIdByName(getApplication(), "string", "ipay_net_file_error");
			break;
			
		case Constant.CONNECT_ERROR:
			id = MResource.getIdByName(getApplication(), "string", "ipay_connect_to_server_failed");
			break;
			
		case Constant.READ_ERROR:
			id = MResource.getIdByName(getApplication(), "string", "ipay_net_read_error");
			break;
			
		case Constant.SPACE_INSUFFICIENT:
			id = MResource.getIdByName(getApplication(), "string", "ipay_net_read_error");
			break;
			
		case Constant.INTERNAL_ERROR:
			id = MResource.getIdByName(getApplication(), "string", "ipay_net_internal_error");
			break;
		}
		
		String message = getString(id);
		ToastHelper.shortDefaultToast(message);
		
		iChangduPay.setResult(ResultCode.Failed, message);
	}
	
	private long exitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
	    if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){ 	    	
    		doWhenFinish();
    		
	    	if (needQuitWhenFinish()) {
				Intent intent = new Intent();
				intent.putExtra(Const.ParamType.TypeNeedQuitOrNot, true);
				setResult(RESULT_OK, intent);
				finish();
				return true;
			}
	    }
		return super.onKeyDown(keyCode, event);
	}
	
	protected void showFailedDailog() {
		View rootView = View.inflate(this, MResource.getIdByName(getApplication(), "layout", "ipay_recharge_failed"), null);
		final Dialog dialog = new CustomDialog(this, rootView);
		Button okButton = (Button)rootView.findViewById(MResource.getIdByName(getApplication(), "id", "ok"));
		okButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
				onCloseFailedDialog();
			}
		});
		
		dialog.show();
	}
	
	protected void onCloseFailedDialog() {
		
	}

	public void disableFlingExit() {
		isFilingExitEnable = false;
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (isInteruptOperation) {
			return true;
		}
		if (isFilingExitEnable) {
			if (flingExitDetector.flingExitProcess(ev)) {
				return true;
			}
		}
		return super.dispatchTouchEvent(ev);
	}

	private FlingExit.OnFlingExcuteCallBack callBack = new FlingExit.OnFlingExcuteCallBack() {
		@Override
		public boolean onFlingExcute() {
			return onFlingExitExcute();
		}
	};

	/*
	 * 控制 滑動動作，是否執行
	 * 子类如果需要在特定情況下才响应右滑事件，则Override 该方法
	 */
	public boolean isNeedExcuteFlingExit() {
		return true;
	}

	protected boolean finishSpecify() {
		return false;
	}
	
	public boolean onFlingExitExcute() {
		if (isNeedExcuteFlingExit()) {
			if (!finishSpecify()) {
				if (Utils.isSoftInputModeAdjustmentOptions(this, LayoutParams.SOFT_INPUT_ADJUST_RESIZE)) {
					// 挤压 Layout 模式
					Utils.hiddenKeyboard(this);
					if (mFilingExitHandler != null) {
						mFilingExitHandler.postDelayed(new Runnable() {
							
							@Override
							public void run() {
								doWhenFinish();
								finish();
							}
						}, 150);
					}
					return true;
				}
				Utils.hiddenKeyboard(this);
				doWhenFinish();
				finish();
			}
			return true;
		} else {
			return false;
		}
	}
	
}
