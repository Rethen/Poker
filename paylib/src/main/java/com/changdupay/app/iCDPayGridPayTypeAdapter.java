package com.changdupay.app;

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
import com.changdupay.widget.IAdapterDataWrap;

public class iCDPayGridPayTypeAdapter extends BaseAdapter {
	private Context mContext = null;
	private Activity mActivity = null;
	private IAdapterDataWrap mAdapterDataWrap = null;
	private Application mApplication = null;
	
	private int mClickPosition = -1;
	public iCDPayGridPayTypeAdapter(Context context, IAdapterDataWrap adapterDataWrap, Application application){
        this.mContext = context;
        this.mAdapterDataWrap = adapterDataWrap;
        mActivity = (Activity)context;
        mApplication = application;
    }
	
	public void setSeclection(int position) 
	{
		mClickPosition = position;
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
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ChoosePayTypeDataWrapper dataWrapper = mAdapterDataWrap.getData(position);
		if(convertView == null){
        	LayoutInflater inflater= ((Activity) mContext).getLayoutInflater();
        	convertView = inflater.inflate(MResource.getIdByName(mApplication, "layout", "ipay_choose_paytype"), null,false);            	
        	TextView label = (TextView)convertView.findViewById(MResource.getIdByName(mApplication, "id", "grid_paytype_title"));
            label.setText(dataWrapper.Name);
            
            ImageView image = (ImageView)convertView.findViewById(MResource.getIdByName(mApplication, "id", "grid_paytype_image"));
            image.setImageResource(MResource.getIdByName(mApplication, "drawable", dataWrapper.ResKey));
            
            ImageView icon = (ImageView)convertView.findViewById(MResource.getIdByName(mApplication, "id", "grid_paytype_subscript_image"));
            if (dataWrapper.KeyFlag)
        	{
            	icon.setVisibility(View.VISIBLE);
            	icon.setImageResource(MResource.getIdByName(mApplication, "drawable", "ipay_recharge_last"));
        	}
            else if (dataWrapper.isHot)
        	{
            	icon.setVisibility(View.VISIBLE);
            	icon.setImageResource(MResource.getIdByName(mApplication, "drawable", "ipay_rm"));
        	}
        	else
        	{
        		icon.setVisibility(View.GONE);
        	}
            convertView.setTag(new ChoosePayTypeViewWrapper(mActivity, convertView));
        }
		else
		{
			ChoosePayTypeViewWrapper wrapper = (ChoosePayTypeViewWrapper)convertView.getTag();
			wrapper.getLabel().setText(dataWrapper.Name);
			wrapper.getImage().setImageResource(MResource.getIdByName(mApplication, "drawable", dataWrapper.ResKey));
			ImageView icon = wrapper.getIcon();
            if (dataWrapper.KeyFlag)
        	{
            	icon.setVisibility(View.VISIBLE);
            	icon.setImageResource(MResource.getIdByName(mApplication, "drawable", "ipay_recharge_last"));
        	}
            else if (dataWrapper.isHot)
        	{
            	icon.setVisibility(View.VISIBLE);
            	icon.setImageResource(MResource.getIdByName(mApplication, "drawable", "ipay_rm"));
        	}
        	else
        	{
        		icon.setVisibility(View.GONE);
        	}
		}
		
        return convertView;
	}

	public class ChoosePayTypeViewWrapper
	{
		TextView mTextView = null;
		ImageView mImageView = null;
		ImageView mIconView = null;
		View mBaseView = null;
		Context mContext = null;
		public ChoosePayTypeViewWrapper(Context context, View base)
		{
			mContext = context;
			mBaseView = base;
		}
		
		public TextView getLabel()
		{
			if (mTextView == null)
			{
				mTextView = (TextView)mBaseView.findViewById(MResource.getIdByName(mApplication, "id", "grid_paytype_title"));
			}
			
			return mTextView;
		}
		
		public ImageView getImage()
		{
			if (mImageView == null)
			{
				mImageView = (ImageView)mBaseView.findViewById(MResource.getIdByName(mApplication, "id", "grid_paytype_image"));
			}
			return mImageView;
		}
		
		public ImageView getIcon()
		{
			if (mIconView == null)
			{
				mIconView = (ImageView)mBaseView.findViewById(MResource.getIdByName(mApplication, "id", "grid_paytype_subscript_image"));
			}
			return mIconView;
		}
	}
}
