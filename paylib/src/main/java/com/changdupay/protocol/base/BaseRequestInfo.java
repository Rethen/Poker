package com.changdupay.protocol.base;
import com.changdupay.encrypt.DesUtils;
import com.changdupay.encrypt.MD5Utils;
import com.changdupay.protocol.pay.PayInfoEncryptType;
import com.changdupay.util.PayConfigReader;
/**公共请求信息
 * 
 * @author Administrator
 *
 */
public class BaseRequestInfo implements IBaseRequestInfo{
	//请求头
	public RequestHeader RequestHeader = null;

	//支付信息具体内容
	public IBaseContent Content = null;
	protected int ActionID = 0;
	
	public BaseRequestInfo()
	{
		RequestHeader = new RequestHeader();
	}
	
	public String getSign()
	{
		StringBuilder str = new StringBuilder();
		str.append(ActionID);
		str.append(PayConst.MERCHANTID);
		str.append(PayConst.APPID);
		str.append(PayConst.VER);
		str.append(PayConst.OSTYPE);
		str.append(PayConst.FORMAT);
		str.append(PayConst.ReturnFormat);
		str.append(PayConst.SIGNTYPE);
		str.append(PayConst.HAS_COMPRESSED);
		str.append(PayConst.IPADDRESS);
		str.append(PayConst.SessionID);
		if (Content != null){
			str.append(Content.toBase64String());
		}
		str.append(DeviceInfo.getInstance().toBase64String());
		str.append(PayConfigReader.getInstance().getPayConfigs().LocalKey);
		
		//解码content
//		String contentBase = Content.toBase64String();
//		byte[] content = Base64.decode(contentBase);
//		String strContent = new String(content);
		
		return str.toString();
	}

	public String GetEncryptSign()
	{
		String data = null;
		String signData = getSign();
		switch(PayConst.SIGNTYPE)
		{
		case PayInfoEncryptType.MD5:
			MD5Utils md5 = new MD5Utils();
			data = md5.MD5_Encode(signData);
			md5 = null;
			break;
		case PayInfoEncryptType.RSA:
			//TODO
			break;
		case PayInfoEncryptType.THREEDES:
			data = DesUtils.encryptMode(signData.getBytes(), null).toString();
			break;
		}
		return data;
	}
	
	public byte[] GetPostData()
	{
		RequestHeader.Sign = GetEncryptSign();
		String header = RequestHeader.getHeader();
		String content = Content.getContent();
		String deviceInfo = DeviceInfo.getInstance().getContent();
		String postData = (header + "&" + content + "&" + deviceInfo);
		
		return postData.getBytes();
	}
	
	public int getActionID() {
		return ActionID;
	}
}
