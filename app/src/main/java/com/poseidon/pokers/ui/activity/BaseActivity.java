package com.poseidon.pokers.ui.activity;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.kymjs.themvp.databind.DataBindActivity;
import com.kymjs.themvp.view.IDelegate;
import com.poseidon.pokers.ui.application.App;
import com.poseidon.pokers.ui.internal.di.component.ApplicationComponent;

/**
 * Created by then on 2016/12/16.
 */

public abstract class BaseActivity<T extends IDelegate> extends DataBindActivity<T> {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public ApplicationComponent getApplicationComponet() {
        App app = (App) getApplication();
        return app.getApplicationComponent();
    }

    protected void toast(CharSequence s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

}
