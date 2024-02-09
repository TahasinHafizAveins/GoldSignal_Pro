package com.example.goldsignalpro.model;

public class AppVersionModel {
    String latest_app_version;
    String app_name;
    String app_drive_link;

    public String getLatest_app_version() {
        return latest_app_version;
    }

    public void setLatest_app_version(String latest_app_version) {
        this.latest_app_version = latest_app_version;
    }

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public String getApp_drive_link() {
        return app_drive_link;
    }

    public void setApp_drive_link(String app_drive_link) {
        this.app_drive_link = app_drive_link;
    }

    @Override
    public String toString() {
        return "AppVersionModel{" +
                "latest_app_version='" + latest_app_version + '\'' +
                ", app_name='" + app_name + '\'' +
                ", app_drive_link='" + app_drive_link + '\'' +
                '}';
    }
}
