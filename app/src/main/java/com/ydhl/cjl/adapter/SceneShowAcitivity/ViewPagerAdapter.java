package com.ydhl.cjl.adapter.SceneShowAcitivity;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import java.util.List;

/**
 * Created by Administrator on 2015/5/6.
 */

/**
 * ViewPager适配器
 */
public class ViewPagerAdapter extends PagerAdapter {
    public List<View> mListViews;

    public ViewPagerAdapter(List<View> mListViews) {
        this.mListViews = mListViews;
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView(mListViews.get(position));
    }

    @Override
    public void finishUpdate(View container) {
    }

    @Override
    public int getCount() {
        return mListViews.size();
    }

    @Override
    public Object instantiateItem(View container, int position) {
        ((ViewPager) container).addView(mListViews.get(position), 0);

        return mListViews.get(position);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void startUpdate(View container) {
    }
}