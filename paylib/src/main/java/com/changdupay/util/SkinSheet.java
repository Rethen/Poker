package com.changdupay.util;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.view.View;

import com.changdupay.app.PayResultActivity;
import com.changdupay.app.WebViewActivity;
import com.changdupay.app.iCDPayCenterActivity;
import com.changdupay.app.iCDPayCenterPhonePayActivity;
import com.changdupay.app.iCDPayCenterSmsPayActivity;
import com.changdupay.app.iCDPayChinaMobileSmsVerifyCodeActivity;
import com.changdupay.app.iCDPayChoaseMoneyWeiXin;
import com.changdupay.app.iCDPayChooseMoneyAlipayActivtiy;
import com.changdupay.app.iCDPayChooseMoneyCreditActivtiy;
import com.changdupay.app.iCDPayChooseMoneyMobileMMActivtiy;
//import com.changdupay.app.iCDPayChooseMoneyMobileMMActivtiy;
import com.changdupay.app.iCDPayChooseMoneyOneClickActivtiy;
import com.changdupay.app.iCDPayChooseMoneyUPPayActivity;
import com.changdupay.app.iCDPayChooseMoneyVisaActivity;
//import com.changdupay.app.iCDPayChooseMoneyUnicomQuickActivity;
//import com.changdupay.app.iCDPayChooseMoneyUnicomShopActivity;
import com.changdupay.app.iCDPayPhoneCardRechargeActivity;
import com.changdupay.app.iCDPayPhoneCardRechargeStep2;
import com.changdupay.app.iCDPaySmsRechargeActivity;

public class SkinSheet {
	private static HashMap<String, Class<? extends SkinSheet>> mSheetMap = null;
	
	protected final boolean mIsIncludeTopBar;
	
	protected final Object[][] mViewSites;
	protected final Object[][] mTopBarSites;
	protected HashMap<Integer, Class<? extends View>[]> mViewPathClassMap;
	
	protected SkinSheet(boolean isIncludeTopBar, Object[][] viewSites) {
		mIsIncludeTopBar = isIncludeTopBar;
		mViewSites = viewSites;

		if(mIsIncludeTopBar){
			mTopBarSites = new Object[][] { 
					{ MResource.getIdByName(((Activity)ContextUtil.getContext()).getApplication(), "id", "title_textview"), Opt.RES_NONE, Opt.TEXT_COLOR },
					{ MResource.getIdByName(((Activity)ContextUtil.getContext()).getApplication(), "id", "title_back"), Opt.RES_NONE, Opt.BG_DRAWABLE },
					{ MResource.getIdByName(((Activity)ContextUtil.getContext()).getApplication(), "id", "title_close"), Opt.RES_NONE, Opt.BG_DRAWABLE }
				};
		}else{
			mTopBarSites = null;
		}
	}
	
	protected final ArrayList<SkinSite> obtainSites() {
		ArrayList<SkinSite> sites = null;
		if (mViewSites != null) {
			sites = new ArrayList<SkinSite>(mViewSites.length + (mIsIncludeTopBar ? mTopBarSites.length : 0));
			
			if(mIsIncludeTopBar){
				for (Object[] value : mTopBarSites) {
					if (value != null && value[0] != null && value[1] != null) {
						sites.add(new SkinSite((Integer) value[0], (Integer) value[1], (Integer) value[2]));
					}
				}
			}
			
			for (Object[] value : mViewSites) {
				if (value != null && value[0] != null && value[1] != null) {
					sites.add(new SkinSite((Integer) value[0], (Integer) value[1], (Integer) value[2]));
				}
			}
		}

		return sites;
	}
	
	public static Class<? extends View>[] getVPath(Class<? extends View>... classes){
		return classes;
	}
	
	protected final Class<? extends View>[] getVPath(int id) {

		return mViewPathClassMap != null ? mViewPathClassMap.get(id) : null;
	}
	
	public static class Opt {
		public static final int RES_NONE					= 0;
		public static final int RES_APPOINT_PARENT			= -1;
		
		/** 为定义 */
		public static final int NONE						= 0x0;
		/** 修改text颜色 */
		public static final int TEXT_COLOR					= 0x1 << 1;
		/** 修改背景颜色 */
		public static final int BG_COLOR					= 0x1 << 2;
		/** 修改背景资源 */
		public static final int BG_DRAWABLE					= 0x1 << 3;
		/** 修改src drawable颜色 */
		public static final int SRC_DRAWABLE				= 0x1 << 4;
		/** 修改src color颜色 */
		public static final int SRC_COLOR					= 0x1 << 5;
		/** 修改compound drawable颜色 */
		public static final int COMPOUND_DRAWABLE			= 0x1 << 6;
		/** 特殊控件 */
		public static final int SPECIFY						= 0x1 << 7;
		/** Drawable */
		public static final int DRAWABLE					= 0x1 << 8;
		/** listview selector */
		public static final int LISTVIEW_SELECTOR			= 0x1 << 9;
		/** radio, check... */
		public static final int COMPOUND_BUTTON				= 0x1 << 10;
		/** ProgressBar, SeekBar */
		public static final int PROGRESS					= 0x1 << 11;
		/** viewGroup */
		public static final int VIEW_GROUP					= 0x1 << 12;
		/** spand, CharacterStyle */
		public static final int CHARACTER_STYLE				= 0x1 << 13;
		
		public static boolean is(int value, int opt){
			return (value & opt) == opt;
		}
	}
	
	protected static class SkinSite {
		public int id;
		public int parent;
		public int opt;

		protected SkinSite(int id, int parent, int opt) {
			this.id = id;
			this.parent = parent;
			this.opt = opt;
		}
	}
	
	/**
	 * 添加View路径, 要把起始节点算进去
	 * 
	 * @param id
	 * @param clazzs
	 */
	protected final void addViewPathClass(int id, Class<? extends View>... clazzs) {
		if (mViewPathClassMap == null) {
			mViewPathClassMap = new HashMap<Integer, Class<? extends View>[]>();
		}

		if (clazzs != null) {
			mViewPathClassMap.put(id, clazzs);
		}
	}
	
	public static String getKey(Class<?> clazz) {
		return clazz != null ? clazz.getSimpleName() : null;
	}
	
	public static HashMap<String, Class<? extends SkinSheet>> getSheetClass() {
		if (mSheetMap == null) {
			mSheetMap = new HashMap<String, Class<? extends SkinSheet>>();
			mSheetMap.put(getKey(iCDPayCenterActivity.class), iCDPayCenterActivitySheet.class);
			mSheetMap.put(getKey(iCDPayChooseMoneyAlipayActivtiy.class), iCDPayChooseMoneyAlipayActivtiySheet.class);
			mSheetMap.put(getKey(iCDPayChooseMoneyCreditActivtiy.class), iCDPayChooseMoneyCreditActivtiySheet.class);
			mSheetMap.put(getKey(iCDPayChooseMoneyUPPayActivity.class), iCDPayChooseMoneyUPPayActivitySheet.class);
			mSheetMap.put(getKey(iCDPayChooseMoneyVisaActivity.class), iCDPayChooseMoneyVisaActivtiySheet.class);
			mSheetMap.put(getKey(iCDPayChooseMoneyOneClickActivtiy.class), iCDPayChooseMoneyOneClickActivtiySheet.class);
//			mSheetMap.put(getKey(iCDPayChooseMoneyUnicomQuickActivity.class), iCDPayChooseMoneyUnicomQuickActivitySheet.class);
//			mSheetMap.put(getKey(iCDPayChooseMoneyUnicomShopActivity.class), iCDPayChooseMoneyUnicomShopActivitySheet.class);
			mSheetMap.put(getKey(iCDPaySmsRechargeActivity.class), iCDPaySmsRechargeActivitySheet.class);
			mSheetMap.put(getKey(iCDPayCenterSmsPayActivity.class), iCDPayCenterSmsPayActivitySheet.class);
			mSheetMap.put(getKey(PayResultActivity.class), PayResultActivitySheet.class);
			mSheetMap.put(getKey(iCDPayPhoneCardRechargeActivity.class), iCDPayPhoneCardRechargeActivitySheet.class);
			mSheetMap.put(getKey(iCDPayPhoneCardRechargeStep2.class), iCDPayPhoneCardRechargeStep2Sheet.class);
			mSheetMap.put(getKey(iCDPayCenterPhonePayActivity.class), iCDPayCenterPhonePayActivitySheet.class);
			mSheetMap.put(getKey(iCDPayChooseMoneyMobileMMActivtiy.class), iCDPayChooseMoneyMobileMMSheet.class);
			mSheetMap.put(getKey(WebViewActivity.class), WebViewActivitySheet.class);
			mSheetMap.put(getKey(iCDPayChinaMobileSmsVerifyCodeActivity.class), iCDPayChinaMobileSmsVerifyCodeActivitySheet.class);
			mSheetMap.put(getKey(iCDPayChoaseMoneyWeiXin.class), iCDPayChoaseMoneyWeiXinSheet.class);
			
		}
		
		return mSheetMap;
	}
	
	public static class iCDPayCenterActivitySheet extends SkinSheet {

		protected iCDPayCenterActivitySheet() {
			super(true, new Object[][] {

			});
		}
	}
	
	public static class iCDPayChooseMoneyActivtiySheet extends SkinSheet {

		protected iCDPayChooseMoneyActivtiySheet() {
			super(true, new Object[][] { 
					{ MResource.getIdByName(((Activity)ContextUtil.getContext()).getApplication(), "id", "choosemoney_next_btn"), Opt.RES_NONE, Opt.BG_DRAWABLE },
					{ MResource.getIdByName(((Activity)ContextUtil.getContext()).getApplication(), "id", "choosemoney_gridview_item_btn"), Opt.RES_NONE, Opt.BG_DRAWABLE }
				});
		}
	}
	
	public static class iCDPayChooseMoneyAlipayActivtiySheet extends iCDPayChooseMoneyActivtiySheet {
		
	}
	
	public static class iCDPayChooseMoneyCreditActivtiySheet extends iCDPayChooseMoneyActivtiySheet {

	}
	public static class iCDPayChooseMoneyUPPayActivitySheet extends iCDPayChooseMoneyActivtiySheet {

	}
	public static class iCDPayChooseMoneyVisaActivtiySheet extends iCDPayChooseMoneyActivtiySheet {

	}
	
	public static class iCDPayChooseMoneyOneClickActivtiySheet extends iCDPayChooseMoneyActivtiySheet {

	}
	
	public static class iCDPayChooseMoneyUnicomQuickActivitySheet extends iCDPayChooseMoneyActivtiySheet {

	}
	
	public static class iCDPayChooseMoneyUnicomShopActivitySheet extends iCDPayChooseMoneyActivtiySheet {

	}
	
	public static class iCDPayChooseMoneyBeanActivtiySheet extends iCDPayChooseMoneyActivtiySheet {

	}
	
	public static class iCDPayChoaseMoneyWeiXinSheet extends iCDPayChooseMoneyActivtiySheet {

	}
	
	public static class iCDPayCenterNopwdPayActivitySheet extends SkinSheet {
		protected iCDPayCenterNopwdPayActivitySheet() {
			super(true, new Object[][] { 
					{ MResource.getIdByName(((Activity)ContextUtil.getContext()).getApplication(), "id", "nopwdpay_pay_btn"), Opt.RES_NONE, Opt.BG_DRAWABLE }
				});
		}
	}
	
	public static class iCDPaySmsRechargeActivitySheet extends SkinSheet {
		protected iCDPaySmsRechargeActivitySheet() {
			super(true, new Object[][] { 
					{ MResource.getIdByName(((Activity)ContextUtil.getContext()).getApplication(), "id", "next_btn"), Opt.RES_NONE, Opt.BG_DRAWABLE },
					{ MResource.getIdByName(((Activity)ContextUtil.getContext()).getApplication(), "id", "choosemoney_gridview_item_btn"), Opt.RES_NONE, Opt.BG_DRAWABLE }
				});
		}
	}
	
	public static class iCDPayCenterSmsPayActivitySheet extends SkinSheet {
		protected iCDPayCenterSmsPayActivitySheet() {
			super(true, new Object[][] { 
					{ MResource.getIdByName(((Activity)ContextUtil.getContext()).getApplication(), "id", "smspay_generatesms_btn"), Opt.RES_NONE, Opt.BG_DRAWABLE }
				});
		}
	}

	public static class PayResultActivitySheet extends SkinSheet {
		protected PayResultActivitySheet() {
			super(true, new Object[][] { 
					{ MResource.getIdByName(((Activity)ContextUtil.getContext()).getApplication(), "id", "recharge"), Opt.RES_NONE, Opt.BG_DRAWABLE }
				});
		}
	}
	
	public static class iCDPayPhoneCardRechargeActivitySheet extends SkinSheet {
		protected iCDPayPhoneCardRechargeActivitySheet() {
			super(true, new Object[][] { 
					
					{ MResource.getIdByName(((Activity)ContextUtil.getContext()).getApplication(), "id", "next_btn"), Opt.RES_NONE, Opt.BG_DRAWABLE },
					{ MResource.getIdByName(((Activity)ContextUtil.getContext()).getApplication(), "id", "textview"), Opt.RES_NONE, Opt.BG_DRAWABLE }
				});
		}
	}
	
	public static class iCDPayPhoneCardRechargeStep2Sheet extends SkinSheet {
		protected iCDPayPhoneCardRechargeStep2Sheet() {
			super(true, new Object[][] { 
					{ MResource.getIdByName(((Activity)ContextUtil.getContext()).getApplication(), "id", "next_btn"), Opt.RES_NONE, Opt.BG_DRAWABLE },
					{ MResource.getIdByName(((Activity)ContextUtil.getContext()).getApplication(), "id", "choosemoney_next_btn"), Opt.RES_NONE, Opt.BG_DRAWABLE }
				});
		}
	}
	
	public static class iCDPayCenterPhonePayActivitySheet extends SkinSheet {
		protected iCDPayCenterPhonePayActivitySheet() {
			super(true, new Object[][] { 
					{ MResource.getIdByName(((Activity)ContextUtil.getContext()).getApplication(), "id", "next_btn"), Opt.RES_NONE, Opt.BG_DRAWABLE }
				});
		}
	}
	
	public static class iCDPayChooseMoneyMobileMMSheet extends SkinSheet {
		protected iCDPayChooseMoneyMobileMMSheet() {
			super(true, new Object[][] { 
					{ MResource.getIdByName(((Activity)ContextUtil.getContext()).getApplication(), "id", "next_btn"), Opt.RES_NONE, Opt.BG_DRAWABLE },
					{ MResource.getIdByName(((Activity)ContextUtil.getContext()).getApplication(), "id", "choosemoney_next_btn"), Opt.RES_NONE, Opt.BG_DRAWABLE }	
			});
		}
	}
	
	public static class WebViewActivitySheet extends SkinSheet {
		protected WebViewActivitySheet() {
			super(true, new Object[][] {

			});
		}
	}
	
	public static class iCDPayChinaMobileSmsVerifyCodeActivitySheet extends SkinSheet {
		protected iCDPayChinaMobileSmsVerifyCodeActivitySheet() {
			super(true, new Object[][] {
					{ MResource.getIdByName(((Activity)ContextUtil.getContext()).getApplication(), "id", "ok"), Opt.RES_NONE, Opt.BG_DRAWABLE }
			});
		}
	}
	
}
