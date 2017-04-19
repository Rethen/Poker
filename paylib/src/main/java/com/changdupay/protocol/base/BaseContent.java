package com.changdupay.protocol.base;

import com.changdupay.encrypt.Base64;
import com.changdupay.util.Utils;

public class BaseContent implements IBaseContent{

	@Override
	public String getContent() {
		StringBuilder content = new StringBuilder();
		content.append("Content=");
		
		content.append(Utils.replaceMd5Data(this.toBase64String()));
		
		return content.toString();
	}

	@Override
	public String toBase64String() {
		String charset = "utf-8";
		try{
			byte[] bytes = this.toString().getBytes(charset);

			return Base64.encode(bytes);			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
}
