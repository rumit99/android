package com.trendingnews.adapter;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.trendingnews.fragment.FragmentMultiMedia;
import com.trendingnews.fragment.FragmentPremium;

public class MyAdapterMultiMedia extends FragmentPagerAdapter {
    Context context;
    int totalTabs;

    public MyAdapterMultiMedia(Context c, FragmentManager fm, int totalTabs) {
        super(fm);
        context = c;
        this.totalTabs = totalTabs;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("Id", "" + position);
        switch (position) {
            case 0:
                FragmentMultiMedia fragmentMultiMedia = new FragmentMultiMedia();
                fragmentMultiMedia.setArguments(bundle);
                return fragmentMultiMedia;
            case 1:
                FragmentMultiMedia fragmentMultiMedia2 = new FragmentMultiMedia();
                fragmentMultiMedia2.setArguments(bundle);
                return fragmentMultiMedia2;
            case 2:
                FragmentMultiMedia fragmentMultiMedia3 = new FragmentMultiMedia();
                fragmentMultiMedia3.setArguments(bundle);
                return fragmentMultiMedia3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
