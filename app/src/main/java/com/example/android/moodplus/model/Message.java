package com.example.android.moodplus.model;

public class Message {

    private String mText;
    private boolean mIsSentByUser;
    public Message(String text, boolean isSentByUser) {
        mText = text;
        mIsSentByUser = isSentByUser;
    }
    public String getText() {
        return mText;
    }
    public boolean isSentByUser() {
        return mIsSentByUser;
    }
}
