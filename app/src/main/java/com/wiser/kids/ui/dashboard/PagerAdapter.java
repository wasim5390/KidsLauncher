package com.wiser.kids.ui.dashboard;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> list;

    public PagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.list = fragments;
        notifyDataSetChanged();
    }
    @Override
    public void destroyItem(ViewGroup collection, int position, Object view)
    {
        if (list != null) {
            collection.removeView(list.get(position).getView());
        }
    }
    @Override
    public int getItemPosition(Object object) {

        return super.getItemPosition(object);
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

}
