package com.poseidon.pokers.ui.activity.register;


import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.kymjs.themvp.viewmodel.BaseViewModel;
import com.poseidon.pokers.databinding.ActivityRegisterBinding;
import com.poseidon.pokers.ui.activity.BaseActivity;

/**
 * Created by 42524 on 2017/4/10.
 */

public class PhoneNumberRegisterActivity extends BaseActivity<PhoneNumberRegisterDelegate> {



    @Override
    public void actionViewModel(View view, BaseViewModel baseViewModel, int actionType) {

    }


    public  static void  start(Context context){
        Intent intent =new Intent(context,PhoneNumberRegisterActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected Class<PhoneNumberRegisterDelegate> getDelegateClass() {
        return PhoneNumberRegisterDelegate.class;
    }
}
