package com.changdupay.app;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.changdupay.util.MResource;
import com.changdupay.util.SkinChangeManager;
import com.changdupay.widget.IAdapterDataWrap;

public class iCDPayChooseMoneyAdapter extends BaseAdapter {
	private Context mContext = null;
	private Activity mActivity = null;
	private IAdapterDataWrap mAdapterDataWrap = null;
	private Application mApplication = null;
	private ArrayList<TextView> mButtons = null;
	private int mInitialSelectedItem = -1;
	
	public iCDPayChooseMoneyAdapter(Context context, IAdapterDataWrap adapterDataWrap, Application application){
        this.mContext = context;
        this.mAdapterDataWrap = adapterDataWrap;
        mActivity = (Activity)context;
        mApplication = application;
        
        mButtons = new ArrayList<TextView>();
    }
	
	public void setItemSelected(int arg0)
	{
		if (mButtons.size() > arg0) {
			mButtons.get(arg0).setSelected(true);
		}
	}
	
	public void setOtherItemsUnSelected(int selectedPos)
	{
		for (int i = 0; i < mButtons.size(); i++)
		{
			mButtons.get(i).setSelected(false);
		}
	}
	
	public void setInitialSelectedItem(int position) {
		mButtons.clear();
		mInitialSelectedItem = position;
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
		if(convertView == null){
        	LayoutInflater inflater= ((Activity) mContext).getLayoutInflater();
        	convertView = inflater.inflate(MResource.getIdByName(mApplication, "layout", "ipay_choose_money_view_item"), null, false);            	
        	convertView.setTag(new ChoosePayTypeViewWrapper(mActivity, convertView));        	
        }
		
		if (parent != null)
    	{
    		addBtns(convertView, position);
    	}
		
		ChoosePayTypeViewWrapper wrapper = (ChoosePayTypeViewWrapper)convertView.getTag();
		if(!TextUtils.isEmpty(dataWrapper.chargeMessage)){
			Spanned spanMsg = Html.fromHtml(String.format("%s\n\n<font color=\"#33CC99\">\n<br>%s</font>", dataWrapper.Name, dataWrapper.chargeMessage));
			wrapper.getButton().setText(spanMsg);
		}
		else{
			wrapper.getButton().setText(dataWrapper.Name);
		}
		if(dataWrapper.giftType > 0){
			ImageView iv_choosemoney_tip = (ImageView)convertView.findViewById(MResource.getIdByName(mApplication, "id", "iv_choosemoney_tip"));
			switch(dataWrapper.giftType){			
				case 1:
					iv_choosemoney_tip.setImageResource(MResource.getIdByName(mApplication, "drawable", "ipay_ico_zheng"));
					iv_choosemoney_tip.setVisibility(View.VISIBLE);
					break;
				case 2:
					iv_choosemoney_tip.setImageResource(MResource.getIdByName(mApplication, "drawable", "ipay_ico_zhe"));
					iv_choosemoney_tip.setVisibility(View.VISIBLE);
					break;
				default:
					break;
			}
		}

		wrapper.getButton().setTag(dataWrapper);
		
		if (mInitialSelectedItem > -1 && position == mInitialSelectedItem) {
			setItemSelected(mInitialSelectedItem);
			mInitialSelectedItem = -1;
		}
		SkinChangeManager.getInstance().onChangeSkin(mActivity, false);
        return convertView;
	}

	private void addBtns(View convertView, int position)
	{
		TextView btnView = (TextView)convertView.findViewById(MResource.getIdByName(mApplication, "id", "choosemoney_gridview_item_btn"));
    	
    	int nSize = mButtons.size();
    	if (nSize == 0)
    	{
    		mButtons.add(position, btnView);
    	}
    	else
    	{
    		if (position < nSize)
    		{
    			if (mButtons.get(position) == null)
    				mButtons.add(position, btnView);
    		}
    		else
    		{
    			mButtons.add(position, btnView);
    		}
    	}
	}
	
	public class ChoosePayTypeViewWrapper
	{
		TextView mBtnView = null;
		View mBaseView = null;
		Context mContext = null;
		public ChoosePayTypeViewWrapper(Context context, View base)
		{
			mContext = context;
			mBaseView = base;
		}
		
		public TextView getButton()
		{
			if (mBtnView == null)
			{
				mBtnView = (TextView)mBaseView.findViewById(MResource.getIdByName(mApplication, "id", "choosemoney_gridview_item_btn"));
				SkinChangeManager.getInstance().changeBackgroundDrawable(mBtnView, false);
			}
			
			return mBtnView;
		}
	}
}
