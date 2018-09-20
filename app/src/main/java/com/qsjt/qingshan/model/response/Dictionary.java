package com.qsjt.qingshan.model.response;

import java.io.Serializable;

public class Dictionary implements Serializable {

    /**
     * recordId : 1
     * name : 车辆类型
     * keyText : 1
     * valueText : 小货车
     * description : 小货车（1.5吨/2.7*1.5*1.7m/6.9m³）
     * cityName : 北京市
     * startPrice : 53
     * unitPrice : 3
     * startDistance : 5
     */

    private String recordId;
    private String name;
    private String keyText;
    private String valueText;
    private String description;
    private String cityName;
    private double startPrice;
    private double unitPrice;
    private double startDistance;

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKeyText() {
        return keyText;
    }

    public void setKeyText(String keyText) {
        this.keyText = keyText;
    }

    public String getValueText() {
        return valueText;
    }

    public void setValueText(String valueText) {
        this.valueText = valueText;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public double getStartPrice() {
        return startPrice;
    }

    public void setStartPrice(double startPrice) {
        this.startPrice = startPrice;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getStartDistance() {
        return startDistance;
    }

    public void setStartDistance(double startDistance) {
        this.startDistance = startDistance;
    }
}
