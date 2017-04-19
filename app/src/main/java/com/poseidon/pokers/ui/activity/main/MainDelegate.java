package com.poseidon.pokers.ui.activity.main;

import com.kymjs.themvp.view.AppDelegate;
import com.poseidon.pokers.R;
import com.poseidon.pokers.databinding.ActivityMainBinding;
import com.poseidon.pokers.ui.viewmodel.page.MainActivityViewModel;

/**
 * Created by 42524 on 2017/4/12.
 */

public class MainDelegate extends AppDelegate<MainActivityViewModel, ActivityMainBinding> {

    @Override
    public int getRootLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void bindViewModel() {
        viewModel = new MainActivityViewModel();
        viewDataBinding.setViewModel(viewModel);
    }

}
