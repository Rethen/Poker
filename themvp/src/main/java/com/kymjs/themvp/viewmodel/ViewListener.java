package com.kymjs.themvp.viewmodel;

import android.view.View;

/**
 * Created by 42524 on 2016/1/8.
 */
 interface ViewListener extends View.OnClickListener,View.OnLongClickListener{


    void  action(View view, int actionType);
}
