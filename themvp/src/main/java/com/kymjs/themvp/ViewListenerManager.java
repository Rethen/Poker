package com.kymjs.themvp;


import com.kymjs.themvp.viewmodel.BaseViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by then on 2016/4/9.
 */
public class ViewListenerManager {


    private Map<Class<? extends BaseViewModel.ViewModelListener>, BaseViewModel.ViewModelListener> viewModelListnerContainer = new HashMap<>();

    private Map<Class<? extends BaseViewModel>, Class<? extends BaseViewModel.ViewModelListener>[]> viewModelMapping = new HashMap<>();


    private static ViewListenerManager ourInstance = new ViewListenerManager();

    public static ViewListenerManager getInstance() {
        return ourInstance;
    }

    private ViewListenerManager() {
    }



    public void initViewModelMap(Class<? extends BaseViewModel> modelAdapterClazz, Class<? extends BaseViewModel.ViewModelListener>... viewModelListenerClazzes) {
        viewModelMapping.put(modelAdapterClazz, viewModelListenerClazzes);
    }


    public void reigester(BaseViewModel.ViewModelListener viewModelListner) {
        if (viewModelListner != null) {
            viewModelListnerContainer.put(viewModelListner.getClass(), viewModelListner);
        }
    }

    public void unreigester(BaseViewModel.ViewModelListener viewModelListner) {
        if (viewModelListner != null) {
            viewModelListnerContainer.remove(viewModelListner.getClass());
        }
    }


    public List<BaseViewModel.ViewModelListener> find(Class<? extends BaseViewModel> clazz) {
        List<BaseViewModel.ViewModelListener> tempViewModelListners = null;
        if (viewModelMapping.get(clazz) != null) {
            tempViewModelListners = new ArrayList<>();
            Class<? extends BaseViewModel.ViewModelListener>[] viewModelListenerClazzes = viewModelMapping.get(clazz);
            for (Class<? extends BaseViewModel.ViewModelListener> tempClass : viewModelListenerClazzes) {
                tempViewModelListners.add(viewModelListnerContainer.get(tempClass));
            }
        }
        return tempViewModelListners;
    }

    public BaseViewModel.ViewModelListener getViewModelListener(Class<? extends BaseViewModel.ViewModelListener> vmlClazz) {
        return viewModelListnerContainer.get(vmlClazz);

    }


}
