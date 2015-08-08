package com.uttamapps.ribbit;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Locale;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    // In the lesson, we factor this code out of MainActivity.class, and thus must add the context
    // as a member variable that gets passed in through the constructor. Nothing about this
    // will change from the video, no deprecation involved, no Android Studio conflicts.

    protected Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch(position){
            case 0:
                return new InboxFragment();
            case 1:
                return new FriendsFragment();
        }
        return new InboxFragment();
    }

    @Override
    public int getCount() {
        // In the video we reduce this boilerplate value from 3
        // to 2 tabs, one for Inbox and one for Friends. Again, no
        // change needed from the video, follow along.
        // Show 2 total pages.
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        // this is where we'll pass the context to variables that are now
        // outside the scope of MainActivity.class after the refactor.
        // again, nothing different than the video, follow the video.
        // Remember to delete the third case in the boilerplate code
        // and change the strings resources entries to Inbox and
        // Friends, respectively, just like in the video.
        switch (position) {
            case 0:
                return mContext.getString(R.string.title_section1).toUpperCase(l);
            case 1:
                return mContext.getString(R.string.title_section2).toUpperCase(l);
        }
        return null;
    }
}
