package com.uttamapps.ribbit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.uttamapps.ribbit.alert_Fragments.LoginAlertFragment;
import com.uttamapps.ribbit.alert_Fragments.LoginExceptionAlertFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    @Bind(R.id.usernameField)    EditText mUsername; //New butterknife
    @Bind(R.id.passwordField) EditText mPassword;

    @Bind(R.id.loginButton)Button mLoginButton;
    @Bind(R.id.progressBar) ProgressBar mProgressBar;

    protected TextView mSignUpTextView; //Can be viewed by subclasses or classes within same package

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS); didn't work in new version of android studio

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mSignUpTextView = (TextView) findViewById(R.id.signUpText);
        mSignUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
        ButterKnife.bind(this);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mUsername.getText().toString();
                String password = mPassword.getText().toString();

                username = username.trim();
                password = password.trim();

                if (username.isEmpty() || password.isEmpty()){
                    LoginAlertFragment userDialog = new LoginAlertFragment();
                    userDialog.show(getFragmentManager(), "signup error dialog");
                }
                else{
                    //Logging in
                    //setProgressBarIndeterminateVisibility(true); didn't work in new version of android studio
                    mProgressBar.setVisibility(View.VISIBLE);
                    ParseUser.logInInBackground(username, password, new LogInCallback() {
                        @Override
                        public void done(ParseUser parseUser, ParseException e) {
                            //setProgressBarIndeterminateVisibility(false);//turns off progress bar
                            mProgressBar.setVisibility(View.INVISIBLE);
                            if (e==null){

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                        }
                        else{
                                LoginExceptionAlertFragment parseExcAlert = new LoginExceptionAlertFragment();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
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
