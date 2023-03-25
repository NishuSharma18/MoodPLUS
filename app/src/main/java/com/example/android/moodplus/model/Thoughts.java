package com.example.android.moodplus.model;


public class Thoughts {

    String thought;
    String time;

    public Thoughts() {
    }

    public Thoughts(String thought, String time) {
        this.thought = thought;
        this.time = time;
    }

    public String getThought() {
        return thought;
    }

    public void setThought(String thought) {
        this.thought = thought;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
