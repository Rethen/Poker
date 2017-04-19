package com.changdupay.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;

import com.changdupay.util.ContextUtil;
import com.changdupay.util.MResource;

public class CustomDialog extends Dialog {
    public CustomDialog(Context context, View view) {
        super(context, MResource.getIdByName(((Activity)ContextUtil.getContext()).getApplication(), "style", "ipay_CustomDialog"));
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(view);
        getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);        
    }
}
