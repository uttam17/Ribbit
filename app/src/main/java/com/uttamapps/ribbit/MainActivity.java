package com.uttamapps.ribbit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.parse.ParseAnalytics;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements ActionBar.TabListener{

    public static final String TAG = MainActivity.class.getSimpleName();

    public static final int TAKE_PHOTO_REQUEST = 0;
    public static final int TAKE_VIDEO_REQUEST = 1;
    public static final int PICK_PHOTO_REQUEST = 2;
    public static final int PICK_VIDEO_REQUEST = 3;

    public static final int MEDIA_TYPE_IMAGE = 4;
    public static final int MEDIA_TYPE_VIDEO = 5;

    public static final int FILE_SIZE_LIMIT = 1024*1024*10; // 10mb

    protected Uri mMediaUri; //used to identify local file resources. Path to photo
    protected List<ParseObject> mMessages;


    protected DialogInterface.OnClickListener mDialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) { //which is what number is clicked on

    switch (which){
    case 0: //take picture
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mMediaUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // reference http://developer.android.com/guide/topics/media/camera.html#saving-media

        if(mMediaUri==null){
            //display error if uri is not available
            Toast.makeText(MainActivity.this, R.string.error_external_storage, Toast.LENGTH_LONG).show();
        }
        else {
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
            startActivityForResult(takePhotoIntent, TAKE_PHOTO_REQUEST);
        }
        break;
    case 1: //take video

        Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        mMediaUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
        if(mMediaUri==null){
            //display error if uri is not available
            Toast.makeText(MainActivity.this, R.string.error_external_storage, Toast.LENGTH_LONG).show();
        }
        else {
            videoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
            videoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10); // 10 second limit
            videoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0); // http://developer.android.com/reference/android/provider/MediaStore.html#EXTRA_VIDEO_QUALITY
            startActivityForResult(videoIntent, TAKE_VIDEO_REQUEST);
        }
        break;
    case 2: //choose picture
        Intent choosePhotoIntent = new Intent(Intent.ACTION_GET_CONTENT); // ACTION_GET_CONTENT is for all media so we limit it on next line
        choosePhotoIntent.setType("image/*");

        startActivityForResult(choosePhotoIntent, PICK_PHOTO_REQUEST);

        break;
    case 3: //choose video
        Intent chooseVideoIntent = new Intent(Intent.ACTION_GET_CONTENT);
        chooseVideoIntent.setType("video/*");
        Toast.makeText(MainActivity.this, R.string.video_file_size_warning, Toast.LENGTH_LONG).show();
        startActivityForResult(chooseVideoIntent, PICK_VIDEO_REQUEST);
        break;
}
        }
    };

    private Uri getOutputMediaFileUri(int mediaType) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        if (isExternalStorageAvailable()) {
            //get the uri

            // 1. Get the external storage directory
            String appName = MainActivity.this.getString(R.string.app_name); //created automatically by android studio
            File mediaStorageDir = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), appName);

            // 2. Create our subdirectory
            if(! mediaStorageDir.exists()){ //if the directory doesn't exist
               if(! mediaStorageDir.mkdirs()){ //if the directory fails to be created
                   Log.e(TAG, "Failed to create directory");
                   return null;
               }
            }

            // 3. Create a file name



            // 4. Create the file
            File mediaFile;
            Date now = new Date();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(now);
            String path = mediaStorageDir.getPath() + File.separator; // separator gives default separator of os
            if(mediaType == MEDIA_TYPE_IMAGE){
                mediaFile = new File(path + "IMG_" + timeStamp + ".jpg");
            }
            else if(mediaType == MEDIA_TYPE_VIDEO){
                mediaFile = new File(path + "VID_" + timeStamp + ".mp4");
            }
            else{
                return null;
            }
            Log.d(TAG, "File: " + Uri.fromFile(mediaFile));
            // 5. Return the file's URI

            return Uri.fromFile(mediaFile);
        }
        else {
            return null;
        }
    }

    private boolean isExternalStorageAvailable(){
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_main);
        ParseAnalytics.trackAppOpenedInBackground(getIntent());

        ParseUser currentUser = ParseUser.getCurrentUser();
        if(currentUser==null) { //Only runs login if there is no current user

            navigateToLogin();
        }
        else{
            Log.i(TAG, currentUser.getUsername());

        }

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //data intent is Uri code from photo or video
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK){
            //add to gallery by broadcasting. Gallery and other apps can listen for broadcasts then take action

            if(requestCode == PICK_PHOTO_REQUEST || requestCode == PICK_VIDEO_REQUEST){ //If media is selected, it passes the uri
                if(data==null){
                    Toast.makeText(this, R.string.general_error, Toast.LENGTH_LONG).show();
                }
                else{
                    mMediaUri = data.getData(); //gets the Uri from the data intent
                }

                Log.i(TAG, "Media URI" + mMediaUri);

                if(requestCode==PICK_VIDEO_REQUEST){
                    //make sure video file is less than 10 mb
                    int fileSize = 0;

                    InputStream inputStream = null; //used to stream media file.
                    try {
                        inputStream = getContentResolver().openInputStream(mMediaUri);
                        fileSize = inputStream.available(); //stores the total number of bytes in the video
                    } catch (FileNotFoundException e) {
                        Toast.makeText(this, R.string.error_opening_file, Toast.LENGTH_LONG).show();
                        return;
                    }
                    catch (IOException e){
                        Toast.makeText(this, R.string.error_opening_file, Toast.LENGTH_LONG).show();
                        return;
                    }
                    finally { //Always gets executed
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            /* Intentionally blank */
                        }
                        if(fileSize >= FILE_SIZE_LIMIT){
                            Toast.makeText(this, R.string.file_size_error, Toast.LENGTH_LONG).show();
                            return; //Don't want to go any farther since file size is too large. Simply return;
                        }
                    }

                }
            }
            else{ //Used to grab the uri for the created image or video
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            mediaScanIntent.setData(mMediaUri);
            sendBroadcast(mediaScanIntent);
        }
            Intent recipientsIntent = new Intent(this, RecipientsActivity.class);
            recipientsIntent.setData(mMediaUri);
            String fileType;
            if(requestCode == PICK_PHOTO_REQUEST  || requestCode==TAKE_PHOTO_REQUEST){
                fileType = ParseConstants.TYPE_IMAGE;
            }
            else{
                fileType = ParseConstants.TYPE_VIDEO;
            }
            recipientsIntent.putExtra(ParseConstants.KEY_FILE_TYPE, fileType);
            startActivity(recipientsIntent);
        }
        else if(resultCode != RESULT_CANCELED){
            Toast.makeText(this, R.string.general_error, Toast.LENGTH_LONG).show();
        }

    }

    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);//Activity working in is the context. Need intents to start activities
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //Logging in should be a new task
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); //Delete the last task so you cant go back to MainActivity from LoginActivity
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case R.id.action_logout:
            ParseUser.logOut();
            navigateToLogin();
                break;
            case R.id.action_edit_friends:
            Intent intent = new Intent(this, EditFriendsActivity.class);
            startActivity(intent);
                break;

            case R.id.action_camera:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setItems(R.array.camera_choices, mDialogListener);
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }






}
