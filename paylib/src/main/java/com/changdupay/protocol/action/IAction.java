package com.changdupay.protocol.action;

import android.app.Activity;

public interface IAction {
	public void doAction(Activity activity, Boolean bQuit);
	public String[] splitParams();
}
