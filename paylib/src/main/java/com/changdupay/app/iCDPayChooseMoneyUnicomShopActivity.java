package com.changdupay.app;//package com.changdupay.app;
//
//import android.os.Bundle;
//
//import com.changdupay.util.ChannelPayType;
//import com.changdupay.util.Const;
//import com.changdupay.util.PayConfigReader;
//import com.changdupay.util.PayConfigs.Channel;
//
//public class iCDPayChooseMoneyUnicomShopActivity extends iCDPayChooseMoneyActivtiy{
//	private String mPhoneNumber = "";
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		mPhoneNumber = getIntent().getStringExtra(Const.ParamType.TypeMobilePhone);
//	}
//	
//	@Override
//	protected String getPayName()
//	{
//		return "unicom_woshop";
//	}
//	
//	@Override
//	protected int getPayCode()
//	{
//		return Const.PayCode.unicom_woshop;
//	}
//	
//	@Override
//	protected String getTopTitle()
//	{
//		return "ipay_unicom_shop";
//	}
//	
//	protected Channel getPayChannel()
//	{
//		ChannelPayType payType = Const.ChannelType.unicomshop;
//		if (payType == null)
//			return null;
//		
//		return PayConfigReader.getInstance().getPayChannelItemPairs(payType.PayType, payType.PayId);
//	}
//	
//	@Override
//	protected String getPhoneNumber()
//	{
//		return mPhoneNumber;
//	}
//	
//	@Override
//	protected void doPayAction()
//	{
//		ChannelPayType payType = Const.ChannelType.unicomshop;
//		doRequest(payType.PayType, payType.PayId);
//	}
//}
