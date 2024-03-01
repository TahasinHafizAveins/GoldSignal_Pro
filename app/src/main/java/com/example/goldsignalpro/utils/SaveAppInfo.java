package com.example.goldsignalpro.utils;

public class SaveAppInfo {
    private static final SaveAppInfo instance = new SaveAppInfo();
    private String app_update_link;

    public static SaveAppInfo getInstance(){
        return instance;
    }
    public String getApp_update_link() {
        return app_update_link;
    }

    public void setApp_update_link(String app_update_link) {
        this.app_update_link = app_update_link;
    }
}
