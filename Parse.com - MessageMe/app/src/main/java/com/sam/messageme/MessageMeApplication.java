package com.sam.messageme;

import com.parse.Parse;
import com.parse.ParseObject;

/*
Sam Painter and Praveen Surenari
InClass8
 */
public class MessageMeApplication extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);
        ParseObject.registerSubclass(Message.class);
        Parse.initialize(this, "0RANFdPp3In6WYS0DAUAJ1SeINrLYFPTThmu2zuh", "BgCUKl3hzujDtdmDUiQFcSxs9k9F1bM8zN9ryv57");
    }
}
