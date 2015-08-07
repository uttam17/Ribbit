package com.uttamapps.ribbit;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by Uttam Kumaran on 8/6/2015.
 */
public class RibbitApplication extends Application {
    public void onCreate(){
        super.onCreate();

        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "cgdOfNHEj2TvMAuVZ9RLAiZJEhGE2MTIabUyFOpy", "mfGQMMd7UOGAfQTOGai13jWFfitrxskQIaH8czuv");


    }
}
