package com.sam.messageme;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;

/*
Sam Painter and Praveen Surenari
InClass8
 */
@ParseClassName("Message")
public class Message extends ParseObject{

    public Message() {

    }

    public ParseUser getAuthor() {
        return getParseUser("author");
    }

    public void setAuthor(ParseUser user) {
        put("author", user);
    }

    public Date getDate() {
        return getCreatedAt();
    }

    public String getMessage() {
        return getString("message");
    }

    public void setMessage(String message) {
        put("message", message);
    }


}
