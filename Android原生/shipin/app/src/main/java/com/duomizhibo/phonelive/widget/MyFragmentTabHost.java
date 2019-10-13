package com.duomizhibo.phonelive.widget;

import android.content.Context;
import android.support.v4.app.FragmentTabHost;
import android.util.AttributeSet;

/**
 * tabhost
 * 
 */

public class MyFragmentTabHost extends FragmentTabHost {
	
	private String mCurrentTag;
	
	private String mNoTabChangedTag = "1";
	private Context context;
	//public  boolean isStartingLive;
	//public int clickNum=0;


	public MyFragmentTabHost(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}
	
	@Override
	public void onTabChanged(String tag) {

		if (tag.equals(mNoTabChangedTag)) {
			setCurrentTabByTag(mCurrentTag);
			//MainActivity mainActivity = ((MainActivity)context);
			//if(clickNum==0)
			//{

			//	clickNum++;
			//mainActivity.startLive();
			//}
		} else {
			super.onTabChanged(tag);
			mCurrentTag = tag;
		}


	}


	
	public void setNoTabChangedTag(String tag) {
		this.mNoTabChangedTag = tag;
	}
}
