package com.changdupay.app;

import org.json.JSONObject;

import mm.purchasesdk.Purchase;
import android.os.Bundle;
import android.view.View;

import com.changdupay.encrypt.JsonUtils;
import com.changdupay.mobileMM.IAPHandler;
import com.changdupay.mobileMM.IAPListener;
import com.changdupay.protocol.ProtocolData.PayEntity;
import com.changdupay.protocol.base.PayConst;
import com.changdupay.util.Const;
import com.changdupay.util.MResource;
import com.changdupay.util.PayConfigReader;
import com.changdupay.util.PayConfigs.Channel;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class iCDPayChoaseMoneyWeiXin extends iCDPayChooseMoneyActivtiy {
	public IWXAPI msgApi;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		findViewById(
//				MResource.getIdByName(getApplication(), "id",
//						"choosemoney_input_layout")).setVisibility(View.GONE);
		msgApi = WXAPIFactory.createWXAPI(this, null);
		msgApi.registerApp(PayOrderInfoManager.getPayOrderInfoManager().mPayOrderInfo.WxKey);

	}

	@Override
	protected String getPayName() {
		return "mobileWeiXin";
	}

	@Override
	protected int getPayCode() {
		return Const.PayCode.weixin;
	}

	@Override
	protected String getTopTitle() {
		return "ipay_mobile_wx";
	}

	@Override
	protected void doPayAction() {
		Channel item = PayConfigReader.getInstance()
				.getPayChannelItemByPayCodeType(getPayCode(), -1);
		if (item != null) {
			gotoPay(item.PayType, item.PayId);
		}
	}

	protected void gotoPay(int nPayType, int nPayId) {
		doRequest(nPayType, nPayId);
	}

	protected void doRequestResult(PayEntity payEntity) {
		try {
			JSONObject obj = new JSONObject(payEntity.Parameter);

			PayReq request = new PayReq();
			request.appId = obj.getString("appid");
			request.partnerId = obj.getString("partnerid");
			request.prepayId = obj.getString("prepayid");
			request.packageValue = obj.getString("package");
			request.nonceStr = obj.getString("noncestr");
			request.timeStamp = obj.getString("timestamp");
			request.sign = obj.getString("sign");
			msgApi.sendReq(request);
		} catch (Exception e) {

		}
	}
}
