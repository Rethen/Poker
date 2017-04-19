package com.poseidon.pokers.ui.activity.register;

import com.kymjs.themvp.view.AppDelegate;
import com.poseidon.pokers.R;
import com.poseidon.pokers.databinding.ActivityRegisterBinding;
import com.poseidon.pokers.ui.viewmodel.page.RegisterViewModel;

/**
 * Created by 42524 on 2017/4/10.
 */

public class PhoneNumberRegisterDelegate extends AppDelegate<RegisterViewModel,ActivityRegisterBinding> {

    @Override
    public int getRootLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void initWidget() {
        super.initWidget();
    }

    @Override
    protected void bindViewModel() {
      viewModel=new RegisterViewModel();
      viewDataBinding.setViewModel(viewModel);
    }
}
