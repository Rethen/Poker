package com.poseidon.pokers.ui.activity.login;

import com.kymjs.themvp.view.AppDelegate;
import com.poseidon.pokers.R;
import com.poseidon.pokers.databinding.ActivityLoginBinding;
import com.poseidon.pokers.ui.viewmodel.page.LoginViewModel;


/**
 * Created by 42524 on 2017/4/6.
 */

public class LoginDelegate extends AppDelegate<LoginViewModel, ActivityLoginBinding> {


    @Override
    public int getRootLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initWidget() {

    }

    @Override
    protected void bindViewModel() {
        viewModel = new LoginViewModel();
        viewDataBinding.setViewModel(viewModel);
    }

    public String getAccount() {
        return viewModel.account.get();
    }

    public  void  setAccount(String account){
        viewModel.account.set(account);
    }

    public String getPwd() {
        return viewModel.pwd.get();
    }


}
