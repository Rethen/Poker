package com.changdupay.app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.changdupay.commonInterface.BaseCommonStruct;
import com.changdupay.commonInterface.CommonConst;
import com.changdupay.commonInterface.CommonInterfaceManager;
import com.changdupay.open.iChangduPay;
import com.changdupay.open.iChangduPay.IPayCallback;
import com.changdupay.protocol.LoginRequestManager;
import com.changdupay.protocol.LoginResponseManager;
import com.changdupay.protocol.base.PayConst;
import com.changdupay.protocol.base.ResponseHandler.OnGetDynamicKeyListener;
import com.changdupay.util.BitmapHelper;
import com.changdupay.util.Const;
import com.changdupay.util.ContextUtil;
import com.changdupay.util.MResource;
import com.changdupay.util.PayConfigReader;
import com.changdupay.util.PayConfigs.AmountLimit;
import com.changdupay.util.PayConfigs.Category;
import com.changdupay.util.PayConfigs.Channel;
import com.changdupay.util.PayConfigs.ChannelList;
import com.changdupay.util.Shape;
import com.changdupay.util.SkinChangeManager;
import com.changdupay.util.ToastHelper;
import com.changdupay.util.Utils;
import com.changdupay.widget.CustomDialog;
import com.changdupay.widget.IAdapterDataWrap;
import com.changdupay.widget.IRefreshCoinObserver;

public class iCDPayCenterActivity extends BaseActivity implements IAdapterDataWrap, IRefreshCoinObserver{
	private static iCDPayCenterActivity mPayCenterActivity = null;
	private GridView mPayTypeGridView = null;
	private iCDPayGridPayTypeAdapter mPayGridPayTypeAdapter = null;

	private boolean mNeedQuitWhenFinish = false;
	
	private ArrayList<Category> mPayCategorys = new ArrayList<Category>();
	private Category mPayCategory = null;
	private String mLastPayChannel = null;
	
	private ChoosePayTypeDataWrapper mChoosePayTypeDataWrapper = null;
		
//	private RelativeLayout mMainViewPager = null;
    private ImageView mUserHeadImageView = null;

	private ImageView mUserVipLevelImageView = null;
    private TextView tv_coin_text = null;
    private TextView tv_giftcoin_text = null;
    private Shape mHeaderImgShape;
    private View mPanelNameView;
    private int mPanelNameWidth;
    private int mVipWidth = 0;
    private TextView mNickNameTextView;
//    private boolean mIsFirstCreated = true;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(MResource.getIdByName(getApplication(), "layout", "ipay_pay_center"));
		
		onPreResume();	
		initView();		

		String packageName = getPackageName();
		if(packageName.compareToIgnoreCase(PayConst.SHOW_CODE_PACKAGE_NAME_CHANGDU) == 0
				|| packageName.compareToIgnoreCase(PayConst.SHOW_CODE_PACKAGE_NAME_ANDREAD) == 0){
			PayConst.isShowMobileMM = true;
		}
		else{
			PayConst.isShowMobileMM = false;
		}
		
		AsyncTask<Void, Void, Boolean> task = new AsyncTask<Void, Void, Boolean>() {
			@Override
			protected Boolean doInBackground(Void... params) {
				return PayConfigReader.getInstance().parseLocalConfigXml();
			}

			protected void onPostExecute(Boolean ret) {
				if(!ret){
					BaseActivity.showWaitCursor(null, ContextUtil.getContext().getString(MResource.getIdByName(ContextUtil.getContext(), "string", "ipay_wait_for_initializing")));
				}			
				
				showConfig();	
				refreshConfig();
			};
		};
		task.execute();
		
		//刷新金币
		BaseCommonStruct refreshCoidStruct=new BaseCommonStruct();
		refreshCoidStruct.obj1=(IRefreshCoinObserver)iCDPayCenterActivity.this;
		CommonInterfaceManager.INSTANCE.CommonInterfaceID(
				CommonConst.MAIN_MODELID, CommonConst.MAIN_REFRESH_COIN_11001, refreshCoidStruct);
	}
	
	protected void onPreResume()
	{
		mQuitNow = false;
		UserInfo.getInstance().RechargeFlag = false;
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		initAccountViews();

		mPayCenterActivity = this;
		resortPayChannel();
		recordLastPay();
	
		PayOrderInfoManager.getPayOrderInfoManager().getPayOrderInfo().CooperatorOrderSerial = "xmks" + java.util.UUID.randomUUID().toString();		
	}
	
    @Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		CommonInterfaceManager.INSTANCE.CommonInterfaceID(
				CommonConst.MAIN_MODELID, CommonConst.MAIN_CLEAR_PULLDATA_10002, null);
		super.onDestroy();
	}
	
	private void initView()
	{
		mHeaderImgShape = BitmapHelper.getBitmapShape(MResource.getIdByName(getApplication(), "drawable", "default_big_avatar"));
		mHeaderImgShape.height -= Utils.dipDimensionInteger(5);
		mHeaderImgShape.width -= Utils.dipDimensionInteger(5);
		
		View basicInfoView = this.findViewById(MResource.getIdByName(getApplication(), "id", "account_basic_info"));
		mNickNameTextView = (TextView)basicInfoView.findViewById(MResource.getIdByName(getApplication(), "id", "account_basicinfo_passportid"));
		tv_coin_text = (TextView)basicInfoView.findViewById(MResource.getIdByName(getApplication(), "id", "tv_coin_text"));
		tv_giftcoin_text = (TextView)basicInfoView.findViewById(MResource.getIdByName(getApplication(), "id", "tv_giftcoin_text"));
		mPanelNameView = basicInfoView.findViewById(MResource.getIdByName(getApplication(), "id", "panel_name"));
		mPanelNameView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				if (mPanelNameView != null) {
					mPanelNameView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
					mPanelNameWidth = mPanelNameView.getWidth();
					if (handler != null) {
						handler.sendEmptyMessage(HANDLER_CODE_NAME);
					}
				}
			}
		});
		
		basicInfoView.findViewById(MResource.getIdByName(getApplication(), "id", "iv_refresh"))
			.setOnClickListener(onClickRefresh);
		
		mUserHeadImageView = (ImageView) basicInfoView.findViewById(MResource.getIdByName(getApplication(), "id", "account_basicinfo_avatar"));
		mUserVipLevelImageView = (ImageView) basicInfoView.findViewById(MResource.getIdByName(getApplication(), "id", "account_basicinfo_vip"));
		
		((TextView) findViewById(MResource.getIdByName(getApplication(), "id", "title_textview"))).setText(getString(MResource.getIdByName(getApplication(), "string", "ipay_recharge_title")));
		enableBackBtn();
	}
	
	private void initAccountViews()
	{		
		
		iCDPayOrderInfo payOrderInfo = PayOrderInfoManager.getPayOrderInfoManager().getPayOrderInfo();

		mNickNameTextView.setText(payOrderInfo.NickName);
		tv_coin_text.setText(String.valueOf(payOrderInfo.ChangduCoins));
		tv_giftcoin_text.setText(String.valueOf(payOrderInfo.GiftCoins));		
	
		if (payOrderInfo.UserHeadImgBitmap != null)
		{
			mUserHeadImageView.setImageBitmap(payOrderInfo.UserHeadImgBitmap);
		}

		if (payOrderInfo.UserVipLevelBitmap != null)
		{
			mUserVipLevelImageView.setImageBitmap(payOrderInfo.UserVipLevelBitmap);
		}
	}
	
	private void recordLastPay()
	{
		mLastPayChannel = Utils.getStringValue(Const.ConfigKeys.LastPaytype);
		if (mPayGridPayTypeAdapter != null)
			mPayGridPayTypeAdapter.notifyDataSetChanged();
	}	
	
	private static OnGetDynamicKeyListener mOnGetDynamicKeyListener = new OnGetDynamicKeyListener()
	{
		@Override
		public void onGetDynamicKey(Object info) {
			// TODO Auto-generated method stub
			Context context = ContextUtil.getContext();
//			int resultCode = 0;
			String errorMsg = "success";
			if (!Utils.isConfigFileExist()
				    && info == null)
			{
				BaseActivity.hideWaitCursor();
				errorMsg = context.getString(MResource.getIdByName(context, "string", "ipay_connect_to_server_failed"));
				ToastHelper.shortDefaultToast(errorMsg);
//				resultCode = ResultCode.Failed;
				//mCallback.onPayCallback(resultCode, errorMsg);
				return;
			}
			
			if (info == null || info instanceof String) {	
				String content = "";
				if (info != null && info instanceof String)
				{
					content = (String)info;
				}
				try {
					Boolean bResult = PayConfigReader.getInstance().parseConfigXml(content);
					if (!bResult)
					{
						BaseActivity.hideWaitCursor();
						errorMsg = context.getString(MResource.getIdByName(context, "string", "ipay_app_init_failed"));
						ToastHelper.shortDefaultToast(errorMsg);
						return;
					}
					
					mPayCenterActivity.showConfig();					

				}
				catch (Exception e) {
					// TODO: handle exception
//					resultCode = ResultCode.Failed;
					errorMsg = context.getString(MResource.getIdByName(context, "string", "ipay_connect_to_server_failed"));
					ToastHelper.shortDefaultToast(errorMsg);
				}				
			}
			else {
//				resultCode = ResultCode.Failed;
				errorMsg = context.getString(MResource.getIdByName(context, "string", "ipay_connect_to_server_failed"));
				ToastHelper.shortDefaultToast(errorMsg);
			}
			BaseActivity.hideWaitCursor();
			
			//mCallback.onPayCallback(resultCode, errorMsg);
		}
		
	};	
	
	private void refreshConfig()
	{
		LoginResponseManager.getInstance().getResponseHandler().setOnGetDynamicKeyListener(mOnGetDynamicKeyListener);	
		LoginRequestManager.getInstance().requestGetDynamicKey(this, PayConst.MERCHANTID);
	}
	
	private void showConfig()
	{
		initPayChannels();
		initPayChannelGridView();		
	}
	
	private void initPayChannels()
	{
		filterPayChannel();
		resortPayChannel();
	}
	
	private void filterPayChannel()
	{
		mPayCategorys.clear();
		ChannelList payChannelList = PayConfigReader.getInstance().getChannelListByMerchandiseId(PayConst.MERCHANDISEID);
		if(payChannelList == null){
			return;
		}
		ArrayList<Category> categoryList = payChannelList.mlCategoryList;
//		int[] simOperators = Deviceinfo.getSimOperators();
//		boolean containUnicom = false;
//		for (int j = 0; j < simOperators.length; j++) {
//			if (simOperators[j] == Const.SimOperator.Unicom) {
//				containUnicom = true;
//				break;
//			}
//		}
		for (int i = 0; i < categoryList.size(); i++)
		{
			//话费充值不显示，不能注释
			Category payCategory = categoryList.get(i);
			
			mPayCategorys.add(payCategory);
		}
	}
	
	public class SortComparator implements Comparator {  
	    @Override  
	    public int compare(Object lhs, Object rhs) {  
	    	Category a = (Category) lhs;  
	    	Category b = (Category) rhs; 

	    	return (a.SortId - b.SortId);  
	    }  
	} 
	
	@SuppressWarnings("unchecked")
	private void resortPayChannel()
	{
		if (mPayCategorys != null && mPayCategorys.size() > 0)
		{
			Collections.sort(mPayCategorys, new SortComparator()); 
			mPayCategory = mPayCategorys.get(0);
		}
	}

	private void initPayChannelGridView()
	{
		mPayTypeGridView = (GridView)findViewById(MResource.getIdByName(getApplication(), "id", "paycenter_gridview"));
		mPayGridPayTypeAdapter = new iCDPayGridPayTypeAdapter(this, this, getApplication());
		mPayTypeGridView.setAdapter(mPayGridPayTypeAdapter);
		mPayTypeGridView.setOnItemClickListener(new PayCenterOnItemClickListener());
		mPayTypeGridView.setOnTouchListener(new OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				 switch (event.getAction()) {
	                case MotionEvent.ACTION_MOVE:
	                        //return true ;
	                default:
	                        break;
	                }
	                return false;
			}});
		mPayTypeGridView.scrollTo(0, 0);
		mPayTypeGridView.invalidate();
	}
	
	
	Handler mHandler = new Handler();
	Runnable mGetBankListRunnable = new Runnable() {
		@Override
		public void run() {
			
			}
		};
	
	private void openWebPay() 
	{
		View rootView = View.inflate(this, MResource.getIdByName(getApplication(), "layout", "ipay_website_recharge"), null);

		View line = (View)rootView.findViewById(MResource.getIdByName(getApplication(), "id", "ipay_dialog_line"));
		SkinChangeManager.getInstance().changeBackGroundColor(line, false);
		
		TextView title = (TextView)rootView.findViewById(MResource.getIdByName(getApplication(), "id", "ipay_website_recharge_title"));
		SkinChangeManager.getInstance().changeTextColor(title, false);
		
		final Dialog dialog = new CustomDialog(this, rootView);
		Button okButton = (Button)rootView.findViewById(MResource.getIdByName(getApplication(), "id", "ok"));
		SkinChangeManager.getInstance().changeBackgroundDrawable(okButton, false);
		
		okButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				recordLastPay();
				dialog.dismiss();
			}
		});
		
		dialog.show();
	}
	
	public class PayCenterOnItemClickListener implements OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			mPayCategory = (Category)arg0.getAdapter().getItem(arg2);
			mPayGridPayTypeAdapter.setSeclection(arg2);
			mPayGridPayTypeAdapter.notifyDataSetChanged();
	    	doNext();
		}
	}

	private void doNext()
	{
		Class startupClass = null;
		Boolean bJumpUnicomShop = false;
		if (Const.PayCode.rechargecard == mPayCategory.Code)
		{
			startupClass = iCDPayPhoneCardRechargeActivity.class;
		}
		else if (Const.PayCode.oneclick == mPayCategory.Code)
		{
			startupClass = iCDPayChooseMoneyOneClickActivtiy.class;
		}
		else if (Const.PayCode.alipay == mPayCategory.Code)
		{
			startupClass = iCDPayChooseMoneyAlipayActivtiy.class;
		}
		else if (Const.PayCode.sms == mPayCategory.Code)
		{
			startupClass = iCDPaySmsRechargeActivity.class;
		}
		else if (Const.PayCode.savingscard == mPayCategory.Code)
		{
			startupClass = iCDPayChooseMoneyCreditActivtiy.class;
		}
		else if (Const.PayCode.mobileMM == mPayCategory.Code)
		{
			startupClass = iCDPayChooseMoneyMobileMMActivtiy.class;
		}
		else if(Const.PayCode.uppay == mPayCategory.Code)
		{
			startupClass = iCDPayChooseMoneyUPPayActivity.class;
		}
		else if(Const.PayCode.visacard == mPayCategory.Code)
		{
			startupClass = iCDPayChooseMoneyVisaActivity.class;
		}
//		else if (TextUtils.equals(Const.PayCode.unicom_quick, mPayCategory.Name))
//		{
//			startupClass = iCDPayChooseMoneyUnicomQuickActivity.class;
//		}
		else if (Const.PayCode.unicom_woshop == mPayCategory.Code)
		{
			startupClass = iCDPaySmsRechargeActivity.class;
			bJumpUnicomShop = true;
		}
		else if(Const.PayCode.weixin == mPayCategory.Code)
		{
			startupClass = iCDPayChoaseMoneyWeiXin.class;
		}
		else
		{
			openWebPay();
			Utils.resetPayOrder(mPayCategory.Name, 1);
			Utils.setStringValue(Const.ConfigKeys.LastPaytype, mPayCategory.Name);
			resortPayChannel();
			return;
		}
		if (startupClass == null)
		{
			return;
		}
		boolean hasPayMoneyItem = false;
		Channel outChannel = null;
		
		if(PayOrderInfoManager.getPayOrderInfoManager().mPayOrderInfo.nPayMoney > 0)
		{
			int money = PayOrderInfoManager.getPayOrderInfoManager().mPayOrderInfo.nPayMoney;
			String shopItemId = PayOrderInfoManager.getPayOrderInfoManager().mPayOrderInfo.shopItemId;
			for(int i = 0; i < mPayCategory.Channels.size(); i ++)
			{
				Channel ch = mPayCategory.Channels.get(i);
				for (AmountLimit item : ch.AmountLimits) {
					if(item.Value == money)
					{
						if(shopItemId.length() > 0 && Integer.parseInt(shopItemId) > 0 )
						{
							if(item.ShopItemId.equalsIgnoreCase(shopItemId))
							//if(item.ShopItemId == shopItemId)
							{
								hasPayMoneyItem = true;
								outChannel = ch;
							}
						}
						else
						{
							hasPayMoneyItem = true;
							outChannel = ch;
						}
					}
				}
				
			}

		}//说明都有对应的金额 
		if(hasPayMoneyItem)
		{
			if (Const.PayCode.alipay == mPayCategory.Code)
			{
				boolean isAppInstall = false;
				if (Utils.isAppInstalled(this,Const.ALIPAY_PACKAGE_NAME))
				{
					isAppInstall = true;
				}else if (Utils.isAppInstalled(this,Const.ALIPAY_PACKAGE_NAME2)){
					isAppInstall = true;
				}
	
				for(Channel itemChannel:mPayCategory.Channels){
					if(isAppInstall){ //有安装支付宝
						if(itemChannel.ViewType != 4){
							outChannel = itemChannel;
							break;
						}
					}
					else{ //未安装支付宝
						//网页跳转 类型 | (支付宝WAP支付)
						if(itemChannel.ViewType == 4){
							outChannel = itemChannel;
							break;
						}
					}
				}
				
			}
			iCDPayUtilsEx.doRequest(this, mPayCategory.Code, mPayCategory.Name, outChannel.PayType, outChannel.PayId);
		}
		else
		{
			Intent intent = new Intent(this, startupClass);
			intent.putExtra(Const.ParamType.TypeNeedQuitOrNot, mNeedQuitWhenFinish);
			intent.putExtra(Const.ParamType.TypeJumpUnicomShop, bJumpUnicomShop);
			intent.putExtra(Const.ParamType.TypePayViewType, mPayCategory.ViewType);
			startActivityForResult(intent, 1000);
		}
	}
	

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mPayCategorys.size();
	}

	@Override
	public ChoosePayTypeDataWrapper getData(int position) {
		// TODO Auto-generated method stub
		if (mChoosePayTypeDataWrapper == null)
		{
			mChoosePayTypeDataWrapper = new ChoosePayTypeDataWrapper();
		}
		mChoosePayTypeDataWrapper.Name = mPayCategorys.get(position).Name;
		mChoosePayTypeDataWrapper.ResKey = mPayCategorys.get(position).ResKey;
		mChoosePayTypeDataWrapper.KeyFlag = (TextUtils.equals(mPayCategorys.get(position).Name, mLastPayChannel)) ? true : false;
		if (!mChoosePayTypeDataWrapper.KeyFlag && (mPayCategorys.get(position).IconType == 1)){
			mChoosePayTypeDataWrapper.isHot = true;
		}
		else{
			mChoosePayTypeDataWrapper.isHot = false;
		}
		return mChoosePayTypeDataWrapper;
	}

	@Override
	public Object getObject(int position) {
		// TODO Auto-generated method stub
		return mPayCategorys.get(position);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (iChangduPay.getResultCode() == iChangduPay.ResultCode.Success) {
			finish();
			doWhenFinish();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	protected void doWhenFinish() {
		IPayCallback callback = iChangduPay.getCallback();
		if (callback != null) {
			callback.onPayCallback(iChangduPay.getResultCode(), iChangduPay.getErrorMsg());
		}
	}
	
//	ChangduReaderUserInfo mChangduReaderUserInfo = null;
//	private OnGetChangduReaderUserInfoListener mOnGetChangduReaderUserInfoListener = new OnGetChangduReaderUserInfoListener() {
//		
//		@Override
//		public void OnResult(Object info) {
//			// TODO Auto-generated method stub
//			if (info instanceof BaseProtocalData) {
//				if (info instanceof ChangduReaderUserInfo) {
//					mChangduReaderUserInfo = (ChangduReaderUserInfo)info;
//					mChangduCoinTextView.setText(String.valueOf(mChangduReaderUserInfo.userChangduCoin));
//					mGiftCoinTextView.setText(String.valueOf(mChangduReaderUserInfo.userChangduGiftCoin));
//					
//					Thread thread = new Thread(new Runnable() {
//						
//						@Override
//						public void run() {
//							// TODO Auto-generated method stub
//							try {
//								iCDPayOrderInfo payOrderInfo = PayOrderInfoManager.getPayOrderInfoManager().getPayOrderInfo();
//								
//								URL picUrl = new URL(mChangduReaderUserInfo.userImgSrc);
//								Bitmap pngBM = BitmapFactory.decodeStream(picUrl.openStream());
//								if (pngBM != null) {
//									Bitmap destBM = Bitmap.createScaledBitmap(pngBM, mHeaderImgShape.width, mHeaderImgShape.height, true);
//									destBM = BitmapHelper.createRoundAvaterBitmap(destBM);
//									payOrderInfo.UserHeadImgBitmap = destBM;	
//								}
//
//								URL vipLevelUrl = new URL(mChangduReaderUserInfo.userLevelSrc);
//								Bitmap pngBM2 = BitmapFactory.decodeStream(vipLevelUrl.openStream()); 
//								payOrderInfo.UserVipLevelBitmap = pngBM2;
//
//								mRefreshHandler.sendEmptyMessage(0);
//							} catch (Exception e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
//						}
//					});
//					thread.start();
//				}
//			}
//		}
//	};
	

	public class RefreshHandler extends Handler{
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 0:
				refreshUserInfo();
				break;
			}
		}
	}
	private RefreshHandler mRefreshHandler = new RefreshHandler();
	
	private void refreshUserInfo() {
		try {		
			iCDPayOrderInfo payOrderInfo = PayOrderInfoManager.getPayOrderInfoManager().getPayOrderInfo();
			if (payOrderInfo.UserHeadImgBitmap != null) {
				mUserHeadImageView.setImageBitmap(payOrderInfo.UserHeadImgBitmap);	
			}
			if (payOrderInfo.UserVipLevelBitmap != null) {
				mUserVipLevelImageView.setImageBitmap(payOrderInfo.UserVipLevelBitmap);
				
				mVipWidth = payOrderInfo.UserVipLevelBitmap.getWidth();
				if (handler != null) {
					handler.sendEmptyMessage(HANDLER_CODE_NAME);
				}
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	private static final int HANDLER_CODE_NAME = 1000;

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HANDLER_CODE_NAME : {
				if (mPanelNameWidth != 0 && mVipWidth != 0 && mNickNameTextView != null && !TextUtils.isEmpty(mNickNameTextView.getText())) {
					LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mNickNameTextView.getLayoutParams();
					int textLen = (int) (Layout.getDesiredWidth(mNickNameTextView.getText(), mNickNameTextView.getPaint()) + 0.5f);

					if (textLen + Utils.dipDimensionInteger(2.0f) + Utils.dipDimensionInteger(5.0f) >= mPanelNameWidth - mVipWidth) {
						params.weight = 1;
					} else {
						params.weight = 0;
					}
					mNickNameTextView.setLayoutParams(params);
				}
				break;
			}
			default : {
				super.handleMessage(msg);
				break;
			}
			}
		}

	};
	
	//刷新畅读币
	OnClickListener onClickRefresh = new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			BaseCommonStruct getViewClickStruct=new BaseCommonStruct();
			getViewClickStruct.obj1=arg0;
			int result = CommonInterfaceManager.INSTANCE.CommonInterfaceID(
					CommonConst.MAIN_MODELID, CommonConst.MAIN_CHECK_VIEW_CLICK_10001, getViewClickStruct);
			if (result != CommonConst.COM_RET_OK){
				return;
			}
			
			BaseCommonStruct refreshCoidStruct=new BaseCommonStruct();
			refreshCoidStruct.obj1=(IRefreshCoinObserver)iCDPayCenterActivity.this;
			result = CommonInterfaceManager.INSTANCE.CommonInterfaceID(
					CommonConst.MAIN_MODELID, CommonConst.MAIN_REFRESH_COIN_11001, refreshCoidStruct);
			if (result == CommonConst.COM_RET_OK){
				String msgStr = String.format(getString(MResource.getIdByName(getApplication(), "string", "ipay_refresh_coin")), PayConst.COIN_NAME);
				BaseActivity.showWaitCursor(null, msgStr);
				return;
			}
			else{
				ToastHelper.shortDefaultToast("刷新失败");
			}
		}
	};
	
	@Override
	public void refreshCoin(boolean refreshRet, int changduCoid, int giftCoin){
		BaseActivity.hideWaitCursor();
		if(refreshRet){
//			if(!mIsFirstCreated){
//				String hint = getString(MResource.getIdByName(getApplication(), "string", "xxx"));
//				ToastHelper.shortDefaultToast(hint);
//			}
//			mIsFirstCreated = false;
			
			PayOrderInfoManager.getPayOrderInfoManager().setPayOrderCoinInfo(changduCoid, giftCoin);
			if(tv_coin_text != null){
				tv_coin_text.setText(changduCoid + "");
			}
			if(tv_giftcoin_text != null){
				tv_giftcoin_text.setText(giftCoin + "");
			}
		}
		else{
			ToastHelper.shortDefaultToast("刷新失败");
		}
	}
}
