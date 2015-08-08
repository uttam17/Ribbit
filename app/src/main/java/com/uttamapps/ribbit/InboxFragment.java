package com.uttamapps.ribbit;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Uttam Kumaran on 8/7/2015.
 */
public class InboxFragment extends ListFragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inbox, container, false); //Like setContentView. Layout inflaters are used to create views
        //first parameter is layout id, second is where it is displayed, third should always be false
        return rootView;
    }

}
