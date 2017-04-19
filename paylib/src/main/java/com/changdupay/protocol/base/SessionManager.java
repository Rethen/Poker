package com.changdupay.protocol.base;

import java.util.HashMap;
import java.util.Map;

public class SessionManager {
	private static SessionManager mSessionManager = null;
	
	private static Map<Integer, PostStruct> sessionMap = new HashMap<Integer,PostStruct>(); 
	public static SessionManager getInstance()
	{
		if (mSessionManager == null)
		{
			mSessionManager = new SessionManager();
		}
		
		return mSessionManager;
	}
	
	public SessionManager()
	{
		
	}
	
	public void addSession(int sessionid, PostStruct ps)
	{
		sessionMap.put(sessionid, ps);
	}
	
	public void removeSession(int sessionid)
	{
		sessionMap.remove(sessionid);
	}
	
	public PostStruct getSessionData(int sessionid)
	{
		if (sessionMap.containsKey(sessionid))
		{
			return sessionMap.get(sessionid);
		}
		
		return null;
	}
}
