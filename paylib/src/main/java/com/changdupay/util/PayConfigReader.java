package com.changdupay.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.util.Arrays;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.util.EncodingUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.changdupay.encrypt.Base64;
import com.changdupay.protocol.base.PayConst;
import com.changdupay.util.PayConfigs.ActionConfig;
import com.changdupay.util.PayConfigs.AmountLimit;
import com.changdupay.util.PayConfigs.AvoidPasswrod;
import com.changdupay.util.PayConfigs.Category;
import com.changdupay.util.PayConfigs.Channel;
import com.changdupay.util.PayConfigs.ChannelList;
import com.changdupay.util.PayConfigs.ClientConfig;
import com.changdupay.util.PayConfigs.DefaultAmountList;
import com.changdupay.util.PayConfigs.Merchandise;
import com.changdupay.util.PayConfigs.MerchandiseList;
import com.changdupay.util.PayConfigs.PayAmount;
import com.changdupay.util.PayConfigs.RechargeAmount;

public class PayConfigReader {
	private PayConfigs mPayConfigs = null;

	private static PayConfigReader mPayConfigReader = null;
	private double mClientVer = 0.0;
	private String mDownloadUrl = null;
	
	public double mParserVer = 2.0;
	public int mSessionTime = 24;
	public AvoidPasswrod mAvoidPasswrod= null;
	public ClientConfig mClientConfig = null;
	public ActionConfig mActionConfig = null;
	public RechargeAmount mRechargeAmount = null;
	public PayAmount mPayAmount = null;
	public DefaultAmountList mDefaultAmountList = null;
//	public TMobile mTMobile = null;
	
	public MerchandiseList mMerchandiseList = null;
	public PayConfigReader()
	{
		mPayConfigs = new PayConfigs();
		mMerchandiseList = mPayConfigs.new MerchandiseList();
		
		mAvoidPasswrod = mPayConfigs.new AvoidPasswrod();
		mClientConfig = mPayConfigs.new ClientConfig();
		mActionConfig = mPayConfigs.new ActionConfig();
		mRechargeAmount = mPayConfigs.new RechargeAmount();
		mPayAmount = mPayConfigs.new PayAmount();
		mDefaultAmountList = mPayConfigs.new DefaultAmountList();
//		mTMobile = mPayConfigs.new TMobile();
	}
	
	public static PayConfigReader getInstance()
	{
		if (mPayConfigReader == null)
		{
			mPayConfigReader = new PayConfigReader();
		}
		
		return mPayConfigReader;
	}
	
	public PayConfigs getPayConfigs()
	{
		return mPayConfigs;
	}
	
	//获取渠道信息
	public Category getPayCategoryByCode(int code)
	{
		Merchandise merchandise = getMerchandiseById(PayConst.MERCHANDISEID);
		ChannelList channelList = merchandise.mlChannelList.get(0);
		
		Category item = null;
		for (int i = 0; i < channelList.mlCategoryList.size(); i++)
		{
			Category category = channelList.mlCategoryList.get(i);
			if (category.Code == code)
			{
				item = category;
				break;
			}
		}
		
		return item;
	}
	
	public Channel getPayChannelItemByPayCodeType(int payCode, int payType) {
		try
		{
			Merchandise merchandise = getMerchandiseById(PayConst.MERCHANDISEID);
			ChannelList channelList = merchandise.mlChannelList.get(0);
			
	//		Channel item = null;
			for (int i = 0; i < channelList.mlCategoryList.size(); i++)
			{
				Category category = channelList.mlCategoryList.get(i);
				if (category.Code != payCode || category.Channels == null 
						|| category.Channels.size() == 0) { 
					continue;
				}
				if(payType == -1){
					return category.Channels.get(0);
				}
				Channel item = null;
				for (int j = 0; j < category.Channels.size(); j++)
				{
					item = category.Channels.get(j);
					if (item.PayType == payType) {
						return item;
					}
				}			
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Log.e("getPayChannelItemByPayCodeType", "payCode:" + payCode + "payId:" + payType);
		}
		
		return null;
	}

	public Category getPayCategoryByPayType(int payType) {
		Merchandise merchandise = getMerchandiseById(PayConst.MERCHANDISEID);
		ChannelList channelList = merchandise.mlChannelList.get(0);
		
		Channel item = null;
		for (int i = 0; i < channelList.mlCategoryList.size(); i++)
		{
			Category category = channelList.mlCategoryList.get(i);
			for (int j = 0; j < category.Channels.size(); j++)
			{
				item = category.Channels.get(j);
				if (item.PayType == payType) {
					return category;
				}
			}
		}
		
		return null;
	}

	public Channel getPayChannelItemPairs(int nPayType, int nPayId)
	{
		Merchandise merchandise = getMerchandiseById(PayConst.MERCHANDISEID);
		ChannelList channelList = merchandise.mlChannelList.get(0);
		
		Channel item = null;
		Boolean bBreak = false;
		for (int i = 0; i < channelList.mlCategoryList.size(); i++)
		{
			if (bBreak)
				break;
			Category category = channelList.mlCategoryList.get(i);
			for (int j = 0; j < category.Channels.size(); j++)
			{
				Channel tempitem = category.Channels.get(j);
				if (tempitem.PayType == nPayType && tempitem.PayId == nPayId)
				{
					item = tempitem;
					bBreak = true;
					break;
				}
			}
		}

		return item;
	}
	
	//标记无用的充值渠道
	public void tokenPayChannelItemPairs(int nPayType, int nPayId)
	{
		Merchandise merchandise = getMerchandiseById(PayConst.MERCHANDISEID);
		ChannelList channelList = merchandise.mlChannelList.get(0);
		
		Channel item = null;
		Boolean bBreak = false;
		for (int i = 0; i < channelList.mlCategoryList.size(); i++)
		{
			if (bBreak)
				break;
			Category category = channelList.mlCategoryList.get(i);
			for (int j = 0; j < category.Channels.size(); j++)
			{
				Channel tempitem = category.Channels.get(j);
				if (tempitem.PayType == nPayType && tempitem.PayId == nPayId)
				{
					item = tempitem;
					item.Tokened = true;
					bBreak = true;
					break;
				}
			}
		}
	}
	
	//获取商户信息
	public Merchandise getMerchandiseById(long Id)
	{
		if (mMerchandiseList == null
		|| mMerchandiseList.mlMerchandises == null
		|| mMerchandiseList.mlMerchandises.size() == 0)
		{
			parseConfigXml(null);
		}

		Merchandise item = null;
		//修改为按商品id来取（若要畅读币信息，则取对应的商品信息）
		if (PayConst.MERCHANDISEID == Id) {
			for (int i = 0; i < mMerchandiseList.mlMerchandises.size(); i++)
			{
				Merchandise merchandise = mMerchandiseList.mlMerchandises.get(i);
				if (merchandise.Id == PayConst.MERCHANDISEID)
				{
					item = merchandise;
					break;
				}
			}
		}
		else {
			for (int i = 0; i < mMerchandiseList.mlMerchandises.size(); i++)
			{
				Merchandise merchandise = mMerchandiseList.mlMerchandises.get(i);
				if (merchandise.Id == Id)
				{
					item = merchandise;
					break;
				}
			}
		}
		
		
		return item;
	}
	
	//获取商户渠道信息
	public ChannelList getChannelListByMerchandiseId(long Id)
	{
		ChannelList channelList = null;
		Merchandise merchandise = getMerchandiseById(Id);
		if(merchandise != null && merchandise.mlChannelList.size() > 0){
			channelList = merchandise.mlChannelList.get(0);
		}
		
		return channelList;
	}
	
	public Boolean isOverPayChannelLimit(String payMoney, int typeid)
	{
		Boolean ret = true;

		Merchandise merchandise = getMerchandiseById(PayConst.MERCHANDISEID);
		ChannelList channelList = merchandise.mlChannelList.get(0);
		
		Boolean bBreak = false;
		for (int i = 0; i < channelList.mlCategoryList.size(); i++)
		{
			if (bBreak)
				break;
			Category category = channelList.mlCategoryList.get(i);
			for (int j = 0; j < category.Channels.size(); j++)
			{
				if (bBreak)
					break;
				Channel channel = category.Channels.get(j);
				if (channel.PayId == typeid)
				{
					for (int m = 0; m < channel.AmountLimits.size(); m++)
					{
						AmountLimit amountLimit = channel.AmountLimits.get(m);
						if (((double)amountLimit.Premium == amountLimit.Premium))
						{
							ret = false;
							bBreak = true;
							break;
						}
					}
				}
			}
		}
		
		return ret;
	}
	
	public String getPayCategoryResIdByCode(Category payChannel, int code)
	{
		String resid = "";
		switch(code){
			case 2: //手机充值卡
				payChannel.ResKey = "ipay_simcard";
				break;
			case 3: //支付宝
				payChannel.ResKey = "ipay_alipay";
				break;
			case 4: //财付通
				payChannel.ResKey = "ipay_oneclick";
				break;
			case 5: //话费充值
				payChannel.ResKey = "ipay_sms";
				break;
			case 6: //储蓄卡
				payChannel.ResKey = "ipay_credit";
				break;
			case 7:
				payChannel.ResKey = "ipay_visa";
				break;
			case 10: //联通沃商店
				payChannel.ResKey = "ipay_unicom_wo";	
				break;
			case 11: //移动MM
				payChannel.ResKey = "ipay_mobile_mm";
				break;	
			case 13:
				payChannel.ResKey = "ipay_uppay";
				break;
			case 14:
				payChannel.ResKey = "ipay_mobile_wx";
				break;
			default:
				payChannel.ResKey = "ipay_web";
				break;		
		}		
		return resid;
	}
	
	
	
	/**
	 * 显示充值channel图标的resid， 写死影响不大
	 * <br>Created 2014年8月6日 上午10:32:51
	 * @param payChannelitem
	 * @param payType
	 * @return
	 * @author       daiyf
	*/
	private String getPhoneCardResIdByPayType(Channel payChannelitem, int payType)
	{
		String resid = "";
		if (payType == 10013 || payType == 20013)
		{
			payChannelitem.ResKey = "ipay_mobile_icon";
		}
		else if (payType == 10014)
		{
			payChannelitem.ResKey = "ipay_unicom_icon";
		}
		else if (payType == 10015)
		{
			payChannelitem.ResKey = "ipay_telcom_icon";
		}
		
		return resid;
	}

	public Boolean parseConfigXml(String raw)
	{
		if (TextUtils.isEmpty(raw)){ //mMerchandiseList.mlMerchandises.size() > 0 && 
			return true;
		}
		
		if (!TextUtils.isEmpty(raw))
		{
			//写文件
			String[] arrStr = raw.split("\\|");
			if(arrStr.length == 4 && arrStr[0].equals("1")){
				writeToFile(arrStr[2]);
				raw = arrStr[2];
			}
			else{
				if (mMerchandiseList.mlMerchandises.size() > 0){
					return true;
				}
				else{
					raw = readFromFile(Const.PAY_DEFAULT_CONFIG_FILENAME);
				}
			}
		}
		else
		{
			raw = readFromFile(Const.PAY_DEFAULT_CONFIG_FILENAME);
		}
		
		if (TextUtils.isEmpty(raw)){
			return false;
		}
		
		raw = new String(Base64.decode(raw));

		return decodeXMLConfig(raw);
	}
	
	private Boolean decodeXMLConfig(String raw) {		
		Boolean bResult = false;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		
		try
		{
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new InputSource(new StringReader(raw))); 
			Element root = doc.getDocumentElement();
			
			//Android和IOS新版本配置文件将分开.申请不同的AppID
			NodeList CommonConfigList = root.getElementsByTagName("CommonConfig");
			Element CommonConfig = (Element)CommonConfigList.item(0);
			
			//配置文件解析器版本号
			NodeList ParserVers = CommonConfig.getElementsByTagName("ParserVer");
			Element ParserVer = (Element)ParserVers.item(0);
			mParserVer = Double.parseDouble(ParserVer.getAttribute("Value"));
			
			//会话有效时长,单位小时
			NodeList SessionTimes = CommonConfig.getElementsByTagName("SessionTime");
			Element SessionTime = (Element)SessionTimes.item(0);
			mSessionTime = Integer.parseInt(SessionTime.getAttribute("Value"));
			
			// 小额免密状态,上限金额
			NodeList AvoidPasswrods = CommonConfig.getElementsByTagName("AvoidPasswrod");
			Element AvoidPasswrod = (Element)AvoidPasswrods.item(0);
			mAvoidPasswrod.Enable = Integer.parseInt(AvoidPasswrod.getAttribute("Enable"));
			mAvoidPasswrod.UpperLimit = Double.parseDouble(AvoidPasswrod.getAttribute("AmountUpper"));
			
			//活动配置
			Element actionConfig  = (Element)root.getElementsByTagName("ActionConfig").item(0);
			mActionConfig.PageSize = Integer.parseInt(actionConfig.getAttribute("PageSize"));
			mActionConfig.LatelyPageSize = Integer.parseInt(actionConfig.getAttribute("LatelyPageSize"));
			
			//客户端信息
			Element Clients  = (Element)root.getElementsByTagName("Client").item(0);
			mClientConfig.Ver = Double.parseDouble(Clients.getAttribute("Ver"));
			mClientConfig.DownloadUrl = Clients.getAttribute("DownloadUrl");
			mClientVer = mClientConfig.Ver;
			Utils.setStringValue(Const.ConfigKeys.ClientVer, String.valueOf(mClientVer));
			mDownloadUrl = mClientConfig.DownloadUrl;
			Utils.setStringValue(Const.ConfigKeys.UpgradeUrl, mDownloadUrl);
			
			//充值金额配置[默认金额,最小金额,最大金额
			NodeList rechargeAmounts = root.getElementsByTagName("Recharge");
			Element rechargeAmount = (Element)rechargeAmounts.item(0);
			mRechargeAmount.DefaultAmount = Double.parseDouble(rechargeAmount.getAttribute("AmountDefault"));
			mRechargeAmount.LowerLimit = Double.parseDouble(rechargeAmount.getAttribute("AmountLower"));
			mRechargeAmount.UpperLimit = Double.parseDouble(rechargeAmount.getAttribute("AmountUpper"));
			
			//支付金额配置[默认金额,最小金额,最大金额
			NodeList payAmounts = root.getElementsByTagName("Payment");
			Element payAmount = (Element)payAmounts.item(0);
			mPayAmount.DefaultAmount = Double.parseDouble(payAmount.getAttribute("AmountDefault"));
			mPayAmount.LowerLimit = Double.parseDouble(payAmount.getAttribute("AmountLower"));
			mPayAmount.UpperLimit = Double.parseDouble(payAmount.getAttribute("AmountUpper"));

			//<!--  消费默认金额,下限,上限 -->
			mDefaultAmountList.AmountLimits.clear();
			NodeList DefaultAmountListNodeList = root.getElementsByTagName("DefaultAmountList");
			for(int index = 0; index < DefaultAmountListNodeList.getLength(); index++)
			{
				Element DefaultAmountListElement = (Element)DefaultAmountListNodeList.item(index);
				NodeList AmountLimitNodeList = DefaultAmountListElement.getElementsByTagName("AmountLimit");
				for (int pos = 0; pos < AmountLimitNodeList.getLength(); pos++)
				{
					Element AmountLimitElement = (Element)AmountLimitNodeList.item(pos);
					AmountLimit amountLimit = mPayConfigs.new AmountLimit();
					amountLimit.Value = Integer.parseInt(AmountLimitElement.getAttribute("Value")); 
					if (AmountLimitElement.getAttribute("Premium") != null
						&& !TextUtils.equals(AmountLimitElement.getAttribute("Premium"), ""))
						amountLimit.Premium = Integer.parseInt(AmountLimitElement.getAttribute("Premium")); 
					if (AmountLimitElement.getAttribute("ChargeMessage") != null
							&& !TextUtils.equals(AmountLimitElement.getAttribute("ChargeMessage"), ""))
						amountLimit.chargeMessage = AmountLimitElement.getAttribute("ChargeMessage"); 
					if (AmountLimitElement.getAttribute("GiftType") != null
							&& !TextUtils.equals(AmountLimitElement.getAttribute("GiftType"), ""))
						amountLimit.giftType = Integer.parseInt(AmountLimitElement.getAttribute("GiftType")); 
					if (AmountLimitElement.getAttribute("Text") != null
							&& !TextUtils.equals(AmountLimitElement.getAttribute("Text"), ""))
						amountLimit.Text = AmountLimitElement.getAttribute("Text");
					if (AmountLimitElement.getAttribute("ShopItemId") != null
							&& !TextUtils.equals(AmountLimitElement.getAttribute("ShopItemId"), ""))
						amountLimit.ShopItemId = AmountLimitElement.getAttribute("ShopItemId");
			
					mDefaultAmountList.AmountLimits.add(amountLimit);
				}
			}
			//<!--  通用配置结束  -->
			Element config = root;
			NodeList MerchandiseLists = config.getElementsByTagName("MerchandiseList");
			//当前支持渠道
			mMerchandiseList.mlMerchandises.clear();
			for(int i = 0; i < MerchandiseLists.getLength(); i++)
			{
				Element itemRoot = (Element)MerchandiseLists.item(i);
				NodeList MerchandiseNodeList = itemRoot.getElementsByTagName("Merchandise");
				for(int j = 0; j < MerchandiseNodeList.getLength(); j++)
				{
					Merchandise merchandise = mPayConfigs.new Merchandise();
					
					Element MerchandiseElement = (Element)MerchandiseNodeList.item(j);
					merchandise.Name = MerchandiseElement.getAttribute("Name");
					merchandise.Id = Long.parseLong(MerchandiseElement.getAttribute("Id"));
					if (MerchandiseElement.getAttribute("Rate") != null
							&& !TextUtils.equals(MerchandiseElement.getAttribute("Rate"), ""))
						merchandise.Rate = (int)Double.parseDouble(MerchandiseElement.getAttribute("Rate"));
					
					NodeList ChannelListNodeList = MerchandiseElement.getElementsByTagName("ChannelList");
					for(int m = 0; m < ChannelListNodeList.getLength(); m++)
					{
						ChannelList channelList = mPayConfigs.new ChannelList();

						Element ChannelListElement = (Element)ChannelListNodeList.item(m);
						NodeList CategoryNodeList = ChannelListElement.getElementsByTagName("Category");
						int categoryCode = 0;
						for(int n = 0; n < CategoryNodeList.getLength(); n++)
						{
							Element CategoryElement = (Element)CategoryNodeList.item(n);
							categoryCode = Integer.parseInt(CategoryElement.getAttribute("Code"));
							if(categoryCode == PayConst.SINGLE_BOOK_NO_SHOW_CODE 
									&& (!PayConst.isShowMobileMM)){  //单行本不显示移动MM
								continue;
							}							
							
							Category category = mPayConfigs.new Category();
	
							category.Code = Integer.parseInt(CategoryElement.getAttribute("Code"));
							category.Name = CategoryElement.getAttribute("Name");
							if (CategoryElement.getAttribute("ViewType") != null
									&& !TextUtils.equals(CategoryElement.getAttribute("ViewType"), ""))
								category.ViewType = Integer.parseInt(CategoryElement.getAttribute("ViewType"));
							getPayCategoryResIdByCode(category, category.Code);
							category.SortId = Utils.getIntValue(category.Name);
							
							if (CategoryElement.getAttribute("IconType") != null
									&& !TextUtils.equals(CategoryElement.getAttribute("IconType"), "")){
								category.IconType = Integer.parseInt(CategoryElement.getAttribute("IconType"));
							}
							
							NodeList ChannelNodeList= CategoryElement.getElementsByTagName("Channel");
							for(int k = 0; k < ChannelNodeList.getLength(); k++)
							{
								Channel channel = mPayConfigs.new Channel();
								
								Element ChannelElement = (Element)ChannelNodeList.item(k);
								channel.Name = ChannelElement.getAttribute("Name");
								getPhoneCardResIdByPayType(channel, channel.PayType);
								channel.Descript = ChannelElement.getAttribute("Descript");
								channel.PayType = Integer.parseInt(ChannelElement.getAttribute("PayType"));
								channel.PayId = Integer.parseInt(ChannelElement.getAttribute("PayId"));
								channel.ViewType = Integer.parseInt(ChannelElement.getAttribute("ViewType"));
								if (ChannelElement.getAttribute("AmountLimit") != null
										&& !TextUtils.equals(ChannelElement.getAttribute("AmountLimit"), ""))
									channel.AmountLimit = Integer.parseInt(ChannelElement.getAttribute("AmountLimit"));
								if (channel.AmountLimit != 0)
								{
									NodeList AmountLimitNodeList = ChannelElement.getElementsByTagName("AmountLimit");
									for (int pos = 0; pos < AmountLimitNodeList.getLength(); pos++)
									{
										Element AmountLimitElement = (Element)AmountLimitNodeList.item(pos);
										AmountLimit amountLimit = mPayConfigs.new AmountLimit();
										amountLimit.Value = (int)Double.parseDouble(AmountLimitElement.getAttribute("Value")); 
										if (AmountLimitElement.getAttribute("Premium") != null
											&& !TextUtils.equals(AmountLimitElement.getAttribute("Premium"), ""))
											amountLimit.Premium = (int)Double.parseDouble(AmountLimitElement.getAttribute("Premium")); 
										if (AmountLimitElement.getAttribute("ChargeMessage") != null
												&& !TextUtils.equals(AmountLimitElement.getAttribute("ChargeMessage"), ""))
												amountLimit.chargeMessage = AmountLimitElement.getAttribute("ChargeMessage");
										if (AmountLimitElement.getAttribute("GiftType") != null
												&& !TextUtils.equals(AmountLimitElement.getAttribute("GiftType"), ""))
											amountLimit.giftType = Integer.parseInt(AmountLimitElement.getAttribute("GiftType")); 										
										if (AmountLimitElement.getAttribute("Text") != null
												&& !TextUtils.equals(AmountLimitElement.getAttribute("Text"), ""))
											amountLimit.Text = AmountLimitElement.getAttribute("Text");
										if (AmountLimitElement.getAttribute("ShopItemId") != null
												&& !TextUtils.equals(AmountLimitElement.getAttribute("ShopItemId"), ""))
											amountLimit.ShopItemId = AmountLimitElement.getAttribute("ShopItemId");
									
										channel.AmountLimits.add(amountLimit);
									}
								}
								else
								{
									//手动处理									
									for (int index = 0; index < mDefaultAmountList.AmountLimits.size(); index++)
									{
										AmountLimit amountLimit = mPayConfigs.new AmountLimit();
										amountLimit.Value = mDefaultAmountList.AmountLimits.get(index).Value;
										amountLimit.Premium = mDefaultAmountList.AmountLimits.get(index).Premium;
										amountLimit.chargeMessage = mDefaultAmountList.AmountLimits.get(index).chargeMessage;
										amountLimit.giftType = mDefaultAmountList.AmountLimits.get(index).giftType;
										amountLimit.Text = mDefaultAmountList.AmountLimits.get(index).Text;
										amountLimit.ShopItemId = mDefaultAmountList.AmountLimits.get(index).ShopItemId;
										
										channel.AmountLimits.add(amountLimit);
									}
								}
								
								//MobileType 运营商号段
								String mobileType = ChannelElement.getAttribute("MobileType");
								if(!TextUtils.isEmpty(mobileType)){
									channel.MobileTypeList.addAll(Arrays.asList(mobileType.split(",")));
								}
								
								category.Channels.add(channel);
							}
							channelList.mlCategoryList.add(category);
						}
						merchandise.mlChannelList.add(channelList);
					}
					mMerchandiseList.mlMerchandises.add(merchandise);
				}
			}
			
//			//运营商号段描述
//			mTMobile.Items.clear();
//			NodeList tMobiles = config.getElementsByTagName("T-Mobile");
//			Element tMobile = (Element)tMobiles.item(0);
//			Element tChinaMobile = (Element)tMobile.getElementsByTagName("ChinaMobile").item(0);
//			TMobileItem chinaMobile = mPayConfigs.new TMobileItem();
//			chinaMobile.SimOperator = Const.SimOperator.Mobile1;
//			String strValue = tChinaMobile.getAttribute("Value");
//			chinaMobile.mlValue.addAll(Arrays.asList(strValue.split(",")));
//			mTMobile.Items.add(chinaMobile);
//			
//			Element tUniCom = (Element)tMobile.getElementsByTagName("UniCom").item(0);
//			TMobileItem unicom = mPayConfigs.new TMobileItem();
//			unicom.SimOperator = Const.SimOperator.Unicom;
//			strValue = tUniCom.getAttribute("Value");
//			unicom.mlValue.addAll(Arrays.asList(strValue.split(",")));
//			mTMobile.Items.add(unicom);
//			
//			Element tTelCom  = (Element)tMobile.getElementsByTagName("TelCom").item(0);
//			TMobileItem telCom  = mPayConfigs.new TMobileItem();
//			telCom.SimOperator = Const.SimOperator.Telcom;
//			strValue = tTelCom.getAttribute("Value");
//			telCom.mlValue.addAll(Arrays.asList(strValue.split(",")));
//			mTMobile.Items.add(telCom);
			
			bResult = true;
		}
		catch (ParserConfigurationException e) {
	         // TODO Auto-generated catch block
	         e.printStackTrace();
	     } catch (SAXException e) {
	         // TODO Auto-generated catch block
	         e.printStackTrace();
	     } catch (IOException e) {
	         // TODO Auto-generated catch block
	         e.printStackTrace();
	     }
	     catch (Exception e){
	    	 e.printStackTrace();
	     }
		
		return bResult;
	}

	public void writeToFile(String raw)
	{
		  try
		  { 
	          /* openFileOutput (String  name, int mode)中mode是打开模式，0或者MODE_PRIVATE为缺省模式， MODE_APPEND表示在文件后增加，MODE_WORLD_READABLE和MODE_WORLD_WRITEABLE是控制权限  */ 
			  OutputStreamWriter out = new OutputStreamWriter(ContextUtil.getContext().openFileOutput(Const.PAY_DEFAULT_CONFIG_FILENAME, Context.MODE_WORLD_READABLE));            
	          out.write(raw); 
	          out.close();    
		  }
		  catch(Throwable t)
		  {
			  
		  }
	}
	
	public String readFromFile(String filename)
	{
		String txt = null;
		try
		{
			FileInputStream in = ContextUtil.getContext().openFileInput(filename);
			if (in != null)
			{
				int length = in.available();  
				byte [] buffer = new byte[length]; 
				in.read(buffer);
				in.close(); 
				txt = EncodingUtils.getString(buffer, "UTF-8"); 
			}
		}
		catch(Throwable t)
		{
			  
		}

		return txt;
	}
	
	public String getConfigBase64Content()
	{
		String content = "xx-xx";

		String fileContent = readFromFile(Const.PAY_DEFAULT_CONFIG_FILENAME);
		if (fileContent != null)
		{
			content = fileContent;
		}

		return content;
	}
	
	//客户端是否升级
	public Boolean needUpgrade()
	{
		Boolean bUpgrade = false;
		double oldVersion = Utils.getVersionCode(ContextUtil.getContext());

		if (oldVersion < mClientVer)
		{
			bUpgrade = true;
		}
		return bUpgrade; 
	}
	
	//获取客户端更新地址
	public String getUpgradeUrl()
	{
		if (!TextUtils.isEmpty(mDownloadUrl))
		{
			return mDownloadUrl;
		}
		else
		{
			return Utils.getStringValue(Const.ConfigKeys.UpgradeUrl);
		}
	}

	public Boolean parseLocalConfigXml() {
		
		String raw = readFromFile(Const.PAY_DEFAULT_CONFIG_FILENAME);
		
		if (TextUtils.isEmpty(raw)){
			return false;
		}
		
		raw = new String(Base64.decode(raw));

		return decodeXMLConfig(raw);
	}
}