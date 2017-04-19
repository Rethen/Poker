package com.changdupay.app;

import java.io.File;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.changdupay.android.lib.R;
import com.changdupay.protocol.ProtocolData.PayEntity;
import com.changdupay.protocol.base.PayConst;
import com.changdupay.util.Const;
import com.changdupay.util.DownloadUtils;
import com.changdupay.util.PayConfigReader;
import com.changdupay.util.PayConfigs.Channel;
import com.changdupay.util.ToastHelper;
import com.unionpay.UPPayAssistEx;



public class iCDPayChooseMoneyUPPayActivity extends iCDPayChooseMoneyActivtiy{
	
	//String fileName = MyApplication.MAIN_PATH + "UPPayPluginEx.apk";
	//String url = "http://mobile.unionpay.com/getclient?platform=android&type=securepayplugin";
	boolean ifPaying = false;
	boolean ifDownloading = false;
	String errMsg = "";
	String tn;
	private static final int PLUGIN_VALID = 0;
	private static final int PLUGIN_NOT_INSTALLED = -1;
	private static final int PLUGIN_NEED_UPGRADE = 2;
	private ProgressDialog mProgress = null;
	/*****************************************************************
	 * mMode参数解释： "00" - 启动银联正式环境 "01" - 连接银联测试环境
	 *****************************************************************/
	private String mMode = "00";//PayConst.INNER_NET? "00":"01";

	private  String MAIN_PATH=Environment.getExternalStorageDirectory().getPath() +"/Changdu/download/software/";
	String fileName = MAIN_PATH + "UPPayPluginEx.apk";
	String url = "http://img.51changdu.com/resource/UPPayPluginExPro.apk";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(PayConst.isAndReader())//说明安卓读书的
		{
			MAIN_PATH =Environment.getExternalStorageDirectory().getPath()+ "/AndroidReader/download/software/";
			fileName = MAIN_PATH + "UPPayPluginEx.apk";
		}
	}
	
	@Override
	protected String getPayName()
	{
		return "UPPaySDK";
	}
	
	@Override
	protected int getPayCode()
	{
		return Const.PayCode.uppay;
	}
	
	@Override
	protected String getTopTitle()
	{
		return "ipay_savings_uppay";
	}
	
	@Override
	protected void doPayAction()
	{
		Channel item = PayConfigReader.getInstance().getPayChannelItemByPayCodeType(getPayCode(), -1);
		if (item != null)
		{
			gotoBankPay(item.PayType, item.PayId);
		}
	}
	
	protected void gotoBankPay(int nPayType, int nPayId)
	{
		doRequest(nPayType, nPayId);
	}
	
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			try {
				if (mProgress != null)
					mProgress.dismiss();
			} catch (Exception e) {

			}

			if (msg.what == 0) {
			} else if (msg.what == -1) {
				/*************************************************
				 * 
				 * 步骤2：通过银联工具类启动支付插件
				 * 
				 ************************************************/
				// mMode参数解释：
				// 0 - 启动银联正式环境
				// 1 - 连接银联测试环境
				//tn = "201409161121390086362";
				try{
					int ret = UPPayAssistEx.startPay(iCDPayChooseMoneyUPPayActivity.this, null, null, tn, mMode);
					if (ret == PLUGIN_NEED_UPGRADE || ret == PLUGIN_NOT_INSTALLED) {
						// 需要重新安装控件
						Log.e("UPPayAssistEx===", " plugin not found or need upgrade!!!");
						if(ret == PLUGIN_NEED_UPGRADE){
							File file = new File(fileName);
							if (file.exists() && file.length() > 0) {
								file.deleteOnExit();
							}
						}
						new AlertDialog.Builder(iCDPayChooseMoneyUPPayActivity.this).setTitle(R.string.ipay_dialog_title).setMessage(R.string.ipay_uppay_install_confirm).setPositiveButton(R.string.ipay_cancel, null)
						.setNegativeButton(R.string.ipay_ok, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								installUPPay();}
						}).show();
	//					Android.QMsgDlg(PayUppayActivity.this, "完成支付需要安装银联支付控件，是否安装？", new IQMsgDlgCallback() {
	//						@Override
	//						public void onSureClick() {
	//							installUPPay();
	//							// UPPayAssistEx.installUPPayPlugin(PayUppayActivity.this);
	//						}
	//					});
					}
				}catch(Error e){
					e.printStackTrace();
					ToastHelper.longToastText(R.string.ipay_recharge_failed);
				}

			} else if (msg.what == -2) {
				ToastHelper.shortToastText(errMsg);
			} else if (msg.what == -4) {

			
			} else if (msg.what == -5) { // 安装插件
				try {
					File file = new File(fileName);
					if (file.exists() && file.length() > 0) {
						
						PackageInfo apkInfo = iCDPayChooseMoneyUPPayActivity.getApkInfo(iCDPayChooseMoneyUPPayActivity.this, fileName);
						if (apkInfo == null) {
							file.delete();
						} else {
							Intent intent = new Intent(Intent.ACTION_VIEW);
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							intent.setAction(Intent.ACTION_VIEW);
							intent.setDataAndType(Uri.fromFile(new File(fileName)), "application/vnd.android.package-archive");
							iCDPayChooseMoneyUPPayActivity.this.startActivity(intent);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			
			}
			else if (msg.what == -6) { // 安装插件
				ToastHelper.shortToastText(R.string.ipay_uppay_download_failed);
			}
		}
	};
	public static PackageInfo getApkInfo(Context context, String archiveFilePath) {
		PackageManager pm = context.getPackageManager();
		PackageInfo apkInfo = pm.getPackageArchiveInfo(archiveFilePath,
				PackageManager.GET_META_DATA);
		return apkInfo;
	}
	private void installUPPay() {
		if (ifDownloading)
			return;

		// 判断是否需要安装组件
		boolean ifExists = false;
		try {
			if (this.getPackageManager().getPackageInfo("com.unionpay.uppay", 0) != null) {
				ifExists = true;
			}
		} catch (Exception e) {
		}
		if (ifExists)
			return;

		//mProgress = Android.runningDlg(this, "正在安装银联支付控件...");
		ToastHelper.longToastText(R.string.ipay_uppay_installing);
		new Thread() {
			public void run() {
				boolean bRet = true;
				try {
					ifDownloading = true;
					File file = new File(fileName);
					if (file.exists() && file.length() > 0) {

					} else {
						file = new File(MAIN_PATH);
						file.mkdirs();
						bRet = DownloadUtils.httpDownload(url, fileName);
						
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(bRet)
				{
					mHandler.sendEmptyMessage(-5);
				
					
				}
				else
				{
					mHandler.sendEmptyMessage(-6);
				}
				ifDownloading = false;
			}
		}.start();
	}
	private boolean checkInstall() {
		// 判断是否需要安装组件
		boolean ifExists = false;
		try {
			if (this.getPackageManager().getPackageInfo("com.unionpay.uppay", 0) != null) {
				ifExists = true;
			}
		} catch (Exception e) {
		}
		if (!ifExists) {
			new AlertDialog.Builder(iCDPayChooseMoneyUPPayActivity.this).setTitle(R.string.ipay_dialog_title).setMessage(R.string.ipay_uppay_install_confirm).setPositiveButton(R.string.ipay_cancel, null)
			.setNegativeButton(R.string.ipay_ok, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					installUPPay();
				}
			}).show();
//			Android.QMsgDlg(PayUppayActivity.this, "进行充值需要安装银联支付控件，是否安装？", new IQMsgDlgCallback() {
//				@Override
//				public void onSureClick() {
//					installUPPay();
//					// UPPayAssistEx.installUPPayPlugin(PayUppayActivity.this);
//				}
//			});
		}
		return ifExists;
	}
	private void pay() {
		if (ifPaying)
			return;
		if (!checkInstall())
			return;
		mHandler.sendEmptyMessage(-1);
				
		ifPaying = false;
			

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		/*************************************************
		 * 
		 * 步骤3：处理银联手机支付控件返回的支付结果
		 * 
		 ************************************************/
		Log.e("onActivityResult", "onActivityResult");
		if (data == null) {
			return;
		}

		String msg = "";
		/*
		 * 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
		 */
		String str = data.getExtras().getString("pay_result");
		if (str.equalsIgnoreCase("success")) {
			//mProgress = Android.runningDlg(this, "正在处理充值...");
			ToastHelper.longToastText(R.string.ipay_pay_success);
		} else if (str.equalsIgnoreCase("fail")) {
	
			ToastHelper.longToastText(R.string.ipay_pay_failed);
		} else if (str.equalsIgnoreCase("cancel")) {
//			msg = "用户取消了支付";
//			ToastHelper.longToastText(msg);
		}
		
		
		data = new Intent();
		data.putExtra(Const.ParamType.TypeNeedQuitOrNot, true);
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	protected void doRequestResult(PayEntity payEntity)
	{
		tn = payEntity.Parameter;
		pay();
	}


}
