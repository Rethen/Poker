package com.changdupay.app;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.changdupay.util.MResource;
import com.changdupay.util.SkinChangeManager;
import com.changdupay.widget.IAdapterDataWrap;

public class iCDPayChooseOperatorAdapter extends BaseAdapter {
	private Context mContext = null;
	private Activity mActivity = null;
	private IAdapterDataWrap mAdapterDataWrap = null;
	private Application mApplication = null;
	private int mInitialSelectedItem = -1;
	
	private Map<Integer, ChooseOperatorHolder> mHashMap = new HashMap<Integer, ChooseOperatorHolder>();
	
	public iCDPayChooseOperatorAdapter(Context context, IAdapterDataWrap adapterDataWrap, Application application){
        this.mContext = context;
        this.mAdapterDataWrap = adapterDataWrap;
        mActivity = (Activity)context;
        mApplication = application;
    }
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mAdapterDataWrap.getCount();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mAdapterDataWrap.getObject(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ChoosePayTypeDataWrapper dataWrapper = mAdapterDataWrap.getData(position);
		ChooseOperatorHolder holder = null;
		if(convertView == null){
        	LayoutInflater inflater= ((Activity) mContext).getLayoutInflater();
        	convertView = inflater.inflate(MResource.getIdByName(mApplication, "layout", "ipay_choose_operator_item"), null, false);            	
        	
        	holder = new ChooseOperatorHolder(mActivity, convertView);
        	if (!mHashMap.containsKey(position)) {
        		mHashMap.put(position, holder);
        	}
            convertView.setTag(new ChooseOperatorHolder(mActivity, convertView));
        }

		holder = (ChooseOperatorHolder)convertView.getTag();
		holder.getButton().setText(dataWrapper.Name);
		//holder.getImage().setVisibility(View.INVISIBLE);
		holder.getButton().setTag(dataWrapper);
		
		if (mInitialSelectedItem > -1 && mInitialSelectedItem == position) {
			selectItem(mInitialSelectedItem);
			mInitialSelectedItem = -1;
		}
		SkinChangeManager.getInstance().onChangeSkin(mActivity, false);
        return convertView;
	}

	public void selectItem(int index) {
		if (mHashMap.containsKey(index)) {
			ChooseOperatorHolder holder = mHashMap.get(index);
			if (holder != null) {
				holder.getButton().setSelected(true);
				holder.getImage().setVisibility(View.VISIBLE);
			}
		}
	}
	
	public void unselectItem(int index) {
		if (mHashMap.containsKey(index)) {
			ChooseOperatorHolder holder = mHashMap.get(index);
			if (holder != null) {
				holder.getButton().setSelected(false);
				holder.getImage().setVisibility(View.INVISIBLE);
			}
		}
	}
	
	public void setInitialSelectedItem(int index) {
		mInitialSelectedItem = index;
	}
	
	private class ChooseOperatorHolder
	{
		TextView mTextView = null;
		ImageView mImageView = null;
		View mBaseView = null;
		Context mContext = null;
		public ChooseOperatorHolder(Context context, View base)
		{
			mContext = context;
			mBaseView = base;
		}
		
		public TextView getButton()
		{
			if (mTextView == null)
			{
				mTextView = (TextView)mBaseView.findViewById(MResource.getIdByName(mApplication, "id", "textview"));
				SkinChangeManager.getInstance().changeBackgroundDrawable(mTextView, false);
			}
			
			return mTextView;
		}
		
		public ImageView getImage()
		{
			if (mImageView == null)
			{
				mImageView = (ImageView)mBaseView.findViewById(MResource.getIdByName(mApplication, "id", "image"));
			}
			
			return mImageView;
		}
	}
}
