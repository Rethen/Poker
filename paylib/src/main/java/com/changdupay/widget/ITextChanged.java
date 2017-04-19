package com.changdupay.widget;

import android.widget.EditText;

public interface ITextChanged {
	void textChanged(EditText edit, String s, int maxLength);
	EditText getEditView();
}
