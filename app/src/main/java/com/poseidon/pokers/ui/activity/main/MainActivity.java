package com.poseidon.pokers.ui.activity.main;

import android.view.View;

import com.kymjs.themvp.viewmodel.BaseViewModel;
import com.poseidon.pokers.databinding.ActivityMainBinding;
import com.poseidon.pokers.ui.activity.BaseActivity;

/**
 * Created by 42524 on 2017/4/12.
 */

public class MainActivity extends BaseActivity<MainDelegate> {


    @Override
    public void actionViewModel(View view, BaseViewModel baseViewModel, int actionType) {

    }

    @Override
    protected Class getDelegateClass() {
        return MainDelegate.class;
    }
}
