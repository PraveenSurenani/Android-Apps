package com.sam.project;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.parse.ParseTwitterUtils;

/**
 * Created by sam on 12/12/15.
 */
public class Project extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this);
        ParseFacebookUtils.initialize(this);
        ParseTwitterUtils.initialize("2NGJp6gCWXMYJqIXzID8ka7MW", "C4Z10ejqYBEa15i6FuFCRDH4C86sia8QH0XiF9GkI3x8qMzo1i");
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}
