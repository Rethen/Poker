package com.changdupay.app;

import mm.purchasesdk.Purchase;
import android.os.Bundle;
import android.view.View;

import com.changdupay.mobileMM.IAPHandler;
import com.changdupay.mobileMM.IAPListener;
import com.changdupay.protocol.base.PayConst;
import com.changdupay.util.Const;
import com.changdupay.util.MResource;
import com.changdupay.util.PayConfigReader;
import com.changdupay.util.PayConfigs.Channel;

public class iCDPayChooseMoneyMobileMMActivtiy extends iCDPayChooseMoneyActivtiy{
	
	public static Purchase purchase;
	private IAPListener mListener;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		findViewById(MResource.getIdByName(getApplication(), "id", "choosemoney_input_layout")).setVisibility(View.GONE);

		IAPHandler iapHandler = new IAPHandler(this);

		/**
		 * IAP组件初始化.包括下面3步。
		 */
		/**
		 * step1.实例化PurchaseListener。实例化传入的参数与您实现PurchaseListener接口的对象有关。
		 * 例如，此Demo代码中使用IAPListener继承PurchaseListener，其构造函数需要Context实例。
		 */
		mListener = new IAPListener(this, iapHandler);
		/**
		 * step2.获取Purchase实例。
		 */
		purchase = Purchase.getInstance();
		/**
		 * step3.向Purhase传入应用信息。APPID，APPKEY。 需要传入参数APPID，APPKEY。 APPID，见开发者文档
		 * APPKEY，见开发者文档
		 */
		try {
			purchase.setAppInfo(PayConst.MobileMMAPPID, PayConst.MobileMMAPPKEY);

		} catch (Exception e1) {
			e1.printStackTrace();
		}
		/**
		 * step4. IAP组件初始化开始， 参数PurchaseListener，初始化函数需传入step1时实例化的
		 * PurchaseListener。
		 */
		try {
			purchase.init(this, mListener);

		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
	
	@Override
	protected String getPayName()
	{
		return "mobileMM";
	}
	
	@Override
	protected int getPayCode()
	{
		return Const.PayCode.mobileMM;
	}
	
	@Override
	protected String getTopTitle()
	{
		return "ipay_mobile_mm";
	}
	
	@Override
	protected void doPayAction()
	{
		Channel item = PayConfigReader.getInstance().getPayChannelItemByPayCodeType(getPayCode(), -1);
		if (item != null)
		{
			gotoPay(item.PayType, item.PayId);
		}
	}
	
	protected void gotoPay(int nPayType, int nPayId)
	{
		doRequest(nPayType, nPayId);
	}
	
	protected void doMMPayAction(String[] arrStr)
	{		
		
		purchase.order(this, arrStr[2], 1, arrStr[3], true, mListener);
	}
}
