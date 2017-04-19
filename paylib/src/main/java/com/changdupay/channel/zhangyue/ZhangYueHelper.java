package com.changdupay.channel.zhangyue;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Application;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.text.TextUtils;
import android.webkit.WebView;

import com.changdupay.app.BaseActivity;
import com.changdupay.encrypt.SmsUtils;
import com.changdupay.open.iChangduPay;
import com.changdupay.open.iChangduPay.ResultCode;
import com.changdupay.util.MResource;
import com.changdupay.util.ToastHelper;

public class ZhangYueHelper {
	private WebView mWebView = null;
	private Application mApplication = null;
	private Activity mActivity = null;
	private ResultData mResultData = null;
	public ZhangYueHelper(Application application, Activity activity, WebView webView)
	{
		mWebView = webView;
		mApplication = application;
		mActivity = activity;
		mWebView.addJavascriptInterface(new ZhangYueJavaScriptInterface(), "ZhangYueJS");
		mResultData = new ResultData();
		
		mActivity.registerReceiver(sendMessage, new IntentFilter(SENT_SMS_ACTION));
	}
	
	public void unRegisterReceiver()
	{
		mActivity.unregisterReceiver(sendMessage);
	}
	
	// 注入js函数监听  
    public void addInputClickListner() {  
        // 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去  
    	mWebView.loadUrl("javascript:(function(){" +  
        "var objs = document.getElementsByTagName(\"input\"); " +   
                "for(var i=0;i<objs.length;i++)  " +   
        "{"  
                + "    if (objs[i].onclick != null )" +
        "    {  "  
	                + "    objs[i].onclick=function()  " +   
	        "    {  "   
	                + "       window.ZhangYueJS.do_start();  " +   
	        "    }  " +   
        "    }  " +   
        "}" +   
        "})()");  
    } 
    
	private void gotoSmsRecharge()
	{
		if (mResultData.SmsContent != null
		   && !TextUtils.equals(mResultData.SmsContent, "")
		   && mResultData.SmsAddress != null
		   && !TextUtils.equals(mResultData.SmsAddress, ""))
		{
			sendSMS(mResultData.SmsAddress, mResultData.SmsContent);
		}
		else
		{
			BaseActivity.hideWaitCursor();
			ToastHelper.shortDefaultToast(mActivity.getString(MResource.getIdByName(mApplication, "string", "ipay_channel_info_error")));	
		}
	}
	
	private void sendSMS(String phoneNumber, String message) 
    {
    	Intent sentIntent = new Intent(SENT_SMS_ACTION);
        PendingIntent sentPI = PendingIntent.getBroadcast(mActivity, 0, sentIntent,0);
    	SmsUtils.sendSms(phoneNumber, message, sentPI);
    }

	private Handler mHandler = new Handler();
	public class ZhangYueJavaScriptInterface {

		ZhangYueJavaScriptInterface() {
        }
		
		public void do_start(){
			BaseActivity.showWaitCursor(null, mActivity.getString(MResource.getIdByName(mApplication, "string", "ipay_wait_for_request_data")));
			mWebView.loadUrl("javascript:submitCheckcode()");
		}
		 
        /**
         * This is not called on the UI thread. Post a runnable to invoke
         * loadUrl on the UI thread.
         * @throws JSONException 
         */
        public void do_command(String jsonObject){
        	final String smsInfo = jsonObject;
            mHandler.post(new Runnable() {
                public void run() {
                	
                	JSONObject jSONContent;
					try {
						jSONContent = new JSONObject(smsInfo);
						mResultData.Action = jSONContent.getString("Action");
						JSONObject data = jSONContent.getJSONObject("Data");
						if (data != null)
						{
							mResultData.ChargingType = data.getString("ChargingType");
							JSONObject charging = data.getJSONObject("Charging");
							if (charging != null)
							{
			                	mResultData.SmsContent = charging.getString("SmsContent");
			                	mResultData.SmsAddress = charging.getString("SmsAddress");
			                	mResultData.SendSmsIndex = charging.getString("SendSmsIndex");
			                	mResultData.ConfirmWait = charging.getString("ConfirmWait");
							}
						}
	                	
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					finally
					{
						gotoSmsRecharge();
					}
                	
                    //mWebView.loadUrl("javascript:smsSendConfirm(true)");
                }
            });
        }
    }
	
	/**发送与接收的广播**/
    String SENT_SMS_ACTION = "SENT_SMS_ACTION";
    private BroadcastReceiver sendMessage = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			BaseActivity.hideWaitCursor();
			//判断短信是否发送成功
            switch (getResultCode()) {
            case Activity.RESULT_OK:
            	mWebView.loadUrl("javascript:smsSendConfirm(true)");
            	iChangduPay.setResult(ResultCode.Success, "success");
                break;
            default:
            	mWebView.loadUrl("javascript:smsSendConfirm(false)");
            	iChangduPay.setResult(ResultCode.Failed, "");
                break;
            }
			
		}
    };
}
