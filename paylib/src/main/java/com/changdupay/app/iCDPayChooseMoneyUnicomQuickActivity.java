package com.changdupay.app;//package com.changdupay.app;
//
//import android.os.Bundle;
//import android.view.View;
//
//import com.changdupay.util.ChannelPayType;
//import com.changdupay.util.Const;
//import com.changdupay.util.MResource;
//import com.changdupay.util.PayConfigReader;
//import com.changdupay.util.Utils;
//import com.changdupay.util.PayConfigs.Channel;
//
//public class iCDPayChooseMoneyUnicomQuickActivity extends iCDPayChooseMoneyActivtiy{
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		findViewById(MResource.getIdByName(getApplication(), "id", "choosemoney_pay_hint")).setVisibility(View.VISIBLE);
//		findViewById(MResource.getIdByName(getApplication(), "id", "choosemoney_input_layout")).setVisibility(View.GONE);
//	}
//	
//	@Override
//	protected String getPayName()
//	{
//		return "unicom_quick";
//	}
//	
//	@Override
//	protected int getPayCode()
//	{
//		return Const.PayCode.unicom_quick;
//	}
//	
//	@Override
//	protected String getTopTitle()
//	{
//		return "ipay_unicom_quick";
//	}
//	
//	protected Channel getPayChannel()
//	{
//		ChannelPayType payType = Const.ChannelType.unicomquick;
//		if (payType == null)
//			return null;
//		
//		return PayConfigReader.getInstance().getPayChannelItemPairs(payType.PayType, payType.PayId);
//	}
//	
//	@Override
//	protected void doPayAction()
//	{
//		ChannelPayType item = Const.ChannelType.unicomquick;
//		if (item != null)
//		{
//			gotoUnicomWeb(item.PayType, item.PayId);
//		}
//	}
//	
//	private void gotoUnicomWeb(int nPayType, int nPayId)
//	{
//		doRequest(nPayType, nPayId);
//	}
//}
