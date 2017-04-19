package com.changdupay.channel.alipay;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.changdupay.android.lib.R;
import com.changdupay.util.Const;
import com.changdupay.util.ContextUtil;
import com.changdupay.util.MResource;
import com.changdupay.util.ToastHelper;
import com.changdupay.util.Utils;

public class AlipayHelper {
//	public static String ALIPAY_PACKAGE_NAME = "com.alipay.android.app";
	private static String TRADE_STATUS = "9000";// 交易状态码，只有9000表示交易成功
	private static String TRADE_STATUS_CONFIRMING = "8000";// 交易状态码，8000表示确认中
	public static final int RQF_PAY = 1;

	private String payInfo = "";
	private Activity currentActivity = null;

	private boolean mNeedQuitWhenFinish = false;

	public boolean excutePay(final String payInfo, Activity cActivity, boolean needQuitWhenFinish) {
		if (cActivity == null) {
			Log.d("AlipayHelper","currentActivity is null");
			ToastHelper.longDefaultToast(R.string.ipay_alipay_pay_success);
			return false;
		}else if(TextUtils.isEmpty(payInfo)){
			Log.d("AlipayHelper", "payInfo is null");
			ToastHelper.longDefaultToast(R.string.ipay_alipay_pay_fail);
			return false;
		}

		if (!(Utils.isAppInstalled(cActivity, Const.ALIPAY_PACKAGE_NAME) 
				|| Utils.isAppInstalled(cActivity, Const.ALIPAY_PACKAGE_NAME2))){
			ToastHelper.longDefaultToast(R.string.ipay_alipay_not_installed);
			return false;
		}

		this.payInfo = payInfo;
		this.currentActivity = cActivity;
		
		this.mNeedQuitWhenFinish = needQuitWhenFinish;

		// 开始支付
		try {
			Log.d("AlipayHelper", "payInfo:" + payInfo);

			// 调用pay方法进行支付
//			MobileSecurePayer msp = new MobileSecurePayer();
//			boolean bRet = msp.pay(payInfo, mHandler, RQF_PAY, currentActivity);
//			if (bRet) {
//				Log.d("AlipayHelper", "正在支付...");
//			}
			
			Runnable payRunnable = new Runnable() {

				@Override
				public void run() {
					// 构造PayTask 对象
					PayTask alipay = new PayTask(currentActivity);
					// 调用支付接口，获取支付结果
					String result = alipay.pay(payInfo); // 完整的符合支付宝参数规范的订单信息

					Message msg = new Message();
					msg.what = RQF_PAY;
					msg.obj = result;
					mHandler.sendMessage(msg);
				}
			};

			// 必须异步调用
			Thread payThread = new Thread(payRunnable);
			payThread.start();
		} catch (Exception ex) {
			Log.e("AlipayHelper", ex.getMessage());

			this.payInfo = "";
			this.currentActivity = null;
		}

		return true;
	}

	// 这里接收支付结果，支付宝手机端同步通知
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			try {
				String ret = (String) msg.obj;
				Log.d("AlipayHelper", ret);
				
				switch (msg.what) {
					case RQF_PAY: {
	
						// 处理交易结果
						try {
							// 获取交易状态码，具体状态代码请参看文档
							String tradeStatus = "resultStatus={";
							int imemoStart = ret.indexOf("resultStatus=");
							imemoStart += tradeStatus.length();
							int imemoEnd = ret.indexOf("};memo=");
							tradeStatus = ret.substring(imemoStart, imemoEnd);
							Log.d("AlipayHelper", "tradeStatus: "+tradeStatus);
							
							if (TRADE_STATUS.equals(tradeStatus)) {
								Utils.gotoPaySuccessActivity("", currentActivity instanceof Activity? (Activity)currentActivity : null);
								//ToastHelper.shortDefaultToast(R.string.ipay_alipay_pay_success);
							} else {
								//Utils.gotoPayFailedActivity(mNeedQuitWhenFinish, currentActivity instanceof Activity? (Activity)currentActivity : null);
								Activity activity = null;
								if (currentActivity instanceof Activity) {
									activity = (Activity)currentActivity;
								}
								else if (ContextUtil.getContext() instanceof Activity) {
									activity = (Activity)ContextUtil.getContext();
								}
								if (activity != null) {
									// 判断resultStatus 为非“9000”则代表可能支付失败
									// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
									if (TRADE_STATUS_CONFIRMING.equals(tradeStatus)) {
										Toast.makeText(activity, "支付结果确认中",
												Toast.LENGTH_SHORT).show();
									} else{
										ToastHelper.shortDefaultToast(MResource.getIdByName(activity.getApplication(), "string", "ipay_alipay_pay_fail"));
									}
									
									if (mNeedQuitWhenFinish) {
										Intent intent = new Intent();
										intent.putExtra(Const.ParamType.TypeNeedQuitOrNot, mNeedQuitWhenFinish);
										activity.setResult(Activity.RESULT_OK, intent);
										activity.finish();	
									}
								}
							}							
	
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					break;
				}

				super.handleMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				payInfo = "";
				currentActivity = null;
			}
		}
	};

	/*
	 * the OnCancelListener for lephone platform. lephone系统使用到的取消dialog监听
	 */
	static class AlixOnCancelListener implements DialogInterface.OnCancelListener {
		Activity mcontext;

		AlixOnCancelListener(Activity context) {
			mcontext = context;
		}

		public void onCancel(DialogInterface dialog) {
			mcontext.onKeyDown(KeyEvent.KEYCODE_BACK, null);
		}
	}
}
