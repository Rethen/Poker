package com.poseidon.pokers.ui.activity.login;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.StringUtils;
import com.changdupay.encrypt.MD5Utils;
import com.elvishew.xlog.XLog;
import com.kymjs.themvp.viewmodel.BaseViewModel;
import com.poseidon.pokers.R;
import com.poseidon.pokers.domain.entity.LoginResp;
import com.poseidon.pokers.domain.interactor.DefaultObserver;
import com.poseidon.pokers.domain.interactor.Login;
import com.poseidon.pokers.domain.interactor.UseCase;
import com.poseidon.pokers.ui.activity.BaseActivity;
import com.poseidon.pokers.ui.activity.register.PhoneNumberRegisterActivity;
import com.poseidon.pokers.ui.internal.di.component.DaggerLoginComponent;
import com.poseidon.pokers.ui.internal.di.component.LoginComponent;
import com.poseidon.pokers.ui.internal.di.module.LoginModule;

import javax.inject.Inject;
import javax.inject.Named;

public class LoginActivity extends BaseActivity<LoginDelegate> {


    private String mPwd;

    private String mAccount;

    private String countryCode = "cn";


    @Inject
    @Named("login")
    UseCase loginUseCase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewDelegate.setOnClickListener(this, R.id.btn_login, R.id.tv_forget_password, R.id.btn_register, R.id.country_select_layout);
        LoginComponent loginComponent = DaggerLoginComponent.builder().applicationComponent(getApplicationComponet()).loginModule(new LoginModule()).build();
        loginComponent.inject(this);
    }

    @Override
    protected Class<LoginDelegate> getDelegateClass() {
        return LoginDelegate.class;
    }

    @Override
    public void actionViewModel(View view, BaseViewModel baseViewModel, int actionType) {
        switch (view.getId()) {

            //登录
            case R.id.btn_login:
                login();
                break;

            //忘记密码
            case R.id.tv_forget_password:
                forgetPwd();
                break;

            //注册
            case R.id.btn_register:
                register();
                break;

            //选择国家
            case R.id.country_select_layout:
                selectCountry();
                break;

        }
    }


    private void login() {
        mPwd = viewDelegate.getPwd();
        mAccount = viewDelegate.getAccount();
        if (!verifiAccountAndPwd()) {
            return;
        }
        Login.AccountLoginReq req = new Login.AccountLoginReq();
        req.country = countryCode;
        req.passwd = new MD5Utils().MD5_Encode(mPwd);
        req.pnum = mAccount;
        loginUseCase.execute(new LoginObserver(), req);
    }

    private boolean verifiAccountAndPwd() {
        if (StringUtils.isEmpty(mAccount) || StringUtils.isEmpty(mPwd)) {
            toast("账号或密码不为空");
            return false;
        }
        if (!RegexUtils.isMobileExact(mAccount)) {
            toast("手机格式不对");
            return false;
        }
        return true;
    }

    private void forgetPwd() {
        Toast.makeText(this, "忘记密码", Toast.LENGTH_SHORT).show();
    }

    private void selectCountry() {
        Toast.makeText(this, "选择国家", Toast.LENGTH_SHORT).show();
    }

    private void register() {
        Toast.makeText(this, "点击注册", Toast.LENGTH_SHORT).show();
        PhoneNumberRegisterActivity.start(getApplicationContext());
    }

    private static class LoginObserver extends DefaultObserver<LoginResp> {
        @Override
        public void onNext(LoginResp loginResp) {
            XLog.d("bduss:"+loginResp.userInfo.bduss);
            XLog.d("errorCode:" + loginResp.message.errorCode);
        }
    }

}
