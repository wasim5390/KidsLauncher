package com.uiu.kids.ui.dashboard;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.List;

public class PagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> list;

    public PagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.list = fragments;
        notifyDataSetChanged();
    }

    public void setSlides(List<Fragment> fragmets){
        this.list.clear();
        notifyDataSetChanged();
        this.list=fragmets;
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
