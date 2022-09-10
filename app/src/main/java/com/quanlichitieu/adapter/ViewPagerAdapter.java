package com.quanlichitieu.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.quanlichitieu.fragment.FragmentAccount;
import com.quanlichitieu.fragment.FragmentAnalytic;
import com.quanlichitieu.fragment.FragmentHistory;
import com.quanlichitieu.fragment.FragmentHome;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new FragmentHome();
            case 1: return new FragmentHistory();
            case 2: return new FragmentAnalytic();
            case 3: return new FragmentAccount();
            default:return new FragmentHome();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
