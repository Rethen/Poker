package com.changdupay.net.netengine;

public class NetUrlContent {
    private String sOldUrl="";	//原Url
    private String sHost="";	//原Host
    private String sNewUrl="";	//替换后的Url
    
	public String getsOldUrl() {
		return sOldUrl;
	}
	public void setsOldUrl(String sOldUrl) {
		this.sOldUrl = sOldUrl;
	}
	public String getsHost() {
		return sHost;
	}
	public void setsHost(String sHost) {
		this.sHost = sHost;
	}
	public String getsNewUrl() {
		return sNewUrl;
	}
	public void setsNewUrl(String sNewUrl) {
		this.sNewUrl = sNewUrl;
	}
    
}
