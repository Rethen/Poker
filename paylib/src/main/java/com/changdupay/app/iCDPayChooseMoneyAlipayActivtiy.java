package com.changdupay.app;

import android.os.Bundle;
import android.util.Log;

import com.changdupay.util.ChannelPayType;
import com.changdupay.util.Const;
import com.changdupay.util.PayConfigReader;
import com.changdupay.util.PayConfigs.Category;
import com.changdupay.util.PayConfigs.Channel;
import com.changdupay.util.Utils;

public class iCDPayChooseMoneyAlipayActivtiy extends iCDPayChooseMoneyActivtiy{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected String getPayName()
	{
		return "alipay";
	}
	
	@Override
	protected int getPayCode()
	{
		return Const.PayCode.alipay;
	}
	
	@Override
	protected String getTopTitle()
	{
		return "ipay_alipay";
	}
	
	protected Channel getPayChannel()
	{
//		ChannelPayType payType = Const.ChannelType.alipaysdk;
//		if (payType == null)
//			return null;
//		
//		return PayConfigReader.getInstance().getPayChannelItemPairs(payType.PayType, payType.PayId);
		//先取第0个
		Channel item = PayConfigReader.getInstance().getPayChannelItemByPayCodeType(getPayCode(), -1);
		return item;
	}
	
	@Override
	protected void doPayAction()
	{
		gotoAlipayPay();
	}
	
	private void gotoAlipayPay()
	{
//		ChannelPayType payType = Const.ChannelType.alipaywap;
//		if (Utils.isAppInstalled(iCDPayChooseMoneyAlipayActivtiy.this,Const.ALIPAY_PACKAGE_NAME))
//		{
//			payType = Const.ChannelType.alipaysdk;
//		}
		
		Category cate = PayConfigReader.getInstance().getPayCategoryByCode(getPayCode());
		if(cate.Channels == null || cate.Channels.size() == 0){
			Log.e("iCDPayChooseMoneyAlipayActivtiy", "no Alipay Channels");
			return;
		}
		
		boolean isAppInstall = false;
		if (Utils.isAppInstalled(iCDPayChooseMoneyAlipayActivtiy.this,Const.ALIPAY_PACKAGE_NAME))
		{
			isAppInstall = true;
		}else if (Utils.isAppInstalled(iCDPayChooseMoneyAlipayActivtiy.this,Const.ALIPAY_PACKAGE_NAME2)){
			isAppInstall = true;
		}
		Channel item = null;
		for(Channel itemChannel:cate.Channels){
			if(isAppInstall){ //有安装支付宝
				if(itemChannel.ViewType != 4){
					item = itemChannel;
					break;
				}
			}
			else{ //未安装支付宝
				//网页跳转 类型 | (支付宝WAP支付)
				if(itemChannel.ViewType == 4){
					item = itemChannel;
					break;
				}
			}
		}
		
		doRequest(item.PayType, item.PayId);
	}
}
