package com.uttamapps.ribbit;

import android.app.AlertDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class RecipientsActivity extends AppCompatActivity {

    private static final String TAG = RecipientsActivity.class.getSimpleName();
    protected List<ParseUser> mFriends;
    protected ParseRelation<ParseUser> mFriendsRelation; //ParseRelation objects are always associated with a parse object
    protected ParseUser mCurrentUser;
    protected MenuItem mSendMenuItem;
    protected ListView recipientsListView;
    protected Uri mMediaUri;
    protected String mFileType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipients);


        recipientsListView = (ListView) findViewById(R.id.recipientsListView);
        recipientsListView.setEmptyView(findViewById(R.id.emptyTextView));
        recipientsListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        recipientsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (recipientsListView.getCheckedItemCount() > 0) {
                    mSendMenuItem.setVisible(true);
                } else {
                    mSendMenuItem.setVisible(false);
                }
            }
        });

        mMediaUri = getIntent().getData();
        mFileType = getIntent().getExtras().getString(ParseConstants.KEY_FILE_TYPE);


    }



    @Override
    public void onResume() {
        super.onResume();
        mCurrentUser = ParseUser.getCurrentUser();
        mFriendsRelation = mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATION);

        ParseQuery<ParseUser> query = mFriendsRelation.getQuery();
        query.addAscendingOrder(ParseConstants.KEY_USERNAME);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> friends, ParseException e) {
                if (e == null) {
                    mFriends = friends;
                    String[] usernames = new String[mFriends.size()];
                    int i = 0;
                    for (ParseUser user : mFriends) {
                        usernames[i] = user.getUsername();
                        i++;
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(recipientsListView.getContext(), android.R.layout.simple_list_item_checked, usernames);
                    recipientsListView.setAdapter(adapter);
                } else {
                    Log.e(TAG, e.getMessage());
                    AlertDialog.Builder builder = new AlertDialog.Builder(RecipientsActivity.this);
                    builder.setMessage(e.getMessage())
                            .setTitle(R.string.error_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) { // when menu is created
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recipients, menu);
        mSendMenuItem = menu.getItem(0); // position zero since only one item
        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
                NavUtils.navigateUpFromSameTask(this);

            //noinspection SimplifiableIfStatement


            case R.id.action_send:
                ParseObject message = createMessage();
                if(message == null){
                    //error
                    AlertDialog.Builder builder = new AlertDialog.Builder(this)
                            .setTitle(R.string.general_error)
                            .setMessage(R.string.error_selecting_file)
                            .setPositiveButton(this.getString(R.string.ok), null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else {
                    send(message);
                    finish(); // returns the user to MainActivity right after the message is sent.
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void send(ParseObject message) {
        message.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null){
                    //success
                    Toast.makeText(RecipientsActivity.this, R.string.success_message, Toast.LENGTH_LONG).show();
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(RecipientsActivity.this)
                            .setTitle(R.string.general_error)
                            .setMessage(R.string.error_sending_message)
                            .setPositiveButton(RecipientsActivity.this.getString(R.string.ok), null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }

    /*@Override
    protected void onListItemClick(ListView l, View v, int position, long id) { // when list item is clicked
        super.onListItemClick(l, v, position, id);

        if(l.getCheckedItemCount() > 0){
            mSendMenuItem.setVisible(true);
        }
        else{
            mSendMenuItem.setVisible(false);
        }

        mSendMenuItem.setVisible(true);
    }*/

    protected ParseObject createMessage(){
        ParseObject message = new ParseObject(ParseConstants.CLASS_MESSAGES); //class of messages
        message.put(ParseConstants.KEY_SENDER_ID, ParseUser.getCurrentUser().getObjectId()); // string key, object id
        message.put(ParseConstants.KEY_SENDER_NAME, ParseUser.getCurrentUser().getUsername());
        message.put(ParseConstants.KEY_RECIPIENTS_IDS, getRecipientsIds()); // calls method below
        message.put(ParseConstants.KEY_FILE_TYPE, mFileType);

        byte[] fileBytes = FileHelper.getByteArrayFromFile(this,mMediaUri);
        if(fileBytes == null){
            return null;
        }
        else{
            if(mFileType.equals(ParseConstants.TYPE_IMAGE)){
                fileBytes = FileHelper.reduceImageForUpload(fileBytes); // in order to reduce large image file size
            }

        String fileName = FileHelper.getFileName(this,mMediaUri, mFileType);
        ParseFile file = new ParseFile(fileName, fileBytes);
        message.put(ParseConstants.KEY_FILE, file);
        return message;
    }}

    protected ArrayList<String> getRecipientsIds(){
        ArrayList<String> recipientIds = new ArrayList<String>();

        for(int i=0; i<recipientsListView.getCount(); i++){  // object ids for selected recipients to the recipientdsIds arrayList
            if(recipientsListView.isItemChecked(i)){
                recipientIds.add(mFriends.get(i).getObjectId());
            }
        }

        return recipientIds;
    }
}
