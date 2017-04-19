package com.changdupay.widget;

import com.changdupay.app.ChoosePayTypeDataWrapper;

public interface IAdapterDataWrap {
	ChoosePayTypeDataWrapper getData(int position);
	Object getObject(int position);
	int    getCount();
}
