package com.qsjt.qingshan.model.response;

public class Order {

    /**
     * orderId : d7341ffe-501a-4047-ae10-fc8f5953383e
     * status : 0
     * type : 1
     * appointStatus :
     * invoiceId :
     * createManId : a5351b13-722b-4ede-95eb-e5f3db8c6dd9
     * driverId :
     * linkMan : 男生女生
     * linkPhone : 17601031100
     * receiveMan : 时间简史
     * receivePhone : 15817469586
     * carType : 1
     * carTypeName : 小货车
     * note :
     * sendCity : 北京市
     * sendAddress : 曙光花园智业园-A座
     * sendDetailAddress : 北京市海淀区曙光花园中路
     * sendLatitude : 39.942790876341334
     * sendLongitude : 116.29604365958555
     * receiveAddress : 你是海淀区统计培训学校
     * receiveDetailAddress : 北京市海淀区昆明湖南路13号附近
     * receiveLatitude : 39.968074
     * receiveLongitude : 116.280627
     * price : 53
     * servicePrice : 1.5899999999999999
     * distance : 5
     * masterSiteWithdrawStatus : 0
     * masterSiteWithdrawId :
     * driverSiteWithdrawStatus : 0
     * driverSiteWithdrawId :
     * freightFeePayType : 3
     * freightFeePayStatus : 0
     * freightFeePayId :
     * serviceFeePayType :
     * serviceFeePayStatus :
     * serviceFeePayId :
     * commentContent :
     * commentStar :
     * driverCommentContent :
     * driverCommentStar :
     * siteComplaintId :
     * driverComplaintId :
     * createTime : 1537843935000
     * updateTime : 1537843935000
     * appointTime :
     * finishTime :
     * timeOut :
     * phoneNumber :
     * nickname :
     * personImageUrl :
     * nowLatitude :
     * nowLongitude :
     * plateNumber :
     * score :
     */

    private String orderId;
    private String status;
    private String type;
    private String appointStatus;
    private String invoiceId;
    private String createManId;
    private String driverId;
    private String linkMan;
    private String linkPhone;
    private String receiveMan;
    private String receivePhone;
    private String carType;
    private String carTypeName;
    private String note;
    private String sendCity;
    private String sendAddress;
    private String sendDetailAddress;
    private double sendLatitude;
    private double sendLongitude;
    private String receiveAddress;
    private String receiveDetailAddress;
    private double receiveLatitude;
    private double receiveLongitude;
    private double price;
    private double servicePrice;
    private double distance;
    private String masterSiteWithdrawStatus;
    private String masterSiteWithdrawId;
    private String driverSiteWithdrawStatus;
    private String driverSiteWithdrawId;
    private String freightFeePayType;
    private String freightFeePayStatus;
    private String freightFeePayId;
    private String serviceFeePayType;
    private String serviceFeePayStatus;
    private String serviceFeePayId;
    private String commentContent;
    private double commentStar;
    private String driverCommentContent;
    private double driverCommentStar;
    private String siteComplaintId;
    private String driverComplaintId;
    private long createTime;
    private long updateTime;
    private long appointTime;
    private long finishTime;
    private String timeOut;
    private String phoneNumber;
    private String nickname;
    private String personImageUrl;
    private double nowLatitude;
    private double nowLongitude;
    private String plateNumber;
    private float score;

    public String getStatusStr() {
        String status = "--";
        switch (getStatus()) {
            case "0":
                status = "等待接单";
                break;
            case "1":
                status = "已被抢单";
                break;
            case "2":
                status = "已被接单";
                break;
            case "3":
                status = "已发货";
                break;
            case "4":
                status = "已完成";
                break;
            case "9":
                status = "已取消";
                break;
        }
        return status;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAppointStatus() {
        return appointStatus;
    }

    public void setAppointStatus(String appointStatus) {
        this.appointStatus = appointStatus;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getCreateManId() {
        return createManId;
    }

    public void setCreateManId(String createManId) {
        this.createManId = createManId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getLinkMan() {
        return linkMan;
    }

    public void setLinkMan(String linkMan) {
        this.linkMan = linkMan;
    }

    public String getLinkPhone() {
        return linkPhone;
    }

    public void setLinkPhone(String linkPhone) {
        this.linkPhone = linkPhone;
    }

    public String getReceiveMan() {
        return receiveMan;
    }

    public void setReceiveMan(String receiveMan) {
        this.receiveMan = receiveMan;
    }

    public String getReceivePhone() {
        return receivePhone;
    }

    public void setReceivePhone(String receivePhone) {
        this.receivePhone = receivePhone;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getCarTypeName() {
        return carTypeName;
    }

    public void setCarTypeName(String carTypeName) {
        this.carTypeName = carTypeName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getSendCity() {
        return sendCity;
    }

    public void setSendCity(String sendCity) {
        this.sendCity = sendCity;
    }

    public String getSendAddress() {
        return sendAddress;
    }

    public void setSendAddress(String sendAddress) {
        this.sendAddress = sendAddress;
    }

    public String getSendDetailAddress() {
        return sendDetailAddress;
    }

    public void setSendDetailAddress(String sendDetailAddress) {
        this.sendDetailAddress = sendDetailAddress;
    }

    public double getSendLatitude() {
        return sendLatitude;
    }

    public void setSendLatitude(double sendLatitude) {
        this.sendLatitude = sendLatitude;
    }

    public double getSendLongitude() {
        return sendLongitude;
    }

    public void setSendLongitude(double sendLongitude) {
        this.sendLongitude = sendLongitude;
    }

    public String getReceiveAddress() {
        return receiveAddress;
    }

    public void setReceiveAddress(String receiveAddress) {
        this.receiveAddress = receiveAddress;
    }

    public String getReceiveDetailAddress() {
        return receiveDetailAddress;
    }

    public void setReceiveDetailAddress(String receiveDetailAddress) {
        this.receiveDetailAddress = receiveDetailAddress;
    }

    public double getReceiveLatitude() {
        return receiveLatitude;
    }

    public void setReceiveLatitude(double receiveLatitude) {
        this.receiveLatitude = receiveLatitude;
    }

    public double getReceiveLongitude() {
        return receiveLongitude;
    }

    public void setReceiveLongitude(double receiveLongitude) {
        this.receiveLongitude = receiveLongitude;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(double servicePrice) {
        this.servicePrice = servicePrice;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getMasterSiteWithdrawStatus() {
        return masterSiteWithdrawStatus;
    }

    public void setMasterSiteWithdrawStatus(String masterSiteWithdrawStatus) {
        this.masterSiteWithdrawStatus = masterSiteWithdrawStatus;
    }

    public String getMasterSiteWithdrawId() {
        return masterSiteWithdrawId;
    }

    public void setMasterSiteWithdrawId(String masterSiteWithdrawId) {
        this.masterSiteWithdrawId = masterSiteWithdrawId;
    }

    public String getDriverSiteWithdrawStatus() {
        return driverSiteWithdrawStatus;
    }

    public void setDriverSiteWithdrawStatus(String driverSiteWithdrawStatus) {
        this.driverSiteWithdrawStatus = driverSiteWithdrawStatus;
    }

    public String getDriverSiteWithdrawId() {
        return driverSiteWithdrawId;
    }

    public void setDriverSiteWithdrawId(String driverSiteWithdrawId) {
        this.driverSiteWithdrawId = driverSiteWithdrawId;
    }

    public String getFreightFeePayType() {
        return freightFeePayType;
    }

    public void setFreightFeePayType(String freightFeePayType) {
        this.freightFeePayType = freightFeePayType;
    }

    public String getFreightFeePayStatus() {
        return freightFeePayStatus;
    }

    public void setFreightFeePayStatus(String freightFeePayStatus) {
        this.freightFeePayStatus = freightFeePayStatus;
    }

    public String getFreightFeePayId() {
        return freightFeePayId;
    }

    public void setFreightFeePayId(String freightFeePayId) {
        this.freightFeePayId = freightFeePayId;
    }

    public String getServiceFeePayType() {
        return serviceFeePayType;
    }

    public void setServiceFeePayType(String serviceFeePayType) {
        this.serviceFeePayType = serviceFeePayType;
    }

    public String getServiceFeePayStatus() {
        return serviceFeePayStatus;
    }

    public void setServiceFeePayStatus(String serviceFeePayStatus) {
        this.serviceFeePayStatus = serviceFeePayStatus;
    }

    public String getServiceFeePayId() {
        return serviceFeePayId;
    }

    public void setServiceFeePayId(String serviceFeePayId) {
        this.serviceFeePayId = serviceFeePayId;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public double getCommentStar() {
        return commentStar;
    }

    public void setCommentStar(double commentStar) {
        this.commentStar = commentStar;
    }

    public String getDriverCommentContent() {
        return driverCommentContent;
    }

    public void setDriverCommentContent(String driverCommentContent) {
        this.driverCommentContent = driverCommentContent;
    }

    public double getDriverCommentStar() {
        return driverCommentStar;
    }

    public void setDriverCommentStar(double driverCommentStar) {
        this.driverCommentStar = driverCommentStar;
    }

    public String getSiteComplaintId() {
        return siteComplaintId;
    }

    public void setSiteComplaintId(String siteComplaintId) {
        this.siteComplaintId = siteComplaintId;
    }

    public String getDriverComplaintId() {
        return driverComplaintId;
    }

    public void setDriverComplaintId(String driverComplaintId) {
        this.driverComplaintId = driverComplaintId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public long getAppointTime() {
        return appointTime;
    }

    public void setAppointTime(long appointTime) {
        this.appointTime = appointTime;
    }

    public long getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(long finishTime) {
        this.finishTime = finishTime;
    }

    public String getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(String timeOut) {
        this.timeOut = timeOut;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPersonImageUrl() {
        return personImageUrl;
    }

    public void setPersonImageUrl(String personImageUrl) {
        this.personImageUrl = personImageUrl;
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

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }
}
