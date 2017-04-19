package com.kymjs.themvp.viewmodel;

import android.databinding.BaseObservable;
import android.view.View;

import com.kymjs.themvp.ViewListenerManager;

import java.util.List;


/**
 * Created by 42524 on 2015/12/15.
 */
public abstract class BaseViewModel extends BaseObservable implements ViewListener {

    protected int viewType;

    protected Class<? extends ViewModelListener> viewModelListenerClazz;


    public BaseViewModel() {

    }

    @Override
    public void action(View view, int actionType) {
        if (viewModelListenerClazz != null) {
            ViewModelListener viewModelListener = ViewListenerManager.getInstance().getViewModelListener(viewModelListenerClazz);
            if (viewModelListener != null) {
                viewModelListener.actionViewModel(view, this, actionType);
            }
        } else {
            List<ViewModelListener> viewModelListenrs = ViewListenerManager.getInstance().find(getClass());
            if (viewModelListenrs != null) {
                for (ViewModelListener viewModelListenr : viewModelListenrs) {
                    if (viewModelListenr != null)
                        viewModelListenr.actionViewModel(view, this, actionType);
                }
            }
        }
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    @Override
    public void onClick(View v) {
        action(v, ItemAction.ONCLICK);
    }

    @Override
    public boolean onLongClick(View v) {
        action(v, ItemAction.LONG_CLICK);
        return true;
    }


    public Class<? extends ViewModelListener> getViewModelListenerClazz() {
        return viewModelListenerClazz;
    }

    public void setViewModelListenerClazz(Class<? extends ViewModelListener> viewModelListenerClazz) {
        this.viewModelListenerClazz = viewModelListenerClazz;
    }

    public interface ViewModelListener {
        void actionViewModel(View view, BaseViewModel baseViewModel, int actionType);
    }

    public interface ItemAction {
        int ONCLICK = 1;
        int LONG_CLICK = 2;
    }

}
