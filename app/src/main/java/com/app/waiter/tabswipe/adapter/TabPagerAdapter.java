package com.app.waiter.tabswipe.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.app.waiter.R;
import com.app.waiter.tabswipe.fragment.SwipeTabFragment;

/**
 * Created by javier.gomez on 05/05/2015.
 */
public class TabPagerAdapter extends FragmentStatePagerAdapter {

    public TabPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        String tab = "";
        int colorResId = 0;
        switch (position) {
            case 0:
                // Home page activity
                tab = "Home";
                colorResId = R.color.color1;
                break;
                //return new HomePageFragment();
            case 1:
                // Day menu fragment activity
                tab = "Day Menu";
                colorResId = R.color.color2;
                break;
                //return new DayMenuFragment();
            case 2:
                // Menu fragment activity
                tab = "Menu";
                colorResId = R.color.color3;
                break;
                //return new MenuFragment();
            case 3:
                // Order fragment activity
                tab = "Order";
                colorResId = R.color.color4;
                break;
                //return new OrderFragment();
            case 4:
                // Contact fragment activity
                tab = "Contact";
                colorResId = R.color.color5;
                break;
                //return new ContactFragment();
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
