package com.changdupay.protocol.action;

public class ActionManager {
	private static ActionManager mActionManager = null;
	public static ActionManager getInstance()
	{
		if (mActionManager == null)
		{
			mActionManager = new ActionManager();;
		}
		
		return mActionManager;
	}
	
	public ActionManager()
	{
		
	}
	
	public IAction getAction(String strAction)
	{
		ActionParser parser = new ActionParser();
		parser.parser(strAction);
		IAction Action = ActionFactory.getInstance().createAction(parser.getAction(), parser.getParams());
		
		return Action;
	}
}
