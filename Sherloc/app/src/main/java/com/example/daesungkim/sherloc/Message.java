package com.example.daesungkim.sherloc;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by daeseongkim on 2015. 12. 6..
 */
@ParseClassName("Message")
public class Message extends ParseObject {
    public String getSenderId() {
        return getString("SenderId");
    }

    public String getBody() {
        return getString("body");
    }

    public String getReceiverId() {
        return getString("ReceiverId");
    }

    public void setSenderId(String userId) {
        put("SenderId", userId);
    }

    public void setReceiverId(String userId) {
        put("ReceiverId", userId);
    }

    public void setBody(String body) {
        put("body", body);
    }
}