package com.poseidon.pokers.ui.viewmodel.page;

import android.databinding.ObservableField;

import com.kymjs.themvp.viewmodel.BaseViewModel;

/**
 * Created by 42524 on 2017/4/10.
 */

public class RegisterViewModel extends BaseViewModel {

    public final ObservableField<String> registerErrorInfo = new ObservableField<>();

    public final ObservableField<String> pwd = new ObservableField<>();

    public final ObservableField<String> captcha = new ObservableField<>();

    public final ObservableField<String> countryCode = new ObservableField<>();

    public final ObservableField<String> phoneNumber = new ObservableField<>();

    public final ObservableField<String> country = new ObservableField<>();


}
