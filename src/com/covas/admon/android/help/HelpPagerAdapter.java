package com.covas.admon.android.help;

import java.util.ArrayList;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class HelpPagerAdapter extends PagerAdapter{

	private ArrayList<ViewGroup> list;
	
	public HelpPagerAdapter(ArrayList<ViewGroup> list){
		this.list = list;
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		
		container.addView(list.get(position));
		return list.get(position);
	}
	
	@Override
	public void destroyItem(View pager, int position, Object view) {	
		((ViewPager)pager).removeView((View)view);
	}
	
	@Override public void restoreState(Parcelable arg0, ClassLoader arg1) {}
	@Override public Parcelable saveState() { return null; }
	@Override public void startUpdate(View arg0) {}
	@Override public void finishUpdate(View arg0) {}
	
	@Override
	public int getCount() {
		
		return list.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object obj) {

		return view == obj;
	}

}
