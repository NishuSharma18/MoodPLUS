package com.example.android.moodplus.model;

public class Community {

    private String CommName;
    private String CommImg;
    private String CommDesc;

    public Community() {
    }

    public Community(String commName, String commImg, String commDesc) {
        CommName = commName;
        CommImg = commImg;
        CommDesc = commDesc;
    }

    public String getCommName() {
        return CommName;
    }

    public void setCommName(String commName) {
        CommName = commName;
    }

    public String getCommImg() {
        return CommImg;
    }

    public void setCommImg(String commImg) {
        CommImg = commImg;
    }

    public String getCommDesc() {
        return CommDesc;
    }

    public void setCommDesc(String commDesc) {
        CommDesc = commDesc;
    }
}

