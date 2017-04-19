package com.poseidon.pokers.ui.viewmodel.page;

import android.databinding.ObservableField;

import com.kymjs.themvp.viewmodel.BaseViewModel;

/**
 * Created by 42524 on 2017/4/7.
 */

public class LoginViewModel extends BaseViewModel {

    public final ObservableField<String> account = new ObservableField<>();

    public final ObservableField<String> pwd = new ObservableField<>();

    public final ObservableField<String> countryDesc = new ObservableField<>();


}
