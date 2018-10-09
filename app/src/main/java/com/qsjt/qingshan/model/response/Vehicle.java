package com.qsjt.qingshan.model.response;

public class Vehicle {

    /**
     * userId : 3c4d76c9-f2a1-464f-aac8-aec788074f12
     * plateNumber : 京A0001
     * accessToken : 4260df53b4ca425da7e91398b3eb2575
     * loginPlate : iOS
     * nowLatitude : 39.976888
     * nowLongitude : 116.321768
     * distance : 4.402737689023457
     */

    private String userId;
    private String plateNumber;
    private String accessToken;
    private String loginPlate;
    private double nowLatitude;
    private double nowLongitude;
    private double distance;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getLoginPlate() {
        return loginPlate;
    }

    public void setLoginPlate(String loginPlate) {
        this.loginPlate = loginPlate;
    }

    public double getNowLatitude() {
        return nowLatitude;
    }

    public void setNowLatitude(double nowLatitude) {
        this.nowLatitude = nowLatitude;
    }

    public double getNowLongitude() {
        return nowLongitude;
    }

    public void setNowLongitude(double nowLongitude) {
        this.nowLongitude = nowLongitude;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
