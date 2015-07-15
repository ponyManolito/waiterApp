package com.app.waiter.tabswipe.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.app.waiter.R;
import com.app.waiter.tabswipe.fragment.CheckOrderTabFragment;
import com.app.waiter.tabswipe.fragment.DayMenuTabFragment;
import com.app.waiter.tabswipe.fragment.HomeTabFragment;
import com.app.waiter.tabswipe.fragment.MenuTabFragment;
import com.app.waiter.tabswipe.fragment.SwipeTabFragment;

/**
 * Created by javier.gomez on 05/05/2015.
 */
public class TabPagerAdapter extends FragmentStatePagerAdapter {
    private HomeTabFragment homeTabFragment;
    private MenuTabFragment menuTabFragment;
    private DayMenuTabFragment dayMenuTabFragment;
    private CheckOrderTabFragment checkOrderTabFragment;

    public TabPagerAdapter(FragmentManager fm) {
        super(fm);
        homeTabFragment = new HomeTabFragment();
        dayMenuTabFragment = new DayMenuTabFragment();
        menuTabFragment = new MenuTabFragment();
        checkOrderTabFragment = new CheckOrderTabFragment();
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        String tab = "";
        int colorResId = 0;
        switch (position) {
            case 0:
                return homeTabFragment;
            case 1:
                return dayMenuTabFragment;
            case 2:
                return menuTabFragment;
            case 3:
                return checkOrderTabFragment;
            case 4:
                tab = "Contact";
                colorResId = R.color.color5;
                break;
        }
        bundle.putString("tab",tab);
        bundle.putInt("color", colorResId);
        SwipeTabFragment swipeTabFragment = new SwipeTabFragment();
        swipeTabFragment.setArguments(bundle);
        return swipeTabFragment;
    }

    @Override
    public int getCount() {
        // Number of tabs
        return 5;
    }

}
