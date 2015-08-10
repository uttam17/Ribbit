package com.uttamapps.ribbit;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

/**
 * Created by Uttam Kumaran on 5/11/15.
 */
public class EditFriendsFragment extends ListFragment {

    public static final String TAG = EditFriendsFragment.class.getSimpleName();


    protected List<ParseUser> mUsers;
    protected ParseRelation<ParseUser> mFriendsRelation; //ParseRelation objects are always associated with a parse object
    protected ParseUser mCurrentUser;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_friends, container, false);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mCurrentUser = ParseUser.getCurrentUser();
        mFriendsRelation = mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATION);


        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.orderByAscending(ParseConstants.KEY_USERNAME);
        query.setLimit(1000);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {
                if (e == null) {
                    //Success
                    mUsers = users;
                    String[] usernames = new String[mUsers.size()];
                    int i = 0;
                    for (ParseUser user : mUsers) {
                        usernames[i] = user.getUsername();

                        i++;
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            getListView().getContext(), android.R.layout.simple_list_item_checked,
                            usernames);
                    setListAdapter(adapter);
                    getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

                    addFriendCheckMarks();

                } else {
                    Log.e(TAG, e.getMessage());
                    AlertDialog.Builder builder = new AlertDialog.Builder(getListView().getContext());
                    builder.setMessage(e.getMessage())
                            .setTitle(R.string.error_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }

    private void addFriendCheckMarks() {
        mFriendsRelation.getQuery().findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> friends, ParseException e) {
                if(e==null){
                    //Success. List was returned - look for match
                    for(int i=0; i<mUsers.size(); i++){ //i is position and mUsers is the list
                       ParseUser user =  mUsers.get(i);
                        for(ParseUser friend : friends){
                            if(friend.getObjectId().equals(user.getObjectId())){
                                getListView().setItemChecked(i, true);
                            }
                        }

                    }
                }
                else{
                    Log.e(TAG, e.getMessage());
                }
            }
        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        if(getListView().isItemChecked(position)){ //if checked
            //add friend
            mFriendsRelation.add(mUsers.get(position));
        }
        else{
            //remove friend
            mFriendsRelation.remove(mUsers.get(position)); //if check is there and it gets removed

        }

        mCurrentUser.saveInBackground(new SaveCallback() { //saves everything
            @Override
            public void done(ParseException e) {
                if(e!=null){
                    Log.e(TAG, e.getMessage());
                }
            }
        });

    }
}