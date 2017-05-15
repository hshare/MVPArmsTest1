package me.jessyan.mvparms.demo.mvp.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import me.jessyan.mvparms.demo.mvp.ui.fragment.HotsFragment;

public class FragmentAdapter extends FragmentStatePagerAdapter {
    private List<String> mIds;
    private List<String> mTitles;

    public FragmentAdapter(FragmentManager fm, List<String> fragments, List<String> titles) {
        super(fm);
        mIds = fragments;
        mTitles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return HotsFragment.newInstance(mIds.get(position));
    }

    @Override
    public int getCount() {
        return mIds.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }
}
