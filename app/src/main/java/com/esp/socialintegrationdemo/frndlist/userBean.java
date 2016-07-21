package com.esp.socialintegrationdemo.frndlist;

/**
 * Created by admin on 25/4/16.
 */
public class userBean {
    public String fb_avtar;
    public String name;

    public userBean(String fb_avtar, String name) {
        this.fb_avtar = fb_avtar;
        this.name = name;
    }

    public String getFb_avtar() {
        return fb_avtar;
    }

    public void setFb_avtar(String fb_avtar) {
        this.fb_avtar = fb_avtar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
