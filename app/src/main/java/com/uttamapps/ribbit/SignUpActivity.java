package com.uttamapps.ribbit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.uttamapps.ribbit.alert_Fragments.SignUpExceptionAlertFragment;
import com.uttamapps.ribbit.alert_Fragments.UserDetailsAlertFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SignUpActivity extends AppCompatActivity {

    @Bind(R.id.usernameField) EditText mUsername;
    @Bind(R.id.passwordField) EditText mPassword;
    @Bind(R.id.emailField) EditText mEmail;
    @Bind(R.id.optionalHometown) EditText mHometown;
    @Bind(R.id.optionalNickname) EditText mNickname;
    @Bind(R.id.signUpButton)Button mSignUpButton;
    @Bind(R.id.progressBar)ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ButterKnife.bind(this);

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mUsername.getText().toString();
                String password = mPassword.getText().toString();
                String email = mEmail.getText().toString();
               String hometown = mHometown.getText().toString().trim();



                    String nickname = mNickname.getText().toString().trim();

                username = username.trim();
                password = password.trim();
                email = email.trim();


                if (username.isEmpty() || password.isEmpty() || email.isEmpty()){
                    UserDetailsAlertFragment userDialog = new UserDetailsAlertFragment();
                    userDialog.show(getFragmentManager(), "signup error dialog");
                }
                else{
                    //create new user
                    mProgressBar.setVisibility(View.VISIBLE);
                    ParseUser newUser = new ParseUser();
                    newUser.setUsername(username);
                    newUser.setPassword(password);
                    newUser.setEmail(email);
                    newUser.put("nickname",nickname);
                    newUser.put("hometown", hometown);
                    newUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            mProgressBar.setVisibility(View.INVISIBLE);
                            if(e==null){
                                //Success!!
                                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                            else{
                                SignUpExceptionAlertFragment parseExcAlert = new SignUpExceptionAlertFragment();
                                Bundle args = new Bundle();
                                args.putString("exception", e.getMessage());
                                parseExcAlert.setArguments(args);
                                parseExcAlert.show(getFragmentManager(), "parse exception dialog");
                            }
                        }
                    });
                }
            }
        });

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
