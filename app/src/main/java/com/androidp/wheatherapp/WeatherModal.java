package com.androidp.wheatherapp;

public class WeatherModal {
    private String Time;
    private String Temperature;
    private String icon;
    private String windSpeed;

    public WeatherModal(String time, String temperature, String icon, String windSpeed) {
        Time = time;
        Temperature = temperature;
        this.icon = icon;
        this.windSpeed = windSpeed;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getTemperature() {
        return Temperature;
    }

    public void setTemperature(String temperature) {
        Temperature = temperature;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }


}
