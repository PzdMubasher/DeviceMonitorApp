package com.pzd.DeviceMonitorApp;

public class Model
{
    String deviceIMEI, internetConnected, batteryCharging, battery, location, timeStamp;


    public Model(String deviceIMEI, String internetConnected, String batteryCharging, String battery, String location, String timeStamp) {
        this.deviceIMEI = deviceIMEI;
        this.internetConnected = internetConnected;
        this.batteryCharging = batteryCharging;
        this.battery = battery;
        this.location = location;
        this.timeStamp = timeStamp;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getDeviceIMEI() {
        return deviceIMEI;
    }

    public void setDeviceIMEI(String deviceIMEI) {
        this.deviceIMEI = deviceIMEI;
    }

    public String getInternetConnected() {
        return internetConnected;
    }

    public void setInternetConnected(String internetConnected) {
        this.internetConnected = internetConnected;
    }

    public String getBatteryCharging() {
        return batteryCharging;
    }

    public void setBatteryCharging(String batteryCharging) {
        this.batteryCharging = batteryCharging;
    }

    public String getBattery() {
        return battery;
    }

    public void setBattery(String battery) {
        this.battery = battery;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
