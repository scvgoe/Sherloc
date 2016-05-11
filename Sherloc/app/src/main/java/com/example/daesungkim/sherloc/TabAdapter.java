package com.example.daesungkim.sherloc;



import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by daeseongkim on 2015. 11. 29..
 */
public class TabAdapter extends FragmentPagerAdapter{

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // Top Rated fragment activity
                return new ObjectListFragment();
            case 1:
                // Movies fragment activity
                return new KeyFinderFragment();
//            case 2:
//                return new TestFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 2;
    }
}
