package com.sam.messagemefragment;

import com.parse.Parse;
import com.parse.ParseObject;

/*
Sam Painter and Praveen Surenani
InClass09
MessageMeFragmentApplication.java
 */
public class MessageMeFragmentApplication extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);
        ParseObject.registerSubclass(Message.class);
        Parse.initialize(this, "0RANFdPp3In6WYS0DAUAJ1SeINrLYFPTThmu2zuh", "BgCUKl3hzujDtdmDUiQFcSxs9k9F1bM8zN9ryv57");
    }
}
