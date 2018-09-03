package com.wiser.kids.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Location {


    private double longitude;
    private double latitude;
    private float course;
    private float speed;
    private long timestamp;
    private float haccuracy;

    @SerializedName("user_id")
    private String userId;
    @SerializedName("helper_id")
    private String helperId;

    public Location() {

    }

    public Location(double longitude, double latitude, float course, float speed, long timestamp,float haccuracy) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.course = course;
        this.speed = speed;
        this.timestamp = timestamp;
        this.haccuracy = haccuracy;

    }


    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public float getCourse() {
        return course;
    }

    public void setCourse(float course) {
        this.course = course;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public float getHAccuracy() {
        return haccuracy;
    }

    public void setHAccuracy(float haccuracy) {
        this.haccuracy = haccuracy;
    }

    public String getUserId() {
        return userId;
    }

    public String getHelperId() {
        return helperId;
    }
}
