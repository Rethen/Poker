package com.changdupay.widget;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class EditTextInputWraper implements TextWatcher, OnEditorActionListener{
	private String mText = "";
	private ITextChanged mView = null;
	private EditText mEdit = null;
	private int mLimitNumber = -1;
	private boolean mIsDoubleType = false; 
	
	public EditTextInputWraper(ITextChanged view, EditText edit, int limitNumber)
	{
		mView = view;
		mEdit = edit;
		mLimitNumber = limitNumber;
	}
	
	public void setMaxLength(int maxLength)
	{
		mLimitNumber = maxLength;
	}
	
	public void setIsDoubleType(boolean bDoubleType) {
		mIsDoubleType = bDoubleType;
	}
	
	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		this.mText = s.toString();
		if(mView != null)
		{
			mView.textChanged(mEdit, this.mText, mLimitNumber);
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		this.mText = s.toString();
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		if (mIsDoubleType) {
			String strText = s.toString();
			if (TextUtils.equals(s, "0"))
			{
				mEdit.setText("");
				mEdit.setSelection(0);
			}
			int index = strText.indexOf('.');
			if (index == 0) {
				mEdit.setText("0" + strText);
				mEdit.setSelection(strText.length() + 1);
			}
			else if (index > 0) {
				int nAfter = strText.length() - index;
				if (nAfter > 3) {
					CharSequence cs = strText.subSequence(0, index + 3);
					mEdit.setText(cs);
					mEdit.setSelection(cs.length());
					return ;
				}
			}
		}
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

}
