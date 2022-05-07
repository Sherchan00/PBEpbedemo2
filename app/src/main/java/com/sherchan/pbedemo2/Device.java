package com.sherchan.pbedemo2;

public class Device {
    String deviceId;
    String fallStatus;
    String currentTime;
    String currentDate;
    String uid;


    public String getDeviceId() {
        return deviceId;
    }

    public String getFallStatus() {
        return fallStatus;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public String getUid() {
        return uid;
    }

    public Device() {

    }

    public Device(String deviceId, String fallStatus, String currentTime, String currentDate,  String uid) {
        this.deviceId = deviceId;
        this.fallStatus = fallStatus;
        this.currentTime = currentTime;
        this.currentDate = currentDate;
        this.uid = uid;
    }
}
