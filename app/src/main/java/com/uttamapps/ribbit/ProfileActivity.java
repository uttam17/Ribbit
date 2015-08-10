package com.uttamapps.ribbit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.parse.ParseUser;

import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    //@Bind(R.id.setHometown)    TextView mSetHometown;
    private TextView mSetNickname;
    protected List<ParseUser> mUsers;
    private TextView mSetHometown;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
       // ButterKnife.bind(this);
        mSetHometown = (TextView)findViewById(R.id.setHometown);
        mSetNickname = (TextView)findViewById(R.id.setNickname);

        if(getIntent().getStringExtra("userNickname")!=null && getIntent().getStringExtra("userHometown")!=null){
        Intent intent = getIntent();
        String mNickname = intent.getStringExtra("userNickname");
        String mHometown = intent.getStringExtra("userHometown");
        mSetHometown.setText(mHometown);
        mSetNickname.setText(mNickname);

    }
        else if (getIntent().getStringExtra("userNickname")==null){
            mSetNickname.setText("Not Set");
        }
        else if (getIntent().getStringExtra("userHometown")==null){
            mSetHometown.setText("Not Set");
        }




}}
