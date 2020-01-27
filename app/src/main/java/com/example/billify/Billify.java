package com.example.billify;

import android.app.Application;

public class Billify extends Application
{
    String fb_flag;

    public String getFb_flag() {
        return fb_flag;
    }

    public void setFb_flag(String fb_flag) {
        this.fb_flag = fb_flag;
    }

    public Billify() {
    }
}
